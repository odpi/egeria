/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.GLOSSARY_TERM;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.SEMANTIC_ASSIGNMENT;

public class GlossaryHandler {

    private static final Logger log = LoggerFactory.getLogger(GlossaryHandler.class);

    private String serviceName;
    private String serverName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;
    private AssetContext graph = new AssetContext();
    private CommonHandler commonHandler;

    /**
     * Construct the discovery engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName             name of the consuming service
     * @param serverName              name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     */
    public GlossaryHandler(String serviceName,
                           String serverName,
                           InvalidParameterHandler invalidParameterHandler,
                           OMRSRepositoryHelper repositoryHelper,
                           RepositoryHandler repositoryHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.commonHandler = new CommonHandler(serviceName, serverName, invalidParameterHandler, repositoryHelper, repositoryHandler);

    }


    /**
     * Returns the glossary term object corresponding to the supplied asset that can possibly have a glossary Term.
     *
     * @param assetGuid guid of the asset that has been created
     * @param userID    String - userId of user making request.
     * @return Glossary Term retrieved from the repository, null if not semantic assignment to the asset
     */
    public Map<String, Set<GraphContext>> getGlossaryTerm(String assetGuid, String userID, EntityDetail entityDetail, AssetContext assetContext) {

        try {
            graph = assetContext;
            boolean glossary = getGlossary(userID, assetGuid, entityDetail.getType().getTypeDefName());

            if (!glossary) {
                log.info("No Semantic assignment for the asset with guid {} found", assetGuid);
                return Collections.emptyMap();
            }

            return graph.getNeighbors();
        } catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e) {
            throw new AssetLineageException(e.getReportedHTTPCode(),
                                            e.getReportingClassName(),
                                            e.getReportingActionDescription(),
                                            e.getErrorMessage(),
                                            e.getReportedSystemAction(),
                                            e.getReportedUserAction());
        }
    }


    /**
     * Retrieves semantic assignments for an asset
     *
     * @param userId      userId
     * @param assetGuid   guid of the asset that has been created.
     * @param typeDefName the typeName of the asset.
     * @return Glossary Term retrieved from the property server
     */
    private boolean getGlossary(String userId, String assetGuid, String typeDefName) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException {
        final String methodName = "getGlossary";

        String typeGuid = commonHandler.getTypeName(userId, SEMANTIC_ASSIGNMENT);
        List<Relationship> semanticAssignments = repositoryHandler.getRelationshipsByType(userId,
                                                                                          assetGuid,
                                                                                          typeDefName,
                                                                                          typeGuid,
                                                                                          SEMANTIC_ASSIGNMENT,
                                                                                          methodName);

        if (semanticAssignments == null) {
            return false;
        }

        return addSemanticAssignmentToContext(userId, semanticAssignments.toArray(new Relationship[0]));
    }

    /**
     * Add semantic assignments for an asset to the Context structure
     *
     * @param userId      userId
     * @param semanticAssignments   array of the semantic assignments
     * @return true if semantic relationships exist, false otherwise
     */
    private boolean addSemanticAssignmentToContext(String userId, Relationship... semanticAssignments) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException {
        final String methodName = "addSemanticAssignmentToContext";

        List<EntityDetail> entityDetails = new ArrayList<>();
        for (Relationship relationship : semanticAssignments) {

            String glossaryTermGuid = relationship.getEntityTwoProxy().getGUID();
            EntityDetail glossaryTerm = repositoryHandler.getEntityByGUID(userId,
                                                                          glossaryTermGuid,
                                                        "guid",
                                                                          GLOSSARY_TERM,
                                                                          methodName);

            entityDetails.add(commonHandler.writeEntitiesAndRelationships(userId, glossaryTerm, relationship, graph));
        }
        return entityDetails.isEmpty();
    }

}

