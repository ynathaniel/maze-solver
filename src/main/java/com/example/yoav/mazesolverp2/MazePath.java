/*************************************************************
 * Name:  Yoav Nathaniel                                    *
 * Project:  Project 2 - Maze Solver                        *
 * Class:  CMPS 331 - Artificial Intelligence               *
 * Date:  March 10, 2015                                    *
 *************************************************************/


package com.example.yoav.mazesolverp2;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

public class MazePath {
    private Vector<Cell> cellPath;
    private Vector<Cell> tempPath;
    private Vector<Cell> Options;
    private Vector<Vector<Cell>> pathsFound;
    private Vector<Vector<Cell>> pathsDone;
    private BuildGrid grid;

    /*
     * Constructor method for the MazePath class. It sets up the variables (mostly vectors).
     * Takes in BuildGrid object.
     */
    public MazePath(BuildGrid g) {
        grid = g;
        cellPath = new Vector<Cell>();
        tempPath = new Vector<Cell>();
        Options = new Vector<Cell>();
        pathsFound = new Vector<Vector<Cell>>();
        pathsDone = new Vector<Vector<Cell>>();
        cellPath.addElement(grid.getStartCell());
        pathsFound.addElement(cellPath);
    }
    /*
     * resets the vectors of the object.
     */
    private void resetMazePath(){
        cellPath.clear();
        pathsFound.clear();
        pathsDone.clear();
        Options.clear();
        tempPath.clear();
        cellPath.add(grid.getStartCell());
        pathsFound.addElement(cellPath);
    }
    /*
     * findBBPaths is the first step to the Branch and Bound algorithm.
     * It generates all of the paths possible, storing temporary ones in pathsFound.
     * Every path that reaches a dead end, is added to pathsDone, a vector of complete paths.
     */
    public void findBBPaths() {
        while (!pathsFound.isEmpty()) {
            cellPath = pathsFound.get(pathsFound.size() - 1);
            pathsFound.removeElement(pathsFound.lastElement());

            Cell current = cellPath.lastElement();
            int x = current.getxCoord();
            int y = current.getyCoord();
            Options.clear();
            if (checkLeft(x, y)) {
                Options.addElement(grid.cells[x - 2][y]);
            }
            if (checkUp(x, y)) {
                Options.addElement(grid.cells[x][y - 2]);
            }
            if (checkRight(x, y)) {
                Options.addElement(grid.cells[x + 2][y]);
            }
            if (checkDown(x, y)) {
                Options.addElement(grid.cells[x][y + 2]);
            }
            if (!Options.isEmpty()) {
                for (int i = Options.size() - 1; i > -1; i--) {
                    tempPath = (Vector<Cell>) cellPath.clone();
                    tempPath.addElement(Options.get(i));
                    pathsFound.addElement(tempPath);

                }
            } else {
                pathsDone.addElement(cellPath);
            }
        }
    }
    /*
     * analyzeBBPath takes a particular path (Cell vector) and returns the number of
     * turns the path takes.
    */
    public double analyzeBBPath(Vector<Cell> path){
        int parent = 0;
        double turns = 0;
        for (int i = 0; i < path.size()-1; i++){
            Cell current = path.get(i);
            int x = current.getxCoord();
            int y = current.getyCoord();
            switch (parent){
                case 0:
                    if (x != grid.getDimens()-1){
                        if (path.get(i+1) != grid.cells[x+2][y]){
                            turns++;
                            parent = verParentFinder(y, path.get(i+1).getyCoord());
                        }
                    }
                    else {
                        turns++;
                        parent = verParentFinder(y, path.get(i+1).getyCoord());
                    }
                    break;
                case 1:
                    if (y != grid.getDimens()-1){
                        if (path.get(i+1) != grid.cells[x][y+2]){
                            turns++;
                            parent = horParentFinder(x, path.get(i+1).getxCoord());
                        }
                    }
                    else {
                        turns++;
                        parent = horParentFinder(x, path.get(i+1).getxCoord());
                    }
                    break;
                case 2:
                    if (x != 0){
                        if (path.get(i+1) != grid.cells[x-2][y]){
                            turns++;
                            parent = verParentFinder(y, path.get(i+1).getyCoord());
                        }
                    }
                    else {
                        turns++;
                        parent = verParentFinder(y, path.get(i+1).getyCoord());
                    }
                    break;
                case 3:
                    if (y != 0){
                        if (path.get(i+1) != grid.cells[x][y-2]){
                            turns++;
                            parent = horParentFinder(x, path.get(i+1).getxCoord());
                        }
                    }
                    else {
                        turns++;
                        parent = horParentFinder(x, path.get(i+1).getxCoord());
                    }
                    break;
            }
        }
        return turns;
    }
    // takes in two y coord values and compares them. Returns the parent accordingly.
    private int verParentFinder(int y, int ny){
        if (ny > y){
            return 1;
        }
        else {
            return 3;
        }
    }
    // takes in two s coord values and compares them. Returns the parent accordingly.
    private int horParentFinder(int x, int nx){
        if (nx > x){
            return 0;
        }
        else {
            return 2;
        }
    }
    // Returns the pathsDone.
    public Vector<Vector<Cell>> getPathsFound(){ return pathsFound; }
    // Returns the pathsFound.
    public Vector<Vector<Cell>> getPathsDone(){ return pathsDone; }
    /*
     * Takes in (x,y) coords of a Cell.
     * returns true if the Cell has on left wall and the cell to the left is not in the cellPath
     * already.
     */
    private boolean checkLeft(int x, int y) {
        if (x != 0) {
            if (!cellPath.contains(grid.cells[x - 2][y]) && !grid.cells[x - 1][y].checkWall()) {
                return true;
            }
        }
        return false;
    }
    /*
     * Takes in (x,y) coords of a Cell.
     * returns true if the Cell has on top wall and the cell above is not in the cellPath
     * already.
     */
    private boolean checkUp(int x, int y) {
        if (y != 0) {
            if (!cellPath.contains(grid.cells[x][y - 2]) && !grid.cells[x][y - 1].checkWall()) {
                return true;
            }
        }
        return false;
    }
    /*
     * Takes in (x,y) coords of a Cell.
     * returns true if the Cell has on right wall and the cell to the right is not in the
     * cellPath already.
     */
    private boolean checkRight(int x, int y) {
        if (x != grid.getDimens() - 1) {
            if (!cellPath.contains(grid.cells[x + 2][y]) && !grid.cells[x + 1][y]
                    .checkWall()) {
                return true;
            }
        }
        return false;
    }
    /*
     * Takes in (x,y) coords of a Cell.
     * returns true if the Cell has a bottom wall and the cell below is not in the cellPath
     * already.
     */
    private boolean checkDown(int x, int y) {
        if (y != grid.getDimens() - 1) {
            if (!cellPath.contains(grid.cells[x][y + 2])){
                if (!grid.cells[x][y + 1].checkWall()) {
                    return true;
                }
            }
        }
        return false;
    }
    /*
     * Solves the BuildGrid object using Depth First Search. It uses a Cell Stack to achieve
     * this blind algorithm.
     * DepthFirstPath returns the path (Cell Vector).
     */
    public Vector<Cell> DepthFirstPath(){
        resetMazePath();
        Stack<Cell> cellStack = new Stack<Cell>();
        cellStack.push(grid.getStartCell());
        Cell current;
        while (!cellPath.contains(grid.getGoalCell())){
            current = cellStack.peek();
            cellStack.pop();
            cellPath.addElement(current);
            int x = current.getxCoord();
            int y = current.getyCoord();
            if (checkLeft(x, y)) {
                if (!tempPath.contains(grid.cells[x - 2][y])){
                    cellStack.push(grid.cells[x - 2][y]);
                    tempPath.addElement(grid.cells[x - 2][y]);
                }
            }
            if (checkUp(x, y)) {
                if (!tempPath.contains(grid.cells[x][y - 2])) {
                    cellStack.push(grid.cells[x][y - 2]);
                    tempPath.addElement(grid.cells[x][y - 2]);
                }
            }
            if (checkRight(x, y)) {
                if (!tempPath.contains(grid.cells[x + 2][y])) {
                    cellStack.push(grid.cells[x + 2][y]);
                    tempPath.addElement(grid.cells[x + 2][y]);
                }
            }
            if (checkDown(x, y)) {
                if (!tempPath.contains(grid.cells[x][y + 2])) {
                    cellStack.push(grid.cells[x][y + 2]);
                    tempPath.addElement(grid.cells[x][y + 2]);
                }
            }
        }
        return cellPath;
    }
    /*
     * Solves the BuildGrid object using Breadth First Search. It uses a Cell Queue to achieve
     * this blind algorithm.
     * BreadthFirstPath returns the path (Cell Vector).
     */
    public Vector<Cell> BreadthFirstPath(){
        resetMazePath();
        Queue<Cell> cellQ = new LinkedList<Cell>();
        cellQ.add(grid.getStartCell());
        Cell current;
        while (!cellPath.contains(grid.getGoalCell())){
            current = cellQ.remove();
            cellPath.addElement(current);
            int x = current.getxCoord();
            int y = current.getyCoord();
            if (checkLeft(x, y)) {
                if (!tempPath.contains(grid.cells[x - 2][y])) {
                    cellQ.add(grid.cells[x - 2][y]);
                    tempPath.addElement(grid.cells[x - 2][y]);
                }
            }
            if (checkUp(x, y)) {
                if (!tempPath.contains(grid.cells[x][y - 2])) {
                    cellQ.add(grid.cells[x][y - 2]);
                    tempPath.addElement(grid.cells[x][y - 2]);
                }
            }
            if (checkRight(x, y)) {
                if (!tempPath.contains(grid.cells[x + 2][y])) {
                    cellQ.add(grid.cells[x + 2][y]);
                    tempPath.addElement(grid.cells[x + 2][y]);
                }
            }
            if (checkDown(x, y)) {
                if (!tempPath.contains(grid.cells[x][y + 2])) {
                    cellQ.add(grid.cells[x][y + 2]);
                    tempPath.addElement(grid.cells[x][y + 2]);
                }
            }
        }
        return cellPath;
    }
    /*
     * Takes Cell as a parameter. This method returns the distance from the cell to the goalCell
     * of the BuildGrid object.
     * Uses the formula : distance = sqrt(((x1-x2)^2)+((y1-y2)^2))
     */
    private int checkGoalDistance(Cell cell){
        int d = 0;
        int cx = cell.getxCoord();
        int cy = cell.getyCoord();
        Cell goal = grid.getGoalCell();
        int gx = goal.getxCoord();
        int gy = goal.getyCoord();

        int xd = 0;
        xd = cx - gx;
        xd = Math.abs(xd);
        int yd = 0;
        yd = cy - gy;
        yd = Math.abs(yd);

        yd *= yd;
        xd *= xd;
        double dub = yd + xd;

        dub = Math.sqrt(dub);
        d = (int)dub;
        return d;
    }
    /*
     * Solves the BuildGrid object using Best First Search. It uses a Cell Stack to achieve
     * this heuristic algorithm. Heuristic depends on distance from Cell x to goalCell.
     * This algorithm is very similar to Depth First Search, except that it includes a Priority
     * Queue to analyze which neighbor it prefers to use.
     * BestFirstPath returns the path (Cell Vector).
     */
    public Vector<Cell> BestFirstPath(){
        resetMazePath();
        Stack<Cell> cellStack = new Stack<Cell>();
        cellStack.push(grid.getStartCell());
        Cell current;
        while (!cellPath.contains(grid.getGoalCell())){
            Options.clear();
            current = cellStack.peek();
            cellStack.pop();
            cellPath.addElement(current);
            int x = current.getxCoord();
            int y = current.getyCoord();
            if (checkLeft(x, y)) {
                if (!tempPath.contains(grid.cells[x - 2][y])){
                    Options.addElement(grid.cells[x - 2][y]);
                }
            }
            if (checkUp(x, y)) {
                if (!tempPath.contains(grid.cells[x][y - 2])) {
                    Options.addElement(grid.cells[x][y - 2]);
                }
            }
            if (checkRight(x, y)) {
                if (!tempPath.contains(grid.cells[x + 2][y])) {
                    Options.addElement(grid.cells[x + 2][y]);
                }
            }
            if (checkDown(x, y)) {
                if (!tempPath.contains(grid.cells[x][y + 2])) {
                    Options.addElement(grid.cells[x][y + 2]);
                }
            }
            if (Options.size() > 0) {
                int Osize = Options.size();
                PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
                int dUnordered[] = new int[Osize];
                int dOrdered[] = new int[Osize];
                boolean match[] = new boolean[Osize];
                for (int i = 0; i < Osize; i++){
                    int distance = checkGoalDistance(Options.get(i));
                    pq.add(distance);
                    dUnordered[i] = distance;
                    match[i] = false;
                }
                for (int i = Osize-1; i >= 0; i--){
                    dOrdered[i] = pq.poll();
                }
                for (int i = 0; i < Osize; i++){
                    for (int o = 0; o < Osize; o++){
                        if (!match[o]){
                            if (dOrdered[i] == dUnordered[o]) {
                                match[o] = true;
                                tempPath.addElement(Options.get(o));
                                cellStack.push(Options.get(o));
                            }
                        }
                    }
                }
            }
            if (cellStack.isEmpty() && !cellPath.contains(grid.getGoalCell())){
                break;
            }
        }
        return cellPath;
    }
    /*
     * Solves the BuildGrid object using A* Search. It uses a Cell Stack to achieve
     * this heuristic algorithm. Heuristic depends on distance from Cell x to goalCell and the
     * length of the path.
     * This algorithm is very similar to Best First Search, except for the first for loop that
     * scans all of the found paths and continues with the path that has most minimal heuristic
     * value.
     * aStarPath returns the path (Cell Vector).
     */
    public Vector<Cell> aStarPath(){
        resetMazePath();
        boolean run = true;
        while (run){
            int min = 10000;
            int select = 0;
            //the for loop below makes cellPath = the shortest path found yet.
            for (int i = 0; i < pathsFound.size(); i++){
                cellPath = pathsFound.get(select);
                int f = cellPath.size();
                Cell current = cellPath.get(f-1);
                f += checkGoalDistance(current) + f;
                if (f < min){
                    min = f;
                    select = i;
                }
            }
            cellPath = pathsFound.get(select);
            pathsFound.removeElementAt(select);
            Cell current = cellPath.get(cellPath.size()-1);
            if (current == grid.getGoalCell()){
                run = false;
            }
            else {
                Options.clear();
                int x = current.getxCoord();
                int y = current.getyCoord();
                if (checkLeft(x, y)) {
                    if (!cellPath.contains(grid.cells[x - 2][y])){
                        Options.addElement(grid.cells[x - 2][y]);
                    }
                }
                if (checkUp(x, y)) {
                    if (!cellPath.contains(grid.cells[x][y - 2])) {
                        Options.addElement(grid.cells[x][y - 2]);
                    }
                }
                if (checkRight(x, y)) {
                    if (!cellPath.contains(grid.cells[x + 2][y])) {
                        Options.addElement(grid.cells[x + 2][y]);
                    }
                }
                if (checkDown(x, y)) {
                    if (!cellPath.contains(grid.cells[x][y + 2])) {
                        Options.addElement(grid.cells[x][y + 2]);
                    }
                }
                if (Options.size() > 0) {
                    int Osize = Options.size();
                    int d[] = new int[Osize];
                    boolean match[] = new boolean[Osize];
                    for (int i = 0; i < Osize; i++) {
                        tempPath = (Vector<Cell>)cellPath.clone();
                        tempPath.addElement(Options.get(i));
                        pathsFound.addElement(tempPath);
                    }
                }

            }



        }
        return cellPath;
    }
}
