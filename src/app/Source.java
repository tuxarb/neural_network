package app;

import app.net.NeuralNet;
import java.util.Random;

public class Source {

    public static void main(String[] args) {
        NeuralNet net = new NeuralNet();
        net.setCount(10, 1);
        net.setRandomWeights(0.01, 0.1);
        net.setTeachCoeff(0.0001);

        double x;
        Random random = new Random();
        for (int i = 1; i < 100000; i++) {
            x = random.nextDouble() % 100000 + 1;
            x /= 10000;

            // TODO: определиться со структурой точки (x1 x2)
            net.teach(x, f(x));
            net.setInput(x);
            System.out.println(i + " " + x + " " + net.calculate() + " " + (f(x) - net.calculate()));
        }
    }

    public static double f(double x1, double x2) {
        return 0; // TODO: определить функцию 2-х переменных
    }

    public static double f(double x) {
        //return x*x*x + 2*x + 1;
        return x * x * x * x * x * x * x * x * x + 7 * x * x * x - x + 5;
    }
}
