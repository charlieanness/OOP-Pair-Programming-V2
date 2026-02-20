package cityrescue;
import cityrescue.enums.*;
import cityrescue.exceptions.*;

public abstract class Unit {

    protected UnitType unitType;
    protected UnitStatus unitStatus;
    protected int unitID;
    protected int x;
    protected int y;

    protected abstract boolean canHandle(IncidentType type);

    protected int getID()
    {
        return this.unitID;
    }
    
}
