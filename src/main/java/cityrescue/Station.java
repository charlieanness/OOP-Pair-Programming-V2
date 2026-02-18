package cityrescue;

public class Station {

    private String name;
    private int capacity;
    private int stationID;
    private static int nextID;
    private int x;
    private int y;

    public Station(String name, int x, int y)
    {
        if (x > this.width || x < 0) {throw new InvalidLocationException("Not a valid location!");}
        if (y > this.height || y < 0) {throw new InvalidLocationException("Not a valid location!");}
        if (name.IsBlank() || name.IsEmpty()) {throw new InvalidNameException("Not a valid name!");}
        this.x = x;
        this.y = y;
        this.name = name;
        this.stationID = nextID++;
    }

    public void setCapacity(int capacity)
    {
        this.capacity = capacity;
    }

    public int getID()
    {
        return this.stationID;
    }

}
