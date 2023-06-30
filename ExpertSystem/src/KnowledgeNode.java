import java.util.ArrayList;

public class KnowledgeNode {
    private Clause tag;
    private ArrayList<Clause> tags = new ArrayList<>();
    private int activation = 0;
    private int threshold;
    private String rulesFilename;

    KnowledgeNode(Clause tag, ArrayList<Clause> tags, int threshold, String rulesFilename) throws Exception {
        this.tag = tag;
        this.tags.addAll(tags);
        this.threshold = threshold;
        this.rulesFilename = rulesFilename;
    }

    public String toString() {
        return "Tag: "+ this.tag + "\nTags List: " + this.tags + "\nThreshold: " + this.threshold +
                "\nRules File Name: " + this.rulesFilename + "\n\n";
    }

    public boolean fired(){
        return this.activation >= this.threshold;
    }

    public void incrementActivation(){
        this.activation++;
    }

    public Clause getTag(){
        return this.tag;
    }

    public String getRulesFilename(){
        return this.rulesFilename;
    }

    public ArrayList<Clause> getTagList(){
        return this.tags;
    }

}
