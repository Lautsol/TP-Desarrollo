
package isi.deso.desarrollotrabajoprueba;

import isi.deso.desarrollotrabajopractico.modelo.Categoria;
import isi.deso.desarrollotrabajopractico.modelo.Cliente;
import isi.deso.desarrollotrabajopractico.modelo.Pedido;
import isi.deso.desarrollotrabajopractico.Controladores.PedidoController;
import isi.deso.desarrollotrabajopractico.modelo.Coordenada;
import isi.deso.desarrollotrabajopractico.modelo.Estado;
import isi.deso.desarrollotrabajopractico.modelo.PedidoDetalle;
import isi.deso.desarrollotrabajopractico.modelo.Plato;
import isi.deso.desarrollotrabajopractico.modelo.TipoDePago;
import isi.deso.desarrollotrabajopractico.modelo.TipoItem;
import isi.deso.desarrollotrabajopractico.modelo.Vendedor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class PedidoControllerTest {

    private static PedidoController pedidoController;
    private static Connection connection;
    private static List<Integer> pedidosAEliminar;

    @BeforeAll
    static void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/grupo11", "root", "grupo11");
        pedidoController = new PedidoController();
        pedidosAEliminar = new ArrayList<>();
    }

    @AfterAll
    static void tearDownAfterClass() throws SQLException {

        String queryDelete = "DELETE FROM pedidos WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/grupo11", "root", "grupo11");
             PreparedStatement stmt = connection.prepareStatement(queryDelete)) {

            for (int pedidoId : pedidosAEliminar) {
                stmt.setInt(1, pedidoId); 
                stmt.executeUpdate();
            }

            connection.close();
        }
    }

    @Test
    void testCrearPedido() throws SQLException {
        
        Coordenada c1 = new Coordenada(54.0, 35.0);
        Vendedor vendedor1 = new Vendedor(1,"Mateo","La Rioja 3240",c1);
        Cliente cliente1 = new Cliente(3,123456,"Lautaro","c001@gmail.com","Corrientes 2766",c1,(long)8,"C1.mp");
        Categoria cat1 = new Categoria(1,"Saludable",TipoItem.COMIDA);
        Plato plato1 = new Plato(1,"Ensalada vegana","Ensalada con quinoa, espinaca y aguacate",3500,cat1,300,450,true,true,true);
        PedidoDetalle pd = new PedidoDetalle(plato1, 2);
        cliente1.iniciarPedido(vendedor1);
        cliente1.agregarProducto(pd);
        
        Pedido pedido1 = cliente1.getPedidoActual();
        int id = pedidoController.crearPedido(pedido1, TipoDePago.MERCADOPAGO, cliente1);

        // Verificar si el pedido fue creado correctamente
        Pedido pedidoCreado = pedidoController.buscarPedido(id);
        assertNotNull(pedidoCreado);
        assertEquals(TipoDePago.MERCADOPAGO, pedidoCreado.getTipoPago());
        
        pedidosAEliminar.add(id);
    }

    @Test
    void testActualizarPedido() throws SQLException {

        Coordenada c1 = new Coordenada(54.0, 35.0);
        Vendedor vendedor1 = new Vendedor(1,"Mateo","La Rioja 3240",c1);
        Cliente cliente1 = new Cliente(3,123456,"Lautaro","c001@gmail.com","Corrientes 2766",c1,(long)8,"C1.mp");
        Categoria cat1 = new Categoria(1,"Saludable",TipoItem.COMIDA);
        Plato plato1 = new Plato(1,"Ensalada vegana","Ensalada con quinoa, espinaca y aguacate",5000,cat1,300,450,true,true,true);
        PedidoDetalle pd = new PedidoDetalle(plato1, 2);
        cliente1.iniciarPedido(vendedor1);
        cliente1.agregarProducto(pd);
        
        Pedido pedido1 = cliente1.getPedidoActual();
        int id = pedidoController.crearPedido(pedido1, TipoDePago.MERCADOPAGO, cliente1);
        
        Pedido pedido2 = pedidoController.buscarPedido(id);

        pedidoController.actualizarPedido(pedido2, cliente1, vendedor1);

        // Verificar que los cambios se han realizado correctamente
        Pedido pedidoActualizado = pedidoController.buscarPedido(id);
        assertNotNull(pedidoActualizado);
        
        assertEquals(cliente1.getId(), pedidoActualizado.getCliente().getId());
        assertEquals(vendedor1.getId(), pedidoActualizado.getVendedor().getId());
        assertEquals(Estado.RECIBIDO, pedidoActualizado.getEstado());
        assertNotNull(pedidoActualizado.getPago());
        assertEquals(10400, pedidoActualizado.getPago().getMonto());
        
        pedidosAEliminar.add(id);
    }

    @Test
    void testEliminarPedido() throws SQLException {

        Coordenada c1 = new Coordenada(54.0, 35.0);
        Vendedor vendedor1 = new Vendedor(1,"Mateo","La Rioja 3240",c1);
        Cliente cliente1 = new Cliente(3,123456,"Lautaro","c001@gmail.com","Corrientes 2766",c1,(long)8,"C1.mp");
        Categoria cat1 = new Categoria(1,"Saludable",TipoItem.COMIDA);
        Plato plato1 = new Plato(1,"Ensalada vegana","Ensalada con quinoa, espinaca y aguacate",5000,cat1,300,450,true,true,true);
        PedidoDetalle pd = new PedidoDetalle(plato1, 2);
        cliente1.iniciarPedido(vendedor1);
        cliente1.agregarProducto(pd);
        
        Pedido pedido1 = cliente1.getPedidoActual();
        int id = pedidoController.crearPedido(pedido1, TipoDePago.MERCADOPAGO, cliente1);

        Pedido pedidoAntesDeEliminar = pedidoController.buscarPedido(id);
        assertNotNull(pedidoAntesDeEliminar);

        pedidoController.eliminarPedido(id);

        // Verificar que el pedido ha sido eliminado
        Pedido pedidoEliminado = pedidoController.buscarPedido(id);
        assertNull(pedidoEliminado);
    }

    @Test
    void testBuscarPedidoPorId() throws SQLException {

        Coordenada c1 = new Coordenada(54.0, 35.0);
        Vendedor vendedor1 = new Vendedor(1,"Mateo","La Rioja 3240",c1);
        Cliente cliente1 = new Cliente(3,123456,"Lautaro","c001@gmail.com","Corrientes 2766",c1,(long)8,"C1.mp");
        Categoria cat1 = new Categoria(1,"Saludable",TipoItem.COMIDA);
        Plato plato1 = new Plato(1,"Ensalada vegana","Ensalada con quinoa, espinaca y aguacate",5000,cat1,300,450,true,true,true);
        PedidoDetalle pd = new PedidoDetalle(plato1, 2);
        cliente1.iniciarPedido(vendedor1);
        cliente1.agregarProducto(pd);
        
        Pedido pedido1 = cliente1.getPedidoActual();
        int id = pedidoController.crearPedido(pedido1, TipoDePago.MERCADOPAGO, cliente1);

        Pedido pedidoBuscado = pedidoController.buscarPedido(id);

        // Verificar que el pedido fue encontrado
        assertNotNull(pedidoBuscado);
        assertEquals(TipoDePago.MERCADOPAGO, pedidoBuscado.getTipoPago());
        
        pedidosAEliminar.add(id);
    }
    
    @Test
    void testBuscarPedidosPorIdVendedor() throws SQLException {
        
        Coordenada c1 = new Coordenada(54.0, 35.0);
        Vendedor vendedor1 = new Vendedor(1, "Mateo", "La Rioja 3240", c1);
        Cliente cliente1 = new Cliente(3, 123456, "Lautaro", "c001@gmail.com", "Corrientes 2766", c1, (long) 8, "C1.mp");
        Categoria cat1 = new Categoria(1, "Saludable", TipoItem.COMIDA);
        Plato plato1 = new Plato(1, "Ensalada vegana", "Ensalada con quinoa, espinaca y aguacate", 5000, cat1, 300, 450, true, true, true);
        PedidoDetalle pd = new PedidoDetalle(plato1, 2);

        cliente1.iniciarPedido(vendedor1);
        cliente1.agregarProducto(pd);
        Pedido pedido1 = cliente1.getPedidoActual();
        int id = pedidoController.crearPedido(pedido1, TipoDePago.MERCADOPAGO, cliente1);

        // Buscar pedidos por id del vendedor
        List<Pedido> pedidosDelVendedor = pedidoController.listarPedidosVendedor(vendedor1.getId());

        // Verificar que los pedidos están asociados al vendedor correcto
        assertNotNull(pedidosDelVendedor);
        assertFalse(pedidosDelVendedor.isEmpty());
        for (Pedido pedido : pedidosDelVendedor) {
            assertEquals(vendedor1.getId(), pedido.getVendedor().getId());
        }
        
        pedidosAEliminar.add(id);
    }
    
    @Test
    void testBuscarPedidosPorIdCliente() throws SQLException {
        
        Coordenada c1 = new Coordenada(54.0, 35.0);
        Vendedor vendedor1 = new Vendedor(1, "Lucia", "Salta 123", c1);
        Cliente cliente1 = new Cliente(3, 123456, "Lautaro", "c001@gmail.com", "Corrientes 2766", c1, (long) 8, "C1.mp");
        Categoria cat1 = new Categoria(1, "Saludable", TipoItem.COMIDA);
        Plato plato1 = new Plato(1, "Ensalada vegana", "Ensalada con quinoa, espinaca y aguacate", 5000, cat1, 300, 450, true, true, true);
        PedidoDetalle pd = new PedidoDetalle(plato1, 2);

        // Crear un pedido con el cliente
        cliente1.iniciarPedido(vendedor1);
        cliente1.agregarProducto(pd);
        Pedido pedido1 = cliente1.getPedidoActual();
        int id = pedidoController.crearPedido(pedido1, TipoDePago.MERCADOPAGO, cliente1);

        // Buscar pedidos por id del cliente
        List<Pedido> pedidosDelCliente = pedidoController.listarPedidosCliente(cliente1.getId());

        // Verificar que los pedidos están asociados al cliente correcto
        assertNotNull(pedidosDelCliente);
        assertFalse(pedidosDelCliente.isEmpty());
        for (Pedido pedido : pedidosDelCliente) {
            assertEquals(cliente1.getId(), pedido.getCliente().getId());
        }
        
        pedidosAEliminar.add(id);
    }

}

