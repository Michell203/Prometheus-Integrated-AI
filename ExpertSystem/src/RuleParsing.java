import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// Joseph Vybihal (c) 2022
//
// This is the class that parses the knowledge-base file converting the database syntax into the Rules of the
// production system (defined in the Productions class).

public class RuleParsing {
    String filename;
    Scanner ptr;

    RuleParsing(String filename) throws FileNotFoundException{
        this.filename = filename;
        ptr = new Scanner(new FileReader(filename));
    }

    Rule getRule() {
        String word = "START";
        int rulePart = -1; // -1 indicates not a rule

        String groupName = "NOGROUP";
        ArrayList<String> conditions = new ArrayList<String>();
        ArrayList<String> actions    = new ArrayList<String>();

        try {
            word = ptr.next().toUpperCase();

            while (!word.equals("RULE:")) {
                word = ptr.next().toUpperCase();
                while(!word.equals(":ENDNOTE")) {
                    word = ptr.next().toUpperCase();
                }
                word = ptr.next().toUpperCase();
            }

            if (word.equals("RULE:")) rulePart = 0;

            while(!word.equals(":END")) {
                switch(rulePart) {
                    case 0: // expecting group name
                        groupName = ptr.next().toUpperCase();
                        rulePart = 1;
                        break;
                    case 1: // expecting conditions until =>
                        word = ptr.next().toUpperCase();
                        while(!word.equals("=>")) {
                            conditions.add(word);
                            word = ptr.next().toUpperCase();
                        }
                        rulePart = 2;
                        break;
                    case 2: // expecting actions until :END
                        word = ptr.next().toUpperCase();
                        while(!word.equals(":END")) {
                            actions.add(word);
                            word = ptr.next().toUpperCase();
                        }
                        rulePart = -1;
                        break;
                }
            }
        } catch (Exception e) {
            return null;
        }

        return new Rule(groupName, conditions, actions);
    }
}
