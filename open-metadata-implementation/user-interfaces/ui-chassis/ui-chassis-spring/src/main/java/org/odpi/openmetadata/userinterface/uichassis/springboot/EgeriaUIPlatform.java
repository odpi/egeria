/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot;

import org.odpi.openmetadata.accessservices.assetcatalog.AssetCatalog;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaImpl;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.governanceservers.openlineage.client.OpenLineageClient;
import org.odpi.openmetadata.http.HttpHelper;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.AuthService;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.SessionAuthService;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.TokenAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ComponentScan({"org.odpi.openmetadata.*"})
@Configuration
public class EgeriaUIPlatform {

    private static final Logger LOG = LoggerFactory.getLogger(EgeriaUIPlatform.class);

    @Value("${strict.ssl}")
    Boolean strictSSL;

    public static void main(String[] args) {
        SpringApplication.run(EgeriaUIPlatform.class, args);
    }

    @Bean
    public InitializingBean getInitialize()
    {
        return () -> {
            if (!strictSSL)
            {
                HttpHelper.noStrictSSL();
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
}



