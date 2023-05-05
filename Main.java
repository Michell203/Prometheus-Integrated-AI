import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;

public class Main {

    static void train_OR(){
        //Perceptron network
        NeuralNetwork neuralNetwork = new Perceptron(2, 1);

        //Training set
        DataSet trainingSet = new DataSet(2,1);

        //Training data to training set (Logical XOR)
        double[] input1 = new double[]{0,0};
        double[] output1 = new double[]{0};
        trainingSet.add(new DataSetRow(input1, output1));

        double[] input2 = new double[]{0,1};
        double[] output2 = new double[]{1};
        trainingSet.add(new DataSetRow(input2, output2));

        double[] input3 = new double[]{1,0};
        double[] output3 = new double[]{1};
        trainingSet.add(new DataSetRow(input3, output3));

        double[] input4 = new double[]{1,1};
        double[] output4 = new double[]{1};
        trainingSet.add(new DataSetRow(input4, output4));

        //Learn training set
        neuralNetwork.learn(trainingSet);

        //Save trained network to file
        neuralNetwork.save("or_perceptron.nnet");

    }

    static void train_NAND(){
        //Perceptron network
        NeuralNetwork neuralNetwork = new Perceptron(2, 1);

        //Training set
        DataSet trainingSet = new DataSet(2,1);

        //Training data to training set (Logical XOR)
        double[] input1 = new double[]{0,0};
        double[] output1 = new double[]{1};
        trainingSet.add(new DataSetRow(input1, output1));

        double[] input2 = new double[]{0,1};
        double[] output2 = new double[]{1};
        trainingSet.add(new DataSetRow(input2, output2));

        double[] input3 = new double[]{1,0};
        double[] output3 = new double[]{1};
        trainingSet.add(new DataSetRow(input3, output3));

        double[] input4 = new double[]{1,1};
        double[] output4 = new double[]{0};
        trainingSet.add(new DataSetRow(input4, output4));

        //Learn training set
        neuralNetwork.learn(trainingSet);

        //Save trained network to file
        neuralNetwork.save("nand_perceptron.nnet");

    }

    static void train_AND(){
        //Perceptron network
        NeuralNetwork neuralNetwork = new Perceptron(2, 1);

        //Training set
        DataSet trainingSet = new DataSet(2,1);

        //Training data to training set (Logical XOR)
        double[] input1 = new double[]{0,0};
        double[] output1 = new double[]{0};
        trainingSet.add(new DataSetRow(input1, output1));

        double[] input2 = new double[]{0,1};
        double[] output2 = new double[]{0};
        trainingSet.add(new DataSetRow(input2, output2));

        double[] input3 = new double[]{1,0};
        double[] output3 = new double[]{0};
        trainingSet.add(new DataSetRow(input3, output3));

        double[] input4 = new double[]{1,1};
        double[] output4 = new double[]{1};
        trainingSet.add(new DataSetRow(input4, output4));

        //Learn training set
        neuralNetwork.learn(trainingSet);

        //Save trained network to file
        neuralNetwork.save("and_perceptron.nnet");

    }

    static double xor(int inp1, int inp2){
        //Load saved network
        train_OR();
        train_NAND();
        train_AND();

        NeuralNetwork orGate = NeuralNetwork.createFromFile("or_perceptron.nnet");
        NeuralNetwork nandGate = NeuralNetwork.createFromFile("nand_perceptron.nnet");
        NeuralNetwork andGate = NeuralNetwork.createFromFile("and_perceptron.nnet");

        //Give inputs
        orGate.setInput(inp1,inp2);
        nandGate.setInput(inp1,inp2);

        //Calculate and get output
        orGate.calculate();
        double[] orOut = orGate.getOutput();

        nandGate.calculate();
        double[] nandOut = nandGate.getOutput();

        //Pass through and
        andGate.setInput(orOut[0],nandOut[0]);
        andGate.calculate();
        double[] xorVal = andGate.getOutput();

        return xorVal[0];
    }

    public static void main(String[] args) {
        System.out.print("Hello World!");

        //Get output
        double output = xor(1,0);
        System.out.print(output);

    }
}