import java.io.FileNotFoundException;
import java.util.ArrayList;

// Joseph Vybihal (c) 2022
//
// This class stores all the knowledge-base rules and implements both the think() method and the loadRules() method from the
// knowledge-base file.

public class Productions {
    private String filenameRuleBase;
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

    public void addFact(Clause aFact) {
        this.facts.add(aFact);
    }

    public ArrayList<Clause> getFacts() {return this.facts;}

    public ArrayList<Clause> getMarkedFacts() {
        for(int i = 0; i < facts.size(); i++) {
            Clause fact = facts.get(i);
            String pred = fact.getPredicate();
//            System.out.println(fact);
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
}
