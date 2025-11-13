package paqueteCasoEstudio;

import java.util.Scanner;

public class Estudia {

    public static final double DESC_ROPA = 0.10;       
    public static final double DESC_TECNO = 0.20;      
    public static final double DESC_ALIM = 0.05;       
    public static final double DESC_EXTRA = 0.05;     

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese el número de productos a comprar (mínimo 1): ");
        int numProductos = sc.nextInt();

        while (numProductos < 1) {
            System.out.print("Debe ingresar al menos 1 producto, intente de nuevo: ");
            numProductos = sc.nextInt();
        }

        double[] precios = new double[numProductos];

        double[] preciosConDesc = new double[numProductos];

        int i = 0;
        while (i < numProductos) {
            System.out.println("\nProducto #" + (i + 1));

            System.out.print("Ingrese el nombre del producto: ");
            String nombre = sc.next();

            System.out.print("Ingrese el tipo (1: Ropa, 2: Tecnología, 3: Alimentos): ");
            int tipo = sc.nextInt();

            System.out.print("Ingrese el precio: ");
            double precio = sc.nextDouble();

            precios[i] = precio; 

            double precioFinal = precio; 

            switch (tipo) {
                case 1:
                    precioFinal = precio - (precio * DESC_ROPA);
                    break;
                case 2:
                    precioFinal = precio - (precio * DESC_TECNO);
                    break;
                case 3:
                    precioFinal = precio - (precio * DESC_ALIM);
                    break;
                default:
                    System.out.println("Tipo no válido, no se aplica descuento.");
            }

            preciosConDesc[i] = precioFinal;
            i++;
        }

        double totalSinDesc = 0;
        double totalConDesc = 0;

        for (int j = 0; j < numProductos; j++) {
            totalSinDesc += precios[j];
            totalConDesc += preciosConDesc[j];
        }

        if (totalConDesc > 500000) {
            totalConDesc = totalConDesc - (totalConDesc * DESC_EXTRA);
        }

        double ahorro = totalSinDesc - totalConDesc;

        System.out.println("\n--- RESUMEN DE COMPRA ---");
        System.out.println("Total sin descuento: $" + totalSinDesc);
        System.out.println("Total con descuento: $" + totalConDesc);
        System.out.println("Ahorro total: $" + ahorro);

        sc.close();
    }
}
