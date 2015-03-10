/*************************************************************
 * Name:  Yoav Nathaniel                                    *
 * Project:  Project 2 - Maze Solver                        *
 * Class:  CMPS 331 - Artificial Intelligence               *
 * Date:  March 10, 2015                                    *
 *************************************************************/


package com.example.yoav.mazesolverp2;

import android.content.Context;
import android.os.Environment;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Created by Yoav on 2/17/2015.
 */
public class FileManager {
    private int fileCount;
    public int selectedFile = 0;
    private Vector<File> files;
    public Vector<String> fileNames;
    private Context context;
    private LinearLayout layout;
    private DrawGrid drawGrid;
    private BuildGrid buildGrid;

    /*
    * sets up file variables for the class.
    * this class manages the whole process of reading text from
    * files and building/printing/solving mazes.
    * Takes context and linear layout as inputs - those will be used for
    * printing data and mazes onto the screen.
    */
    FileManager(Context c, LinearLayout l){
        context = c;
        layout = l;
        files = new Vector<File>();
        fileNames = new Vector<String>();
    }
    /*
    * retrieves all of the files from the public Documents directory of the Android device.
    * only finds .txt files with containing 'theMaze' in their name.
    * for example: theMaze5.txt
    * It puts all of the files and their names into files and fileName vectors, respectively.
    */
    public int getTextFiles() {
        File path = Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DOCUMENTS);
        fileCount = 0;
        for (File f : path.listFiles()) {
            if (f.isFile()) {
                String fileName = f.getName();
                if (fileName.contains("theMaze") && fileName.contains(".txt")) {
                    fileCount++;
                    files.add(f);
                    fileNames.add(fileName);
                }
            }
        }
        return fileCount;
    }
    /*
    * Takes the selected file (0 is the first file) from files vector.
    * Takes two TextViews to display information on the screen (dimensions
    * and complexity of maze).
    * The boolean reset is to let the method know there is a change in the selected file.
    * getFileInfo takes the selected file, reads the bytes, and moves to translateToChar.
    */
    public void getFileInfo(int selected, TextView dataFoundText, TextView complexityText,
                            boolean reset) {
        selectedFile = selected;
        File currentFile = files.get(selectedFile);
        int length = (int)currentFile.length();
        byte[] bytes = new byte[length];

        try {
            FileInputStream in = new FileInputStream(currentFile);
            in.read(bytes);
            in.close();
            dataFoundText.setText("Successfully Read File");
        }
        catch(IOException e){
            dataFoundText.setText("Could Not Read File");
            return;
        }
        translateToChar(bytes, dataFoundText, complexityText, reset);
    }
    /*
    * Called from getFileInfo. It takes an array of bytes (data found in text file), the two
    * TextViews to display data, and the boolean to see if this is the first time a maze is
    * being built or not.
    * translateToChar converts all of the bytes into characters, gets and prints the
    * dimensions, and calls translateToGrid.
     */
    private void translateToChar(byte [] bytes, TextView dataFoundText, TextView complexityText,
                                 boolean reset) {
        char contents[] = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++){
            contents[i] = (char)bytes[i];
        }

        int Dimens = 0;
        int d = 0;
        while (contents[Dimens] != '\n'){
            Dimens++;
            if (contents[Dimens] == '-'){
                d++;
            }
        }
        String dimensions = translateDimensions(d);
        dataFoundText.setText("Dimensions of Maze: " + dimensions);
        translateToGrid(contents, Dimens, d, complexityText, reset);
    }
    /*
    * Takes in array of chars (data of the file), ldimens = (amount of chars of first line - 1)
    * d = dimensions of grid (if 6x6, d = 6),  TextView of the complexity of the maze, and the
    * reset boolean.
    * translateToGrid builds a maze and prints it out. Then, it finds the complexity of the maze.
     */
    private void translateToGrid(char [] chararray, int ldimens, int d, TextView complexityText,
                                 boolean reset) {
        int rd = (d*2)-1;
        if (reset){
            buildGrid.reBuild(chararray, ldimens, rd);
            drawGrid.reDraw(buildGrid, rd);
        }
        else {
            buildGrid = new BuildGrid(chararray, ldimens, rd);
            drawGrid = new DrawGrid(layout, rd, context, buildGrid);
        }
        String complex = buildGrid.findBBPaths();
        complexityText.setText("Complexity of Maze: " + complex);

    }
    /*
    Takes int as parameter. translateDimensions translates the dimensions of the grid into text.
    Example: if maze is 6x6, count = 6 and the return string = "6 x 6".
     */
    private String translateDimensions(int count){
        String d = "";
        switch (count){
            case 6:
                d = "6 x 6";
                break;
            case 7:
                d = "7 x 7";
                break;
            case 8:
                d = "8 x 8";
                break;
            case 9:
                d = "9 x 9";
                break;
            case 10:
                d = "10 x 10";
                break;
        }
        return d;
    }

    /*
    * Takes int a parameter. search uses the MazePath class to get the search path of the maze
    * (depending on the searchType the user seeks), and draws the steps the search path took
    * using DrawGrid's drawSearch method.
     */
    public void search(int searchType){
        MazePath mazePath = new MazePath(buildGrid);
        Vector<Cell> path;
        drawGrid.reDraw(buildGrid, buildGrid.getDimens());
        switch (searchType){
            case 0:
                break;
            case 1:
                path = mazePath.DepthFirstPath();
                if (!path.isEmpty()) {
                    drawGrid.drawSearch(path);
                }
                break;
            case 2:
                path = mazePath.BreadthFirstPath();
                if (!path.isEmpty()) {
                    drawGrid.drawSearch(path);
                }
                break;
            case 3:
                path = mazePath.BestFirstPath();
                if (!path.isEmpty()) {
                    drawGrid.drawSearch(path);
                }
                break;
            case 4:
                path = mazePath.aStarPath();
                if (!path.isEmpty()) {
                    drawGrid.drawSearch(path);
                }
                break;
        }
    }


}
