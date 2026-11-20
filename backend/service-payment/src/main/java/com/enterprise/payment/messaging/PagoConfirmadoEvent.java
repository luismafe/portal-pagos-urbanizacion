package com.enterprise.payment.messaging;

import java.math.BigDecimal;

public class PagoConfirmadoEvent {
    private String numeroFactura;
    private BigDecimal montoPagado;
    private String referenciaTransaccion;

    public PagoConfirmadoEvent() {
    }

    public PagoConfirmadoEvent(String numeroFactura, BigDecimal montoPagado, String referenciaTransaccion) {
        this.numeroFactura = numeroFactura;
        this.montoPagado = montoPagado;
        this.referenciaTransaccion = referenciaTransaccion;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public BigDecimal getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(BigDecimal montoPagado) {
        this.montoPagado = montoPagado;
    }

    public String getReferenciaTransaccion() {
        return referenciaTransaccion;
    }

    public void setReferenciaTransaccion(String referenciaTransaccion) {
        this.referenciaTransaccion = referenciaTransaccion;
    }
}

