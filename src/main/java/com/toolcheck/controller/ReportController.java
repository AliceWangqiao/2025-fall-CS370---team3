package com.toolcheck.controller;

import com.toolcheck.dao.ReportDAOInterface;
import com.toolcheck.model.Report;

import java.util.List;

// It acts as a bridge between the data layer (DAO) and the application logic.
public class ReportController implements ReportControllerInterface {

    // Reference to the DAO interface for reports.
    private final ReportDAOInterface dao;

    // Constructor to initialize the controller with a specific DAO implementation.
    public ReportController(ReportDAOInterface dao) {
        this.dao = dao;
    }

    // Adds a new report after validating its type.
    @Override
    public boolean addReport(Report report) {
        // Check that report type is provided
        if (report.getReportType() == null) return false;

        // Normalize the type to lowercase and remove extra spaces
        String type = report.getReportType().trim().toLowerCase();

        // Only allow valid report types: damage, maintenance, lost
        if (!(type.equals("damage") || type.equals("maintenance") || type.equals("lost"))) return false;

        // Set the normalized report type
        report.setReportType(type);

        // Delegate saving to the DAO
        return dao.addReport(report);
    }

    // Marks a report as resolved.
    @Override
    public boolean resolveReport(long id) {
        return dao.resolveReport(id);
    }

    // Get all reports from the DAO.
    @Override
    public List<Report> getAllReports() {
        return dao.getAllReports();
    }
}