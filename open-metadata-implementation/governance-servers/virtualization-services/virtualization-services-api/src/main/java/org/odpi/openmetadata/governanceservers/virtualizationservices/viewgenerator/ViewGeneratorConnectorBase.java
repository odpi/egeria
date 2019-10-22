/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
/**
 * This is the interface for the generic operations on data virtualization solutions
 */
package org.odpi.openmetadata.governanceservers.virtualizationservices.viewgenerator;

import org.odpi.openmetadata.accessservices.informationview.events.TableContextEvent;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.auditable.AuditableConnector;

import java.util.Map;

public class ViewGeneratorConnectorBase extends ConnectorBase implements ViewGenerationInterface, AuditableConnector {

    protected OMRSAuditLog omrsAuditLog;
    /**
     * Initialize the connector.
     *
     * @param connectorInstanceId - unique id for the connector instance - useful for messages etc
     * @param connectionProperties - POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties) {
        super.initialize(connectorInstanceId, connectionProperties);
    }

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
     * Pass the instance of OMRS Audit Log
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(OMRSAuditLog auditLog) {
        this.omrsAuditLog = auditLog;
    }


}
