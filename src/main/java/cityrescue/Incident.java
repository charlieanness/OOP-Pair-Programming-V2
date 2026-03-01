package cityrescue;
import cityrescue.enums.*;
import cityrescue.exceptions.*;

/**
 * The Incident class contains all logic
 * concerning incidents, such as its status,
 * type, coordinates, etc.
 * Contains static functions that relate to incident(s).
 */
public class Incident {

    private IncidentStatus incidentStatus;
    private IncidentType incidentType;
    private int incidentID;
    private int severity;
    private int assignedUnitID; //CRUCIAL: this is assigned 999 when no unit is assigned
    private int x;
    private int y;

    //public constructor
    public Incident(int id, IncidentType type, int severity, int x, int y)
    {
        this.incidentID = id;
        this.incidentType = type;
        this.severity = severity;
        this.x = x;
        this.y = y;
        this.assignedUnitID = 999;
        this.incidentStatus = IncidentStatus.REPORTED;
    }

    public int getID()
    {
        return incidentID;
    }

    //returns a current incident by its specified ID
    public static Incident getIncidentFromID(Incident[] incidents, int incidentID) throws IDNotRecognisedException
    {
        for (int i=0; i<incidents.length; i++)
        {
            if (incidents[i] != null)
            {
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

    public IncidentType getIncidentType()
    {
        return incidentType;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    //checks if incident is not already being processed
    public boolean canBeCancelled()
    {
        return ((incidentStatus == IncidentStatus.REPORTED) || (incidentStatus == IncidentStatus.DISPATCHED));
    }

    //checks if incident can be escalated (not resolved or cancelled)
    public boolean canBeEscalated()
    {
        return ((incidentStatus != IncidentStatus.RESOLVED) && (incidentStatus != IncidentStatus.CANCELLED));
    }

    public int getAssignedUnitID()
    {
        return assignedUnitID;
    }

    public void setAssignedUnitID(int id)
    {
        assignedUnitID = id;
    }

    public void setSeverity(int newSeverity)
    {
        severity = newSeverity;
    }

    /*
    Returns a formatted string containing
    information about the incident
    */
    public String viewIncidentStats()
    {
        //making assignedUnitID = "-" for formatting if it is 999 (not assigned)
        String assignedUnitIDString;
        if (assignedUnitID == 999) {assignedUnitIDString = "-";}
        else {assignedUnitIDString = String.valueOf(assignedUnitID);}

        return
        (
            "I#"+incidentID+
            " TYPE="+incidentType+
            " SEVERITY="+severity+
            " LOC=("+x+","+y+")"+
            " STATUS="+incidentStatus+
            " UNIT="+assignedUnitIDString
        );
    }

    /*
    Uses a sorted incident IDs array to construct and return
    an array of incidents in ID order (ascending).
    */
    public static Incident[] getSortedIncidents(Incident[] incidents, int incidentCount, int[] sortedIDs) throws IDNotRecognisedException
    {
        Incident[] sortedIncidents = new Incident[incidentCount];
        int pos = 0;
        
        for (int i=0; i<sortedIDs.length; i++)
        {
            Incident incident = Incident.getIncidentFromID(incidents, sortedIDs[i]);
            sortedIncidents[pos] = incident;
            pos++;
        }

        return sortedIncidents;
    }

    //returns array of all current incidents that are reported
    public static Incident[] getReportedIncidents(Incident[] incidents)
    {
        Incident[] reportedIncidents = new Incident[countReportedIncidents(incidents)];
        int pos = 0;

        for (int i=0; i<incidents.length;i++)
        {
            if (incidents[i].getIncidentStatus() == IncidentStatus.REPORTED)
            {
                reportedIncidents[pos] = incidents[i];
                pos++;
            }
        }
        return reportedIncidents;
    }

    //returns number of incidents that are in the "reported" state
    public static int countReportedIncidents(Incident[] incidents)
    {
        int count = 0;

        for (int i=0; i<incidents.length; i++)
        {
            if (incidents[i].getIncidentStatus() == IncidentStatus.REPORTED)
            {
                count++;
            }
        }

        return count;
    }

}
