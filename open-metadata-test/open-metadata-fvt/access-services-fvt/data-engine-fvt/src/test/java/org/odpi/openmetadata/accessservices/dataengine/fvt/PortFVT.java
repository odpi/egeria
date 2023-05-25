/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.fvt;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.accessservices.dataengine.RepositoryService;
import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
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

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Holds FVTs related to PortImplementation type
 */
public class PortFVT extends DataEngineFVT{

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void upsertAndDeletePortImplementation(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {

        Process process = processSetupService.createOrUpdateSimpleProcess(userId, dataEngineClient, null);
        PortImplementation portImplementation = portSetupService
                .createOrUpdatePortImplementation(userId, dataEngineClient, getPortImplementationToUpsertAndDelete(), process.getQualifiedName());

        // assert Port Implementation
        List<EntityDetail> portImplementations = repositoryService.
                findEntityByPropertyValue(PORT_IMPLEMENTATION_TYPE_GUID, portImplementation.getQualifiedName());
        assertPort(portImplementation, portImplementations);

        //update Port Implementation
        String newSuffix = "-new";
        portImplementation.setDisplayName(process.getDisplayName() + newSuffix);
        portImplementation.setPortType(PortType.OUTPUT_PORT);

        PortImplementation updatedPortImplementation = portSetupService
                .createOrUpdatePortImplementation(userId, dataEngineClient, portImplementation, process.getQualifiedName());
        List<EntityDetail> updatedPortImplementations = repositoryService.
                findEntityByPropertyValue(PORT_IMPLEMENTATION_TYPE_GUID, updatedPortImplementation.getQualifiedName());
        EntityDetail updatedPortImplementationAsEntityDetail =
                assertPort(updatedPortImplementation, updatedPortImplementations);

        // delete Port Implementation
        portSetupService.deletePortImplementation(userId, dataEngineClient,
                updatedPortImplementationAsEntityDetail.getProperties().getPropertyValue(QUALIFIED_NAME).valueAsString(),
                updatedPortImplementationAsEntityDetail.getGUID());
        List<EntityDetail> deletedPortImplementations = repositoryService.
                findEntityByPropertyValue(PORT_IMPLEMENTATION_TYPE_GUID, updatedPortImplementation.getQualifiedName());
        assertNull(deletedPortImplementations);
    }

    private PortImplementation getPortImplementationToUpsertAndDelete(){
        PortImplementation portImplementation = new PortImplementation();
        portImplementation.setQualifiedName("to-upsert-and-delete-port-implementation-qualified-name");
        portImplementation.setDisplayName("to-upsert-and-delete-port-implementation-display-name");
        portImplementation.setPortType(PortType.INPUT_PORT);
        portImplementation.setSchemaType(getSchemaType());
        return portImplementation;
    }

    private SchemaType getSchemaType() {
        SchemaType schemaType = new SchemaType();
        schemaType.setQualifiedName("schema-type-qualified-name");
        schemaType.setDisplayName("schema-type-display-name");
        schemaType.setAttributeList(getAttributes());
        return schemaType;
    }

    private List<Attribute> getAttributes() {
        List<Attribute> attributes = new ArrayList<>();

        Attribute attribute = new Attribute();
        attribute.setQualifiedName("attribute-qualified-name");
        attribute.setDisplayName("attribute-display-name");
        attributes.add(attribute);

        return attributes;
    }
}
