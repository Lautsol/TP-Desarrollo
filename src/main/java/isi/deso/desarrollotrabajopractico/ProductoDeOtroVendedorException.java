
package isi.deso.desarrollotrabajopractico;

public class ProductoDeOtroVendedorException extends Exception {
    
    public ProductoDeOtroVendedorException(){
        super("ERROR: EL PEDIDO CONTIENE PRODUCTOS DE DIFERENTES VENDEDORES.");
    }
            
}
