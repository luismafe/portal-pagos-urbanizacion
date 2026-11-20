package com.enterprise.payment.service;

import com.enterprise.payment.config.RabbitMQConfig;
import com.enterprise.payment.messaging.PagoConfirmadoEvent;
import com.enterprise.payment.model.Pago;
import com.enterprise.payment.repository.PagoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final RabbitTemplate rabbitTemplate;

    public PagoService(PagoRepository pagoRepository, RabbitTemplate rabbitTemplate) {
        this.pagoRepository = pagoRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }

    public List<Pago> buscarPorFactura(String numeroFactura) {
        return pagoRepository.findByNumeroFactura(numeroFactura);
    }

    @Transactional
    public Pago procesarPago(Pago pago) {
        pago.setFechaPago(LocalDateTime.now());
        if (pago.getReferenciaTransaccion() == null || pago.getReferenciaTransaccion().isBlank()) {
            pago.setReferenciaTransaccion("TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        pago.setEstado("APROBADO");

        Pago pagoGuardado = pagoRepository.save(pago);

        PagoConfirmadoEvent event = new PagoConfirmadoEvent(
                pagoGuardado.getNumeroFactura(),
                pagoGuardado.getMontoPagado(),
                pagoGuardado.getReferenciaTransaccion()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.PAGOS_QUEUE, event);

        return pagoGuardado;
    }
}

