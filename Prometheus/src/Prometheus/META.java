package Prometheus;

import expertsystem.*;
import java.util.ArrayList;

public class META {
    private ArrayList<Clause> classifications;
    private ArrayList<Clause> commands;

    META() {
        this.classifications = new ArrayList<Clause>();
        this.commands        = new ArrayList<Clause>();
    }

    public void clearInput() {
        this.classifications.clear();
        this.commands.clear();
    }

    public void think(ArrayList<Clause> classifications, ArrayList<Clause> commands) {
        this.classifications = classifications;
        this.commands        = commands;
    }

    public void printClassifications() {}
    public void printCommands() {}
}
