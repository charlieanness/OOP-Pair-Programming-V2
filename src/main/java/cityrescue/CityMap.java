package cityrescue;

import cityrescue.exceptions.InvalidGridException;
import cityrescue.exceptions.InvalidLocationException;

public class CityMap {

    private int[][] map;
    private int width;
    private int height;

    public CityMap(int width, int height) {
        if (width <= 0 || height <= 0) {throw InvalidGridException;}

        this.map = new int[height][width];
        this.width = width;
        this.height = height;
    }

    public int[] getDimensions()
    {
        int[] dimensions = {width, height};
        return dimensions;
    }

    public void addObstacle(int x, int y)
    {
        if (x > this.width || x < 0) {throw InvalidLocationException;}
        if (y > this.height || y < 0) {throw InvalidLocationException;}
        this.map[y][x] = 1;
    }

    public void removeObstacle(int x, int y)
    {
        if (x > this.width || x < 0) {throw InvalidLocationException;}
        if (y > this.height || y < 0) {throw InvalidLocationException;}
        this.map[y][x] = 0;
    }

    public void clearObstacles() {
        for (int i=0; i<map.length; i++) {
            for (int j=0; i<map[i].length; i++) {
                map[i][j] = 0;
            }
        }
    }
    
}

