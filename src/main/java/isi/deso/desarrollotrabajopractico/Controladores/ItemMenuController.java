
package isi.deso.desarrollotrabajopractico.Controladores;

import isi.deso.desarrollotrabajopractico.modelo.Alcohol;
import isi.deso.desarrollotrabajopractico.modelo.Categoria;
import isi.deso.desarrollotrabajopractico.DAOS.CategoriaMySQLDAO;
import isi.deso.desarrollotrabajopractico.DAOS.FactoryDAO;
import isi.deso.desarrollotrabajopractico.DAOS.ItemMenuMySQLDAO;
import isi.deso.desarrollotrabajopractico.modelo.Gaseosa;
import isi.deso.desarrollotrabajopractico.Interfaces.Categorias;
import isi.deso.desarrollotrabajopractico.Interfaces.CrearItemMenu;
import isi.deso.desarrollotrabajopractico.Interfaces.ListaDeItemMenu;
import isi.deso.desarrollotrabajopractico.Interfaces.ModificarItemMenu;
import isi.deso.desarrollotrabajopractico.modelo.ItemMenu;
import isi.deso.desarrollotrabajopractico.modelo.Plato;
import isi.deso.desarrollotrabajopractico.modelo.TipoItem;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.border.Border;

public class ItemMenuController implements ActionListener, WindowListener {
    private ListaDeItemMenu listaDeItemMenu;
    private CrearItemMenu interfazCrearItemMenu;
    private ModificarItemMenu interfazModificarItemMenu;
    private Categorias interfazCategorias;
    int idCategoria = 0;

    public ItemMenuController() {
       
    }

    public ItemMenuController(ListaDeItemMenu listaDeItemMenu) {
        this.listaDeItemMenu = listaDeItemMenu;
    }

    public ItemMenuController(CrearItemMenu interfazCrearItemMenu) {
        this.interfazCrearItemMenu = interfazCrearItemMenu;
    }

    public ItemMenuController(ModificarItemMenu interfazModificarItemMenu) {
        this.interfazModificarItemMenu = interfazModificarItemMenu;
    }

    public void setCrearItemMenu(CrearItemMenu crearItemMenu) {
        this.interfazCrearItemMenu = crearItemMenu;
    }

    public void setModificarItemMenu(ModificarItemMenu modificarItemMenu) {
        this.interfazModificarItemMenu = modificarItemMenu;
        interfazModificarItemMenu.addWindowListener(this);
    }
    
    public void setInterfazCategorias(Categorias interfazCategorias) {
        this.interfazCategorias = interfazCategorias;
    }
    
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        Object source = e.getSource();

        if(comando.equals("Crear nuevo item menú")) {
            interfazCrearItemMenu = new CrearItemMenu();  
            interfazCrearItemMenu.setControlador(this);
            setCrearItemMenu(interfazCrearItemMenu);  
            idCategoria = 0;
        } 
        
        else if(comando.equals("Editar")) {
           interfazModificarItemMenu = new ModificarItemMenu();  
           interfazModificarItemMenu.setControlador(this);
           setModificarItemMenu(interfazModificarItemMenu);  
           interfazModificarItemMenu.getjTextField1().setEditable(false);
           interfazModificarItemMenu.getjComboBox1().setEnabled(false);
           
           if(listaDeItemMenu.getjTable1().isEditing()) {
            listaDeItemMenu.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
           }
           
           int row = listaDeItemMenu.getjTable1().getSelectedRow(); // Obtener la fila seleccionada
           
           int id = (Integer) listaDeItemMenu.getModelo().getValueAt(row, 0);
           String tipoDeItem = (String) listaDeItemMenu.getModelo().getValueAt(row, 1);
           String nombre = (String) listaDeItemMenu.getModelo().getValueAt(row, 2);
           String descripcion = (String) listaDeItemMenu.getModelo().getValueAt(row, 3);
           double precio = (Double) listaDeItemMenu.getModelo().getValueAt(row, 4);
           idCategoria = (Integer) listaDeItemMenu.getModelo().getValueAt(row, 5);
           
           ItemMenu item = buscarItemMenu(id);
           
           if(item instanceof Plato) interfazModificarItemMenu.getjComboBox1().setSelectedItem("PLATO");
           else if(item instanceof Gaseosa) interfazModificarItemMenu.getjComboBox1().setSelectedItem("GASEOSA"); 
           else interfazModificarItemMenu.getjComboBox1().setSelectedItem("ALCOHOL");
       
           interfazModificarItemMenu.getjTextField1().setText(String.valueOf(id)); 
           interfazModificarItemMenu.getjTextField2().setText(nombre); 
           interfazModificarItemMenu.getjTextField3().setText(descripcion); 
           interfazModificarItemMenu.getjTextField4().setText(String.valueOf(precio));    
            
        } 
        
