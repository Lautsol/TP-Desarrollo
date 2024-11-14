
package isi.deso.desarrollotrabajopractico.Controladores;

import isi.deso.desarrollotrabajopractico.Cliente;
import isi.deso.desarrollotrabajopractico.DAOS.ClienteMySQLDAO;
import isi.deso.desarrollotrabajopractico.DAOS.FactoryDAO;
import isi.deso.desarrollotrabajopractico.DAOS.ItemMenuMySQLDAO;
import isi.deso.desarrollotrabajopractico.DAOS.PedidoMySQLDAO;
import isi.deso.desarrollotrabajopractico.DAOS.VendedorMySQLDAO;
import isi.deso.desarrollotrabajopractico.Estado;
import isi.deso.desarrollotrabajopractico.Interfaces.CrearPedido;
import isi.deso.desarrollotrabajopractico.Interfaces.ItemsMenuPedido;
import isi.deso.desarrollotrabajopractico.Interfaces.ListaDeItemMenu;
import isi.deso.desarrollotrabajopractico.Interfaces.ListaDePedidos;
import isi.deso.desarrollotrabajopractico.Interfaces.ModificarPedido;
import isi.deso.desarrollotrabajopractico.ItemMenu;
import isi.deso.desarrollotrabajopractico.Memory.ClienteMemory;
import isi.deso.desarrollotrabajopractico.Memory.PedidosMemory;
import isi.deso.desarrollotrabajopractico.Memory.VendedorMemory;
import isi.deso.desarrollotrabajopractico.Pedido;
import isi.deso.desarrollotrabajopractico.PedidoDetalle;
import isi.deso.desarrollotrabajopractico.ProductoDeOtroVendedorException;
import isi.deso.desarrollotrabajopractico.TipoDePago;
import isi.deso.desarrollotrabajopractico.Vendedor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;

public class PedidoController implements ActionListener{
    private ListaDePedidos listaDePedidos;
    private CrearPedido interfazCrearPedido;
    private ModificarPedido interfazModificarPedido;
    private ItemsMenuPedido interfazItemsMenuPedido;
    private Pedido pedido;
    private ArrayList<PedidoDetalle> pedidosDetalles;

    public PedidoController() {
    }
    
    public PedidoController(ListaDePedidos listaDePedidos) {
        this.listaDePedidos = listaDePedidos;
    }

    public PedidoController(CrearPedido interfazCrearPedido) {
        this.interfazCrearPedido = interfazCrearPedido;
    }

    public PedidoController(ModificarPedido interfazModificarPedido) {
        this.interfazModificarPedido = interfazModificarPedido;
    }

    public void setCrearPedido(CrearPedido crearPedido) {
        this.interfazCrearPedido = crearPedido;
    }

    public void setModificarPedido(ModificarPedido modificarPedido) {
        this.interfazModificarPedido = modificarPedido;
    }
    
    public void setItemsMenuPedido(ItemsMenuPedido interfazItemsMenuPedido) {
        this.interfazItemsMenuPedido = interfazItemsMenuPedido;
    }
    
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        Object source = e.getSource();
        

        if (comando.equals("Crear nuevo pedido")) {
            interfazCrearPedido = new CrearPedido();  
            interfazCrearPedido.setControlador(this);
            setCrearPedido(interfazCrearPedido);  
        } 
        
        else if (comando.equals("Editar")) {
           interfazModificarPedido = new ModificarPedido();  
           interfazModificarPedido.setControlador(this);
           setModificarPedido(interfazModificarPedido); 
           interfazModificarPedido.getjTextField1().setEditable(false);
           interfazModificarPedido.getjTextField2().setEditable(false);
           interfazModificarPedido.getjTextField3().setEditable(false);
           interfazModificarPedido.getjTextField4().setEditable(false);
           interfazModificarPedido.getjComboBox1().setEnabled(false);
           
           if (listaDePedidos.getjTable1().isEditing()) {
            listaDePedidos.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
           }
           
           int row = listaDePedidos.getjTable1().getSelectedRow(); // Obtener la fila seleccionada
           
           int idPedido = (Integer) listaDePedidos.getModelo().getValueAt(row,0);
           int idCliente = (Integer) listaDePedidos.getModelo().getValueAt(row, 1);
           int idVendedor = (Integer) listaDePedidos.getModelo().getValueAt(row, 2);
           double total = (Double) listaDePedidos.getModelo().getValueAt(row, 3);
           TipoDePago tipoDePago = (TipoDePago) listaDePedidos.getModelo().getValueAt(row, 4);
           String formaDePago = tipoDePago.name();  
           Estado est = (Estado) listaDePedidos.getModelo().getValueAt(row, 5);
           String estado = est.name();
                    
           interfazModificarPedido.getjTextField4().setText(String.valueOf(idPedido));
           interfazModificarPedido.getjTextField1().setText(String.valueOf(idCliente)); 
           interfazModificarPedido.getjTextField2().setText(String.valueOf(idVendedor)); 
           interfazModificarPedido.getjTextField3().setText(String.valueOf(total)); 
           interfazModificarPedido.getjComboBox1().setSelectedItem(formaDePago); 
           interfazModificarPedido.getjComboBox2().setSelectedItem(estado);
          
        } 
        
