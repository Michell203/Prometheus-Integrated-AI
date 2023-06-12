// Joseph Vybihal (c) 2022
//
// The variable object for the list of variables in the Rule class

public class Variable {
    private String identifier;
    private String value;

    Variable(String id, String val) {
        this.identifier = id;
        this.value      = val;
    }

    public void setValue(String val) { this.value = val; }

    public String getIdentifier() { return this.identifier; }

    public String getValue() { return this.value; }

    public String toString() {
        return this.identifier + " = " + this.value;
    }
}
