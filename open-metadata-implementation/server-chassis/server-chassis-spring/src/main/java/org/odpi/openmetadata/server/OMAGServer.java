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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication(
        scanBasePackages = {"${scan.packages:org.odpi.openmetadata.*}"}
)
//TODO: Add java docs
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
    public OMAGServer(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication.run(OMAGServer.class, args);
    }

    //TODO: This code is common for the platform and server spring boot applications - as such should be moved to common util class to avoid code duplication and potentially inconsistent behaviour.
    @Bean
    public InitializingBean getInitialize() {
        return () ->
        {
            log.info("Working directory is: " + System.getProperty("user.dir"));

            if (!strictSSL)
            {
                log.warn("Option strict.ssl is set to false! Invalid certificates will be accepted for connection!");
                HttpHelper.noStrictSSL();
            }

            if (System.getProperty("javax.net.ssl.trustStore") == null)
            {
                log.warn("Java trust store 'javax.net.ssl.trustStore' is null - this is needed by Tomcat - using 'server.ssl.trust-store'");

                /*
                 * load the 'javax.net.ssl.trustStore' and 'javax.net.ssl.trustStorePassword' from application.properties.
                 * Note, these variables should only used for mutual SSL.  This function is provided for backward compatibility.
                 * Also note that there is an NPE if the java variables are set to null.
                 */
                if (env.getProperty("server.ssl.trust-store") != null)
                {
                    System.setProperty("javax.net.ssl.trustStore", env.getProperty("server.ssl.trust-store"));
                }
                if (env.getProperty("server.ssl.trust-store-password") != null)
                {
                    System.setProperty("javax.net.ssl.trustStorePassword", env.getProperty("server.ssl.trust-store-password"));
                }
            }
        };
    }

    @EventListener(ContextRefreshedEvent.class)
    private void onContextRefreshedEvent(){
        log.info("Context refreshed");
        activateOMAGServer();
    }

    @EventListener(ApplicationReadyEvent.class)
    private void onApplicationReadyEvent() {
        log.info("Application ready");
    }
    @EventListener(ApplicationFailedEvent.class)
    private void onApplicationFailedEvent() {
        log.info("Application failed");
        //TODO: Temporary deactivate server
    }

    @EventListener(ContextClosedEvent.class)
    private void onContextClosedEvent()
    {
        log.info("Application stopped");
        //TODO: Temporary deactivate server
    }
    private void activateOMAGServer() {
        try {
            OMAGServerConfig omagServerConfig = null;
            if (omagServerConfigLocation != null) {
                log.info("Using OMAG Server configuration from location {}", omagServerConfigLocation.getFile());
                log.debug(Files.readString(Path.of(omagServerConfigLocation.getFile().getPath())));
                omagServerConfig = OBJECT_READER.readValue(omagServerConfigLocation.getFile(), OMAGServerConfig.class);
                log.info("Successfully loaded configuration for OMAG server {}", omagServerConfig.getLocalServerName());
            }

            if (omagServerConfig != null) {
                SuccessMessageResponse response = operationalServices.activateWithSuppliedConfig(sysUser.trim(), omagServerConfig.getLocalServerName() ,omagServerConfig);
                if (response.getRelatedHTTPCode() == 200) {
                    log.info("Successfully started OMAG server {}", omagServerConfig.getLocalServerName());
                } else {
                    log.info("OMAG server activation failure");
                    //TODO: Initiate start up error handling sequence
                }
            } else {
                log.info("OMAG server configuration is null, server cannot be activated");
                //TODO: Initiate proper spring boot application shutdown sequence.
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
