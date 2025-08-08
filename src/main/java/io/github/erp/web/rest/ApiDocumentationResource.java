package io.github.erp.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for API documentation dashboard.
 */
@RestController
@RequestMapping("/api/docs")
public class ApiDocumentationResource {

    /**
     * GET /dashboard : Get API documentation dashboard information
     *
     * @return the ResponseEntity with status 200 (OK) and dashboard data
     */
    @GetMapping(value = "/dashboard", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("title", "ERP System API Documentation");
        dashboard.put("version", "1.8.2");
        dashboard.put("description", "Complete API reference for the ERP System");
        
        Map<String, String> links = new HashMap<>();
        links.put("swagger-ui", "/swagger-ui/");
        links.put("api-docs", "/v2/api-docs");
        links.put("endpoints-list", "/api/system/endpoints");
        dashboard.put("links", links);
        
        Map<String, String> categories = new HashMap<>();
        categories.put("AssetManagement", "Fixed asset registration, depreciation, and lifecycle management");
        categories.put("PaymentProcessing", "Settlement processing and payment management");
        categories.put("Reporting", "Financial and operational reporting system");
        categories.put("LeaseManagement", "IFRS 16 compliant lease accounting");
        categories.put("DocumentManagement", "File upload and document handling");
        categories.put("GranularDataIntegration", "Central bank reporting and data integration");
        dashboard.put("categories", categories);
        
        return ResponseEntity.ok(dashboard);
    }
}
