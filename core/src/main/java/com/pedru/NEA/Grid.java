package com.pedru.NEA;


import static com.pedru.NEA.GameScreen.NonTraversableColorTexture;
import static com.pedru.NEA.GameScreen.TraversableColorTexture;

public class Grid {
    int squareSize;
    Square[][] gameGrid;
    int amountXSquares;
    int amountYSquares;
    Map Map;
    int[][] map;
    public Grid(int squareSize, int amountXSquares, int amountYSquares, Map map){
        this.squareSize = squareSize;
        this.amountXSquares = 100;
        this.amountYSquares = 100;
        this.Map = map;
        this.map = Map.getMap(10);
    }
    public void createGrid(){
        Square[][] grid = new Square[amountXSquares][amountYSquares];
        for (int i=0;i<amountXSquares; i++){
            for(int j=0; j<amountYSquares; j++){
                if(map[i][j]==0){
                    grid[i][j] = new Square(i,j,squareSize,squareSize,1, NonTraversableColorTexture);
                }else{
                    grid[i][j] = new Square(i,j,squareSize,squareSize,2, TraversableColorTexture);
                }
            }
        }
        this.gameGrid = grid;
    }
    public void changeGridMap(){
        for (int i=0;i<amountXSquares; i++){
            for(int j=0; j<amountYSquares; j++){
                if(map[i][j]==0){
                    this.gameGrid[i][j].setTexture(NonTraversableColorTexture);
                    this.gameGrid[i][j].setColor(1);
                }else{
                    this.gameGrid[i][j].setTexture(TraversableColorTexture);
                    this.gameGrid[i][j].setColor(2);
                }
            }
        }
    }

    public void setMap(int mapPointer) {
        this.map = Map.getMap(mapPointer);
        changeGridMap();
    }
}
