import java.util.ArrayList;

// Joseph Vybihal (c) 2022
//
// A library of all the functions as static methods
public class Functions {
    //
    // Internal private process methods ------------------------------------------------------------------------------
    //
    private static boolean found = false;

    private static String getVar(Clause X, ArrayList<Variable> vars, int position) {
        found = true; // assume we will find it

        if (vars.isEmpty() || vars.size()<position) {found = false; return null;}

        if (X.getTerms().get(position).charAt(0) != '<') return "null";
        else return X.getTerms().get(position);
    }

    private static String getVal(Clause X, ArrayList<Variable> vars, int position, String var) {
        if (vars.isEmpty() || vars.size()<position) {found = false; return null;}

        if (X.getTerms().get(position).charAt(0) != '<') {found = true; return X.getTerms().get(position);}
        else {
            found = false;
            for(Variable V: vars) {
                if (V.getIdentifier().equals(var)) {found=true; return V.getValue();}
            }

            if (!found) return null;
        }

        found = false;
        return null;
    }

    //
    // Public CONDITION function methods ----------------------------------------------------------------------------
    //
    public static boolean isEqual(Clause X, ArrayList<Variable> vars) {
        String var1="", val1="";
        String var2="", val2="";
        int position, result;

        // Instruction syntax Correct?

        if (X.getTerms().size() != 2) return false;

        // Get parameters

        position = 0;
        var1 = Functions.getVar(X, vars, position);
        if (!found) return false;
        val1 = Functions.getVal(X, vars, position, var1);
        if (!found) return false;

        position = 1;
        var2 = Functions.getVar(X, vars, position);
        if (!found) return false;
        val2 = Functions.getVal(X, vars, position, var2);
        if (!found) return false;

        // Do operation

        try {
            result = val1.compareTo(val2);
            if (result != 0) return false;
        } catch(NullPointerException e) {
            return false;
        }

        return true;
    }

    public static boolean lessthan(Clause X, ArrayList<Variable> vars) {
        String var1="", val1="";
        String var2="", val2="";
        int position, result;

        // Instruction syntax Correct?

        if (X.getTerms().size() != 2) return false;

        // Get parameters

        position = 0;
        var1 = Functions.getVar(X, vars, position);
        if (!found) return false;
        val1 = Functions.getVal(X, vars, position, var1);
        if (!found) return false;

        position = 1;
        var2 = Functions.getVar(X, vars, position);
        if (!found) return false;
        val2 = Functions.getVal(X, vars, position, var2);
        if (!found) return false;

        // Do operation

        try {
            result = val1.compareTo(val2);
            if (result == 0 || result < 0) return false;
        } catch(NullPointerException e) {
            return false;
        }

        return true;
    }

    public static boolean greaterthan(Clause X, ArrayList<Variable> vars) {
        int position, result;
        String var1="", val1="";
        String var2="", val2="";

        // Instruction syntax correct?

        if (X.getTerms().size() != 2) return false;

        // Get parameters

        position = 0;
        var1 = Functions.getVar(X, vars, position);
        if (!found) return false;
        val1 = Functions.getVal(X, vars, position, var1);
        if (!found) return false;

        position = 1;
        var2 = Functions.getVar(X, vars, position);
        if (!found) return false;
        val2 = Functions.getVal(X, vars, position, var2);
        if (!found) return false;

        // Do operation

        try {
            result = val1.compareTo(val2);
            if (result == 0 || result > 0) return false;
        } catch(NullPointerException e) {
            return false;
        }

        return true;
    }

    //
    // Public ACTION function methods -------------------------------------------------------------------------------
    //

}
