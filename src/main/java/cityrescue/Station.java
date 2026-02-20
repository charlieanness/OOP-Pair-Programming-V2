package cityrescue;

import cityrescue.exceptions.IDNotRecognisedException;

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
        this.capacity = capacity; //NUMBER OF UNITS THAT IT CAN HOLD (MAX), not the number it has
    }

    public Unit[] getUnits()
    {
        return this.units;
    }

    public boolean isFull()
    {
        return (this.units.length == this.capacity);
    }

    public int getID()
    {
        return this.stationID;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public static Station getStationFromID(Station[] stations, int stationID) throws IDNotRecognisedException
    {
        for (int i=0; i<stations.length; i++)
        {
            if (stations[i].getID() == stationID)
            {
                return stations[i];
            }
        }
        throw new IDNotRecognisedException("No station with that ID exists!");
    }

}
