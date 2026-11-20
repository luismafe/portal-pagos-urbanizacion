import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Factura } from '../../models/factura.model';
import { FacturaService } from '../../services/factura.service';

@Component({
    selector: 'app-portal-pagos',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './portal-pagos.component.html',
    styleUrl: './portal-pagos.component.css'
})
export class PortalPagosComponent implements OnInit {
    apartamentoBusqueda = 'Torre 3 - Apto 502';
    facturas: Factura[] = [];
    facturasPagadas: Factura[] = [];
    cargando = false;
    cargandoHistorial = false;
    procesandoPago = false;
    facturaSeleccionadaParaPago: Factura | null = null;
    mensajeEstado = '';
    errorMensaje = '';

    constructor(private facturaService: FacturaService) { }

    ngOnInit(): void {
        this.buscarFacturas();
    }

    buscarFacturas(): void {
        this.errorMensaje = '';
        this.mensajeEstado = '';
        const apartamento = this.apartamentoBusqueda.trim();
        if (!apartamento) {
            this.errorMensaje = 'Ingresa un apartamento para consultar tus facturas.';
            return;
        }

        this.cargando = true;
        this.facturaService.obtenerFacturasPendientes(apartamento).subscribe({
            next: (data) => {
                this.facturas = data;
                this.cargando = false;
                if (data.length === 0) {
                    this.mensajeEstado = 'No hay facturas pendientes para este apartamento.';
                }
            },
            error: () => {
                this.cargando = false;
                this.errorMensaje = 'No fue posible consultar las facturas. Verifica que el microservicio esté activo.';
            }
        });

        this.cargarHistorialPagadas(apartamento);
    }

    cargarHistorialPagadas(apartamento: string): void {
        this.cargandoHistorial = true;
        this.facturaService.obtenerFacturasPagadas(apartamento).subscribe({
            next: (pagadas) => {
                this.facturasPagadas = pagadas;
                this.cargandoHistorial = false;
            },
            error: () => {
                this.cargandoHistorial = false;
            }
        });
    }

    abrirModalPago(factura: Factura): void {
        this.errorMensaje = '';
        this.mensajeEstado = '';
        this.facturaSeleccionadaParaPago = factura;
    }

    cerrarModalPago(): void {
        if (!this.procesandoPago) {
            this.facturaSeleccionadaParaPago = null;
        }
    }

    confirmarPago(): void {
        if (!this.facturaSeleccionadaParaPago) {
            return;
        }
        const factura = this.facturaSeleccionadaParaPago;
        this.procesandoPago = true;
        this.errorMensaje = '';
        this.mensajeEstado = '';

        this.facturaService.pagarFactura(factura.numeroFactura).subscribe({
            next: (actualizada) => {
                this.procesandoPago = false;
                this.facturaSeleccionadaParaPago = null;
                this.mensajeEstado = `Factura ${actualizada.numeroFactura} pagada con éxito. Se ha enviado la notificación asíncrona.`;
                this.facturas = this.facturas.filter((f) => f.numeroFactura !== actualizada.numeroFactura);
                this.facturasPagadas = [actualizada, ...this.facturasPagadas];
            },
            error: () => {
                this.procesandoPago = false;
                this.facturaSeleccionadaParaPago = null;
                this.errorMensaje = `Error al procesar el pago de la factura ${factura.numeroFactura}.`;
            }
        });
    }
}
