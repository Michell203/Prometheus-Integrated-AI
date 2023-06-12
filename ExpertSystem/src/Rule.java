// Joseph Vybihal (c) 2022
//

import java.lang.reflect.Array;
import java.util.ArrayList;

// Rule evaluation operates this way:
// Syntax:
// COND1 COND2 COND3 FN1 FN2 => ACT1 ACT2 ACT3 FN3 FN4
// Where:
//        CONDn are a series a predicates with or without arguments, or #predicate
//        FNn are a series of predicates with arguments
//        ACTn are a series of predicates with or without arguments, or #predicate
// Evaluation:
//        Step 1: Match all the CONDn terms with the facts-list, return false if anything does not match
//        Step 2: Passing step 1, we evaluate each FNn on the condition side, return false is anything does not match
//        Step 3: If step 1 and step 2 pass, then the rule is considered to be true
//        Step 4: Given step 3, add all ACTn terms to the facts-list
//        Step 5: Execute each FNn from the action part of the rule
// Note:
//        Condition side functions: these are all true/false execution expressions
//        Action side functions: these are all commands that change the rule-base in some way
//        #predicates: these are considered "marked" and are collected into a separate list
//                     these marked predicates are often used to mean something in other parts of the AI

public class Rule {
    private boolean fired;
    private String GroupName;
    private ArrayList<Clause> conditions;
    private ArrayList<Clause> actions;
    private ArrayList<Variable> variables;

    // --- Condition Side of Rule ---
    // $greater <x> <y>                 (true/false)
    // $less <x> <y>                    (true/false)
    // $equal <x> <y>                   (true/false)
    // --- Action Side of Rule ---
    // $assert clause-as-string-token   (overwrites)
    private ArrayList<Clause> functions;

    Rule(String groupName, ArrayList<String> cond, ArrayList<String> act) {
        Clause temp;

        this.fired      = false;
        this.GroupName  = groupName;
        this.conditions = new ArrayList<Clause>();
        this.actions    = new ArrayList<Clause>();
        this.variables  = new ArrayList<Variable>();
        this.functions  = new ArrayList<Clause>();

        try {
            for (String c : cond) {
                temp = new Clause(c);
                if (temp.getPredicate().charAt(0) == '$') this.functions.add(temp);
                else this.conditions.add(temp);
            }
            for (String a: act)   this.actions.add(new Clause(a));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean isFound(Clause key, ArrayList<Clause> facts) {
        int result;
        String var, val;
        boolean found=false;

        for(Clause F: facts) {
            result = key.match(F);
            if (result == 1) return true;
            if (result == 2) {
                // find the variables in the clause, if one exists, get the value from the fact
                for(int i=0; i<key.getTerms().size(); i++) {
                    var = key.getTerms().get(i);
                    val = F.getTerms().get(i);

                    if (var.charAt(0)=='<') {
                        // It is a variable, then
                        // does it exist in Variables? if not add, if yes update value
                        found=false;

                        for(int j=0; j<this.variables.size(); j++) {
                            if (var.equals(this.variables.get(j))) {
                                this.variables.get(j).setValue(val);
                                found=true;
                                break;
                            }
                        }

                        if (!found) this.variables.add(new Variable(var,val));
                    }
                }

                return true;
            }
        }

        return false;
    }

    public boolean match(ArrayList<Clause> facts) throws Exception {
        if (this.fired) return false;

        for(Clause c: this.conditions) {
            // Added NOT
            if(c.getPredicate().charAt(0) == '-'){
                Clause withoutNot = new Clause(c.getPredicate().substring(1), c.getTerms());
//                System.out.println("Without NOT: " + withoutNot);

                if (isFound(withoutNot,facts)){
                    return false;
                }

            } else {
                if(!isFound(c,facts)){
                    return false;
                }
            }

//            if (!isFound(c,facts)) return false; // Original
        }

        for(Clause c: this.functions) {
            if (c.getPredicate().equals("$GREATER") && !Functions.greaterthan(c,this.variables)) return false;
            if (c.getPredicate().equals("$LESS") && !Functions.lessthan(c,this.variables)) return false;
            if (c.getPredicate().equals("$EQUAL") && !Functions.isEqual(c,this.variables)) return false;
        }

        this.fired = true;
        return true;
    }

    private String getVarVal(String aTerm) {
        if (aTerm.charAt(0) != '<') return new String(aTerm);
        else {
            for (Variable V : this.variables) {
                if (V.getIdentifier().equals(aTerm)) {
                    return new String(V.getValue());
                }
            }
        }
        return null;
    }

    public ArrayList<Clause> getProcessedActions() {
        ArrayList<Clause> processed = new ArrayList<Clause>();
        ArrayList<String> terms = new ArrayList<String>();
        Clause temp;
        ArrayList<String> tempTerms;

        for(Clause A: this.actions) {
            temp = null;

            if (A.getPredicate().charAt(0) == '$') { // TODO: finish action functions
                if (A.getPredicate().equals("$ASSERT")) {
                    for(String T : A.getTerms()) {
                        try {
                            temp      = new Clause("$"+T);
                            tempTerms = new ArrayList<String>();
                            for(String subterm: temp.getTerms()) {
                                tempTerms.add(this.getVarVal(subterm));
                            }
                            temp = new Clause(temp.getPredicate(), tempTerms);
                        } catch(Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            } else {
                for (String T : A.getTerms()) terms.add(this.getVarVal(T));
            }

            if (temp == null) processed.add(new Clause(A.getPredicate(), terms));
            else processed.add(temp);
        }

        return processed;
    }

    public ArrayList<Clause> getActions() {
        return this.actions;
    }

    public String getGroupName() {
        return this.GroupName;
    }

    public String toString() {
        return "Rule: "+ this.GroupName + " Fired: " + this.fired + "\nConditions: " + this.conditions.toString() +
                "\nActions: " + this.actions.toString() + "\nVariables: " + this.variables.toString() + "\nFunctions: " +
                this.functions.toString() + "\n\n";
    }
}
