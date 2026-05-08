/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * ReferenceableHandler manages methods on generic referenceables.
 */
public class ReferenceableHandler<B> extends OpenMetadataAPITemplateHandler<B>
{
    /**
     * Construct the handler information needed to interact with the repository services
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
    public ReferenceableHandler(OpenMetadataAPIGenericConverter<B> converter,
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
}
