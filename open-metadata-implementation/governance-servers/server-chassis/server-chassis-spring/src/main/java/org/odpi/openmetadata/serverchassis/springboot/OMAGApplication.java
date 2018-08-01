/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.serverchassis.springboot;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan({"org.odpi.openmetadata.*"})
@EnableSwagger2
@Configuration

public class OMAGApplication
{
    public static void main(String[] args)
    {
        BasicConfigurator.configure();

        SpringApplication.run(OMAGApplication.class, args);
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

}
