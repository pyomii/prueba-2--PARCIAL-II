package prueba2_ii.datos;

import java.io.*;
import java.util.ArrayList;
import prueba2_ii.modelo.Pedido;
import prueba2_ii.modelo.Producto;

public class ArchivoDatos {
    public static final String ARCHIVO_PRODUCTOS = "productos.dat";
    public static final String ARCHIVO_PEDIDOS = "pedidos.dat";

    public static void guardarProductos(ArrayList<Producto> productos) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO_PRODUCTOS));
        out.writeObject(productos);
        out.close();
    }

    public static void guardarPedidos(ArrayList<Pedido> pedidos) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO_PEDIDOS));
        out.writeObject(pedidos);
        out.close();
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Producto> cargarProductos() {
        ArrayList<Producto> lista = new ArrayList<Producto>();
        File archivo = new File(ARCHIVO_PRODUCTOS);
        if (!archivo.exists()) {
            return lista;
        }
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo));
            lista = (ArrayList<Producto>) in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se pudieron leer los productos: " + e.getMessage());
        }
        return lista;
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Pedido> cargarPedidos() {
        ArrayList<Pedido> lista = new ArrayList<Pedido>();
        File archivo = new File(ARCHIVO_PEDIDOS);
        if (!archivo.exists()) {
            return lista;
        }
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo));
            lista = (ArrayList<Pedido>) in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se pudieron leer los pedidos: " + e.getMessage());
        }
        return lista;
    }
}
