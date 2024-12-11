
package isi.deso.desarrollotrabajopractico.modelo;

import java.util.ArrayList;

public class Cliente implements ClienteObserver {
    
    private int id;
    private long cuit;
    private String email;
    private String direccion;
    private String nombre;
    private Coordenada coordenadas;
    private ArrayList<Pedido> pedidos;
    private Pedido pedidoActual;
    private Long cbu;
    private String alias;
   
    public Cliente(int id, long cuit, String nombre, String email, String direccion, Coordenada coordenadas, Long cbu, String alias) {
        this.id = id;
        this.cuit = cuit;
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
        this.coordenadas = coordenadas;
        pedidos = new ArrayList<>();
        this.cbu = cbu;
        this.alias = alias;
    }
    
    public Cliente(int id, long cuit, String nombre, String email, String direccion, Coordenada coordenadas, Long cbu) {
        this.id = id;
        this.cuit = cuit;
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
        this.coordenadas = coordenadas;
        pedidos = new ArrayList<>();
        this.cbu = cbu;
    }
    
    public Cliente(int id, long cuit, String nombre, String email, String direccion, Coordenada coordenadas, String alias) {
        this.id = id;
        this.cuit = cuit;
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
        this.coordenadas = coordenadas;
        pedidos = new ArrayList<>();
        this.alias = alias;
    }
    
    public Cliente(){}
    
    public Pedido iniciarPedido(Vendedor vendedor) {
        
        if(pedidoActual == null){
            pedidoActual = new Pedido(this, vendedor);
        }
        
        return pedidoActual;
    }
 
    public boolean agregarProducto(PedidoDetalle pd) {
        boolean exito = false;
        
        if(pedidoActual != null){
            pedidoActual.agregarPedidoDetalle(pd);
            exito = true;
        } 
        return exito;
    }
    
    public boolean confirmarPedido(TipoDePago tipoPago) {
        boolean confirmado = false;
        
        if(pedidoActual != null && !pedidoActual.getPedidoDetalle().isEmpty()){
           pedidoActual.setTipoPago(tipoPago);
           pedidos.add(pedidoActual);
           pedidoActual.getVendedor().agregarPedido(pedidoActual);
           pedidoActual.calcularPrecioPedido();
           pedidoActual = null;
           confirmado = true;
        }
        return confirmado;
    }
    
    public Long getCbu() {
        return cbu;
    }

    public void setCbu(Long cbu) {
        this.cbu = cbu;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }
   
    public int getId() {
        return id;
    }

    public long getCuit() {
        return cuit;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public Coordenada getCoordenadas() {
        return coordenadas;
    }
    
    public Pedido getPedidoActual() {
        return pedidoActual;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCuit(long cuit) {
        this.cuit = cuit;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setCoordenadas(Coordenada coordenadas) {
        this.coordenadas = coordenadas;
    }
    
    public void agregarPedido(Pedido p) {
        p.setCliente(this);
        pedidos.add(p);
    }
    
    public String toString() {
        return "ID: " + id + ", cuit: " + cuit + ", nombre: " + nombre + ", email: " + email + ", direccion: " + direccion + ".";
    }
    
    public void update(PedidoObservable pedidoObservable) {
        
        if(pedidoObservable instanceof Pedido){
            Pedido p = (Pedido) pedidoObservable;

               if(pedidos.contains(p)){

                    if(p.getTipoPago() == TipoDePago.MERCADOPAGO){
                        p.generarMercadoPago();
                    }
                    else p.generarTransferencia();
                }
            }
        }
    
    public boolean suscripcionEstadoPedido(PedidoObservable pedidoObservable) {
        
        boolean suscripto = false;
        
        if(pedidoObservable instanceof Pedido){
            Pedido p = (Pedido) pedidoObservable;
            
            if(pedidos.contains(p)){
                p.addObserver(this);
                suscripto = true;
            }
        }
        return suscripto;
    }
    
    public Estado verEstadoPedido(int indice) {
        return pedidos.get(indice).getEstado();
    }
}
   
   
   
