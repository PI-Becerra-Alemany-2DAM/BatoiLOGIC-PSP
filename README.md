# ⚙️ Servicio en Segundo Plano - Automatización de Pedidos (PSP)

Módulo de **Programación de Servicios y Procesos (2º DAM)** desarrollado en Java para el proyecto intermodular (1ª Evaluación). 

**Autores:** Pepe Alemany Márquez y Adrián Becerra Pérez.

Implementa un servicio en segundo plano (*demonio*) encargado de ejecutar diariamente a las **06:00 AM** un proceso externo independiente para exportar los pedidos pendientes a formato CSV y actualizar su estado de preparación.

---

## 🏗️ Arquitectura y Conceptos Aplicados

El proyecto está estructurado siguiendo los principios de programación multiproceso y multihilo:

* **Planificación sin espera activa (`ScheduledExecutorService`):** Uso de un *thread pool* programado que comprueba la franja horaria cada 30 segundos sin bloquear la CPU ni recurrir a bucles infinitos.
* **Lanzamiento de procesos externos (`ProcessBuilder`):** Ejecución aislada de la tarea de exportación en un proceso hijo de la JVM, resolviendo dinámicamente `java.home` y el `classpath` del entorno activo y sincronizando su finalización mediante `waitFor()`.
* **Control de concurrencia (`synchronized`):** Protección de la sección crítica de lectura/escritura mediante un objeto monitor (`LOCK`) para garantizar la exclusión mutua y evitar condiciones de carrera.
* **Cierre controlado (`ShutdownHook`):** Captura de señales de interrupción del sistema para detener el *scheduler* de forma limpia antes de finalizar la JVM.

---

## 📂 Estructura del Proyecto

```text
ProyectoPSP_1EVAL/
├── src/
│   ├── MainPSP.java                      # Punto de entrada: inicializa el scheduler y el ShutdownHook
│   ├── ServicioComandes.java             # Tarea Runnable que verifica la hora (06:00) y lanza el proceso hijo
│   └── ProcesoExportarYActualizar.java   # Proceso independiente que exporta el CSV y actualiza estados
├── .gitignore                            # Exclusión de binarios (.class) y ficheros CSV generados
└── README.md                             # Documentación del módulo
