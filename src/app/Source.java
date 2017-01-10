package app;

import app.net.NeuralNet;

public class Source {

    public static void main(String[] args) {
        NeuralNet net = new NeuralNet();
        net.setCount(10, 1);
        net.setRandomWeights(0.01, 0.1);
        net.setTeachCoeff(0.0001);
    }

}
