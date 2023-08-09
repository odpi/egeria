/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer;

import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.ConnectionElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.ConnectorTypeElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DatabaseElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.EndpointElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.ConnectionProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.EndpointProperties;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests.Jdbc;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests.Omas;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import java.util.List;
import java.util.function.Consumer;

/**
 * If a connector type is stored in the omas, triggers the building of the asset connection structure for provided database.
 * This structure consists of a Connection, a ConnectorType and an Endpoint. For further details on how to store a connector
 * type consult section
 * @see <a href="https://egeria-project.org/concepts/open-metadata-archive/#storage-structures">Open Metadata Archives</>
 */
class CreateConnectionStructure implements Consumer<DatabaseElement> {

    private final Omas omas;
    private final Jdbc jdbc;
    private final String connectorTypeQualifiedName;
    private final AuditLog auditLog;

    CreateConnectionStructure(Omas omas, Jdbc jdbc, String connectorTypeQualifiedName, AuditLog auditLog) {
        this.omas = omas;
        this.jdbc = jdbc;
        this.connectorTypeQualifiedName = connectorTypeQualifiedName;
        this.auditLog = auditLog;
    }

    /**
     * Triggers creation of database connection structure, if all information is readily available. If any part is missing,
     * nothing will be created
     *
     * @param databaseElement database
     */
    @Override
    public void accept(DatabaseElement databaseElement) {
        // determining database guid
        String databaseGuid = databaseElement.getElementHeader().getGUID();
        if(StringUtils.isBlank(databaseGuid)){
            auditLog.logMessage("Missing database guid. Skipping database to connection relationship", null);
            return;
        }

        // determining connection guid
        ConnectionProperties connectionProperties = createConnectionProperties(databaseElement);
        String connectionGuid = determineConnectionGuid(connectionProperties);
        if(StringUtils.isBlank(connectionGuid)){
            auditLog.logMessage("Missing connection guid. Skipping database to connection relationship", null);
            return;
        }

        // determining endpoint guid
        EndpointProperties endpointProperties = createEndpointProperties(connectionProperties);
        String endpointGuid = determineEndpointGuid(endpointProperties);
        if(StringUtils.isBlank(endpointGuid)){
            auditLog.logMessage("Missing endpoint guid. Skipping connection to endpoint setup", null);
            return;
        }

        // determining connector type guid
        if(StringUtils.isBlank(connectorTypeQualifiedName)){
            auditLog.logMessage("Missing connector type qualified name. Skipping connection to connector type relationship", null);
            return;
        }
        String connectorTypeGuid = determineConnectorTypeGuid(connectorTypeQualifiedName);
        if(StringUtils.isBlank(connectorTypeGuid)){
            auditLog.logMessage("Missing connector type guid. Skipping connection to connector type relationship", null);
            return;
        }

        omas.setupAssetConnection(databaseGuid, databaseElement.getDatabaseProperties().getDescription(), connectionGuid);
        omas.setupEndpoint(connectionGuid, endpointGuid);
        omas.setupConnectorType(connectionGuid, connectorTypeGuid);

        auditLog.logMessage("Asset connection structure completed", null);
    }

    /**
     * Create connection properties
     *
     * @param databaseElement database
     *
     * @return connection properties
     */
    private ConnectionProperties createConnectionProperties(DatabaseElement databaseElement){
        ConnectionProperties connectionProperties = new ConnectionProperties();
        connectionProperties.setDisplayName(databaseElement.getDatabaseProperties().getDisplayName() + " Connection");
        connectionProperties.setQualifiedName(databaseElement.getDatabaseProperties().getQualifiedName() + "::connection");
        connectionProperties.setConfigurationProperties(databaseElement.getDatabaseProperties().getExtendedProperties());

        return connectionProperties;
    }

    /**
     * Determine connection guid
     *
     * @param connectionProperties properties
     *
     * @return guid otherwise null
     */
    private String determineConnectionGuid(ConnectionProperties connectionProperties){
        List<ConnectionElement> connections = omas.getConnectionsByName(connectionProperties.getQualifiedName());
        if(connections.isEmpty()){
            return omas.createConnection(connectionProperties).orElse("");
        }else{
            if(connections.size() == 1){
                return connections.get(0).getElementHeader().getGUID();
            }
        }

        return null;
    }

    /**
     * Determine connector type guid
     *
     * @param connectorTypeQualifiedName properties
     *
     * @return guid otherwise null
     */
    private String determineConnectorTypeGuid(String connectorTypeQualifiedName){
        List<ConnectorTypeElement> connectorTypes = omas.getConnectorTypesByName(connectorTypeQualifiedName);
        if(connectorTypes.size() == 1){
            return connectorTypes.get(0).getElementHeader().getGUID();
        }
        return null;
    }

    /**
     * Create endpoint properties
     *
     * @param connectionProperties connection properties
     *
     * @return endpoint properties
     */
    private EndpointProperties createEndpointProperties(ConnectionProperties connectionProperties){
        EndpointProperties endpointProperties = new EndpointProperties();
        endpointProperties.setDisplayName(connectionProperties.getDisplayName() + " Endpoint");
        endpointProperties.setQualifiedName(connectionProperties.getQualifiedName()+"::endpoint");
        endpointProperties.setAddress(jdbc.getUrl());

        return endpointProperties;
    }

    /**
     * Determine endpoint guid
     *
     * @param endpointProperties properties
     *
     * @return guid otherwise null
     */
    private String determineEndpointGuid(EndpointProperties endpointProperties){
        List<EndpointElement> endpoints = omas.findEndpoints(endpointProperties.getQualifiedName());
        if(endpoints.isEmpty()){
            return omas.createEndpoint(endpointProperties).orElse("");
        }else{
            if(endpoints.size() == 1) {
                return endpoints.get(0).getElementHeader().getGUID();
            }
        }
        return null;
    }

}
