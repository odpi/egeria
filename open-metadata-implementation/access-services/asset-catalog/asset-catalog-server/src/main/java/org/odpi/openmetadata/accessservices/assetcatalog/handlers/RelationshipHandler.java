/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.handlers;

import org.odpi.openmetadata.accessservices.assetcatalog.converters.AssetCatalogConverter;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetCatalogBean;
import org.odpi.openmetadata.accessservices.assetcatalog.util.Constants;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * Relationship Handler supports the lookup of the asset's relationship from the repositories.
 * It runs on the server-side of the Asset Catalog OMAS, fetches the relationships using the RepositoryHandler.
 */
public class RelationshipHandler {

    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final OpenMetadataAPIGenericHandler<AssetCatalogBean> assetHandler;
    private final RepositoryErrorHandler errorHandler;
    private final CommonHandler commonHandler;

    private final String sourceName;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param sourceName              name of the component
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param assetHandler            provides utilities for manipulating asset catalog objects using a generic handler
     * @param errorHandler            provides common validation routines for the other handler classes
     */
    public RelationshipHandler(String sourceName, InvalidParameterHandler invalidParameterHandler, RepositoryHandler repositoryHandler,
                               OMRSRepositoryHelper repositoryHelper, OpenMetadataAPIGenericHandler<AssetCatalogBean> assetHandler,
                               RepositoryErrorHandler errorHandler) {
        this.sourceName = sourceName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.errorHandler = errorHandler;
        this.assetHandler = assetHandler;
        this.commonHandler = new CommonHandler(sourceName, repositoryHandler, repositoryHelper, assetHandler, errorHandler);
    }

    /**
     * Fetch relationship between entities details based on its unique identifier of the ends
     *
     * @param userId           String unique identifier for the user
     * @param serverName       String server name
     * @param entity1GUID      Entity guid of the first end of the relationship
     * @param entity2GUID      Entity guid of the second end of the relationship
     * @param relationshipType Type of the relationship
     * @return the relationship between entities
     * @throws InvalidParameterException  full path or userId is null
     * @throws PropertyServerException    problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship getRelationshipBetweenEntities(String userId,
                                                                                                               String serverName,
                                                                                                               String entity1GUID,
                                                                                                               String entity2GUID,
                                                                                                               String relationshipType)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        final String methodName = "getRelationshipBetweenEntities";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(entity1GUID, "entity1GUID", methodName);
        invalidParameterHandler.validateGUID(entity2GUID, "entity2GUID", methodName);

        String relationshipTypeGUID = null;
        if (relationshipType != null) {
            relationshipTypeGUID = commonHandler.getTypeDefGUID(userId, relationshipType);
        }

        EntityDetail entityDetail1 = commonHandler.getEntityByGUID(userId, entity1GUID, null);
        if(entityDetail1 == null){
            invalidParameterHandler.throwUnknownElement(userId, entity1GUID, "-unknown-", sourceName, serverName, methodName);
        }
        String entity1TypeName = entityDetail1.getType().getTypeDefName();

        Relationship relationshipBetweenEntities = repositoryHandler.getRelationshipBetweenEntities(userId,
                entity1GUID,
                entity1TypeName,
                entity2GUID,
                relationshipTypeGUID,
                relationshipType,
                methodName);

        if (relationshipBetweenEntities != null) {
            AssetCatalogConverter<AssetCatalogBean> converter = new AssetCatalogConverter<>(repositoryHelper, sourceName, assetHandler.getServerName());
            return converter.convertRelationship(relationshipBetweenEntities);
        }

        return null;
    }
}