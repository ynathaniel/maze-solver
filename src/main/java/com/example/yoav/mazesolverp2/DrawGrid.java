/*************************************************************
 * Name:  Yoav Nathaniel                                    *
 * Project:  Project 2 - Maze Solver                        *
 * Class:  CMPS 331 - Artificial Intelligence               *
 * Date:  March 10, 2015                                    *
 *************************************************************/


package com.example.yoav.mazesolverp2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Vector;


public class DrawGrid {

    private ImageView image;
    private int imageID;
    private Canvas canvas;
    private Bitmap bitmap;
    private int dimens;
    private Context context;
    private LinearLayout layout;
    private BuildGrid buildGrid;

    /*
    * Consturctor method of DrawGrid class. It takes a LinearLayout,
    * the dimensions of the maze (6x6 means 11), the context, and the maze (BuildGrid object).
    * It uses a bitmap and a canvas to draw the maze. It calls drawImage and drawMaze methods.
     */
    public DrawGrid(LinearLayout l, int d, Context c, BuildGrid g) {
        buildGrid = g;
        layout = l;
        dimens = d;
        context = c;

        bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        drawImage();
        drawMaze();
        layout.addView(image);
    }
    /*
    * 're-Consturctor' method of DrawGrid class. it takes the maze (BuildGrid object),
    * and the dimensions of the maze (6x6 means 11).
    * It uses a bitmap and a canvas to draw the maze. It calls bitmapSurround and
    * drawMaze methods.
     */
    public void reDraw(BuildGrid g, int d) {
        dimens = d;
        buildGrid = g;
        canvas.drawColor(Color.WHITE);
        bitmapSurround();
        drawMaze();
        layout.removeView(image);
        layout.addView(image);
    }
    /*
    * Goes through the entire array of cells in BuildGrid object. Draws each cells,
    * room and wall.
    * Calls drawMJ method.
     */
    private void drawMaze(){
        int actualGrid = (dimens + 1) / 2;
        int sizeUse = 1000 / actualGrid;

        drawMJ();

        for (int i = 0; i <  dimens; i++){
            for (int o = 0; o < dimens; o++){
                if (i != 0) {
                    if (o%2 == 0) {
                        if (buildGrid.cells[i - 1][o].checkWall()) {
                            DrawCell drawCell = new DrawCell(context, (i / 2) * sizeUse,
                                    (o / 2) * sizeUse, 10, sizeUse);
                            drawCell.onDraw(canvas);
                        }
                    }
                }
                if (i != dimens - 1) {
                    if (o%2 == 0) {
                        if (buildGrid.cells[i + 1][o].checkWall()) {
                            DrawCell drawCell = new DrawCell(context, ((i + 2) / 2) * sizeUse,
                                    (o / 2) * sizeUse, 10,
                                    sizeUse);
                            drawCell.onDraw(canvas);
                        }
                    }
                }

                if (o != 0) {
                    if (i%2 == 0) {
                        if (buildGrid.cells[i][o - 1].checkWall()) {
                            DrawCell drawCell = new DrawCell(context, (i / 2) * sizeUse,
                                    (o / 2) * sizeUse, sizeUse, 10);
                            drawCell.onDraw(canvas);
                        }
                    }
                }

                if (o != dimens - 1) {
                    if (i%2 == 0) {
                        if (buildGrid.cells[i][o + 1].checkWall()) {
                            DrawCell drawCell = new DrawCell(context, (i / 2) * sizeUse,
                                    ((o + 2) / 2) * sizeUse,
                                    sizeUse, 10);
                            drawCell.onDraw(canvas);
                        }
                    }
                }
            }
        }
    }
    /*
    * This method marks the start and goal cells of the BuildGrid object.
    * Green for startCell and red for goalCell.
     */
    private void drawMJ(){
        int sx = buildGrid.getStartCell().getxCoord();
        int sy= buildGrid.getStartCell().getyCoord();
        int ex = buildGrid.getGoalCell().getxCoord();
        int ey= buildGrid.getGoalCell().getyCoord();
        int actualGrid = (dimens + 1) / 2;
        int sizeUse = 1000 / actualGrid;
        DrawCell drawStartCell = new DrawCell(context, ((sx / 2) * sizeUse)+ (sizeUse/4),
                ((sy / 2) * sizeUse)+(sizeUse/4), sizeUse/2, sizeUse/2);
        drawStartCell.onDraw(canvas);
        DrawCell drawGoalCell = new DrawCell(context, ((ex / 2) * sizeUse)+(sizeUse/4),
                ((ey / 2) * sizeUse)+(sizeUse/4), sizeUse/2, sizeUse/2);
        Paint red = new Paint();
        red.setColor(Color.RED);
        drawGoalCell.changeColor(red);
        drawGoalCell.onDraw(canvas);
    }
    /*
    * drawImage creates and connects the ImageView that's seen on the screen. It calls
    * bitmapSurround method.
     */
    private void drawImage() {
        image = new ImageView(context);
        imageID = image.generateViewId();
        image.setId(imageID);
        canvas.drawColor(Color.WHITE);
        image.setImageBitmap(bitmap);
        image.setPadding(200, 0, 20, 20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        image.setLayoutParams(params);
        bitmapSurround();

    }
    /*
    * draws a black line on the edges of the canvas to show where it ends.
     */
    private void bitmapSurround() {
        Paint black = new Paint();
        black.setColor(Color.BLACK);
        canvas.drawRect(0, 0, 1000, 5, black);
        canvas.drawRect(0, 0, 5, 1000, black);
        canvas.drawRect(0, 995, 1000, 1000, black);
        canvas.drawRect(995, 0, 1000, 1000, black);

    }
    /*
    * Takes in a vector<cell> object as a path that solves the maze.
    * DrawSearch reads the vector, for each item, it print prints the position of the item on the
    * cell found in the canvas.
     */
    public void drawSearch(Vector<Cell> path){
        int actualGrid = (dimens + 1) / 2;
        int sizeUse = 1000 / actualGrid;
        Paint b = new Paint();
        b.setColor(Color.BLACK);
        switch (dimens){
            case 11:
                b.setTextSize(75);
                break;
            case 13:
                b.setTextSize(65);
                break;
            case 15:
                b.setTextSize(57);
                break;
            case 17:
                b.setTextSize(50);
                break;
            case 19:
                b.setTextSize(46);
                break;
        }
        Cell current;
        for (int i = 0; i < path.size(); i++){
            current = path.get(i);
            int x = current.getxCoord();
            int y = current.getyCoord();
            canvas.drawText(i+1+"", ((x / 2) * sizeUse)+ (sizeUse/4),((y / 2) * sizeUse)+
                    ((sizeUse*2)/3), b);
        }
        layout.removeView(image);
        layout.addView(image);
    }
}
