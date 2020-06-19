/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot;

import org.odpi.openmetadata.accessservices.assetcatalog.AssetCatalog;
import org.odpi.openmetadata.accessservices.glossaryview.client.GlossaryViewClient;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.governanceservers.openlineage.client.OpenLineageClient;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.AuthService;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.SessionAuthService;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.TokenAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@SpringBootApplication
@ComponentScan({"org.odpi.openmetadata.*"})
@Configuration
public class EgeriaUIPlatform {

    private static final Logger LOG = LoggerFactory.getLogger(EgeriaUIPlatform.class);
    @Autowired
    private Environment env;

    @Value("${strict.ssl}")
    Boolean strictSSL;

    public static void main(String[] args) {
        SpringApplication.run(EgeriaUIPlatform.class, args);
    }

    @Bean
    public InitializingBean getInitialize()
    {
        return () -> {
            // TODO: This section will be removed once the new implementation is complete and tested
            if (!strictSSL) {
                LOG.warn("strict.ssl is *DEPRECATED*! Please use egeria.ssl.client.verify system property or environment variable. If already set they will take precedence over strict.ssl. Note also that strict.ssl=true is the same as egeria.ssl.client.verify=false, and in the new implementation only affects client interaction");
                System.setProperty("egeria.ssl.client.verify", "false"); // inverse of strictSSL
            }
        };
    }


    @Bean
    public AssetCatalog getAssetCatalog(@Value("${omas.server.url}") String serverUrl,
                                        @Value("${omas.server.name}") String serverName) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        return new AssetCatalog(serverName, serverUrl);
    }

    @Bean
    public SubjectArea getSubjectArea(@Value("${omas.server.url}") String serverUrl,
                                      @Value("${omas.server.name}") String serverName) throws InvalidParameterException {
        return new SubjectAreaImpl(serverName, serverUrl);
    }

    @Bean
    public GlossaryViewClient getGlossaryViewClient(@Value("${omas.server.url}") String serverUrl,
                                             @Value("${omas.server.name}") String serverName) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        return new GlossaryViewClient(serverName, serverUrl);
    }

    @Bean
    public OpenLineageClient getOpenLineage(@Value("${open.lineage.server.url}") String serverUrl,
                                            @Value("${open.lineage.server.name}") String serverName) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        return new OpenLineageClient(serverName, serverUrl);
    }

    @Bean
    public AuthService getAuthService(@Value("${authentication.mode}") String authenticationMode)  {
        if(null == authenticationMode || authenticationMode.isEmpty() || "token".equals(authenticationMode)){
            return new TokenAuthService();
        }
        return new SessionAuthService();
    }

    @PostConstruct
    private void configureTrustStore() {

        //making sure truststore was not set using JVM options
        // and strict.ssl is true ( if false, truststore will ignored anyway )
        // TODO: further security cleanup - for now we'll inject this into environment for our SSL code to use
        // We are saving the server specific spring setting for potential later use by the rest client code
        System.setProperty("server.ssl.trust-store", env.getProperty("server.ssl.trust-store"));
        System.setProperty("server.ssl.trust-store-password", env.getProperty("server.ssl.trust-store-password"));
    }
}



