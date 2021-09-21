/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.fvt;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.accessservices.dataengine.RepositoryService;
import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
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

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Holds FVTs related to type Process
 */
public class ProcessFVT extends DataEngineFVT{

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void verifyProcessAndUpdate(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {

        Process process = processSetupService.createOrUpdateSimpleProcess(userId, dataEngineClient, null);

        List<EntityDetail> processes = repositoryService.findEntityByPropertyValue(PROCESS_TYPE_GUID, process.getQualifiedName());
        assertProcess(process, processes);

        //update process
        String newSuffix = "-new";
        process.setDisplayName(process.getDisplayName() + newSuffix);
        process.setName(process.getName() + newSuffix);
        process.setDescription(process.getDescription() + newSuffix);

        Process updatedProcess = processSetupService.createOrUpdateSimpleProcess(userId, dataEngineClient, process);

        List<EntityDetail> updatedProcesses =
                repositoryService.findEntityByPropertyValue(PROCESS_TYPE_GUID, updatedProcess.getQualifiedName());
        assertProcess(updatedProcess, updatedProcesses);
    }

    @ParameterizedTest
    @MethodSource("org.odpi.openmetadata.accessservices.dataengine.PlatformConnectionProvider#getConnectionDetails")
    public void deleteProcess(String userId, DataEngineClient dataEngineClient, RepositoryService repositoryService)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {

        Process process = processSetupService.createOrUpdateSimpleProcess(userId, dataEngineClient, getProcessToDelete());

        List<EntityDetail> processes = repositoryService.findEntityByPropertyValue(PROCESS_TYPE_GUID, process.getQualifiedName());
        EntityDetail processAsEntityDetail = assertProcess(process, processes);

        //delete Process
        processSetupService.deleteProcess(userId, dataEngineClient, process.getQualifiedName(), processAsEntityDetail.getGUID());
        List<EntityDetail> processesToDelete = repositoryService.findEntityByPropertyValue(PROCESS_TYPE_GUID, process.getQualifiedName());
        assertNull(processesToDelete);
    }

    private Process getProcessToDelete() {
        Process process;
        process = new Process();
        process.setQualifiedName("to-delete-simple-process-qualified-name");
        process.setDisplayName("to-delete-simple-process-display-name");
        process.setName("to-delete-simple-process-name");
        process.setDescription("to-delete-simple-process-description");
        process.setOwner("to-delete-simple-process-owner");
        return process;
    }

}
