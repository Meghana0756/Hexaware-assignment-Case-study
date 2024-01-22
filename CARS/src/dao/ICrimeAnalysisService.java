package dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import Entity.Incidents;

public interface ICrimeAnalysisService {

	

	List<Incidents> searchIncidentsByType(String incidentType);

	List<Incidents> getIncidentsInDateRange(String startDateStr, String endDateStr);

	boolean updateIncidentStatus(int incidentId, String status);

	boolean createIncident(Incidents incident);

	Object generateIncidentReport(Incidents incident, int reportingOfficer);

	Incidents getIncidentById(int incidentId);

	
	

}
