/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.server;

import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AuditLogHandler manages the logging of audit records for the asset.
 */
public class AuditLogHandler
{
    private static final Logger log = LoggerFactory.getLogger(AuditLogHandler.class);

    private String                  serviceName;
    private OMRSRepositoryConnector repositoryConnector;



    /**
     * Construct the audit log handler with a link to the property server's connector and this access service's
     * official name.
     *
     * @param serviceName  name of this service
     * @param repositoryConnector  connector to the property server.
     */
    AuditLogHandler(String                  serviceName,
                    OMRSRepositoryConnector repositoryConnector)
    {
        this.serviceName = serviceName;
        this.repositoryConnector = repositoryConnector;
    }


    /**
     * Creates an Audit log record for the asset.  This log record is stored in the Asset's Audit Log.
     *
     * @param userId  String - userId of user making request.
     * @param assetGUID  String - unique id for the asset.
     * @param connectorInstanceId  String - (optional) id of connector in use (if any).
     * @param connectionName  String - (optional) name of the connection (extracted from the connector).
     * @param connectorType  String - (optional) type of connector in use (if any).
     * @param contextId  String - (optional) function name, or processId of the activity that the caller is performing.
     * @param message  log record content.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem adding the asset properties to
     *                                   the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void  addLogMessageToAsset(String      userId,
                                      String      assetGUID,
                                      String      connectorInstanceId,
                                      String      connectionName,
                                      String      connectorType,
                                      String      contextId,
                                      String      message) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String        methodName = "addLogMessageToAsset";

        // TODO
        log.warn(methodName + " not implemented");
    }
}
