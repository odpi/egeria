/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot;

import org.odpi.openmetadata.adminservices.OMAGServerOperationalServices;
import org.odpi.openmetadata.adminservices.rest.SuccessMessageResponse;
import org.odpi.openmetadata.http.HttpHelper;
import org.odpi.openmetadata.platformservices.server.OMAGServerPlatformActiveServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.stereotype.Component;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@SpringBootApplication
@ComponentScan({"org.odpi.openmetadata.*"})
@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories("org.odpi.openmetadata.userinterface.security.springboot.repository")
@EntityScan("org.odpi.openmetadata.userinterface.security.springboot.domain")
@EnableSwagger2
public class EgeriaUIPlatform {
    @Value("${strict.ssl}")
    Boolean strictSSL;

    @Value("${startup.user}")
    String sysUser;

    @Value("${startup.server.list}")
    String startupServers;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private boolean triggeredRuntimeHalt = false;
    private String startupMessage = "";
    private OMAGServerOperationalServices operationalServices = new OMAGServerOperationalServices();
    OMAGServerPlatformActiveServices platformActiveServices = new OMAGServerPlatformActiveServices();

    private static final Logger log = LoggerFactory.getLogger(EgeriaUIPlatform.class);

    public static void main(String[] args) {
        SpringApplication.run(EgeriaUIPlatform.class, args);
    }

    @Bean
    public InitializingBean getInitialize() {
        return () -> {
            if (!strictSSL) {
                log.warn("strict.ssl is set to false! Invalid certificates will be accepted for connection!");
                HttpHelper.noStrictSSL();
            }

        };
    }
    /**
     * starts the servers specified in the startup.server.list property
     */
    private void autoStartConfig() {
        if (!sysUser.trim().isEmpty() && !startupServers.trim().isEmpty()) {
            log.info("Startup detected for servers: {}", startupServers);
            String[] splits = startupServers.split(",");
            //remove eventual duplicates
            HashSet<String> servers = new HashSet<>(Arrays.asList(splits));

            for (String server : servers) {
                SuccessMessageResponse response = operationalServices.activateWithStoredConfig(sysUser, server);
                if (response.getRelatedHTTPCode() == 200) {
                    startupMessage += "UI Server '" + server + "' SUCCESS start , with message: " +
                            response.getSuccessMessage() + System.lineSeparator();
                } else {
                    startupMessage += "UI Server '" + server + "' ERROR while startup, with error message: " +
                            response.getExceptionErrorMessage() + System.lineSeparator();
                    StartupFailEvent customSpringEvent =
                            new StartupFailEvent(this, "server " + server + " startup failed.");
                    applicationEventPublisher.publishEvent(customSpringEvent);
                    triggeredRuntimeHalt = true;
                    break;
                }
            }

        } else {
            log.info("No UI server in startup configuration");
            startupMessage = "No UI server in startup configuration";
        }
    }

    /**
     *  Read all the active servers in the platform and call for temporary deactivate
     */
    private void temporaryDeactivateAllServers(){
        List<String> activeServers = platformActiveServices.getActiveServerList(sysUser).getServerList();
        if(activeServers != null){
            activeServers.forEach(server -> {
                log.info("Temporary deactivate the server '{}'",server);
                operationalServices.deactivateTemporarily(sysUser , server);
            });
        }
    }

    /**
     *
     * @return Swagger documentation bean used to show API documentation
     */
    @Bean
    public Docket swaggerDocumentationAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }


    @Component
    public class ApplicationContextListener  {

        @EventListener
        public void onApplicationEvent(ContextRefreshedEvent event) {
            System.out.println();
            System.out.println(EgeriaUIPlatform.this.startupMessage);
            if(triggeredRuntimeHalt){
                Runtime.getRuntime().halt(43);
            }
            System.out.println(new Date().toString() + " UI server platform ready for more configurations");
        }

        @EventListener
        public void onApplicationEvent(ContextClosedEvent event) {
            System.out.println("Context closed received. Temporary deactivating servers");
            temporaryDeactivateAllServers();
        }
    }

    @Component
    public class CustomSpringEventListener implements ApplicationListener<StartupFailEvent> {
        @Override
        public void onApplicationEvent(StartupFailEvent event) {
            log.info("Received startup fail event with message: {} " + event.getMessage());
            temporaryDeactivateAllServers();
        }

    }



}
