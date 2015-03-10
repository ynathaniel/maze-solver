/*************************************************************
 * Name:  Yoav Nathaniel                                    *
 * Project:  Project 2 - Maze Solver                        *
 * Class:  CMPS 331 - Artificial Intelligence               *
 * Date:  March 10, 2015                                    *
 *************************************************************/


package com.example.yoav.mazesolverp2;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener{

    Context context;
    TextView dataFoundText, complexityText;
    FileManager fileManager;
    int selectedFile = 0;
    int selectedSearch = 0;
    Spinner Filespinner;
    Spinner Searchspinner;
    boolean goodTOgo = false;
    String searchTypes[] = {"", "Depth First Search", "Breadth First Search", "Best First Search",
            "A* Search"};

    //This onCreate method connects to all of the needed views, fills in the spinners, and
    //initializes the FileManager object.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataFoundText = (TextView)findViewById(R.id.dataFoundTextView);
        complexityText = (TextView)findViewById(R.id.ComplexityTextView);
        LinearLayout layout = (LinearLayout)findViewById(R.id.theMazeLayout);
        Searchspinner = (Spinner) findViewById(R.id.search_spinner);
        Filespinner = (Spinner) findViewById(R.id.file_spinner);
        context = this;

        fileManager = new FileManager(context, layout);
        int fileCount = fileManager.getTextFiles();
        if (fileCount > 0) {
            goodTOgo = true;
            ArrayAdapter<String> Fileadapter = new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_spinner_item, fileManager.fileNames);
            Fileadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Filespinner.setAdapter(Fileadapter);
            Filespinner.setOnItemSelectedListener(MainActivity.this);

            ArrayAdapter<String> Searchadapter = new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_spinner_item, searchTypes);
            Searchadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Searchspinner.setAdapter(Searchadapter);
            Searchspinner.setOnItemSelectedListener(MainActivity.this);

            fileManager.getFileInfo(selectedFile, dataFoundText, complexityText, false);
        }
        else {
            dataFoundText.setText("There are no .txt files in Documents containing 'theMaze' in " +
                    "their name.");
        }


    }

    //Takes the information received by spinner selections and calls the appropriate action.
    //@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (goodTOgo){
            if (parent.getId() == R.id.file_spinner) {
                if (selectedFile != position) {
                    selectedFile = position;
                    fileManager.getFileInfo(selectedFile, dataFoundText, complexityText, true);
                    Searchspinner.setSelection(0);
                }
            }
            else if (parent.getId() == R.id.search_spinner) {
                if (selectedSearch != position) {
                    selectedSearch = position;
                    fileManager.search(selectedSearch);
                }
            }
        }
    }

    //@Override
    public void onNothingSelected(AdapterView<?> parent) {

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
