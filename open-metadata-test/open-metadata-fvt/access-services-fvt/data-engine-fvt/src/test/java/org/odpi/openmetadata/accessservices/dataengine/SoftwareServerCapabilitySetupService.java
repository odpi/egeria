/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * Generates test data of type SoftwareCapability, and triggers requests via client for aforementioned type
 */
public class SoftwareServerCapabilitySetupService {

    public SoftwareServerCapabilitySetupService() { }

    /**
     * Registers an external data engine source.
     *
     * @param userId the user which creates the data engine
     * @param dataEngineOMASClient the data engine client that is used to create the external data engine
     * @param softwareServerCapability capability to create. If null, a default will be used
     *
     * @return software server capability containing sent values
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SoftwareServerCapability createExternalDataEngine(String userId, DataEngineClient dataEngineOMASClient,
                                                             SoftwareServerCapability softwareServerCapability)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, ConnectorCheckedException {
        if(softwareServerCapability == null){
            softwareServerCapability = getDefaultSoftwareServerCapability();
        }
        dataEngineOMASClient.createExternalDataEngine(userId, softwareServerCapability);
        return softwareServerCapability;
    }

    /**
     * Delete a SoftwareCapability using the dataEngineClient received
     *
     * @param userId user id
     * @param dataEngineClient data engine client
     * @param qualifiedName qualified name
     * @param guid guid
     */
    public void deleteExternalDataEngine(String userId, DataEngineClient dataEngineClient, String qualifiedName, String guid )
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, ConnectorCheckedException {
        if(qualifiedName == null || guid == null){
            throw new IllegalArgumentException("Unable to delete External Tool. QualifiedName and Guid are both required. Missing at least one");
        }
        dataEngineClient.deleteExternalDataEngine(userId, qualifiedName, guid);
    }

    private SoftwareServerCapability getDefaultSoftwareServerCapability(){
        SoftwareServerCapability softwareServerCapability = new SoftwareServerCapability();
        softwareServerCapability.setName("Data Engine Display Name");
        softwareServerCapability.setQualifiedName("DataEngine");
        softwareServerCapability.setDescription("Data Engine Description");
        softwareServerCapability.setEngineType("DataEngine");
        softwareServerCapability.setEngineVersion("1");
        softwareServerCapability.setPatchLevel("2");
        softwareServerCapability.setSource("source");
        return softwareServerCapability;
    }

}
