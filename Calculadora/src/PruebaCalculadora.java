/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author alian
 */
import java.util.Scanner;

public class PruebaCalculadora {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            Calculadora calc = new Calculadora();
            int opcion;
            
            do {
                System.out.println("\n--- CALCULADORA ---");
                System.out.println("1. Sumar");
                System.out.println("2. Restar");
                System.out.println("3. Multiplicar");
                System.out.println("4. Dividir");
                System.out.println("0. Salir");
                System.out.print("Elige una opcion: ");
                opcion = sc.nextInt();
                
                if (opcion >= 1 && opcion <= 3) {
                    System.out.print("Cuantos numeros quieres usar? (2, 3 o 4): ");
                    int n = sc.nextInt();
                    int resultado = 0;
                    
                    if (n == 2) {
                        System.out.print("Ingresa el primer numero: ");
                        int a = sc.nextInt();
                        System.out.print("Ingresa el segundo numero: ");
                        int b = sc.nextInt();
                        
                        if (opcion == 1) resultado = calc.sumar(a, b);
                        if (opcion == 2) resultado = calc.restar(a, b);
                        if (opcion == 3) resultado = calc.multiplicar(a, b);
                        
                    } else if (n == 3) {
                        System.out.print("Ingresa el primer numero: ");
                        int a = sc.nextInt();
                        System.out.print("Ingresa el segundo numero: ");
                        int b = sc.nextInt();
                        System.out.print("Ingresa el tercer numero: ");
                        int c = sc.nextInt();
                        
                        if (opcion == 1) resultado = calc.sumar(a, b, c);
                        if (opcion == 2) resultado = calc.restar(a, b, c);
                        if (opcion == 3) resultado = calc.multiplicar(a, b, c);
                        
                    } else if (n == 4) {
                        System.out.print("Ingresa el primer numero: ");
                        int a = sc.nextInt();
                        System.out.print("Ingresa el segundo numero: ");
                        int b = sc.nextInt();
                        System.out.print("Ingresa el tercer numero: ");
                        int c = sc.nextInt();
                        System.out.print("Ingresa el cuarto numero: ");
                        int d = sc.nextInt();
                        
                        if (opcion == 1) resultado = calc.sumar(a, b, c, d);
                        if (opcion == 2) resultado = calc.restar(a, b, c, d);
                        if (opcion == 3) resultado = calc.multiplicar(a, b, c, d);
                        
                    } else {
                        System.out.println("Numero de parametros invalido.");
                        continue;
                    }
                    
                    System.out.println("Resultado: " + resultado);
                    
                } else if (opcion == 4) {
                    System.out.print("Ingresa el dividendo: ");
                    int a = sc.nextInt();
                    System.out.print("Ingresa el divisor: ");
                    int b = sc.nextInt();
                    int resultado = calc.dividir(a, b);
                    System.out.println("Resultado: " + resultado);
                }
                
            } while (opcion != 0);
            
            System.out.println("Gracias por usar la calculadora!");
        }
    }
}
