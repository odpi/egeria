/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan({"org.odpi.openmetadata.accessservices.ui.*"})
public class EgeriaUIApplication {

    private static final Logger LOG = LoggerFactory.getLogger(EgeriaUIApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EgeriaUIApplication.class, args);
    }
}