        else if(source == listaDeItemMenu.getjTextField1()) {
            String texto = listaDeItemMenu.getjTextField1().getText();

        // Intentar convertir el texto a un int
        try {
            int idItemMenu = Integer.parseInt(texto);
            
            // Llamar al método de búsqueda por ID
            ItemMenu itemMenu = buscarItemMenu(idItemMenu);
            if(itemMenu != null) {
                actualizarTablaConItemMenu(itemMenu);
            } 
            else {
                listaDeItemMenu.mostrarMensaje();
                restablecerTablaConDatosOriginales();
            }
            
        } catch (NumberFormatException ex) {
            
            // Si no se puede convertir a int, se asume que es un String
            String nombreItemMenu = texto;
            
            // Llamar al método de búsqueda por nombre
            List<ItemMenu> itemsMenu = listarItemsMenu(nombreItemMenu);
            
            if(!itemsMenu.isEmpty()){
            actualizarTabla(itemsMenu);
            }
            else{
                listaDeItemMenu.mostrarMensaje();
                restablecerTablaConDatosOriginales();
                }
            }
        }
        
        else if(comando.equals("Eliminar")) {
            
            if(listaDeItemMenu.getjTable1().isEditing()) {
            listaDeItemMenu.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
            }
            
            if(listaDeItemMenu.confirmarAccion()) {
                int row = listaDeItemMenu.getjTable1().getSelectedRow();

                int id = (Integer) listaDeItemMenu.getModelo().getValueAt(row, 0);
                eliminarItemMenu(id);
                listaDeItemMenu.getModelo().removeRow(row);
            }
          
        } 
        
        else if(comando.equals("Seleccionar")) {
            
            interfazCategorias = new Categorias(this);
            setInterfazCategorias(interfazCategorias);
            cargarCategoriasTabla();
            if(interfazModificarItemMenu != null) {
                int row = listaDeItemMenu.getjTable1().getSelectedRow();
                if(row != -1) {
                    int idItem = Integer.parseInt(interfazModificarItemMenu.getjTextField1().getText());
                    seleccionarFilaPorID(interfazCategorias.getjTable(), buscarItemMenu(idItem).getCategoria().getId());
                }
            }
        }
        
        else if(comando.equals("Aceptar")) {
            
                int row = interfazCategorias.getjTable().getSelectedRow(); 
                if(row == -1) interfazCategorias.mostrarMensaje();
                else {
                    idCategoria = (Integer) interfazCategorias.getModelo().getValueAt(row, 0);
                    interfazCategorias.dispose();
                }
        
        }
        
