package com.toolcheck.controller;

import com.toolcheck.model.Report;
import java.util.List;

//  It is used by the controller layer to manage users in the system.
public interface ReportControllerInterface {
    boolean addReport(Report report);       // Create a new report in the system.
    boolean resolveReport(long id);         // Resolve a report in the system
    List<Report> getAllReports();           // Get all reports in the system.
}