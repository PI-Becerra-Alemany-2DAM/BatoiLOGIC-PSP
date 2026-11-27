import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public class ProcesoExportarYActualizar {

    // Objeto monitor para garantizar exclusión mutua
    private static final Object LOCK = new Object();

    public static void main(String[] args) {
        System.out.println("[PROCESO HIJO] Arrancando exportación y actualización de pedidos...");

        // Sección crítica protegida con synchronized
        synchronized (LOCK) {
            try {
                // PASO 1: Exportar pedidos pendientes del día
                exportarComandes();

                // PASO 2: Actualizar el estado de esos pedidos
                actualizarEstadoComandes();

                System.out.println("[PROCESO HIJO] Tareas finalizadas con éxito.");
                System.exit(0); // Código 0 = todo correcto para ProcessBuilder

            } catch (Exception e) {
                System.err.println("[PROCESO HIJO] Error durante la ejecución: " + e.getMessage());
                System.exit(1); // Código 1 = error
            }
        }
    }

    private static void exportarComandes() throws IOException {
        String nombreFichero = "comandes_export_" + LocalDate.now() + ".csv";

        // Aquí conectaríamos con la BD (SELECT * FROM comandes WHERE estado = 'PENDIENTE')
        // De momento genera el fichero CSV en la raíz del proyecto como prueba funcional:
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreFichero))) {
            writer.println("ID_COMANDA;CLIENTE;FECHA;ESTADO_ANTERIOR");
            writer.println("101;Cliente Demo 1;" + LocalDate.now() + ";PENDIENTE");
            writer.println("102;Cliente Demo 2;" + LocalDate.now() + ";PENDIENTE");
        }

        System.out.println("[PROCESO HIJO] Pedidos exportados a: " + nombreFichero);
    }

    private static void actualizarEstadoComandes() {
        // Aquí iría nuestro UPDATE en la BD de Acceso a Datos:
        // UPDATE comandes SET estado = 'EN_PREPARACION' WHERE estado = 'PENDIENTE'
        System.out.println("[PROCESO HIJO] Estados actualizados a 'EN_PREPARACION'.");
    }
}