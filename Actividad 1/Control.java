package paqueteControlAsistenciaEstudiantes;

import java.util.Scanner;

public class Control {

	public static void main(String[] args) {
		
		final int DIAS_SEMANA = 5;
		final int NUM_ESTUDIANTES = 4;
		
		Scanner Lista = new Scanner(System.in); 
		
		System.out.print("Ingrese el número de asistencias (mínimo 1): ");
        int numAsistencias = Lista.nextInt();

        while (numAsistencias < 1) {
            System.out.print("Debe ingresar al menos 1 producto, intente de nuevo: ");
            numAsistencias = Lista.nextInt();
        }

        String[]P_Presente,A_ausente= new String[6];
        int[] Asistencia = new int[6];

        double totalSinAsistencia = 0;
        double totalConAsistencia = 0;

        int i = 0;
        while (i < numAsistencias) {
            System.out.println("Asistencia #" + (i + 1));

            System.out.print("Ingrese el nombre del estudiante: ");
            String nombre = Lista.next(); 

            System.out.print("Ingrese el tipo (1: AsistenciaPorEstudiante, 2: EstudiantesAsistenciaCompleta, 3: DiasMayorAsistencia): ");
            int tipo = Lista.nextInt();

            System.out.print("Ingrese el Estudiante: ");
            int Estudiante = Lista.nextInt();

            switch (tipo) {
                case 1:
                	numAsistencias = DIAS_SEMANA;
                    break;
                case 2:
                    numAsistencias = NUM_ESTUDIANTES;
                    break;
                default:
                    System.out.println("Tipo no válido, no se aplica descuento.");
            }
        }
       
        System.out.println("--- MENU OPCIONES ---");
        System.out.println("1. Ver Asistencia Individual: ");
        System.out.println("2. Ver resumen general: ");
        System.out.println("3. Volver a Registar: ");
        System.out.println("4. salir");

        Lista.close();
    }
        
	}

