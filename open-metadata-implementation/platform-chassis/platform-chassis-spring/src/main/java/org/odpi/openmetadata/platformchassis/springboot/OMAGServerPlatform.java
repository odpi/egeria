/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformchassis.springboot;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.odpi.openmetadata.adminservices.server.OMAGServerAdminStoreServices;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.tokenmanager.http.HTTPRequestHeadersFilter;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataPlatformSecurityVerifier;
import org.odpi.openmetadata.http.HttpHelper;
import org.odpi.openmetadata.serveroperations.rest.SuccessMessageResponse;
import org.odpi.openmetadata.serveroperations.server.OMAGServerOperationalServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import java.util.*;


/**
 * OMAGServerPlatform provides the main program for the OMAG Server Platform.
 */
@SpringBootApplication(
        scanBasePackages = {"${scan.packages:org.odpi.openmetadata.*}"}
)

@OpenAPIDefinition(
        info = @Info(
                title = "Egeria's Open Metadata and Governance (OMAG) Server Platform",
                version = "4.4-SNAPSHOT",
                description = "The OMAG Server Platform provides a runtime process and platform for Open Metadata and Governance (OMAG) Services.\n" +
                        "\n" +
                        "The OMAG services are configured and activated in OMAG Servers using the Administration Services.\n" +
                        "The configuration operations of the admin services create configuration documents, one for each OMAG Server.  " +
                        "Inside a configuration document is the definition of which OMAG services to activate in the server. " +
                        "These include the repository services (any type of server), the access services (for metadata access points " +
                        "and metadata servers), governance services (for governance servers) and view services (for view servers).  " +
                        "Once a configuration document is defined, the OMAG Server can be started and stopped multiple times by " +
                        "the admin services server instance operations.  \n" +
                        "\n" +
                        "The OMAG Server Platform also supports platform services to query details of the servers running on the platform.\n" +
                        "\n" +
                        "The OMAG Server Platform can host multiple OMAG servers at any one time. " +
                        "Each OMAG server is isolated within the server platform and so the OMAG server platform can be used to support multi-tenant " +
                        "operation for a cloud service, " +
                        "or host a variety of different OMAG Servers needed at a particular location.\n" +
                        "\n" +
                        "NOTE: many REST APIs are not guaranteed to be backward-compatible from release to release since they have supported Java clients.  " +
                        "REST APIs may be used for development, testing, evaluation.  Click on the documentation for each module to discover more ...",
                license = @License(name = "Apache-2.0 License", url = "https://www.apache.org/licenses/LICENSE-2.0"),
                contact = @Contact(url = "https://egeria-project.org", name = "Egeria Project",
                                   email = "egeria-technical-discuss@lists.lfaidata.foundation")
        ),

        externalDocs = @ExternalDocumentation(description = "OMAG Server Platform documentation",
                url="https://egeria-project.org/concepts/omag-server-platform/")
        )
public class OMAGServerPlatform
{
    @Value("${strict.ssl:true}") // Default value is true
    Boolean strictSSL;

    @Value("${startup.user:system}") // Default value is "system"
    String sysUser;

    @Value("${startup.server.list:}") // Default value is zero length string
    String startupServers;

    @Value("${authn.header.name.list:}") // Default value is zero length string
    List<String> headerNames;

    @Value("${platform.configstore.provider:}") // Default value is zero length string
    String configStoreProvider;

    @Value("${platform.configstore.endpoint:}") // Default value is zero length string
    String configStoreEndpoint;

    @Value("${platform.security.provider:}") // Default value is zero length string
    String platformSecurityProvider;
    @Value("${platform.security.name:}") // Default value is zero length string
    String platformSecurityName;

    @Autowired
    private Environment env;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private boolean triggeredRuntimeHalt = false;
    private       String                        startupMessage      = "";
    private final OMAGServerOperationalServices operationalServices = new OMAGServerOperationalServices();
    private final OMAGServerAdminStoreServices  configStoreServices = new OMAGServerAdminStoreServices();

    private static final Logger log = LoggerFactory.getLogger(OMAGServerPlatform.class);


    /**
     * Java main
     * @param args program arguments used to override environment variables
     */
    public static void main(String[] args)
    {
        SpringApplication.run(OMAGServerPlatform.class, args);
    }


