package vladek.lab2;

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
        double[] F = new double[n];

        // Ввод значений переменных хj и определение базисных переменных
        for (int i = 0; i < n; i++) {
            System.out.printf("Введите коэффициент переменной x%d = ", i + 1);
            F[i] = -1 * sc.nextInt();

            if (F[i] == 0) {
                basis.add(i + 1);
            }
        }

        double[][] table = getSimplexTable(sc, F, basis);
        System.out.println("Изначальная симплекс-таблица");
        printTable(table);

        while (!isOptimal(table)) {
            int col = pivotColumnIndex(table);
            int row = pivotRowIndex(table, col);

            if (row < 0) {
                System.out.println("Функция не ограничена. Оптимальное решение отсутствует");
                break;
            }

            recalculateTable(table, row, col);
            System.out.printf("%nПересчитанная симплекс-таблица%n");
            printTable(table);
        }

        if (isOptimal(table)) {
            System.out.println("Решение:");

            for (int i = 0; i < basis.size(); i++) {
                System.out.printf("x%d = %.2f%n", basis.get(i), table[i][table[i].length - 1]);
            }
        }

        sc.close();
    }

    /**
     * Заполнят симплекс-таблицу начальными данными
     *
     * @param sc    для считывания значений
     * @param F     функция
     * @param basis позиции базисных переменных
     * @return симплекс-таблица
     */
    public static double[][] getSimplexTable(Scanner sc, double[] F, List<Integer> basis) {
        // Количество строк: количество базисных переменных + 1
        // Количество столбцов: количество переменных функции + 1
        double[][] result = new double[basis.size() + 1][F.length + 1];

        for (int i = 0; i < result.length - 1; i++) {
            System.out.printf("Введите %d элементов %d строки симлекс-таблицы%n", F.length + 1, i + 1);
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = sc.nextInt();
            }
        }

        // Копируем в последнюю строку функцию
        System.arraycopy(F, 0, result[result.length - 1], 0, F.length);

        return result;
    }

    /**
     * Проверка решения на оптимальность
     *
     * @param table симплекс-таблица
     * @return {@code true}, если в решении нет отрицательных значений. Иначе {@code false}
     */
    public static boolean isOptimal(double[][] table) {
        for (int i = 0; i < table[table.length - 1].length; i++) {
            if (table[table.length - 1][i] < 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Определение ведущего столбца симплекс-таблицы
     *
     * @param table симплекс-таблица
     * @return индекс столбца
     */
    public static int pivotColumnIndex(double[][] table) {
        double min = table[table.length - 1][0];
        int minIndex = 0;

        for (int i = 1; i < table[table.length - 1].length; i++) {
            if (min > table[table.length - 1][i]) {
                min = table[table.length - 1][i];
                minIndex = i;
            }
        }

        return minIndex;
    }

    /**
     * Определение ведущей строки симплекс-таблицы
     *
     * @param table симплекс-таблица
     * @param col   индекс ведущего столбца
     * @return {@code отрицательное 0}, если все элементы ведущего столбца отрицательны,
     * иначе {@code индекс ведущей строки}
     */
    public static int pivotRowIndex(double[][] table, int col) {
        Double min = null;
        int minIndex = -1;

        for (int i = 0; i < table.length; i++) {
            double temp = table[i][table[0].length - 1] / table[i][col];

            if (temp <= 0.0 || temp <= -0.0) continue;

            if (min == null || min > temp) {
                min = temp;
                minIndex = i;
            }
        }

        return minIndex;
    }

    /**
     * Пересчитывает симплекс-таблицу
     *
     * @param table симплекс-таблица
     * @param col   ведущий столбец
     * @param row   ведущая строка
     */
    public static void recalculateTable(double[][] table, int row, int col) {
        // Определяем ведущий элемент
        double pivotElement = table[row][col];

        // Делим на него всю ведущую строку
        for (int i = 0; i < table[row].length; i++) {
            table[row][i] /= pivotElement;
        }

        // Обнуляем остальные элементы
        for (int i = 0; i < table.length; i++) {
            if (i == row) continue;

            double pivotColumnElement = table[i][col];
            for (int j = 0; j < table[i].length; j++) {
                table[i][j] = table[i][j] - pivotColumnElement * table[row][j];
            }
        }
    }

    public static void printTable(double[][] table) {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                System.out.printf("%8.2f", table[i][j]);
            }

            System.out.println();
        }
    }
}
