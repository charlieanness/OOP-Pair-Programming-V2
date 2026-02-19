package cityrescue;

public class Station {

    private String name;
    private int capacity;
    private int stationID;
    private Unit[] units;
    private int x;
    private int y;

    public Station(String name, int ID, int x, int y)
    {
        this.x = x;
        this.y = y;
        this.name = name;
        this.stationID = ID;
        this.units = new Unit[CityRescueImpl.MAX_UNITS];
    }

    public void setCapacity(int capacity)
    {
        this.capacity = capacity;
    }

    public Unit[] getUnits()
    {
        return this.units;
    }

    public int getID()
    {
        return this.stationID;
    }

}
