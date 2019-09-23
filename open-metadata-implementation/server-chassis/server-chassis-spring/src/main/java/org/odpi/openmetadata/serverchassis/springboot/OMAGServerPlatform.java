/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverchassis.springboot;

import org.odpi.openmetadata.adminservices.OMAGServerOperationalServices;
import org.odpi.openmetadata.adminservices.rest.SuccessMessageResponse;
import org.odpi.openmetadata.http.HttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
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
@EnableSwagger2
@Configuration
public class OMAGServerPlatform {
    @Value("${strict.ssl}")
    Boolean strictSSL;

    @Value("${startup.user}")
    String startupUser;

    @Value("${startup.server.list}")
    String startupServers;

    @Autowired
    EventPublisherManager eventPublisher;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private ApplicationContext appContext;


    private String startupMessage = "";
    private OMAGServerOperationalServices operationalServices = new OMAGServerOperationalServices();

    private static final Logger log = LoggerFactory.getLogger(OMAGServerPlatform.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(OMAGServerPlatform.class, args);
    }

    @Bean
    public InitializingBean getInitialize() {
        return () -> {
            if (!strictSSL) {
                log.warn("strict.ssl is set to false! Invalid certificates will be accepted for connection!");
                HttpHelper.noStrictSSL();
            }
            autoStartConfig();
        };
    }

    /**
     * starts the servers specified in the startup.server.list property
     */
    private boolean autoStartConfig() {
        if (!startupUser.trim().isEmpty() && !startupServers.trim().isEmpty()) {
            log.info("Startup detected for servers: {}", startupServers);
            String[] splits = startupServers.split(",");
            //remove eventual duplicates
            HashSet<String> servers = new HashSet<>(Arrays.asList(splits));

            for (String server : servers) {
                SuccessMessageResponse response = operationalServices.activateWithStoredConfig(startupUser, server);
                if (response.getRelatedHTTPCode() == 200) {
                    startupMessage += "OMAG Server '" + server + "' SUCCESS start , with message: " +
                            response.getSuccessMessage() + System.lineSeparator();
                } else {
                    startupMessage += "OMAG Server '" + server + "' ERROR while startup, with error message: " +
                            response.getExceptionErrorMessage() + System.lineSeparator();
                    return false;
                }
            }

        } else {
            log.info("No OMAG server in startup configuration");
            startupMessage = "No OMAG server in startup configuration";
        }
        return true;
    }

    private void temporaryDeactivateServers(){
        List<String> activeServerList = platformAPI.getActiveServerList(startupUser).getServerList();
        for (String server : activeServerList) {
            VoidResponse voidResponse = operationalServices.deactivateTemporarily(startupUser, server);
            if(200 == voidResponse.getRelatedHTTPCode()){
                log.info("OMAG Server '{}' temporary deactivated SUCCESSFULLY  ",server); ;
            } else {
                log.error("OMAG Server '{}' temporary deactivation FAIL with code {}  and message {} ",
                        server,
                        voidResponse.getRelatedHTTPCode(),
                        voidResponse.getExceptionErrorMessage()); ;
            }
        }
    }


    /**
     * @return Swagger documentation bean
     */
    @Bean
    public Docket egeriaAPI() {
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
            System.out.println(OMAGServerPlatform.this.startupMessage);
            System.out.println(new Date().toString() + " OMAG server platform ready for more configurations");
        }

        @EventListener
        public void onApplicationEvent(ContextClosedEvent event) {
            System.out.println("Context closed event received. Calling temporary deactivate the servers...");
            temporaryDeactivateServers();
        }
    }

}
