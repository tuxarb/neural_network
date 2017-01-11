package app;

import app.net.NeuralNet;

import java.util.Random;

public class Source {

    public static void main(String[] args) {
        NeuralNet net = new NeuralNet();
        net.setCount(10, 1); //инициализация списков нейронов 1-го, 2-го скрытого слоев, а также выходного нейрона и установка степени нейрона и числа входящих ребер для каждого из нейронов
        net.setRandomWeights(0.01, 0.1); //устанавливаем рандомные веса [0.01, 0.1] для всех ребер между нейронами 1-го и 2-го слоев, а также нейронами 2-го и выходного слоев
        net.setTeachCoeff(0.0001); //устанавливаем коэффициент обучения
        net.setNumberIterations(20_000); //количество итераций обучения

        Random random = new Random();

        for (int i = 1; i < net.getNumberIterations(); i++) {
            double x = random.nextDouble() % 100_000 + 1;
            x /= 10_000; // x принадлежит [0, 0,0002]

            net.teach(x, f(x));
            double result = net.calculate();

            System.out.println(i + " " + x + " " + result + " \t\t" + (f(x) - result));
        }
    }

    private static double f(double x) {
        //return Math.pow(x, 3) + 2 * x + 1;
        return Math.pow(x, 9) + 7 * Math.pow(x, 3) - x + 5;
    }
}
