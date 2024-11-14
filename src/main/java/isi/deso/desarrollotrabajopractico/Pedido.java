
package isi.deso.desarrollotrabajopractico;

import isi.deso.desarrollotrabajopractico.DAOS.FactoryDAO;
import isi.deso.desarrollotrabajopractico.DAOS.ItemMenuVendedorMySQLDAO;
import java.time.LocalDate;
import java.util.ArrayList;

public class Pedido  extends PedidoObservable {
    private int id_pedido;
    private Cliente cliente;
    private ArrayList<PedidoDetalle> pedidoDetalle;
    private double total;
    private Pago formaDePago;
    private Estado estado;
    private Vendedor vendedor;
    private boolean cambioEstado;
    private TipoDePago tipoPago;
     
    public Pedido(int id, Cliente cliente, Vendedor vendedor) {
        id_pedido = id;
        this.cliente = cliente;
        this.vendedor = vendedor;
        pedidoDetalle = new ArrayList<>();
        estado = Estado.EN_PROCESO;
        clientesObserver = new ArrayList<>();
    }

    public Pedido() {
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setFormaDePago(Pago formaDePago) {
        this.formaDePago = formaDePago;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public void setCambioEstado(boolean cambioEstado) {
        this.cambioEstado = cambioEstado;
    }
    
    public int getId_pedido() {
        return id_pedido;
    }
    
    public Vendedor getVendedor() {
        return vendedor;
    }
    
    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    private double calcularPrecioPedido() {
        double precioTotal = 0;
        
        for(PedidoDetalle p : pedidoDetalle){
            precioTotal += p.calcularPrecio();
        }
        
        return precioTotal;
    }
    
    public boolean pedidosDeUnVendedor(ArrayList<PedidoDetalle> pd) {
        
        boolean unVendedor = true;
        
        for(PedidoDetalle ped : pd){
           if(!vendedor.getItems().contains(ped.getProducto())){
               unVendedor = false;
           }
        }
        
        return unVendedor;
    }

    public void setTipoPago(TipoDePago tipoPago) {
        this.tipoPago = tipoPago;
    }

    public TipoDePago getTipoPago() {
        return tipoPago;
    }

    public Pago generarTransferencia() {
        
        try{
            if(formaDePago != null) throw new PagoYaEfectuadoException();
            formaDePago = new Transferencia(cliente.getCbu(), cliente.getCuit(), LocalDate.now());
            formaDePago.setMonto(calcularPrecioFinal());
        }
        catch(PagoYaEfectuadoException e){
           System.out.println(e.getMessage());
        }
        
        return formaDePago;
    }
    
    public Pago generarMercadoPago() {
        
         try{
            if(formaDePago != null) throw new PagoYaEfectuadoException();
            formaDePago = new MercadoPago(cliente.getAlias(), LocalDate.now());
            formaDePago.setMonto(calcularPrecioFinal());
        }
        catch(PagoYaEfectuadoException e){
           System.out.println(e.getMessage());
        }
        return formaDePago;
    }

    public double calcularPrecioFinal() {
        total = formaDePago.calcularPrecio(calcularPrecioPedido());
        return total;
    }
    
    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ArrayList<PedidoDetalle> getPedidoDetalle() {
        return pedidoDetalle;
    }
    
    public void setPedidoDetalle(ArrayList<PedidoDetalle> pedidoDetalle) throws ProductoDeOtroVendedorException {
       
            // if(!pedidosDeUnVendedor(pedidoDetalle)) throw new ProductoDeOtroVendedorException();
            ItemMenuVendedorMySQLDAO itemsMenuVendedorMySQLDAO = (ItemMenuVendedorMySQLDAO) FactoryDAO.getItemMenuVendedorDAO();
            for(PedidoDetalle pd : pedidoDetalle) {
                if(!itemsMenuVendedorMySQLDAO.itemMenuDeVendedor(vendedor.getId(), pd.getProducto().getId())) throw new ProductoDeOtroVendedorException();
            }
            this.pedidoDetalle = pedidoDetalle;
    }
    
    public void agregarPedidoDetalle(PedidoDetalle pd) {
        
        try{
            if(!vendedor.getItems().contains(pd.getProducto())) throw new ProductoDeOtroVendedorException();
            pedidoDetalle.add(pd);
        
        }
        catch(ProductoDeOtroVendedorException e){
            System.out.println(e.getMessage());
        }
    }
    
    public Estado getEstado() {
        return estado;
    }
    
     public void addObserver(ClienteObserver cliente){
         clientesObserver.add(cliente);
     }
    
     public boolean removeObserver(ClienteObserver cliente){
        return clientesObserver.remove(cliente);
    }
     
    public void notifyObservers(){
       
        for(ClienteObserver c : clientesObserver){
            c.update(this);
        }
        cambioEstado = false;
    }
    
    public void setChanged(){
        cambioEstado = true;
    }
}