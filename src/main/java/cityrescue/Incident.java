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

    public static Incident[] getReportedIncidents(Incident[] incidents)
    {
        Incident[] reportedIncidents = new Incident[countReportedIncidents(incidents)];
        int pos = 0;

        for (int i=0; i<incidents.length;i++)
        {
            if (incidents[i].getIncidentStatus() == IncidentStatus.REPORTED)
            {
                reportedIncidents[pos] = incidents[i];
            }
        }
        return reportedIncidents;
    }

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
