package com.toolcheck.dao;

import com.toolcheck.model.Report;
import java.util.List;

//  ReportDAOInterface defines the standard operations for managing User data.
public interface ReportDAOInterface {
    boolean addReport(Report report); // Add a new report in the system.
    boolean resolveReport(long id);  // Resolve a report in the system.
    List<Report> getAllReports();    // Get all reports in the system
    Report getReportById(long id);   // Get a report by ID
}
