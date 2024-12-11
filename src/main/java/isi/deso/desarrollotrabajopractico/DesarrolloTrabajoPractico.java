package isi.deso.desarrollotrabajopractico;

import isi.deso.desarrollotrabajopractico.modelo.Vendedor;
import isi.deso.desarrollotrabajopractico.modelo.Cliente;
import isi.deso.desarrollotrabajopractico.Interfaces.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.UIManager;

public class DesarrolloTrabajoPractico {

    public static void main(String[] args) {
        
        /*
        Coordenada c1 = new Coordenada(54.0, 35.0);
        Coordenada c2 = new Coordenada(32.0, 42.0);
        Coordenada c3 = new Coordenada(73.0, 13.0);
        Coordenada c4 = new Coordenada(13.4, 53.6);
        Coordenada c5 = new Coordenada(90.3, 76.2);
        Coordenada c6 = new Coordenada(55.0, 01.0);
        Vendedor v1 = new Vendedor(1,"Mateo","La Rioja 3240",c1);
        Vendedor v2 = new Vendedor(2,"Valentino","Mendoza 2341",c2);
        Vendedor v3 = new Vendedor(3,"Mateo","Cruz Roja 235",c3);
        Cliente cliente1 = new Cliente(1,123456,"Lautaro","c001@gmail.com","Corrientes 2766",c4,54534,"C1.mp");
        Cliente cliente2 = new Cliente(2,678923,"Lautaro","c002@gmail.com","San Juan 1234",c5,48594,"C2.mp");
        Cliente cliente3 = new Cliente(3,192837,"Pedro","c003@gmail.com","San Martin 299",c6,13921,"C3.mp");
        ItemsPedidoMemory ipm = new ItemsPedidoMemory();
        
        ArrayList<Vendedor> vendedores = new ArrayList<>();
        vendedores.add(v1);
        vendedores.add(v2);
        vendedores.add(v3);
        
        ArrayList<Cliente> clientes = new ArrayList<>();
        clientes.add(cliente1);
        clientes.add(cliente2);
        clientes.add(cliente3);
        
        System.out.println(buscarPorIdVendedor(2, vendedores).toString());
        System.out.println(buscarPorIdCliente(1, clientes).toString());
        
        for(Vendedor v : buscarPorNombreVendedor("Mateo", vendedores)){
            System.out.println(v.toString());
        }
        
        for(Cliente c : buscarPorNombreCliente("Lautaro", clientes)){
            System.out.println(c.toString());
        }
        
        System.out.println(" ");
        
        
        Categoria cat1 = new Categoria(1,"Saludable",TipoItem.COMIDA);
        Categoria cat2 = new Categoria(2,"Tradicional",TipoItem.COMIDA);
        Categoria cat3 = new Categoria(3,"Gourmet", TipoItem.COMIDA);
        Plato plato1 = new Plato(1,"Ensalada vegana","Ensalada con quinoa, espinaca y aguacate",3500,cat1,300,450,true,true,true);
        Plato plato2 = new Plato(2,"Lasagna de verduras","Lasagna con calabacín, berenjena y queso",8000,cat2,500,800,false,true,false);
        Plato plato3 = new Plato(3,"Pollo con papas","Pollo al horno con papas",13000,cat3,400,900,false,false,true);
        
        Categoria cat4 = new Categoria(4,"Refrescante",TipoItem.BEBIDA);
        Categoria cat5 = new Categoria(5,"Premium",TipoItem.BEBIDA);
        Categoria cat6 = new Categoria(6,"Económico",TipoItem.BEBIDA);
        Bebida bebida1 = new Alcohol(2,"Vino Malbec","Vino tinto Malbec",5000,cat5,750,14);
        Bebida bebida2 = new Gaseosa(3,"Coca-Cola","Coca-Cola sabor clásico",1000,cat4,500);
        Bebida bebida3 = new Alcohol(4,"Cerveza Brahma","Cerveza lager Brahma",650,cat6,473,4.5);
        Bebida bebida4 = new Gaseosa(5,"Sprite","Sprite sabor limón",850,cat4,500);
        
        ArrayList<ItemMenu> items = new ArrayList<>();
        items.add(plato1);
        items.add(plato2);
        items.add(plato3);
        items.add(bebida1);
        items.add(bebida2);
        
        ArrayList<ItemMenu> items1 = new ArrayList<>();
        items1.add(plato1);
        items1.add(plato2);
        items1.add(bebida3);
        items1.add(bebida4);
        
        v1.setItems(items);
        v2.setItems(items1);
        
        /*
        ArrayList<Bebida> prueba = v1.getBebidas();
        System.out.println("Bebidas del vendedor: ");
        for(Bebida b: prueba){    
            System.out.println(b.getNombre());
        }
        System.out.println(" ");
        
        ArrayList<Plato> prueba1 = v1.getPlatos();
        System.out.println("Platos del vendedor: ");
        for(Plato b: prueba1){    
            System.out.println(b.getNombre());
        }
        System.out.println(" ");
        
        ArrayList<Plato> prueba2 = v1.getPlatosVeganos();
        System.out.println("Platos veganos del vendedor: ");
        for(Plato b: prueba2){    
            System.out.println(b.getNombre());
        }
        System.out.println(" "); 
        
        ArrayList<Bebida> prueba3 = v1.getBebidasSinAlcohol();
        System.out.println("Bebidas sin alcohol del vendedor: ");
        for(Bebida b: prueba3){    
            System.out.println(b.getNombre());
        }
        System.out.println(" "); 
       
        cliente3.iniciarPedido(v2);
        cliente3.agregarProducto(plato1, 2);
        cliente3.agregarProducto(plato2, 1);
        cliente3.agregarProducto(bebida1, 1);
        
        
        p1.generarMercadoPago();
        p1.generarTransferencia();
        p2.generarTransferencia();
        p2.getEstado();
        
        
        ArrayList<ItemMenu> itemsMenu = new ArrayList<>();
        itemsMenu.add(plato1);
        itemsMenu.add(plato3);
        itemsMenu.add(bebida2);
        itemsMenu.add(bebida3);
        
        
        try{
            
          //pruebas filtrarPorCliente
          ArrayList<PedidoDetalle> pedidoDetallesCliente1 = ipm.filtrarPorCliente(clientes, 1);
          ArrayList<PedidoDetalle> pedidoDetallesCliente5 = ipm.filtrarPorCliente(clientes, 5);
          
          for(PedidoDetalle pdc1 : pedidoDetallesCliente1){
              System.out.println(pdc1.getProducto().getNombre());
          }
          
          //pruebas filtrarPorVendedor
          ArrayList<ItemMenu> itemsMenuVendedor1 = ipm.filtrarPorVendedor(vendedores, 1);
          ArrayList<ItemMenu> itemsMenuVendedor5 = ipm.filtrarPorVendedor(vendedores, 5);
          
          for(ItemMenu imv1 : itemsMenuVendedor1){
              System.out.println(imv1.getNombre());
          }
          
          //pruebas ordenarPorNombreCliente
          ArrayList<Cliente> clientes_aux = new ArrayList<>();
          ArrayList<Cliente> cli1 = ipm.ordenarPorNombreCliente(clientes_aux);
          ArrayList<Cliente> cli2 = ipm.ordenarPorNombreCliente(clientes);
          
          for(Cliente c : cli2){
              System.out.println(c.getNombre());
          }
          
          //pruebas ordenarPorNombreVendedor
          ArrayList<Vendedor> vendedores_aux = new ArrayList<>();
          ArrayList<Vendedor> vend1 = ipm.ordenarPorNombreVendedor(vendedores_aux);
          ArrayList<Vendedor> vend2 = ipm.ordenarPorNombreVendedor(vendedores);
          
          for(Vendedor v : vend2){
              System.out.println(v.getNombre());
          }
          
          //pruebas busquedaPorRangoPrecios
          ArrayList<ItemMenu> busquedaItems = ipm.busquedaPorRangoPrecios(itemsMenu, 500, 1000);
          ArrayList<ItemMenu> busquedaItems2 = ipm.busquedaPorRangoPrecios(itemsMenu, 50000, 60000);
          
          for(ItemMenu im : busquedaItems){
              System.out.println(im.getNombre());
          }
          
          //pruebas busquedaPorRestaurante
          ArrayList<Plato> platosV1 = ipm.busquedaPorRestaurante(vendedores, 1);
          ArrayList<Plato> platosV2 = ipm.busquedaPorRestaurante(vendedores, 2);
          ArrayList<Plato> platosV5 = ipm.busquedaPorRestaurante(vendedores, 5);
          
          for(Plato p : platosV1){
              System.out.println(p.getNombre());
          }
          
        }
        
        catch(ItemNoEncontradoException e){
            System.out.print(e.getMessage());
        }
        
        
        cliente1.iniciarPedido(1, v1);
        cliente1.agregarProducto(plato1, 2);
        cliente1.agregarProducto(plato2, 1);
        cliente1.agregarProducto(bebida1, 1);
        cliente1.agregarProducto(bebida2, 4);
        cliente1.confirmarPedido(TipoDePago.MERCADOPAGO);
        boolean suscripto = cliente1.suscripcionEstadoPedido(cliente1.getPedidos().get(0));
        
        System.out.println(suscripto);
        System.out.println("Estado del pedido: " + cliente1.verEstadoPedido(0));
        
        v1.cambiarEstadoPedido(cliente1.getPedidos().get(0));
        
        System.out.println("Estado del pedido: " + cliente1.verEstadoPedido(0));

        */
        
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        new Principal();
    }
    
