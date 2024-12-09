
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.modelo.Pedido;
import isi.deso.desarrollotrabajopractico.modelo.PedidoDetalle;
import java.util.ArrayList;

public interface ItemMenuPedidoDAO {
    
    public void agregarItemsPedido(Pedido pedido);
    public ArrayList<PedidoDetalle> obtenerDetallesPedido(Pedido pedido);
}
