
package isi.deso.desarrollotrabajopractico;

import java.util.ArrayList;
import java.util.Iterator;

public class Vendedor {
    
    private int id;
    private String nombre;
    private String direccion;
    private Coordenada coordenadas;
    private ArrayList<ItemMenu> items;
    private ArrayList<Pedido> pedidos;
    
    public Vendedor(int id, String nombre, String direccion, Coordenada coordenadas){
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.coordenadas = coordenadas;
        items = new ArrayList<>();
        pedidos = new ArrayList<>();
    }
    
    public Vendedor(){}
    
    public void setItems(ArrayList<ItemMenu> items) {
        this.items = items;
    }
    
    public int getId(){
        return id;
    }

    public ArrayList<ItemMenu> getItems() {
        return items;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getDireccion(){
        return direccion;
    }

    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    public Coordenada getCoordenadas(){
        return coordenadas;
    }

    public void setCoordenadas(Coordenada coordenadas){
        this.coordenadas = coordenadas;
    }
    
    public String toString(){
        return "ID: " + id + ", nombre: " + nombre + ", direccion: " + direccion + ".";
    }
    
    public double distancia(Cliente unCliente){
        double dLatitud =  (unCliente.getCoordenadas().getLatitud() - coordenadas.getLatitud())/2;
        double dLongitud = (unCliente.getCoordenadas().getLongitud() - coordenadas.getLongitud())/2;
        double radio = 6372.8;
        
        double distancia;
        double raiz = Math.sqrt(Math.pow((Math.sin(dLatitud)),2) + (Math.cos(coordenadas.getLatitud())*Math.cos(unCliente.getCoordenadas().getLatitud())*(Math.pow((Math.sin(dLongitud)),2))));
        distancia = 2*radio*Math.asin(raiz);
        
        return distancia; 
    }
        
    public ArrayList<Bebida> getBebidas(){
        ArrayList<Bebida> bebidas = new ArrayList<>();
        
        for(int i = 0 ; i < items.size(); i++){
            if(items.get(i) instanceof Bebida){
                bebidas.add((Bebida)items.get(i));
            }
        }
        return bebidas;
    }
    
    public ArrayList<Plato> getPlatos(){
        ArrayList<Plato> platos = new ArrayList<>();
        
        for(int i = 0 ; i< items.size(); i++){
            if(items.get(i) instanceof Plato){
                platos.add((Plato)items.get(i));
            }
        }
        return platos;
    }
    
    public ArrayList<Plato> getPlatosVeganos(){
        ArrayList<Plato> platos = getPlatos();
        Iterator iterador = platos.iterator();
        
        while(iterador.hasNext()){
           Plato p = (Plato)iterador.next();
           if(!p.aptoVegano()){
               iterador.remove();
           }
        }
        return platos;
   }
    
    public ArrayList<Bebida> getBebidasSinAlcohol(){
        ArrayList<Bebida> bebidas = getBebidas();
        Iterator<Bebida> iterador = bebidas.iterator();
        
        while(iterador.hasNext()){
           Bebida b = iterador.next();
           if(b instanceof Alcohol){
               iterador.remove();
           }
        }
        return bebidas;
   }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
    
   public void agregarPedido(Pedido pedido){
       pedidos.add(pedido);
    }
   
   public ArrayList<Pedido> buscarPedidosPorEstado(Estado estado){
      ArrayList<Pedido> pedidosPorEstado = new ArrayList();
       
       for(Pedido p : pedidos){
           if(p.getEstado() == estado){
               pedidosPorEstado.add(p);
           }
       }
       return pedidosPorEstado;
   }
   
   public void agregarItems(ArrayList<ItemMenu> items) {
       
       for(ItemMenu im : items) {
           this.items.add(im);
       }
   }
  
   public void cambiarEstadoPedido(PedidoObservable pedido){
     
     ArrayList<Pedido> pedidosEnProceso = buscarPedidosPorEstado(Estado.EN_PROCESO);
     
     if(pedido instanceof Pedido){
     Pedido p = (Pedido) pedido;
     
     if(pedidosEnProceso.contains(p)){
       p.setEstado(Estado.EN_ENVIO);
       p.setChanged();
       p.notifyObservers();
     }
    }
   }

  }
 