        else if(comando.equals("Crear")) {
            
            if(validarCamposVacios(interfazCrearItemMenu)) interfazCrearItemMenu.mostrarMensajeCamposVacios();
            else if(!validarTiposDeDatos(interfazCrearItemMenu)) interfazCrearItemMenu.mostrarMensajeDatosInvalidos();
            else if(idCategoria == 0) interfazCrearItemMenu.mostrarMensajeCategoria("Debe especificar la categoría del item menú.");
            else {
            String nombre = interfazCrearItemMenu.getjTextField2().getText();
            String descripcion = interfazCrearItemMenu.getjTextField3().getText();
            double precio = Double.parseDouble(interfazCrearItemMenu.getjTextField4().getText());
            String opcion = (String)interfazCrearItemMenu.getjComboBox1().getSelectedItem();
            
            TipoItem tipoItem;
            if(opcion.equals("PLATO")) tipoItem = TipoItem.COMIDA;
            else tipoItem = TipoItem.BEBIDA;
            
            Categoria categoria = buscarCategoria(idCategoria);
            
            if(!categoriaCorrecta(categoria, tipoItem)) interfazCrearItemMenu.mostrarMensajeCategoria("El tipo de item no corresponde a esa categoría.");
            else if(interfazCrearItemMenu.confirmarAccion()) {
                crearItemMenu(nombre, descripcion, precio, idCategoria, opcion);
                restablecerTablaConDatosOriginales();
                idCategoria = 0;
                listaDeItemMenu.getjTable1().clearSelection();
                interfazCrearItemMenu.dispose();
           }
        }
      } 

        else if (comando.equals("Guardar")) {
            
            if(validarCamposVaciosModificar(interfazModificarItemMenu)) interfazModificarItemMenu.mostrarMensajeCamposVacios();
            else if (!validarTiposDeDatosModificar(interfazModificarItemMenu)) interfazModificarItemMenu.mostrarMensajeDatosInvalidos();
            else {
            int id = Integer.parseInt(interfazModificarItemMenu.getjTextField1().getText());
            String nombre = interfazModificarItemMenu.getjTextField2().getText();
            String descripcion = interfazModificarItemMenu.getjTextField3().getText();
            String tipoDeItem = interfazModificarItemMenu.getjComboBox1().getSelectedItem().toString();
            double precio = Double.parseDouble(interfazModificarItemMenu.getjTextField4().getText());
            String item = (String)interfazModificarItemMenu.getjComboBox1().getSelectedItem();
            
            TipoItem tipoItem;
            if(tipoDeItem.equals("PLATO")) tipoItem = TipoItem.COMIDA;
            else tipoItem = TipoItem.BEBIDA;
            
            Categoria categoria = buscarCategoria(idCategoria);
            
            if(!categoriaCorrecta(categoria, tipoItem)) interfazModificarItemMenu.mostrarMensajeCategoria("El tipo de item no corresponde a esa categoría.");
            else if(interfazModificarItemMenu.confirmarAccion()) {
                actualizarItemMenu(id, tipoItem, nombre, descripcion, precio, idCategoria, item);
                restablecerTablaConDatosOriginales();
                interfazModificarItemMenu.dispose();
            }
          }
        } 
        
        else if(comando.equals("CancelarCrear")) {
            interfazCrearItemMenu.dispose();
            interfazCrearItemMenu = null;
        } 
        
        else if(comando.equals("CancelarModificar")) {
                interfazModificarItemMenu.dispose();  
                interfazModificarItemMenu = null;
        }
        
