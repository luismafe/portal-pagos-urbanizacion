package com.enterprise.payment.messaging;

import com.enterprise.payment.config.RabbitMQConfig;
import com.enterprise.payment.model.Pago;
import com.enterprise.payment.repository.PagoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificacionPagoListener {

    private static final Logger log = LoggerFactory.getLogger(NotificacionPagoListener.class);
    private final PagoRepository pagoRepository;

    public NotificacionPagoListener(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICACIONES_PAGOS_QUEUE)
    public void recibirNotificacionPago(PagoConfirmadoEvent event) {
        log.info("Recibida notificación asíncrona de pago para factura {} (ref: {})",
                event.getNumeroFactura(), event.getReferenciaTransaccion());
        Pago pago = new Pago();
        pago.setNumeroFactura(event.getNumeroFactura());
        pago.setMontoPagado(event.getMontoPagado());
        pago.setMetodoPago("PORTAL_WEB");
        pago.setReferenciaTransaccion(event.getReferenciaTransaccion());
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado("APROBADO");
        pagoRepository.save(pago);
        log.info("Transacción de pago registrada exitosamente para factura {}", event.getNumeroFactura());
    }
}

