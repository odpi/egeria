/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.handlers;

import org.odpi.openmetadata.accessservices.assetcatalog.builders.AssetConverter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;

public class RelationshipHandler {

    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;

    private List<String> supportedZones;
    private CommonHandler commonHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param supportedZones          configurable list of zones that Asset Catalog is allowed to serve Assets from
     */
    public RelationshipHandler(InvalidParameterHandler invalidParameterHandler, RepositoryHandler repositoryHandler,
                               OMRSRepositoryHelper repositoryHelper, List<String> supportedZones) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.supportedZones = supportedZones;
        this.commonHandler = new CommonHandler(repositoryHandler, repositoryHelper);
    }

    public org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship getRelationshipBetweenEntities(String userId,
                                                                                                               String entity1GUID,
                                                                                                               String entity2GUID,
                                                                                                               String relationshipType) throws org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException {
        final String methodName = "getRelationshipBetweenEntities";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entity1GUID, "entity1GUID", methodName);
        invalidParameterHandler.validateGUID(entity2GUID, "entity2GUID", methodName);

        String relationshipTypeGUID = null;
        if (relationshipType != null) {
            relationshipTypeGUID = commonHandler.getTypeDefGUID(userId, relationshipType);
        }

        Relationship relationshipBetweenEntities = repositoryHandler.getRelationshipBetweenEntities(userId,
                entity1GUID,
                "",
                entity2GUID,
                relationshipTypeGUID,
                relationshipType,
                methodName);

        if (relationshipBetweenEntities != null) {
            AssetConverter converter = new AssetConverter(repositoryHelper);
            return converter.convertRelationship(relationshipBetweenEntities);
        }

        return null;
    }
}