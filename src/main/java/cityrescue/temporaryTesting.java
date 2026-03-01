package cityrescue;

import cityrescue.enums.*;

public class temporaryTesting {

    public static void main(String[] args) throws Exception
    {
        CityRescue cr;
        cr = new CityRescueImpl();
        cr.initialise(5, 5);

        int[] sz = cr.getGridSize();

        cr.addObstacle(1,1);
        cr.addStation("A", 2, 3);
        cr.addUnit(1, UnitType.AMBULANCE);
        cr.reportIncident(IncidentType.MEDICAL, 3, 3, 3);

        System.out.println(sz);
        System.out.println(cr.getStatus());
        cr.dispatch();
        cr.tick();
        System.out.println(cr.getStatus());

    }
}
