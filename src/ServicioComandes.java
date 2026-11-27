import java.time.LocalDate;
import java.time.LocalTime;

// Implementamos Runnable
public class ServicioComandes implements Runnable {

    private LocalDate ultimaEjecucion = null;

    @Override
    public void run() {
        LocalTime ahora = LocalTime.now();
        LocalDate hoy = LocalDate.now();

        // Comprobamos si son las 06:00 (entre 06:00 y 06:01) y no se ha ejecutado hoy
        if (ahora.getHour() == 6 && ahora.getMinute() == 0 && !hoy.equals(ultimaEjecucion)) {
            System.out.println("[" + Thread.currentThread().getName() + "] Son las 06:00. Iniciando automatización de pedidos...");

            ultimaEjecucion = hoy; // Marcamos que hoy ya se ha lanzado
            lanzarProcesoExterno();
        }
    }

    private void lanzarProcesoExterno() {
        try {
            //ProcessBuilder lanzando la clase compilada dentro de la carpeta "bin"
            ProcessBuilder pb = new ProcessBuilder("java", "-cp", "bin", "ProcesoExportarYActualizar");

            // Redirigimos la salida y errores del proceso hijo a la consola de este servicio
            pb.inheritIO();

            // Arrancamos el proceso hijo
            Process proceso = pb.start();

            // Esperamos a que el proceso externo termine antes de continuar
            int codigoSalida = proceso.waitFor();

            if (codigoSalida == 0) {
                System.out.println("[SERVICIO] Exportación y actualización completadas correctamente.");
            } else {
                System.err.println("[SERVICIO] El proceso terminó con error. Código: " + codigoSalida);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}