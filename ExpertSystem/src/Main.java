// Joseph Vybihal (c) 2022
//
// Finished: Stage 1 - Basic simple production system
// Finished: Stage 1.1 - Rule-base text file
// Finished: Stage 1.2 - Parsing rule base into production system
// Finished: Stage 1.3 - Rule object
// Finished: Stage 1.4 - Production system think()
// Finished: Stage 1.5 - Testing stage 1
// Finished: Stage 2 - Complex rule processing
// Finished: Stage 2.1 - Rule variables
// Finished: Stage 2.2 - Rule functions
// Finished: Stage 2.3 - Testing stage 2
// TODO: Stage 3 - Real rule-base case study
// TODO: Stage 4 - Merge with Knowledge Node System

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Tester t = new Tester();

            String names[] = t.getFileName("resources/setup.txt");

            String factsFName = names[0];
            String ruleBase = names[1];

            Clause[] facts = t.loadFactsFromFile(factsFName);

            System.out.println("Hello world!");

            // Tests for Stage 1
            Productions p = new Productions(ruleBase, facts);
            p.think();
            System.out.println(p);
            System.out.println(p.getMarkedFacts());

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}