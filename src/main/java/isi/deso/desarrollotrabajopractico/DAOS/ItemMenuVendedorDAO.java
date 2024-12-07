
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.ItemMenu;
import isi.deso.desarrollotrabajopractico.Vendedor;
import java.util.ArrayList;

public interface ItemMenuVendedorDAO {
    public void agregarItemsVendedor(Vendedor vendedor);
    public boolean itemMenuDeVendedor(int idVendedor, int idItem);
    public ArrayList<ItemMenu> obtenerItemsMenuDeVendedor(Vendedor vendedor);
    public void eliminarItemsVendedor(Vendedor vendedor, ArrayList<ItemMenu> items);

}
