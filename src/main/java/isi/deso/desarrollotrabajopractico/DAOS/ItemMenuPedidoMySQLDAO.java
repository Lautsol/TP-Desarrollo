
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.modelo.Alcohol;
import isi.deso.desarrollotrabajopractico.modelo.Gaseosa;
import isi.deso.desarrollotrabajopractico.modelo.ItemMenu;
import isi.deso.desarrollotrabajopractico.modelo.Pedido;
import isi.deso.desarrollotrabajopractico.modelo.PedidoDetalle;
import isi.deso.desarrollotrabajopractico.modelo.Plato;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemMenuPedidoMySQLDAO implements ItemMenuPedidoDAO {
    
    private static Connection con;
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/grupo11";
    private static final String USER = "root";
    private static final String PASSWORD = "grupo11";
    private static ItemMenuPedidoMySQLDAO instancia; 
    
    private ItemMenuPedidoMySQLDAO() {
    }
    
    public static ItemMenuPedidoMySQLDAO obtenerInstancia() {
        
        if(instancia == null){
            instancia = new ItemMenuPedidoMySQLDAO();
        }
        
        return instancia;
    }
    
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        
        con = null;
        try {
            Class.forName(driver);
            con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            throw e; 
        }
        return con;
    }
    
    public void agregarItemsPedido(Pedido pedido) {
        
        String sqlItemsVendedor = "INSERT INTO itemsMenuPedido (idPedido, idItem, cantidad) VALUES (?, ?, ?)";

        try (Connection connection = getConnection(); 
            PreparedStatement pstmtItemsPedido = connection.prepareStatement(sqlItemsVendedor)  
        ) {
            for (PedidoDetalle pd : pedido.getPedidoDetalle()) {
                pstmtItemsPedido.setInt(1, pedido.getId_pedido());
                pstmtItemsPedido.setInt(2, pd.getProducto().getId());
                pstmtItemsPedido.setInt(3, pd.getCantidad());
                pstmtItemsPedido.executeUpdate();
            }
        } catch (SQLException e) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, "Error al agregar ítems al pedido", e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, "Clase no encontrada", ex);
        }
    }
    
    public ArrayList<PedidoDetalle> obtenerDetallesPedido(Pedido pedido) {
        String sqlItemsVendedor = "SELECT im.idItem, im.cantidad, p.nombre, p.descripcion, p.precio, p.item, p.id_categoria " +
                                  "FROM itemsMenuPedido im " +
                                  "JOIN itemsMenu p ON im.idItem = p.id WHERE im.idPedido = ?";

        try (Connection connection = getConnection();
             PreparedStatement pstmtItemsPedido = connection.prepareStatement(sqlItemsVendedor)) {
            pstmtItemsPedido.setInt(1, pedido.getId_pedido());
            try (ResultSet rs = pstmtItemsPedido.executeQuery()) {
                while (rs.next()) {
                    int idItem = rs.getInt("idItem");
                    int cantidad = rs.getInt("cantidad");
                    String nombreProducto = rs.getString("nombre");
                    double precioProducto = rs.getDouble("precio");
                    String tipoItem = rs.getString("item");
                    int categoriaId = rs.getInt("id_categoria");

                    ItemMenu itemMenu = null;

                    switch (tipoItem) {
                        case "PLATO":
                            itemMenu = new Plato(
                                idItem,
                                nombreProducto,
                                rs.getString("descripcion"),
                                precioProducto,
                                FactoryDAO.getCategoriaDAO().buscarCategoria(categoriaId),
                                0, 0, false, false, false
                            );
                            break;
                        case "GASEOSA":
                            itemMenu = new Gaseosa(
                                idItem,
                                nombreProducto,
                                rs.getString("descripcion"),
                                precioProducto,
                                FactoryDAO.getCategoriaDAO().buscarCategoria(categoriaId),
                                0
                            );
                            break;
                        case "ALCOHOL":
                            itemMenu = new Alcohol(
                                idItem,
                                nombreProducto,
                                rs.getString("descripcion"),
                                precioProducto,
                                FactoryDAO.getCategoriaDAO().buscarCategoria(categoriaId),
                                0, 0
                            );
                            break;
                        }

                    if (itemMenu != null) {
                        PedidoDetalle pd = new PedidoDetalle(itemMenu, cantidad);
                        pedido.agregarPedidoDetalle(pd);
                    }
                }
            }
            
            return pedido.getPedidoDetalle();
            
        } catch (SQLException e) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, "Error al obtener ítems del pedido", e);
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PedidoMySQLDAO.class.getName()).log(Level.SEVERE, "Clase no encontrada", ex);
            return null;
        }
    }

}

