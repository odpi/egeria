/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;

/**
 * GovernanceActionProcessStepHandler manages GovernanceActionProcessStep entities and their relationships.
 */
public class GovernanceActionProcessStepHandler<B> extends OpenMetadataAPIGenericHandler<B>
{

    /**
     * Construct the handler for metadata elements.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param auditLog destination for audit log events.
     */
    public GovernanceActionProcessStepHandler(OpenMetadataAPIGenericConverter<B> converter,
                                              Class<B>                           beanClass,
                                              String                             serviceName,
                                              String                             serverName,
                                              InvalidParameterHandler            invalidParameterHandler,
                                              RepositoryHandler                  repositoryHandler,
                                              OMRSRepositoryHelper               repositoryHelper,
                                              String                             localServerUserId,
                                              OpenMetadataServerSecurityVerifier securityVerifier,
                                              AuditLog                           auditLog)
    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              auditLog);

    }


    /**
     * Retrieve the governance action process step metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param processStepGUID unique identifier of the governance action process step
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getGovernanceActionProcessStepByGUID(String       userId,
                                                  String       processStepGUID,
                                                  Date         effectiveTime,
                                                  String       methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String guidParameterName = "processStepGUID";

        return this.getBeanFromRepository(userId,
                                          processStepGUID,
                                          guidParameterName,
                                          OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                          false,
                                          false,
                                          effectiveTime,
                                          methodName);
    }
}
