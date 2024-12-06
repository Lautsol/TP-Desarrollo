
package isi.deso.desarrollotrabajoprueba;

import isi.deso.desarrollotrabajopractico.Categoria;
import isi.deso.desarrollotrabajopractico.Cliente;
import isi.deso.desarrollotrabajopractico.Pedido;
import isi.deso.desarrollotrabajopractico.Controladores.PedidoController;
import isi.deso.desarrollotrabajopractico.Coordenada;
import isi.deso.desarrollotrabajopractico.Estado;
import isi.deso.desarrollotrabajopractico.PedidoDetalle;
import isi.deso.desarrollotrabajopractico.Plato;
import isi.deso.desarrollotrabajopractico.TipoDePago;
import isi.deso.desarrollotrabajopractico.TipoItem;
import isi.deso.desarrollotrabajopractico.Vendedor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class PedidoControllerTest {

    private static PedidoController pedidoController;
    private static Connection connection;

    @BeforeAll
    static void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/grupo11", "root", "grupo11");
        pedidoController = new PedidoController();
    }

    @AfterAll
    static void tearDownAfterClass() throws SQLException {

        List<Integer> pedidosAEliminar = Arrays.asList(30, 31, 32, 33, 34, 35, 36);

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
        cliente1.iniciarPedido(30, vendedor1);
        cliente1.agregarProducto(pd);
        
        Pedido pedido1 = cliente1.getPedidoActual();
        pedidoController.crearPedido(pedido1, TipoDePago.MERCADOPAGO, cliente1);

        // Verificar si el pedido fue creado correctamente
        Pedido pedidoCreado = pedidoController.buscarPedido(30);
        assertNotNull(pedidoCreado);
        assertEquals(pedido1.getId_pedido(), pedidoCreado.getId_pedido());
        assertEquals(TipoDePago.MERCADOPAGO, pedidoCreado.getTipoPago());

    }

    @Test
    void testActualizarPedido() throws SQLException {

        Coordenada c1 = new Coordenada(54.0, 35.0);
        Vendedor vendedor1 = new Vendedor(1,"Mateo","La Rioja 3240",c1);
        Cliente cliente1 = new Cliente(3,123456,"Lautaro","c001@gmail.com","Corrientes 2766",c1,(long)8,"C1.mp");
        Categoria cat1 = new Categoria(1,"Saludable",TipoItem.COMIDA);
        Plato plato1 = new Plato(1,"Ensalada vegana","Ensalada con quinoa, espinaca y aguacate",5000,cat1,300,450,true,true,true);
        PedidoDetalle pd = new PedidoDetalle(plato1, 2);
        cliente1.iniciarPedido(31, vendedor1);
        cliente1.agregarProducto(pd);
        
        Pedido pedido1 = cliente1.getPedidoActual();
        pedidoController.crearPedido(pedido1, TipoDePago.MERCADOPAGO, cliente1);
        
        Pedido pedido2 = pedidoController.buscarPedido(31);

        pedidoController.actualizarPedido(pedido2, cliente1, vendedor1);

        // Verificar que los cambios se han realizado correctamente
        Pedido pedidoActualizado = pedidoController.buscarPedido(31);
        assertNotNull(pedidoActualizado);
        
        assertEquals(pedido2.getId_pedido(), pedidoActualizado.getId_pedido());
        assertEquals(cliente1.getId(), pedidoActualizado.getCliente().getId());
        assertEquals(vendedor1.getId(), pedidoActualizado.getVendedor().getId());
        assertEquals(Estado.RECIBIDO, pedidoActualizado.getEstado());
        assertNotNull(pedidoActualizado.getPago());
        assertEquals(10400, pedidoActualizado.getPago().getMonto());
    }

    @Test
    void testEliminarPedido() throws SQLException {

        Coordenada c1 = new Coordenada(54.0, 35.0);
        Vendedor vendedor1 = new Vendedor(1,"Mateo","La Rioja 3240",c1);
        Cliente cliente1 = new Cliente(3,123456,"Lautaro","c001@gmail.com","Corrientes 2766",c1,(long)8,"C1.mp");
        Categoria cat1 = new Categoria(1,"Saludable",TipoItem.COMIDA);
        Plato plato1 = new Plato(1,"Ensalada vegana","Ensalada con quinoa, espinaca y aguacate",5000,cat1,300,450,true,true,true);
        PedidoDetalle pd = new PedidoDetalle(plato1, 2);
        cliente1.iniciarPedido(33, vendedor1);
        cliente1.agregarProducto(pd);
        
        Pedido pedido1 = cliente1.getPedidoActual();
        pedidoController.crearPedido(pedido1, TipoDePago.MERCADOPAGO, cliente1);

        Pedido pedidoAntesDeEliminar = pedidoController.buscarPedido(33);
        assertNotNull(pedidoAntesDeEliminar);

        pedidoController.eliminarPedido(33);

        // Verificar que el pedido ha sido eliminado
        Pedido pedidoEliminado = pedidoController.buscarPedido(33);
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
        cliente1.iniciarPedido(34, vendedor1);
        cliente1.agregarProducto(pd);
        
        Pedido pedido1 = cliente1.getPedidoActual();
        pedidoController.crearPedido(pedido1, TipoDePago.MERCADOPAGO, cliente1);

        Pedido pedidoBuscado = pedidoController.buscarPedido(pedido1.getId_pedido());

        // Verificar que el pedido fue encontrado
        assertNotNull(pedidoBuscado);
        
        assertEquals(pedido1.getId_pedido(), pedidoBuscado.getId_pedido());
        assertEquals(TipoDePago.MERCADOPAGO, pedidoBuscado.getTipoPago());
    }
    
    @Test
    void testBuscarPedidosPorIdVendedor() throws SQLException {
        
        Coordenada c1 = new Coordenada(54.0, 35.0);
        Vendedor vendedor1 = new Vendedor(1, "Mateo", "La Rioja 3240", c1);
        Cliente cliente1 = new Cliente(3, 123456, "Lautaro", "c001@gmail.com", "Corrientes 2766", c1, (long) 8, "C1.mp");
        Categoria cat1 = new Categoria(1, "Saludable", TipoItem.COMIDA);
        Plato plato1 = new Plato(1, "Ensalada vegana", "Ensalada con quinoa, espinaca y aguacate", 5000, cat1, 300, 450, true, true, true);
        PedidoDetalle pd = new PedidoDetalle(plato1, 2);

        cliente1.iniciarPedido(35, vendedor1);
        cliente1.agregarProducto(pd);
        Pedido pedido1 = cliente1.getPedidoActual();
        pedidoController.crearPedido(pedido1, TipoDePago.MERCADOPAGO, cliente1);

        // Buscar pedidos por id del vendedor
        List<Pedido> pedidosDelVendedor = pedidoController.listarPedidosVendedor(vendedor1.getId());

        // Verificar que los pedidos están asociados al vendedor correcto
        assertNotNull(pedidosDelVendedor);
        assertFalse(pedidosDelVendedor.isEmpty());
        for (Pedido pedido : pedidosDelVendedor) {
            assertEquals(vendedor1.getId(), pedido.getVendedor().getId());
        }
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
        cliente1.iniciarPedido(36, vendedor1);
        cliente1.agregarProducto(pd);
        Pedido pedido1 = cliente1.getPedidoActual();
        pedidoController.crearPedido(pedido1, TipoDePago.MERCADOPAGO, cliente1);

        // Buscar pedidos por id del cliente
        List<Pedido> pedidosDelCliente = pedidoController.listarPedidosCliente(cliente1.getId());

        // Verificar que los pedidos están asociados al cliente correcto
        assertNotNull(pedidosDelCliente);
        assertFalse(pedidosDelCliente.isEmpty());
        for (Pedido pedido : pedidosDelCliente) {
            assertEquals(cliente1.getId(), pedido.getCliente().getId());
        }
    }

}

