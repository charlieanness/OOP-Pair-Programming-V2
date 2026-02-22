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

    protected static Unit getUnitFromID(Unit[] units, int unitID) throws IDNotRecognisedException
    {
        for (int i=0; i<units.length; i++)
        {
            if (units[i].getID() == unitID)
            {
                return units[i];
            }
        }
        throw new IDNotRecognisedException("No unit with that ID exists!");
    }

    protected boolean isBusy()
    {
        return !(unitStatus == UnitStatus.EN_ROUTE || unitStatus == UnitStatus.AT_SCENE);
    }
    
}
