import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class KNN {
    public static ArrayList<KnowledgeNode> nodes = new ArrayList<>();

    KNN(String knnFilename) throws Exception { // Parse KNN text file, KN: TAG THRESHOLD => TAG_LIST => FILENAME :END
        this.loadKNN(knnFilename);
    }

    public static ArrayList<String> think(ArrayList<Clause> inputs, int depth){ // depth <= 0 then goes to quiescence
        ArrayList<String> fired = new ArrayList<>();

        ArrayList<Clause> facts = new ArrayList<>();
        facts.addAll(inputs);

        for(int i = 0; i < depth; i++){
            if (facts.size() == 0) break;
//            for(Clause fact: facts){
            ArrayList<Clause> toRemove = new ArrayList<>();
            int initialSize = facts.size();
            for(int j = 0; j < initialSize; j++){
                Clause fact = facts.get(j);
                KnowledgeNode node = match(fact);

                if(node != null){
                    if(node.fired()) continue;

                    node.incrementActivation();

                    if(node.fired()){
                        fired.add(node.getRulesFilename());
//                        facts.remove(node.getTag());
                        toRemove.add(node.getTag());
                        facts.addAll(node.getTagList());
    //                        facts.remove()
                    }
                }

            }

            facts.removeAll(toRemove);

        }

        return fired;
    }

    public void loadKNN(String filename) throws Exception {
        KNNParsing parser = new KNNParsing(filename);

        KnowledgeNode KNode = parser.getKN();
        while (KNode != null) {
            this.nodes.add(KNode);
            KNode = parser.getKN();
        }
    }

    public static void main(String[] args) throws Exception {
        KNN knn = new KNN("./src/RuleBases/knn.txt");

//        for(KnowledgeNode kn : knn.nodes){
//            System.out.println(kn);
//        }

        ArrayList<Clause> inputs = new ArrayList<>();
        inputs.add(new Clause("$A"));
        ArrayList<String> fired = think(inputs, 3);

        Clause[] inputsArr = inputs.toArray(new Clause[inputs.size()]);

        System.out.println(fired);
        Productions p = new Productions(fired,inputsArr);
        p.printRuleBase();
    }

    public static KnowledgeNode match(Clause fact){ // Matches facts with a tag
        for(KnowledgeNode node: nodes){
            if(node.getTag().equals(fact)){
                return node;
            }
        }

        return null;
    }

}
