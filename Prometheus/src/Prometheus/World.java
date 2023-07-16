package Prometheus;

import expertsystem.*;

public class World {
    private char map[][];
    private int robotX, robotY, direction; // 0 = Down, 1 = Up, 2 = Left, 3 = Right
    private boolean won = false;
    private boolean lost = false;

    World(char[][] map, int x, int y, int dir) { this.map = map; robotX=x; robotY=y; direction=dir; }

    // Getters
    public int IRFront() {
        int count = 0;

        int row = robotY;
        int col = robotX;
        int i;

        if (direction == 0 && row < 9) { // Down
            for(i = row + 1; i < map.length && map[i][col] != 'X' && map[i][col] != 'D' && i < 10 && col < 10; i++) {
                count++;
            }

        } else if (direction == 1 && row > 0) { // Up
            for(i = row - 1; i > -1 && map[i][col] != 'X' && map[i][col] != 'D' && col < 10; i--) {
                count++;
            }

        } else if (direction == 2 && col > 0) { // Left
            for(i = col - 1; i > -1 && map[row][i] != 'X' && map[row][i] != 'D' && i < 10 && col < 10; i--) {
                count++;
            }

        } else if (direction == 3) { // Right
            for(i = col + 1; i < map.length && map[row][i] != 'X' && map[row][i] != 'D'  && i < 10 && col < 10; i++) {
                count++;
            }
        }

        return count;
    }

    public int IRLeft(){ // Sensor at left side
        int count = 0;

        int row = robotY;
        int col = robotX;

        if(direction == 0 && row < 9){ // Facing down
            for(int i = col+1; i < map.length; i++){
                if(map[row][i] != 'X' && map[row][i] != 'D' && i < 10 && col < 10){
                    count++;
                } else {
                    break;
                }
            }

        } else if(direction == 1 && row > 0){ // Facing up
            for(int i = col-1; i > -1; i--){
                if(map[row][i] != 'X' && map[row][i] != 'D' && i < 10 && col < 10){
                    count++;
                } else {
                    break;
                }
            }

        } else if(direction == 2 && col > 0){ // Facing left
            for(int i = row+1; i < map.length; i++){
                if(map[i][col] != 'X' && map[i][col] != 'D' && i < 20 && col < 20){
                    count++;
                } else {
                    break;
                }
            }

        } else if(direction == 3){ // Facing right
            for(int i = row-1; i > -1; i--){
                if(i > -1 && map[i][col] != 'X' && map[i][col] != 'D' && col < 10){
                    count++;
                } else {
                    break;
                }
            }
        }
        return count;
    }

    public int IRRight(){ // Sensor at right side
        int count = 0;

        int row = robotY;
        int col = robotX;

        if(direction == 0 && row < 9){ // Facing down
            for(int i = col-1; i > -1; i--){
                if(map[row][i] != 'X' && map[row][i] != 'D' && i < 10 && col < 10){
                    count++;
                } else {
                    break;
                }
            }

        } else if(direction == 1 && row > 0){ // Facing up
            for(int i = col+1; i < map.length; i++){
                if(map[row][i] != 'X' && map[row][i] != 'D' && i < 10 && col < 10){
                    count++;
                } else {
                    break;
                }
            }

        } else if(direction == 2 && col > 0){ // Facing left
            for(int i = row-1; i > -1; i--){
                if(i > -1 && map[i][col] != 'X' && map[i][col] != 'D' && col < 10){
                    count++;
                } else {
                    break;
                }
            }

        } else if(direction == 3){ // Facing right
            for(int i = row+1; i < map.length; i++){
                if(map[i][col] != 'X' && map[i][col] != 'D' && i < 10 && col < 10){
                    count++;
                } else {
                    break;
                }
            }
        }
        return count;
    }

    public int isDirB(){
        int row = robotY;
        int col = robotX;

        int i;
        if(direction == 0 && row < 9){ // Down
            for(i = row+1; i < map.length; i++){
                for(int j = 0; j < map[0].length; j++){
                    if(map[i][j] == 'B') {
//                        System.out.println("isDirB");
                        return 1;
                    }

                }
            }

        } else if(direction == 1 && row > 0){ // Up
            for(i = row-1; i > 0; i--){
                for(int j = 0; j < map[0].length; j++){
                    if(map[i][j] == 'B') {
//                        System.out.println("isDirB");
                        return 1;
                    }
                }
            }

        } else if(direction == 2 && col > 0){ // Left
            for(i = col-1; i > 0; i--){
                for(int j = 0; j < map.length; j++){
                    if(map[j][i] == 'B') {
//                        System.out.println("isDirB");
                        return 1;
                    }
                }
            }

        } else if(direction == 3 && col < 9){ // Right
            for(i = col+1; i < map.length; i++){
                for(int j = 0; j < map.length; j++){
                    if(map[j][i] == 'B') {
//                        System.out.println("isDirB");
                        return 1;
                    }
                }
            }
        }
//        System.out.println("Not isDirB");
        return 0;

    }

