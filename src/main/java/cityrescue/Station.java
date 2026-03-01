package cityrescue;

import cityrescue.exceptions.IDNotRecognisedException;
import cityrescue.exceptions.CapacityExceededException;

/**
 * The station class contains all
 * information regarding stations,
 * also contains a list of units that it owns.
 * Contains static functions that relate to station(s).
 */
public class Station {

    private String name;
    private int capacity; //number of units station can hold
    private int stationID; //unique ID
    private Unit[] units; //array of owned units
    private int unitCount; //number of owned units
    private int x;
    private int y;

    //public constructor with name, ID and coords arguments
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

    //returns existing station by a specified ID
    public static Station getStationFromID(Station[] stations, int stationID) throws IDNotRecognisedException
    {
        for (int i=0; i<stations.length; i++)
        {
            if (stations[i] != null)
            {
                if (stations[i].getID() == stationID)
                {
                    return stations[i];
                }
            }
        }
        throw new IDNotRecognisedException("No station with that ID exists!");
    }

    /*
    Adds a unit to a specific station,
    changing its owner station, etc.
    */
    public void addUnitToStation(Unit unit)
    {
        //checks that the unit count is not the same as the max units array size, throws error if it is
        if (this.unitCount == units.length) {throw new CapacityExceededException("Can't add another unit!");} 
        for (int i=0; i<units.length;i++)
        {
            if (this.units[i] == null) //checks for space in array
            {
                this.units[i] = unit;
                this.unitCount++;
                this.units[i].ownerStationID = this.getID(); //units owner ID is set to this stations ID
                break;
            }
        }
        
    }

    //removes a unit from a station based on the units ID
    public void removeUnitFromStation(Unit unit)
    {
        for (int i=0; i<units.length; i++)
        {
            if (units[i] == null) {continue;}

            if (units[i].getID() == unit.getID())
            {
                units[i] = null;
                unitCount--;
                break;
            }
        }
    }

}
