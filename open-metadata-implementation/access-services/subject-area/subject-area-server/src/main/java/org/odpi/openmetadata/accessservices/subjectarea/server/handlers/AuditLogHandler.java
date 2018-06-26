/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.server.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


/**
 * AuditLogHandler manages the logging of audit records for the asset.
 */
public class AuditLogHandler
{
    private String                  serviceName;
    private OMRSRepositoryConnector repositoryConnector;



    /**
     * Construct the audit log handler with a link to the property org.odpi.openmetadata.accessservices.subjectarea.server's connector and this access service's
     * official name.
     *
     * @param serviceName - name of this service
     * @param repositoryConnector - connector to the property org.odpi.openmetadata.accessservices.subjectarea.server.
     */
    public AuditLogHandler(String                  serviceName,
                           OMRSRepositoryConnector repositoryConnector)
    {
        this.serviceName = serviceName;
        this.repositoryConnector = repositoryConnector;
    }


    /**
     * Creates an Audit log record for the asset.  This log record is stored in the Asset's Audit Log.
     *
     * @param userId - String - userId of user making request.
     * @param assetGUID - String - unique id for the asset.
     * @param connectorInstanceId - String - (optional) id of connector in use (if any).
     * @param connectionName - String - (optional) name of the connection (extracted from the connector).
     * @param connectorType - String - (optional) type of connector in use (if any).
     * @param contextId - String - (optional) function name, or processId of the activity that the caller is performing.
     * @param message - log record content.
     *
     * @throws InvalidParameterException - one of the parameters is null or invalid.
     * @throws PropertyServerException - There is a problem adding the asset properties to
     *                                   the property org.odpi.openmetadata.accessservices.subjectarea.server.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
//    public void  addLogMessageToAsset(String      userId,
//                                      String      assetGUID,
//                                      String      connectorInstanceId,
//                                      String      connectionName,
//                                      String      connectorType,
//                                      String      contextId,
//                                      String      message) throws InvalidParameterException,
//                                                                  PropertyServerException,
//                                                                  UserNotAuthorizedException
//    {
//        // TODO
//    }


    /**
     * Check that there is a repository connector.
     *
     * @param methodName - name of the method being called
     * @return metadata collection that provides access to the properties in the property org.odpi.openmetadata.accessservices.subjectarea.server
     * @throws PropertyServerException - exception thrown if the repository connector
     */
    private OMRSMetadataCollection validateRepositoryConnector(String   methodName) throws PropertyServerException
    {
        if (this.repositoryConnector == null)
        {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.OMRS_NOT_INITIALIZED;
            String        errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());

        }

        if (! this.repositoryConnector.isActive())
        {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.OMRS_NOT_AVAILABLE;
            String        errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }

        try
        {
            return repositoryConnector.getMetadataCollection();
        }
        catch (Throwable error)
        {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.NO_METADATA_COLLECTION;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }
}
