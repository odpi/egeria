/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.fvt;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.accessservices.dataengine.RepositoryService;
import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Holds FVTs related to types Connection and Endpoint
 */
public class ConnectionAndEndpointFVT extends DataEngineFVT {

    private static final String COLON = ":";
    private static final String CONNECTION = " Connection";
    private static final String ENDPOINT = " Endpoint";

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void deleteConnectionAndEndpoint(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {

        engineSetupService.createExternalDataEngine(userId, dataEngineClient, null);
        DataFile dataFile = dataStoreAndRelationalTableSetupService.upsertDataFile(userId, dataEngineClient, getDataFile());

        // assert Connection
        String fileType = dataFile.getFileType();
        String qualifiedName = dataFile.getQualifiedName();
        String connectionQualifiedName = getConnectionQualifiedName(fileType, qualifiedName);
        List<EntityDetail> connections = repositoryService.findEntityByPropertyValue(CONNECTION_TYPE_GUID, connectionQualifiedName);
        assertNotNull(connections);
        assertEquals(1, connections.size());

        EntityDetail connectionAsEntityDetail = connections.get(0);
        assertEquals(connectionQualifiedName, connectionAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());

        // assert Endpoint
        String endpointQualifiedName = getEndpointQualifiedName(fileType, qualifiedName);
        List<EntityDetail> endpoints = repositoryService.findEntityByPropertyValue(ENDPOINT_TYPE_GUID, endpointQualifiedName);
        assertNotNull(endpoints);
        assertEquals(1, endpoints.size());

        EntityDetail endpointAsEntityDetail = endpoints.get(0);
        assertEquals(endpointQualifiedName, endpointAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());

        //delete Endpoint
        connectionAndEndpointSetupService.deleteEndpoint(userId, dataEngineClient, endpointQualifiedName,
                endpointAsEntityDetail.getGUID());
        List<EntityDetail> deletedEndpoints = repositoryService.findEntityByPropertyValue(ENDPOINT_TYPE_GUID, endpointQualifiedName);
        assertNull(deletedEndpoints);

        //delete Connection
        connectionAndEndpointSetupService.deleteConnection(userId, dataEngineClient, connectionQualifiedName,
                connectionAsEntityDetail.getGUID());
        List<EntityDetail> deletedConnections = repositoryService.findEntityByPropertyValue(CONNECTION_TYPE_GUID, connectionQualifiedName);
        assertNull(deletedConnections);
    }

    private DataFile getDataFile(){
        DataFile dataFile = new DataFile();
        dataFile.setQualifiedName("connection-and-endpoint-data-file-qualified-name");
        dataFile.setDisplayName("connection-and-endpoint-data-file-display-name");
        dataFile.setDescription("connection-and-endpoint-data-file-description");
        dataFile.setFileType("DataFile");
        dataFile.setProtocol("connection-and-endpoint-data-file-protocol");
        dataFile.setNetworkAddress("connection-and-endpoint-data-file-network-address");
        dataFile.setPathName("/connection-and-endpoint-data-file-pathname/");
        dataFile.setColumns(buildTabularColumns());
        return dataFile;
    }

    private List<Attribute> buildTabularColumns(){
        List<Attribute> columns = new ArrayList<>();

        Attribute column = new Attribute();
        column.setQualifiedName("connection-and-endpoint-column-qualified-name");
        column.setDisplayName("connection-and-endpoint-column-display-name");
        column.setDescription("connection-and-endpoint-column-description");
        columns.add(column);

        return columns;
    }

    private String getConnectionQualifiedName(String assetTypeName, String assetQualifiedName) {
        return assetTypeName + COLON + assetQualifiedName + CONNECTION;
    }

    private String getEndpointQualifiedName(String assetTypeName, String assetQualifiedName) {
        return assetTypeName + COLON + assetQualifiedName + ENDPOINT;
    }

}
