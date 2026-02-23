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
    
}
