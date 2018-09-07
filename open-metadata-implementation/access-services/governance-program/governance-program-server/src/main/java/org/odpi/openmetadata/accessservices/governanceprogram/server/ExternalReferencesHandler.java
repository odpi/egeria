/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.server;


import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.UnrecognizedGUIDException;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.ExternalReference;
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


    private String                        serviceName;
    private OMRSRepositoryHelper          repositoryHelper = null;
    private String                        serverName       = null;
    private GovernanceProgramErrorHandler errorHandler     = null;


    /**
     * Construct the external references handler with a link to the property server's connector and this access service's
     * official name.
     *
     * @param serviceName  name of this service
     * @param repositoryConnector  connector to the property server.
     */
    ExternalReferencesHandler(String                  serviceName,
                              OMRSRepositoryConnector repositoryConnector)
    {
        this.serviceName = serviceName;
        if (repositoryConnector != null)
        {
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            this.serverName = repositoryConnector.getServerName();
            errorHandler = new GovernanceProgramErrorHandler(repositoryConnector);
        }
    }



    /**
     * Store the list of external references.
     *
     * @param userId the name of the calling user.
     * @param attachedEntityGUID entity linked to external references
     * @param externalReferences links to addition information.  This could be, for example, the home page
     *                           for the governance officer, or details of the role.
     * @throws UnrecognizedGUIDException the entity GUID is not recognized by the server.
     * @throws InvalidParameterException the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public void storeExternalReferences(String                     userId,
                                        String                     attachedEntityGUID,
                                        List<ExternalReference>    externalReferences) throws UnrecognizedGUIDException,
                                                                                              InvalidParameterException,
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
     * @throws UnrecognizedGUIDException the entity GUID is not recognized by the server.
     * @throws InvalidParameterException the external references are not correctly specified, or are null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public List<ExternalReference> retrieveExternalReferences(String                     userId,
                                                              String                     attachedEntityGUID) throws UnrecognizedGUIDException,
                                                                                                                    InvalidParameterException,
                                                                                                                    PropertyServerException,
                                                                                                                    UserNotAuthorizedException
    {
        final String        methodName = "retrieveExternalReferences";

        // TODO
        log.warn(methodName + " not implemented");

        return null;
    }
}
