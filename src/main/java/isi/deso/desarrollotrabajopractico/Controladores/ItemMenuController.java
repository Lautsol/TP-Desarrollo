
package isi.deso.desarrollotrabajopractico.Controladores;

import isi.deso.desarrollotrabajopractico.Alcohol;
import isi.deso.desarrollotrabajopractico.Categoria;
import isi.deso.desarrollotrabajopractico.DAOS.FactoryDAO;
import isi.deso.desarrollotrabajopractico.DAOS.ItemMenuMySQLDAO;
import isi.deso.desarrollotrabajopractico.Gaseosa;
import isi.deso.desarrollotrabajopractico.Interfaces.CrearItemMenu;
import isi.deso.desarrollotrabajopractico.Interfaces.ListaDeItemMenu;
import isi.deso.desarrollotrabajopractico.Interfaces.ModificarItemMenu;
import isi.deso.desarrollotrabajopractico.ItemMenu;
import isi.deso.desarrollotrabajopractico.Plato;
import isi.deso.desarrollotrabajopractico.TipoItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ItemMenuController implements ActionListener{
    private ListaDeItemMenu listaDeItemMenu;
    private CrearItemMenu interfazCrearItemMenu;
    private ModificarItemMenu interfazModificarItemMenu;

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
    }
    
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        Object source = e.getSource();

        if (comando.equals("Crear nuevo item menú")) {
            interfazCrearItemMenu = new CrearItemMenu();  
            interfazCrearItemMenu.setControlador(this);
            setCrearItemMenu(interfazCrearItemMenu);  
            int idItemMenu = obtenerID();
            interfazCrearItemMenu.getjTextField1().setText(String.valueOf(idItemMenu));
            interfazCrearItemMenu.getjTextField1().setEditable(false);
        } 
        
        else if (comando.equals("Editar")) {
           interfazModificarItemMenu = new ModificarItemMenu();  
           interfazModificarItemMenu.setControlador(this);
           setModificarItemMenu(interfazModificarItemMenu);  
           interfazModificarItemMenu.getjTextField1().setEditable(false);
           interfazModificarItemMenu.getjComboBox1().setEnabled(false);
           
           if (listaDeItemMenu.getjTable1().isEditing()) {
            listaDeItemMenu.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
           }
           
           int row = listaDeItemMenu.getjTable1().getSelectedRow(); // Obtener la fila seleccionada
           
           int id = (Integer) listaDeItemMenu.getModelo().getValueAt(row, 0);
           String tipoDeItem = (String) listaDeItemMenu.getModelo().getValueAt(row, 1);
           String nombre = (String) listaDeItemMenu.getModelo().getValueAt(row, 2);
           String descripcion = (String) listaDeItemMenu.getModelo().getValueAt(row, 3);
           double precio = (Double) listaDeItemMenu.getModelo().getValueAt(row, 4);
           int idCategoria = (Integer) listaDeItemMenu.getModelo().getValueAt(row, 5);
           
           ItemMenu item = buscarItemMenu(id);
           
           if(item instanceof Plato) interfazModificarItemMenu.getjComboBox1().setSelectedItem("PLATO");
           else if(item instanceof Gaseosa) interfazModificarItemMenu.getjComboBox1().setSelectedItem("GASEOSA"); 
           else interfazModificarItemMenu.getjComboBox1().setSelectedItem("ALCOHOL");
       
           interfazModificarItemMenu.getjTextField1().setText(String.valueOf(id)); 
           interfazModificarItemMenu.getjTextField2().setText(nombre); 
           interfazModificarItemMenu.getjTextField3().setText(descripcion); 
           interfazModificarItemMenu.getjTextField4().setText(String.valueOf(precio)); 
           interfazModificarItemMenu.getjTextField5().setText(String.valueOf(idCategoria));       
            
        } 
        
        else if (source == listaDeItemMenu.getjTextField1()) {
            String texto = listaDeItemMenu.getjTextField1().getText();

        // Intentar convertir el texto a un int
        try {
            int idItemMenu = Integer.parseInt(texto);
            
            // Llamar al método de búsqueda por ID
            ItemMenu itemMenu = buscarItemMenu(idItemMenu);
            if (itemMenu != null) {
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
        
        else if (comando.equals("Eliminar")) {
            
            if (listaDeItemMenu.getjTable1().isEditing()) {
            listaDeItemMenu.getjTable1().getCellEditor().stopCellEditing(); // Detener la edición si está activa
            }
            
            int row = listaDeItemMenu.getjTable1().getSelectedRow();
            
            int id = (Integer) listaDeItemMenu.getModelo().getValueAt(row, 0);
            eliminarItemMenu(id);
            listaDeItemMenu.getModelo().removeRow(row);
          
        } 
        
        else if (comando.equals("Crear")) {
            
            if(validarCamposVacios(interfazCrearItemMenu)) interfazCrearItemMenu.mostrarMensajeCamposVacios();
            else if (!validarTiposDeDatos(interfazCrearItemMenu)) interfazCrearItemMenu.mostrarMensajeDatosInvalidos();
            else {
            int id = Integer.parseInt(interfazCrearItemMenu.getjTextField1().getText());
            String nombre = interfazCrearItemMenu.getjTextField2().getText();
            String descripcion = interfazCrearItemMenu.getjTextField3().getText();
            double precio = Double.parseDouble(interfazCrearItemMenu.getjTextField4().getText());
            int idCategoria = Integer.parseInt(interfazCrearItemMenu.getjTextField5().getText());
            String opcion = (String)interfazCrearItemMenu.getjComboBox1().getSelectedItem();
            
            TipoItem tipoItem;
            if(opcion.equals("PLATO")) tipoItem = TipoItem.COMIDA;
            else tipoItem = TipoItem.BEBIDA;
            
            Categoria categoria = verificarCategoria(idCategoria);
            
            if(categoria == null) interfazCrearItemMenu.mostrarMensajeCategoria();
            else if (!categoriaCorrecta(categoria, tipoItem)) interfazCrearItemMenu.mostrarMensajeCategoriaIncorrecta();
            else{
            crearItemMenu(id, nombre, descripcion, precio, idCategoria, opcion);
            listaDeItemMenu.agregarItemMenuALaTabla(id, tipoItem, nombre, descripcion, precio, idCategoria);
            interfazCrearItemMenu.setearCamposEnBlanco();
            int idItemMenu = obtenerID();
            interfazCrearItemMenu.getjTextField1().setText(String.valueOf(idItemMenu));
           }
         
        }
      } 

        else if (comando.equals("Guardar")) {
            
            if(validarCamposVaciosModificar(interfazModificarItemMenu)) interfazModificarItemMenu.mostrarMensajeCamposVacios();
            else if (!validarTiposDeDatosModificar(interfazModificarItemMenu)) interfazModificarItemMenu.mostrarMensajeDatosInvalidos();
            else {
            int row = listaDeItemMenu.getjTable1().getSelectedRow(); // Obtener la fila seleccionada
            
            int id = Integer.parseInt(interfazModificarItemMenu.getjTextField1().getText());
            String nombre = interfazModificarItemMenu.getjTextField2().getText();
            String descripcion = interfazModificarItemMenu.getjTextField3().getText();
            String tipoDeItem = interfazModificarItemMenu.getjComboBox1().getSelectedItem().toString();
            double precio = Double.parseDouble(interfazModificarItemMenu.getjTextField4().getText());
            int idCategoria = Integer.parseInt(interfazModificarItemMenu.getjTextField5().getText());
            String item = (String)interfazModificarItemMenu.getjComboBox1().getSelectedItem();
            
            TipoItem tipoItem;
            if(tipoDeItem.equals("PLATO")) tipoItem = TipoItem.COMIDA;
            else tipoItem = TipoItem.BEBIDA;
            
            Categoria categoria = verificarCategoria(idCategoria);
            
            if(categoria == null) interfazModificarItemMenu.mostrarMensajeCategoria();
            else if (!categoriaCorrecta(categoria, tipoItem)) interfazModificarItemMenu.mostrarMensajeCategoriaIncorrecta();
            else{
            
            actualizarItemMenu(id, tipoItem, nombre, descripcion, precio, idCategoria, item);
            
            listaDeItemMenu.getModelo().setValueAt(id, row, 0); 
            listaDeItemMenu.getModelo().setValueAt(tipoItem.toString(), row, 1);
            listaDeItemMenu.getModelo().setValueAt(nombre, row, 2);      
            listaDeItemMenu.getModelo().setValueAt(descripcion, row, 3);    
            listaDeItemMenu.getModelo().setValueAt(precio, row, 4);    
            listaDeItemMenu.getModelo().setValueAt(idCategoria, row, 5);   
            }
          }
        } 
        
        else if (comando.equals("CancelarCrear")) {
            interfazCrearItemMenu.dispose();
        } 
        
        else if (comando.equals("CancelarModificar")) {
                interfazModificarItemMenu.dispose();  
        }
        
        else if(comando.equals("Reiniciar la búsqueda")) {
            restablecerTablaConDatosOriginales();
            listaDeItemMenu.getjTextField1().setText("Buscar por nombre o por ID");
        }
    }
    
    public ItemMenu buscarItemMenu(int id) {
        
        /*
        for (ItemMenu itemMenu : ItemMenuMemory.listaItemMenu) {
            if (itemMenu.getId() == id) {
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
        for (ItemMenu itemMenu : ItemMenuMemory.listaItemMenu) {
            if (itemMenu.getNombre().equals(nombre)) {
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
        for (ItemMenu itemMenu : itemsMenu) {
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
        while (iterador.hasNext()) {
            ItemMenu i = iterador.next();
            if (i.getId() == id) {
                iterador.remove();
            }
        }
        */
        
        FactoryDAO.getItemMenuDAO().eliminarItemMenu(id);
    }
    
    public void crearItemMenu(int id, String nombre, String descripcion, double precio, int idCategoria, String opcion) {
        
        ItemMenu itemMenu;
        Categoria categoria;
        
        if(opcion.equals("PLATO")){
            itemMenu=new Plato(); 
            categoria=FactoryDAO.getCategoriaDAO().buscarCategoria(idCategoria);
            itemMenu.setId(id);
            itemMenu.setNombre(nombre);
            itemMenu.setDescripcion(descripcion);
            itemMenu.setPrecio(precio);
            itemMenu.setCategoria(categoria);
            // ItemMenuMemory.listaItemMenu.add(itemMenu); 
            FactoryDAO.getItemMenuDAO().crearItemMenu(itemMenu);
        }
        else if(opcion.equals("GASEOSA")){
            itemMenu=new Gaseosa(); 
            categoria=FactoryDAO.getCategoriaDAO().buscarCategoria(idCategoria);
            itemMenu.setId(id);
            itemMenu.setNombre(nombre);
            itemMenu.setDescripcion(descripcion);
            itemMenu.setPrecio(precio);
            itemMenu.setCategoria(categoria);
            // ItemMenuMemory.listaItemMenu.add(itemMenu); 
            FactoryDAO.getItemMenuDAO().crearItemMenu(itemMenu);
        }
        else if(opcion.equals("ALCOHOL")){
            itemMenu=new Alcohol(); 
            categoria=FactoryDAO.getCategoriaDAO().buscarCategoria(idCategoria);
            itemMenu.setId(id);
            itemMenu.setNombre(nombre);
            itemMenu.setDescripcion(descripcion);
            itemMenu.setPrecio(precio);
            itemMenu.setCategoria(categoria);
            // ItemMenuMemory.listaItemMenu.add(itemMenu); 
            FactoryDAO.getItemMenuDAO().crearItemMenu(itemMenu);
        }
        
    }
    
    public void actualizarItemMenu(int id, TipoItem tipoItem, String nombre, String descripcion, double precio, int idCategoria, String item) {
        /*
        for (ItemMenu itemMenu : ItemMenuMemory.listaItemMenu) {
            if (itemMenu.getId() == id) {
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
        
        if(interfaz.getjTextField1().getText().trim().isEmpty() || 
           interfaz.getjTextField2().getText().trim().isEmpty() ||
           interfaz.getjTextField3().getText().trim().isEmpty() ||
           interfaz.getjTextField4().getText().trim().isEmpty() ||
           interfaz.getjTextField5().getText().trim().isEmpty()) vacio = true;
        
        return vacio;
    }
    
    private boolean validarTiposDeDatos(CrearItemMenu interfaz) {
        
        boolean correcto = true;
        
        if(!interfaz.getjTextField1().getText().matches("\\d+") || 
           !interfaz.getjTextField2().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+") ||
           !interfaz.getjTextField3().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s,]+") ||
           !interfaz.getjTextField4().getText().matches("-?\\d+(\\.\\d+)?") ||
           !interfaz.getjTextField5().getText().matches("\\d+")) correcto = false;
        
        return correcto;
    }
    
    private boolean validarCamposVaciosModificar(ModificarItemMenu interfaz) {
        
        boolean vacio = false;
        
        if(interfaz.getjTextField2().getText().trim().isEmpty() ||
           interfaz.getjTextField3().getText().trim().isEmpty() ||
           interfaz.getjTextField4().getText().trim().isEmpty() ||
           interfaz.getjTextField5().getText().trim().isEmpty()) vacio = true;
        
        return vacio;
    }
    
    private boolean validarTiposDeDatosModificar(ModificarItemMenu interfaz) {
        
        boolean correcto = true;
        
        if(!interfaz.getjTextField2().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+") ||
           !interfaz.getjTextField3().getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s,]+") ||
           !interfaz.getjTextField4().getText().matches("-?\\d+(\\.\\d+)?") ||
           !interfaz.getjTextField5().getText().matches("\\d+")) correcto = false;
        
        return correcto;
    }
    
    private Categoria verificarCategoria(int id_categoria) {
        
        /*
        Categoria categoria = null; 
        
        for (Categoria c : CategoriaMemory.listaCategorias) {
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
    
    private int obtenerID() {
        ItemMenuMySQLDAO itemMenuMySQLDAO = (ItemMenuMySQLDAO) FactoryDAO.getItemMenuDAO();
        return itemMenuMySQLDAO.obtenerID();
    }
}
