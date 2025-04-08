import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VentaEntradasTeatroMoroo {
    public static void main(String[] args) {
        Teatro teatroMoro = new Teatro("Teatro Moro", 50);
        teatroMoro.iniciarSistema();
    }
}

// Clase que representa un asiento
class Asiento {
    private int numero;
    private boolean ocupado;
    private Zona zona;
    
    public Asiento(int numero, Zona zona) {
        this.numero = numero;
        this.ocupado = false;
        this.zona = zona;
    }
    
    public int getNumero() {
        return numero;
    }
    
    public boolean isOcupado() {
        return ocupado;
    }
    
    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }
    
    public Zona getZona() {
        return zona;
    }
    
    @Override
    public String toString() {
        return ocupado ? " X " : String.format("%2d ", numero);
    }
}

// Clase de una zona
class Zona {
    private String nombre;
    private double tarifa;
    private char codigo;
    private int asientoInicio;
    private int asientoFin;
    
    public Zona(String nombre, double tarifa, char codigo, int asientoInicio, int asientoFin) {
        this.nombre = nombre;
        this.tarifa = tarifa;
        this.codigo = codigo;
        this.asientoInicio = asientoInicio;
        this.asientoFin = asientoFin;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public double getTarifa() {
        return tarifa;
    }
    
    public char getCodigo() {
        return codigo;
    }
    
    public int getAsientoInicio() {
        return asientoInicio;
    }
    
    public int getAsientoFin() {
        return asientoFin;
    }
    
    public boolean contieneAsiento(int numeroAsiento) {
        return numeroAsiento >= asientoInicio && numeroAsiento <= asientoFin;
    }
    
    @Override
    public String toString() {
        return codigo + ". Zona " + nombre + " - $" + tarifa + " (Asientos " + asientoInicio + " al " + asientoFin + ")";
    }
}

class Cliente {
    public static final int TIPO_ESTUDIANTE = 1;
    public static final int TIPO_TERCERA_EDAD = 2;
    public static final int TIPO_GENERAL = 3;
    
    private int tipo;
    private int edad;
    
    public Cliente(int tipo, int edad) {
        this.tipo = tipo;
        this.edad = edad;
    }
    
    public double calcularDescuento() {
        double descuento = 0;
        
        // Descuento por tipo de cliente
        if (tipo == TIPO_ESTUDIANTE) {
            descuento = 0.10; // 10% descuento
        } else if (tipo == TIPO_TERCERA_EDAD) {
            descuento = 0.15; // 15% descuento
        }
        
        // Descuento adicional por edad
        if (edad < 12) {
            descuento += 0.05; // Adicional 5% para niños
        }
        
        return descuento;
    }
    
    public String getTipoCliente() {
        switch(tipo) {
            case TIPO_ESTUDIANTE: return "Estudiante";
            case TIPO_TERCERA_EDAD: return "Tercera Edad";
            default: return "Público General";
        }
    }
}

// Entrada
class Entrada {
    private Asiento asiento;
    private Cliente cliente;
    private double precioFinal;
    
    public Entrada(Asiento asiento, Cliente cliente) {
        this.asiento = asiento;
        this.cliente = cliente;
        this.precioFinal = calcularPrecioFinal();
        this.asiento.setOcupado(true);
    }
    
    private double calcularPrecioFinal() {
        double descuento = cliente.calcularDescuento();
        return asiento.getZona().getTarifa() * (1 - descuento);
    }
    
    public Asiento getAsiento() {
        return asiento;
    }
    
    public double getPrecioFinal() {
        return precioFinal;
    }
    
    public Zona getZona() {
        return asiento.getZona();
    }
    
    public Cliente getCliente() {
        return cliente;
    }
}

// Manejo del teatro y sistema
class Teatro {
    private static final double TARIFA_ZONA_VIP = 15.0;
    private static final double TARIFA_ZONA_MEDIA = 10.0;
    private static final double TARIFA_ZONA_ECONOMICA = 7.5;
    
