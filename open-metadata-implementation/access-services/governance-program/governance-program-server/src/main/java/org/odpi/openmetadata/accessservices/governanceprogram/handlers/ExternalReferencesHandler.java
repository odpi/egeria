/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.handlers;


import org.odpi.openmetadata.accessservices.governanceprogram.properties.ExternalReference;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * ExternalReferencesHandler retrieves external references objects that are linked to a specific entity
 * from the property server.  It runs server-side in the GovernanceProgram
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public class ExternalReferencesHandler
{
    private static final String externalReferenceTypeGUID               = "af536f20-062b-48ef-9c31-1ddd05b04c56";
    private static final String externalReferenceTypeName               = "ExternalReference";
    private static final String externalReferenceIdPropertyName         = "qualifiedName";
    private static final String displayNamePropertyName                 = "displayName";
    private static final String uriPropertyName                         = "url";
    private static final String versionPropertyName                     = "version";
    private static final String resourceDescriptionPropertyName         = "description";
    private static final String organizationPropertyName                = "organization";
    private static final String additionalPropertiesName                = "additionalProperties";


    private static final String externalReferenceLinkTypeGUID           = "7d818a67-ab45-481c-bc28-f6b1caf12f06";
    private static final String externalReferenceLinkTypeName           = "ExternalReferenceLink";
    private static final String localReferenceIdPropertyName            = "referenceId";
    private static final String linkDescriptionPropertyName             = "description";

    private static final Logger log = LoggerFactory.getLogger(ExternalReferencesHandler.class);


    private String                  serviceName;
    private String                  serverName;
    private RepositoryHandler       repositoryHandler;
    private OMRSRepositoryHelper    repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;


    /**
     * Construct the handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName name of the consuming service
     * @param serverName name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper helper used by the converters
     * @param repositoryHandler handler for calling the repository services
     */
    public ExternalReferencesHandler(String                  serviceName,
                                     String                  serverName,
                                     InvalidParameterHandler invalidParameterHandler,
                                     OMRSRepositoryHelper    repositoryHelper,
                                     RepositoryHandler       repositoryHandler)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
    }



    /**
     * Store the list of external references.
     *
     * @param userId the name of the calling user.
     * @param attachedEntityGUID entity linked to external references
     * @param externalReferences links to addition information.  This could be, for example, the home page
     *                           for the governance officer, or details of the role.
     * @throws InvalidParameterException the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void storeExternalReferences(String                     userId,
                                        String                     attachedEntityGUID,
                                        List<ExternalReference>    externalReferences) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String        methodName = "storeExternalReferences";

        // TODO
        log.warn(methodName + " not implemented");

    }


    /**
     * Retrieve the list of external references for this entity.
     *
     * @param userId the name of the calling user.
     * @param attachedEntityGUID entity linked to external references
     * @return links to addition information.  This could be, for example, the home page
     *                           for the governance officer, or details of the role.
     * @throws InvalidParameterException the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<ExternalReference> retrieveExternalReferences(String                     userId,
                                                              String                     attachedEntityGUID) throws InvalidParameterException,
                                                                                                                    PropertyServerException,
                                                                                                                    UserNotAuthorizedException
    {
        final String        methodName = "retrieveExternalReferences";

        // TODO
        log.warn(methodName + " not implemented");

        return null;
    }
}
