package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Entity.Incidents;
import dao.ICrimeAnalysisServiceImpl;

public class CrimeDaoTest {
	
	private ICrimeAnalysisServiceImpl crimeDao;
    
   
    
    @Before
    public void setUp() {
        crimeDao = new ICrimeAnalysisServiceImpl();
       
        
    }
 
    
    @After
    public void tearDown() {
        // Clean up resources if needed
        crimeDao = null;
       
       
    }
    
    @Test
    public void testCreateIncident() {
    	
    	Incidents testnewincident = new Incidents(
    			19,
    			"theft",
    			java.sql.Date.valueOf("2023-10-15"),
    			"Latitude: 40, Longitude: 50",
    			"Theft at Temple",
    			"Open",
    			2,
    			6
    			);
    	boolean result = crimeDao.createIncident(testnewincident);
    	
    	assertTrue("Incident creation success", result);
    	
    }
    
    @Test
    public void updateIncidentStatus() {
    	int incidentId = 18;
        String status = "CLOSED";
    	
    	boolean result = crimeDao.updateIncidentStatus(incidentId, status);
    	
    	assertTrue("Incident status updated successfully", result);
        
        
    	
    }

}
