package GaussJordan;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ProyectoGaussJordan {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("***RESOLUCIÓN DE SISTEMA DE ECUACIONES***\n");

        while (true) {
            int n = obtenerDimension(scanner);
            if (n == -1) {
                break;
            }

            double[][] matrix = new double[n][n + 1];
            if (!ingresarValoresMatriz(scanner, matrix, n)) {
                break;
            }

            System.out.println("\nMatriz ingresada:");
            printMatrix(matrix, n);

            gaussSimple(matrix);
            System.out.println("\nResolución de Gauss Simple:");
            printMatrix(matrix, n);

            gaussJordan(matrix);
            System.out.println("\nResolución de Gauss-Jordan:");
            printMatrix(matrix, n);

            System.out.println("\nValores de X:");
            for (int i = 0; i < n; i++) {
                double solucion = matrix[i][n];
                String fraccion = decimalAFraccion(solucion);
                if (solucion == (int) solucion) {
                    System.out.printf("x%d = %d (%s)\n", i + 1, (int) solucion, fraccion);
                } else {
                    System.out.printf("x%d = %s (%s)\n", i + 1, fraccion, decimalAFraccion(solucion));
                }

            }

            System.out.print("\nPresione Enter para resolver otro problema: ");
            String respuesta = scanner.nextLine();
            if (!respuesta.isEmpty()) {
                break;
            }
        }

        scanner.close();
    }

    public static int obtenerDimension(Scanner scanner) {
        while (true) {
            System.out.print("Ingrese la dimensión de la matriz (Dimensión entre 2 y 4): ");
            try {
                int n = scanner.nextInt();
                scanner.nextLine();
                if (n >= 2 && n <= 4) {
                    return n;
                } else {
                    System.out.println("No es posible trabajar para esta dimensión. Debe ser entre 2 y 4.");
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("No es posible trabajar con letras. Ingrese un número.");
            }
        }
    }

    public static boolean ingresarValoresMatriz(Scanner scanner, double[][] matrix, int n) {
        System.out.println("Ingrese los elementos de la matriz:");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n + 1; j++) {
                while (true) {
                    System.out.printf("Ingrese el elemento para D[%d][%d] : ", i + 1, j + 1);
                    String input = scanner.nextLine();
                    if (input.isEmpty()) {
                        matrix[i][j] = 0;
                        break;
                    }
                    try {
                        matrix[i][j] = Double.parseDouble(input);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("No es posible trabajar con letras. Ingrese un número.");
                    }
                }
            }
        }
        return true;
    }

    public static void gaussSimple(double[][] matrix) {
        int n = matrix.length;

        for (int i = 0; i < n; i++) {
            if (matrix[i][i] == 0) {
                System.out.println("Pivote cero encontrado en la fila " + (i + 1) + ". No se puede resolver el sistema.");
                return;
            }

            double pivot = matrix[i][i];
            for (int j = 0; j < n + 1; j++) {
                matrix[i][j] /= pivot;
            }

            for (int j = i + 1; j < n; j++) {
                double factor = matrix[j][i];
                for (int k = 0; k < n + 1; k++) {
                    matrix[j][k] -= factor * matrix[i][k];
                }
            }
        }
    }

    public static void gaussJordan(double[][] matrix) {
        int n = matrix.length;

        for (int i = 0; i < n; i++) {
            if (matrix[i][i] == 0) {
                for (int j = i + 1; j < n; j++) {
                    if (matrix[j][i] != 0) {
                        double[] temp = matrix[i];
                        matrix[i] = matrix[j];
                        matrix[j] = temp;
                        break;
                    }
                }
            }

            double pivot = matrix[i][i];
            for (int j = 0; j < n + 1; j++) {
                matrix[i][j] /= pivot;
            }

            for (int j = 0; j < n; j++) {
                if (j != i) {
                    double factor = matrix[j][i];
                    for (int k = 0; k < n + 1; k++) {
                        matrix[j][k] -= factor * matrix[i][k];
                    }
                }
            }
        }
    }

    public static void printMatrix(double[][] matrix, int n) {
        int m = matrix.length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (Math.abs(matrix[i][j]) < 1e-10) {
                    matrix[i][j] = 0;
                }
                String fraccion = decimalAFraccion(matrix[i][j]);
                System.out.printf("%8s ", fraccion);
            }
            String fraccion = decimalAFraccion(matrix[i][n]);
            System.out.printf("| %8s\n", fraccion);
        }
    }

    public static String decimalAFraccion(double decimal) {
        final int maxDenominador = 1000000;
        int denominador = 1;
        int numerador = (int) (decimal * denominador);

        while (Math.abs((double) numerador / denominador - decimal) > 1E-3) {
            denominador++;
            numerador = (int) (decimal * denominador);
        }

        int gcd = gcd(numerador, denominador);
        numerador /= gcd;
        denominador /= gcd;

        if (denominador == 1) {
            return String.valueOf(numerador);
        } else {
            return numerador + "/" + denominador;
        }
    }

    public static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
