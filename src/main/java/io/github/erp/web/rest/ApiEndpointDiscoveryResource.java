package io.github.erp.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for API endpoint discovery and documentation.
 */
@RestController
@RequestMapping("/api/system")
public class ApiEndpointDiscoveryResource {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * GET /endpoints : Get all available API endpoints
     *
     * @return the ResponseEntity with status 200 (OK) and list of endpoints
     */
    @GetMapping("/endpoints")
    public ResponseEntity<Map<String, Object>> getAllEndpoints() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

        List<Map<String, Object>> endpoints = new ArrayList<>();
        Map<String, Integer> categoryCount = new HashMap<>();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            HandlerMethod method = entry.getValue();

            if (info.getPatternsCondition() != null) {
                for (String pattern : info.getPatternsCondition().getPatterns()) {
                    if (pattern.startsWith("/api/")) {
                        Map<String, Object> endpoint = new HashMap<>();
                        endpoint.put("path", pattern);
                        endpoint.put("methods", info.getMethodsCondition().getMethods());
                        endpoint.put("controller", method.getBeanType().getSimpleName());
                        endpoint.put("method", method.getMethod().getName());
                        
                        String category = categorizeEndpoint(pattern);
                        endpoint.put("category", category);
                        categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
                        
                        endpoints.add(endpoint);
                    }
                }
            }
        }

        endpoints.sort((a, b) -> ((String) a.get("path")).compareTo((String) b.get("path")));

        Map<String, Object> response = new HashMap<>();
        response.put("totalEndpoints", endpoints.size());
        response.put("categories", categoryCount);
        response.put("endpoints", endpoints);
        response.put("timestamp", new Date());

        return ResponseEntity.ok(response);
    }

    private String categorizeEndpoint(String path) {
        if (path.contains("/fixed-asset")) return "Asset Management";
        if (path.contains("/payments")) return "Payment Processing";
        if (path.contains("/reports") || path.contains("/report")) return "Reporting";
        if (path.contains("/leases")) return "Lease Management";
        if (path.contains("/app/")) return "Application Services";
        if (path.contains("/granular-data")) return "Granular Data Integration";
        if (path.contains("/taxes")) return "Tax Management";
        if (path.contains("/docs")) return "Document Management";
        if (path.contains("/system")) return "System Administration";
        return "General";
    }
}
