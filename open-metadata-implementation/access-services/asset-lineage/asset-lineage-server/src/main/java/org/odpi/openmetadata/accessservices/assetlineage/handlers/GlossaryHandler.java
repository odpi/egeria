/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.GlossaryTerm;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.*;

public class GlossaryHandler {

    private static final Logger log = LoggerFactory.getLogger(GlossaryHandler.class);

    private String serviceName;
    private String serverName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;

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
    }


    /**
     * Returns the glossary term object corresponding to the supplied asset that can possibly have a glossary Term.
     *
     * @param assetGuid guid of the asset that has been created
     * @param userID    String - userId of user making request.
     * @return Glossary Term retrieved from the repository, null if not semantic assignment to the asset
     */
    public GlossaryTerm getGlossaryTerm(String assetGuid, String userID, String typeDefName) {

        try {
            Optional<EntityDetail> entityDetail = getGlossary(userID, assetGuid, typeDefName);

            if (!entityDetail.isPresent()) {
                log.error("No Semantic assignment for the asset with guid {} found", assetGuid);
                return null;
            }

            return getGlossaryProperties(entityDetail.get());
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
     * Returns the entity for the Glossary Term if the asset has a semantic assignment.
     *
     * @param userId userId
     * @param assetGuid  guid of the asset that has been created.
     * @param typeDefName  the typeName of the asset.
     *
     * @return Glossary Term retrieved from the property server
     */
    private Optional<EntityDetail> getGlossary(String userId, String assetGuid, String typeDefName) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String methodName = "getGlossary";
        CommonHandler commonHandler = new CommonHandler(serviceName,serverName,invalidParameterHandler,repositoryHelper,repositoryHandler);

        String typeGuid = commonHandler.getTypeName(userId, SEMANTIC_ASSIGNMENT);
        List<Relationship> semanticAssignment = repositoryHandler.getRelationshipsByType(userId,
                assetGuid,
                typeDefName,
                typeGuid,
                SEMANTIC_ASSIGNMENT,
                methodName);

        if (semanticAssignment != null) {
            String glossaryTermGuid = semanticAssignment.get(0).getEntityTwoProxy().getGUID();
            return Optional.ofNullable(repositoryHandler.getEntityByGUID(userId,
                    glossaryTermGuid,
                    "guid",
                    GLOSSARY_TERM,
                    methodName));
        }
        return Optional.empty();
    }


    /**
     * Retrieves properties for a Glossary Term.
     *
     * @param entityDetail entity for Glossary that is retrieved from a repository
     *
     * @return Glossary Term with all the relevant properties
     */
    private GlossaryTerm getGlossaryProperties(EntityDetail entityDetail) {
        final String methodName = "getGlossaryProperties";
        GlossaryTerm glossaryTerm = new GlossaryTerm();

        String qualifiedName = repositoryHelper.getStringProperty(ASSET_LINEAGE_OMAS,
                "qualifiedName",
                entityDetail.getProperties(),
                methodName);

        String displayName = repositoryHelper.getStringProperty(ASSET_LINEAGE_OMAS,
                "displayName",
                entityDetail.getProperties(),
                methodName);

        List<Classification> classifications = entityDetail.getClassifications();

        glossaryTerm.setGuid(entityDetail.getGUID());
        glossaryTerm.setQualifiedName(qualifiedName);
        glossaryTerm.setType(entityDetail.getType().getTypeDefName());
        glossaryTerm.setDisplayName(displayName);
        glossaryTerm.setClassifications(classifications);

        return glossaryTerm;
    }
}