    private String nombre;
    private List<Asiento> asientos;
    private List<Zona> zonas;
    
    public Teatro(String nombre, int totalAsientos) {
        this.nombre = nombre;
        this.asientos = new ArrayList<>();
        
        // Inicializar zonas
        this.zonas = new ArrayList<>();
        Zona zonaVIP = new Zona("VIP", TARIFA_ZONA_VIP, 'V', 1, 10);
        Zona zonaMedia = new Zona("Media", TARIFA_ZONA_MEDIA, 'M', 11, 40);
        Zona zonaEconomica = new Zona("Económica", TARIFA_ZONA_ECONOMICA, 'E', 41, 50);
        
        zonas.add(zonaVIP);
        zonas.add(zonaMedia);
        zonas.add(zonaEconomica);
        
        //  asientos zonas
        for (int i = 1; i <= totalAsientos; i++) {
            Zona zonaAsiento = null;
            
            for (Zona zona : zonas) {
                if (zona.contieneAsiento(i)) {
                    zonaAsiento = zona;
                    break;
                }
            }
            
            if (zonaAsiento != null) {
                asientos.add(new Asiento(i, zonaAsiento));
            }
        }
    }
    
    public void iniciarSistema() {
        Scanner scanner = new Scanner(System.in);
        boolean continuarPrograma = true;
      
        for (int i = 0; continuarPrograma; i++) {
            mostrarMenuPrincipal();
            int opcion = leerOpcion(scanner);
            
            switch (opcion) {
                case 1:
                    procesarCompra(scanner);
                    break;
                case 2:
                    mostrarAsientosDisponibles();
                    break;
                case 3:
                    System.out.println("¡Gracias por preferirnos! ¡" + nombre + " agradece su preferencia!");
                    continuarPrograma = false;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción del menú.");
            }
        }
        
        scanner.close();
    }
    
    private void mostrarMenuPrincipal() {
        System.out.println("\n=== " + nombre.toUpperCase() + " - SISTEMA DE VENTA DE ENTRADAS ===");
        System.out.println("1. Comprar entrada");
        System.out.println("2. Ver asientos disponibles");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opción: ");
    }
    
    private int leerOpcion(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un número válido.");
            return -1;
        }
    }
    
    private void mostrarAsientosDisponibles() {
        System.out.println("\n===   ASIENTOS DISPONIBLES     ===");
        System.out.println("            PANTALLA              ");
        System.out.println("---------------------------------");
        
        // Fila VIP
        for (int i = 1; i <= 10; i++) {
            Asiento asiento = asientos.get(i - 1);
            String display = asiento.isOcupado() ? " X " : String.format("%2d ", asiento.getNumero());
            System.out.print(display);
        }
        System.out.println();
        System.out.println();
        
        // Fila Media
        for (int i = 11; i <= 30; i++) {
            Asiento asiento = asientos.get(i - 1);
            String display = asiento.isOcupado() ? " X " : String.format("%2d ", asiento.getNumero());
            System.out.print(display);
            
            if (i == 20) {
                System.out.println();
            }
        }
        System.out.println();
        
        // Fila Media
        for (int i = 31; i <= 40; i++) {
            Asiento asiento = asientos.get(i - 1);
            String display = asiento.isOcupado() ? " X " : String.format("%2d ", asiento.getNumero());
            System.out.print(display);
        }
        System.out.println();
        System.out.println();
        
        // Fila Económica
        for (int i = 41; i <= 50; i++) {
            Asiento asiento = asientos.get(i - 1);
            String display = asiento.isOcupado() ? " X " : String.format("%2d ", asiento.getNumero());
            System.out.print(display);
        }
        System.out.println();
        
        System.out.println("----------------------------------");
        System.out.println("Zonas:");
        System.out.println("V - VIP (Asientos 1-10)");
        System.out.println("M - Media (Asientos 11-40)");
        System.out.println("E - Económica (Asientos 41-50)");
        
        System.out.println("\nLeyenda: X = Ocupado, Número = Disponible");
        System.out.println("Asientos disponibles: " + contarAsientosDisponibles());
    }
    
