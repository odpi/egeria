/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.handlers;

import org.odpi.openmetadata.accessservices.assetcatalog.builders.AssetConverter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.List;

public class RelationshipHandler {

    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;

    private List<String> supportedZones;
    private List<String> defaultZones;
    private OpenMetadataServerSecurityVerifier securityVerifier = new OpenMetadataServerSecurityVerifier();
    private CommonHandler commonHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param defaultZones            default list of zones that Asset Catalog is allowed to serve Assets from
     * @param supportedZones          configurable list of zones that Asset Catalog is allowed to serve Assets from
     */
    public RelationshipHandler(InvalidParameterHandler invalidParameterHandler, RepositoryHandler repositoryHandler,
                               OMRSRepositoryHelper repositoryHelper, List<String> defaultZones, List<String> supportedZones) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.defaultZones = defaultZones;
        this.supportedZones = supportedZones;
        this.commonHandler = new CommonHandler(repositoryHandler, repositoryHelper);
    }

    /**
     * Set up a new security verifier (the handler runs with a default verifier until this
     * method is called).
     * <p>
     * The security verifier provides authorization checks for access and maintenance
     * changes to open metadata.  Authorization checks are enabled through the
     * OpenMetadataServerSecurityConnector.
     *
     * @param securityVerifier new security verifier
     */
    public void setSecurityVerifier(OpenMetadataServerSecurityVerifier securityVerifier) {
        this.securityVerifier = securityVerifier;
    }

    public org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship getRelationshipBetweenEntities(String userId,
                                                                                                               String entity1GUID,
                                                                                                               String entity2GUID,
                                                                                                               String relationshipType) throws org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, RepositoryErrorException {
        final String methodName = "getRelationshipBetweenEntities";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entity1GUID, "entity1GUID", methodName);
        invalidParameterHandler.validateGUID(entity2GUID, "entity2GUID", methodName);

        String relationshipTypeGUID = commonHandler.getTypeDefGUID(userId, relationshipType);

        Relationship relationshipBetweenEntities = repositoryHandler.getRelationshipBetweenEntities(userId,
                entity1GUID,
                "",
                entity2GUID,
                relationshipTypeGUID,
                relationshipType,
                methodName);

        if (relationshipBetweenEntities != null) {

            securityVerifier.validateUserForRelationshipRead(userId,
                    commonHandler.getOMRSMetadataCollectionName(userId),
                    relationshipBetweenEntities);

            AssetConverter converter = new AssetConverter(repositoryHelper);
            return converter.convertRelationship(relationshipBetweenEntities);
        }
        return null;
    }
}