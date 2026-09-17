package com.pedru.NEA;

public class Map {

    private int[][][] mapArray;

    public void loadMaps(){
        this.mapArray = MapLoader.loadAllMaps("maps.bin");
    }

    public Map(){
        loadMaps();
        if(mapArray == null){
            throw new IllegalStateException("Maps not loaded");
        }

    }

    public int[][] getMap(int pointer) {
        return mapArray[pointer];
    }
}
