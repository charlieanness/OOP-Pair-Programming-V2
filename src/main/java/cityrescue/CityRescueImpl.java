package cityrescue;

import cityrescue.enums.*;
import cityrescue.exceptions.*;
import cityrescue.Station;
import java.util.Arrays;

/**
 * CityRescueImpl (Starter)
 *
 * Your task is to implement the full specification.
 * You may add additional classes in any package(s) you like.
 */
public class CityRescueImpl implements CityRescue {

    // TODO: add fields (map, arrays for stations/units/incidents, counters, tick, etc.)
    public static final int MAX_STATIONS = 20;
    public static final int MAX_UNITS = 50;
    public static final int MAX_INCIDENTS = 200;

    public int nextStationID;
    public int nextUnitID;
    public int nextIncidentID;

    public CityMap cityMap;
    public int currentTick;
    public Station[] stations;
    public Unit[] units;
    public Incident[] incidents;
    public int stationCount;
    public int unitCount;
    public int incidentCount;

    @Override
    public void initialise(int width, int height) throws InvalidGridException {
        if (width <= 0 || height <= 0) {throw new InvalidGridException("Not a valid size!");}

        cityMap = new CityMap(width, height);

        stations = new Station[MAX_STATIONS];
        stationCount = 0;
        nextStationID = 1;

        units = new Unit[MAX_UNITS];
        unitCount = 0;
        nextUnitID = 1;

        incidents = new Incident[MAX_INCIDENTS];
        incidentCount = 0;
        nextIncidentID = 1;

        currentTick = 0;
    }

    @Override
    public int[] getGridSize() {
        return cityMap.getDimensions();
    }

    @Override
    public void addObstacle(int x, int y) throws InvalidLocationException {
        if (x > cityMap.getWidth() || x < 0) {throw new InvalidLocationException("Not a valid location!");}
        if (y > cityMap.getHeight() || y < 0) {throw new InvalidLocationException("Not a valid location!");}

        cityMap.addObstacle(x, y);
    }

    @Override
    public void removeObstacle(int x, int y) throws InvalidLocationException {
        if (x > cityMap.getWidth() || x < 0) {throw new InvalidLocationException("Not a valid location!");}
        if (y > cityMap.getHeight() || y < 0) {throw new InvalidLocationException("Not a valid location!");}

        cityMap.removeObstacle(x, y);
    }

    @Override
    public int addStation(String name, int x, int y) throws InvalidNameException, InvalidLocationException {
        if (x > cityMap.getWidth() || x < 0) {throw new InvalidLocationException("Not a valid location!");}
        if (y > cityMap.getHeight() || y < 0) {throw new InvalidLocationException("Not a valid location!");}
        if (name.isBlank() || name.isEmpty()) {throw new InvalidNameException("Not a valid name!");}
        if (stationCount == MAX_STATIONS) {throw new CapacityExceededException("Can't add another station!");}

        Station newStation = new Station(name, nextStationID++, x, y); //create new station and increment
        for (int i=0; i<stations.length; i++)
        {
            if (stations[i] == null)
            {
                stations[i] = newStation;
                stationCount++;
                break;
            }
        }
        return newStation.getID();
    }

    @Override
    public void removeStation(int stationId) throws IDNotRecognisedException, IllegalStateException {
        for (int i=0; i<stations.length; i++)
        {
            if (stations[i].getID() == stationId)
            {
                if (stations[i].getUnitCount() == 0) //if it doesnt have any units left, then it can be removed
                {
                stations[i] = null;
                stationCount--;
                break;
                } //if it still has units, it cannot be removed
                else {throw new IllegalStateException("This station still has units!");}
            }
            if (i == MAX_STATIONS-1) {throw new IDNotRecognisedException("No station with that ID exists!");}
        }
    }

    @Override
    public void setStationCapacity(int stationId, int maxUnits) throws IDNotRecognisedException, InvalidCapacityException {
        if (maxUnits <= 0 || maxUnits < unitCount) {throw new InvalidCapacityException("Invalid Capacity");}

        Station station = Station.getStationFromID(stations, stationId); //can throw IDNotRecognisedException
        station.setCapacity(maxUnits);
    }

    @Override
    public int[] getStationIds() {
        int[] ids = new int[stationCount];
        int pos = 0;
        for (int i=0; i<stations.length; i++)
        {
            if (stations[i] != null)
            {
                ids[pos++] = stations[i].getID();
            }
        }
        Arrays.sort(ids);
        return ids;
    }

