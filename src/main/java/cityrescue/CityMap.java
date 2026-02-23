package cityrescue;

import cityrescue.exceptions.*;

public class CityMap {

    private boolean[][] blocked;
    private int width;
    private int height;

    public CityMap(int width, int height)
    {
        this.blocked = new boolean[height][width];
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
        this.blocked[y][x] = true;
    }

    public void removeObstacle(int x, int y)
    {
        this.blocked[y][x] = false;
    }

    public boolean isBlocked(int x, int y)
    {
        return blocked[x][y];
    }

    public void clearObstacles() {
        for (int i=0; i<blocked.length; i++) {
            for (int j=0; i<blocked[i].length; i++) {
                blocked[i][j] = false;
            }
        }
    }

    public static boolean checkAllNull(Object[] arr)
    {
        for (int i=0; i<arr.length;i++)
        {
            if (arr[i] != null)
            {
                return false;
            }
        }
        return true;

    }

    
}

