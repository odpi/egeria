/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.odpi.openmetadata.accessservices.assetcatalog.AssetCatalog;
import org.odpi.openmetadata.accessservices.glossaryview.client.GlossaryViewClient;
import org.odpi.openmetadata.governanceservers.openlineage.client.OpenLineageClient;
import org.odpi.openmetadata.http.HttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication(
        scanBasePackages = {"${scan.packages}"}
)

@OpenAPIDefinition(
        info = @Info(
                title = "Egeria's Spring Boot based UI RESTful web services API",
                version = "4.3",
                description = "",
                license = @License(name = "Apache-2.0 License", url = "https://www.apache.org/licenses/LICENSE-2.0"),
                contact = @Contact(url = "https://egeria-project.org", name = "Egeria Project",
                        email = "egeria-technical-discuss@lists.lfaidata.foundation")
        ),

        externalDocs = @ExternalDocumentation(description = "Egeria documentation",
                url="https://egeria-project.org")
)
public class EgeriaUIPlatform {

    private static final Logger LOG = LoggerFactory.getLogger(EgeriaUIPlatform.class);
    @Autowired
    private Environment env;

    @Value("${strict.ssl}")
    Boolean strictSSL;

    @Value("${cors.allowed-origins}")
    String[] allowedOrigins;

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
            }else if( System.getProperty("javax.net.ssl.trustStore")==null) {
                //load the 'javax.net.ssl.trustStore' and
                //'javax.net.ssl.trustStorePassword' from application.properties
                System.setProperty("javax.net.ssl.trustStore", env.getProperty("server.ssl.trust-store"));
                System.setProperty("javax.net.ssl.trustStorePassword", env.getProperty("server.ssl.trust-store-password"));
                System.out.println("test");
            }
        };
    }

    @Bean
    public AssetCatalog getAssetCatalog(@Value("${omas.server.url}") String serverUrl,
                                        @Value("${omas.server.name}") String serverName) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        return new AssetCatalog(serverName, serverUrl);
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

//    @Bean
//    public AuthService getAuthService(@Value("${authentication.mode:token}") String authenticationMode)  {
//        if( "token".equals(authenticationMode) ){
//            return new TokenAuthService();
//        }else if( "redis".equals(authenticationMode) ){
//            return new RedisAuthService();
//        }
//        return new SessionAuthService();
//    }
//
//    @Bean(value = "tokenClient")
//    @ConditionalOnProperty(value = "authentication.mode", havingValue = "token", matchIfMissing = true)
//    public TokenClient stateLessTokenClient(){
//        return new TokenClient() {
//        };
//    }

}



