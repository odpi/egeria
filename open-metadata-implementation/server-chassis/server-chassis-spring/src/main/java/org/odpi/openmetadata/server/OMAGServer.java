/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.http.HttpHelper;
import org.odpi.openmetadata.platformservices.rest.SuccessMessageResponse;
import org.odpi.openmetadata.platformservices.server.OMAGServerOperationalServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@SpringBootApplication(
        scanBasePackages = {"${scan.packages:org.odpi.openmetadata.*}"}
)
//TODO: ADD JAVADOCS!!!
public class OMAGServer {
    private final Environment env;
    private static final Logger log = LoggerFactory.getLogger(OMAGServer.class);
    private static final ObjectReader OBJECT_READER = new ObjectMapper().reader();

    @Value("${strict.ssl:true}") // Default value is true
    Boolean strictSSL;

    @Value("${startup.user:system}") // Default value is "system"
    String sysUser;

    @Value("${omag.server.config.location}")
    Resource omagServerConfigLocation;
    private final OMAGServerOperationalServices operationalServices = new OMAGServerOperationalServices();

    private String serverName = null;


    public OMAGServer(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
                new SpringApplicationBuilder().sources(OMAGServer.class).run(args);
//        new SpringApplicationBuilder().sources(OMAGServer.class).listeners(new ApplicationPreparedEventHandler(),new ApplicationContextInitializedEventHandler(), new ApplicationStartingEventHandler(), new ApplicationEnvironmentPreparedEventHandler()).run(args);
    }

    //TODO: This code is common for the platform and server spring boot applications - as such should be moved to common util class to avoid code duplication and potentially inconsistent behaviour.
    @Bean
    public InitializingBean getInitialize() {
        return () ->
        {
            log.info("Working directory is: " + System.getProperty("user.dir"));

            if (!strictSSL) {
                log.warn("Option strict.ssl is set to false! Invalid certificates will be accepted for connection!");
                HttpHelper.noStrictSSL();
            }

            if (System.getProperty("javax.net.ssl.trustStore") == null) {
                log.warn("Java trust store 'javax.net.ssl.trustStore' is null - this is needed by Tomcat - using 'server.ssl.trust-store'");

                /*
                 * load the 'javax.net.ssl.trustStore' and 'javax.net.ssl.trustStorePassword' from application.properties.
                 * Note, these variables should only used for mutual SSL.  This function is provided for backward compatibility.
                 * Also note that there is an NPE if the java variables are set to null.
                 */
                if (env.getProperty("server.ssl.trust-store") != null) {
                    System.setProperty("javax.net.ssl.trustStore", env.getProperty("server.ssl.trust-store"));
                }
                if (env.getProperty("server.ssl.trust-store-password") != null) {
                    System.setProperty("javax.net.ssl.trustStorePassword", env.getProperty("server.ssl.trust-store-password"));
                }
            }
        };
    }

    @EventListener(ApplicationReadyEvent.class)
    private void onApplicationReadyEvent() {
        log.debug(">> Application ready");
        //TODO: application liveness state is TRUE
    }

    @EventListener(ApplicationFailedEvent.class)
    private void onApplicationFailedEvent() {
        log.debug(">> Application failed");
        //TODO: application liveness state is FALSE (?) We need to investigate more this scenario.
    }

    @EventListener(ContextClosedEvent.class)
    private void onContextClosedEvent() {
        log.debug(">> Context closed");
        if (serverName != null) {
            log.info("Application stopped, deactivating server {}", serverName);
            operationalServices.deactivateTemporarilyServerList(sysUser, List.of(serverName));
        }
    }

    @EventListener(WebServerInitializedEvent.class)
    public void onWebServerInitialized() {
        log.debug(">> WebServer initialized");
        activateOMAGServerUsingPlatformServices();
    }

    private void activateOMAGServerUsingPlatformServices() {

        try {

            log.info("Activating OMAGServer using platform services and configuration from location {}", omagServerConfigLocation.getFile());
            OMAGServerConfig omagServerConfig = null;
            if (omagServerConfigLocation != null) {
                log.trace(Files.readString(Path.of(omagServerConfigLocation.getFile().getPath())));
                omagServerConfig = OBJECT_READER.readValue(omagServerConfigLocation.getFile(), OMAGServerConfig.class);
                serverName = omagServerConfig.getLocalServerName();
                log.info("Successfully loaded configuration document for OMAG server {}", serverName);
            }

            if (omagServerConfig != null) {
                SuccessMessageResponse response = operationalServices.activateWithSuppliedConfig(sysUser.trim(), omagServerConfig.getLocalServerName(), omagServerConfig);
                if (response.getRelatedHTTPCode() == 200) {
                    log.info("Successfully started OMAG server {}", omagServerConfig.getLocalServerName());
                    //TODO: Readiness probe state TRUE
                } else {
                    log.info("OMAG server activation failure");
//                    throw new RuntimeException(response.getExceptionErrorMessage());
                    //TODO: Readiness probe state FALSE
                }
            } else {
                log.info("OMAG server configuration is null, server cannot be activated");
                //TODO: Initiate proper spring boot application shutdown sequence.
                throw new RuntimeException("OMAG server configuration is null");
            }

        } catch (IOException e) {
            log.error("Exception while trying to activate OMAG server", e);
            throw new RuntimeException(e);
        }

    }
//    static class ApplicationPreparedEventHandler implements ApplicationListener<ApplicationPreparedEvent> {
//        @Override
//        public void onApplicationEvent(ApplicationPreparedEvent applicationPreparedEvent) {
//            log.debug(">>> Application prepared");
//        }
//    }
//    static class ApplicationContextInitializedEventHandler implements ApplicationListener<ApplicationContextInitializedEvent> {
//        @Override
//        public void onApplicationEvent(ApplicationContextInitializedEvent applicationContextInitializedEvent) {
//            log.debug(">>> Application context initialized");
//        }
//    }
//
//    static class ApplicationStartingEventHandler implements ApplicationListener<ApplicationStartingEvent> {
//        @Override
//        public void onApplicationEvent(ApplicationStartingEvent applicationStartingEvent) {
//            log.debug(">>> Application starting");
//        }
//    }
//
//    static class ApplicationEnvironmentPreparedEventHandler implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
//
//        @Override
//        public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) {
//            log.debug(">>> Application prepared");
//        }
//    }
}
