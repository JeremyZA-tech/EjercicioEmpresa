package vista;

import io.IO;

public class Menu {
	
    public void mostrarMenu() {

        while (true) {
            System.out.println("Seleccione una tabla:");
            System.out.println("1. Departamento");
            System.out.println("2. Empleado");
            System.out.println("0. Salir");

            int opcion = IO.readInt();

            if (opcion == 0) {
                break;
            }

            switch (opcion) {
                case 1:
                    menuDepartamento();
                    break;
                case 2:
                    menuEmpleado();
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }
        }

        System.out.println("Saliendo del programa.");
    }

    private void menuDepartamento() {

        while (true) {
            System.out.println("Menú Departamento:");
            System.out.println("1. Añadir Departamento");
            System.out.println("2. Mostrar Tabla Departamento");
            System.out.println("3. Eliminar Dato de la Tabla Departamento");
            System.out.println("4. Actualizar Dato de la Tabla Departamento");
            System.out.println("0. Volver al Menú Principal");

            int opcion = IO.readInt();

            if (opcion == 0) {
                break;
            }

            switch (opcion) {
                case 1:

                	break;
                case 2:

                	break;
                case 3:

                	break;
                case 4:
                	
                	break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }
        }
    }

    private void menuEmpleado() {

        while (true) {
            System.out.println("Menú Empleado:");
            System.out.println("1. Añadir Empleado");
            System.out.println("2. Mostrar Tabla Empleado");
            System.out.println("3. Eliminar Dato de la Tabla Empleado");
            System.out.println("4. Actualizar Dato de la Tabla Empleado");
            System.out.println("0. Volver al Menú Principal");

            int opcion = IO.readInt();

            if (opcion == 0) {
                break;
            }

            switch (opcion) {
                case 1:

                	break;
                case 2:

                	break;
                case 3:

                	break;
                case 4:

                	break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }
        }
    }
}
