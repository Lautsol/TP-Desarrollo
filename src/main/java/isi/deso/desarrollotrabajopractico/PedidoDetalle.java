
package isi.deso.desarrollotrabajopractico;

public class PedidoDetalle {
    private ItemMenu producto;
    private int cantidad;

    public PedidoDetalle(ItemMenu producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }
    
    public double calcularPrecio(){
        return cantidad * producto.getPrecio();
    }
    
    public ItemMenu getProducto() {
        return producto;
    }

    public void setProducto(ItemMenu producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
}
