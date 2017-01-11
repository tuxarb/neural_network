package app.net;

import app.neurons.Neuron;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry.
 */

public class NeuralNet {
    private double inputX;
    private int layer1Count, layer2Count;
    private double teachCoeff;
    private int numberIterations;
    private List<Neuron> neuronsLayer2;
    private List<Neuron.FirstHiddenNeuron> neuronsLayer1;
    private Neuron output;

    public NeuralNet() {
        neuronsLayer1 = new ArrayList<>();
        neuronsLayer2 = new ArrayList<>();
        output = new Neuron();
    }

    public void setCount(int layer1Count, int layer2Count) {
        this.layer1Count = layer1Count;
        this.layer2Count = layer2Count;
        initNeuronsLayer1(layer1Count);
        initNeuronsLayer2(layer2Count);

        for (int i = 0; i < layer1Count; i++) {
            neuronsLayer1.get(i).setDegree(layer1Count - i - 1);
        }

        for (int i = 0; i < layer2Count; i++) {
            neuronsLayer2.get(i).setDegree(layer2Count - i - 1);
        }

        for (int i = 0; i < layer2Count; i++) {
            neuronsLayer2.get(i).setInputCount(layer1Count);
        }

        output.setInputCount(layer2Count);
        output.setDegree(1);
    }

    private void initNeuronsLayer1(int size) {
        for (int i = 0; i < size; i++) {
            neuronsLayer1.add(new Neuron().new FirstHiddenNeuron());
        }
    }

    private void initNeuronsLayer2(int size) {
        for (int i = 0; i < size; i++) {
            neuronsLayer2.add(new Neuron());
        }
    }

    void setInput(double input) {
        this.inputX = input;
        for (int i = 0; i < layer1Count; i++) {
            neuronsLayer1.get(i).setInput(inputX);
        }
    }

    public double calculate() {
        List<Double> layer1Output = new ArrayList<>();

        for (int i = 0; i < layer1Count; i++) {
            layer1Output.add(i, neuronsLayer1.get(i).activate());
        }

        for (int i = 0; i < layer2Count; i++) {
            neuronsLayer2.get(i).setInput(layer1Output);
        }
        List<Double> layer2Output = new ArrayList<>();

        for (int i = 0; i < layer2Count; i++) {
            layer2Output.add(neuronsLayer2.get(i).activate());
        }
        for (int i = 0; i < layer2Count; i++) {
            layer2Output.add(i, Math.pow(layer2Output.get(i), neuronsLayer2.get(i).getDegree()));
        }

        output.setInput(layer2Output);
        return output.activate();
    }

    public void setRandomWeights(double a, double b) {
        for (int i = 0; i < layer2Count; i++)
            neuronsLayer2.get(i).setRandomWeights(a, b);

        output.setRandomWeights(a, b);
    }

    public void teach(double inputX, double realResult) {
        // устанавливает число x для каждого нейрона 1-го скрытого слоя на входе
        this.setInput(inputX);

        List<Double> layer1Output = new ArrayList<>();
        for (int i = 0; i < layer1Count; i++) {
            layer1Output.add(neuronsLayer1.get(i).activate());
        }
        for (int i = 0; i < layer2Count; i++) {
            neuronsLayer2.get(i).setInput(layer1Output);
        }

        List<Double> layer2Output = new ArrayList<>();
        for (int i = 0; i < layer2Count; i++) {
            layer2Output.add(neuronsLayer2.get(i).activate());
        }
        for (int i = 0; i < layer2Count; i++) {
            layer2Output.add(i, Math.pow(layer2Output.get(i), neuronsLayer2.get(i).getDegree()));
        }

        output.setInput(layer2Output);
        double result = output.activate();

        double deltaY = realResult - result;

        /**
         * Пересчет весов для ребер между 2-ым скрытым и выходным слоями
         */
        List<Double> alpha = output.getAlpha();
        List<Double> beta = output.getBeta();

        for (int i = 0; i < output.getInputCount(); i++) {
            alpha.add(i, alpha.get(i) + teachCoeff * deltaY * output.getInput().get(i) *
                    (1 + alpha.get(i) - alpha.get(i) / (1 + Math.exp(-alpha.get(i)))) /
                    (1 + Math.exp(-alpha.get(i))));

            beta.add(i, beta.get(i) + teachCoeff * deltaY * output.getInput().get(i) *
                    (1 - beta.get(i) + beta.get(i) / (1 + Math.exp(beta.get(i)))) /
                    (1 + Math.exp(beta.get(i))));
        }
        List<Double> layer2OutputWithoutPow = new ArrayList<>();

        for (int i = 0; i < layer2Count; i++) {
            if (neuronsLayer2.get(i).getDegree() != 0) {
                layer2OutputWithoutPow.add(Math.pow(layer2Output.get(i), neuronsLayer2.get(i).getDegree() - 1));
            } else {
                layer2OutputWithoutPow.add(Math.pow(layer2Output.get(i), neuronsLayer2.get(i).getDegree()));
            }
        }
        List<Double> deltaZ = new ArrayList<>();
        for (int i = 0; i < layer2Count; i++) {
            deltaZ.add(teachCoeff * deltaY * neuronsLayer2.get(i).getDegree() * layer2OutputWithoutPow.get(i) *
                    (alpha.get(i) / (1 + Math.exp(-alpha.get(i))) +
                            beta.get(i) / (1 + Math.exp(-beta.get(i)))));
        }

        /**
         * Пересчет весов для ребер между 1-ым и 2-ым скрытыми слоями
         */
        for (int neuron = 0; neuron < layer2Count; neuron++) {
            Neuron neuronLayer2 = neuronsLayer2.get(neuron);
            alpha = neuronLayer2.getAlpha();
            beta = neuronLayer2.getBeta();
            for (int j = 0; j < neuronLayer2.getInput().size(); j++) {
                alpha.add(j, alpha.get(j) + deltaZ.get(neuron) * neuronLayer2.getInput().get(j) *
                        (1 + alpha.get(j) - alpha.get(j) / (1 + Math.exp(-alpha.get(j)))) /
                        (1 + Math.exp(-alpha.get(j))));

                beta.add(j, beta.get(j) + deltaZ.get(neuron) * neuronLayer2.getInput().get(j) *
                        (1 - beta.get(j) + beta.get(j) / (1 + Math.exp(beta.get(j))) /
                                (1 + Math.exp(beta.get(j)))));
            }
        }
    }

    public void setTeachCoeff(double newTeachCoeff) {
        this.teachCoeff = newTeachCoeff;
    }

    public void setNumberIterations(int numberIterations) {
        this.numberIterations = numberIterations;
    }

    public int getNumberIterations() {
        return numberIterations;
    }
}
