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
    private List<Neuron> neurons = new ArrayList<>();
    private List<Neuron.FirstHiddenNeuron> neuronsOnTheFirstLayer = new ArrayList<>();


    public NeuralNet() {
    }

    public void setCount(int layer1Count, int layer2Count){

    }

    public void setInput(double input){

    }

    public double calculate()
    {
        return 0;
    }

    public void setRandomWeights(double a, double b){

    }

    public void teach(double input, double trueAnswer){

    }

    public void setTeachCoeff(double newTC){

    }

    public double getInputX() {
        return inputX;
    }

    public void setInputX(double inputX) {
        this.inputX = inputX;
    }
}
