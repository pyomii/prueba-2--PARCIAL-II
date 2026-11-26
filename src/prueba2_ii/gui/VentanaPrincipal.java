package prueba2_ii.gui;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import prueba2_ii.datos.ArchivoDatos;
import prueba2_ii.enums.CategoriaProducto;
import prueba2_ii.enums.EstadoPedido;
import prueba2_ii.enums.ModalidadServicio;
import prueba2_ii.modelo.Bebida;
import prueba2_ii.modelo.ItemPedido;
import prueba2_ii.modelo.Pedido;
import prueba2_ii.modelo.Plato;
import prueba2_ii.modelo.Postre;
import prueba2_ii.modelo.Producto;

public class VentanaPrincipal extends JFrame {
    private ArrayList<Producto> productos;
    private ArrayList<Pedido> pedidos;
    private JTabbedPane tabs;

    private JTextField campoCodigo;
    private JTextField campoNombre;
    private JTextField campoPrecio;
    private JTextField campoDetalle;
    private JComboBox<CategoriaProducto> comboCategoria;
    private DefaultTableModel modeloProd = new DefaultTableModel(
            new String[]{"Codigo", "Nombre", "Categoria", "Precio", "Detalle"}, 0);
    private JTable tablaProd;

    private JComboBox<Producto> comboProductos;
    private JTextField campoCantidad;
    private JComboBox<ModalidadServicio> comboModalidad;
    private DefaultTableModel modeloDet = new DefaultTableModel(
            new String[]{"Producto", "Cantidad", "Subtotal"}, 0);
    private JTable tablaDet;
    private JLabel etiquetaTotal;
    private Pedido pedido;
    private boolean pedidoGuardado;

    private DefaultTableModel modeloPed = new DefaultTableModel(
            new String[]{"ID", "Fecha", "Modalidad", "Estado", "Total"}, 0);
    private JTable tablaPed;
    private JComboBox<EstadoPedido> comboEstado;
    private JTextArea areaDet;

    private JLabel etiquetaArchivos;

