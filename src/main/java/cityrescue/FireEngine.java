package cityrescue;

import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;

public class FireEngine extends Unit{

    public FireEngine(int x, int y)
    {
        this.unitType = UnitType.FIRE_ENGINE;
        this.unitStatus = UnitStatus.IDLE;
        this.x = x;
        this.y = y;
        //TO DO: FINISH ALL UNITS, DECIDE ON HOW IDS WORK, CHANGE MAX ARRAY SIZES TO RECOM CONSTS
    }

}
