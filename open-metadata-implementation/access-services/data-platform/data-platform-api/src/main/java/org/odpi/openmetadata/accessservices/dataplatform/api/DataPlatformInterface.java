/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform;

import org.odpi.openmetadata.accessservices.dataplatform.properties.DeployedDatabaseSchema;
import org.odpi.openmetadata.accessservices.dataplatform.properties.SoftwareServerCapability;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * DataPlatformInterface provides the interface for managing information about data assets hosted on a data platform.
 * The DataInfrastructureInterface provides methods for describing the infrastructure of the data platform.
 */
public interface DataPlatformInterface {

    /**
     * Create the software server capability entity for registering data platforms as external source.
     *
     * @param userId                   the name of the calling user
     * @param softwareServerCapability the software server capability bean
     * @return unique identifier of the server in the repository
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    GUIDResponse createExternalDataPlatform(String userId, SoftwareServerCapability softwareServerCapability) throws
            InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException;


    /**
     * Create deployed database schema asset.
     *
     * @param userId                 the user id
     * @param deployedDatabaseSchema the deployed database schema
     * @return the string
     * @throws InvalidParameterException  the invalid parameter exception
     * @throws UserNotAuthorizedException the user not authorized exception
     * @throws PropertyServerException    the property server exception
     */
    GUIDResponse createDeployedDatabaseSchema(String userId, DeployedDatabaseSchema deployedDatabaseSchema)throws
            InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException;

}
