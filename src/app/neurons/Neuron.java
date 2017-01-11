package app.neurons;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by pharrell.
 */
public class Neuron {
    // Векторы с коэффициентами "альфа" и "бета"
    private List<Double> alpha;
    private List<Double> beta;

    // Вектор входов нейронов 1-го скрытого слоя для 2-го скрытого и вектор входов нейронов 2-го скрытого слоя для нейрона выходного слоя
    private List<Double> input;

    // Количество входов
    private int inputCount;

    // Степень
    private int degree;

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

    // Установка количества нейронов на входе для каждого нейрона
    public void setInputCount(int inputCount) {
        this.inputCount = inputCount;
        alpha.clear();
        beta.clear();
        input.clear();
    }

    public int getInputCount() {
        return inputCount;
    }

    public List<Double> getAlpha() {
        return alpha;
    }

    public List<Double> getBeta() {
        return beta;
    }

    // Установка списка нейронов на входе в каждый нейрон 2-го скрытого и выходного слоев
    public void setInput(List<Double> input) {
        this.input = input;
    }

    public List<Double> getInput() {
        return input;
    }

    public void setRandomWeights(double a, double b) {
        double weight;
        for (int i = 0; i < inputCount; i++) {
            Random random = new Random();
            weight = random.nextDouble() % 1000 * (b - a);
            weight = weight / 1000 + a;
            alpha.add(i, Math.abs(weight));
            weight = random.nextDouble() % 1000 * (b - a);
            weight = weight / 1000 + a;
            beta.add(i, Math.abs(weight));
        }
    }

    // Установка степени
    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getDegree() {
        return degree;
    }

    public class FirstHiddenNeuron {

        // Вход в нейрон
        private double input;

        // Степень
        private int degree;

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
