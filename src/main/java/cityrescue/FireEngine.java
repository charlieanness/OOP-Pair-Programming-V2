package cityrescue;

import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.IncidentType;

public class FireEngine extends Unit{

    public FireEngine(int ID, int x, int y)
    {
        this.unitType = UnitType.FIRE_ENGINE;
        this.unitStatus = UnitStatus.IDLE;
        this.unitID = ID;
        this.x = x;
        this.y = y;
        //TO DO: FINISH ALL UNITS, DECIDE ON HOW IDS WORK
    }

    @Override
    public boolean canHandle(IncidentType type)
    {
        return (type == IncidentType.FIRE);
    }

}