    private int contarAsientosDisponibles() {
        int contador = 0;
        for (Asiento asiento : asientos) {
            if (!asiento.isOcupado()) {
                contador++;
            }
        }
        return contador;
    }
    
    private void procesarCompra(Scanner scanner) {
        System.out.println("\n=== COMPRA DE ENTRADAS ===");
        
        mostrarAsientosDisponibles();
        
        // Solicitar la cantidad de entradas a comprar
        System.out.print("¿Cuántas entradas desea comprar? ");
        int cantidadEntradas = leerEntero(scanner);
        if (cantidadEntradas <= 0) {
            System.out.println("La cantidad de entradas debe ser mayor a 0.");
            return;
        }
        
        // Disponibilidad de asientos
        int asientosDisponibles = contarAsientosDisponibles();
        if (cantidadEntradas > asientosDisponibles) {
            System.out.println("No hay suficientes asientos disponibles. Asientos disponibles: " + asientosDisponibles);
            return;
        }
        
        // Seleccionar zona
        Zona zonaSeleccionada = seleccionarZona(scanner);
        if (zonaSeleccionada == null) return;
        
        // Verificar disponibilidad en la zona seleccionada
        int asientosDisponiblesEnZona = contarAsientosDisponiblesEnZona(zonaSeleccionada);
        if (cantidadEntradas > asientosDisponiblesEnZona) {
            System.out.println("No hay suficientes asientos disponibles en la zona " + 
                              zonaSeleccionada.getNombre() + ". Asientos disponibles: " + asientosDisponiblesEnZona);
            return;
        }
        
        // Crear cliente
        Cliente cliente = crearCliente(scanner);
        if (cliente == null) return;
        
        // Seleccionar asientos y crear entradas
        List<Entrada> entradas = new ArrayList<>();
        for (int i = 0; i < cantidadEntradas; i++) {
            Asiento asiento = seleccionarAsientoEnZona(scanner, zonaSeleccionada, i + 1, cantidadEntradas);
            if (asiento == null) return;
            
            entradas.add(new Entrada(asiento, cliente));
        }
        
        // Mostrar resumen de compra
        mostrarResumenCompra(entradas, cliente);
    }
    
    private int contarAsientosDisponiblesEnZona(Zona zona) {
        int contador = 0;
        for (Asiento asiento : asientos) {
            if (!asiento.isOcupado() && asiento.getZona() == zona) {
                contador++;
            }
        }
        return contador;
    }
    
