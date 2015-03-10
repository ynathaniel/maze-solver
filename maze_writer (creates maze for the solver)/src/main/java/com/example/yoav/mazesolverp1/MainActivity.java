package com.example.yoav.mazesolverp1;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class MainActivity extends ActionBarActivity {
    TextView writeID;
    int dimens;
    Context context;
    String[] sizes = {"6x6", "7x7", "8x8", "9x9", "10x10"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dimens = 11;

        writeID = (TextView)findViewById(R.id.writeID);
    }

    public void radioClick(View view){
        boolean selected = ((RadioButton) view).isChecked();
        switch (view.getId()){
            case R.id.rb6:
                if (selected) {
                    dimens = 11;
                }
                break;
            case R.id.rb7:
                if (selected) {
                    dimens = 13;
                }
                break;
            case R.id.rb8:
                if (selected) {
                    dimens = 15;
                }
                break;
            case R.id.rb9:
                if (selected) {
                    dimens = 17;
                }
                break;
            case R.id.rb10:
                if (selected) {
                    dimens = 19;
                }
                break;
        }
    }
    public void generateMaze(View view){
        Grid grid = new Grid(dimens);
        while (!grid.getCellStack().isEmpty()){
            grid.explore();
        }
        if (!grid.getGoalCell().getVisited()){
            grid.resetGrid();
            generateMaze(view);
            return;
        }
        int fileDimens = dimens+2;
        char mazeCode[] = new char[(fileDimens*fileDimens)+(fileDimens*2)];
        createCode(mazeCode, grid, fileDimens);
        writeToTextFile(mazeCode);


    }
    public void createCode(char [] mazeCode, Grid grid, int ndimens){

        int sy = grid.getStartCell().getxCoord() + 1;
        int gy = grid.getGoalCell().getxCoord() + 1;
        int counter = 0;
        for (int i = 0; i < ndimens; i++){
            for (int o = 0; o <= ndimens+1; o++){
                if (o < ndimens){
                    if (i%2 == 0){
                        if (o%2 == 0){
                            mazeCode[counter] = ' ';
                        }
                        else{
                            if (i == 0 || i == ndimens-1){
                                mazeCode[counter] = '-';
                            }
                            else {
                                if (grid.cells[i-1][o-1].checkWall()){
                                    mazeCode[counter] = '-';
                                }
                                else {
                                    mazeCode[counter] = ' ';
                                }
                            }
                        }
                    }
                    else {
                        if (o%2 == 0){
                            if (o == 0 || o == ndimens-1){
                                if (i != sy && i != gy){
                                    mazeCode[counter] = '|';
                                }
                                else {
                                    if (o == 0 && i == sy) {
                                        mazeCode[counter] = ' ';
                                    }
                                    else if (o == ndimens-1 && i == gy) {
                                        mazeCode[counter] = ' ';
                                    }
                                }
                            }
                            else {
                                if (grid.cells[i-1][o-1].checkWall()){
                                    mazeCode[counter] = '|';
                                }
                                else {
                                    mazeCode[counter] = ' ';
                                }
                            }
                        }
                        else {
                            mazeCode[counter] = ' ';
                        }
                    }
                }
                else {
                    if (o == ndimens)
                    {
                        mazeCode[counter] = '\r';
                    }
                    else {
                        mazeCode[counter] = '\n';
                    }
                }
                counter++;
            }
        }
    }
    public void writeToTextFile(char[] code){
        File path = Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DOCUMENTS);
        File file = findFileName(path);

        byte mazeBytes[] = new byte[code.length];
        for (int i = 0; i < code.length; i++){
            mazeBytes[i] = (byte)code[i];
        }

        try {
            path.mkdirs();
            OutputStream os = new FileOutputStream(file);
            os.write(mazeBytes);
            os.close();
            writeID.setText("Wrote File '"+ file.getName()+"' Successfully!");
        }
        catch (IOException e) {
            // Unable to create file, likely because external storage is
            // not currently mounted.
            writeID.setText("Error Writing File");
        }
    }
    public File findFileName(File path){
        boolean foundName = false;
        int countName = 0;
        File file;

        do {
            file = new File(path, "theMaze" + countName + ".txt");
            if (file.exists()) {
                countName++;
            } else {
                foundName = true;
            }
        } while (!foundName);

        return file;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