        else if (source == listaDePedidos.getjTextField1()) {
            String texto = listaDePedidos.getjTextField1().getText();
            

            if(listaDePedidos.getjComboBox1().getSelectedItem().equals("ID pedido")) {
                int idPedido = Integer.parseInt(texto);
                Pedido pedido = buscarPedido(idPedido);
                
                if (pedido != null) {
                actualizarTablaConPedido(pedido);
                } 
                else {
                listaDePedidos.mostrarMensaje();
                restablecerTablaConDatosOriginales();
                }
                
            }
            
            else if(listaDePedidos.getjComboBox1().getSelectedItem().equals("ID cliente")) {
                int idCliente = Integer.parseInt(texto);
                List<Pedido> pedidos = listarPedidosCliente(idCliente);
                
                if(!pedidos.isEmpty()){
                actualizarTabla(pedidos);
                }
                else{
                listaDePedidos.mostrarMensaje();
                restablecerTablaConDatosOriginales();
                }
            }
                
            
            else {
                int idVendedor = Integer.parseInt(texto);
                List<Pedido> pedidos = listarPedidosVendedor(idVendedor);
                
                 if(!pedidos.isEmpty()){
                actualizarTabla(pedidos);
                }
                else{
                listaDePedidos.mostrarMensaje();
                restablecerTablaConDatosOriginales();
                }
            }
        }
        
        else if (comando.equals("Eliminar")) {
            
            if (listaDePedidos.getjTable1().isEditing()) {
            listaDePedidos.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
            }
            
            int row = listaDePedidos.getjTable1().getSelectedRow();
            
            int idPedido = (Integer) listaDePedidos.getModelo().getValueAt(row, 0);
            eliminarPedido(idPedido);
            listaDePedidos.getModelo().removeRow(row);
          
        }
        
        else if (comando.equals("Crear")) {
            
            if(validarCamposVacios(interfazCrearPedido)) interfazCrearPedido.mostrarMensajeCamposVacios();
            else if (!validarTiposDeDatos(interfazCrearPedido)) interfazCrearPedido.mostrarMensajeDatosInvalidos();
            else {
            int idPedido = Integer.parseInt(interfazCrearPedido.getCampoIDpedido().getText());
            int idCliente  = Integer.parseInt(interfazCrearPedido.getCampoIDcliente().getText());
            int idVendedor = Integer.parseInt(interfazCrearPedido.getCampoIDvendedor().getText());
            double total = Double.parseDouble(interfazCrearPedido.getCampoTotal().getText());
            String formaDePago = (String)interfazCrearPedido.getCampoFormaDePago().getSelectedItem();
            String estado = (String)interfazCrearPedido.getCampoEstado().getSelectedItem();
              
            if(verificarID(idPedido)) interfazCrearPedido.mostrarMensajeID();
            else if(!verificarCliente(idCliente)) interfazCrearPedido.mostrarMensajeCliente();
            else if(!verificarVendedor(idVendedor)) interfazCrearPedido.mostrarMensajeVendedor();
            else{
            Cliente cliente = new ClienteController().buscarCliente(idCliente);
            Vendedor vendedor = new VendedorController().buscarPorIdVendedor(idVendedor);
            
            if(cliente.getAlias() == null && formaDePago.equals("MERCADOPAGO")) {
                interfazCrearPedido.mostrarMensajeAlias();
            }
            else if (cliente.getCbu() == 0 && formaDePago.equals("TRANSFERENCIA")) {
                
                interfazCrearPedido.mostrarMensajeCbu();
            }
            else if (pedidosDetalles == null || pedidosDetalles.isEmpty()) {
                interfazCrearPedido.mostrarMensajePedidoVacio();
            }
            else {
                try {
                crearPedido(idPedido, cliente,  vendedor, (TipoDePago.valueOf(formaDePago)), pedidosDetalles);
                listaDePedidos.agregarPedidoALaTabla(idPedido, idCliente,  idVendedor, pedido.calcularPrecioFinal(), formaDePago, estado); 
                interfazCrearPedido.setearCamposEnBlanco();
                } catch (ProductoDeOtroVendedorException e1) {
                    interfazCrearPedido.mostrarMensajeProductoOtroVendedor();
                    interfazCrearPedido.dispose();
                }
             }
           }
         }
       }
        
