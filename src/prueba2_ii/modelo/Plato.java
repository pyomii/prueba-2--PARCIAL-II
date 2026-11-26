package prueba2_ii.modelo;

import prueba2_ii.enums.CategoriaProducto;

public class Plato extends Producto {
    private static final long serialVersionUID = 1L;

    private String tipo;

    public Plato(String codigo, String nombre, double precio, String tipo) {
        super(codigo, nombre, precio, CategoriaProducto.PLATO);
        this.tipo = tipo;
    }

    @Override
    public String getDetalle() {
        return "Tipo: " + tipo;
    }
}
