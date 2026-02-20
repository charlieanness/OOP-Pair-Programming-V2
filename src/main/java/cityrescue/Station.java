package cityrescue;

import cityrescue.exceptions.IDNotRecognisedException;
import cityrescue.exceptions.CapacityExceededException;

public class Station {

    private String name;
    private int capacity;
    private int stationID;
    private Unit[] units;
    private int unitCount;
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

    public int getUnitCount()
    {
        return this.unitCount;
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

    public void addUnitToStation(Unit unit)
    {
        //checks that the unitcount is not the same as the max units array size, throws error if it is
        if (this.unitCount == this.units.length) {throw new CapacityExceededException("Can't add another unit!");} 
        for (int i=0; i<this.units.length;i++)
        {
            if (this.units[i] == null) //checks for space
            {
                this.units[i] = unit;
                this.unitCount++;
                break;
            }
        }
        
    }

}
