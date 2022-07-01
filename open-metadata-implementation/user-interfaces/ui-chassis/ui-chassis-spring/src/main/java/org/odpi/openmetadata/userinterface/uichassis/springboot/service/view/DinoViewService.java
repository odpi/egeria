/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service.view;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.viewservices.dino.admin.DinoViewAdmin;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DinoViewService extends DinoViewAdmin implements InitializingBean {
    @Value("${viewServiceServerName}")
    private String viewServerName;

    @Value("${viewServiceMaxPageSize:0}")
    private int maxPageSize;

    @Autowired
    IntegrationViewServiceConfigComponent integrationViewServiceConfigComponent;

    @Autowired
    private AuditLogDestinationService auditLogDestinationService;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initialize(
                viewServerName,
                integrationViewServiceConfigComponent,
                getAuditLog(),
                "system",
                maxPageSize
        );
    }
    private AuditLog getAuditLog(){
        AuditLog auditLog = new AuditLog(
                auditLogDestinationService,
                84431,
                ComponentDevelopmentStatus.IN_DEVELOPMENT,
                "dino-view",
                "dino view service",
                "");
        return auditLog;
    }

    @Override
    public void initialize(String serverName, ViewServiceConfig viewServiceConfig, AuditLog auditLog, String serverUserName, int maxPageSize)
        throws OMAGConfigurationErrorException {
        super.initialize(serverName, viewServiceConfig, auditLog, serverUserName, maxPageSize);
    }

}