    public int atB(){
        int rows = map.length;
        int cols = map[0].length;

        int rRow = robotY;
        int rCol = robotX;

        for (int i = rRow - 1; i <= rRow + 1; i++) {
            for (int j = rCol - 1; j <= rCol + 1; j++) {
                if (i >= 0 && i < rows && j >= 0 && j < cols && map[i][j] == 'B') {
                    return 1;
                }
            }
        }

        return 0;
    }

    // Setters
    public void step() {
        switch (this.direction) {
            case 0: // Down
                if(robotX < 9 && map[robotY + 1][robotX] == 'D'){
                    lost = true;
                    break;
                }

                map[robotY][robotX] = map[robotY + 1][robotX];
                map[robotY + 1][robotX] = 'R';

                this.robotY += 1;
                break;

            case 1: // Up
                if(robotX > 0 && map[robotY - 1][robotX] == 'D'){
                    lost = true;
                    break;
                }

                map[robotY][robotX] = map[robotY - 1][robotX];
                map[robotY - 1][robotX] = 'R';

                this.robotY -= 1;
                break;

            case 2: // Left
                if(robotY > 0 && map[robotY][robotX-1] == 'D'){
                    lost = true;
                    break;
                }

                map[robotY][robotX] = map[robotY][robotX-1];
                map[robotY][robotX-1] = 'R';

                this.robotX -= 1;
                break;

            case 3: // Right
                if(robotY < 9 && map[robotY][robotX+1] == 'D'){
                    lost = true;
                    break;
                }

                map[robotY][robotX] = map[robotY][robotX+1];
                map[robotY][robotX+1] = 'R';

                this.robotX += 1;
                break;

        }
    }

    public void turn(String direction) {
        switch (this.direction) {
            case 0: // Down
                if(direction.equals("R")){
                    this.direction = 2;

                } else {
                    this.direction = 3;

                }
                break;

            case 1: // Up
                if(direction.equals("R")){
                    this.direction = 3;

                } else {
                    this.direction = 2;

                }
                break;

            case 2: // Left
                if(direction.equals("R")){
                    this.direction = 1;

                } else {
                    this.direction = 0;

                }
                break;

            case 3: // Right
                if(direction.equals("R")){
                    this.direction = 0;

                } else {
                    this.direction = 1;

                }
                break;

        }

    }

    public void stop(){
        won = true;
    }

    public void displayMap() {  // Displays map & robot vars
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void generateMap(){
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                map[i][j] = '.';
            }
        }

        int count = 0;

//        while(count < 10){ // Change number of bad guys
//            int n1 = (int)(Math.random() * 10);
//            int n2 = (int)(Math.random() * 10);
//
//            map[n1][n2] = 'D';
//            count++;
//        }

        while(count < 2){ // Change number of bad guys
            int n1 = (int)(Math.random() * 10);
            int n2 = (int) Math.floor(Math.random() * 7);

            map[n1][n2] = 'D';
            count++;
        }

        map[robotY][robotX] = 'R';
        map[0][0] = 'A';
        map[map.length-1][map[0].length-1] = 'B';

        int first = 3;
        int second = 9 - first+1;

        for(int i = first; i < map.length; i++){
            for(int j = 0; j < first; j++){
                map[i][j] = 'X';
            }
        }

        for(int i = second-1; i >= 0; i--){
            for(int j = second; j < map[0].length; j++){
                map[i][j] = 'X';
            }
        }
    }

    public void printDir(){
        if(direction == 0){
            System.out.println("Direction of R: Down");

        } else if(direction == 1){
            System.out.println("Direction of R: Up");

        } else if(direction == 2){
            System.out.println("Direction of R: Left");

        } else {
            System.out.println("Direction of R: Right");
        }
    }

    public boolean wonGame(){
        return won;
    }

    public boolean lostGame(){
        return lost;
    }

    public int[] findR() {
        for(int i = 0; i < map.length; ++i) {
            for(int j = 0; j < map[i].length; ++j) {
                if (map[i][j] == 'R') {
                    return new int[]{i, j};
                }
            }
        }

        return new int[]{-1, -1};
    }

    public char[][] getMap(){
        return this.map;
    }
}
