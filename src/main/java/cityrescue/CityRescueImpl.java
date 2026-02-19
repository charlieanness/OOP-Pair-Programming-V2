package cityrescue;

import cityrescue.enums.*;
import cityrescue.exceptions.*;
import cityrescue.Station;

/**
 * CityRescueImpl (Starter)
 *
 * Your task is to implement the full specification.
 * You may add additional classes in any package(s) you like.
 */
public class CityRescueImpl implements CityRescue {

    // TODO: add fields (map, arrays for stations/units/incidents, counters, tick, etc.)
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
        cityMap.clearObstacles();

        int size = cityMap.getWidth()*cityMap.getHeight();

        stations = new Station[size];
        stationCount = 0;

        units = new Unit[size];
        unitCount = 0;

        incidents = new Incident[size];
        incidentCount = 0;

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

        Station newStation = new Station(name, x, y);
        for (int i=0; i<stations.length; i++)
        {
            if (stations[i] == null)
            {
                stations[i] = newStation;
            }
        }
        stationCount++;
        return newStation.getID();
    }

    @Override
    public void removeStation(int stationId) throws IDNotRecognisedException, IllegalStateException {
        for (int i=0; i<stations.length; i++)
        {
            if (stations[i].getID() == stationId)
            {
                stations[i] = null;
            }
        }
    }

    @Override
    public void setStationCapacity(int stationId, int maxUnits) throws IDNotRecognisedException, InvalidCapacityException {
        if (maxUnits <= 0 || maxUnits < unitCount) {throw new InvalidCapacityException("Invalid Capacity");}
        for (int i=0; i<stations.length; i++)
        {
            if (stations[i].getID() == stationId)
            {
                stations[i].setCapacity(maxUnits);
            }
            if (i==stations.length-1)
            {
                throw new IDNotRecognisedException("No station exists with that ID");
            }
        }
    }

    @Override
    public int[] getStationIds() {
        int[] ids = new int[stationCount];
        int pos = 0;
        for (int i=0; i<stations.length; i++)
        {
            if (station[i] != null)
            {
                ids[pos++] = station[i].getID();
            }
        }
        Arrays.sort(ids);
        return ids;
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int addUnit(int stationId, UnitType type) throws IDNotRecognisedException, InvalidUnitException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
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
