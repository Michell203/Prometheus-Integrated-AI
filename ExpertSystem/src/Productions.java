import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

// Joseph Vybihal (c) 2022
//
// This class stores all the knowledge-base rules and implements both the think() method and the loadRules() method from the
// knowledge-base file.

public class Productions {
    private String filenameRuleBase;
    private ArrayList<String> filenameRuleBases;
    private boolean validRuleBase;
    private ArrayList<Rule> ruleBase;
    private ArrayList<Clause> facts;
    private ArrayList<Clause> newFacts;
    private ArrayList<Clause> markedFacts; // facts with hashtag

    Productions(String filenameRuleBase) {
        this.filenameRuleBase = filenameRuleBase;

        this.ruleBase    = new ArrayList<Rule>();
        this.facts       = new ArrayList<Clause>();
        this.newFacts    = new ArrayList<Clause>();
        this.markedFacts = new ArrayList<Clause>();

        this.validRuleBase = false;

        this.loadRules(filenameRuleBase);
    }

    Productions(String filenameRuleBase, Clause initialFacts[]) {
        this.filenameRuleBase = filenameRuleBase;

        this.ruleBase    = new ArrayList<Rule>();
        this.facts       = new ArrayList<Clause>();
        this.newFacts    = new ArrayList<Clause>();
        this.markedFacts = new ArrayList<Clause>();

        this.validRuleBase = false;

        this.loadRules(filenameRuleBase);

        for(int i=0; i<initialFacts.length; i++) this.facts.add(initialFacts[i]);
    }

    Productions(ArrayList<String> filenameRuleBase, Clause initialFacts[]) {
        this.filenameRuleBases = filenameRuleBase;

        this.ruleBase    = new ArrayList<Rule>();
        this.facts       = new ArrayList<Clause>();
        this.newFacts    = new ArrayList<Clause>();
        this.markedFacts = new ArrayList<Clause>();

        this.validRuleBase = false;

        this.loadMultipleRules(filenameRuleBases);

        for(int i=0; i<initialFacts.length; i++) this.facts.add(initialFacts[i]);
    }

    private void loadRules(String filename) {
        Rule R;

        try {
            RuleParsing rp = new RuleParsing(filenameRuleBase);

            R = rp.getRule();
            while (R != null) {
                if (!R.getGroupName().equals("NOGROUP")) this.ruleBase.add(R);
                R = rp.getRule();
            }

        } catch(FileNotFoundException e) {
            System.out.println("Unable to open rule-base file!");
        }

        this.validRuleBase = true;
    }

    private void loadMultipleRules(ArrayList<String> filenames) {
        for(String name1: filenames) {
            String name = "./src/KNNRuleBases/" + name1;
            Rule R;

            try {
                RuleParsing rp = new RuleParsing(name);

                R = rp.getRule();
                while (R != null) {
                    if (!R.getGroupName().equals("NOGROUP")) this.ruleBase.add(R);
                    R = rp.getRule();
                }

            } catch (FileNotFoundException e) {
                System.out.println("Unable to open rule-base file!");
            }

            this.validRuleBase = true;
        }
    }

    public void addFact(Clause aFact) {
        this.facts.add(aFact);
    }

    public ArrayList<Clause> getFacts() {return this.facts;}

    public ArrayList<Clause> getMarkedFacts() {
        for(int i = 0; i < facts.size(); i++) {
            Clause fact = facts.get(i);
            String pred = fact.getPredicate();

            if(pred.charAt(0) == '#'){
                markedFacts.add(fact);
            }
        }

        return this.markedFacts;
    } // See the TODO below

    public boolean think() throws Exception {
        boolean quiescence = false;
        boolean fired = false;
        String temp;
        boolean tempFound = false;

//        if (!this.validRuleBase || this.facts.isEmpty() || this.ruleBase.isEmpty()) return false;
        if (!this.validRuleBase || this.ruleBase.isEmpty()) return false;

        // cascading processing
        while (!quiescence) {
            this.newFacts = new ArrayList<Clause>();
            fired = false;

            // fire rules
            for(Rule R: ruleBase) {
                if (R.match(facts)) {
                    for(Clause A: R.getProcessedActions()) {
                        if (A.getPredicate().charAt(0) == '$') {
                            tempFound = false;
                            temp      = A.getPredicate().substring(1);
                            for(Clause F: this.facts) {
                                if (F.getPredicate().equals(temp)) {
                                    F.setPredicate("*DEL*"+F.getPredicate());
                                    A.setPredicate(temp);
                                    tempFound = true;
                                    this.facts.add(A);
                                    break;
                                }
                            }
                            if (!tempFound) {A.setPredicate(temp); this.newFacts.add(A);}
                        } else {
                            this.newFacts.add(A);
                        }
                    }
                    //this.newFacts.addAll(R.getProcessedActions());
                    fired = true;
                }
            }

            if (!fired) quiescence = true;

            // append the new facts to facts
            this.facts.addAll(this.newFacts);
        }

        // extract marked rules
        // TODO: Extract the marked facts here.
        // Traverse newFacts ArrayList, if starts with # then it is a marked fact

//        for(int i = 0; i < facts.size(); i++) {
//            Clause fact = facts.get(i);
//            System.out.println("in for loop\n");
//            System.out.println(fact);
//        }

        return true;
    }

