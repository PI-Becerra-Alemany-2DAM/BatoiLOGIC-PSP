import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainPSP {

    public static void main(String[] args) {
        // ScheduledExecutorService soporta tareas futuras o repetitivas
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        ServicioComandes servicio = new ServicioComandes();

        System.out.println("Servicio en segundo plano iniciado. Esperando a las 06:00...");

        // Ejecuta la comprobación inmediatamente (delay 0) y la repite cada 30 segundos
        // Así nunca se nos pasa el minuto 06:00 y evitamos la espera activa
        scheduler.scheduleAtFixedRate(servicio, 0, 30, TimeUnit.SECONDS);

        // Hook para cerrar el scheduler limpiamente al detener el programa
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Deteniendo servicio en segundo plano...");
            scheduler.shutdown();
        }));
    }
}