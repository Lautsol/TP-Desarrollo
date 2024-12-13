
package isi.deso.desarrollotrabajopractico.Controladores;

import isi.deso.desarrollotrabajopractico.modelo.Cliente;
import isi.deso.desarrollotrabajopractico.DAOS.FactoryDAO;
import isi.deso.desarrollotrabajopractico.DAOS.ItemMenuVendedorMySQLDAO;
import isi.deso.desarrollotrabajopractico.DAOS.PedidoMySQLDAO;
import isi.deso.desarrollotrabajopractico.modelo.Estado;
import isi.deso.desarrollotrabajopractico.Interfaces.CrearPedido;
import isi.deso.desarrollotrabajopractico.Interfaces.ItemsMenuPedido;
import isi.deso.desarrollotrabajopractico.Interfaces.ListaDePedidos;
import isi.deso.desarrollotrabajopractico.Interfaces.ModificarPedido;
import isi.deso.desarrollotrabajopractico.Interfaces.VerItems;
import isi.deso.desarrollotrabajopractico.modelo.ItemMenu;
import isi.deso.desarrollotrabajopractico.modelo.Pedido;
import isi.deso.desarrollotrabajopractico.modelo.PedidoDetalle;
import isi.deso.desarrollotrabajopractico.modelo.TipoDePago;
import isi.deso.desarrollotrabajopractico.modelo.Vendedor;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class PedidoController implements ActionListener, WindowListener {
    private ListaDePedidos listaDePedidos;
    private CrearPedido interfazCrearPedido;
    private ModificarPedido interfazModificarPedido;
    private ItemsMenuPedido interfazItemsMenuPedido;
    private VerItems interfazVerItemsPedido;
    private ArrayList<PedidoDetalle> pedidoDetalles = new ArrayList<>();
    private Cliente cliente;

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
        interfazCrearPedido.addWindowListener(this);
    }

    public void setModificarPedido(ModificarPedido modificarPedido) {
        this.interfazModificarPedido = modificarPedido;
    }
    
    public void setItemsMenuPedido(ItemsMenuPedido interfazItemsMenuPedido) {
        this.interfazItemsMenuPedido = interfazItemsMenuPedido;
    }
    
    public void setItemsPedido(VerItems interfazVerItemsPedido) {
        this.interfazVerItemsPedido = interfazVerItemsPedido;
    }
    
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        Object source = e.getSource();
        
        if(comando.equals("Crear nuevo pedido")) {
            
            interfazCrearPedido = new CrearPedido();  
            interfazCrearPedido.setControlador(this);
            setCrearPedido(interfazCrearPedido);  
            pedidoDetalles = new ArrayList<>();
            interfazCrearPedido.getCampoEstado().setEnabled(false);
        } 
        
        else if(comando.equals("Editar")) {
            
            if(listaDePedidos.getjTable1().isEditing()) {
             listaDePedidos.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
            }

            int row = listaDePedidos.getjTable1().getSelectedRow(); 
            
            interfazModificarPedido = new ModificarPedido();  
            interfazModificarPedido.setControlador(this);
            setModificarPedido(interfazModificarPedido);     
            interfazModificarPedido.getjTextField1().setEditable(false);
            interfazModificarPedido.getjTextField2().setEditable(false);
            interfazModificarPedido.getjTextField3().setEditable(false);
            interfazModificarPedido.getjTextField4().setEditable(false);
            interfazModificarPedido.getjTextField5().setEditable(false);
            interfazModificarPedido.getjComboBox1().setEnabled(false);

            int idPedido = (Integer) listaDePedidos.getModelo().getValueAt(row,0);
            Pedido pedido = FactoryDAO.getPedidoDAO().buscarPedidoPorID(idPedido);
            Object idCliente = listaDePedidos.getModelo().getValueAt(row, 1);
            Object idVendedor = listaDePedidos.getModelo().getValueAt(row, 2);
            double total = pedido.getTotal();
            Object formaDePagoObj = listaDePedidos.getModelo().getValueAt(row, 4);
            String formaDePagoStr = formaDePagoObj.toString();  
            Object estadoObj = listaDePedidos.getModelo().getValueAt(row, 5);
            String estadoStr = estadoObj.toString();
            
            if(estadoStr.equals("RECIBIDO")) {
                interfazModificarPedido.getjComboBox2().addItem("RECIBIDO");
            }
            
            if(estadoStr.equals("RECIBIDO") || idCliente.equals("No disponible") ||
              idVendedor.equals("No disponible")) {
                interfazModificarPedido.getjLabel().setText("Ver pedido");
                interfazModificarPedido.getjButton1().setEnabled(false);
                interfazModificarPedido.getjButton2().setText("Cerrar");
                interfazModificarPedido.getjComboBox2().setEnabled(false);
            }
           
            double recargo = 0;
            if(formaDePagoStr.equals("MERCADOPAGO")) recargo = pedido.getTotal() * 0.04;
            else if(formaDePagoStr.equals("TRANSFERENCIA")) recargo = pedido.getTotal() * 0.02;
                
            interfazModificarPedido.getjTextField4().setText(String.valueOf(idPedido));
            interfazModificarPedido.getjTextField1().setText(String.valueOf(idCliente)); 
            interfazModificarPedido.getjTextField2().setText(String.valueOf(idVendedor)); 
            interfazModificarPedido.getjTextField3().setText(String.valueOf(total)); 
            interfazModificarPedido.getjComboBox1().setSelectedItem(formaDePagoStr); 
            interfazModificarPedido.getjTextField5().setText(String.valueOf(recargo));
            interfazModificarPedido.getjComboBox2().setSelectedItem(estadoStr);
        } 
        
        else if(source == listaDePedidos.getjTextField1()) {
            
            String texto = listaDePedidos.getjTextField1().getText();
            
            if(listaDePedidos.getjComboBox1().getSelectedItem().equals("ID pedido")) {
                int idPedido = Integer.parseInt(texto);
                Pedido pedido = buscarPedido(idPedido);
                
                if(pedido != null) {
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
        
        else if(comando.equals("Eliminar")) {
            
            if(listaDePedidos.getjTable1().isEditing()) {
            listaDePedidos.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
            }
            
            if(listaDePedidos.confirmarAccion()) {
                int row = listaDePedidos.getjTable1().getSelectedRow();

                int idPedido = (Integer) listaDePedidos.getModelo().getValueAt(row, 0);
                eliminarPedido(idPedido);
                listaDePedidos.getModelo().removeRow(row);
            }
          
        }
        
        else if(comando.equals("Crear")) {
            
            if(validarCamposVacios(interfazCrearPedido)) interfazCrearPedido.mostrarMensajeCamposVacios();
            else if(!validarTiposDeDatos(interfazCrearPedido)) interfazCrearPedido.mostrarMensajeDatosInvalidos();
            else {
            int idCliente  = Integer.parseInt(interfazCrearPedido.getCampoIDcliente().getText());
            int idVendedor = Integer.parseInt(interfazCrearPedido.getCampoIDvendedor().getText());
            String formaDePago = (String)interfazCrearPedido.getCampoFormaDePago().getSelectedItem();
            
            Border borde = interfazCrearPedido.getBordeTexto();
            
            if(!verificarCliente(idCliente)) {
                interfazCrearPedido.getCampoIDcliente().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                interfazCrearPedido.mostrarMensajeCliente();
            } else interfazCrearPedido.getCampoIDcliente().setBorder(borde);
            
            if(!verificarVendedor(idVendedor)) {
                interfazCrearPedido.getCampoIDvendedor().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                interfazCrearPedido.mostrarMensajeVendedor();
            } else interfazCrearPedido.getCampoIDvendedor().setBorder(borde);
            
            if(verificarCliente(idCliente) && verificarVendedor(idVendedor)) {
            cliente = new ClienteController().buscarCliente(idCliente);
            Vendedor vendedor = new VendedorController().buscarPorIdVendedor(idVendedor);
            
            if(cliente.getAlias() == null && formaDePago.equals("MERCADOPAGO")) {
                    interfazCrearPedido.mostrarMensajeAlias();
            }
            else if(cliente.getCbu() == 0 && formaDePago.equals("TRANSFERENCIA")) {
                interfazCrearPedido.mostrarMensajeCbu();
            }
            else if(pedidoDetalles == null || pedidoDetalles.isEmpty()) {
            interfazCrearPedido.mostrarMensajePedidoVacio();
            }
            else if(interfazCrearPedido.confirmarAccion()) {
                    cliente.iniciarPedido(vendedor);

                    for(PedidoDetalle pd : pedidoDetalles) {
                        cliente.agregarProducto(pd); 
                    }

                    crearPedido(cliente.getPedidoActual(), TipoDePago.valueOf(formaDePago), cliente);
                    restablecerTablaConDatosOriginales();                
                    pedidoDetalles = null;
                    interfazCrearPedido.dispose();
                }
             }
           }
        }
       
        else if(comando.equals("Agregar items")) {
            
            if(validarCamposVacios(interfazCrearPedido)) interfazCrearPedido.mostrarMensajeCamposVacios();
            else if(!validarTiposDeDatos(interfazCrearPedido)) interfazCrearPedido.mostrarMensajeDatosInvalidos();
            else {
            
            Border borde = interfazCrearPedido.getBordeTexto();
            
            if(!verificarVendedor(Integer.parseInt(interfazCrearPedido.getCampoIDvendedor().getText()))) {
                
                    interfazCrearPedido.getCampoIDvendedor().setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.RED),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                    interfazCrearPedido.mostrarMensajeVendedor();
                    
                } else interfazCrearPedido.getCampoIDvendedor().setBorder(borde);
            
            if(verificarVendedor(Integer.parseInt(interfazCrearPedido.getCampoIDvendedor().getText()))) {
                
                    int idVendedor = Integer.parseInt(interfazCrearPedido.getCampoIDvendedor().getText());
                    interfazCrearPedido.getCampoIDvendedor().setEditable(false);
                    interfazItemsMenuPedido = new ItemsMenuPedido(this);
                    setItemsMenuPedido(interfazItemsMenuPedido);
                    interfazItemsMenuPedido.getjLabel1().setText("Agregar items al pedido");
                    cargarDatosOriginalesEnTablaItems(idVendedor);
                }
            }
        }
        
        else if(comando.equals("Agregar")) {
            
            if(interfazItemsMenuPedido.getjTable1().isEditing()) {
            interfazItemsMenuPedido.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
            }
           
            int row = interfazItemsMenuPedido.getjTable1().getSelectedRow(); // Obtener la fila seleccionada
           
            if(interfazItemsMenuPedido.getModelo().getValueAt(row, 6) == null) interfazItemsMenuPedido.mostrarMensajeError("La cantidad debe ser mayor a 0.");
            else {
            
            int id = (Integer) interfazItemsMenuPedido.getModelo().getValueAt(row, 0);
            int cantidad = (Integer) interfazItemsMenuPedido.getModelo().getValueAt(row, 6);
                
            if(cantidad <= 0) interfazItemsMenuPedido.mostrarMensajeError("La cantidad debe ser mayor a 0.");
            
            else {
                
                ItemMenu item = (new ItemMenuController()).buscarItemMenu(id);
            
                PedidoDetalle pedidoDetalle = new PedidoDetalle(item, cantidad);
                
                boolean agregado = false;
                for(PedidoDetalle pd : pedidoDetalles) {
                    if(pd.getProducto().getId() == item.getId()) {
                        agregado = true;
                        pd.setCantidad(pd.getCantidad() + cantidad);
                        break; 
                    }
                }
                
                if(!agregado) {
                    pedidoDetalles.add(pedidoDetalle);
            }

            interfazItemsMenuPedido.mostrarMensajeExitoso();
            
            }
        }
    }
        
        else if(comando.equals("Ver detalles del pedido")) {
            
            interfazVerItemsPedido = new VerItems();
            interfazVerItemsPedido.setControladorPedido(this);
            setItemsPedido(interfazVerItemsPedido);
            interfazVerItemsPedido.getjLabel1().setText("Detalles del pedido");
            
            if(interfazModificarPedido != null) {
                
                interfazVerItemsPedido.ocultarColumnaAcciones();
                int idPedido = Integer.parseInt(interfazModificarPedido.getjTextField4().getText());
                Pedido pedido = new Pedido();
                pedido.setId_pedido(idPedido);
                cargarPedidoDetalles(FactoryDAO.getItemMenuPedidoDAO().obtenerDetallesPedido(pedido), interfazVerItemsPedido);
            }
            
            else cargarPedidoDetalles(pedidoDetalles, interfazVerItemsPedido);
        }
        
        else if(comando.equals("EliminarItem")) {
            
            if(interfazVerItemsPedido.confirmarAccion("¿Está seguro de que desea eliminar el item del pedido?")) {
                int row = interfazVerItemsPedido.getjTable1().getSelectedRow();
                int id = (Integer) interfazVerItemsPedido.getModelo().getValueAt(row, 0);
                
                Iterator<PedidoDetalle> iterator = pedidoDetalles.iterator();
                    
                    while(iterator.hasNext()) {
                        PedidoDetalle pd = iterator.next();
                        if(pd.getProducto().getId() == id) {
                            iterator.remove();
                            break;
                        }
                    }
                
                interfazVerItemsPedido.getModelo().removeRow(row);
            }
            
        }
        
        else if(comando.equals("CancelarCrear")) {
                interfazCrearPedido.dispose();
        }
        
        else if(comando.equals("Guardar")) {
            
            if(interfazModificarPedido.confirmarAccion()) {
                
                int idPedido = Integer.parseInt(interfazModificarPedido.getjTextField4().getText());
                int idCliente  = Integer.parseInt(interfazModificarPedido.getjTextField1().getText());
                int idVendedor = Integer.parseInt(interfazModificarPedido.getjTextField2().getText());
                double total = Double.parseDouble(interfazModificarPedido.getjTextField3().getText());
                String formaDePago = (String)interfazModificarPedido.getjComboBox1().getSelectedItem();
                String estado = (String)interfazModificarPedido.getjComboBox2().getSelectedItem();

                if(Estado.valueOf(estado) == Estado.EN_ENVIO) {
                Cliente cliente = new ClienteController().buscarCliente(idCliente);
                Vendedor vendedor = new VendedorController().buscarPorIdVendedor(idVendedor);
                Pedido pedido = buscarPedido(idPedido);

                actualizarPedido(pedido, cliente, vendedor);
                interfazModificarPedido.mostrarPagoGenerado(pedido.getPago());
                interfazModificarPedido.dispose();
                restablecerTablaConDatosOriginales();
                
            } else interfazModificarPedido.dispose();
        }
    }
        
        else if(comando.equals("CancelarModificar")){
            interfazModificarPedido.dispose();
        }
        
        else if(comando.equals("Reiniciar la búsqueda")) {
            restablecerTablaConDatosOriginales();
        }

    }
    
    private void actualizarTabla(List<Pedido> pedidos) {
        // Limpia la tabla actual
        listaDePedidos.getModelo().setRowCount(0); 
        double total;
        Object fecha;
        
        // Agregar los pedidos a la tabla
        for(Pedido pedido : pedidos) {
            
            if(pedido.getPago() == null) {
                total = pedido.getTotal();
                fecha = "";
            }
            else {
                total = pedido.getPago().getMonto();
                fecha = formatoFecha(pedido.getPago().getFecha());
            }
            
            Object idCliente;
            Object idVendedor;

            if(pedido.getCliente() != null) idCliente = pedido.getCliente().getId();
            else idCliente = "No disponible";

            if(pedido.getVendedor() != null) idVendedor = pedido.getVendedor().getId();
            else idVendedor = "No disponible";
            
            Object[] rowData = {
                pedido.getId_pedido(),
                idCliente,
                idVendedor,
                total,
                pedido.getTipoPago(),
                pedido.getEstado(),
                fecha
            };
            listaDePedidos.getModelo().addRow(rowData);
        }
    }
    
    private void actualizarTablaConPedido(Pedido pedido) {
        // Limpia la tabla actual
        listaDePedidos.getModelo().setRowCount(0); 
        double total;
        Object fecha;
        
        if(pedido.getPago() == null) {
            total = pedido.getTotal();
            fecha = "";
        }
        else {
            total = pedido.getPago().getMonto();
            fecha = formatoFecha(pedido.getPago().getFecha());
        }
        
        Object idCliente;
        Object idVendedor;
            
        if(pedido.getCliente() != null) idCliente = pedido.getCliente().getId();
        else idCliente = "No disponible";
            
        if(pedido.getVendedor() != null) idVendedor = pedido.getVendedor().getId();
        else idVendedor = "No disponible";
            
        // Agregar el pedido a la tabla
        Object[] rowData = {
            pedido.getId_pedido(),
            idCliente,
            idVendedor,
            total,
            pedido.getTipoPago(),
            pedido.getEstado(),
            fecha
        };
        listaDePedidos.getModelo().addRow(rowData);
    }
            
    private void restablecerTablaConDatosOriginales() {
        listaDePedidos.getModelo().setRowCount(0); // Limpiar la tabla actual
        cargarDatosOriginalesEnTabla(); // Volver a cargar los datos originales
    }
    
    public void cargarDatosOriginalesEnTabla() {
        
        /*
            for(Pedido pedido : PedidosMemory.listaPedidos) {
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
        double total;
        Object fecha;
        
        for(Pedido pedido : pedidoMySQLDAO.obtenerTodosLosPedidos()) {
            
            if(pedido.getPago() == null) {
                total = pedido.getTotal();
                fecha = "";
            }
            else {
                total = pedido.getPago().getMonto();
                fecha = formatoFecha(pedido.getPago().getFecha());
            }
            
            Object idCliente;
            Object idVendedor;
            
            if(pedido.getCliente() != null) idCliente = pedido.getCliente().getId();
            else idCliente = "No disponible";
            
            if(pedido.getVendedor() != null) idVendedor = pedido.getVendedor().getId();
            else idVendedor = "No disponible";
                
            Object[] rowData = {
                pedido.getId_pedido(),
                idCliente,
                idVendedor,
                total,
                pedido.getTipoPago(),
                pedido.getEstado(),
                fecha
            };
            listaDePedidos.getModelo().addRow(rowData);
        }
    }
     
    public int crearPedido(Pedido pedido, TipoDePago tipoDePago, Cliente cliente) {
        
        cliente.confirmarPedido(tipoDePago);

        // PedidosMemory.listaPedidos.add(pedido);  
        int id = FactoryDAO.getPedidoDAO().crearPedido(pedido);
        pedido.setId_pedido(id);
        FactoryDAO.getItemMenuPedidoDAO().agregarItemsPedido(pedido);
        
        pedidoDetalles = new ArrayList<>();
        
        return id;
    }
    
    public double actualizarPedido(Pedido pedido, Cliente cliente, Vendedor vendedor) {
        
        /*
        for(Pedido pedido : PedidosMemory.listaPedidos) {
            if(pedido.getId_pedido() == idPedido) {
                pedido.setTotal(total);
                pedido.setTipoPago(TipoDePago.valueOf(formaDePago));
                pedido.setEstado(Estado.valueOf(estado));
            }
        }
        */
 
        cliente.agregarPedido(pedido);
        vendedor.agregarPedido(pedido);
        cliente.suscripcionEstadoPedido(pedido);
        vendedor.cambiarEstadoPedido(pedido);
        
        FactoryDAO.getPagoDAO().registrarPago(pedido);
        FactoryDAO.getPedidoDAO().actualizarPedido(pedido);
        
        return pedido.getPago().getMonto();
    }
    
    public void eliminarPedido(int id_pedido) {
        
        /*
        Iterator<Pedido> iterador = PedidosMemory.listaPedidos.iterator();
        while(iterador.hasNext()) {
            Pedido p = iterador.next();
            if(p.getId_pedido() == id_pedido) {
                iterador.remove();
            }
        }
        */
        
        FactoryDAO.getPedidoDAO().eliminarPedido(id_pedido);
    }
    
    public Pedido buscarPedido(int id_pedido) {
        
        /*
        for(Pedido pedido : PedidosMemory.listaPedidos) {
            if(pedido.getId_pedido() == id_pedido) {
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
        for(Pedido pedido : PedidosMemory.listaPedidos) {
            if(pedido.getCliente().getId() == id_cliente) {
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
        for(Pedido pedido : PedidosMemory.listaPedidos) {
            if(pedido.getVendedor().getId() == id_vendedor) {
                pedidoFiltrados.add(pedido);
            }
        }
        return pedidoFiltrados;
        */
        
        return FactoryDAO.getPedidoDAO().buscarPedidosPorVendedor(id_vendedor);
    }
     
    private boolean validarCamposVacios(CrearPedido interfaz) {
        
        boolean vacio = false;
        Border defaultBorder = interfaz.getBordeTexto();

        if (interfaz.getCampoIDvendedor().getText().trim().isEmpty()) {
            interfaz.getCampoIDvendedor().setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.RED),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            vacio = true;
        } else {
            interfaz.getCampoIDvendedor().setBorder(defaultBorder);
        }
        
        if (interfaz.getCampoIDcliente().getText().trim().isEmpty()) {
            interfaz.getCampoIDcliente().setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.RED),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            vacio = true;
        } else {
            interfaz.getCampoIDcliente().setBorder(defaultBorder);
        }

        return vacio;
    }

    private boolean validarTiposDeDatos(CrearPedido interfaz) {
        
        boolean correcto = true;
        Border defaultBorder = interfaz.getBordeTexto();

        if (!interfaz.getCampoIDvendedor().getText().matches("\\d+")) {
            interfaz.getCampoIDvendedor().setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.RED),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            correcto = false;
        } else {
            interfaz.getCampoIDvendedor().setBorder(defaultBorder);
        }

        if (!interfaz.getCampoIDcliente().getText().matches("\\d+")) {
            interfaz.getCampoIDcliente().setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.RED),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            correcto = false;
        } else {
            interfaz.getCampoIDcliente().setBorder(defaultBorder);
        }

        return correcto;
    }
    
    private boolean verificarCliente(int id_cliente) {
        /*
        boolean existe = false;
        
        for(Cliente cliente : ClienteMemory.listaClientes) {
            if(cliente.getId() == id_cliente) existe = true;
        }
        
        return existe;
        */
       
        return (new ClienteController().verificarID(id_cliente));
    }
    
    private boolean verificarVendedor(int id_vendedor) {
        
        /*
        boolean existe = false;
        
        for(Vendedor vendedor : VendedorMemory.listaVendedores) {
            if(vendedor.getId() == id_vendedor) existe = true;
        }
        
        return existe;
        */
        
        return (new VendedorController()).verificarID(id_vendedor);
    }
    
    public void cargarDatosOriginalesEnTablaItems(int idVendedor) {
        
        /*
        for(ItemMenu itemMenu : ItemMenuMemory.listaItemMenu) {
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
        
        Vendedor vendedor = new Vendedor();
        vendedor.setId(idVendedor);
        ItemMenuVendedorMySQLDAO itemMenuVendedorMySQLDAO = (ItemMenuVendedorMySQLDAO) FactoryDAO.getItemMenuVendedorDAO();
        
        for(ItemMenu itemMenu : itemMenuVendedorMySQLDAO.obtenerItemsMenuDeVendedor(vendedor)) {
                Object[] rowData = {
                itemMenu.getId(),
                itemMenu.getCategoria().getTipo_item().toString(),
                itemMenu.getNombre(),
                itemMenu.getDescripcion(),
                itemMenu.getPrecio(),
                itemMenu.getCategoria().getDescripcion(), 
                1
            };
            interfazItemsMenuPedido.getModelo().addRow(rowData);
        }
    }
    
    public void cargarPedidoDetalles(ArrayList<PedidoDetalle> pedidoDetalles, VerItems interfaz) {
        
        for(PedidoDetalle pd : pedidoDetalles) {
                Object[] rowData = {
                pd.getProducto().getId(),
                pd.getProducto().getCategoria().getTipo_item().toString(),
                pd.getProducto().getNombre(),
                pd.getProducto().getDescripcion(),
                pd.getProducto().getPrecio(),
                pd.getProducto().getCategoria().getDescripcion(),
                pd.getCantidad(),
                pd.getProducto().getPrecio() * pd.getCantidad()
            };
            interfaz.getModelo().addRow(rowData);
        }
    }
    
    private String formatoFecha(LocalDate fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return fecha.format(formatter);
    }
    
    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        interfazModificarPedido = null;
        pedidoDetalles = new ArrayList<>();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
}
