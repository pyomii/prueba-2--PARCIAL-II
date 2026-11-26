package prueba2_ii.modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import prueba2_ii.enums.EstadoPedido;
import prueba2_ii.enums.ModalidadServicio;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String fecha;
    private ArrayList<ItemPedido> items;
    private ModalidadServicio modalidad;
    private EstadoPedido estado;
    private double total;

    public Pedido(String id, ModalidadServicio modalidad) {
        this.id = id;
        this.modalidad = modalidad;
        this.fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        this.items = new ArrayList<ItemPedido>();
        this.estado = EstadoPedido.PENDIENTE;
        this.total = 0;
    }

    public void agregarItem(Producto producto, int cantidad) {
        items.add(new ItemPedido(producto, cantidad));
        actualizarTotal();
    }

    public void eliminarItem(int indice) {
        items.remove(indice);
        actualizarTotal();
    }

    public ArrayList<ItemPedido> getItems() {
        return items;
    }

    public String getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public ModalidadServicio getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadServicio modalidad) {
        this.modalidad = modalidad;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    private void actualizarTotal() {
        total = calcularTotal(items.size() - 1);
    }

    private double calcularTotal(int indice) {
        if (indice < 0) {
            return 0;
        }
        return items.get(indice).getSubtotal() + calcularTotal(indice - 1);
    }
}
