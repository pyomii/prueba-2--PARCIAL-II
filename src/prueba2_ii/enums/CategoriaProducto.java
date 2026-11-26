package prueba2_ii.enums;

public enum CategoriaProducto {
    PLATO("Plato"),
    BEBIDA("Bebida"),
    POSTRE("Postre");

    private final String texto;

    private CategoriaProducto(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }
}
