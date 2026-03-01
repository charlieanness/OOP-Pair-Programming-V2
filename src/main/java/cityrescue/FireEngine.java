package cityrescue;

import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.IncidentType;

/**
 * The FireEngine class is a subtype of
 * the Unit class and so inherits its attributes and methods.
 * Attributes are assigned to values unique to a fire engine.
 * Abstract methods make use of polymorphism through overriding.
 */
public class FireEngine extends Unit{

    public FireEngine(int ID, int x, int y)
    {
        this.unitType = UnitType.FIRE_ENGINE;
        this.unitStatus = UnitStatus.IDLE;
        this.unitID = ID;
        this.x = x;
        this.y = y;
        this.currentIncidentID = 999;
    }

    //checks if incident is a fire, so this unit can resolve it
    @Override
    public boolean canHandle(IncidentType type)
    {
        return (type == IncidentType.FIRE);
    }

    //a fire engine will always take 4 ticks to resolve a fire
    @Override
    public int getTicksToResolve()
    {
        return 4;
    }

}