    public VentanaPrincipal() {
        setTitle("Sistema de Gestion de Pedidos - Restaurante");
        setSize(900, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        productos = ArchivoDatos.cargarProductos();
        pedidos = ArchivoDatos.cargarPedidos();

        tabs = new JTabbedPane();
        tabs.addTab("Inicio", crearPanelInicio());
        tabs.addTab("Productos", crearPanelProductos());
        tabs.addTab("Pedidos", crearPanelPedidos());
        tabs.addTab("Consulta de pedidos", crearPanelConsulta());
        tabs.addTab("Almacenamiento", crearPanelArchivos());
        add(tabs);

        actualizarTablaProductos();
        actualizarComboProductos();
        actualizarTablaPedidos();
        limpiarDetalle();
    }

    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Sistema de Gestion de Pedidos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        JTextArea texto = new JTextArea(
                "Restaurante: consumo en el establecimiento y pedidos a domicilio.\n\n"
                + "Use las pestanas para:\n"
                + "1. Registrar platos, bebidas y postres.\n"
                + "2. Armar pedidos con cantidades y calcular el total.\n"
                + "3. Consultar pedidos y cambiar su estado.\n"
                + "4. Guardar y recuperar los datos en archivos binarios.");
        texto.setEditable(false);
        texto.setFocusable(false);
        texto.setBackground(panel.getBackground());
        texto.setFont(new Font("Arial", Font.PLAIN, 15));
        panel.add(texto, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(boton("Gestionar productos", 1));
        botones.add(boton("Crear pedido", 2));
        botones.add(boton("Consultar pedidos", 3));
        botones.add(boton("Guardar / recuperar", 4));
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private JButton boton(String texto, final int indice) {
        JButton b = new JButton(texto);
        b.addActionListener(e -> tabs.setSelectedIndex(indice));
        return b;
    }

    private void soloTexto(JTextField campo) {
        campo.setDocument(new PlainDocument() {
            @Override
            public void insertString(int pos, String texto, AttributeSet a) throws BadLocationException {
                if (texto != null) {
                    super.insertString(pos, texto.replaceAll("[^a-zA-Z0-9 ]", ""), a);
                }
            }
        });
    }

    private void soloNumero(JTextField campo) {
        campo.setDocument(new PlainDocument() {
            @Override
            public void insertString(int pos, String texto, AttributeSet a) throws BadLocationException {
                if (texto != null) {
                    super.insertString(pos, texto.replaceAll("[^0-9.]", ""), a);
                }
            }
        });
    }

    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formulario = new JPanel(new GridLayout(5, 2, 5, 5));
        campoCodigo = new JTextField();
        campoNombre = new JTextField();
        campoPrecio = new JTextField();
        campoDetalle = new JTextField();
        comboCategoria = new JComboBox<CategoriaProducto>(CategoriaProducto.values());
        soloTexto(campoCodigo);
        soloTexto(campoNombre);
        soloNumero(campoPrecio);
        soloTexto(campoDetalle);

        formulario.add(new JLabel("Codigo:"));
        formulario.add(campoCodigo);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(campoNombre);
        formulario.add(new JLabel("Precio:"));
        formulario.add(campoPrecio);
        formulario.add(new JLabel("Categoria:"));
        formulario.add(comboCategoria);
        formulario.add(new JLabel("Detalle (tipo / tamano / presentacion):"));
        formulario.add(campoDetalle);

        JPanel botones = new JPanel(new FlowLayout());
        JButton registrar = new JButton("Registrar producto");
        registrar.addActionListener(e -> registrarProducto());
        JButton limpiar = new JButton("Limpiar campos");
        limpiar.addActionListener(e -> limpiarProducto());
        JButton eliminar = new JButton("Eliminar producto");
        eliminar.addActionListener(e -> eliminarProducto());
        botones.add(registrar);
        botones.add(limpiar);
        botones.add(eliminar);

        JPanel arriba = new JPanel(new BorderLayout());
        arriba.add(formulario, BorderLayout.CENTER);
        arriba.add(botones, BorderLayout.SOUTH);
        panel.add(arriba, BorderLayout.NORTH);
        tablaProd = new JTable(modeloProd);
        tablaProd.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(tablaProd), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelPedidos() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formulario = new JPanel(new GridLayout(4, 2, 5, 5));
        comboProductos = new JComboBox<Producto>();
        campoCantidad = new JTextField("1");
        comboModalidad = new JComboBox<ModalidadServicio>(ModalidadServicio.values());
        etiquetaTotal = new JLabel("0.00");

        formulario.add(new JLabel("Producto del catalogo:"));
        formulario.add(comboProductos);
        formulario.add(new JLabel("Cantidad:"));
        formulario.add(campoCantidad);
        formulario.add(new JLabel("Modalidad del servicio:"));
        formulario.add(comboModalidad);
        formulario.add(new JLabel("Total del pedido:"));
        formulario.add(etiquetaTotal);

        JPanel botones = new JPanel(new FlowLayout());
        JButton agregar = new JButton("Agregar producto");
        agregar.addActionListener(e -> agregarItem());
        JButton quitar = new JButton("Quitar seleccionado");
        quitar.addActionListener(e -> quitarItem());
        JButton nuevo = new JButton("Nuevo pedido");
        nuevo.addActionListener(e -> nuevoPedido());
        JButton guardar = new JButton("Guardar pedido");
        guardar.addActionListener(e -> guardarPedido());
        botones.add(agregar);
        botones.add(quitar);
        botones.add(nuevo);
        botones.add(guardar);

        JPanel arriba = new JPanel(new BorderLayout());
        arriba.add(formulario, BorderLayout.CENTER);
        arriba.add(botones, BorderLayout.SOUTH);
        panel.add(arriba, BorderLayout.NORTH);
        tablaDet = new JTable(modeloDet);
        tablaDet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(tablaDet), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelConsulta() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel arriba = new JPanel(new FlowLayout());
        comboEstado = new JComboBox<EstadoPedido>(EstadoPedido.values());
        JButton cambiar = new JButton("Actualizar estado");
        cambiar.addActionListener(e -> cambiarEstado());
        JButton eliminar = new JButton("Eliminar pedido");
        eliminar.addActionListener(e -> eliminarPedido());
        arriba.add(new JLabel("Nuevo estado:"));
        arriba.add(comboEstado);
        arriba.add(cambiar);
        arriba.add(eliminar);

        tablaPed = new JTable(modeloPed);
        tablaPed.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPed.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalle();
            }
        });
        JScrollPane lista = new JScrollPane(tablaPed);

        areaDet = new JTextArea(6, 40);
        areaDet.setEditable(false);
        JPanel abajo = new JPanel(new BorderLayout());
        abajo.add(new JLabel("Detalle del pedido seleccionado:"), BorderLayout.NORTH);
        abajo.add(new JScrollPane(areaDet), BorderLayout.CENTER);

        panel.add(arriba, BorderLayout.NORTH);
        panel.add(lista, BorderLayout.CENTER);
        panel.add(abajo, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelArchivos() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel titulo = new JLabel("Almacenamiento de informacion", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel botones = new JPanel(new FlowLayout());
        JButton guardar = new JButton("Guardar datos");
        guardar.addActionListener(e -> guardarDatos());
        JButton recuperar = new JButton("Recuperar datos");
        recuperar.addActionListener(e -> recuperarDatos());
        JButton borrar = new JButton("Borrar todos los datos");
        borrar.addActionListener(e -> borrarTodo());
        botones.add(guardar);
        botones.add(recuperar);
        botones.add(borrar);

        etiquetaArchivos = new JLabel("Archivos binarios: productos.dat y pedidos.dat", SwingConstants.CENTER);

        panel.add(titulo);
        panel.add(botones);
        panel.add(etiquetaArchivos);
        return panel;
    }

    private void registrarProducto() {
        String codigo = campoCodigo.getText().trim();
        String nombre = campoNombre.getText().trim();
        String detalle = campoDetalle.getText().trim();
        double precio;

        if (codigo.isEmpty() || nombre.isEmpty()) {
            mensaje("Ingrese el codigo y el nombre del producto.");
            return;
        }
        try {
            precio = Double.parseDouble(campoPrecio.getText().trim());
        } catch (NumberFormatException e) {
            mensaje("El precio debe ser un numero valido.");
            return;
        }
        if (precio <= 0) {
            mensaje("El precio debe ser mayor que cero.");
            return;
        }
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                mensaje("Ya existe un producto con ese codigo.");
                return;
            }
        }

        Producto nuevo = null;
        CategoriaProducto categoria = (CategoriaProducto) comboCategoria.getSelectedItem();
        if (categoria == CategoriaProducto.PLATO) {
            nuevo = new Plato(codigo, nombre, precio, detalle);
        } else if (categoria == CategoriaProducto.BEBIDA) {
            nuevo = new Bebida(codigo, nombre, precio, detalle);
        } else if (categoria == CategoriaProducto.POSTRE) {
            nuevo = new Postre(codigo, nombre, precio, detalle);
        }
        productos.add(nuevo);
        actualizarTablaProductos();
        actualizarComboProductos();
        limpiarProducto();
        mensaje("Producto registrado correctamente.");
    }

    private void limpiarProducto() {
        campoCodigo.setText("");
        campoNombre.setText("");
        campoPrecio.setText("");
        campoDetalle.setText("");
    }

    private void eliminarProducto() {
        int fila = tablaProd.getSelectedRow();
        if (fila < 0) {
            mensaje("Seleccione un producto de la tabla.");
            return;
        }
        Producto p = productos.get(fila);
        productos.remove(fila);
        actualizarTablaProductos();
        actualizarComboProductos();
        mensaje("Producto eliminado: " + p.getNombre());
    }

    private void eliminarPedido() {
        int fila = tablaPed.getSelectedRow();
        if (fila < 0) {
            mensaje("Seleccione un pedido de la tabla.");
            return;
        }
        Pedido p = pedidos.get(fila);
        pedidos.remove(fila);
        if (p == pedido) {
            pedido = null;
            pedidoGuardado = false;
            limpiarDetalle();
        }
        actualizarTablaPedidos();
        areaDet.setText("");
        mensaje("Pedido eliminado: " + p.getId());
    }

    private void borrarTodo() {
        productos.clear();
        pedidos.clear();
        pedido = null;
        pedidoGuardado = false;
        new File(ArchivoDatos.ARCHIVO_PRODUCTOS).delete();
        new File(ArchivoDatos.ARCHIVO_PEDIDOS).delete();
        actualizarTablaProductos();
        actualizarComboProductos();
        actualizarTablaPedidos();
        limpiarDetalle();
        areaDet.setText("");
        etiquetaArchivos.setText("Datos borrados. Las tablas quedan vacias.");
    }

    private void actualizarTablaProductos() {
        modeloProd.setRowCount(0);
        for (Producto p : productos) {
            modeloProd.addRow(new Object[]{
                p.getCodigo(), p.getNombre(), p.getCategoria().getTexto(),
                String.format("%.2f", p.getPrecio()), p.getDetalle()
            });
        }
    }

    private void actualizarComboProductos() {
        comboProductos.removeAllItems();
        for (Producto p : productos) {
            comboProductos.addItem(p);
        }
    }

    private void agregarItem() {
        if (comboProductos.getItemCount() == 0) {
            mensaje("Primero registre productos en el catalogo.");
            return;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(campoCantidad.getText().trim());
        } catch (NumberFormatException e) {
            mensaje("La cantidad debe ser un numero entero.");
            return;
        }
        if (cantidad <= 0) {
            mensaje("La cantidad debe ser mayor que cero.");
            return;
        }
        if (pedido == null) {
            String id = "PED-" + (pedidos.size() + 1);
            pedido = new Pedido(id, (ModalidadServicio) comboModalidad.getSelectedItem());
            pedidoGuardado = false;
        }
        Producto producto = (Producto) comboProductos.getSelectedItem();
        pedido.agregarItem(producto, cantidad);
        actualizarDetalle();
    }

    private void quitarItem() {
        int fila = tablaDet.getSelectedRow();
        if (pedido == null || fila < 0) {
            mensaje("Seleccione un articulo del detalle.");
            return;
        }
        pedido.eliminarItem(fila);
        actualizarDetalle();
    }

    private void nuevoPedido() {
        pedido = null;
        pedidoGuardado = false;
        limpiarDetalle();
        mensaje("Listo para crear un pedido nuevo.");
    }

    private void guardarPedido() {
        if (pedido == null || pedido.getItems().isEmpty()) {
            mensaje("No se puede guardar un pedido sin productos.");
            return;
        }
        pedido.setModalidad((ModalidadServicio) comboModalidad.getSelectedItem());
        if (!pedidoGuardado) {
            pedidos.add(pedido);
            pedidoGuardado = true;
        }
        actualizarTablaPedidos();
        mensaje("Pedido " + pedido.getId() + " guardado. Puede agregar mas productos.");
    }

    private void actualizarDetalle() {
        modeloDet.setRowCount(0);
        if (pedido != null) {
            for (ItemPedido item : pedido.getItems()) {
                modeloDet.addRow(new Object[]{
                    item.getProducto().getNombre(), item.getCantidad(),
                    String.format("%.2f", item.getSubtotal())
                });
            }
            etiquetaTotal.setText(String.format("%.2f", pedido.getTotal()));
        } else {
            etiquetaTotal.setText("0.00");
        }
    }

    private void limpiarDetalle() {
        modeloDet.setRowCount(0);
        etiquetaTotal.setText("0.00");
    }

    private void actualizarTablaPedidos() {
        modeloPed.setRowCount(0);
        for (Pedido p : pedidos) {
            modeloPed.addRow(new Object[]{
                p.getId(), p.getFecha(), p.getModalidad().getTexto(),
                p.getEstado().getTexto(), String.format("%.2f", p.getTotal())
            });
        }
    }

    private void cambiarEstado() {
        int fila = tablaPed.getSelectedRow();
        if (fila < 0) {
            mensaje("Seleccione un pedido de la tabla.");
            return;
        }
        Pedido p = pedidos.get(fila);
        p.setEstado((EstadoPedido) comboEstado.getSelectedItem());
        actualizarTablaPedidos();
        tablaPed.setRowSelectionInterval(fila, fila);
        mostrarDetalle();
        mensaje("Estado actualizado a: " + p.getEstado().getTexto());
    }

    private void mostrarDetalle() {
        int fila = tablaPed.getSelectedRow();
        if (fila < 0 || fila >= pedidos.size()) {
            areaDet.setText("");
            return;
        }
        Pedido p = pedidos.get(fila);
        String texto = "Pedido: " + p.getId() + "     Fecha: " + p.getFecha() + "\n"
                + "Modalidad: " + p.getModalidad().getTexto() + "\n"
                + "Estado: " + p.getEstado().getTexto() + "\n\n";
        for (ItemPedido item : p.getItems()) {
            texto += item.getCantidad() + " x " + item.getProducto().getNombre()
                    + "     Subtotal: " + String.format("%.2f", item.getSubtotal()) + "\n";
        }
        texto += "\nTOTAL: " + String.format("%.2f", p.getTotal());
        areaDet.setText(texto);
    }

    private void guardarDatos() {
        try {
            ArchivoDatos.guardarProductos(productos);
            ArchivoDatos.guardarPedidos(pedidos);
            etiquetaArchivos.setText("Datos guardados: " + productos.size() + " productos y "
                    + pedidos.size() + " pedidos.");
        } catch (IOException e) {
            etiquetaArchivos.setText("Error al guardar: " + e.getMessage());
        }
    }

    private void recuperarDatos() {
        productos = ArchivoDatos.cargarProductos();
        pedidos = ArchivoDatos.cargarPedidos();
        pedido = null;
        pedidoGuardado = false;
        actualizarTablaProductos();
        actualizarComboProductos();
        actualizarTablaPedidos();
        limpiarDetalle();
        areaDet.setText("");
        etiquetaArchivos.setText("Datos recuperados: " + productos.size() + " productos y "
                + pedidos.size() + " pedidos.");
    }

    private void mensaje(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }
}
