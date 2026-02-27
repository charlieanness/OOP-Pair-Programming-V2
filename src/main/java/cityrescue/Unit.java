package cityrescue;
import cityrescue.enums.*;
import cityrescue.exceptions.*;

public abstract class Unit {

    protected UnitType unitType;
    protected UnitStatus unitStatus;
    protected int unitID;
    protected int x;
    protected int y;
    protected int ownerStationID;
    protected int currentIncidentID;
    protected int currentIncidentWork;

    protected abstract boolean canHandle(IncidentType type); //i should use this in getEligibleUnits()

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

    protected boolean hasArrived(Incident incident)
    {
        return ((x == incident.getX()) && (y == incident.getY()));
    }

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
            " WORK ="+currentIncidentWork
        );
    }

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

    protected void moveCoordsToStation(Station newStation)
    {
        x = newStation.getX();
        y = newStation.getY();
    }

    protected boolean isEnRoute()
    {
        return (unitStatus == UnitStatus.EN_ROUTE);
    }

    protected boolean isBusy()
    {
        return !(unitStatus == UnitStatus.EN_ROUTE || unitStatus == UnitStatus.AT_SCENE);
    }

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

    public static Unit[] getEligibleUnits(Unit[] units, int unitCount, int[] sortedIDs, Incident incident) throws IDNotRecognisedException
    {
        Unit[] eligibleUnits = new Unit[unitCount];
        int pos = 0;

        Unit[] sortedUnits = getSortedUnits(units, unitCount, sortedIDs);
        for (Unit unit : sortedUnits)
        {
            //replaced by canHandle() check below
            // if (unit.getUnitType() == UnitType.AMBULANCE && incident.getIncidentType() != IncidentType.MEDICAL) {continue;}
            // if (unit.getUnitType() == UnitType.FIRE_ENGINE && incident.getIncidentType() != IncidentType.FIRE) {continue;}
            // if (unit.getUnitType() == UnitType.POLICE_CAR && incident.getIncidentType() != IncidentType.CRIME) {continue;}

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
        return eligibleUnits; //can have nulls, usually will
    }

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

    public static Unit applyTieBreaker(Unit unit1, Unit unit2, Incident incident)
    {
        int unit1Dist = CityMap.calculateManhattanDistance(unit1.getX(), unit1.getY(), incident.getX(), incident.getY());
        int unit2Dist = CityMap.calculateManhattanDistance(unit2.getX(), unit2.getY(), incident.getX(), incident.getY());

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
