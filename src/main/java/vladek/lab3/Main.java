package vladek.lab3;

public class Main {
    public static void main(String[] args) {
        double x1 = 0;
        double x2 = 0;
        double epsilon = 1e-6;
        int maxIterations = 3000;

        coordinateDescent(x1, x2, epsilon, maxIterations);
    }

    /**
     * Рассчитывает значение функции Розенброка
     *
     * @param x1 координата
     * @param x2 координата
     * @return значение функции
     */
    public static double F(double x1, double x2) {
        return 100 * Math.pow(x1 * x1 - x2, 2) + Math.pow(1 - x1, 2);
    }

    /**
     * Первая частная производная функции F(x1, x2) по x1
     *
     * @param x1 координата
     * @param x2 координата
     * @return значение производной
     */
    public static double dFx1(double x1, double x2) {
        return 400 * Math.pow(x1, 3) - 400 * x1 * x2 + 2 * x1 - 2;
    }

    /**
     * Вторая частная производная функции F(x1, x2) по x1
     *
     * @param x1 координата
     * @param x2 координата
     * @return значение производной
     */
    public static double d2Fx1(double x1, double x2) {
        return 1200 * x1 * x1 - 400 * x2 + 2;
    }

    /**
     * Метод Ньютона для минимизации по х1
     *
     * @param x1      координата
     * @param x2      координата
     * @param epsilon точность
     * @return значение корня х1
     */
    public static double minimizeX1(double x1, double x2, double epsilon) {
        double x1New = x1;
        double delta;

        do {
            double f = dFx1(x1New, x2);
            double df = d2Fx1(x1New, x2);
            delta = f / df;
            x1New -= delta;
        } while (Math.abs(delta) > epsilon);

        return x1New;
    }

    /**
     * Минимизация по х2. Было получено аналитически
     *
     * @param x1 координата
     * @return значение корня х2
     */
    public static double minimizeX2(double x1) {
        return x1 * x1;
    }

    /**
     * Метод покоординатного спуска
     * @param x1 координата
     * @param x2 координата
     * @param epsilon точность
     * @param maxIterations максимальное количество итераций
     */
    public static void coordinateDescent(double x1, double x2, double epsilon, int maxIterations) {
        System.out.printf("Начальная точка: (%.3f, %.3f)%n", x1, x2);

        for (int i = 0; i < maxIterations; i++) {
            double x1New = minimizeX1(x1, x2, epsilon);
            double x2New = minimizeX2(x1New);

            // Проверка сходимости
            if (Math.abs(x1New - x1) < epsilon && Math.abs(x2New - x2) < epsilon) {
                System.out.printf("Сходимость достигнута на шаге %d%n", i + 1);
                break;
            }

            x1 = x1New;
            x2 = x2New;

            System.out.printf("Шаг %d: (%.3f, %.3f)%n", i + 1, x1, x2);
        }

        System.out.printf("Финальная точка: (%.3f, %.3f)%n", x1, x2);
        System.out.printf("Значение функции F(x1, x2) = %.3f%n", F(x1, x2));
    }
}
