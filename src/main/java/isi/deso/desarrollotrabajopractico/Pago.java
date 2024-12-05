
package isi.deso.desarrollotrabajopractico;

import java.time.LocalDate;

public abstract class Pago {
    
    protected int id_pago;
    protected LocalDate fecha;
    protected double monto;
    protected int id_pedido;
    public abstract double calcularPrecio(double total);
    
    public void setMonto(double monto){
        this.monto = monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getMonto() {
        return monto;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getId() {
        return id_pago;
    }
    
    public void setId(int id) {
        id_pago = id;
    }
    
}