    @Override
    public int addUnit(int stationId, UnitType type) throws IDNotRecognisedException, InvalidUnitException, IllegalStateException {
        if (type == null) {throw new InvalidUnitException("Unit type is null!");}
        Station station = Station.getStationFromID(stations, stationId); //will throw IDNotRecognisedException if necessary
        if (station.isFull()) {throw new IllegalStateException("Station is full!");}
        if (unitCount == MAX_UNITS) {throw new CapacityExceededException("Can't add more units!");}

        Unit newUnit;
        switch (type) {
        case AMBULANCE:
            newUnit = new Ambulance(nextUnitID++, station.getX(), station.getY());
            break;
        case POLICE_CAR:
            newUnit = new PoliceCar(nextUnitID++, station.getX(), station.getY());
            break;
        case FIRE_ENGINE:
            newUnit = new FireEngine(nextUnitID++, station.getX(), station.getY());
            break;
        default:
            throw new InvalidUnitException("Unit type is not valid!");
        }
        
        station.addUnitToStation(newUnit); //add unit to station

        //adding newUnit to city units array and incrementing city unit count
        for (int i=0; i<units.length;i++)
        {
            if (units[i] == null)
            {
                units[i] = newUnit;
                unitCount++; //increment city unit count
                break;
            }
        }
        
        return newUnit.getID();
    }

