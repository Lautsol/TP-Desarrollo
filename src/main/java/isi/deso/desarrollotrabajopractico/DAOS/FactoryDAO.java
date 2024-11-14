
package isi.deso.desarrollotrabajopractico.DAOS;

import static isi.deso.desarrollotrabajopractico.DAOS.ItemMenuMySQLDAO.obtenerInstancia;

public class FactoryDAO {
    
    public static ClienteDAO getClienteDAO() {
        return ClienteMySQLDAO.obtenerInstancia();
    }
    
    public static VendedorDAO getVendedorDAO() {
        return VendedorMySQLDAO.obtenerInstancia();
    }
    
    public static PedidoDAO getPedidoDAO() {
        return PedidoMySQLDAO.obtenerInstancia();
    }
    
    public static ItemMenuDAO getItemMenuDAO() {
        return ItemMenuMySQLDAO.obtenerInstancia();
    }
    
    public static CategoriaDAO getCategoriaDAO() {
        return CategoriaMySQLDAO.obtenerInstancia();
    }
    
    public static ItemMenuVendedorDAO getItemMenuVendedorDAO() {
        return ItemMenuVendedorMySQLDAO.obtenerInstancia();
    }
}
