
package isi.deso.desarrollotrabajopractico.DAOS;

import isi.deso.desarrollotrabajopractico.Pedido;
import java.util.ArrayList;

public interface PedidoDAO {
        
    public void crearPedido(Pedido pedido);
    public void actualizarPedido(Pedido pedido);
    public void eliminarPedido(int id);
    public ArrayList<Pedido> buscarPedidosPorCliente(int id_cliente);
    public ArrayList<Pedido> buscarPedidosPorVendedor(int id_vendedor);
    public Pedido buscarPedidoPorID(int id);
}
