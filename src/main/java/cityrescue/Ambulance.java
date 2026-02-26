package cityrescue;

import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.IncidentType;

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

    @Override
    public boolean canHandle(IncidentType type)
    {
        return (type == IncidentType.MEDICAL);
    }

    @Override
    public int getTicksToResolve(int severity)
    {
        return 2;
    }
}
