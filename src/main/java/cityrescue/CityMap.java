package cityrescue;

import cityrescue.exceptions.*;

public class CityMap {

    private int[][] map;
    private int width;
    private int height;

    public CityMap(int width, int height)
    {
        this.map = new int[height][width];
        this.width = width;
        this.height = height;
    }

    public int[] getDimensions()
    {
        int[] dimensions = {width, height};
        return dimensions;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public void addObstacle(int x, int y)
    {
        this.map[y][x] = 1;
    }

    public void removeObstacle(int x, int y)
    {
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

