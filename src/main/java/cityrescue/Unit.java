package cityrescue;
import cityrescue.enums.*;
import cityrescue.exceptions.*;

/**
 * The Unit class is an abstract class
 * which defines protected attributes and methods,
 * as well as abstract methods for the PoliceCar, 
 * Ambulance and FireEngine classes to inherit from.
 * Contains static functions that relate to Unit(s).
 */
public abstract class Unit {

    protected UnitType unitType; //type of unit
    protected UnitStatus unitStatus; //current status
    protected int unitID; //unique ID
    protected int x;
    protected int y;
    protected int ownerStationID; //ID of owner station
    protected int currentIncidentID; //CRUCIAL: this is assigned 999 when not attached to an incident
    protected int currentIncidentWork; //amount of work completed on an incident

    /**
     * Abstract method to check if a unit can resolve a specific incident by its type.
     * @param type
     * @return a boolean indicating whether this unit is matched to the incident type
     */
    protected abstract boolean canHandle(IncidentType type);

    /**
     * Abstract method to determine ticks required to do its job.
     * @return int number of ticks required
     */
    protected abstract int getTicksToResolve();

    protected int getID()
    {
        return this.unitID;
    }

    protected int getOwnerStationID()
    {
        return this.ownerStationID;
    }

    protected UnitStatus getUnitStatus()
    {
        return this.unitStatus;
    }

    protected void setUnitStatus(UnitStatus status)
    {
        unitStatus = status;
    }

    protected UnitType getUnitType()
    {
        return unitType;
    }

    protected int getCurrentIncidentID()
    {
        return currentIncidentID;
    }

    protected void setCurrentIncidentID(int id)
    {
        currentIncidentID = id;
    }

    protected int getCurrentIncidentWork()
    {
        return currentIncidentWork;
    }

    protected void doWork()
    {
        currentIncidentWork++;
    }

    protected int getX()
    {
        return x;
    }

    protected int getY()
    {
        return y;
    }

    protected void move(int x, int y)
    {
        this.x += x;
        this.y += y;
    }

    //returns a boolean to indicate whether a unit has arrived at an incident yet
    protected boolean hasArrived(Incident incident)
    {
        return ((x == incident.getX()) && (y == incident.getY()));
    }

    //returns a formatted string of information regarding the unit
    protected String viewUnitStats()
    {
        return
        (
            "U#"+unitID+
            " TYPE="+unitType+
            " HOME="+ownerStationID+
            " LOC=("+x+","+y+")"+
            " STATUS="+unitStatus+
            " INCIDENT="+currentIncidentID+
            " WORK="+currentIncidentWork
        );
    }

    //returns an existing unit specified by its ID
    protected static Unit getUnitFromID(Unit[] units, int unitID) throws IDNotRecognisedException
    {
        for (int i=0; i<units.length; i++)
        {
            if (units[i] != null)
            {
                if (units[i].getID() == unitID)
                {
                    return units[i];
                }
            }
        }
        throw new IDNotRecognisedException("No unit with that ID exists!");
    }

    //changes unit coords based on a station
    protected void moveCoordsToStation(Station newStation)
    {
        x = newStation.getX();
        y = newStation.getY();
    }

    protected boolean isEnRoute()
    {
        return (unitStatus == UnitStatus.EN_ROUTE);
    }

    //checks if unit is already occupied with an incident
    protected boolean isBusy()
    {
        return !(unitStatus == UnitStatus.EN_ROUTE || unitStatus == UnitStatus.AT_SCENE);
    }

    /*
    Uses a list of sorted unit IDs to
    construct an array of current units
    ordered by ID (ascending).
    */
    public static Unit[] getSortedUnits(Unit[] units, int unitCount, int[] sortedIDs) throws IDNotRecognisedException
    {
        Unit[] sortedUnits = new Unit[unitCount];
        int pos = 0;
        
        for (int i=0; i<sortedIDs.length; i++)
        {
            Unit unit = Unit.getUnitFromID(units, sortedIDs[i]);
            sortedUnits[pos] = unit;
            pos++;
        }

        return sortedUnits;
    }

    /*
    Sorts units into ascending ID order,
    constructing an array of the units that
    determine it eligible for a specific incident.
    Returns this array to getBestUnit().
    */
    public static Unit[] getEligibleUnits(Unit[] units, int unitCount, int[] sortedIDs, Incident incident) throws IDNotRecognisedException
    {
        Unit[] eligibleUnits = new Unit[unitCount];
        int pos = 0;

        Unit[] sortedUnits = getSortedUnits(units, unitCount, sortedIDs);
        for (Unit unit : sortedUnits)
        {
            //if its type does not match the incident, then ignore it
            if (unit.canHandle(incident.getIncidentType()) == false) {continue;}

            //if it is out of service, ignore it
            if (unit.getUnitStatus() == UnitStatus.OUT_OF_SERVICE) {continue;}

            //if it is already assigned an incident, ignore it
            if (unit.getCurrentIncidentID() != 999) {continue;}

            //it must be eligible
            eligibleUnits[pos] = unit;
            pos++;
        }
        return eligibleUnits;
    }

    /*
    Applies tiebreaker function to all eligible units 
    to return the unit best fit for a specific incident.
    */
    public static Unit getBestUnit(Unit[] units, int unitCount, int[] sortedIDs, Incident incident) throws IDNotRecognisedException
    {
        Unit[] eligibleUnits = Unit.getEligibleUnits(units, unitCount, sortedIDs, incident); //gets all eligible units
        Unit bestUnit = eligibleUnits[0]; //best unit is defaultly set to the first one

        //iterate through list, comparing best with each unit, updating bestUnit to the better one out of the comparison
        for (int i=1;i<eligibleUnits.length;i++)
        {
            if (eligibleUnits[i] != null)
            {
                //applies all three tiebreakers to return best unit out of the two
                bestUnit = applyTieBreaker(bestUnit, eligibleUnits[i], incident);
            }
        }
        return bestUnit;
    }

    /*
    Applies movement tiebreaker by comparing distances to incident,
    then comparing IDs (if distances tie), then comparing station IDs (if unit IDs tie).
    Returns prioritised unit based on these tiebreakers.
     */
    public static Unit applyTieBreaker(Unit unit1, Unit unit2, Incident incident)
    {
        //calculates distances
        int unit1Dist = CityMap.calculateManhattanDistance(unit1.getX(), unit1.getY(), incident.getX(), incident.getY());
        int unit2Dist = CityMap.calculateManhattanDistance(unit2.getX(), unit2.getY(), incident.getX(), incident.getY());

        //first compares distances
        if (unit1Dist < unit2Dist) {return unit1;}
        if (unit2Dist < unit1Dist) {return unit2;}

        //continues to here if dists are equal
        if (unit1.getID() < unit2.getID()) {return unit1;}
        if (unit2.getID() < unit1.getID()) {return unit2;}

        //continues here if IDs are equal
        if (unit1.getOwnerStationID() < unit2.getOwnerStationID()) {return unit1;}
        else {return unit2;}

    }
    
}
