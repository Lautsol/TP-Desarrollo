package isi.deso.desarrollotrabajopractico;

public class Categoria {
    private int id;
    private String descripcion;
    private TipoItem tipo_item;
    
    public Categoria() {}
    
    public Categoria(int id, String descripcion, TipoItem tipo_item) {
        this.id = id;
        this.descripcion = descripcion;
        this.tipo_item = tipo_item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoItem getTipo_item() {
        return tipo_item;
    }

    public void setTipo_item(TipoItem tipo_item) {
        this.tipo_item = tipo_item;
    }
    
    
}
