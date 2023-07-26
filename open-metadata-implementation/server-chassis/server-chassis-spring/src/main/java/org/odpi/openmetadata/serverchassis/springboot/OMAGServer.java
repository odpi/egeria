/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverchassis.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.platformservices.rest.SuccessMessageResponse;
import org.odpi.openmetadata.platformservices.server.OMAGServerOperationalServices;
import org.odpi.openmetadata.serverchassis.springboot.exception.OMAGServerActivationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * OMAGServer provides the main program for the OMAG Server spring-boot based starter application.
 */
@SpringBootApplication(
        scanBasePackages = {"org.odpi.openmetadata"}
)
public class OMAGServer implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(OMAGServer.class);
    private final OMAGServerOperationalServices operationalServices;
    @Value("${omag.server.user:system}")
    String omagServerUser;
    @Value("${omag.server.config:#{null}}")
    Resource omagServerConfigResource;
    private ObjectMapper objectMapper;
    private String serverName = null;
    private OMAGServerConfig serverConfig = null;
    private final ConfigurableApplicationContext context;

    /**
     * Constructor injecting the beans required.
     * It also creates instance of OMAGServerOperationalServices.
     * @see org.odpi.openmetadata.platformservices.server.OMAGServerOperationalServices
     */
    @Autowired
    public OMAGServer(ObjectMapper objectMapper, ConfigurableApplicationContext ctx) {
        this.context = ctx;
        this.objectMapper = objectMapper;
        this.operationalServices = new OMAGServerOperationalServices();
    }

    /**
     * Main program, creating spring boot application instance.
     * @param args
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(OMAGServer.class).run(args);
    }

    /**
     * ApplicationRunner implementation.
     * The purpose of this class is to provide a standard way to run the OMAG server activation task, separate from the main application thread.
     * Executing the server activation in a runner task affects internal availability state by default as the runners are part of the standard application lifecycle.
     */
    @Override
    public void run(ApplicationArguments args) {
        LOG.debug("Application runner executing run");
        try {
            activateOMAGServerUsingPlatformServices();
        } catch (OMAGServerActivationError e) {
            LOG.error("Server activation failed due to internal application error", e);
            /**
             *  Any exception captured at this point means that there was problem activating the OMAG server, thus the application should be shut down.
             *  Since this is executed as application runner in a separate thread then main propagating the error further does not properly stop the application, instead we are explicitly exiting the application and jvm.
             */
            System.exit(SpringApplication.exit(context));
        }
    }

    /**
     * Handling Spring Boot ContextClosedEvent
     * This handler provides a way to hook to the standard Spring Boot application shut-down event and call OMAG operational services to deactivate the server instance by name.
     */
    @EventListener(ContextClosedEvent.class)
    private void onContextClosedEvent() {
        if (serverName != null) {
            LOG.info("Application stopped, deactivating server: {}", serverName);
            operationalServices.deactivateTemporarilyServerList(omagServerUser, List.of(serverName));
        }
    }

    /**
     * This method activates OMAGServer instance using OMAG platform operational services.
     * @see org.odpi.openmetadata.platformservices.server.OMAGServerOperationalServices
     *
     * The activation process requires OMAGServerConfig document.
     * @see org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig
     *
     * The OMAGServerConfig document file location is provided as org.springframework.core.io.Resource and configured by application property ${omag.server.config}
     *
     * @throws OMAGServerActivationError
     */
    private void activateOMAGServerUsingPlatformServices() throws OMAGServerActivationError {

        LOG.debug("Activation started");

        if (omagServerConfigResource == null) {
            LOG.info("Configuration loading failed, the cause is that configuration resource provided is null");
            throw new OMAGServerActivationError("Configuration failed, the cause is that configuration resource provided is null");
        }

        try {
            LOG.info("Configuration file: {} is being parsed", omagServerConfigResource.getFile());
            LOG.trace("{}", Files.readString(Path.of(omagServerConfigResource.getFile().getPath())));
            serverConfig = objectMapper.reader().readValue(omagServerConfigResource.getFile(), OMAGServerConfig.class);
            serverName = serverConfig.getLocalServerName();
            LOG.info("Configuration document for server: {} loaded successfully", serverName);
        } catch (IOException e) {
            LOG.info("Configuration cannot be loaded from the resource provided");
            throw new OMAGServerActivationError(
                    String.format("Configuration cannot be loaded from the resource provided"),e);
        }

        if (serverConfig == null) {
            LOG.info("Activation failed, the cause is that configuration is null");
            throw new OMAGServerActivationError("Activation failed the cause is that configuration is null");

        }

        LOG.info("Sending activation request for server: {}", serverName);

        SuccessMessageResponse response = operationalServices
                .activateWithSuppliedConfig(omagServerUser.trim(), serverConfig.getLocalServerName(), serverConfig);

        if (response == null) {
            LOG.info("Activation has failed. The cause is that response is null");
            throw new OMAGServerActivationError("Activation has failed. The cause is that response is null");
        }

        if (response.getRelatedHTTPCode() != 200) {
            LOG.info("Activation has failed with response code: {}", response.getRelatedHTTPCode());
            throw new OMAGServerActivationError(String.format("Server activation failed with response code %s", response.getRelatedHTTPCode()));
        }

        if (response.getRelatedHTTPCode() == 200) {
            LOG.info("Activation succeeded for server: {}", serverConfig.getLocalServerName());
        }

    }

}
