package cityrescue;

import cityrescue.*;
import cityrescue.enums.*;
import cityrescue.exceptions.*;

public class temporaryTesting {

    public static void main(String[] args) throws Exception
    {
        CityRescue cr;
        cr = new CityRescueImpl();
        cr.initialise(5, 5);

        int s = cr.addStation("A", 0, 0);
        int u = cr.addUnit(s, UnitType.AMBULANCE);

        int i = cr.reportIncident(IncidentType.MEDICAL, 1, 0, 1);
        cr.dispatch();

        cr.tick(); // should arrive at (0,1) in one tick
        System.out.println(cr.viewUnit(u));

        cr.tick();
        cr.tick();
        System.out.println(cr.viewIncident(i)); //should say status resolved
        System.out.println(cr.viewUnit(u)); //should say status idle
    }
}
