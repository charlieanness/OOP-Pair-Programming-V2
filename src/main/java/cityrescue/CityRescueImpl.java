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
        
        station.addUnitToStation(newUnit);
        return newUnit.getID();
    }

    @Override
    public void decommissionUnit(int unitId) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int[] getUnitIds() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y) throws InvalidSeverityException, InvalidLocationException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void escalateIncident(int incidentId, int newSeverity) throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int[] getIncidentIds() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void dispatch() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void tick() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String getStatus() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
