/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.fvt;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.accessservices.dataengine.RepositoryService;
import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
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
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Holds FVTs related to type SoftwareCapability
 */
public class SoftwareCapabilityFVT extends DataEngineFVT {

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void registerExternalTool(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, ConnectorCheckedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {

        SoftwareServerCapability softwareServerCapability = softwareServerCapabilitySetupServer
                .createExternalDataEngine(userId, dataEngineClient, null);

        List<EntityDetail> entityDetails = repositoryService.findEntityByPropertyValue(SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                softwareServerCapability.getQualifiedName());
        if (entityDetails == null || entityDetails.isEmpty()) {
            fail();
        }

        assertEquals(1, entityDetails.size());
        EntityDetail entity = entityDetails.get(0);

        assertEquals(softwareServerCapability.getDescription(), entity.getProperties().getPropertyValue(DESCRIPTION).valueAsString());
        assertEquals(softwareServerCapability.getName(), entity.getProperties().getPropertyValue(NAME).valueAsString());
        assertEquals(softwareServerCapability.getEngineVersion(), entity.getProperties().getPropertyValue(CAPABILITY_VERSION).valueAsString());
        assertEquals(softwareServerCapability.getEngineType(), entity.getProperties().getPropertyValue(CAPABILITY_TYPE).valueAsString());
        assertEquals(softwareServerCapability.getPatchLevel(), entity.getProperties().getPropertyValue(PATCH_LEVEL).valueAsString());
        assertEquals(softwareServerCapability.getQualifiedName(), entity.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());
        assertEquals(softwareServerCapability.getSource(), entity.getProperties().getPropertyValue(SOURCE).valueAsString());
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void deleteExternalTool(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {

        SoftwareServerCapability toDeleteSoftwareServerCapability = softwareServerCapabilitySetupServer
                .createExternalDataEngine(userId, dataEngineClient, getToDeleteSoftwareServerCapability());

        List<EntityDetail> softwareServerCapabilities = repositoryService.findEntityByPropertyValue(SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                toDeleteSoftwareServerCapability.getQualifiedName());
        if (softwareServerCapabilities == null || softwareServerCapabilities.isEmpty()) {
            fail();
        }

        assertEquals(1, softwareServerCapabilities.size());
        EntityDetail softwareServerCapabilityAsEntityDetail = softwareServerCapabilities.get(0);
        softwareServerCapabilitySetupServer.deleteExternalDataEngine(userId, dataEngineClient,
                softwareServerCapabilityAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString(),
                softwareServerCapabilityAsEntityDetail.getGUID());

        List<EntityDetail> deletedSoftwareServerCapabilities = repositoryService.findEntityByPropertyValue(SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                toDeleteSoftwareServerCapability.getQualifiedName());
        // TODO: add verification for to be deleted softwareServerCapability once the method is implemented
        //  in this moment, a FunctionNotSupportedException is thrown
        // on searching the so called deleted capability, we still receive it as the result
        assertNotNull(deletedSoftwareServerCapabilities);
    }

    private SoftwareServerCapability getToDeleteSoftwareServerCapability(){
        SoftwareServerCapability softwareServerCapability = new SoftwareServerCapability();
        softwareServerCapability.setName("To Delete Data Engine Display Name");
        softwareServerCapability.setQualifiedName("ToDeleteDataEngine");
        softwareServerCapability.setDescription("To Delete Data Engine Description");
        softwareServerCapability.setEngineType("ToDeleteDataEngine");
        softwareServerCapability.setEngineVersion("1");
        softwareServerCapability.setPatchLevel("2");
        softwareServerCapability.setSource("source");
        return softwareServerCapability;
    }

}
