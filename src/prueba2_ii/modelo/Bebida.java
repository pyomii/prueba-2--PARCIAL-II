package prueba2_ii.modelo;

import prueba2_ii.enums.CategoriaProducto;

public class Bebida extends Producto {
    private static final long serialVersionUID = 1L;

    private String tamano;

    public Bebida(String codigo, String nombre, double precio, String tamano) {
        super(codigo, nombre, precio, CategoriaProducto.BEBIDA);
        this.tamano = tamano;
    }

    @Override
    public String getDetalle() {
        return "Tamano: " + tamano;
    }
}