    public String toString() {
        return "\nRules:\n"+this.ruleBase.toString()+"\nFacts:\n"+this.facts.toString();
    }

    public ArrayList<Rule> optimizer(){
        ArrayList<Rule> optimizedRules = new ArrayList<>();
        for(int i = 0; i < ruleBase.size(); i++){
            for(int j = i+1; j < ruleBase.size(); j++){
                Rule r1 =  ruleBase.get(i);
                Rule r2 =  ruleBase.get(j);

                optimizedRules.addAll(connect(r1,r2));
            }
        }

        return optimizedRules;
    }

    public ArrayList<Rule> connect(Rule r1, Rule r2){ // If actions1, conds2 match, then make new rule with conds1, actions2
        ArrayList<Rule> newRules = new ArrayList<>();

        ArrayList<Clause> conds1 = r1.getConds();
        conds1.addAll(r1.getFunctions());
        ArrayList<Clause> actions1 = r1.getActions();

        ArrayList<Clause> conds2 = r2.getConds();
        conds2.addAll(r2.getFunctions());
        ArrayList<Clause> actions2 = r2.getActions();

        if(actions1.size() == conds2.size()){
//            boolean equal = new HashSet<>(actions1).equals(new HashSet<>(conds2));
//            boolean equal = true;
//            for(int i = 0; i < actions1.size(); i++){
//                Clause c1 = actions1.get(i);
//                Clause c2 = conds2.get(i);
//
//                if(!Objects.equals(c1.getPredicate(), c2.getPredicate()) || !Objects.equals(c1.getTerms(), c2.getTerms())){
//                    equal = false;
//                    break;
//                }
//
//            }

            boolean equal = actions1.containsAll(conds2);

            if(equal){
                Rule newRule = new Rule(conds1,actions2,"OPTIMIZED");
//                System.out.println(newRule);
                if(!inRuleBase(newRule)){
                    ruleBase.add(newRule);
                    newRules.add(newRule);
                }
            }

        }

        return newRules;
    }

    public void objectify(ArrayList<Clause> inputs, ArrayList<Clause> cmds, int hyper){
        boolean equal = false;
        int ruleNo = 0;

        for(int i = 0; i < ruleBase.size(); i++){
            Rule rule = ruleBase.get(i);
            ArrayList<Clause> actions = rule.getActions();

            if(actions.containsAll(cmds)){
                equal = true;
                ruleNo = i;
                break;
            }
        }

        if(equal){ // Found an action equal to cmds
            Rule prune = ruleBase.get(ruleNo);
            ArrayList<Clause> condsPrune = prune.getConds();
            ArrayList<Clause> overlap = new ArrayList<>();

            int hypCounter = 0;
            for (Clause inp : inputs) {
                for (Clause cond : condsPrune) {
                    if (inp.equals(cond) && hypCounter < hyper) {
                        overlap.add(inp);
                        hypCounter++;

                    }
                }
            }

            if(!overlap.isEmpty()){
                Rule prunedRule = new Rule(overlap,cmds,"PRUNED");
                ruleBase.remove(ruleNo);
                if(!inRuleBase(prunedRule)) ruleBase.add(prunedRule);
//                System.out.println(prunedRule);

            } else {
                int i = condsPrune.size()-1;
                while(condsPrune.size() > hyper){
                    condsPrune.remove(i);
                    i--;
                }
            }

        } else {
            Rule newRule = new Rule(inputs,cmds,"OBJECTIFIED");
            if(!inRuleBase(newRule)) ruleBase.add(newRule);
//            System.out.println(newRule);

        }
    }

    public void printRuleBase(){
        for(Rule r : ruleBase){
            System.out.println(r);
        }
    }

    public boolean inRuleBase(Rule r){
        for(Rule rb : ruleBase){
            if(rb.getConds().containsAll(r.getConds()) && rb.getActions().containsAll(r.getActions())){
                return true;
            }
        }

        return false;
    }

    public void addToRuleBase(ArrayList<Rule> rules){
        ruleBase.addAll(rules);
    }
}