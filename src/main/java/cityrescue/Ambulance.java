package cityrescue;

import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.IncidentType;

/**
 * The Ambulance class is a subtype of
 * the Unit class and so inherits its attributes and methods.
 * Attributes are assigned to values unique to an ambulance.
 * Abstract methods make use of polymorphism through overriding.
 */
public class Ambulance extends Unit{

    public Ambulance(int ID, int x, int y)
    {
        this.unitType = UnitType.AMBULANCE;
        this.unitStatus = UnitStatus.IDLE;
        this.unitID = ID;
        this.x = x;
        this.y = y;
        this.currentIncidentID = 999;
    }

    //checks if the incident is medical, so this unit can resolve it
    @Override
    public boolean canHandle(IncidentType type)
    {
        return (type == IncidentType.MEDICAL);
    }

    //an ambulance always takes 2 ticks to resolve a medical issue
    @Override
    public int getTicksToResolve()
    {
        return 2;
    }
}
