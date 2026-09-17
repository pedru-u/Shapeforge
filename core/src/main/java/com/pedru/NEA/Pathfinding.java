package com.pedru.NEA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class Pathfinding {
    Grid grid;

    public Pathfinding(Grid grid){
        this.grid = grid;
    }

    public int octileDistance(Square currentNode, Square endNode){
        int dx = abs(currentNode.getX() - endNode.getX());
        int dy = abs(currentNode.getY() - endNode.getY());

        return 10*(dx+dy) - 6*min(dx,dy);
    }

    public ArrayList<Square> aStar(Square startNode, Square endNode){
        PriorityQueue<Square> openList = new PriorityQueue<>((a,b)-> Integer.compare(a.getF(),b.getF()));
        boolean[][] inOpen = new boolean[100][100];
        boolean[][] inClosed = new boolean[100][100];
        int[][] gBest = new int[100][100];
        for(int x=0; x<100; x++) Arrays.fill(gBest[x], Integer.MAX_VALUE);
        Square currentNode;
        ArrayList<Square> path  = new ArrayList<Square>();
        startNode.setParent(null);
        startNode.setG(0);
        startNode.setHeuristic(octileDistance(startNode, endNode));
        startNode.setF(startNode.getG() + startNode.getHeuristic());
        gBest[startNode.getX()][startNode.getY()] = 0;
        openList.add(startNode);
        inOpen[startNode.getX()][startNode.getY()] = true;
        while (!openList.isEmpty()){
            currentNode = openList.poll();

            if(currentNode.getG()  != gBest[currentNode.getX()][currentNode.getY()]){
                continue;
            }


            inOpen[currentNode.getX()][currentNode.getY()] = false;
            inClosed[currentNode.getX()][currentNode.getY()] = true;

            if(currentNode.getX() == endNode.getX() && currentNode.getY() == endNode.getY()){
                while (currentNode != null){
                    path.add(0, currentNode);
                    currentNode = currentNode.getParent();
                }
                return path;
            }

            ArrayList<Square> children = currentNode.getAdjacentSquares(grid);

            for(Square child : children){
                if(inClosed[child.getX()][child.getY()]){
                    continue;
                }

                int dx = abs(child.getX() - currentNode.getX());
                int dy = abs(child.getY() - currentNode.getY());

                int step = (dx != 0 && dy != 0) ? 14:10;

                int tempG = currentNode.getG() + step;

                if(tempG < gBest[child.getX()][child.getY()]){
                    gBest[child.getX()][child.getY()] = tempG;
                    child.setParent(currentNode);
                    child.setG(tempG);
                    child.setHeuristic(octileDistance(child,endNode));
                    child.setF(child.getG() + child.getHeuristic());
                    openList.add(child);
                }
            }
        }
        return path;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }
}
