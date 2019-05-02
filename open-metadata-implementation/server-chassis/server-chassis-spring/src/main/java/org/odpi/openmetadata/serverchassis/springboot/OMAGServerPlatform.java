/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverchassis.springboot;

import org.odpi.openmetadata.http.HttpHelper;
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

import java.util.Date;

@SpringBootApplication
@ComponentScan({"org.odpi.openmetadata.*"})
@EnableSwagger2
@Configuration
public class OMAGServerPlatform
{
    @Value("${strict.ssl}")
    Boolean strictSSL;

    public static void main(String[] args)
    {
        SpringApplication.run(OMAGServerPlatform.class, args);
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
    public Docket egeriaAPI()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }


    @Component
    public class ApplicationContextListener implements
                                            ApplicationListener<ContextRefreshedEvent>
    {
        @Override
        public void onApplicationEvent(ContextRefreshedEvent event)
        {
            System.out.println(new Date().toString() + " OMAG server platform ready for configuration");
        }

    }

}
