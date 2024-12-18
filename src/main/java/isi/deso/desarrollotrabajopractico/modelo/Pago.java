
package isi.deso.desarrollotrabajopractico.modelo;

import java.time.LocalDate;

public abstract class Pago {
    
    protected int id_pago;
    protected LocalDate fecha;
    protected double monto;
    public abstract double calcularPrecio(double total);
    
    public void setMonto(double monto) {
        this.monto = monto;
    }
    
    public double getMonto() {
        return monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id_pago;
    }
    
    public void setId(int id) {
        id_pago = id;
    }
    
}
