package cityrescue;

import cityrescue.enums.*;

/**
 * This class is here solely for separate individual tests that I wanted to run
 * during debugging, etc
 */
public class temporaryTesting {

    public static void main(String[] args) throws Exception
    {
        CityRescue cr;
        cr = new CityRescueImpl();
        cr.initialise(5, 5);

        int[] sz = cr.getGridSize();

        cr.addStation("A", 2, 3);
        cr.addUnit(1, UnitType.AMBULANCE);
        cr.addUnit(1, UnitType.POLICE_CAR);
        cr.reportIncident(IncidentType.MEDICAL, 3, 3, 3);

        System.out.println(sz);
        System.out.println(cr.getStatus());

        cr.dispatch();
        cr.tick();
        System.out.println(cr.getStatus());

        cr.tick();
        System.out.println(cr.getStatus());

        cr.tick();
        System.out.println(cr.getStatus());

        cr.decommissionUnit(1);
        System.out.println(cr.getStatus());

        cr.reportIncident(IncidentType.CRIME, 1, 2, 2);
        cr.dispatch();
        cr.tick();
        System.out.println(cr.getStatus());

        cr.tick();
        System.out.println(cr.getStatus());

        cr.tick();
        System.out.println(cr.getStatus());

        cr.tick();
        System.out.println(cr.getStatus());

        cr.reportIncident(IncidentType.CRIME, 1, 3, 3);

        cr.dispatch();
        cr.tick();
        System.out.println(cr.getStatus());

        cr.cancelIncident(3);

        System.out.println(cr.getStatus());

        cr.reportIncident(IncidentType.FIRE, 4, 2, 2);
        cr.tick();
        System.out.println(cr.getStatus());

        cr.escalateIncident(4, 5);
        System.out.println(cr.getStatus());

        cr.decommissionUnit(2);

        cr.removeStation(1);

        System.out.println(cr.getStatus());

        cr.addStation("B", 4, 4);

        System.out.println(cr.getStatus());

        cr.addUnit(2, UnitType.AMBULANCE);

        System.out.println(cr.getStatus());

        cr.reportIncident(IncidentType.MEDICAL, 2, 0, 1);

        cr.dispatch();

        cr.tick();
        System.out.println(cr.getStatus());

        cr.tick();
        System.out.println(cr.getStatus());

        cr.tick();
        System.out.println(cr.getStatus());

        cr.tick();
        System.out.println(cr.getStatus());

        cr.tick();
        System.out.println(cr.getStatus());

        cr.tick();
        System.out.println(cr.getStatus());

        cr.tick();
        System.out.println(cr.getStatus());

        cr.tick();
        System.out.println(cr.getStatus()); //work = 1 at incident
        



















    }
}
