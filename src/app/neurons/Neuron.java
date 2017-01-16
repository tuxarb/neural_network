package app.neurons;


import java.util.ArrayList;
import java.util.List;

import static app.Source.RANDOM;

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

        for (int i = 0; i < inputCount; i++) {
            sum += input.get(i) * (
                    alpha.get(i) / (1 + Math.exp(-alpha.get(i))) +
                            beta.get(i) / (1 + Math.exp(beta.get(i)))
            );
        }
        return sum;
    }

    // Установка количества нейронов на входе для каждого нейрона
    public void setInputCount(int inputCount) {
        this.inputCount = inputCount;
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

    public void setAlpha(List<Double> alpha) {
        this.alpha = alpha;
    }

    public void setBeta(List<Double> beta) {
        this.beta = beta;
    }

    // Установка списка нейронов на входе в каждый нейрон 2-го скрытого и выходного слоев
    public void setInput(List<Double> input) {
        this.input = input;
    }

    public List<Double> getInput() {
        return input;
    }

    public void setRandomWeights(double a, double b) {
        for (int i = 0; i < inputCount; i++) {
            alpha.add(-Math.abs(getWeight(a, b)));
            beta.add(Math.abs(getWeight(a, b)));
        }
    }

    private double getWeight(double a, double b){
        double weight = RANDOM.nextDouble() % 1000 * (b - a);
        return weight / 1000 + a;
    }

    // Установка степени
    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getDegree() {
        return degree;
    }

    public class FirstHiddenNeuron {

        // Входы в нейрон
        private double input1;
        private double input2;

        // Степень
        private int degree;

        // Конструктор
        public FirstHiddenNeuron() {
            this.input1 = 1;
            this.input2 = 1;
            this.degree = 0;
        }

        // Активация нейрона
        public double activate() {
            return Math.pow(input1 * input2, degree);
        }

        // Установка входа (x1 и x2)
        public void setInput(double input1, double input2) {
            this.input1 = input1;
            this.input2 = input2;
        }

        // Установка степени
        public void setDegree(int degree) {
            this.degree = degree;
        }
    }
}
