package com.enterprise.billing.controller;

import com.enterprise.billing.model.Factura;
import com.enterprise.billing.service.FacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@CrossOrigin(origins = "*") // Permite la comunicación inicial con Angular
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping("/apartamento/{apartamento}")
    public ResponseEntity<List<Factura>> obtenerDeudas(@PathVariable String apartamento) {
        List<Factura> deudas = facturaService.obtenerFacturasPorApartamento(apartamento);
        return ResponseEntity.ok(deudas);
    }

    @GetMapping("/apartamento/{apartamento}/pagadas")
    public ResponseEntity<List<Factura>> obtenerHistorialPagadas(@PathVariable String apartamento) {
        List<Factura> pagadas = facturaService.obtenerFacturasPagadasPorApartamento(apartamento);
        return ResponseEntity.ok(pagadas);
    }

    @PostMapping("/pagar/{numeroFactura}")
    public ResponseEntity<Factura> pagarFactura(@PathVariable String numeroFactura) {
        Factura facturaPagada = facturaService.procesarPagoFactura(numeroFactura);
        return ResponseEntity.ok(facturaPagada);
    }
}