
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.Vendedor;

public interface ItemMenuVendedorDAO {
    public void agregarItemsVendedor(Vendedor vendedor);
    public boolean itemMenuDeVendedor(int idVendedor, int idItem);
}
