/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverchassis.springboot;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.serverchassis.springboot.config.OMAGConfigHelper;
import org.odpi.openmetadata.serverchassis.springboot.exception.OMAGServerActivationError;
import org.odpi.openmetadata.serveroperations.rest.SuccessMessageResponse;
import org.odpi.openmetadata.serveroperations.server.OMAGServerOperationalServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import java.util.List;

/**
 * OMAGServer provides the main program for the OMAG Server spring-boot based starter application.
 */
@SpringBootApplication(
        scanBasePackages = {"org.odpi.openmetadata"}
)
public class OMAGServer implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(OMAGServer.class);
    private final ConfigurableApplicationContext context;
    private final OMAGServerOperationalServices operationalServices;
    private OMAGConfigHelper configHelper;

    /**
     * Constructor injecting the beans required.

     */
    @Autowired
    public OMAGServer(ConfigurableApplicationContext ctx, OMAGConfigHelper configHelper, OMAGServerOperationalServices omagServerOperationalServices) {
        this.context = ctx;
        this.operationalServices = omagServerOperationalServices;
        this.configHelper = configHelper;
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
            activateOMAGServer();
        } catch (OMAGServerActivationError e) {
            LOG.error("Server activation failed due to internal application error", e);
            /**
             *  Any exception captured at this point means that there was problem activating the OMAG server, thus the application should be shut down.
             *  Propagating the error further does not properly stop the application, instead we are explicitly exiting the application and jvm. //TODO: Check if this is the most optimal solution.
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
        if (configHelper.getOmagServerConfig() != null) {
            LOG.info("Application stopped, deactivating server: {}", configHelper.getOmagServerConfig().getLocalServerName());
            operationalServices.deactivateTemporarilyServerList(configHelper.getOmagServerConfig().getLocalServerUserId(), List.of(configHelper.getOmagServerConfig().getLocalServerName()));
        }
    }

    /**
     * This method activates OMAGServer instance using OMAG platform operational services.
     * @see OMAGServerOperationalServices
     *
     * The activation process requires OMAGServerConfig document.
     * @see OMAGServerConfig
     *
     * The OMAGServerConfig document location is provided as org.springframework.core.io.Resource and configured by application property `omag.server-config`.
     *
     * @throws OMAGServerActivationError
     */
    private void activateOMAGServer() throws OMAGServerActivationError {

        LOG.debug("Activation started");

        configHelper.loadConfig();

        if (configHelper.getOmagServerConfig() == null) {
            LOG.info("Activation failed, the cause is that the OMAGServerConfig document is null");
            throw new OMAGServerActivationError("Activation failed, the cause is that the OMAGServerConfig document is null");
        }

        LOG.info("Sending activation request for server: {} and user: {}", configHelper.getOmagServerConfig().getLocalServerName(), configHelper.getOmagServerConfig().getLocalServerUserId());

        SuccessMessageResponse response = operationalServices
                .activateWithSuppliedConfig(configHelper.getOmagServerConfig().getLocalServerUserId(), configHelper.getOmagServerConfig().getLocalServerName(), configHelper.getOmagServerConfig());

        if (response == null) {
            LOG.info("Activation has failed. The cause is that response is null");
            throw new OMAGServerActivationError("Activation has failed. The cause is that response is null");
        }

        if (response.getRelatedHTTPCode() != 200) {
            LOG.info("Activation has failed with response code: {}", response.getRelatedHTTPCode());
            throw new OMAGServerActivationError(String.format("Server activation failed with response code %s", response.getRelatedHTTPCode()));
        }

        if (response.getRelatedHTTPCode() == 200) {
            LOG.info("Activation succeeded for server: {}", configHelper.getOmagServerConfig().getLocalServerName());
        }

    }

}
