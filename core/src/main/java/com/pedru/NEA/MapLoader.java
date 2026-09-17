package com.pedru.NEA;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.DataInputStream;

public class MapLoader {
    private MapLoader(){}
    public static int[][][] loadAllMaps(String internalPath){
        FileHandle fh = Gdx.files.internal(internalPath);

        try(DataInputStream in = new DataInputStream(fh.read())){
            int mapCount = in.readUnsignedShort();
            int[][][] maps = new int[mapCount][][];

            for(int i = 0; i<mapCount; i++){
                int w = in.readUnsignedShort();
                int h = in.readUnsignedShort();

                int[][] grid = new int[h][w];

                for(int y = 0; y<h; y++ ){
                    for(int x = 0; x <w; x++){
                        grid[y][x] =  in.readUnsignedByte();
                    }
                }
                maps[i] = grid;
            }
            return maps;
        }catch(Exception e){
            throw new RuntimeException("Map Loading failed");
        }
    }
}
