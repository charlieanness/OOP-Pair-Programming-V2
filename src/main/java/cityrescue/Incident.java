package cityrescue;
import cityrescue.enums.*;
import cityrescue.exceptions.*;

public class Incident {

    private IncidentStatus incidentStatus;
    private IncidentType incidentType;
    private int incidentID;
    private int severity;
    private int assignedUnitID;
    private int x;
    private int y;

    public Incident(int id, int severity, int x, int y)
    {
        this.incidentID = id;
        this.severity = severity;
        this.x = x;
        this.y = y;
        this.assignedUnitID = 999;
    }

    public int getID()
    {
        return incidentID;
    }

    public static Incident getIncidentFromID(Incident[] incidents, int incidentID) throws IDNotRecognisedException
    {
        for (int i=0; i<incidents.length; i++)
        {
            if (incidents[i] != null)
            {
                //for testing
                //System.out.println("Incident Object found. ID is:" + incidents[i].getID() + ", looking for ID: "+incidentID);

                if (incidents[i].getID() == incidentID)
                {
                    return incidents[i];
                }
            }
        }
        throw new IDNotRecognisedException("No incident with that ID exists!");
    }

    public IncidentStatus getIncidentStatus()
    {
        return incidentStatus;
    }

    public void setIncidentStatus(IncidentStatus status)
    {
        incidentStatus = status;
    }

    public boolean canBeCancelled()
    {
        return ((incidentStatus != IncidentStatus.REPORTED) && (incidentStatus != IncidentStatus.DISPATCHED));
    }

    public boolean canBeEscalated()
    {
        return ((incidentStatus != IncidentStatus.RESOLVED) && (incidentStatus != IncidentStatus.CANCELLED));
    }

    public int getAssignedUnitID()
    {
        return assignedUnitID;
    }

    public void setSeverity(int newSeverity)
    {
        severity = newSeverity;
    }

    public String viewIncidentStats()
    {
        return
        (
            "I#"+incidentID+
            " TYPE="+incidentType+
            " SEVERITY="+severity+
            " LOC=("+x+","+y+")"+
            " STATUS="+incidentStatus+
            " UNIT="+assignedUnitID
        );
    }

}
