import java.util.List;

/**
 * Created by pharrell.
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

    // Активация нейрона
    public double activate() {
        return -1;
    }

    // Установка количества входов
    public void setInputCount(int inputCount) {

    }

    // Установка входов в нейрон
    public void setInput(List<Double> input) {

    }

    // Установка произвольных весов
    public void setRandomWeights(double a, double b) {

    }

    // Установка степени
    public void setDegree(int degree) {

    }

    public class FirstHiddenNeuron {

        // Вход в нейрон
        public double input;

        // Степень
        public int degree;

        // Активация нейрона
        public double activate() {
            return -1;
        }

        // Установка входа
        public void setInput(double input) {

        }

        // Установка степени
        public void setDegree(int degree) {

        }

    }

}
