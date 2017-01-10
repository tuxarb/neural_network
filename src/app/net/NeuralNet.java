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
    private int maxCycleCount;
    private List<Neuron> neuronsLayer2;
    private List<Neuron.FirstHiddenNeuron> neuronsLayer1;
    private Neuron output;

    public NeuralNet() {
    }

    public void setCount(int layer1Count, int layer2Count) {
        this.layer1Count = layer1Count;
        this.layer2Count = layer2Count;
        neuronsLayer1.clear();
        neuronsLayer2.clear();

        for (int i = 0; i < layer1Count; i++) {
            neuronsLayer1.get(i).setDegree(neuronsLayer1.size() - i - 1);
        }

        for (int i = 0; i < layer2Count; i++) {
            neuronsLayer2.get(i).setDegree(neuronsLayer2.size() - i - 1);
        }

        for (int i = 0; i < layer2Count; i++) {
            neuronsLayer2.get(i).setInputCount(layer1Count);
        }

        output.setInputCount(layer2Count);
        output.setDegree(1);
    }

    public void setInput(double input) {
        this.inputX = input;
        for (int i = 0; i < layer1Count; i++) {
            neuronsLayer1.get(i).setInput(inputX);
        }
    }

    public double calculate() {
        List<Double> layer1Output = new ArrayList<>();

        for (int i = 0; i < neuronsLayer1.size(); i++) {
            layer1Output.add(neuronsLayer1.get(i).activate());
        }

        for (int i = 0; i < neuronsLayer2.size(); i++) {
            neuronsLayer2.get(i).setInput(layer1Output);
        }

        List<Double> layer2Output = new ArrayList<>();

        for (int i = 0; i < neuronsLayer2.size(); i++) {
            layer2Output.add(neuronsLayer2.get(i).activate());
        }

        for (int i = 0; i < neuronsLayer2.size(); i++) {
            layer2Output.add(Math.pow(layer2Output.get(i), neuronsLayer2.get(i).degree));
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
            layer2Output.add(i, Math.pow(layer2Output.get(i), neuronsLayer2.get(i).degree));
        }

        output.setInput(layer2Output);
        double result = output.activate();

        double deltaY = realResult - result;

        for (int i = 0; i < output.inputCount; i++) {
            output.alpha.add(i, output.alpha.get(i) + teachCoeff * deltaY * output.input.get(i) *
                    (1 + output.alpha.get(i) - output.alpha.get(i) / (1 + Math.exp(-output.alpha.get(i)))) /
                    (1 + Math.exp(-output.alpha.get(i))));

            output.beta.add(i, output.beta.get(i) + teachCoeff * deltaY * output.input.get(i) *
                    (1 - output.beta.get(i) + output.beta.get(i) / (1 + Math.exp(output.beta.get(i)))) /
                    (1 + Math.exp(output.beta.get(i))));
        }

        List<Double> layer2OutputWithoutPow = new ArrayList<>();

        for (int i = 0; i < layer2Count; i++) {
            if (neuronsLayer2.get(i).degree != 0) {
                layer2OutputWithoutPow.add(i, Math.pow(layer2OutputWithoutPow.get(i), neuronsLayer2.get(i).degree - 1));
            } else {
                layer2OutputWithoutPow.add(i, Math.pow(layer2OutputWithoutPow.get(i), neuronsLayer2.get(i).degree));
            }
        }

        List<Double> deltaZ = new ArrayList<>();
        for (int i = 0; i < layer2Count; i++) {
            deltaZ.add(teachCoeff * deltaY * neuronsLayer2.get(i).degree * layer2OutputWithoutPow.get(i) *
                    (output.alpha.get(i) /
                            (1 + Math.exp(-output.alpha.get(i))) + output.beta.get(i) / (1 + Math.exp(-output.beta.get(i)))));
        }

        for (int neuron = 0; neuron < layer2Count; neuron++) {
            for (int j = 0; j < neuronsLayer2.get(neuron).input.size(); j++) {
                neuronsLayer2.get(neuron).alpha.add(j, neuronsLayer2.get(neuron).alpha.get(j) + deltaZ.get(neuron) *
                        neuronsLayer2.get(neuron).input.get(j) *
                        (1 + neuronsLayer2.get(neuron).alpha.get(j) - neuronsLayer2.get(neuron).alpha.get(j) /
                                (1 + Math.exp(-neuronsLayer2.get(neuron).alpha.get(j)))) /
                        (1 + Math.exp(-neuronsLayer2.get(neuron).alpha.get(j))));
                neuronsLayer2.get(neuron).beta.add(j, neuronsLayer2.get(neuron).beta.get(j) + deltaZ.get(neuron) *
                        neuronsLayer2.get(neuron).input.get(j) *
                        (1 - neuronsLayer2.get(neuron).beta.get(j) + neuronsLayer2.get(neuron).beta.get(j) /
                                (1 + Math.exp(neuronsLayer2.get(neuron).beta.get(j))) /
                                (1 + Math.exp(neuronsLayer2.get(neuron).beta.get(j)))));
            }
        }
    }

    public void setTeachCoeff(double newTeachCoeff) {
        this.teachCoeff = newTeachCoeff;
    }

    public double getInputX() {
        return inputX;
    }

    public void setInputX(double inputX) {
        this.inputX = inputX;
    }

    public Neuron getOutput() {
        return output;
    }

    public void setOutput(Neuron output) {
        this.output = output;
    }

    public int getLayer1Count() {
        return layer1Count;
    }

    public void setLayer1Count(int layer1Count) {
        this.layer1Count = layer1Count;
    }

    public int getLayer2Count() {
        return layer2Count;
    }

    public void setLayer2Count(int layer2Count) {
        this.layer2Count = layer2Count;
    }

    public double getTeachCoeff() {
        return teachCoeff;
    }

    public int getMaxCycleCount() {
        return maxCycleCount;
    }

    public void setMaxCycleCount(int maxCycleCount) {
        this.maxCycleCount = maxCycleCount;
    }

    public List<Neuron> getNeuronsLayer2() {
        return neuronsLayer2;
    }

    public void setNeuronsLayer2(List<Neuron> neuronsLayer2) {
        this.neuronsLayer2 = neuronsLayer2;
    }

    public List<Neuron.FirstHiddenNeuron> getNeuronsLayer1() {
        return neuronsLayer1;
    }

    public void setNeuronsLayer1(List<Neuron.FirstHiddenNeuron> neuronsLayer1) {
        this.neuronsLayer1 = neuronsLayer1;
    }
}
