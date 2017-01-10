package app.neurons;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pharrell :).
 */
public class Neuron {

    // Векторы с коэффициентами "альфа" и "бета"
    public List<Double> alpha;
    public List<Double> beta;

    // Вектор входов
    public List<Double> input;

    // Количество входов
    public int inputCount;

    // Степень
    public int degree;

    public Neuron() {
        this.alpha = new ArrayList<>();
        this.beta = new ArrayList<>();

        this.input = new ArrayList<>();
    }

    // Активация нейрона
    public double activate() {
        double sum = 0;

        for (int i = 0; i < inputCount; i++)
            sum += input.get(i) * (
                    alpha.get(i) / (1 + Math.exp(-alpha.get(i))) +
                            beta.get(i) / (1 + Math.exp(beta.get(i)))
            );

        return sum;
    }

    // Установка количества входов
    public void setInputCount(int inputCount) {
        this.inputCount = inputCount;
        alpha.clear();
        beta.clear();
        input.clear();
    }

    // Установка входов в нейрон
    public void setInput(List<Double> input) {
        if (input.size() == this.input.size())
            this.input = input;
    }

    // Установка произвольных весов
    public void setRandomWeights(double a, double b) {
        double weight;
        for (int i = 0; i < inputCount; i++) {
            weight = Math.random() % 1000 * (b - a);
            weight = weight / 1000 + a;
            alpha.add(Math.abs(weight));
            weight = Math.random() % 1000 * (b - a);
            weight = weight / 1000 + a;
            beta.add(Math.abs(weight));
        }
    }

    // Установка степени
    public void setDegree(int degree) {
        this.degree = degree;
    }

    public class FirstHiddenNeuron {

        // Вход в нейрон
        public double input;

        // Степень
        public int degree;

        // Конструктор
        public FirstHiddenNeuron() {
            this.input = 1;
            this.degree = 0;
        }

        // Активация нейрона
        public double activate() {
            return Math.pow(input, degree);
        }

        // Установка входа
        public void setInput(double input) {
            this.input = input;
        }

        // Установка степени
        public void setDegree(int degree) {
            this.degree = degree;
        }

    }

}
