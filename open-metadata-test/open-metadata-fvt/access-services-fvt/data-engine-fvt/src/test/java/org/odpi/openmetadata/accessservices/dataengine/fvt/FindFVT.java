/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.fvt;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.accessservices.dataengine.RepositoryService;
import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Holds FVTs related to find
 */
public class FindFVT extends DataEngineFVT {

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void find(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, ConnectorCheckedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {

        engineSetupService.createExternalDataEngine(userId, dataEngineClient, null);
        DataFile dataFile = dataStoreAndRelationalTableSetupService.upsertDataFile(userId, dataEngineClient, null);

        GUIDListResponse guidListResponse = findSetupService.find(userId, dataEngineClient, dataFile.getQualifiedName(),
                null, null);
        assertNotNull(guidListResponse);
        assertEquals(1, guidListResponse.getGUIDs().size());

        List<EntityDetail> dataFiles = repositoryService
                .findEntityByPropertyValue(DATAFILE_TYPE_GUID, dataFile.getQualifiedName());
        assertNotNull(dataFiles);
        assertEquals(1, dataFiles.size());

        EntityDetail dataFileAsEntityDetail = dataFiles.get(0);
        assertEquals(guidListResponse.getGUIDs().get(0), dataFileAsEntityDetail.getGUID());
    }

}
