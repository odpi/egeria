/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.handlers;


import org.odpi.openmetadata.accessservices.communityprofile.properties.ContactMethod;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * UserIdentityHandler is responsible for the management of UserIdentity type entities.
 */
public class ContactMethodHandler
{
    private String                  serviceName;
    private String                  serverName;
    private RepositoryErrorHandler  errorHandler;
    private RepositoryHandler       repositoryHandler;
    private OMRSRepositoryHelper    repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;


    private static final Logger log = LoggerFactory.getLogger(ContactMethodHandler.class);


    /**
     * Construct the discovery engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName name of the consuming service
     * @param serverName name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper helper used by the converters
     * @param repositoryHandler handler for calling the repository services
     * @param errorHandler handler for repository service errors
     */
    public ContactMethodHandler(String                  serviceName,
                                String                  serverName,
                                InvalidParameterHandler invalidParameterHandler,
                                OMRSRepositoryHelper    repositoryHelper,
                                RepositoryHandler       repositoryHandler,
                                RepositoryErrorHandler  errorHandler)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.errorHandler = errorHandler;
        this.repositoryHandler = repositoryHandler;
    }


    /**
     * Return all of the contact details associated with the identified profile.
     *
     * @param userId calling user
     * @param profileGUID unique identifier of the profile
     * @param methodName calling method
     *
     * @return list of contact method beans
     * @throws InvalidParameterException the userId of the guid is null
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<ContactMethod>  getContactMethods(String   userId,
                                           String   profileGUID,
                                           String   methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        // todo
        return null;
    }
}
