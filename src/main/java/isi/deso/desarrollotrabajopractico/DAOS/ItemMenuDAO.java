
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.ItemMenu;
import java.util.ArrayList;

public interface ItemMenuDAO {
    
    public void crearItemMenu(ItemMenu itemMenu);
    public void actualizarItemMenu(ItemMenu itemMenu);
    public void eliminarItemMenu(int id);
    public ArrayList<ItemMenu> buscarItemsMenuPorNombre(String nombre);
    public ItemMenu buscarItemMenuPorID(int id);
    
}
