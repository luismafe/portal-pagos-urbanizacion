package com.enterprise.payment.controller;

import com.enterprise.payment.model.Pago;
import com.enterprise.payment.service.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    public List<Pago> listarPagos() {
        return pagoService.listarPagos();
    }

    @GetMapping("/factura/{numeroFactura}")
    public List<Pago> buscarPorFactura(@PathVariable String numeroFactura) {
        return pagoService.buscarPorFactura(numeroFactura);
    }

    @PostMapping
    public ResponseEntity<Pago> registrarPago(@RequestBody Pago pago) {
        Pago procesado = pagoService.procesarPago(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(procesado);
    }
}

