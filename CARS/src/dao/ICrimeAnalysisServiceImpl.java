package dao;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import Entity.*;
import dao.ICrimeAnalysisService;

import util.DBConnUtil;
import util.DBPropertyUtil;

public class ICrimeAnalysisServiceImpl implements ICrimeAnalysisService {

    private Connection connection;

    public ICrimeAnalysisServiceImpl() {
        this.connection = DBConnUtil.getConnection(DBPropertyUtil.getConnectionString());
    }
    
    @Override 
	public boolean createIncident(Incidents incident) {
        try {
String sql = "INSERT INTO Incidents (IncidentID, IncidentType, IncidentDate, Location, Descriptions, Statuses, VictimID, SuspectID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, incident.getIncidentID());
                preparedStatement.setString(2, incident.getIncidentType());
                preparedStatement.setDate(3, new java.sql.Date(incident.getIncidentDate().getTime()));
                preparedStatement.setString(4, incident.getLocation());
                preparedStatement.setString(5, incident.getDescriptions());
                preparedStatement.setString(6, incident.getStatuses());
                preparedStatement.setInt(7, incident.getVictimID());
                preparedStatement.setInt(8, incident.getSuspectID());

                int rowsAffected = preparedStatement.executeUpdate();

                return rowsAffected > 0;
            }
        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }
	
	@Override 
	public boolean updateIncidentStatus(int incidentId,String statuses) {
    	try {
           
            String sql = "UPDATE Incidents SET Statuses = ? WHERE IncidentID = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, statuses);
                preparedStatement.setInt(2, incidentId);

                int rowsAffected = preparedStatement.executeUpdate();

                return rowsAffected > 0;
            }
        } catch (SQLException e) {
           
            e.printStackTrace();
            return false;
        }
    }
	 
	@Override 
	public List<Incidents> getIncidentsInDateRange(String startDateStr, String endDateStr) {
        List<Incidents> incidentsList = new ArrayList<>();
        try {
           
            String sql = "SELECT * FROM Incidents WHERE IncidentDate BETWEEN ? AND ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = new java.sql.Date(dateFormat.parse(startDateStr).getTime());
                Date endDate = new java.sql.Date(dateFormat.parse(endDateStr).getTime());
                preparedStatement.setDate(1, startDate);
                preparedStatement.setDate(2, endDate);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Incidents incident = new Incidents();
                        incident.setIncidentID(resultSet.getInt("incidentID"));
                        incident.setIncidentType(resultSet.getString("incidentType"));
                        incident.setIncidentDate(resultSet.getDate("incidentDate"));
                        incident.setLocation(resultSet.getString("location"));
                        incident.setDescriptions(resultSet.getString("descriptions"));
                        incident.setStatuses(resultSet.getString("statuses"));
                        incident.setVictimID(resultSet.getInt("victimID"));
                        incident.setSuspectID(resultSet.getInt("suspectID"));
                        incidentsList.add(incident);
                    }
                }
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        } 
        return incidentsList;
    }
   
	@Override 
	public List<Incidents> searchIncidentsByType(String incidentType) {
        List<Incidents> incidentsList = new ArrayList<>();

        try {
           

            String sql = "SELECT * FROM Incidents WHERE IncidentType = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, incidentType);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Incidents incident = new Incidents();
                        
               
                        incident.setIncidentID(resultSet.getInt("IncidentID"));
                        incident.setIncidentType(resultSet.getString("IncidentType"));
                        incident.setIncidentDate(resultSet.getDate("IncidentDate"));
                        incident.setLocation(resultSet.getString("Location"));
                        incident.setDescriptions(resultSet.getString("Descriptions"));
                        incident.setStatuses(resultSet.getString("Statuses"));
                        incident.setVictimID(resultSet.getInt("VictimID"));
                        incident.setSuspectID(resultSet.getInt("SuspectID"));

                        incidentsList.add(incident);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 

        return incidentsList;
    }
	
	@Override
	public Object generateIncidentReport(Incidents incident, int reportingOfficer ) {
	    try {
	        String reportDetails = "Incident Report\n"
	                + "Incident Type: " + incident.getIncidentType() + "\n"
	                + "Incident Date: " + incident.getIncidentDate() + "\n"
	                + "Location: " + incident.getLocation() + "\n"
	                + "Description: " + incident.getDescriptions() + "\n"
	                + "Status: " + incident.getStatuses();

	        Map<String, Object> reportObject = new HashMap<>();
	        reportObject.put("reportDetails", reportDetails);
	        reportObject.put("reportDate", new java.util.Date());
	        reportObject.put("statuss", "Draft");

	        // Assuming you have a Reports table in the database to store incident reports
	        String insertReportQuery = "INSERT INTO Reports (IncidentID, ReportDetails, ReportDate, Statuss, ReportingOfficer) " +
	                "VALUES (?, ?, ?, ?, ?)";
	        try (PreparedStatement statement = connection.prepareStatement(insertReportQuery, Statement.RETURN_GENERATED_KEYS)) {
	            statement.setInt(1, incident.getIncidentID());
	            statement.setString(2, reportDetails);
	            statement.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
	            statement.setString(4, "Draft");
	            statement.setInt(5, reportingOfficer);

	            int rowsAffected = statement.executeUpdate();
	            if (rowsAffected > 0) {
	                ResultSet generatedKeys = statement.getGeneratedKeys();
	                if (generatedKeys.next()) {
	                    int reportID = generatedKeys.getInt(1);
	                    reportObject.put("reportID", reportID);
	                }

	                System.out.println("Incident report saved to the database.");
	            } else {
	                System.out.println("Failed to save incident report to the database.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return null;
	        }

	        return reportObject;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	@Override
	public Incidents getIncidentById(int incidentId) {
	    try {
	        String sql = "SELECT * FROM Incidents WHERE IncidentID = ?";
	        try (PreparedStatement statement = connection.prepareStatement(sql)) {
	            statement.setInt(1, incidentId);

	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                    // Create an Incidents object and set its properties
	                    Incidents incident = new Incidents();
	                    incident.setIncidentID(resultSet.getInt("IncidentID"));
	                    incident.setIncidentType(resultSet.getString("IncidentType"));
	                    incident.setIncidentDate(resultSet.getDate("IncidentDate"));
	                    incident.setDescriptions(resultSet.getString("Descriptions"));
	                    incident.setLocation(resultSet.getString("Location"));
	                    incident.setStatuses(resultSet.getString("Statuses"));
	                    incident.setVictimID(resultSet.getInt("VictimID"));
	                    incident.setSuspectID(resultSet.getInt("SuspectID"));

	                    // Return the populated Incidents object
	                    return incident;
	                } else {
	                    // Return null if no incident is found
	                    return null;
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
	}


}                                        
