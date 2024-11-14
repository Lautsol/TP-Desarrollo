package isi.deso.desarrollotrabajoprueba;

import isi.deso.desarrollotrabajopractico.Cliente;
import isi.deso.desarrollotrabajopractico.DAOS.ClienteMySQLDAO;
import isi.deso.desarrollotrabajopractico.DAOS.PedidoMySQLDAO;
import isi.deso.desarrollotrabajopractico.DAOS.FactoryDAO;
import isi.deso.desarrollotrabajopractico.DAOS.VendedorMySQLDAO;
import isi.deso.desarrollotrabajopractico.Estado;
import isi.deso.desarrollotrabajopractico.Pedido;
import isi.deso.desarrollotrabajopractico.TipoDePago;
import isi.deso.desarrollotrabajopractico.Vendedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PedidoTest {
    
    PedidoMySQLDAO pedidoDAO;
    ClienteMySQLDAO clienteDAO;
    VendedorMySQLDAO vendedorDAO;
    Connection connection;
    Connection connection1;
    Connection connection2;
    
        
    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        pedidoDAO = (PedidoMySQLDAO) FactoryDAO.getPedidoDAO();
        clienteDAO = (ClienteMySQLDAO) FactoryDAO.getClienteDAO();
        vendedorDAO = (VendedorMySQLDAO) FactoryDAO.getVendedorDAO();
        connection = pedidoDAO.getConnection();
        connection1 = clienteDAO.getConnection();
        connection2 = vendedorDAO.getConnection();
        limpiarTablaPedidos();
        limpiarTablaClientes();
        limpiarTablaVendedores();
    }   
        
    @AfterEach
    void tearDown() throws SQLException {
        limpiarTablaPedidos();
        limpiarTablaClientes();
        limpiarTablaVendedores();
        connection.close();
        connection1.close();
        connection2.close();
    }    
    
    // Método para limpiar la tabla de pedidos
    private void limpiarTablaPedidos() throws SQLException {
        String sql = "DELETE FROM grupo11.pedidos";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }
    
    // Método para limpiar la tabla de clientes
    private void limpiarTablaClientes() throws SQLException {
        String sql = "DELETE FROM grupo11.clientes";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }
    
    // Método para limpiar la tabla de vendedores
    private void limpiarTablaVendedores() throws SQLException {
        String sql = "DELETE FROM grupo11.vendedores";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }
    
    @Test 
    void testCrearPedido() {
        
        Cliente cliente = new Cliente(20, 1234, "Juan Pérez", "jperez@gmail.com", "La Rioja 1250", null, 1222L, "juan01");
        
        clienteDAO.crearCliente(cliente);
        
        // Verificar que el cliente fue insertado en la base de datos
        Cliente clienteRecuperado = clienteDAO.buscarClientePorID(20);
        assertNotNull(clienteRecuperado);
        
        Vendedor vendedor = new Vendedor(20, "Pedro Sánchez", "San Juan 1200", null);
        
        vendedorDAO.crearVendedor(vendedor);
        
        // Verificar que el vendedor fue insertado en la base de datos
        Vendedor vendedorRecuperado = vendedorDAO.buscarVendedorPorID(20);
        assertNotNull(vendedorRecuperado);
        
        Pedido pedido = new Pedido(20, cliente, vendedor);
        pedido.setTotal(22);
        pedido.setTipoPago(TipoDePago.MERCADOPAGO);
        pedido.setEstado(Estado.RECIBIDO);
        
        pedidoDAO.crearPedido(pedido);
        
        // Verificar que el pedido fue insertado en la base de datos
        Pedido pedidoRecuperado = pedidoDAO.buscarPedidoPorID(20);
        assertNotNull(pedidoRecuperado);
        assertEquals(pedidoRecuperado.getCliente().getId(), pedido.getCliente().getId());
        assertEquals(pedidoRecuperado.getVendedor().getId(), pedido.getVendedor().getId());
        assertEquals(pedidoRecuperado.getTotal(), pedido.getTotal());
        assertEquals(pedidoRecuperado.getTipoPago(), pedido.getTipoPago());
        assertEquals(pedidoRecuperado.getEstado(), pedido.getEstado());
        
    }
    
    @Test
    void testActualizarPedido() {
        
        Cliente cliente = new Cliente(20, 1234, "Juan Pérez", "jperez@gmail.com", "La Rioja 1250", null, 1222L, "juan01");
        
        clienteDAO.crearCliente(cliente);
        
        // Verificar que el cliente fue insertado en la base de datos
        Cliente clienteRecuperado = clienteDAO.buscarClientePorID(20);
        assertNotNull(clienteRecuperado);
        
        Vendedor vendedor = new Vendedor(20, "Pedro Sánchez", "San Juan 1200", null);
        
        vendedorDAO.crearVendedor(vendedor);
        
        // Verificar que el vendedor fue insertado en la base de datos
        Vendedor vendedorRecuperado = vendedorDAO.buscarVendedorPorID(20);
        assertNotNull(vendedorRecuperado);
        
        Pedido pedido = new Pedido(20, cliente, vendedor);
        pedido.setTotal(22);
        pedido.setTipoPago(TipoDePago.MERCADOPAGO);
        pedido.setEstado(Estado.RECIBIDO);
        
        pedidoDAO.crearPedido(pedido);
        
        // Verificar que el pedido fue insertado en la base de datos
        Pedido pedidoRecuperado = pedidoDAO.buscarPedidoPorID(20);
        assertNotNull(pedidoRecuperado);
        
        
        Pedido pedidoActualizado = new Pedido(20, cliente, vendedor);
        pedidoActualizado.setTotal(40);
        pedidoActualizado.setTipoPago(TipoDePago.MERCADOPAGO);
        pedidoActualizado.setEstado(Estado.RECIBIDO);
        
        pedidoDAO.actualizarPedido(pedidoActualizado);
        
        pedidoRecuperado = pedidoDAO.buscarPedidoPorID(20);
        assertEquals(pedidoRecuperado.getCliente().getId(), pedidoActualizado.getCliente().getId());
        assertEquals(pedidoRecuperado.getVendedor().getId(), pedidoActualizado.getVendedor().getId());
        assertEquals(pedidoRecuperado.getTotal(), pedidoActualizado.getTotal());
        assertEquals(pedidoRecuperado.getTipoPago(), pedidoActualizado.getTipoPago());
        assertEquals(pedidoRecuperado.getEstado(), pedidoActualizado.getEstado());
        
    }
    
    @Test
    void testEliminarPedido() {
        
        Cliente cliente = new Cliente(20, 1234, "Juan Pérez", "jperez@gmail.com", "La Rioja 1250", null, 1222L, "juan01");
        
        clienteDAO.crearCliente(cliente);
        
        // Verificar que el cliente fue insertado en la base de datos
        Cliente clienteRecuperado = clienteDAO.buscarClientePorID(20);
        assertNotNull(clienteRecuperado);
        
        Vendedor vendedor = new Vendedor(20, "Pedro Sánchez", "San Juan 1200", null);
        
        vendedorDAO.crearVendedor(vendedor);
        
        // Verificar que el vendedor fue insertado en la base de datos
        Vendedor vendedorRecuperado = vendedorDAO.buscarVendedorPorID(20);
        assertNotNull(vendedorRecuperado);
        
        Pedido pedido = new Pedido(20, cliente, vendedor);
        pedido.setTotal(22);
        pedido.setTipoPago(TipoDePago.MERCADOPAGO);
        pedido.setEstado(Estado.RECIBIDO);
        
        pedidoDAO.crearPedido(pedido);
        
        // Verificar que el pedido fue insertado en la base de datos
        Pedido pedidoRecuperado = pedidoDAO.buscarPedidoPorID(20);
        assertNotNull(pedidoRecuperado);
        
        pedidoDAO.eliminarPedido(20);
        
        pedidoRecuperado = pedidoDAO.buscarPedidoPorID(20);
        // Verificar que el pedido fue eliminado
        assertNull(pedidoRecuperado);
        
    }
    
    @Test
    void testBuscarPedidosPorNombre() {
        
        Cliente cliente = new Cliente(20, 1234, "Juan Pérez", "jperez@gmail.com", "La Rioja 1250", null, 1222L, "juan01");
        
        clienteDAO.crearCliente(cliente);
        
        // Verificar que el cliente fue insertado en la base de datos
        Cliente clienteRecuperado = clienteDAO.buscarClientePorID(20);
        assertNotNull(clienteRecuperado);
        
        Vendedor vendedor = new Vendedor(20, "Pedro Sánchez", "San Juan 1200", null);
        
        vendedorDAO.crearVendedor(vendedor);
        
        // Verificar que el vendedor fue insertado en la base de datos
        Vendedor vendedorRecuperado = vendedorDAO.buscarVendedorPorID(20);
        assertNotNull(vendedorRecuperado);
        
        Pedido pedido1 = new Pedido(20, cliente, vendedor);
        pedido1.setTotal(22);
        pedido1.setTipoPago(TipoDePago.MERCADOPAGO);
        pedido1.setEstado(Estado.RECIBIDO);
        
        Pedido pedido2 = new Pedido(21, cliente, vendedor);
        pedido2.setTotal(40);
        pedido2.setTipoPago(TipoDePago.TRANSFERENCIA);
        pedido2.setEstado(Estado.EN_ENVIO);
        
        pedidoDAO.crearPedido(pedido1);
        pedidoDAO.crearPedido(pedido2);
        
        // Verificar que el pedido fue insertado en la base de datos
        Pedido pedidoRecuperado1 = pedidoDAO.buscarPedidoPorID(20);
        assertNotNull(pedidoRecuperado1);
        
        Pedido pedidoRecuperado2 = pedidoDAO.buscarPedidoPorID(21);
        assertNotNull(pedidoRecuperado2);
        
        ArrayList<Pedido> busquedaPedidos = pedidoDAO.buscarPedidosPorCliente(cliente.getId());
        
        assertNotNull(busquedaPedidos.get(0));
        assertNotNull(busquedaPedidos.get(1));
        assertEquals(busquedaPedidos.get(0).getCliente().getId(), pedido1.getCliente().getId());
        assertEquals(busquedaPedidos.get(1).getCliente().getId(), pedido2.getCliente().getId());
        
    }
    
    @Test
    void testBuscarPedidosPorID() {
        
        Cliente cliente = new Cliente(20, 1234, "Juan Pérez", "jperez@gmail.com", "La Rioja 1250", null, 1222L, "juan01");
        
        clienteDAO.crearCliente(cliente);
        
        // Verificar que el cliente fue insertado en la base de datos
        Cliente clienteRecuperado = clienteDAO.buscarClientePorID(20);
        assertNotNull(clienteRecuperado);
        
        Vendedor vendedor = new Vendedor(20, "Pedro Sánchez", "San Juan 1200", null);
        
        vendedorDAO.crearVendedor(vendedor);
        
        // Verificar que el vendedor fue insertado en la base de datos
        Vendedor vendedorRecuperado = vendedorDAO.buscarVendedorPorID(20);
        assertNotNull(vendedorRecuperado);
        
        Pedido pedido = new Pedido(20, cliente, vendedor);
        pedido.setTotal(22);
        pedido.setTipoPago(TipoDePago.MERCADOPAGO);
        pedido.setEstado(Estado.RECIBIDO);
        
        pedidoDAO.crearPedido(pedido);
        
        // Verificar que el pedido fue insertado en la base de datos
        Pedido pedidoRecuperado = pedidoDAO.buscarPedidoPorID(20);
        assertNotNull(pedidoRecuperado);
        
        pedidoRecuperado = pedidoDAO.buscarPedidoPorID(20);
        
        assertNotNull(pedidoRecuperado);
        assertEquals(pedidoRecuperado.getCliente().getId(), pedido.getCliente().getId());
        assertEquals(pedidoRecuperado.getVendedor().getId(), pedido.getVendedor().getId());
        assertEquals(pedidoRecuperado.getTotal(), pedido.getTotal());
        assertEquals(pedidoRecuperado.getTipoPago(), pedido.getTipoPago());
        assertEquals(pedidoRecuperado.getEstado(), pedido.getEstado());
        
    }
        
}