        else if (comando.equals("Agregar items")) {
            
                interfazItemsMenuPedido = new ItemsMenuPedido(this);
                setItemsMenuPedido(interfazItemsMenuPedido);
                cargarDatosOriginalesEnTablaItems();
                pedidosDetalles = new ArrayList<>();
        }
        
        else if (comando.equals("Agregar")) {
            
            if (interfazItemsMenuPedido.getjTable1().isEditing()) {
            interfazItemsMenuPedido.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
            }
           
            int row = interfazItemsMenuPedido.getjTable1().getSelectedRow(); // Obtener la fila seleccionada
           
            if(interfazItemsMenuPedido.getModelo().getValueAt(row, 6) != null) {
                
                int id = (Integer) interfazItemsMenuPedido.getModelo().getValueAt(row, 0);
                int cantidad = (Integer) interfazItemsMenuPedido.getModelo().getValueAt(row, 6);
                ItemMenu item = FactoryDAO.getItemMenuDAO().buscarItemMenuPorID(id);
            
                PedidoDetalle pedidoDetalle = new PedidoDetalle(item, cantidad);
                pedidosDetalles.add(pedidoDetalle);
        }
    }
        
        else if (comando.equals("CancelarCrear")) {
            
                interfazCrearPedido.dispose();
                
        }
        
        else if (comando.equals("Guardar")) {
            
            if(validarCamposVaciosModificar(interfazModificarPedido)) interfazModificarPedido.mostrarMensajeCamposVacios();
            else if (!validarTiposDeDatosModificar(interfazModificarPedido)) interfazModificarPedido.mostrarMensajeDatosInvalidos();
            else {
            int row = listaDePedidos.getjTable1().getSelectedRow();
            int idPedido = Integer.parseInt(interfazModificarPedido.getjTextField4().getText());
            int idCliente  = Integer.parseInt(interfazModificarPedido.getjTextField1().getText());
            int idVendedor = Integer.parseInt(interfazModificarPedido.getjTextField2().getText());
            double total = Double.parseDouble(interfazModificarPedido.getjTextField3().getText());
            String formaDePago = (String)interfazModificarPedido.getjComboBox1().getSelectedItem();
            String estado = (String)interfazModificarPedido.getjComboBox2().getSelectedItem();
            
            Cliente cliente = new ClienteController().buscarCliente(idCliente);
            Vendedor vendedor = new VendedorController().buscarPorIdVendedor(idVendedor);
            
            actualizarPedido(idPedido, cliente,  vendedor, total, formaDePago,  estado);
            listaDePedidos.getModelo().setValueAt(idPedido, row, 0);
            listaDePedidos.getModelo().setValueAt(idCliente,row,1);
            listaDePedidos.getModelo().setValueAt(idVendedor,row,2);
            listaDePedidos.getModelo().setValueAt(total,row,3);
            listaDePedidos.getModelo().setValueAt(formaDePago,row,4);
            listaDePedidos.getModelo().setValueAt(estado,row,5);
            
        }
    }
        
        else if (comando.equals("CancelarModificar")){
                    interfazModificarPedido.dispose();
        }
        
