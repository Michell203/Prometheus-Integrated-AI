import java.util.ArrayList;

// Joseph Vybihal (c) 2022
//
// Each rule is made out of a set of clauses both in the condition and actions parts.

public class Clause {
    private String predicate;
    private ArrayList<String> terms;

    Clause(String token) throws Exception {
        this.predicate = "";
        this.terms     = new ArrayList<String>();

        parse(token);
    }

    Clause(String predicate, ArrayList<String> terms) {
        this.predicate = predicate;
        this.terms     = new ArrayList<String>();

        this.terms.addAll(terms);
    }

    private void parse(String token) throws Exception {
        String term="", value="";
        char c;
        int state = 0; //0=pred, 1=term, 2=done

        for(int i=0; i<token.length() && state!=2; i++) {
            c = token.charAt(i);
            if (state==0 && c != '(') this.predicate += c;
            if (state==0 && c == '(') {state = 1; continue; }
            if (state==1 && c != ',' && c != ')') term += c;
            if (state==1 && (c == ',' || c == ')') ) {
                if (c==',') state = 1; else state = 2;

                if (term.isEmpty()) throw new Exception("Rule Clause Term has no value for token: "+token);

                this.terms.add(term);

                term="";
            }
        }
    }

    public void setPredicate(String s) {this.predicate = s;}
    public String getPredicate() { return this.predicate; }
    public ArrayList<String> getTerms() { return this.terms; }
    public boolean isVariable(int index) {
        if (this.terms.get(index).charAt(0) == '<') return true;
        return false;
    }

    // Returns: 0=false, 1=matched w/o vars, 2=matched w/ vars
    public int match(Clause fact) {
        boolean hasVar=false;
        String term="";

        if (this.predicate.equals((fact.getPredicate()))) {
            if (this.terms.isEmpty() && fact.getTerms().isEmpty()) return 1;
            if (this.terms.size() != fact.getTerms().size()) return 0;

            for(int i=0; i<this.terms.size(); i++) {
                term = this.terms.get(i);
                if (term.charAt(0)=='<') hasVar = true;
                else {
                    if (!term.equals(fact.getTerms().get(i))) return 0;
                }
            }

            if (hasVar) return 2;
            return 1;
        }

        return 0;
    }

    public String toString() {
        return this.predicate + " " + this.terms.toString();
    }
}
