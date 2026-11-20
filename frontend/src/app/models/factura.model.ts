export interface Factura {
    id: number;
    numeroFactura: string;
    apartamento: string;
    propietario: string;
    valor: number;
    fechaVencimiento: string;
    estado: 'PENDIENTE' | 'PAGADA';
}