    private int leerEntero(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un número válido.");
            return -1;
        }
    }
    
    private Zona seleccionarZona(Scanner scanner) {
        System.out.println("\nZonas disponibles:");
        for (Zona zona : zonas) {
            System.out.println(zona);
        }
        
        while (true) {
            System.out.print("Seleccione la zona (V/M/E): ");
            String eleccion = scanner.nextLine().toUpperCase();
            
            if (eleccion.length() != 1) {
                System.out.println("Error: Ingrese solo una letra.");
                continue;
            }
            
            char codigo = eleccion.charAt(0);
            for (Zona zona : zonas) {
                if (zona.getCodigo() == codigo) {
                    return zona;
                }
            }
            
            System.out.println("Error: Zona no válida. Intente nuevamente.");
        }
    }
    
    private Cliente crearCliente(Scanner scanner) {
        // Tipo de cliente
        System.out.println("\nTipos de cliente:");
        System.out.println("1. Estudiante (10% descuento)");
        System.out.println("2. Tercera edad (15% descuento)");
        System.out.println("3. Público General");
        
        int tipoCliente;
        while (true) {
            System.out.print("Ingrese el tipo de cliente (1/2/3): ");
            tipoCliente = leerEntero(scanner);
            
            if (tipoCliente >= 1 && tipoCliente <= 3) {
                break;
            }
            
            System.out.println("Error: Tipo de cliente no válido. Por favor seleccione 1, 2 o 3.");
        }
        
        // Solicitar edad
        int edad;
        while (true) {
            System.out.print("Ingrese su edad: ");
            edad = leerEntero(scanner);
            
            if (edad > 0 && edad <= 120) {
                break;
            }
            
            System.out.println("Error: Edad no válida. Debe estar entre 1 y 120 años.");
        }
        
        return new Cliente(tipoCliente, edad);
    }
    
    private Asiento seleccionarAsientoEnZona(Scanner scanner, Zona zona, int actual, int total) {
        // Mostrar asientos de la zona seleccionada 
        System.out.println("\nAsientos disponibles en Zona " + zona.getNombre() + ":");
        
        // Mostrar asientos de la zona seleccionada 
        if (zona.getCodigo() == 'V') {
            // Zona VIP (1-10)
            for (int i = 1; i <= 10; i++) {
                Asiento asiento = asientos.get(i - 1);
                String display = asiento.isOcupado() ? " X " : String.format("%2d ", asiento.getNumero());
                System.out.print(display);
            }
            System.out.println();
        } else if (zona.getCodigo() == 'M') {
            // Zona Media (11-40)
            for (int i = 11; i <= 40; i++) {
                Asiento asiento = asientos.get(i - 1);
                String display = asiento.isOcupado() ? " X " : String.format("%2d ", asiento.getNumero());
                System.out.print(display);
                
                if ((i - 10) % 10 == 0) {
                    System.out.println();
                }
            }
        } else if (zona.getCodigo() == 'E') {
            // Zona Económica (41-50)
            for (int i = 41; i <= 50; i++) {
                Asiento asiento = asientos.get(i - 1);
                String display = asiento.isOcupado() ? " X " : String.format("%2d ", asiento.getNumero());
                System.out.print(display);
            }
            System.out.println();
        }
        
        while (true) {
            System.out.print("\nSeleccione el asiento " + actual + " de " + total + " (entre " + 
                            zona.getAsientoInicio() + " y " + zona.getAsientoFin() + "): ");
            int numeroAsiento = leerEntero(scanner);
            
            if (numeroAsiento < zona.getAsientoInicio() || numeroAsiento > zona.getAsientoFin()) {
                System.out.println("Error: Número de asiento no válido. Debe estar entre " + 
                                  zona.getAsientoInicio() + " y " + zona.getAsientoFin());
                continue;
            }
            
            Asiento asiento = asientos.get(numeroAsiento - 1);
            if (asiento.isOcupado()) {
                System.out.println("Error: El asiento " + numeroAsiento + " ya está ocupado.");
                continue;
            }
            
            return asiento;
        }
    }
    
    private void mostrarResumenCompra(List<Entrada> entradas, Cliente cliente) {
        double total = 0;
        Zona zona = entradas.get(0).getZona(); 
        
        System.out.println("\n=== RESUMEN DE COMPRA ===");
        System.out.println("Ubicación: " + zona.getNombre());
        System.out.println("Tipo de cliente: " + cliente.getTipoCliente());
        System.out.println("Cantidad de entradas: " + entradas.size());
        
        System.out.print("Asientos seleccionados: ");
        for (Entrada entrada : entradas) {
            System.out.print(entrada.getAsiento().getNumero() + " ");
            total += entrada.getPrecioFinal();
        }
        
        System.out.println("Precio base por entrada: $" + zona.getTarifa());
        System.out.println("Descuento aplicado: " + (cliente.calcularDescuento() * 100) + "%");
        System.out.println("Precio final por entrada: $" + entradas.get(0).getPrecioFinal());
        System.out.println("Total a pagar: $" + total);
        System.out.println("¡Disfrute de la función! ¡Teatro Moro agradece su preferencia!");
    }
}