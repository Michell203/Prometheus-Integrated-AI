package Prometheus;

import expertsystem.Clause;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NN {
    NeuralNetwork neuralNetworkIR;
    NeuralNetwork neuralNetworkB;
    double[] sensors;

    NN(String filename1, String filename2, World w){
        neuralNetworkIR = NeuralNetwork.createFromFile(filename1);
        neuralNetworkIR.setInput(w.IRFront(), w.IRLeft(), w.IRRight());

        neuralNetworkB = NeuralNetwork.createFromFile(filename2);
        neuralNetworkB.setInput(w.isDirB(), w.atB());

        neuralNetworkIR.calculate();
        neuralNetworkB.calculate();

        double[] IR = neuralNetworkIR.getOutput();
        double[] B = neuralNetworkB.getOutput();

//        ArrayList<Double> values = new ArrayList<>();
//        ArrayList<Double> IR = new ArrayList(Arrays.asList(neuralNetworkIR.getOutput()));
//        ArrayList<Double> B = new ArrayList(Arrays.asList(neuralNetworkB.getOutput()));
//
//        values.addAll(IR);
//        values.addAll(B);

        sensors = new double[IR.length + B.length];

        // Convert each element from Object to double
        for (int i = 0; i < IR.length; i++) {
            sensors[i] = IR[i];
        }
        for (int i = 3; i < B.length+3; i++) {
            sensors[i] = B[i-3];
        }

//        sensors = values.toArray();
    }


    public static void trainNN() {
        DataSet IRSet = new DataSet(3, 3);
        DataSet BSet = new DataSet(2, 2);

        // Input: IRFront, IRLeft, IRRight
        // Output: $FREE, $ADJACENTL, $ADJACENTR

        // Input: isDirB, atB
        // Output: $ISDIRB, $B

        IRSet.add(new DataSetRow(new double[]{1.0, 1.0, 1.0},
                new double[]{1.0, 0.0, 0.0}));
        IRSet.add(new DataSetRow(new double[]{1.0, 0.0, 1.0},
                new double[]{1.0, 1.0, 0.0}));
        IRSet.add(new DataSetRow(new double[]{1.0, 1.0, 0.0},
                new double[]{1.0, 0.0, 1.0}));
        IRSet.add(new DataSetRow(new double[]{1.0, 0.0, 0.0},
                new double[]{1.0, 1.0, 1.0}));
        IRSet.add(new DataSetRow(new double[]{0.0, 1.0, 2.0},
                new double[]{0.0, 0.0, 0.0}));
        IRSet.add(new DataSetRow(new double[]{1.0, 3.0, 1.0},
                new double[]{1.0, 0.0, 0.0}));
        IRSet.add(new DataSetRow(new double[]{1.0, 1.0, 5.0},
                new double[]{1.0, 0.0, 0.0}));
        IRSet.add(new DataSetRow(new double[]{5.0, 1.0, 0.0},
                new double[]{1.0, 0.0, 1.0}));
        IRSet.add(new DataSetRow(new double[]{4.0, 0.0, 0.0},
                new double[]{1.0, 1.0, 1.0}));
        IRSet.add(new DataSetRow(new double[]{8.0, 0.0, 0.0},
                new double[]{1.0, 1.0, 1.0}));
        IRSet.add(new DataSetRow(new double[]{4.0, 2.0, 6.0},
                new double[]{1.0, 0.0, 0.0}));
        IRSet.add(new DataSetRow(new double[]{7.0, 4.0, 9.0},
                new double[]{1.0, 0.0, 0.0}));
        IRSet.add(new DataSetRow(new double[]{6.0, 0.0, 3.0},
                new double[]{1.0, 1.0, 0.0}));
        IRSet.add(new DataSetRow(new double[]{2.0, 0.0, 7.0},
                new double[]{1.0, 1.0, 0.0}));
        IRSet.add(new DataSetRow(new double[]{5.0, 4.0, 0.0},
                new double[]{1.0, 0.0, 1.0}));
        IRSet.add(new DataSetRow(new double[]{8.0, 3.0, 0.0},
                new double[]{1.0, 0.0, 1.0}));
        IRSet.add(new DataSetRow(new double[]{0.0, 5.0, 0.0},
                new double[]{0.0, 0.0, 1.0}));
        IRSet.add(new DataSetRow(new double[]{0.0, 2.0, 6.0},
                new double[]{0.0, 0.0, 0.0}));
        IRSet.add(new DataSetRow(new double[]{0.0, 8.0, 2.0},
                new double[]{0.0, 0.0, 0.0}));
        IRSet.add(new DataSetRow(new double[]{2.0, 5.0, 1.0},
                new double[]{1.0, 0.0, 0.0}));
        IRSet.add(new DataSetRow(new double[]{5.0, 3.0, 7.0},
                new double[]{1.0, 0.0, 0.0}));
        IRSet.add(new DataSetRow(new double[]{0.0, 0.0, 0.0},
                new double[]{0.0, 1.0, 1.0}));

        BSet.add(new DataSetRow(new double[]{1.0, 1.0},
                new double[]{1.0, 1.0}));
        BSet.add(new DataSetRow(new double[]{0.0, 1.0},
                new double[]{0.0, 1.0}));
        BSet.add(new DataSetRow(new double[]{1.0, 0.0},
                new double[]{1.0, 0.0}));
        BSet.add(new DataSetRow(new double[]{0.0, 0.0},
                new double[]{0.0, 0.0}));

        NeuralNetwork neuralNetworkIR = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, new int[]{3, 4, 3});
        neuralNetworkIR.randomizeWeights();

        NeuralNetwork neuralNetworkB = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, new int[]{2, 3, 2});
        neuralNetworkB.randomizeWeights();

        neuralNetworkB.learn(BSet);
        neuralNetworkIR.learn(IRSet);

//        for(int i = 0; i < 100; ++i) {
//            neuralNetworkIR.learn(IRSet);
//        }

        neuralNetworkIR.save("RobotIR.nnet");
        neuralNetworkB.save("RobotB.nnet");
    }

    public Clause match(int indx, String type, Double percentage) throws Exception {
        // Output: $FREE, $BGorWALL, $ADJACENTL, $ADJACENTR, $ISDIRB, $B
        if(type.equals("IR")){
            switch (indx) {
                case 0:
                    if(percentage >= 0.7) {
                        return new Clause("$FREE");
                    } else {
                        return new Clause("BGORWALL");
                    }


                case 1:
                    if(percentage >= 0.7) return new Clause("ADJACENTL");

                case 2:
                    if(percentage >= 0.7) return new Clause("ADJACENTR");
            }

        } else if(type.equals("B")){
            switch (indx) {
                case 3:
                    if(percentage >= 0.7) return new Clause("ISDIRB");

                case 4:
                    if(percentage >= 0.7) return new Clause("$B");

            }
        }

        return new Clause("");

    }

    public ArrayList<Clause> classifications() throws Exception { // If NN is above 70%, fire
        ArrayList<Clause> facts = new ArrayList<>();

        for(int n = 0; n < sensors.length; n++){
            if(n < 3){
                facts.add(match(n, "IR", sensors[n]));

            } else {
                facts.add(match(n, "B", sensors[n]));
            }
        }

        Clause[] factsArr = facts.toArray(new Clause[facts.size()]);
        return facts;
    }


}
