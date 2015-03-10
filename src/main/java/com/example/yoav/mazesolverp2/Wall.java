/*************************************************************
 * Name:  Yoav Nathaniel                                    *
 * Project:  Project 2 - Maze Solver                        *
 * Class:  CMPS 331 - Artificial Intelligence               *
 * Date:  March 10, 2015                                    *
 *************************************************************/

package com.example.yoav.mazesolverp2;

public class Wall extends Cell {
    private boolean state = false;

    //sets the status of the wall. true means the wall should be set,
    // false means it should be turned off.
    public void setWall(boolean b) {
        state = b;
    }

    //returns the status of the wall. true means the wall has been set.
    public boolean checkWall() {
        return state;
    }
}