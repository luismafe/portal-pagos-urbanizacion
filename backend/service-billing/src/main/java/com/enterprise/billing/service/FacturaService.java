package com.enterprise.billing.service;

import com.enterprise.billing.model.Factura;
import com.enterprise.billing.repository.FacturaRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "urbanizacion.exchange";
    private static final String ROUTING_KEY = "notificaciones.routingKey";

    public FacturaService(FacturaRepository facturaRepository, RabbitTemplate rabbitTemplate) {
        this.facturaRepository = facturaRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Factura> listarTodas() {
        return facturaRepository.findAll();
    }

    public Optional<Factura> obtenerPorNumero(String numeroFactura) {
        return facturaRepository.findByNumeroFactura(numeroFactura);
    }

    public List<Factura> obtenerFacturasPorApartamento(String apartamento) {
        return facturaRepository.findByApartamentoAndEstado(apartamento, "PENDIENTE");
    }

    public List<Factura> obtenerFacturasPagadasPorApartamento(String apartamento) {
        return facturaRepository.findByApartamentoAndEstado(apartamento, "PAGADA");
    }

    public List<Factura> buscarPendientesPorApartamento(String apartamento) {
        return obtenerFacturasPorApartamento(apartamento);
    }

    @Transactional
    public Factura crearFactura(Factura factura) {
        if (factura.getEstado() == null || factura.getEstado().isBlank()) {
            factura.setEstado("PENDIENTE");
        }
        return facturaRepository.save(factura);
    }

    @Transactional
    public Factura procesarPagoFactura(String numeroFactura) {
        Factura factura = facturaRepository.findByNumeroFactura(numeroFactura)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada: " + numeroFactura));

        if ("PAGADA".equalsIgnoreCase(factura.getEstado())) {
            throw new IllegalStateException("La factura ya se encuentra pagada");
        }

        factura.setEstado("PAGADA");
        Factura facturaActualizada = facturaRepository.save(factura);

        String mensajeEvento = String.format(
                "Pago confirmado para la factura %s del apartamento %s por valor de $%s",
                facturaActualizada.getNumeroFactura(),
                facturaActualizada.getApartamento(),
                facturaActualizada.getValor()
        );

        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, mensajeEvento);
        } catch (Exception ignored) {
            // Si RabbitMQ no está disponible temporalmente, se mantiene el pago confirmado en BD
        }

        return facturaActualizada;
    }

    @Transactional
    public Factura marcarComoPagada(String numeroFactura) {
        return procesarPagoFactura(numeroFactura);
    }
}
