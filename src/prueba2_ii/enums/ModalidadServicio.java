package prueba2_ii.enums;

public enum ModalidadServicio {
    ESTABLECIMIENTO("Consumo en el establecimiento"),
    DOMICILIO("Pedido a domicilio");

    private final String texto;

    private ModalidadServicio(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }
}