    /**
     * Make adjustments to the local environment.
     *
     * @return bean
     */
    @Bean
    public InitializingBean getInitialize()
    {
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

    /**
     * Extract the list of servers to auto start along with the administration userId.
     * The userId is in property "sysUser" and the list of server names are in property
     * "startupServers".  If either are null then no servers are auto started.
     */
    List<String>  getAutoStartList()
    {
        if (!startupServers.trim().isEmpty())
        {
            String[] splits = startupServers.split(",");
            List<String> serverList = new ArrayList<>();

            for (String serverName : splits)
            {
                if ((serverName != null) && (! serverName.trim().isEmpty()))
                {
                    serverList.add(serverName.trim());
                }
            }

            if (! serverList.isEmpty())
            {
                return serverList;
            }
        }

        return null;
    }


    /**
     * Starts the servers specified in the startup.server.list property
     */
    private void autoStartConfig()
    {
        List<String>  servers = getAutoStartList();

        if (servers != null)
        {
            log.info("Startup detected for servers: {}", startupServers);
        }

        SuccessMessageResponse response = operationalServices.activateServerListWithStoredConfig(sysUser.trim(), servers);

        if (response.getRelatedHTTPCode() == 200)
        {
            startupMessage = response.getSuccessMessage();
        }
        else
        {
            startupMessage = "Server startup failed with error: " + response.getExceptionErrorMessage();

            StartupFailEvent customSpringEvent = new StartupFailEvent(this, startupMessage);
            applicationEventPublisher.publishEvent(customSpringEvent);
            triggeredRuntimeHalt = true;
        }
    }


    /**
     * Deactivate all servers that were started automatically
     */
    private void temporaryDeactivateServers()
    {
        List<String>  servers = getAutoStartList();

        if (servers != null)
        {
            log.info("Temporarily deactivating any auto-started servers '{}'", servers);

            System.out.println(new Date() + " OMAG Server Platform shutdown requested. Shutting down auto-started servers (if running): " + servers);

            operationalServices.deactivateTemporarilyServerList(sysUser, servers);
        }
    }


    /**
     * ApplicationContextListener detects various events during the lifetime of the OMAGServerPlatform run.
     */
    @Component
    public class ApplicationContextListener
    {

        /**
         * Print out message to say that the platform is ready.
         */
        @EventListener(ApplicationReadyEvent.class)
        public void applicationReady()
        {
            try
            {
                if ((configStoreProvider != null) && (! configStoreProvider.isEmpty()))
                {
                    Connection    configStoreConnection = new Connection();
                    ConnectorType connectorType         = new ConnectorType();
                    connectorType.setConnectorProviderClassName(configStoreProvider);

                    configStoreConnection.setConnectorType(connectorType);

                    if (configStoreEndpoint != null)
                    {
                        Endpoint endpoint = new Endpoint();
                        endpoint.setAddress(configStoreEndpoint);

                        configStoreConnection.setEndpoint(endpoint);
                    }

                    configStoreServices.setConfigurationStoreConnection(sysUser, configStoreConnection);
                }

                if ((platformSecurityProvider != null) && (! platformSecurityProvider.isEmpty()))
                {
                    Connection    securityConnection = new Connection();
                    ConnectorType connectorType      = new ConnectorType();
                    connectorType.setConnectorProviderClassName(platformSecurityProvider);

                    securityConnection.setConnectorType(connectorType);

                    String platformName = platformSecurityName;
                    if ((platformName == null) || (platformName.isEmpty()))
                    {
                        platformName = Long.valueOf(ProcessHandle.current().pid()).toString();
                    }

                    OpenMetadataPlatformSecurityVerifier.setPlatformSecurityConnection(sysUser,
                                                                                       platformName,
                                                                                       securityConnection);
                }
            }
            catch (Exception error)
            {
                log.error("Unable to set up platform connectors", error);
            }

            autoStartConfig();
            System.out.println(OMAGServerPlatform.this.startupMessage);

            if (triggeredRuntimeHalt)
            {
                Runtime.getRuntime().halt(43);
            }
            System.out.println(new Date() + " OMAG server platform ready for more configuration");
        }


        /**
         * Detect platform shutdown.
         *
         * @param event unused event information
         */
        @EventListener
        public void onApplicationEvent(ContextClosedEvent event)
        {
            temporaryDeactivateServers();
        }
    }

    /**
     * Detect failures
     */
    @Component
    public class CustomSpringEventListener implements ApplicationListener<StartupFailEvent>
    {
        @Override
        public void onApplicationEvent(StartupFailEvent event)
        {
            log.info("Received startup fail event with message: {} " + event.getMessage());
            temporaryDeactivateServers();
        }
    }

    /**
     * Initialization of HTTPRequestHeadersFilter. headerNames is a list of headers defined in application properties.
     * @return bean of an initialized FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean<HTTPRequestHeadersFilter> getRequestHeadersFilter()
    {
        FilterRegistrationBean<HTTPRequestHeadersFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new HTTPRequestHeadersFilter(headerNames));
        registrationBean.addUrlPatterns("/servers/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
