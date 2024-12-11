
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.modelo.ItemMenu;
import isi.deso.desarrollotrabajopractico.modelo.Vendedor;
import java.util.ArrayList;

public interface ItemMenuVendedorDAO {
    
    public void agregarItemsVendedor(Vendedor vendedor);
    public ArrayList<ItemMenu> obtenerItemsMenuDeVendedor(Vendedor vendedor);
    public void eliminarItemsVendedor(Vendedor vendedor, ArrayList<ItemMenu> items);

}
