package com.enterprise.billing.config;

import com.enterprise.billing.model.Factura;
import com.enterprise.billing.repository.FacturaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(FacturaRepository facturaRepository) {
        return args -> {
            if (facturaRepository.count() == 0) {
                Factura f0 = new Factura();
                f0.setNumeroFactura("FAC-2026-000");
                f0.setApartamento("Torre 3 - Apto 502");
                f0.setPropietario("Carlos Rodríguez");
                f0.setValor(new BigDecimal("385000"));
                f0.setFechaVencimiento(LocalDate.now().minusDays(20));
                f0.setEstado("PAGADA");
                facturaRepository.save(f0);

                Factura f1 = new Factura();
                f1.setNumeroFactura("FAC-2026-001");
                f1.setApartamento("Torre 3 - Apto 502");
                f1.setPropietario("Carlos Rodríguez");
                f1.setValor(new BigDecimal("385000"));
                f1.setFechaVencimiento(LocalDate.now().plusDays(10));
                f1.setEstado("PENDIENTE");
                facturaRepository.save(f1);

                Factura f2 = new Factura();
                f2.setNumeroFactura("FAC-2026-002");
                f2.setApartamento("Torre 3 - Apto 502");
                f2.setPropietario("Carlos Rodríguez");
                f2.setValor(new BigDecimal("120000"));
                f2.setFechaVencimiento(LocalDate.now().plusDays(15));
                f2.setEstado("PENDIENTE");
                facturaRepository.save(f2);
            }
        };
    }
}