        else if(comando.equals("Reiniciar la búsqueda")) {
            restablecerTablaConDatosOriginales();
            listaDeItemMenu.getjTextField1().setText("Buscar por nombre o por ID");
        }
    }
    
    public ItemMenu buscarItemMenu(int id) {
        
        /*
        for(ItemMenu itemMenu : ItemMenuMemory.listaItemMenu) {
            if(itemMenu.getId() == id) {
                return itemMenu;
            }
        }
        return null;  
        */
        
        return FactoryDAO.getItemMenuDAO().buscarItemMenuPorID(id);
    }
    
    private void actualizarTablaConItemMenu(ItemMenu itemMenu) {
        // Limpia la tabla actual
        listaDeItemMenu.getModelo().setRowCount(0); 

        // Agregar el item a la tabla
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
    
    public void cargarDatosOriginalesEnTabla() {
        
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
            listaDeItemMenu.getModelo().addRow(rowData);
        }
    }
    
    private void restablecerTablaConDatosOriginales() {
        listaDeItemMenu.getModelo().setRowCount(0); // Limpiar la tabla actual
        cargarDatosOriginalesEnTabla(); // Volver a cargar los datos originales
    }
    
    public ArrayList<ItemMenu> listarItemsMenu(String nombre) {
        
        /*
        ArrayList<ItemMenu> itemsMenuFiltrados = new ArrayList<>();
        for(ItemMenu itemMenu : ItemMenuMemory.listaItemMenu) {
            if(itemMenu.getNombre().equals(nombre)) {
                itemsMenuFiltrados.add(itemMenu);
            }
        }
        return itemsMenuFiltrados;
        */
        
        return FactoryDAO.getItemMenuDAO().buscarItemsMenuPorNombre(nombre);
    }
    
    private void actualizarTabla(List<ItemMenu> itemsMenu) {
        // Limpia la tabla actual
        listaDeItemMenu.getModelo().setRowCount(0); 

        // Agregar los items a la tabla
        for(ItemMenu itemMenu : itemsMenu) {
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
    }
    
    public void eliminarItemMenu(int id) {
        /*
        Iterator<ItemMenu> iterador = ItemMenuMemory.listaItemMenu.iterator();
        while(iterador.hasNext()) {
            ItemMenu i = iterador.next();
            if(i.getId() == id) {
                iterador.remove();
            }
        }
        */
        
        FactoryDAO.getItemMenuDAO().eliminarItemMenu(id);
    }
    
    public int crearItemMenu(String nombre, String descripcion, double precio, int idCategoria, String opcion) {
        
        ItemMenu itemMenu;
        Categoria categoria;
        
        if(opcion.equals("PLATO")){
            itemMenu=new Plato(); 
            categoria=FactoryDAO.getCategoriaDAO().buscarCategoria(idCategoria);
            itemMenu.setNombre(nombre);
            itemMenu.setDescripcion(descripcion);
            itemMenu.setPrecio(precio);
            itemMenu.setCategoria(categoria);
            // ItemMenuMemory.listaItemMenu.add(itemMenu); 
            return FactoryDAO.getItemMenuDAO().crearItemMenu(itemMenu);
        }
        else if(opcion.equals("GASEOSA")){
            itemMenu=new Gaseosa(); 
            categoria=FactoryDAO.getCategoriaDAO().buscarCategoria(idCategoria);
            itemMenu.setNombre(nombre);
            itemMenu.setDescripcion(descripcion);
            itemMenu.setPrecio(precio);
            itemMenu.setCategoria(categoria);
            // ItemMenuMemory.listaItemMenu.add(itemMenu); 
            return FactoryDAO.getItemMenuDAO().crearItemMenu(itemMenu);
        }
        else if(opcion.equals("ALCOHOL")){
            itemMenu=new Alcohol(); 
            categoria=FactoryDAO.getCategoriaDAO().buscarCategoria(idCategoria);
            itemMenu.setNombre(nombre);
            itemMenu.setDescripcion(descripcion);
            itemMenu.setPrecio(precio);
            itemMenu.setCategoria(categoria);
            // ItemMenuMemory.listaItemMenu.add(itemMenu); 
            return FactoryDAO.getItemMenuDAO().crearItemMenu(itemMenu);
        }
        
        return 0;
    }
    
    public void actualizarItemMenu(int id, TipoItem tipoItem, String nombre, String descripcion, double precio, int idCategoria, String item) {
        /*
        for(ItemMenu itemMenu : ItemMenuMemory.listaItemMenu) {
            if(itemMenu.getId() == id) {
            itemMenu.getCategoria().setTipo_item(tipoItem);
            itemMenu.setNombre(nombre);
            itemMenu.setDescripcion(descripcion);
            itemMenu.setPrecio(precio);
            itemMenu.getCategoria().setId(idCategoria);
            }
        }
        */
        
        Categoria categoria; 
        ItemMenu itemMenu;
        
        if(item.equals("PLATO")) {
            itemMenu = FactoryDAO.getItemMenuDAO().buscarItemMenuPorID(id);
            categoria=FactoryDAO.getCategoriaDAO().buscarCategoria(idCategoria);
            itemMenu.setCategoria(categoria);
            itemMenu.getCategoria().setTipo_item(tipoItem);
            itemMenu.setNombre(nombre);
            itemMenu.setDescripcion(descripcion);
            itemMenu.setPrecio(precio);
            itemMenu.getCategoria().setId(idCategoria);
            FactoryDAO.getItemMenuDAO().actualizarItemMenu(itemMenu);
        }
        else if(item.equals("GASEOSA")) {
            itemMenu = FactoryDAO.getItemMenuDAO().buscarItemMenuPorID(id);
            categoria=FactoryDAO.getCategoriaDAO().buscarCategoria(idCategoria);
            itemMenu.setCategoria(categoria);
            itemMenu.getCategoria().setTipo_item(tipoItem);
            itemMenu.setNombre(nombre);
            itemMenu.setDescripcion(descripcion);
            itemMenu.setPrecio(precio);
            itemMenu.getCategoria().setId(idCategoria);
            FactoryDAO.getItemMenuDAO().actualizarItemMenu(itemMenu);
        }
        else {
            itemMenu = FactoryDAO.getItemMenuDAO().buscarItemMenuPorID(id);
            categoria=FactoryDAO.getCategoriaDAO().buscarCategoria(idCategoria);
            itemMenu.setCategoria(categoria);
            itemMenu.getCategoria().setTipo_item(tipoItem);
            itemMenu.setNombre(nombre);
            itemMenu.setDescripcion(descripcion);
            itemMenu.setPrecio(precio);
            itemMenu.getCategoria().setId(idCategoria);
            FactoryDAO.getItemMenuDAO().actualizarItemMenu(itemMenu);
        }
    }
    
    private boolean validarCamposVacios(CrearItemMenu interfaz) {
        
        boolean vacio = false;
        Border defaultBorder = BorderFactory.createLineBorder(Color.GRAY);

        if (interfaz.getjTextField2().getText().trim().isEmpty()) {
            interfaz.getjTextField2().setBorder(BorderFactory.createLineBorder(Color.RED));
            vacio = true;
        } else {
            interfaz.getjTextField2().setBorder(defaultBorder);
        }

        if (interfaz.getjTextField3().getText().trim().isEmpty()) {
            interfaz.getjTextField3().setBorder(BorderFactory.createLineBorder(Color.RED));
            vacio = true;
        } else {
            interfaz.getjTextField3().setBorder(defaultBorder);
        }

        if (interfaz.getjTextField4().getText().trim().isEmpty()) {
            interfaz.getjTextField4().setBorder(BorderFactory.createLineBorder(Color.RED));
            vacio = true;
        } else {
            interfaz.getjTextField4().setBorder(defaultBorder);
        }

        return vacio;
    }

    private boolean validarTiposDeDatos(CrearItemMenu interfaz) {
        
        boolean correcto = true;
        Border defaultBorder = BorderFactory.createLineBorder(Color.GRAY);

        if (!interfaz.getjTextField2().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            interfaz.getjTextField2().setBorder(BorderFactory.createLineBorder(Color.RED));
            correcto = false;
        } else {
            interfaz.getjTextField2().setBorder(defaultBorder);
        }

        if (!interfaz.getjTextField3().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s,]+")) {
            interfaz.getjTextField3().setBorder(BorderFactory.createLineBorder(Color.RED));
            correcto = false;
        } else {
            interfaz.getjTextField3().setBorder(defaultBorder);
        }

        if (!interfaz.getjTextField4().getText().matches("-?\\d+(\\.\\d+)?")) {
            interfaz.getjTextField4().setBorder(BorderFactory.createLineBorder(Color.RED));
            correcto = false;
        } else {
            interfaz.getjTextField4().setBorder(defaultBorder);
        }

        return correcto;
    }

    private boolean validarCamposVaciosModificar(ModificarItemMenu interfaz) {
        
        boolean vacio = false;
        Border defaultBorder = BorderFactory.createLineBorder(Color.GRAY);

        if (interfaz.getjTextField2().getText().trim().isEmpty()) {
            interfaz.getjTextField2().setBorder(BorderFactory.createLineBorder(Color.RED));
            vacio = true;
        } else {
            interfaz.getjTextField2().setBorder(defaultBorder);
        }

        if (interfaz.getjTextField3().getText().trim().isEmpty()) {
            interfaz.getjTextField3().setBorder(BorderFactory.createLineBorder(Color.RED));
            vacio = true;
        } else {
            interfaz.getjTextField3().setBorder(defaultBorder);
        }

        if (interfaz.getjTextField4().getText().trim().isEmpty()) {
            interfaz.getjTextField4().setBorder(BorderFactory.createLineBorder(Color.RED));
            vacio = true;
        } else {
            interfaz.getjTextField4().setBorder(defaultBorder);
        }

        return vacio;
    }

    private boolean validarTiposDeDatosModificar(ModificarItemMenu interfaz) {
        
        boolean correcto = true;
        Border defaultBorder = BorderFactory.createLineBorder(Color.GRAY);

        if (!interfaz.getjTextField2().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            interfaz.getjTextField2().setBorder(BorderFactory.createLineBorder(Color.RED));
            correcto = false;
        } else {
            interfaz.getjTextField2().setBorder(defaultBorder);
        }

        if (!interfaz.getjTextField3().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s,]+")) {
            interfaz.getjTextField3().setBorder(BorderFactory.createLineBorder(Color.RED));
            correcto = false;
        } else {
            interfaz.getjTextField3().setBorder(defaultBorder);
        }

        if (!interfaz.getjTextField4().getText().matches("-?\\d+(\\.\\d+)?")) {
            interfaz.getjTextField4().setBorder(BorderFactory.createLineBorder(Color.RED));
            correcto = false;
        } else {
            interfaz.getjTextField4().setBorder(defaultBorder);
        }

        return correcto;
    }
    
    private Categoria buscarCategoria(int id_categoria) {
        
        /*
        Categoria categoria = null; 
        
        for(Categoria c : CategoriaMemory.listaCategorias) {
            if(c.getId() == id_categoria) categoria = c;
            
        }
        
        return categoria;
        */
        
        return FactoryDAO.getCategoriaDAO().buscarCategoria(id_categoria);
    }
    
    private boolean categoriaCorrecta(Categoria categoria, TipoItem tipoItem) {
        
        // return (categoria.getTipo_item() == tipoItem);
        return(FactoryDAO.getCategoriaDAO().buscarCategoria(categoria.getId()).getTipo_item() == tipoItem);
    }
    
    private void cargarCategoriasTabla() {
        CategoriaMySQLDAO categoriaMySQLDAO = (CategoriaMySQLDAO) FactoryDAO.getCategoriaDAO();
        for(Categoria categoria : categoriaMySQLDAO.obtenerTodasLasCategorias()) {
            Object[] rowData = {
                categoria.getId(),
                categoria.getTipo_item().toString(),
                categoria.getDescripcion()
            };
            interfazCategorias.getModelo().addRow(rowData);
        }
    }
    
    private void seleccionarFilaPorID(JTable tabla, int idBuscado) {
        for(int i = 0; i < tabla.getRowCount(); i++) {
            int id = (Integer) tabla.getValueAt(i, 0);  
            if(id == idBuscado) {
                tabla.setRowSelectionInterval(i, i);  
                break;  
            }
        }
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        interfazModificarItemMenu = null;
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