    @Override
    public void decommissionUnit(int unitId) throws IDNotRecognisedException, IllegalStateException {

        Unit selectedUnit = null; //initially set to null
        int pos = 0; //indicates index of unit in city units array

        for (int i=0; i<units.length; i++) //find required unit by ID
        {
            if (units[i].getID() == unitId)
            {
                selectedUnit = units[i];
                pos = i;
                break;
            }
            if (i==MAX_UNITS-1) {throw new IDNotRecognisedException("No unit exists with that ID");}
        }
        if (selectedUnit.isBusy()) {throw new IllegalStateException("Unit is busy, cannot retire!");}

        //finds owner station, removes unit from that station, then removes unit from city array and decrements count
        Station ownerStation = Station.getStationFromID(stations, selectedUnit.getOwnerStationID());
        ownerStation.removeUnitFromStation(selectedUnit);
        units[pos] = null;
        unitCount--;
    }

    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException {
        Unit selectedUnit = Unit.getUnitFromID(units, unitId); //finds unit
        if (selectedUnit.getUnitStatus() != UnitStatus.IDLE) {throw new IllegalStateException("Unit is not IDLE!");} //checks idle
        
        Station oldStation = Station.getStationFromID(stations, selectedUnit.getOwnerStationID()); //ref to old station
        Station newStation = Station.getStationFromID(stations, newStationId); //ref to new station

        newStation.addUnitToStation(selectedUnit); //adds to new station
        oldStation.removeUnitFromStation(selectedUnit); //removes from old station

        selectedUnit.moveCoordsToStation(newStation); //changes unit coords to new stations coords
    }

    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) throws IDNotRecognisedException, IllegalStateException {
        Unit selectedUnit = Unit.getUnitFromID(units, unitId);
        if (outOfService)
            {
                if (selectedUnit.getUnitStatus() != UnitStatus.IDLE) {throw new IllegalStateException("Unit not IDLE, cannot become OUT_OF_SERVICE!");}
                else {selectedUnit.setUnitStatus(UnitStatus.OUT_OF_SERVICE);}
            }
        else {selectedUnit.setUnitStatus(UnitStatus.IDLE);}
    }

    @Override
    public int[] getUnitIds() {
        int[] ids = new int[unitCount];
        int pos = 0;
        for (Unit unit : units)
        {
            if (unit != null)
            {
                ids[pos] = unit.getID();
                pos++;
            }
        }
        Arrays.sort(ids);
        return ids;
    }

    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        Unit unit = Unit.getUnitFromID(units, unitId); //gets unit, will throw ID error appropriately
        return unit.viewUnitStats(); //prints unit info
    }

    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y) throws InvalidSeverityException, InvalidLocationException {
        if (type == null) {throw new NullPointerException("Incident type is null!");} //just used nullpointerexception
        if (severity < 1 || severity > 5) {throw new InvalidSeverityException("Invalid severity!");}
        if (cityMap.isBlocked(x, y)) {throw new InvalidLocationException("Location is blocked!");}
        if (x > cityMap.getWidth() || x < 0) {throw new InvalidLocationException("Invalid X location!");}
        if (y > cityMap.getHeight() || y < 0) {throw new InvalidLocationException("Invalid Y location!");}
        if (incidentCount == MAX_INCIDENTS) {throw new CapacityExceededException("Can't add another incident!");}

        Incident newIncident = new Incident(nextIncidentID++, type, severity, x, y);
        for (int i=0;i<incidents.length;i++)
        {
            if (incidents[i] == null)
            {
                incidents[i] = newIncident;
                incidentCount++;
                break;
            }
        }
        return newIncident.getID();
    }

    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {
        Incident selectedIncident = Incident.getIncidentFromID(incidents, incidentId);
        if (selectedIncident.canBeCancelled() == false) {throw new IllegalStateException("Incident cannot be cancelled!");}
        
        if (selectedIncident.getIncidentStatus() == IncidentStatus.DISPATCHED)
        {
            Unit assignedUnit = Unit.getUnitFromID(units, selectedIncident.getAssignedUnitID()); //recently changed, could be wrong
            assignedUnit.unitStatus = UnitStatus.IDLE;
            assignedUnit.currentIncidentID = 999;
        }
        selectedIncident.setIncidentStatus(IncidentStatus.CANCELLED);
        //dont need to remove from incidents array, just becomes cancelled
    }

    @Override
    public void escalateIncident(int incidentId, int newSeverity) throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {
        if (newSeverity < 1 || newSeverity > 5) {throw new InvalidSeverityException("Invalid severity!");}
        
        Incident selectedIncident = Incident.getIncidentFromID(incidents, incidentId);
        if (selectedIncident.canBeEscalated() == false) {throw new IllegalStateException("Incident cannot be escalated!");}

        selectedIncident.setSeverity(newSeverity);
    }

    @Override
    public int[] getIncidentIds() {
        int[] ids = new int[incidentCount];
        int pos = 0;
        for (Incident incident : incidents)
        {
            if (incident != null)
            {
                ids[pos] = incident.getID();
                pos++;
            }
        }
        Arrays.sort(ids);
        return ids;
    }

    @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        Incident incident = Incident.getIncidentFromID(incidents, incidentId);
        return incident.viewIncidentStats();
    }

    @Override
    public void dispatch() {

        try
        {
            Incident[] sortedIncidents = Incident.getSortedIncidents(incidents, incidentCount, getIncidentIds());
            Incident[] reportedIncidents = Incident.getReportedIncidents(sortedIncidents); //gets reported incidents in ID order
            for (Incident incident : reportedIncidents)
            {
                if (incident == null) {continue;} //skips loop if its null
                
                Unit bestUnit = Unit.getBestUnit(units, unitCount, getUnitIds(), incident);

                bestUnit.setCurrentIncidentID(incident.getID());
                bestUnit.setUnitStatus(UnitStatus.EN_ROUTE);

                incident.setAssignedUnitID(bestUnit.getID());
                incident.setIncidentStatus(IncidentStatus.DISPATCHED);
            }
        }
        catch (Exception e) {System.out.println("An error has occurred: " + e);}
    }

    @Override
    public void tick() {
        //increment tick
        currentTick++;
        
        try
        {
            Unit[] sortedUnits = Unit.getSortedUnits(units, unitCount, getUnitIds());
            Unit[] enRouteUnits = new Unit[sortedUnits.length];
            int pos = 0;

            //get en route units into an array
            for (Unit unit : sortedUnits)
            {
                if (unit == null) {continue;} //i think sortedunits has no nulls, so maybe not necessary
                
                if (unit.isEnRoute())
                {
                    enRouteUnits[pos] = unit;
                    pos++;
                }
            }

            //resolve completed incidents
            Incident[] sortedIncidents = Incident.getSortedIncidents(incidents, incidentCount, getIncidentIds());
            for (Incident incident : sortedIncidents)
            {
                if (incident == null) {continue;}

                Unit assignedUnit = Unit.getUnitFromID(units, incident.getAssignedUnitID());

                if (incident.getIncidentStatus() == IncidentStatus.IN_PROGRESS)
                {
                    //resolving completed incidents
                    if (assignedUnit.getCurrentIncidentWork() == assignedUnit.getTicksToResolve())
                    {
                        //unit has completed incident, resetting stats for unit and resolving incident
                        incident.setIncidentStatus(IncidentStatus.RESOLVED);
                        incident.setAssignedUnitID(999); //just to ensure it is not atttached to unit anymore
                        assignedUnit.currentIncidentWork = 0;
                        assignedUnit.currentIncidentID = 999;
                        assignedUnit.unitStatus = UnitStatus.IDLE;
                    }

                    //process on-scene work
                    if (assignedUnit.getCurrentIncidentWork() < assignedUnit.getTicksToResolve())
                    {
                        assignedUnit.doWork();
                    }
                }

                if (incident.getIncidentStatus() == IncidentStatus.DISPATCHED)
                {
                    //mark arrivals
                    if (assignedUnit.hasArrived(incident))
                    {
                        assignedUnit.setUnitStatus(UnitStatus.AT_SCENE);
                        incident.setIncidentStatus(IncidentStatus.IN_PROGRESS);
                    }
                }
            }

            //move en-route units
            for (Unit unit : enRouteUnits) 
            {
                Incident incident = Incident.getIncidentFromID(incidents, unit.getCurrentIncidentID());
                cityMap.applyMovementRule(unit, incident);
            }
        }
        catch (Exception e) {System.out.println("An error has occurred: " + e);}
    }

    @Override
    public String getStatus() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
