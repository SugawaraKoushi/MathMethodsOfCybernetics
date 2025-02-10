package vladek.lab1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        // Ввод количества коэффициентов функции
        Scanner sc = new Scanner(System.in);

        System.out.println("Введите количество переменных функции:");
        int n = sc.nextInt();   // общее количество переменных
        List<Integer> basis = new ArrayList<>();
        int[] F = new int[n];

        // Ввод значений переменных хj и определение базисных переменных
        for (int i = 0; i < n; i++) {
            System.out.printf("Введите переменную значение переменной x%d = ", i + 1);
            F[i] = sc.nextInt();

            if (F[i] == 0) {
                basis.add(i + 1);
            }
        }

        int[][] table = getSimplexTable(sc, F, basis);

        while (!isOptimal(table)) {
            int col = pivotColumnIndex(table);
            int row = pivotRowIndex(table, col);
        }

        sc.close();
    }

    /**
     * Заполнят симплекс-таблицу начальными данными
     * @param sc для считывания значений
     * @param F функция
     * @param basis позиции базисных переменных
     * @return симплекс-таблица
     */
    public static int[][] getSimplexTable(Scanner sc, int[] F, List<Integer> basis) {
        // Ввод начальной симплекс-таблицы
        // Количество строк: количество базисных переменных + 1
        // Количество столбцов: количество переменных функции + 1
        int[][] result = new int[basis.size() + 1][F.length + 1];

        for (int i = 0; i < result.length - 1; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = sc.nextInt();
            }
        }

        // Копируем в последнюю строку функцию
        System.arraycopy(F, 0, result[result.length - 1], 0, 6);

        return result;
    }

    /**
     * Проверка решения на оптимальность
     * @param table симплекс-таблица
     * @return {@code true}, если в решении нет отрицательных значений. Иначе {@code false}
     */
    public static boolean isOptimal(int[][] table) {
        for (int i = 0; i < table[0].length; i++) {
            if (table[0][i] < 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Определение ведущего столбца симплекс-таблицы
     * @param table симплекс-таблица
     * @return индекс столбца
     */
    public static int pivotColumnIndex(int[][] table) {
        int min = table[table.length - 1][0];
        int minIndex = 0;

        for (int i = 1; i < table[table.length - 1].length; i++) {
            if (min < table[table.length - 1][i]) {
                min = table[table.length - 1][i];
                minIndex = i;
            }
        }

        return minIndex;
    }

    /**
     * Определение ведущей строки симплекс-таблицы
     * @param table симплекс-таблица
     * @param col индекс ведущего столбца
     * @return индекс ведущей строки
     */
    public static int pivotRowIndex(int[][] table, int col) {
        int min = table[0][table[0].length - 1] / table[0][col];
        int minIndex = 0;

        for (int i = 1; i < table[table.length - 1].length; i++) {
            int temp = table[i][table[0].length - 1] / table[i][col];

            if (min < temp) {
                min = temp;
                minIndex = i;
            }
        }

        return minIndex;
    }
}
