/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

public class JerseyAppConfig extends ResourceConfig {
    
    public JerseyAppConfig() {
        register(HelloResource.class);
        
        property(ServerProperties.MONITORING_ENABLED, false);
        property(ServerProperties.MONITORING_STATISTICS_ENABLED, false);
        property(ServerProperties.MONITORING_STATISTICS_MBEANS_ENABLED, false);
    }
}
