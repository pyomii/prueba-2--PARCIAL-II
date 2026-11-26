package prueba2_ii.modelo;

import java.io.Serializable;
import prueba2_ii.enums.CategoriaProducto;

public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String codigo;
    private String nombre;
    private double precio;
    private CategoriaProducto categoria;

    public Producto(String codigo, String nombre, double precio, CategoriaProducto categoria) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public String getDetalle() {
        return "";
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
