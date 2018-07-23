/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.server;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ComponentScan({"org.odpi.openmetadata.*"})
@Configuration

public class IGCRepositoryProxy
{
    public static void main(String[] args)
    {
        BasicConfigurator.configure();

        SpringApplication.run(IGCRepositoryProxy.class, args);
    }
}
