/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
/**
 * This is the interface for the generic operations on data virtualization solutions
 */
package org.odpi.openmetadata.governanceservers.virtualizationservices.viewgenerator;

import org.odpi.openmetadata.accessservices.informationview.events.TableContextEvent;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;

import java.util.Map;

public class ViewGeneratorConnectorBase extends ConnectorBase implements ViewGenerationInterface, AuditLoggingComponent
{
    protected AuditLog auditLog;


    /**
     * Process the serialized  information view event
     *
     * @param tableContextEvent event
     * @return the table sent to Gaian
     */
    @Override
    public Map<String, String> processInformationViewEvent(TableContextEvent tableContextEvent) {
        /*Do Nothing*/
        return null;
    }


    /**
     * Save the instance of the Audit Log for this connector
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog auditLog) {
        this.auditLog = auditLog;
    }
}
