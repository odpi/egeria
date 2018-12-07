/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.virtualdataconnector.igc.server;

import org.odpi.openmetadata.http.HttpHelper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
@ComponentScan({"org.odpi.openmetadata.*"})
public class IGCRepositoryProxy {

    @Value("${strict.ssl}")
    Boolean strictSSL;

    public static void main(String[] args) {
        SpringApplication.run(IGCRepositoryProxy.class, args);
    }

    @Bean
    public InitializingBean getInitialize() {
        return () -> {
            if (!strictSSL) {
                HttpHelper.noStrictSSL();
            }
        };
    }


    @Bean
    public Docket egeriaAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
