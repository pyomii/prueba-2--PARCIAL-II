package prueba2_ii.modelo;

import prueba2_ii.enums.CategoriaProducto;

public class Postre extends Producto {
    private static final long serialVersionUID = 1L;

    private String presentacion;

    public Postre(String codigo, String nombre, double precio, String presentacion) {
        super(codigo, nombre, precio, CategoriaProducto.POSTRE);
        this.presentacion = presentacion;
    }

    @Override
    public String getDetalle() {
        return "Presentacion: " + presentacion;
    }
}