    public static Vendedor buscarPorIdVendedor(int id, ArrayList<Vendedor> vendedores) {
       Vendedor vendedor = new Vendedor();
        
        for(int i=0; i<vendedores.size(); i++){
            if(vendedores.get(i).getId() == id){
                vendedor = vendedores.get(i);
            }
         }
        
        return vendedor;
    }
    
    public static ArrayList<Vendedor> buscarPorNombreVendedor(String nombre, ArrayList<Vendedor> vendedores) {
        ArrayList<Vendedor> vendedores1 = new ArrayList<>();
        
        for(int i=0; i<vendedores.size(); i++){
            if(nombre.equals(vendedores.get(i).getNombre())){
                vendedores1.add(vendedores.get(i));
            }
         }
        
        return vendedores1;
    }
    
    public static void eliminarVendedor(int id, ArrayList<Vendedor> vendedores) {
        Iterator iterador = vendedores.iterator();
        
        while(iterador.hasNext()){
           Vendedor v = (Vendedor)iterador.next();
           if(v.getId() == id){
               iterador.remove();
           }
        }
    }
    
    public static Cliente buscarPorIdCliente(int id, ArrayList<Cliente> clientes) {
       Cliente cliente = new Cliente();
        
        for(int i=0; i<clientes.size(); i++){
            if(clientes.get(i).getId() == id){
                cliente = clientes.get(i);
            }
         }
        
        return cliente;
    }
    
    public static ArrayList<Cliente> buscarPorNombreCliente(String nombre, ArrayList<Cliente> clientes) {
        ArrayList<Cliente> clientes1 = new ArrayList<>();
        
        for(int i=0; i<clientes.size(); i++){
            if(nombre.equals(clientes.get(i).getNombre())){
                clientes1.add(clientes.get(i));
            }
         }
        
        return clientes1;
    }
    
    public static void eliminarCliente(int id, ArrayList<Cliente> clientes) {
        Iterator iterador = clientes.iterator();
        
        while(iterador.hasNext()){
           Cliente c = (Cliente)iterador.next();
           if(c.getId() == id){
               iterador.remove();
           }
        }
    }
            
}
