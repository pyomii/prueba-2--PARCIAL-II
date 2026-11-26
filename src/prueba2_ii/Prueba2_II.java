package prueba2_ii;

import javax.swing.SwingUtilities;
import prueba2_ii.gui.VentanaPrincipal;

public class Prueba2_II {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }
}
