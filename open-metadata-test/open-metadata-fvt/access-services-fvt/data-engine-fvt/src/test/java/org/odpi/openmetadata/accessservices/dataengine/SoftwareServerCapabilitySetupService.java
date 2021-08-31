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
 * The class is used in the DataEngineFVT in order to generate a job process containing stages, port implementations with
 * their schemas and attributes. It creates virtual assets for a CSV file with 4 columns and a database table with the same
 * number of columns. The process contains 3 stages which read from the CSV, rename the columns, then write the values into a
 * database table.
 * The class also helps the setup with creating an external data engine using a SoftwareServerCapability object.
 */
public class SoftwareServerCapabilitySetupService {

    public SoftwareServerCapabilitySetupService() { }

    /**
     * Registers an external data engine source.
     *
     * @param userId               the user which creates the data engine
     * @param dataEngineOMASClient the data engine client that is used to create the external data engine
     *
     * @return the software server capability used to create the external data source that is needed for checks inside the FVT
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SoftwareServerCapability createExternalDataEngine(String userId, DataEngineClient dataEngineOMASClient)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, ConnectorCheckedException {
        SoftwareServerCapability softwareServerCapability = new SoftwareServerCapability();
        softwareServerCapability.setName("Data Engine Display Name");
        softwareServerCapability.setQualifiedName("DataEngine");
        softwareServerCapability.setDescription("Data Engine Description");
        softwareServerCapability.setEngineType("DataEngine");
        softwareServerCapability.setEngineVersion("1");
        softwareServerCapability.setPatchLevel("2");
        softwareServerCapability.setSource("source");
        dataEngineOMASClient.createExternalDataEngine(userId, softwareServerCapability);
        return softwareServerCapability;
    }

}
