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
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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
import org.springframework.stereotype.Component;

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
    private static final Logger LOG = LoggerFactory.getLogger(OMAGServer.class);
    private static final ObjectReader OBJECT_READER = new ObjectMapper().reader();

    @Value("${strict.ssl:true}")
    Boolean strictSSL;

    @Value("${server.ssl.enabled:true}")
    Boolean serverSSL;

    @Value("${startup.user:system}")
    String sysUser;

    @Value("${omag.server.config}")
    Resource omagServerConfigLocation;
    private final OMAGServerOperationalServices operationalServices;
    private String serverName = null;
    private OMAGServerConfig serverConfig = null;


    public OMAGServer(Environment env) {
        this.env = env;
        this.operationalServices = new OMAGServerOperationalServices();
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(OMAGServer.class).run(args);
//        new SpringApplicationBuilder().sources(OMAGServer.class).listeners(new ApplicationContextInitializedEventHandler(), new ApplicationStartedEventHandler()).run(args);
    }

    //TODO: This code is common for the platform and server spring boot applications - as such should be moved to common util class to avoid code duplication and potentially inconsistent behaviour.
    @Bean
    public InitializingBean getInitialize() {
        return () ->
        {
            LOG.info("Working directory is: " + System.getProperty("user.dir"));

            if (!strictSSL) {
                LOG.warn("Option strict.ssl is set to false! Invalid certificates will be accepted for connection!");
                HttpHelper.noStrictSSL();
            }

            if (serverSSL && System.getProperty("javax.net.ssl.trustStore") == null) {
                LOG.warn("Java trust store 'javax.net.ssl.trustStore' is null - this is needed by Tomcat - using 'server.ssl.trust-store'");

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
        LOG.debug(">> Application ready");
    }

    @EventListener(ApplicationFailedEvent.class)
    private void onApplicationFailedEvent() {
        LOG.debug(">> Application failed");
    }

    @EventListener(ContextClosedEvent.class)
    private void onContextClosedEvent() {
        LOG.debug(">> Context closed");
        if (serverName != null) {
            LOG.info("Application stopped, deactivating server {}", serverName);
            operationalServices.deactivateTemporarilyServerList(sysUser, List.of(serverName));
        }
    }

    @EventListener(WebServerInitializedEvent.class)
    public void onWebServerInitialized() throws Exception {
        LOG.debug(">> WebServer initialized");
        loadServerConfig();
    }

    @Component
    public class OMAGServerStartup implements ApplicationRunner {
        @Override
        public void run(ApplicationArguments args) throws Exception {
            LOG.debug(">> Application runner");
//            loadServerConfig();
            activateOMAGServerUsingPlatformServices();
        }
    }

    private void activateOMAGServerUsingPlatformServices() throws Exception {

        try {
            if (serverConfig != null) {
                LOG.info("Activating OMAG server with name {}", serverName);
                SuccessMessageResponse response = operationalServices.activateWithSuppliedConfig(sysUser.trim(), serverConfig.getLocalServerName(), serverConfig);
                if (response.getRelatedHTTPCode() == 200) {
                    LOG.info("Successfully started OMAG server {}", serverConfig.getLocalServerName());
                    //TODO: Readiness probe state TRUE
                } else {
                    LOG.info("OMAG server activation failure");
//                    throw new RuntimeException(response.getExceptionErrorMessage());
                    //TODO: Readiness probe state FALSE
                }
            } else {
                LOG.info("OMAG server configuration is null, server cannot be activated");
                throw new Exception("OMAG server configuration is null");
            }

        } catch (Exception e) {
            LOG.error("Exception while trying to activate OMAG server", e);
            throw new Exception(e);
        }

    }

    private void loadServerConfig() throws IOException {
        if (omagServerConfigLocation != null) {
            LOG.info("Lading OMAG server configuration from location {}", omagServerConfigLocation.getFile());
            LOG.trace(Files.readString(Path.of(omagServerConfigLocation.getFile().getPath())));
            serverConfig = OBJECT_READER.readValue(omagServerConfigLocation.getFile(), OMAGServerConfig.class);
            serverName = serverConfig.getLocalServerName();
            LOG.info("Successfully loaded configuration document for OMAG server {}", serverName);
        }
    }

//    static class ApplicationContextInitializedEventHandler implements ApplicationListener<ApplicationContextInitializedEvent> {
//        @Override
//        public void onApplicationEvent(ApplicationContextInitializedEvent applicationContextInitializedEvent) {
//            LOG.debug(">> Application context initialized");
//        }
//    }
//
//    static class ApplicationStartedEventHandler implements ApplicationListener<ApplicationStartedEvent> {
//        @Override
//        public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
//            LOG.debug(">> Application started");
//        }
//    }
}
