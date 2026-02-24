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

    protected abstract boolean canHandle(IncidentType type);

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

    protected int getX()
    {
        return x;
    }

    protected int getY()
    {
        return y;
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
            //if its type does not match the incident, then ignore it
            if (unit.getUnitType() == UnitType.AMBULANCE && incident.getIncidentType() != IncidentType.MEDICAL) {continue;}
            if (unit.getUnitType() == UnitType.FIRE_ENGINE && incident.getIncidentType() != IncidentType.FIRE) {continue;}
            if (unit.getUnitType() == UnitType.POLICE_CAR && incident.getIncidentType() != IncidentType.CRIME) {continue;}

            //if it is out of service, ignore it
            if (unit.getUnitStatus() == UnitStatus.OUT_OF_SERVICE) {continue;}

            //if it is not already assigned an incident, ignore it
            if (unit.getCurrentIncidentID() != 999) {continue;}

            //it must be eligible
            eligibleUnits[pos] = unit;
            pos++;
        }
        return eligibleUnits; //can have nulls, usually will
    }

    public static Unit getBestUnit(Unit[] units, int unitCount, int[] sortedIDs, Incident incident) throws IDNotRecognisedException
    {
        Unit[] eligibleUnits = Unit.getEligibleUnits(units, unitCount, sortedIDs, incident);
        Unit bestUnit;
        int minDist = 10000;

        for (Unit unit : eligibleUnits)
        {
            if (unit != null)
            {
                int dist = CityMap.calculateManhattanDistance(unit.getX(), unit.getY(), incident.getX(), incident.getY());
            }
        }
        return null; //temporary
    }
    
}
