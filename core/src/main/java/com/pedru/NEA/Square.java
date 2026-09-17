package com.pedru.NEA;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;

import java.util.ArrayList;

import static com.pedru.NEA.Main.deviceHeight;
import static com.pedru.NEA.Main.deviceWidth;
import static java.lang.Math.sqrt;

public class Square {

    private int x;
    private int y;

    private ArrayList<Square> adjacentSquares;
    private int[][] adjacentSquaresPositions;

    private int width;
    private int height;

    private float w;
    private float h;

    private float IsoCenterX;
    private float IsoCenterY;

    private float[] PolyCoordsY;
    private float[] PolyCoordsX;
    private float[] PolyVertices;

    private Square parent = null;

    private short[] indices;

    private int f = 0;
    private int g = 0;
    private int heuristic = 0;

    private boolean traversable = true;

    PolygonSprite poly;

    private Texture colorTexture;

    Color color;
    Color black = Color.BLACK;
    Color blue = Color.BLUE;

    public Square(int xIndex, int yIndex, int width, int height, int color, Texture colorTexture) {

        this.adjacentSquaresPositions = new int[][]{
            {0, 1}, {1, 1}, {1, 0}, {1, -1},
            {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}
        };

        this.width = width;
        this.height = height;

        if (color == 1) {
            traversable = false;
        }

        this.x = xIndex;
        this.y = yIndex;

        // 0,0 bottom left
        this.w = (float) (width * 0.5);
        this.h = (float) (height * 0.25);

        this.IsoCenterX = (float) (((x - y) * w) + (0.5 * (deviceWidth) - w));
        this.IsoCenterY = (float) (((x + y) * h) + (0.125 * deviceHeight - 8 * h));

        this.PolyCoordsX = new float[]{
            // x1
            IsoCenterX,
            // x2
            IsoCenterX + w,
            // x3
            IsoCenterX,
            // x4
            IsoCenterX - w
        };

        this.PolyCoordsY = new float[]{
            // y1
            IsoCenterY + h,
            // y2
            IsoCenterY,
            // y3
            IsoCenterY - h,
            // y4
            IsoCenterY
        };

        int n = PolyCoordsX.length;

        this.PolyVertices = new float[n * 2];

        for (int i = 0, j = 0; i < n; i++) {
            this.PolyVertices[j++] = PolyCoordsX[i];
            this.PolyVertices[j++] = PolyCoordsY[i];
        }

        this.colorTexture = colorTexture;

        EarClippingTriangulator triangulator = new EarClippingTriangulator();
        this.indices = triangulator.computeTriangles(PolyVertices).toArray();

        PolygonRegion region =
            new PolygonRegion(new TextureRegion(colorTexture), PolyVertices, indices);

        this.poly = new PolygonSprite(region);
    }

    public ArrayList<Square> getAdjacentSquares(Grid grid) {

        ArrayList<Square> adjacentSquares = new ArrayList<Square>();

        for (int[] position : adjacentSquaresPositions) {

            int nx = this.x + position[0];
            int ny = this.y + position[1];

            if (nx >= 0 && ny >= 0 && nx <= 99 && ny <= 99) {

                if (grid.gameGrid[nx][ny].traversable) {
                    adjacentSquares.add(grid.gameGrid[nx][ny]);
                }
            }
        }

        return adjacentSquares;
    }

    public void drawIsoTop(ShapeRenderer sr) {

        sr.setColor(Color.WHITE);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.polygon(this.PolyVertices);
        sr.end();
    }

    public void drawSquare(ShapeRenderer sr) {

        /*
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color);
        sr.rect((x * width), (y * height), width, height);
        sr.end();
        */
    }

    public void drawTop(PolygonSpriteBatch polyBatch) {
        this.poly.draw(polyBatch);
    }

    public void drawPath(ShapeRenderer sr) {

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.GREEN);
        sr.rect((x * width), (y * height), width, height);
        sr.end();
    }

    public void drawOutline(ShapeRenderer sr) {

        /*
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);
        sr.rect((x * width), (y * height), width, height);
        sr.end();
        */
    }

    public float getIsoCenterX() {
        return this.IsoCenterX;
    }

    public float getIsoCenterY() {
        return this.IsoCenterY;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Square getParent() {
        return parent;
    }

    public void setParent(Square parent) {
        this.parent = parent;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(int h) {
        this.heuristic = h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public boolean isTraversable() {
        return traversable;
    }

    public float getw() {
        return w;
    }

    public float geth() {
        return h;
    }

    public void setTexture(Texture colorTexture) {

        PolygonRegion region =
            new PolygonRegion(new TextureRegion(colorTexture), PolyVertices, indices);

        this.poly = new PolygonSprite(region);
    }

    public void setColor(int color) {

        if (color == 1) {
            traversable = false;
        } else {
            traversable = true;
        }
    }
}
