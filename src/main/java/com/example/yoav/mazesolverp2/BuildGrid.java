/*************************************************************
 * Name:  Yoav Nathaniel                                    *
 * Project:  Project 2 - Maze Solver                        *
 * Class:  CMPS 331 - Artificial Intelligence               *
 * Date:  March 10, 2015                                    *
 *************************************************************/

package com.example.yoav.mazesolverp2;


import java.util.Vector;

public class BuildGrid {
    private int dimens;
    public Cell cells[][];
    private Cell startCell;
    private Cell goalCell;
    public double complexity;

    /*
    * The constructor method of the BuildGrid class.
    * Takes array of characters, the (number of characters - 1) of the first row, and
    * the dimensions (6x6 maze has dimens = 11)
    * It calls buildMaze.
     */
    BuildGrid(char [] data, int nums, int dimens) {
        buildMaze(data, nums, dimens);
    }
    /*
    * The 're-constructor' method of the BuildGrid class.
    * Takes array of characters, the (number of characters - 1) of the first row, and
    * the dimensions (6x6 maze has dimens = 11)
    * It calls buildMaze.
     */
    public void reBuild(char [] data, int nums, int dimens){
        buildMaze(data, nums, dimens);
    }
    /*
    * The method that builds a maze.
    * Takes array of characters, the (number of characters - 1) of the first row, and
    * the dimensions (6x6 maze has dimens = 11)
    * Fills up the 2D array of cells with empty cells, walls or rooms.
    * Then it calls majorCells to designate the start and goal cells.
     */
    private void buildMaze(char [] data, int nums, int d){
        dimens = d;
        cells = new Cell[dimens][dimens];
        int sy = 0;
        int gy = 0;
        int dataCount = 0;

        for (int i = 0; i < nums-1; i++) {
            for (int o = 0; o <= nums; o++) {
                if (o < nums-1) {
                    if (o != 0 && i != 0 && o != nums-2 && i != nums-2) {
                        if (i % 2 == 0) {
                            if (o % 2 == 0) {
                                cells[o - 1][i - 1] = new Cell();
                                cells[o - 1][i - 1].type = 0;
                            } else {
                                cells[o - 1][i - 1] = new Wall();
                                cells[o - 1][i - 1].type = 2;
                                if (data[dataCount] == '-') {
                                    cells[o - 1][i - 1].setWall(true);
                                }
                            }
                        } else {
                            if (o % 2 == 0) {
                                cells[o - 1][i - 1] = new Wall();
                                cells[o - 1][i - 1].type = 2;
                                if (data[dataCount] == '|') {
                                    cells[o - 1][i - 1].setWall(true);
                                }

                            } else {
                                if (data[dataCount] == ' ') {
                                    cells[o - 1][i - 1] = new Room();
                                    cells[o - 1][i - 1].type = 1;
                                }
                            }
                        }
                        cells[o-1][i-1].setCoords(o-1, i-1);
                    }
                }
                dataCount++;
            }
        }
        int border = 0;
        for (int i = dimens+4; i < dataCount; i += ((dimens+4)*2)){
            if (data[i] == ' '){
                sy = border;
                break;
            }
            border+=2;
        }
        border = 0;
        for (int i = (dimens*2)+5; i < dataCount; i += ((dimens+4)*2)){
            if (data[i] == ' '){
                gy = border;
                break;
            }
            border+=2;
        }
        majorCells(sy, gy);
    }
    /*
    * Takes the y values of the start and goal cells. Designates coordinates for those cells.
     */
    private void majorCells(int sy, int gy){
        startCell = cells[0][sy];
        goalCell = cells[dimens - 1][gy];
    }
    //returns startCell
    public Cell getStartCell() {
        return startCell;
    }
    //returns goalCell
    public Cell getGoalCell() {
        return goalCell;
    }
    //returns dimensions
    public int getDimens() { return dimens; }
    /*
    * findBBPaths finds the complexity of the maze. It uses the MazePath class to solve this
    * maze using Branch and Bound algorithm. It finds all of the possible paths, and analyzes
    * each of them to find the turns and length of each path. Then it adds up the products of
    * the length and the turns of each path, divides that by the dimensions,
    * (6x6 makes dimensions = 6) and returns the results as a string.
     */
    public String findBBPaths() {
        MazePath path = new MazePath(this);
        path.findBBPaths();
        double newDimens = (dimens+1)/2;
        String r = "";
        Vector<Vector<Cell>> g = path.getPathsDone();
        double sum = 0;
        for (int i = 0; i < g.size(); i++){
            Vector<Cell> s = g.get(i);
            int length = s.size();
            double turns = path.analyzeBBPath(s);
            sum += (length*turns);
        }
        double m = sum / newDimens;
        r = m + "";
        return r;
    }

}
