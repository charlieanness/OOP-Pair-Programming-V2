package cityrescue;

import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.IncidentType;

/**
 * The PoliceCar class is a subtype of
 * the Unit class and so inherits its attributes and methods.
 * Attributes are assigned to values unique to a police car.
 * Abstract methods make use of polymorphism through overriding.
 */
public class PoliceCar extends Unit{

    public PoliceCar(int ID, int x, int y)
    {
        this.unitType = UnitType.POLICE_CAR;
        this.unitStatus = UnitStatus.IDLE;
        this.unitID = ID;
        this.x = x;
        this.y = y;
        this.currentIncidentID = 999;
    }

    //checks if incident is crime, so this unit can resolve it
    @Override
    public boolean canHandle(IncidentType type)
    {
        return (type == IncidentType.CRIME);
    }

    //a police car will always take 3 ticks to resolve a crime
    @Override
    public int getTicksToResolve()
    {
        return 3;
    }
}