        else if(comando.equals("Reiniciar la búsqueda")) {
            restablecerTablaConDatosOriginales();
        }

  }
    
    private void actualizarTabla(List<Pedido> pedidos) {
        // Limpia la tabla actual
        listaDePedidos.getModelo().setRowCount(0); 

        // Agregar los pedidos a la tabla
        for (Pedido pedido : pedidos) {
            Object[] rowData = {
                pedido.getId_pedido(),
                pedido.getCliente().getId(),
                pedido.getVendedor().getId(),
                pedido.getTotal(),
                pedido.getTipoPago(),
                pedido.getEstado()
            };
            listaDePedidos.getModelo().addRow(rowData);
        }
    }
    
   private void actualizarTablaConPedido(Pedido pedido) {
        // Limpia la tabla actual
        listaDePedidos.getModelo().setRowCount(0); 

        // Agregar el pedido a la tabla
        Object[] rowData = {
            pedido.getId_pedido(),
            pedido.getCliente().getId(),
            pedido.getVendedor().getId(),
            pedido.getTotal(),
            pedido.getTipoPago(),
            pedido.getEstado()
        };
        listaDePedidos.getModelo().addRow(rowData);
    }
            
    private void restablecerTablaConDatosOriginales() {
        listaDePedidos.getModelo().setRowCount(0); // Limpiar la tabla actual
        cargarDatosOriginalesEnTabla(); // Volver a cargar los datos originales
    }
    
    public void cargarDatosOriginalesEnTabla() {
        
        /*
            for (Pedido pedido : PedidosMemory.listaPedidos) {
                Object[] rowData = {
                pedido.getId_pedido(),
                pedido.getCliente().getId(),
                pedido.getVendedor().getId(),
                pedido.getTotal(),
                pedido.getTipoPago(),
                pedido.getEstado()
            };
        
            listaDePedidos.getModelo().addRow(rowData);
        }
        */
        
            PedidoMySQLDAO pedidoMySQLDAO = (PedidoMySQLDAO) FactoryDAO.getPedidoDAO();
            for (Pedido pedido : pedidoMySQLDAO.obtenerTodosLosPedidos()) {
                Object[] rowData = {
                pedido.getId_pedido(),
                pedido.getCliente().getId(),
                pedido.getVendedor().getId(),
                pedido.getTotal(),
                pedido.getTipoPago(),
                pedido.getEstado()
            };
        
            listaDePedidos.getModelo().addRow(rowData);
        }
    }
     
    public void actualizarPedido(int idPedido, Cliente cliente, Vendedor vendedor, double total, String formaDePago, String estado) {
        
        /*
        for (Pedido pedido : PedidosMemory.listaPedidos) {
            if (pedido.getId_pedido() == idPedido) {
                pedido.setTotal(total);
                pedido.setTipoPago(TipoDePago.valueOf(formaDePago));
                pedido.setEstado(Estado.valueOf(estado));
            }
        }
        */
        Pedido pedido = new Pedido();
        pedido.setId_pedido(idPedido);
        pedido.setTotal(total);
        pedido.setTipoPago(TipoDePago.valueOf(formaDePago));
        pedido.setEstado(Estado.valueOf(estado));
        
        FactoryDAO.getPedidoDAO().actualizarPedido(pedido);
    }
     
    public void crearPedido(int idPedido, Cliente cliente, Vendedor vendedor, TipoDePago formaDePago, ArrayList<PedidoDetalle> pedidosDetalles) throws ProductoDeOtroVendedorException {
        
        pedido = new Pedido();
        
        pedido.setId_pedido(idPedido);
        pedido.setCliente(cliente);
        pedido.setVendedor(vendedor);
        pedido.setPedidoDetalle(pedidosDetalles);
        
        if(formaDePago == TipoDePago.TRANSFERENCIA) {
            pedido.generarTransferencia();
        }
        else pedido.generarMercadoPago();
        
        pedido.setTotal(pedido.calcularPrecioFinal());
        pedido.setTipoPago(formaDePago);
        pedido.setEstado(Estado.EN_PROCESO);

        // PedidosMemory.listaPedidos.add(pedido);  
        FactoryDAO.getPedidoDAO().crearPedido(pedido);
        
        pedidosDetalles = null;
    }
    
    public void eliminarPedido(int id_pedido) {
        
        /*
        Iterator<Pedido> iterador = PedidosMemory.listaPedidos.iterator();
        while (iterador.hasNext()) {
            Pedido p = iterador.next();
            if (p.getId_pedido() == id_pedido) {
                iterador.remove();
            }
        }
        */
        
        FactoryDAO.getPedidoDAO().eliminarPedido(id_pedido);
    }
    
    public Pedido buscarPedido(int id_pedido) {
        
        /*
        for (Pedido pedido : PedidosMemory.listaPedidos) {
            if (pedido.getId_pedido() == id_pedido) {
                return pedido;
            }
        }
        return null;  
        */
        
        return FactoryDAO.getPedidoDAO().buscarPedidoPorID(id_pedido);
    }
     
    public ArrayList<Pedido> listarPedidosCliente(int id_cliente) {
        
        /*
        ArrayList<Pedido> pedidoFiltrados = new ArrayList<>();
        for (Pedido pedido : PedidosMemory.listaPedidos) {
            if (pedido.getCliente().getId() == id_cliente) {
                pedidoFiltrados.add(pedido);
            }
        }
        return pedidoFiltrados;
        */
        
        return FactoryDAO.getPedidoDAO().buscarPedidosPorCliente(id_cliente);
    }
    
     public ArrayList<Pedido> listarPedidosVendedor(int id_vendedor) {
         
        /*
        ArrayList<Pedido> pedidoFiltrados = new ArrayList<>();
        for (Pedido pedido : PedidosMemory.listaPedidos) {
            if (pedido.getVendedor().getId() == id_vendedor) {
                pedidoFiltrados.add(pedido);
            }
        }
        return pedidoFiltrados;
        */
        
        return FactoryDAO.getPedidoDAO().buscarPedidosPorVendedor(id_vendedor);
    }
     
     
    private boolean validarCamposVacios(CrearPedido interfaz) {
        
        boolean vacio = false;
        
        if(interfaz.getCampoIDpedido().getText().trim().isEmpty() || 
           interfaz.getCampoIDvendedor().getText().trim().isEmpty() ||
           interfaz.getCampoIDcliente().getText().trim().isEmpty() ||
           interfaz.getCampoTotal().getText().trim().isEmpty()) vacio = true;
        
        return vacio;
    }
    
    private boolean validarTiposDeDatos(CrearPedido interfaz) {
        
        boolean correcto = true;
        
        if(!interfaz.getCampoIDpedido().getText().matches("\\d+") || 
           !interfaz.getCampoIDvendedor().getText().matches("\\d+") ||
           !interfaz.getCampoIDcliente().getText().matches("\\d+") ||
           !interfaz.getCampoTotal().getText().matches("-?\\d+(\\.\\d+)?")) correcto = false;
        
        return correcto;
    }
    
     private boolean validarCamposVaciosModificar(ModificarPedido interfaz) {
        
        boolean vacio = false;
        
        if(interfaz.getjTextField3().getText().trim().isEmpty()) vacio = true;
        
        return vacio;
    }
    
    private boolean validarTiposDeDatosModificar(ModificarPedido interfaz) {
        
        boolean correcto = true;
        
        if(!interfaz.getjTextField3().getText().matches("-?\\d+(\\.\\d+)?")) correcto = false;
        
        return correcto;
    }
    
    private boolean verificarID(int id_pedido) {
        /*
        boolean existe = false;
        
        for (Pedido pedido : PedidosMemory.listaPedidos) {
            if(pedido.getId_pedido() == id_pedido) existe = true; 
        }
        
        return existe;
        */
        
        PedidoMySQLDAO pedidoMySQLDAO = (PedidoMySQLDAO) FactoryDAO.getPedidoDAO();
        return pedidoMySQLDAO.existeID(id_pedido);
    }
    
    private boolean verificarCliente(int id_cliente) {
        
        /*
        boolean existe = false;
        
        for (Cliente cliente : ClienteMemory.listaClientes) {
            if(cliente.getId() == id_cliente) existe = true;
        }
        
        return existe;
        */
        
        ClienteMySQLDAO clienteMySQLDAO = (ClienteMySQLDAO) FactoryDAO.getClienteDAO();
        return clienteMySQLDAO.existeID(id_cliente);
    }
    
    private boolean verificarVendedor(int id_vendedor) {
        
        /*
        boolean existe = false;
        
        for (Vendedor vendedor : VendedorMemory.listaVendedores) {
            if(vendedor.getId() == id_vendedor) existe = true;
        }
        
        return existe;
        */
        
        VendedorMySQLDAO vendedorMySQLDAO = (VendedorMySQLDAO) FactoryDAO.getVendedorDAO();
        return vendedorMySQLDAO.existeID(id_vendedor);
    }
    
    public void cargarDatosOriginalesEnTablaItems() {
        
        /*
        for (ItemMenu itemMenu : ItemMenuMemory.listaItemMenu) {
                Object[] rowData = {
                itemMenu.getId(),
                itemMenu.getCategoria().getTipo_item().toString(),
                itemMenu.getNombre(),
                itemMenu.getDescripcion(),
                itemMenu.getPrecio(),
                itemMenu.getCategoria().getId()
            };
        
            listaDeItemMenu.getModelo().addRow(rowData);
        }
        */
        
        ItemMenuMySQLDAO itemMenuMySQLDAO = (ItemMenuMySQLDAO) FactoryDAO.getItemMenuDAO();
        for (ItemMenu itemMenu : itemMenuMySQLDAO.obtenerTodosLosItemsMenu()) {
                Object[] rowData = {
                itemMenu.getId(),
                itemMenu.getCategoria().getTipo_item().toString(),
                itemMenu.getNombre(),
                itemMenu.getDescripcion(),
                itemMenu.getPrecio(),
                itemMenu.getCategoria().getId()
            };
        
            interfazItemsMenuPedido.getModelo().addRow(rowData);
        }
    }
}
