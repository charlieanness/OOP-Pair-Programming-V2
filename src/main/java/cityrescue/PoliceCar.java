package cityrescue;

import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.IncidentType;

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

    @Override
    public boolean canHandle(IncidentType type)
    {
        return (type == IncidentType.CRIME);
    }

    @Override
    public int getTicksToResolve()
    {
        return 3;
    }
}
