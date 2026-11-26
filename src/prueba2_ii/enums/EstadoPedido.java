package prueba2_ii.enums;

public enum EstadoPedido {
    PENDIENTE("Pendiente"),
    EN_PREPARACION("En preparacion"),
    LISTO("Listo"),
    ENTREGADO("Entregado");

    private final String texto;

    private EstadoPedido(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }
}
