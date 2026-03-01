package cityrescue;

/**
 * The CityMap class contains all information about
 * the state of the map, such as blocked cells.
 * Contains various static and non-static functions
 * relating to movement of units.
 */
public class CityMap {

    private boolean[][] blocked; //2D boolean array to track blocked positions
    private int width;
    private int height;

    //public constructor with width and height
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

    //assigns a blockage (true) to a cell
    public void addObstacle(int x, int y)
    {
        this.blocked[y][x] = true;
    }

    //removes blockage (false) to a cell
    public void removeObstacle(int x, int y)
    {
        this.blocked[y][x] = false;
    }

    //checks if a cell is blocked
    public boolean isBlocked(int x, int y)
    {
        return blocked[x][y];
    }

    //turns all cells to false, clearing the map of obstacles
    public void clearObstacles() {
        for (int i=0; i<blocked.length; i++) {
            for (int j=0; j<blocked[i].length; j++) {
                blocked[i][j] = false;
            }
        }
    }

    //returns number of current obstacles on map
    public int countObstacles()
    {
        int count = 0;

        for (int i=0; i<blocked.length; i++) {
            for (int j=0; j<blocked[i].length; j++) {
                if (blocked[i][j])
                {
                    count++;
                }
            }
        }

        return count;
    }

    //useful function to check if an array only contains null
    public static boolean isAllNull(Object[] arr)
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

    /*
    Calculates and returns manhattan
    distance between two points (x1,y1) 
    and (x2,y2)
    */
    public static int calculateManhattanDistance(int x1, int y1, int x2, int y2)
    {
        return (Math.abs(x1-x2)+Math.abs(y1-y2));
    }

    /*
    Moves a unit based on the specification's movement rule.
    Moves unit to reduce distance to 
    incident, else moves in valid direction,
    else does not move.
    */
    public void applyMovementRule(Unit unit, Incident incident)
    {
        if (directionIsValid(unit, 0, 1) && reducesDist(unit, incident, 0, 1)) //testing north - across 0, up +1
        {
            unit.move(0, 1); //move north
        }
        else if (directionIsValid(unit, 1, 0) && reducesDist(unit, incident, 1, 0)) //testing east - across +1, up 0
        {
            unit.move(1, 0); //move east
        }
        else if (directionIsValid(unit, 0, -1) && reducesDist(unit, incident, 0, -1)) //testing south - across 0, up -1
        {
            unit.move(0,-1); //move south
        }
        else if (directionIsValid(unit, -1, 0) && reducesDist(unit, incident, -1, 0)) //testing west - across -1, up 0
        {
            unit.move(-1,0); //move west
        }
        else
        {
            //if none reduce distance, move first direction that is valid
            if (directionIsValid(unit, 0, 1)) {unit.move(0,1);}
            else if (directionIsValid(unit, 1, 0)) {unit.move(1,0);}
            else if (directionIsValid(unit, 0, -1)) {unit.move(0,-1);}
            else if (directionIsValid(unit, -1, 0)) {unit.move(-1,0);}
            else {return;} //no direction is valid, so no change is made
        }

    }

    /*
    Adjusts the coordinate of a unit by a specified amount,
    checking whether the new position is valid (not out of
    range and not blocked).
    */
    public boolean directionIsValid(Unit unit, int xAdj, int yAdj)
    {
        int newX = unit.getX() + xAdj;
        int newY = unit.getY() + yAdj;

        if (newX < 0 || newX > 5) {return false;}
        if (newY < 0 || newY > 5) {return false;}
        if (blocked[newX][newY]) {return false;}

        return true;
    }

    /*
    Checks whether distance would be reduced by
    making a specified adjustment in position
    */
    public boolean reducesDist(Unit unit, Incident incident, int xAdj, int yAdj)
    {
        int currentDist = calculateManhattanDistance(unit.getX(), unit.getY(), incident.getX(), incident.getY());
        int adjustedDist = calculateManhattanDistance(unit.getX()+xAdj, unit.getY()+yAdj, incident.getX(), incident.getY());

        if (adjustedDist < currentDist) {return true;}
        else {return false;}
    }

    //returns formatted string of current object counts, for use in getStatus()
    public String countsToString(int stationCount, int unitCount, int incidentCount)
    {
        return
        (
            "STATIONS="+stationCount+
            " UNITS="+unitCount+
            " INCIDENTS="+incidentCount+
            " OBSTACLES="+countObstacles()
        );
    }
    
}

