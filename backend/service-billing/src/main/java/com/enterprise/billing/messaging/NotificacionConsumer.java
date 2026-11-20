package com.enterprise.billing.messaging;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacionConsumer {

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "notificaciones.pagos.queue", durable = "true"), exchange = @Exchange(value = "urbanizacion.exchange", type = "topic", durable = "true"), key = "notificaciones.routingKey"))
    public void procesarNotificacionPago(String mensaje) {
        System.out.println(">>> [Worker Asíncrono RabbitMQ] Evento recibido: " + mensaje);
        // Aquí puedes integrar el envío de correo con recibo de pago,
        // generación de PDF o registro de auditoría sin bloquear al usuario.
    }
}