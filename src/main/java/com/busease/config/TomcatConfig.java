package com.busease.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Value;

/**
 * Custom Tomcat configuration to support both Replit and IDE environments.
 * This configuration enhances server stability and accessibility.
 * 
 * For IDEs: Uses standard port 8080 (default)
 * For Replit: Uses port 5000 with enhanced network settings
 */
@Configuration
public class TomcatConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    
    @Value("${server.port:8080}")
    private int serverPort;
    
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        try {
            // Check if running in Replit
            boolean isReplitEnvironment = System.getenv("REPL_ID") != null;
            
            if (isReplitEnvironment) {
                // Ensure Tomcat binds to all network interfaces in Replit
                factory.setAddress(java.net.InetAddress.getByName("0.0.0.0"));
                factory.setPort(5000); // Explicit for Replit
                
                // Enhanced settings for Replit environment
                factory.addConnectorCustomizers(connector -> {
                    connector.setProperty("connectionTimeout", "120000"); // 2 minutes
                    connector.setProperty("maxKeepAliveRequests", "200");
                    connector.setProperty("maxThreads", "100");
                    connector.setProperty("acceptCount", "50");
                });
                
                System.out.println("Running in Replit environment on port 5000");
            } else {
                // For non-Replit environments (IDEs, etc.)
                factory.setPort(serverPort);
                
                // Standard settings for IDEs
                factory.addConnectorCustomizers(connector -> {
                    connector.setProperty("connectionTimeout", "60000"); // 1 minute
                });
                
                System.out.println("Running in IDE/local environment on port " + serverPort);
            }
        } catch (UnknownHostException e) {
            // Log the error but don't crash
            System.err.println("Failed to set server address: " + e.getMessage());
            // Continue with default settings
        }
    }
}