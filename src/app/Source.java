package app;

import app.net.NeuralNet;

import java.util.Random;

public class Source {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        NeuralNet.numberIterations = 20_000; //количество итераций обучения

        NeuralNet net1 = new NeuralNet();
        net1.setCount(10, 1); //инициализация списков нейронов 1-го, 2-го скрытого слоев, а также выходного нейрона и установка степени нейрона и числа входящих ребер для каждого из нейронов
        net1.setRandomWeights(0.01, 0.1); //устанавливаем рандомные веса [0.01, 0.1] для всех ребер между нейронами 1-го и 2-го слоев, а также нейронами 2-го и выходного слоев
        net1.setTeachCoeff(0.0001); //устанавливаем коэффициент обучения

        NeuralNet net2 = new NeuralNet();
        net2.setCount(10, 1);
        net2.setRandomWeights(0.01, 0.1);
        net2.setTeachCoeff(0.0001);

        for (int i = 0; i < NeuralNet.numberIterations; i++) {
            double x1 = getRandomX();
            net1.teach(x1, f1(x1));
            double result1 = net1.calculate();

            double x2 = getRandomX();
            net2.teach(x2, f2(x2));
            double result2 = net2.calculate();

            double commonResult = result1 + result2;

            //System.out.println(i + "\t\tневязка1= " + (f1(x1) - result1) + " \t\tневязка2= " + (f2(x2) - result2));
            System.out.println("итерация=" + i + "\tневязка= " + (f(x1, x2) - commonResult));
        }
    }

    private static double f(double x) {
        //return Math.pow(x, 3) + 2 * x + 1;
        //return Math.pow(x, 9) + 7 * Math.pow(x, 3) - x + 5;
        //return x + 2;
        return 2 * x - 3;
    }

    private static double getRandomX() {

        double x = RANDOM.nextDouble() % 100_000 + 1;
        x /= 10_000;
        return x; // x принадлежит [0, 0,0002]
    }

    private static double f(double x1, double x2) {
        return f1(x1) + f2(x2);
    }

    private static double f1(double x) {
        return x + 2;
    }

    private static double f2(double x) {
        return 2 * x - 3;
    }
}
