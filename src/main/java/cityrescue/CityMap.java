package cityrescue;

import cityrescue.exceptions.*;

public class CityMap {

    private int[][] map;
    private int width;
    private int height;

    public CityMap(int width, int height) throws InvalidGridException
    {
        if (width <= 0 || height <= 0) {throw new InvalidGridException("Not a valid size!");}

        this.map = new int[height][width];
        this.width = width;
        this.height = height;
    }

    public int[] getDimensions()
    {
        int[] dimensions = {width, height};
        return dimensions;
    }

    public void addObstacle(int x, int y) throws InvalidLocationException
    {
        if (x > this.width || x < 0) {throw new InvalidLocationException("Not a valid location!");}
        if (y > this.height || y < 0) {throw new InvalidLocationException("Not a valid location!");}
        this.map[y][x] = 1;
    }

    public void removeObstacle(int x, int y) throws InvalidLocationException
    {
        if (x > this.width || x < 0) {throw new InvalidLocationException("Not a valid location!");}
        if (y > this.height || y < 0) {throw new InvalidLocationException("Not a valid location!");}
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

