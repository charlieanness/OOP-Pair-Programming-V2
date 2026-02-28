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

    //define constants for array sizes and checks
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
        //check for exceptions
        if (width <= 0 || height <= 0) {throw new InvalidGridException("Not a valid size!");}

        cityMap = new CityMap(width, height);

        //initialise station variables
        stations = new Station[MAX_STATIONS];
        stationCount = 0;
        nextStationID = 1;

        //initialise unit variables
        units = new Unit[MAX_UNITS];
        unitCount = 0;
        nextUnitID = 1;

        //initialise incident variables
        incidents = new Incident[MAX_INCIDENTS];
        incidentCount = 0;
        nextIncidentID = 1;

        //initialise tick to 0
        currentTick = 0;
    }

    @Override
    public int[] getGridSize() {
        return cityMap.getDimensions();
    }

    @Override
    public void addObstacle(int x, int y) throws InvalidLocationException {
        //check exceptions
        if (x > cityMap.getWidth() || x < 0) {throw new InvalidLocationException("Not a valid location!");}
        if (y > cityMap.getHeight() || y < 0) {throw new InvalidLocationException("Not a valid location!");}

        cityMap.addObstacle(x, y);
    }

    @Override
    public void removeObstacle(int x, int y) throws InvalidLocationException {
        //check exceptions
        if (x > cityMap.getWidth() || x < 0) {throw new InvalidLocationException("Not a valid location!");}
        if (y > cityMap.getHeight() || y < 0) {throw new InvalidLocationException("Not a valid location!");}

        cityMap.removeObstacle(x, y);
    }

    @Override
    public int addStation(String name, int x, int y) throws InvalidNameException, InvalidLocationException {
        //check exceptions
        if (x > cityMap.getWidth() || x < 0) {throw new InvalidLocationException("Not a valid location!");}
        if (y > cityMap.getHeight() || y < 0) {throw new InvalidLocationException("Not a valid location!");}
        if (name.isBlank() || name.isEmpty()) {throw new InvalidNameException("Not a valid name!");}
        if (stationCount == MAX_STATIONS) {throw new CapacityExceededException("Can't add another station!");}

        //create new station, incrementing ID after
        Station newStation = new Station(name, nextStationID++, x, y);
        //iterate through stations, place new station in available place in array
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
        //iterate through stations
        for (int i=0; i<stations.length; i++)
        {
            if (stations[i].getID() == stationId) //if stationID matches the specified one
            {
                if (stations[i].getUnitCount() == 0) //if it doesnt have any units left, then it can be removed
                {
                    stations[i] = null;
                    stationCount--;
                    break;
                } 
                else {throw new IllegalStateException("This station still has units!");} //if it still has units, it cannot be removed
            }
            //check for exception if id never found
            if (i == MAX_STATIONS-1) {throw new IDNotRecognisedException("No station with that ID exists!");}
        }
    }

    @Override
    public void setStationCapacity(int stationId, int maxUnits) throws IDNotRecognisedException, InvalidCapacityException {
        //check exceptions
        if (maxUnits <= 0 || maxUnits < unitCount) {throw new InvalidCapacityException("Invalid Capacity");}

        Station station = Station.getStationFromID(stations, stationId); //can throw IDNotRecognisedException
        station.setCapacity(maxUnits);
    }

    @Override
    public int[] getStationIds() {
        //make new ids array and index tracker, pos
        int[] ids = new int[stationCount];
        int pos = 0;
        //iterate through stations, adding a stations ID to the id array when one is found
        for (int i=0; i<stations.length; i++)
        {
            if (stations[i] != null)
            {
                ids[pos++] = stations[i].getID();
            }
        }
        //sort the id array into ascending order and return
        Arrays.sort(ids);
        return ids;
    }

    @Override
    public int addUnit(int stationId, UnitType type) throws IDNotRecognisedException, InvalidUnitException, IllegalStateException {
        //checks exceptions
        if (type == null) {throw new InvalidUnitException("Unit type is null!");}
        Station station = Station.getStationFromID(stations, stationId); //will throw IDNotRecognisedException if necessary
        if (station.isFull()) {throw new IllegalStateException("Station is full!");}
        if (unitCount == MAX_UNITS) {throw new CapacityExceededException("Can't add more units!");}

        //defines empty unit variable
        Unit newUnit;
        //create unit based on specified type argument
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
            //throw IDNotRecognisedException if unit with specified ID is not found
            if (i==MAX_UNITS-1) {throw new IDNotRecognisedException("No unit exists with that ID");}
        }
        //if unit isBusy, throw exception
        if (selectedUnit.isBusy()) {throw new IllegalStateException("Unit is busy, cannot retire!");}

        //finds owner station by ID, removes unit from that station, then removes unit from city array and decrements count
        Station ownerStation = Station.getStationFromID(stations, selectedUnit.getOwnerStationID());
        ownerStation.removeUnitFromStation(selectedUnit);
        units[pos] = null;
        unitCount--;
    }

    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException {
        Unit selectedUnit = Unit.getUnitFromID(units, unitId); //finds unit
        //checks for exception if unit is not idle
        if (selectedUnit.getUnitStatus() != UnitStatus.IDLE) {throw new IllegalStateException("Unit is not IDLE!");}
        
        //finds previous and new station associated with the unit based on IDs
        Station oldStation = Station.getStationFromID(stations, selectedUnit.getOwnerStationID()); //ref to old station
        Station newStation = Station.getStationFromID(stations, newStationId); //ref to new station

        newStation.addUnitToStation(selectedUnit); //adds to new station
        oldStation.removeUnitFromStation(selectedUnit); //removes from old station

        selectedUnit.moveCoordsToStation(newStation); //changes unit coords to new stations coords
    }

    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) throws IDNotRecognisedException, IllegalStateException {
        //finds unit based on ID
        Unit selectedUnit = Unit.getUnitFromID(units, unitId);
        if (outOfService)
            {
                //throw exception if unit is not idle, otherwise set unit to out of service
                if (selectedUnit.getUnitStatus() != UnitStatus.IDLE) {throw new IllegalStateException("Unit not IDLE, cannot become OUT_OF_SERVICE!");}
                else {selectedUnit.setUnitStatus(UnitStatus.OUT_OF_SERVICE);}
            }
        else {selectedUnit.setUnitStatus(UnitStatus.IDLE);} //if outOfService arg was false, set unit to idle
    }

    @Override
    public int[] getUnitIds() {
        //make new ids array and index tracker, pos
        int[] ids = new int[unitCount];
        int pos = 0;
        //iterate through units, adding a units ID to the id array when one is found
        for (Unit unit : units)
        {
            if (unit != null)
            {
                ids[pos] = unit.getID();
                pos++;
            }
        }
        Arrays.sort(ids); //sort ids into ascending order and return
        return ids;
    }

    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        Unit unit = Unit.getUnitFromID(units, unitId); //gets unit, will throw ID error appropriately
        return unit.viewUnitStats(); //returns unit information as a string, in desired format
    }

    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y) throws InvalidSeverityException, InvalidLocationException {
        //checks for exceptions
        if (type == null) {throw new NullPointerException("Incident type is null!");} //just used nullpointerexception
        if (severity < 1 || severity > 5) {throw new InvalidSeverityException("Invalid severity!");}
        if (cityMap.isBlocked(x, y)) {throw new InvalidLocationException("Location is blocked!");}
        if (x > cityMap.getWidth() || x < 0) {throw new InvalidLocationException("Invalid X location!");}
        if (y > cityMap.getHeight() || y < 0) {throw new InvalidLocationException("Invalid Y location!");}
        if (incidentCount == MAX_INCIDENTS) {throw new CapacityExceededException("Can't add another incident!");}

        //create new incident
        Incident newIncident = new Incident(nextIncidentID++, type, severity, x, y);
        //iterate through incidents, place new incident in available place
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
        //gets incident from specified ID, throws exception if doesnt exist or it can't be cancelled
        Incident selectedIncident = Incident.getIncidentFromID(incidents, incidentId);
        if (selectedIncident.canBeCancelled() == false) {throw new IllegalStateException("Incident cannot be cancelled!");}
        
        //only dispatched incidents can be cancelled
        if (selectedIncident.getIncidentStatus() == IncidentStatus.DISPATCHED)
        {
            Unit assignedUnit = Unit.getUnitFromID(units, selectedIncident.getAssignedUnitID()); //find unit by id
            assignedUnit.unitStatus = UnitStatus.IDLE; //set unit idle
            assignedUnit.currentIncidentID = 999; //999 indicates that unit is not assigned to an incident
        }
        //dont need to remove from incidents array, just becomes cancelled, regardless of whether it is dispatched or reported
        selectedIncident.setIncidentStatus(IncidentStatus.CANCELLED);
    }

    @Override
    public void escalateIncident(int incidentId, int newSeverity) throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {
        //check exception
        if (newSeverity < 1 || newSeverity > 5) {throw new InvalidSeverityException("Invalid severity!");}
        
        //gets incident from ID - can throw IDNotRecognisedException - and checks IllegalStateException
        Incident selectedIncident = Incident.getIncidentFromID(incidents, incidentId);
        if (selectedIncident.canBeEscalated() == false) {throw new IllegalStateException("Incident cannot be escalated!");}

        //update severity
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
        Incident incident = Incident.getIncidentFromID(incidents, incidentId); //gets incident, can throw ID error
        return incident.viewIncidentStats(); //return incident info as a string in desired format
    }

    @Override
    public void dispatch() {

        try
        {
            //gets reported incidents in ID order
            Incident[] sortedIncidents = Incident.getSortedIncidents(incidents, incidentCount, getIncidentIds());
            Incident[] reportedIncidents = Incident.getReportedIncidents(sortedIncidents);

            //iterate through reported incidents
            for (Incident incident : reportedIncidents)
            {
                if (incident == null) {continue;} //skips loop if its null
                
                //gets bestUnit for particular incident
                Unit bestUnit = Unit.getBestUnit(units, unitCount, getUnitIds(), incident);

                //assigns incident to unit and sets unit en route
                bestUnit.setCurrentIncidentID(incident.getID());
                bestUnit.setUnitStatus(UnitStatus.EN_ROUTE);

                //assigns unit to incident and sets incident dispatched
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
            //gets incidents by ascending ID order
            Incident[] sortedIncidents = Incident.getSortedIncidents(incidents, incidentCount, getIncidentIds());
            for (Incident incident : sortedIncidents)
            {
                if (incident == null) {continue;} //skips loop if incident is null

                //in-progress incidents are the only incidents that need to process on-scene work or be resolved
                if (incident.getIncidentStatus() == IncidentStatus.IN_PROGRESS)
                {
                    //gets unit assigned to incident
                    Unit assignedUnit = Unit.getUnitFromID(units, incident.getAssignedUnitID());

                    //process on-scene work if unit has not yet resolved incident
                    if (assignedUnit.getCurrentIncidentWork() < assignedUnit.getTicksToResolve())
                    {
                        assignedUnit.doWork();
                    }

                    //resolving completed incidents
                    if (assignedUnit.getCurrentIncidentWork() == assignedUnit.getTicksToResolve())
                    {
                        //unit has completed incident, resetting stats for unit and resolving incident
                        incident.setIncidentStatus(IncidentStatus.RESOLVED);
                        incident.setAssignedUnitID(999); //ensures incident is not atttached to unit anymore
                        assignedUnit.currentIncidentWork = 0;
                        assignedUnit.currentIncidentID = 999;
                        assignedUnit.unitStatus = UnitStatus.IDLE;
                    }
                }
            }

            //initialising arrays for moving units
            Unit[] sortedUnits = Unit.getSortedUnits(units, unitCount, getUnitIds());
            Unit[] enRouteUnits = new Unit[sortedUnits.length];
            int pos = 0;

            //get en route units into an array, sorted by ascending ID
            for (Unit unit : sortedUnits)
            {
                if (unit == null) {continue;} //skips loop if unit is null
                
                if (unit.isEnRoute())
                {
                    enRouteUnits[pos] = unit;
                    pos++;
                }
            }

            //iterates through en route units, moving them
            for (Unit unit : enRouteUnits) 
            {
                if (unit == null) {continue;} //skips loop if unit is null

                //gets incident that unit is assigned to, and moves unit based on tiebreaker rules
                Incident incident = Incident.getIncidentFromID(incidents, unit.getCurrentIncidentID());
                cityMap.applyMovementRule(unit, incident);
            }

            //mark arrivals, have to go through sortedIncidents again so its in ID order
            sortedIncidents = Incident.getSortedIncidents(incidents, incidentCount, getIncidentIds());
            for (Incident incident : sortedIncidents)
            {
                if (incident == null) {continue;} //skips loop if incident is null

                //only incidents that will have arrived are dispatched units
                if (incident.getIncidentStatus() == IncidentStatus.DISPATCHED)
                {
                    //gets unit by incident ID (as this section needs to be done by incident ID order)
                    Unit assignedUnit = Unit.getUnitFromID(units, incident.getAssignedUnitID());

                    //set unit to at-scene and incident to in-progress if unit has arrived
                    if (assignedUnit.hasArrived(incident))
                    {
                        assignedUnit.setUnitStatus(UnitStatus.AT_SCENE);
                        incident.setIncidentStatus(IncidentStatus.IN_PROGRESS);
                    }
                }
            }
        }
        catch (Exception e) {System.out.println("An error has occurred: " + e);}
    }

    @Override
    public String getStatus() {
        //gets all the counts of stations, units, incidents and obstacles as a string
        String counts = cityMap.countsToString(stationCount, unitCount, incidentCount);
        //initialise blank strings for incident and unit info
        String allIncidentsString = new String();
        String allUnitsString = new String();
        try
        {
            //appends incident info to string for each incident
            for (Incident incident : incidents)
            {
                if (incident == null) {continue;}

                allIncidentsString += viewIncident(incident.getID()) + "\n";
            }

            //apends unit info to string for each unit
            for (Unit unit : units)
            {
                if (unit == null) {continue;}

                allUnitsString += viewUnit(unit.getID()) + "\n";
            }
        }
        catch (Exception e) {System.out.println("An ID error has occurred in getStatus(): " + e);}

        //returns large formatted strings, making use of new-line characters
        return
        (
            "TICK="+currentTick + "\n" +
            counts + "\n" +
            "INCIDENTS" + "\n" +
            allIncidentsString +
            "UNITS" + "\n" +
            allUnitsString
        );
    }
}
