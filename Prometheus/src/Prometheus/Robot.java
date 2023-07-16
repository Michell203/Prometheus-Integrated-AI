package Prometheus;

import expertsystem.*;
import java.util.ArrayList;

public class Robot {
    private char[][] localMap;
    private int x,y,direction;

    Robot(char[][] map, int x, int y, int direction){
        this.localMap = map;
        this.x = x; this.y = y; this.direction = direction;
    }

    public void update(ArrayList<Clause> classifications, ArrayList<Clause> commands) {

    }

    public void printLocalMap() {
        for (int i = 0; i < localMap.length; i++) {
            for (int j = 0; j < localMap[i].length; j++) {
                System.out.print(localMap[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printPositionInfo() {
        System.out.println("Robot X: " + this.x);
        System.out.println("Robot Y: " + this.y);
        System.out.println("Direction: " + this.direction);
    }
}
