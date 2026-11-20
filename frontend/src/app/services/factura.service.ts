import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Factura } from '../models/factura.model';

@Injectable({
    providedIn: 'root'
})
export class FacturaService {
    private apiUrl = 'http://localhost:8081/api/facturas';

    constructor(private http: HttpClient) { }

    obtenerFacturasPendientes(apartamento: string): Observable<Factura[]> {
        return this.http.get<Factura[]>(`${this.apiUrl}/apartamento/${encodeURIComponent(apartamento)}`);
    }

    obtenerFacturasPagadas(apartamento: string): Observable<Factura[]> {
        return this.http.get<Factura[]>(`${this.apiUrl}/apartamento/${encodeURIComponent(apartamento)}/pagadas`);
    }

    pagarFactura(numeroFactura: string): Observable<Factura> {
        return this.http.post<Factura>(`${this.apiUrl}/pagar/${encodeURIComponent(numeroFactura)}`, {});
    }
}
