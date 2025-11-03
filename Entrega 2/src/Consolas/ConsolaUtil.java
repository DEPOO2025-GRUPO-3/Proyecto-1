package Consolas;

import java.util.Scanner;

public final class ConsolaUtil {
    private ConsolaUtil() {}
    public static final Scanner IN = new Scanner(System.in);

    public static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = IN.nextLine();
            if (s != null && !s.isBlank()) return s.trim();
            System.out.println("Entrada vacía. Intente de nuevo.");
        }
    }

    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = IN.nextLine();
            try {
                int v = Integer.parseInt(line.trim());
                if (v < min || v > max) {
                    System.out.println("Valor fuera de rango [" + min + "," + max + "].");
                } else {
                    return v;
                }
            } catch (Exception e) {
                System.out.println("Debe ingresar un número entero.");
            }
        }
    }

    public static double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = IN.nextLine();
            try {
                double v = Double.parseDouble(line.trim());
                if (v < min || v > max) {
                    System.out.println("Valor fuera de rango [" + min + "," + max + "].");
                } else {
                    return v;
                }
            } catch (Exception e) {
                System.out.println("Debe ingresar un número (use punto decimal).");
            }
        }
    }

    public static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (s/n): ");
            String s = IN.nextLine().trim().toLowerCase();
            if (s.equals("s") || s.equals("si")) return true;
            if (s.equals("n") || s.equals("no")) return false;
            System.out.println("Responda 's' o 'n'.");
        }
    }

    public static void pause() {
        System.out.print("Presione ENTER para continuar...");
        IN.nextLine();
    }
}
