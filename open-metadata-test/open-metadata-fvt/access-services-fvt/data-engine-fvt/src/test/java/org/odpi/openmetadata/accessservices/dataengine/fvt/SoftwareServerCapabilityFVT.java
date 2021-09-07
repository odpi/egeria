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
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This class holds functional verification tests written with the help of the Junit framework. There are parametrized tests
 * covering the creation of an external data engine source and a whole job process containing stages.
 * Depending on the number of the series of parameters of each test method, the tests will run or not multiple times.
 * The parameters are computed in the method indicated in the @MethodSource annotation.
 */
public class SoftwareServerCapabilityFVT extends DataEngineFVT {

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void registerExternalTool(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, ConnectorCheckedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {

        SoftwareServerCapability softwareServerCapability = softwareServerCapabilitySetupServer.createExternalDataEngine(userId, dataEngineClient);

        List<EntityDetail> entityDetails = repositoryService.findEntityByPropertyValue(SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                softwareServerCapability.getQualifiedName());
        if (entityDetails == null || entityDetails.isEmpty()) {
            fail();
        }

        assertEquals(1, entityDetails.size());
        EntityDetail entity = entityDetails.get(0);
        assertEquals(softwareServerCapability.getDescription(), entity.getProperties().getPropertyValue(DESCRIPTION).valueAsString());
        assertEquals(softwareServerCapability.getName(), entity.getProperties().getPropertyValue(NAME).valueAsString());
        assertEquals(softwareServerCapability.getEngineType(), entity.getProperties().getPropertyValue(DEPLOYED_IMPLEMENTATION_TYPE).valueAsString());
        assertEquals(softwareServerCapability.getEngineVersion(), entity.getProperties().getPropertyValue(VERSION).valueAsString());
        assertEquals(softwareServerCapability.getPatchLevel(), entity.getProperties().getPropertyValue(PATCH_LEVEL).valueAsString());
        assertEquals(softwareServerCapability.getQualifiedName(), entity.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString());
        assertEquals(softwareServerCapability.getSource(), entity.getProperties().getPropertyValue(SOURCE).valueAsString());
    }

}
