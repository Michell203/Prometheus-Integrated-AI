import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class KNNParsing {
    String filename;
    Scanner ptr;

    KNNParsing(String filename) throws FileNotFoundException {
        this.filename = filename;
        ptr = new Scanner(new FileReader(filename));
    }

    KnowledgeNode getKN() throws Exception {
        String word = "START";
        int rulePart = -1; // -1 indicates not a rule

//        String groupName = "NOGROUP";
        String tag = "tag";
        int threshold = 1;
        ArrayList<Clause> tagList = new ArrayList<>();
        String filename = "filename";

        try {
            word = ptr.next().toUpperCase();

            while (!word.equals("KN:")) {
                word = ptr.next().toUpperCase();
                while(!word.equals(":ENDNOTE")) {
                    word = ptr.next().toUpperCase();
                }
                word = ptr.next().toUpperCase();
            }

            if (word.equals("KN:")) rulePart = 0;

            while(!word.equals(":END")) {
                switch(rulePart) {
                    case 0: // expecting tag
                        tag = ptr.next().toUpperCase();
                        rulePart = 1;
                        break;
                    case 1: // expecting threshold
//                        word = ptr.next().toUpperCase();

                        threshold = Integer.parseInt(ptr.next());
                        rulePart = 2;

                        while(!word.equals("=>")) {
                            word = ptr.next().toUpperCase();
                        }

                        break;
                    case 2: // expecting tagList until =>
                        word = ptr.next().toUpperCase();
                        while(!word.equals("=>")) {
                            tagList.add(new Clause(word));
                            word = ptr.next().toUpperCase();
                        }
                        rulePart = 3;
                        break;

                    case 3: // expecting filename
//                        word = ptr.next().toUpperCase();
//                        while(!word.equals("=>")) {
//                            tagList.add(word);
//                            word = ptr.next().toUpperCase();
//                        }
//                        rulePart = 3;
                        filename = ptr.next();
                        rulePart = -1;
                        word = ptr.next().toUpperCase();
                        break;
                }
            }
        } catch (Exception e) {
            return null;
        }

        return new KnowledgeNode(new Clause(tag), tagList, threshold, filename);
    }
}
