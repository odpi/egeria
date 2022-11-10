/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.Engine;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * Generates test data of type SoftwareCapability, and triggers requests via client for aforementioned type
 */
public class EngineSetupService {

    public EngineSetupService() { }

    /**
     * Registers an external data engine source.
     *
     * @param userId the user which creates the data engine
     * @param dataEngineOMASClient the data engine client that is used to create the external data engine
     * @param engine capability to create. If null, a default will be used
     *
     * @return engine containing sent values
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public Engine createExternalDataEngine(String userId, DataEngineClient dataEngineOMASClient,
                                           Engine engine)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, ConnectorCheckedException {
        if(engine == null){
            engine = getDefaultEngine();
        }
        dataEngineOMASClient.createExternalDataEngine(userId, engine);
        return engine;
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

    private Engine getDefaultEngine(){
        Engine engine = new Engine();
        engine.setName("Data Engine Display Name");
        engine.setQualifiedName("DataEngine");
        engine.setDescription("Data Engine Description");
        engine.setEngineType("DataEngine");
        engine.setEngineVersion("1");
        engine.setPatchLevel("2");
        engine.setSource("source");
        return engine;
    }

}
