package cityrescue;
import cityrescue.enums.*;
import cityrescue.exceptions.*;

public class Incident {

    private IncidentStatus incidentStatus;
    private IncidentType incidentType;
    private int incidentID;
    private int severity;
    private int x;
    private int y;

    public Incident(int id, int severity, int x, int y)
    {
        this.incidentID = id;
        this.severity = severity;
        this.x = x;
        this.y = y;
    }

    public int getID()
    {
        return incidentID;
    }
}
