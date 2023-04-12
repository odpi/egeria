/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.rex;


import org.odpi.openmetadata.repositoryservices.clients.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;


import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.SecureController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The RepositoryExplorerController provides the server-side implementation
 * of the RepositoryExplorer UI-component (aka Rex)
 *
 * This view service provides a number of functions that are needed for Rex.
 * get type information from a repository server
 *   returns: a TypeExplorerResponse object
 * get an entity by guid from a repository server
 *   returns: a RexEntityDetailResponse object
 * get a relationship by guid from a repository server
 * search for entities that match a search string and property filters
 * search for relationships that match a search string and property filters
 */
@RestController
public class RepositoryExplorerController extends SecureController
{

    private static final String USERNAME_NOT_AUTHORIZED_TO_PERFORM_THE_REQUEST = "Sorry - this username was not authorized to perform the request";
    private static final String REPOSITORY_COULD_NOT_BE_REACHED = "The repository could not be reached, please check the server name and URL root and verify that the server is running ";
    private static final String REQUEST_HAS_AN_INVALID_PARAMETER = "The request to load type information reported an invalid parameter, please check the server name and URL root parameters";
    private static final String COULD_NOT_FIND_AN_ENTITY_WITH_THE_GUID_SPECIFIED = "The system could not find an entity with the GUID specified - please check the GUID and try again";
    private static final String THE_SYSTEM_COULD_ONLY_FIND_AN_ENTITY_PROXY_USING_THE_GUID_SPECIFIED = "The system could only find an entity proxy using the GUID specified - please check the GUID and try again";
    private static final String THERE_WAS_A_PROBLEM_WITH_TYPE_INFORMATION_PLEASE_CHECK_AND_RETRY = "There was a problem with Type information - please check and retry";
    private static final String THERE_WAS_A_PROBLEM_WITH_PROPERTY_INFORMATION_PLEASE_CHECK_AND_RETRY = "There was a problem with Property information - please check and retry";
    private static final String THE_UI_TRIED_TO_USE_AN_UNSUPPORTED_FUNCTION = "The UI tried to use an unsupported function";
    private static final String INVALID_PARAMETER_IN_REX_REQUEST = "The request body used in the request to /api/instances/rex-traversal contained an invalid parameter or was missing a parameter. Please check the client code.";
    private static final String TAG_NAME = "tagName";
    private static final String DATA_FIELD_NAME = "dataFieldName";
    private static final String ATTACHMENT_TYPE = "attachmentType";
    private static final String DISPLAY_NAME = "displayName";
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String ENTITY_GUID = "entityGUID";
    private static String className = RepositoryExplorerController.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger(className);


    String                 metadataCollectionId;
    OMRSMetadataCollection metadataCollection;

    private static final int TRUNCATED_STRING_LENGTH = 24;

    /**
     * Default constructor
     *
     */
    public RepositoryExplorerController() {
        metadataCollectionId = null;
        metadataCollection = null;
    }


    /*
     * This method retrieves all the types from the server in a TypeExplorer object.
     * In the RequestBody:
     *   serverName is the name of the repository server to be interrogated.
     *   serverURLRoot is the root of the URL to use to connect to the server.
     *   enterpriseOption is a string "true" or "false" indicating whether to include results from the cohorts to which the server belongs
     */

    @PostMapping( path = "/api/types/rexTypeExplorer")
    public TypeExplorerResponse rexTypeExplorer(@RequestBody RexTypesRequestBody body)
    {
        // Look up types in server and construct TEX
        TypeExplorerResponse texResp;
        String exceptionMessage;

        String serverName;
        String serverURLRoot;
        boolean enterpriseOption;

        try {
            serverName       = body.getServerName();
            serverURLRoot    = body.getServerURLRoot();
            enterpriseOption = body.getEnterpriseOption();
        }
        catch (Exception e) {

            exceptionMessage = "The request body used in the request to /api/types/rexTypeExplorer contained an invalid parameter or was missing a parameter. Please check the client code.";
            // For any of the above exceptions, incorporate the exception message into a response object
            texResp = new TypeExplorerResponse(400, exceptionMessage, null);
            return texResp;
        }


        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        try {

            TypeExplorer tex = this.getTypeExplorer(userId, serverName, serverURLRoot, enterpriseOption);

            if (tex != null) {

                texResp = new TypeExplorerResponse(200, "", tex);

            } else {

                texResp = new TypeExplorerResponse(400, "Could not retrieve type information", null);
            }
            return texResp;
        }
        catch (UserNotAuthorizedException e) {

            exceptionMessage = USERNAME_NOT_AUTHORIZED_TO_PERFORM_THE_REQUEST;
        }
        catch (RepositoryErrorException e) {

            exceptionMessage = REPOSITORY_COULD_NOT_BE_REACHED;
        }
        catch (InvalidParameterException e) {

            exceptionMessage = REQUEST_HAS_AN_INVALID_PARAMETER;
        }

        // For any of the above exceptions, incorporate the exception message into a response object
        texResp = new TypeExplorerResponse(400, exceptionMessage, null);

        return texResp;

    }





    private TypeExplorer getTypeExplorer(String  userId,
                                         String  serverName,
                                         String  serverURLRoot,
                                         boolean enterpriseOption)
    throws
    UserNotAuthorizedException,
    RepositoryErrorException,
    InvalidParameterException
    {

        /*
         *  Switch between local and enterprise services clients depending
         *  on enterprise option...
         */
        MetadataCollectionServicesClient repositoryServicesClient;
        if (!enterpriseOption) {
            repositoryServicesClient = this.getLocalRepositoryServicesClient(serverName, serverURLRoot);
        }
        else {
            repositoryServicesClient = this.getEnterpriseRepositoryServicesClient(serverName, serverURLRoot);
        }

        TypeExplorer tex = new TypeExplorer();

        TypeDefGallery typeDefGallery = repositoryServicesClient.getAllTypes(userId);

        List<TypeDef> typeDefs = typeDefGallery.getTypeDefs();
        for (TypeDef typeDef : typeDefs) {
            TypeDefCategory tdCat = typeDef.getCategory();
            switch (tdCat) {
                case ENTITY_DEF:
                    EntityExplorer eex = new EntityExplorer((EntityDef) typeDef);
                    tex.addEntityExplorer(typeDef.getName(), eex);
                    break;
                case RELATIONSHIP_DEF:
                    RelationshipExplorer rex = new RelationshipExplorer((RelationshipDef) typeDef);
                    tex.addRelationshipExplorer(typeDef.getName(), rex);
                    break;
                case CLASSIFICATION_DEF:
                    ClassificationExplorer cex = new ClassificationExplorer((ClassificationDef) typeDef);
                    tex.addClassificationExplorer(typeDef.getName(), cex);
                    break;
                default:
                    // Ignore this typeDef and continue with next
                    break;
            }
        }

        // Include EnumDefs in the TEX
        List<AttributeTypeDef> attributeTypeDefs = typeDefGallery.getAttributeTypeDefs();
        for (AttributeTypeDef attributeTypeDef : attributeTypeDefs) {
            AttributeTypeDefCategory tdCat = attributeTypeDef.getCategory();
            switch (tdCat) {
                case ENUM_DEF:
                    tex.addEnumExplorer(attributeTypeDef.getName(), (EnumDef)attributeTypeDef);
                    break;
                default:
                    // Ignore this AttributeTypeDef and continue with next
                    break;
            }
        }

        // All typeDefs processed, resolve linkages and return the TEX object
        tex.resolve();
        return tex;

    }


    /**
     * getLocalRepositoryServicesClient
     *
     * This method will get the above client object, which then provides access to all the methods of the
     * MetadataCollection interface. This client is used when the enterprise option is not set, and will
     * connect to the local repository.
     *
     * @param serverName
     * @param serverURLRoot
     * @throws InvalidParameterException
     */
    private LocalRepositoryServicesClient getLocalRepositoryServicesClient(String serverName,
                                                                           String serverURLRoot)
        throws
            InvalidParameterException
    {
        /*
         * The serverName is used as the repositoryName
         * The serverURLRoot is used as part of the restURLRoot, along with the serverName
         */

        /*
         * The only exception thrown by the CTOR is InvalidParameterException, and this is not caught
         * here because we want to surface it to the REST API that called this method so that the
         * exception can be wrapped and a suitable indication sent in the REST Response.
         */
        String restURLRoot = serverURLRoot + "/servers/" + serverName;

        return new LocalRepositoryServicesClient(serverName, restURLRoot);
    }

    /**
     * getEnterpriseRepositoryServicesClient
     *
     * This method will get the above client object, which then provides access to all the methods of the
     * MetadataCollection interface. This client is used when the enterprise option is set, and will
     * perform federation.
     *
     * @param serverName
     * @param serverURLRoot
     * @throws InvalidParameterException
     */
    private EnterpriseRepositoryServicesClient getEnterpriseRepositoryServicesClient(String serverName,
                                                                                     String serverURLRoot)
        throws
            InvalidParameterException
    {
        /*
         * The serverName is used as the repositoryName
         * The serverURLRoot is used as part of the restURLRoot, along with the serverName
         */

        /*
         * The only exception thrown by the CTOR is InvalidParameterException, and this is not caught
         * here because we want to surface it to the REST API that called this method so that the
         * exception can be wrapped and a suitable indication sent in the REST Response.
         */
        String restURLRoot = serverURLRoot + "/servers/" + serverName;

        return new EnterpriseRepositoryServicesClient(serverName, restURLRoot);
    }

    /*
     * This method retrieves the stats affecting a proposed traversal of an instance sub-graph starting from an entity.
     * The response is packaged in a RexPreTraversal object which contains types and counts for relationships and
     * neighboring entities.
     */
    @PostMapping(path = "/api/instances/rex-pre-traversal")
    public RexPreTraversalResponse rexPreTraversal(@RequestBody RexTraversalRequestBody body)
    {
        // Look up types in server and construct TEX
        RexPreTraversalResponse rexPreTraversalResponse;
        String exceptionMessage;

        // If a filter typeGUID was not selected in the UI then it will not appear in the body.

        String serverName;
        String serverURLRoot;
        boolean enterpriseOption;
        String entityGUID;
        Integer depth;

        try {
            serverName          = body.getServerName();
            serverURLRoot       = body.getServerURLRoot();
            enterpriseOption    = body.getEnterpriseOption();
            entityGUID          = body.getEntityGUID();
            depth               = body.getDepth();
        }
        catch (Exception e) {
            exceptionMessage = "The request body used in the request to /api/instances/rex-pre-traversal contained an invalid parameter or was missing a parameter. Please check the client code.";
            // For any of the above exceptions, incorporate the exception message into a response object
            rexPreTraversalResponse = new RexPreTraversalResponse(400, exceptionMessage, null);
            return rexPreTraversalResponse;
        }

        // Pre-traversal the filters are always empty
        List<String> entityTypeGUIDs           = null;
        List<String> relationshipTypeGUIDs     = null;
        List<String> classificationNames       = null;


        String userId = getUser();

        try {

            InstanceGraph instGraph = this.getTraversal(userId, serverName, serverURLRoot, enterpriseOption, entityGUID, entityTypeGUIDs, relationshipTypeGUIDs, classificationNames, depth);

            if (instGraph == null) {
                String excMsg = "Could not retrieve subgraph for entity with guid"+entityGUID;
                rexPreTraversalResponse = new RexPreTraversalResponse(400, excMsg, null);
                return rexPreTraversalResponse;
            }
            // Parse the RexTraversal into a RexPreTraversal for the PreTraversalResponse
            RexPreTraversal rexPreTraversal = new RexPreTraversal();
            rexPreTraversal.setEntityGUID(entityGUID);
            rexPreTraversal.setDepth(depth);

            // Process entities
            Map<String, RexTypeStats> entityCountsByType = new HashMap<>();
            Map<String, RexTypeStats> classificationCountsByType = new HashMap<>();

            processEntities(entityGUID, instGraph, entityCountsByType, classificationCountsByType);
            // Process relationships
            Map<String, RexTypeStats> relationshipCountsByType = processRelationships(instGraph);
            // Update the rexPreTraversal
            rexPreTraversal.setEntityInstanceCounts(entityCountsByType);
            rexPreTraversal.setRelationshipInstanceCounts(relationshipCountsByType);
            rexPreTraversal.setClassificationInstanceCounts(classificationCountsByType);

            rexPreTraversalResponse = new RexPreTraversalResponse(200, "", rexPreTraversal);
            return rexPreTraversalResponse;
        }
        catch (UserNotAuthorizedException e) {

            exceptionMessage = USERNAME_NOT_AUTHORIZED_TO_PERFORM_THE_REQUEST;
        }
        catch (RepositoryErrorException e) {

            exceptionMessage = REPOSITORY_COULD_NOT_BE_REACHED;
        }
        catch (InvalidParameterException e) {

            exceptionMessage = REQUEST_HAS_AN_INVALID_PARAMETER;
        }
        catch (EntityNotKnownException e) {

            exceptionMessage = COULD_NOT_FIND_AN_ENTITY_WITH_THE_GUID_SPECIFIED;
        }
        catch (EntityProxyOnlyException e) {

            exceptionMessage = THE_SYSTEM_COULD_ONLY_FIND_AN_ENTITY_PROXY_USING_THE_GUID_SPECIFIED;
        }
        catch (TypeErrorException e) {

            exceptionMessage = THERE_WAS_A_PROBLEM_WITH_TYPE_INFORMATION_PLEASE_CHECK_AND_RETRY;
        }
        catch (PropertyErrorException e) {

            exceptionMessage = THERE_WAS_A_PROBLEM_WITH_PROPERTY_INFORMATION_PLEASE_CHECK_AND_RETRY;
        }
        catch (FunctionNotSupportedException e) {

            exceptionMessage = THE_UI_TRIED_TO_USE_AN_UNSUPPORTED_FUNCTION;
        }
        // For any of the above exceptions, incorporate the exception message into a response object
        rexPreTraversalResponse = new RexPreTraversalResponse(400, exceptionMessage, null);

        return rexPreTraversalResponse;

    }

    private Map<String, RexTypeStats> processRelationships(InstanceGraph instGraph) {
        List<Relationship> relationships = instGraph.getRelationships();
        Map<String, RexTypeStats> relationshipCountsByType = new HashMap<>();
        if (relationships != null) {
            for (Relationship rel : relationships) {
                InstanceType instanceType = rel.getType();
                String typeGUID = instanceType.getTypeDefGUID();
                String typeName = instanceType.getTypeDefName();
                updateCountsByType(relationshipCountsByType, typeGUID, typeName);
            }
        }
        return relationshipCountsByType;
    }

    private void processEntities(String entityGUID, InstanceGraph instGraph, Map<String, RexTypeStats> entityCountsByType, Map<String, RexTypeStats> classificationCountsByType) {
        List<EntityDetail> entities = instGraph.getEntities();
        if (entities != null) {
            for (EntityDetail ent : entities) {
                // Process entity type information
                /* Skip the entity that the traversal started from.
                 * Counting the starting entity will distort the counts
                 */
                if (!ent.getGUID().equals(entityGUID)) {

                    InstanceType instanceType = ent.getType();
                    String typeGUID = instanceType.getTypeDefGUID();
                    String typeName = instanceType.getTypeDefName();
                    updateCountsByType(entityCountsByType, typeGUID, typeName);
                    // Process entity classification information
                    List<Classification> classifications = ent.getClassifications();
                    if (classifications != null) {
                        for (Classification classification : classifications) {
                            String classificationName = classification.getName();
                            updateCountsByType(classificationCountsByType, null, classificationName);
                        }
                    }
                }
            }
        }
    }

    private void updateCountsByType(Map<String, RexTypeStats> entityCountsByType, String typeGUID, String typeName) {
        if (entityCountsByType.get(typeName) == null) {
            // First sight of an instance of this type
            RexTypeStats stats = new RexTypeStats(typeGUID, 1);
            entityCountsByType.put(typeName, stats);
        } else {
            // Add to the count of instances of this type
            Integer existingCount = entityCountsByType.get(typeName).getCount();
            entityCountsByType.get(typeName).setCount(existingCount + 1);
        }
    }

    @PostMapping(path = "/api/instances/rex-traversal")
    public RexTraversalResponse rexTraversal(@RequestBody RexTraversalRequestBody body)
    {

        RexTraversalResponse rexTraversalResponse;
        String exceptionMessage;

        String serverName;
        String serverURLRoot;
        boolean enterpriseOption;
        String entityGUID;
        Integer depth;
        Integer gen;
        List<String> entityTypeGUIDs;
        List<String> relationshipTypeGUIDs;
        List<String> classificationNames;

        try {
            serverName          = body.getServerName();
            serverURLRoot       = body.getServerURLRoot();
            enterpriseOption    = body.getEnterpriseOption();
            entityGUID          = body.getEntityGUID();
            depth               = body.getDepth();
            gen                 = body.getGen();
            entityTypeGUIDs       = body.getEntityTypeGUIDs();
            relationshipTypeGUIDs = body.getRelationshipTypeGUIDs();
            classificationNames   = body.getClassificationNames();
        }
        catch (Exception e) {
            exceptionMessage = INVALID_PARAMETER_IN_REX_REQUEST;
            // For any of the above exceptions, incorporate the exception message into a response object
            rexTraversalResponse = new RexTraversalResponse(400, exceptionMessage, null);
            return rexTraversalResponse;
        }

        // If a filter typeGUID was not selected in the UI then it will not appear in the body.

        String userId = getUser();

        try {

            /*
             * Because we will want to extract labels based on type we'll need to know the types supported by the repository...
             */

            TypeExplorer typeExplorer = getTypeExplorer(userId, serverName, serverURLRoot, enterpriseOption);


            InstanceGraph instGraph = this.getTraversal(userId, serverName, serverURLRoot, enterpriseOption, entityGUID, entityTypeGUIDs, relationshipTypeGUIDs, classificationNames, depth);

            if (instGraph != null) {

                RexTraversal rt = new RexTraversal();

                /* Format the results
                 *
                 * The format of the digests in the RexTraversal is as follows:
                 *   a map of entityGUID       --> { entityGUID, label, gen }
                 *   a map of relationshipGUID --> { relationshipGUID, end1GUID, end2GUID, idx, label, gen }
                 */

                /*
                 * An InstanceGraph contains relationships and entities that are homed by the repository that
                 * created the InstanceGraph. The relationships may refer to entities that are not homed by that
                 * repository, so those entities will not be included in the 'entities' portion of the InstanceGraph.
                 * Since our RexTraversal needs to be 'complete' - i.e. we have an EntityDigest for each end of
                 * every relationship, we must generate digests not just from the entities lit, but also spot any
                 * relationship ends that are NOT in the list and generate a digest for each of them as well.
                 * Start by processing the homed entities.
                 * Then process the relationships and check fr each end of each relationship whether we need to
                 * augment the RexTraversal entityDigestMap.
                 */
                List<EntityDetail> entities = instGraph.getEntities();
                Map<String, RexEntityDigest> entityDigestMap = null;
                if (entities != null && !entities.isEmpty()) {
                    entityDigestMap = new HashMap<>();
                    for (EntityDetail entityDetail : entities) {
                        /*
                         * We need entityGUID, label (computed) and if !preTraversal also include gen
                         */
                        String entGUID = entityDetail.getGUID();

                        // Pass the typeExplorer to the labeller so that it can traverse...
                        String entLabel = this.chooseLabelForEntity(entityDetail, typeExplorer);

                        RexEntityDigest red = new RexEntityDigest(entGUID, entLabel, gen, entityDetail.getMetadataCollectionName());
                        entityDigestMap.put(entGUID, red);
                    }

                }

                List<Relationship> relationships = instGraph.getRelationships();
                Map<String, RexRelationshipDigest> relationshipDigestMap = null;
                if (relationships != null && !relationships.isEmpty()) {
                    relationshipDigestMap = new HashMap<>();
                    for (Relationship relationship : relationships) {
                        /*
                         * We need: entityGUID, label (computed) and if !preTraversal also include gen
                         *   relationshipGUID, label (computed), end1GUID, end2GUID, idx (computed), gen
                         */
                        String relGUID = relationship.getGUID();
                        String relLabel = this.chooseLabelForRelationship(relationship);
                        String end1GUID = relationship.getEntityOneProxy().getGUID();
                        String end2GUID = relationship.getEntityTwoProxy().getGUID();

                        /* check for proxies... */
                        if (entityDigestMap.get(end1GUID) == null) {
                            /* add a digest for this proxy... */
                            EntityProxy end1Proxy = relationship.getEntityOneProxy();
                            String end1Label = this.chooseLabelForEntityProxy(end1Proxy, typeExplorer);
                            RexEntityDigest red = new RexEntityDigest(end1GUID, end1Label, gen, end1Proxy.getMetadataCollectionName());
                            entityDigestMap.put(end1GUID, red);
                        }
                        if (entityDigestMap.get(end2GUID) == null) {
                            /* add a digest for this proxy... */
                            EntityProxy end2Proxy = relationship.getEntityTwoProxy();
                            String end2Label = this.chooseLabelForEntityProxy(end2Proxy, typeExplorer);
                            RexEntityDigest red = new RexEntityDigest(end2GUID, end2Label, gen, end2Proxy.getMetadataCollectionName());
                            entityDigestMap.put(end2GUID, red);
                        }


                        int idx = 0;

                        RexRelationshipDigest rrd = new RexRelationshipDigest(relGUID, relLabel, end1GUID, end2GUID, idx,
                                                                              gen, relationship.getMetadataCollectionName());
                        relationshipDigestMap.put(relGUID, rrd);
                    }
                }

                rt.setEntityGUID(entityGUID);
                rt.setDepth(depth);
                rt.setGen(gen);
                // Instead of using type guids in the traversal (which is to be sent to the browser) use type names instead.
                List<String> entityTypeNames = new ArrayList<>();
                if (entityTypeGUIDs != null && !entityTypeGUIDs.isEmpty()) {
                    for (String entityTypeGUID : entityTypeGUIDs) {
                        // Convert from typeGIUD to typeName
                        String entityTypeName = typeExplorer.getEntityTypeName(entityTypeGUID);
                        entityTypeNames.add(entityTypeName);
                    }
                }
                rt.setEntityTypeNames(entityTypeNames);
                rt.setRelationshipTypeGUIDs(relationshipTypeGUIDs);
                rt.setClassificationNames(classificationNames);
                rt.setEntities(entityDigestMap);
                rt.setRelationships(relationshipDigestMap);
                rt.setServerName(serverName);

                rexTraversalResponse = new RexTraversalResponse(200, "", rt);

            } else {

                String excMsg = "Could not retrieve subgraph for entity with guid" + entityGUID;
                rexTraversalResponse = new RexTraversalResponse(400, excMsg, null);

            }
            return rexTraversalResponse;
        }
        catch (UserNotAuthorizedException e) {

            exceptionMessage = USERNAME_NOT_AUTHORIZED_TO_PERFORM_THE_REQUEST;
        }
        catch (RepositoryErrorException e) {

            exceptionMessage = REPOSITORY_COULD_NOT_BE_REACHED;
        }
        catch (InvalidParameterException e) {

            exceptionMessage = REQUEST_HAS_AN_INVALID_PARAMETER;
        }
        catch (EntityNotKnownException e) {

            exceptionMessage = COULD_NOT_FIND_AN_ENTITY_WITH_THE_GUID_SPECIFIED;
        }
        catch (EntityProxyOnlyException e) {

            exceptionMessage = THE_SYSTEM_COULD_ONLY_FIND_AN_ENTITY_PROXY_USING_THE_GUID_SPECIFIED;
        }
        catch (TypeErrorException e) {

            exceptionMessage = THERE_WAS_A_PROBLEM_WITH_TYPE_INFORMATION_PLEASE_CHECK_AND_RETRY;
        }
        catch (PropertyErrorException e) {

            exceptionMessage = THERE_WAS_A_PROBLEM_WITH_PROPERTY_INFORMATION_PLEASE_CHECK_AND_RETRY;
        }
        catch (FunctionNotSupportedException e) {

            exceptionMessage = THE_UI_TRIED_TO_USE_AN_UNSUPPORTED_FUNCTION;
        }
        // For any of the above exceptions, incorporate the exception message into a response object
        rexTraversalResponse = new RexTraversalResponse(400, exceptionMessage, null);

        return rexTraversalResponse;

    }



    private String chooseLabelForEntity(EntityDetail entityDetail, TypeExplorer typeExplorer)
    {

        // By default, use the GUID of the instance. This is not a desirable
        // label but if there is really nothing else to use, then the GUID is
        // better than nothing at all. Maybe.
        String label = entityDetail.getGUID();



        // Find the effective typeName - this is the highest supertype of the instance type
        String instanceTypeName = null;

        InstanceType instanceType = entityDetail.getType();
        if (instanceType != null) {
            instanceTypeName = instanceType.getTypeDefName();
        }
        if (instanceTypeName == null || instanceTypeName.equals("")) {
            // Drop out - there is no proper type information for the instance - just adopt the default set above.
            return label;
        }

        // We know that instanceTypeName is set to something we can use...

        // Traverse the TypeExplorer looking for the highest supertype..
        Map<String, EntityExplorer> entityTypes = typeExplorer.getEntities();

        // Get the immediate entity instance type...
        EntityExplorer eex = entityTypes.get(instanceTypeName);
        TypeDefLink superType = eex.getEntityDef().getSuperType();
        while (superType != null) {
            String superTypeName = superType.getName();
            eex = entityTypes.get(superTypeName);
            superType = eex.getEntityDef().getSuperType();
        }
        // eex is now the effective type entry
        TypeDef effectiveTypeDef = eex.getEntityDef();

        String effTypeName = effectiveTypeDef.getName();

        switch (effTypeName) {

            case "InformalTag":
                if (entityDetail.getProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties().get(TAG_NAME) != null &&
                        entityDetail.getProperties().getInstanceProperties().get(TAG_NAME).valueAsString() != null) {

                    label = entityDetail.getProperties().getInstanceProperties().get(TAG_NAME).valueAsString();
                }
                break;

            case "Like":
            case "Rating":
                if (entityDetail.getCreatedBy() != null) {
                    label = entityDetail.getCreatedBy();
                }
                break;

            case "DataField":
                if (entityDetail.getProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties().get(DATA_FIELD_NAME) != null &&
                        entityDetail.getProperties().getInstanceProperties().get(DATA_FIELD_NAME).valueAsString() != null) {

                    label = entityDetail.getProperties().getInstanceProperties().get(DATA_FIELD_NAME).valueAsString();
                }
                break;

            case "Annotation":
            case "AnnotationReview":
                label = instanceTypeName;  // use the local type name for anything under these types
                break;

            case "LastAttachment":
                if (entityDetail.getProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties().get(ATTACHMENT_TYPE) != null &&
                        entityDetail.getProperties().getInstanceProperties().get(ATTACHMENT_TYPE).valueAsString() != null) {

                    label = entityDetail.getProperties().getInstanceProperties().get(ATTACHMENT_TYPE).valueAsString();
                }
                break;

            default:
                // Anything that is left should be a Referenceable.
                // If it has a displayName use that.
                // Else if it has a name use that.
                // Otherwise if it has a qualifiedName use up to the last TRUNCATED_STRING_LENGTH chars of that.
                // If there is not qualifiedName drop through and use GUID (default)
                if (entityDetail.getProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties().get(DISPLAY_NAME) != null &&
                        entityDetail.getProperties().getInstanceProperties().get(DISPLAY_NAME).valueAsString() != null) {

                    label = entityDetail.getProperties().getInstanceProperties().get(DISPLAY_NAME).valueAsString();
                }
                else if (entityDetail.getProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties().get("name") != null &&
                        entityDetail.getProperties().getInstanceProperties().get("name").valueAsString() != null) {

                    label = entityDetail.getProperties().getInstanceProperties().get("name").valueAsString();
                }
                else if (entityDetail.getProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties().get(QUALIFIED_NAME) != null) {

                    String fullQN = entityDetail.getProperties().getInstanceProperties().get(QUALIFIED_NAME).valueAsString();

                    int lengthQN = fullQN.length();
                    if (lengthQN > TRUNCATED_STRING_LENGTH) {
                        String tailQN = "..." + fullQN.substring(lengthQN-TRUNCATED_STRING_LENGTH, lengthQN);
                        label = tailQN;
                    } else {
                        label = fullQN;
                    }
                }
        }

        return label;

    }



    private String chooseLabelForEntityProxy(EntityProxy entityProxy, TypeExplorer typeExplorer)
    {

        // Refer to the comment in chooseLabelForEntity for labelling strategy - similar applies
        // here but implementation caters for the entity only being a proxy.
        // By default, use the GUID of the instance. This is not a desirable
        // label but if there is really nothing else to use, then the GUID is
        // better than nothing at all. Maybe.
        String label = entityProxy.getGUID();


        // Find the effective typeName - this is the highest supertype of the instance type
        String instanceTypeName = null;

        InstanceType instanceType = entityProxy.getType();
        if (instanceType != null) {
            instanceTypeName = instanceType.getTypeDefName();
        }
        if (instanceTypeName == null || instanceTypeName.equals("")) {
            // Drop out - there is no proper type information for the instance - just adopt the default set above.
            return label;
        }

        // We know that instanceTypeName is set to something we can use...

        // Traverse the TypeExplorer looking for the highest supertype..
        Map<String, EntityExplorer> entityTypes = typeExplorer.getEntities();

        // Get the immediate entity instance type...
        EntityExplorer eex = entityTypes.get(instanceTypeName);
        TypeDefLink superType = eex.getEntityDef().getSuperType();
        while (superType != null) {
            String superTypeName = superType.getName();
            eex = entityTypes.get(superTypeName);
            superType = eex.getEntityDef().getSuperType();
        }
        // eex is now the effective type entry
        TypeDef effectiveTypeDef = eex.getEntityDef();

        String effTypeName = effectiveTypeDef.getName();

        switch (effTypeName) {

            case "InformalTag":
                if (entityProxy.getUniqueProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get(TAG_NAME) != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get(TAG_NAME).valueAsString() != null) {

                    label = entityProxy.getUniqueProperties().getInstanceProperties().get(TAG_NAME).valueAsString();

                }
                break;

            case "Like":
            case "Rating":
                if (entityProxy.getCreatedBy() != null) {
                    label = entityProxy.getCreatedBy();

                }
                break;

            case "DataField":
                if (entityProxy.getUniqueProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get(DATA_FIELD_NAME) != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get(DATA_FIELD_NAME).valueAsString() != null) {

                    label = entityProxy.getUniqueProperties().getInstanceProperties().get(DATA_FIELD_NAME).valueAsString();

                }
                break;

            case "Annotation":
            case "AnnotationReview":
                label = instanceTypeName;  // use the local type name for anything under these types
                break;

            default:
                label = getDefaultLabelFromEntityProxy(entityProxy, label);
        }

        return label;

    }

    private String getDefaultLabelFromEntityProxy(EntityProxy entityProxy, String label) {
        // Anything that is left should be a Referenceable.
        // If it has a displayName use that.
        // Otherwise if it has a qualifiedName use up to the last TRUNCATED_STRING_LENGTH chars of that.
        // If there is not qualifiedName drop through and use GUID (default)
        if (entityProxy.getUniqueProperties() != null &&
                entityProxy.getUniqueProperties().getInstanceProperties() != null &&
                entityProxy.getUniqueProperties().getInstanceProperties().get(DISPLAY_NAME) != null &&
                entityProxy.getUniqueProperties().getInstanceProperties().get(DISPLAY_NAME).valueAsString() != null) {

            label = entityProxy.getUniqueProperties().getInstanceProperties().get(DISPLAY_NAME).valueAsString();

        }
        else if (entityProxy.getUniqueProperties() != null &&
                entityProxy.getUniqueProperties().getInstanceProperties() != null &&
                entityProxy.getUniqueProperties().getInstanceProperties().get("name") != null &&
                entityProxy.getUniqueProperties().getInstanceProperties().get("name").valueAsString() != null) {

            label = entityProxy.getUniqueProperties().getInstanceProperties().get("name").valueAsString();

        }
        else if (entityProxy.getUniqueProperties() != null &&
                entityProxy.getUniqueProperties().getInstanceProperties() != null &&
                entityProxy.getUniqueProperties().getInstanceProperties().get(QUALIFIED_NAME) != null) {

            String fullQN = entityProxy.getUniqueProperties().getInstanceProperties().get(QUALIFIED_NAME).valueAsString();

            int lengthQN = fullQN.length();
            if (lengthQN > TRUNCATED_STRING_LENGTH) {
                String tailQN = "..." + fullQN.substring(lengthQN-TRUNCATED_STRING_LENGTH, lengthQN);
                label = tailQN;
            } else {
                label = fullQN;
            }
        }
        return label;
    }


    private String chooseLabelForRelationship(Relationship relationship)
    {

        // By default, use the GUID of the instance. This is not a desirable
        // label but if there is really nothing else to use, then the GUID is
        // better than nothing at all. Maybe.
        String label = relationship.getGUID();

        String instanceTypeName = null;

        InstanceType instanceType = relationship.getType();
        if (instanceType != null) {
            instanceTypeName = instanceType.getTypeDefName();
        }
        if (instanceTypeName == null || instanceTypeName.equals("")) {
            // Drop out - there is no proper type information for the instance - just adopt the default set above.
            return label;
        }

        // We know that instanceTypeName is set to something we can use...

        // For now simply label relationships by type name.
        label = instanceTypeName;

        return label;

    }




    // The filters work as follows:
    // If set to null then no filtering is performed - the typeGUID lists are set to null
    // If set to a list of string values then those types are allowed.
    // Returns - a completed RexTraversal or null
    private InstanceGraph getTraversal(String         userId,
                                      String         serverName,
                                      String         serverURLRoot,
                                      Boolean        enterpriseOption,
                                      String         entityGUID,
                                      List<String>   entityTypeGUIDs,
                                      List<String>   relationshipTypeGUIDs,
                                      List<String>   classificationNames,
                                      Integer        depth
                                      )
            throws
            UserNotAuthorizedException,
            RepositoryErrorException,
            InvalidParameterException,
            EntityNotKnownException,
            EntityProxyOnlyException,
            TypeErrorException,
            PropertyErrorException,
            FunctionNotSupportedException
    {

        String methodName = "getTraversal";


        // If no entityGUID is specified or depth is not positive, there is no point in continuing...

        if (entityGUID == null) {
            // We have a problem - the entityGUID has not been specified.
            // We cannot do a query...

            final String parameterName = ENTITY_GUID;

            throw new InvalidParameterException(RexErrorCode.NO_GUID.getMessageDefinition(entityGUID, methodName, serverName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        if (depth < 0 ) {
            // We have a problem - the depth is negative.
            // In either case we cannot do a query...

            final String parameterName = "depth";

            throw new InvalidParameterException(RexErrorCode.INVALID_VALUE.getMessageDefinition(depth.toString(), methodName, serverName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);

        }


        /*
         *  Switch between local and enterprise services clients depending
         *  on enterprise option...
         */
        MetadataCollectionServicesClient repositoryServicesClient;
        if (!enterpriseOption) {
            repositoryServicesClient = this.getLocalRepositoryServicesClient(serverName, serverURLRoot);
        }
        else {
            repositoryServicesClient = this.getEnterpriseRepositoryServicesClient(serverName, serverURLRoot);
        }

        if (depth >0) {

            return repositoryServicesClient.getEntityNeighborhood(
                    userId,
                    entityGUID,
                    entityTypeGUIDs,
                    relationshipTypeGUIDs,
                    null,
                    classificationNames,
                    null,
                    depth);
        }


        else {

            /*
             * Since depth is 0 - use getEntityDetail instead of neighbourhood
             */

            EntityDetail entityDetail = metadataCollection.getEntityDetail(
                    userId,
                    entityGUID);

            if (entityDetail != null) {

                // Construct an InstanceGraph containing just the entityDetail
                InstanceGraph instGraph = new InstanceGraph();

                List<EntityDetail> entityDetailList = new ArrayList<>();
                entityDetailList.add(entityDetail);
                instGraph.setEntities(entityDetailList);

                return instGraph;

            }

            else {
                // Entity could not be found - should have already had an exception but just to be sure...

                final String parameterName = ENTITY_GUID;

                throw new InvalidParameterException(RexErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(entityGUID, methodName, serverName),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    parameterName);

            }
        }

    }



    /*
     *  This method gets an entity detail.
     *
     *  When retrieving a single entity we return the whole EntityDetail object. This is
     *  because the entity is being used as the user focus object and will be displayed in
     *  the details pane.
     *
     *  This method has a body because things like serverName etc are for the repo server - they need
     *  to be unpacked here and used to interrogate the repository's metadata collection interface.
     *
     *  The method used is POST because the parameters supplied by the UI to the VS are conveyed in
     *  the request body.
     */
    @PostMapping( path = "/api/instances/entity")
    public RexEntityDetailResponse getEntityDetail(@RequestBody  RexEntityRequestBody   body)
    {

        RexEntityDetailResponse response;
        String exceptionMessage;

        String serverName;
        String serverURLRoot;
        boolean enterpriseOption;
        String entityGUID;

        try {
            serverName          = body.getServerName();
            serverURLRoot       = body.getServerURLRoot();
            enterpriseOption    = body.getEnterpriseOption();
            entityGUID          = body.getEntityGUID();
        }
        catch (Exception e) {
            exceptionMessage = INVALID_PARAMETER_IN_REX_REQUEST;
            // For any of the above exceptions, incorporate the exception message into a response object
            response = new RexEntityDetailResponse(400, exceptionMessage, null);
            return response;
        }


        String userId = getUser();

        try {

            EntityDetail entityDetail = this.getEntityDetail(userId, serverName, serverURLRoot, enterpriseOption, entityGUID);

            TypeExplorer typeExplorer = getTypeExplorer(userId, serverName, serverURLRoot, enterpriseOption);
            String label = this.chooseLabelForEntity(entityDetail, typeExplorer);

            RexExpandedEntityDetail rexExpEntityDetail = new RexExpandedEntityDetail(entityDetail, label, serverName);

            response = new RexEntityDetailResponse(200, "", rexExpEntityDetail);

            return response;
        }
        catch (UserNotAuthorizedException e) {

            exceptionMessage = USERNAME_NOT_AUTHORIZED_TO_PERFORM_THE_REQUEST;
        }
        catch (RepositoryErrorException e) {

            exceptionMessage = REPOSITORY_COULD_NOT_BE_REACHED;
        }
        catch (InvalidParameterException e) {

            exceptionMessage = REQUEST_HAS_AN_INVALID_PARAMETER;
        }
        catch (EntityNotKnownException e) {

            exceptionMessage = COULD_NOT_FIND_AN_ENTITY_WITH_THE_GUID_SPECIFIED;
        }
        catch (EntityProxyOnlyException e) {

            exceptionMessage = THE_SYSTEM_COULD_ONLY_FIND_AN_ENTITY_PROXY_USING_THE_GUID_SPECIFIED;
        }

        // For any of the above exceptions, incorporate the exception message into a response object
        response = new RexEntityDetailResponse(400, exceptionMessage, null);

        return response;

    }



    private EntityDetail getEntityDetail(String   userId,
                                           String   serverName,
                                           String   serverURLRoot,
                                           boolean  enterpriseOption,
                                           String   entityGUID)
    throws
    UserNotAuthorizedException,
    RepositoryErrorException,
    InvalidParameterException,
    EntityNotKnownException,
    EntityProxyOnlyException

    {

        String methodName = "getEntityDetail";

        if (entityGUID == null) {
            // If no entityGUID is specified, there is no point in continuing...
            // We have a problem - the entityGUID has not been specified .
            // In either case we cannot do a query...

            final String parameterName = ENTITY_GUID;

            throw new InvalidParameterException(RexErrorCode.NO_GUID.getMessageDefinition(entityGUID, methodName, serverName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);

        }

        if ("trouble-at-mill".equals(entityGUID)) {
            // If a stupid entityGUID is specified, there is no point in continuing...

            final String parameterName = ENTITY_GUID;

            throw new InvalidParameterException(RexErrorCode.TROUBLE_AT_MILL.getMessageDefinition(entityGUID, methodName, serverName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);

        }

        /*
         *  Switch between local and enterprise services clients depending
         *  on enterprise option...
         */
        MetadataCollectionServicesClient repositoryServicesClient;
        if (!enterpriseOption) {
            repositoryServicesClient = this.getLocalRepositoryServicesClient(serverName, serverURLRoot);
        }
        else {
            repositoryServicesClient = this.getEnterpriseRepositoryServicesClient(serverName, serverURLRoot);
        }

        EntityDetail entityDetail = repositoryServicesClient.getEntityDetail(
                userId,
                entityGUID);

        if (entityDetail != null) {

            return entityDetail;

        } else {

            // Entity could not be found - should have already had an exception but just to be sure...

            final String parameterName = ENTITY_GUID;

            throw new InvalidParameterException(RexErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(entityGUID, methodName, serverName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);

        }

    }

    /*
     *  This method gets a relationship.
     *
     *  When retrieving a single relationship we return the whole Relationship object. This is
     *  because the relationship is being used as the user focus object and will be displayed in
     *  the details pane.
     *
     *  This method has a body because things like serverName etc are for the repo server - they need
     *  to be unpacked here and used to interrogate the repository's metadata collection interface.
     *
     *  The method used is POST because the parameters supplied by the UI to the VS are conveyed in
     *  the request body.
     */
    @PostMapping( path = "/api/instances/relationship")
    public RexRelationshipResponse getRelationship(@RequestBody  RexRelationshipRequestBody   body)
    {

        RexRelationshipResponse response;
        String exceptionMessage;

        String serverName;
        String serverURLRoot;
        boolean enterpriseOption;
        String relationshipGUID;

        try {
            serverName          = body.getServerName();
            serverURLRoot       = body.getServerURLRoot();
            enterpriseOption    = body.getEnterpriseOption();
            relationshipGUID    = body.getRelationshipGUID();
        }
        catch (Exception e) {
            exceptionMessage = INVALID_PARAMETER_IN_REX_REQUEST;
            // For any of the above exceptions, incorporate the exception message into a response object
            response = new RexRelationshipResponse(400, exceptionMessage, null);
            return response;
        }


        String userId = getUser();

        try {

            Relationship relationship = this.getRelationship(userId, serverName, serverURLRoot, enterpriseOption, relationshipGUID);

            // Create digests for both ends

            TypeExplorer typeExplorer = getTypeExplorer(userId, serverName, serverURLRoot, enterpriseOption);
            EntityProxy entity1 = relationship.getEntityOneProxy();
            EntityProxy entity2 = relationship.getEntityTwoProxy();
            String label1 = this.chooseLabelForEntityProxy(entity1, typeExplorer);
            String label2 = this.chooseLabelForEntityProxy(entity2, typeExplorer);

            RexEntityDigest digest1 = new RexEntityDigest(entity1.getGUID(), label1, 0, entity1.getMetadataCollectionName());
            RexEntityDigest digest2 = new RexEntityDigest(entity2.getGUID(), label2, 0, entity2.getMetadataCollectionName());

            String label = this.chooseLabelForRelationship(relationship);

            RexExpandedRelationship rexExpRelationship = new RexExpandedRelationship(relationship, label, digest1, digest2, serverName);

            response = new RexRelationshipResponse(200, "", rexExpRelationship);

            return response;
        }
        catch (UserNotAuthorizedException e) {

            exceptionMessage = USERNAME_NOT_AUTHORIZED_TO_PERFORM_THE_REQUEST;
        }
        catch (RepositoryErrorException e) {

            exceptionMessage = REPOSITORY_COULD_NOT_BE_REACHED;
        }
        catch (InvalidParameterException e) {

            exceptionMessage = REQUEST_HAS_AN_INVALID_PARAMETER;
        }
        catch (RelationshipNotKnownException e) {

            exceptionMessage = "The system could not find an relationship with the GUID specified - please check the GUID and try again";
        }

        // For any of the above exceptions, incorporate the exception message into a response object
        response = new RexRelationshipResponse(400, exceptionMessage, null);

        return response;

    }

    //

    private Relationship getRelationship(String   userId,
                                         String   serverName,
                                         String   serverURLRoot,
                                         boolean  enterpriseOption,
                                         String   relationshipGUID)
    throws
    UserNotAuthorizedException,
    RepositoryErrorException,
    InvalidParameterException,
    RelationshipNotKnownException
    {

        String methodName = "getRelationship";


        if (relationshipGUID == null) {
            // If no relationshipGUID is specified, there is no point in continuing...
            // We have a problem - the relationshipGUID has not been specified .
            // In either case we cannot do a query...

            final String parameterName = "relationshipGUID";

            throw new InvalidParameterException(RexErrorCode.NO_GUID.getMessageDefinition(relationshipGUID, methodName, serverName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);

        }

        /*
         *  Switch between local and enterprise services clients depending
         *  on enterprise option...
         */
        MetadataCollectionServicesClient repositoryServicesClient;
        if (!enterpriseOption) {
            repositoryServicesClient = this.getLocalRepositoryServicesClient(serverName, serverURLRoot);
        }
        else {
            repositoryServicesClient = this.getEnterpriseRepositoryServicesClient(serverName, serverURLRoot);
        }

        Relationship relationship = repositoryServicesClient.getRelationship(
                userId,
                relationshipGUID);

        if (relationship != null) {

            return relationship;

        } else {

            // Relationship could not be found - should have already had an exception but just to be sure...

            final String parameterName = "relationshipGUID";

            throw new InvalidParameterException(RexErrorCode.RELATIONSHIP_NOT_KNOWN.getMessageDefinition(relationshipGUID, methodName, serverName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

    }


    // SEARCH FUNCTIONS

    /*
     *  This method searches for entities that match property value - using search text.
     *
     *
     *  This method has a body because things like serverName etc are for the repo server - they need
     *  to be unpacked here and used to interrogate the repository's metadata collection interface.
     *
     *  The method used is POST because the parameters supplied by the UI to the VS are conveyed in
     *  the request body.
     */
    @PostMapping( path = "/api/instances/entities/by-property-value")
    public RexSearchResponse entitySearch(@RequestBody  RexSearchBody   body)
    {
        RexSearchResponse response;
        String exceptionMessage;

        String searchCategory = "Entity";

        String serverName;
        String serverURLRoot;
        boolean enterpriseOption;
        String entityTypeName;
        String searchText;

        try {
            serverName          = body.getServerName();
            serverURLRoot       = body.getServerURLRoot();
            enterpriseOption    = body.getEnterpriseOption();
            entityTypeName      = body.getTypeName();
            searchText          = body.getSearchText();
        }
        catch (Exception e) {
            exceptionMessage = INVALID_PARAMETER_IN_REX_REQUEST;
            // For any of the above exceptions, incorporate the exception message into a response object
            response = new RexSearchResponse(400, exceptionMessage, null, null, searchCategory, null, null);
            return response;
        }




        String userId = getUser();

        String entityTypeGUID = null;

        // Convert type name to typeGUID.
        try {

            TypeExplorer typeExplorer = getTypeExplorer(userId, serverName, serverURLRoot, enterpriseOption);
            entityTypeGUID = typeExplorer.getEntityTypeGUID(entityTypeName);

            List<EntityDetail> entities = this.findEntities(userId, serverName, serverURLRoot, enterpriseOption, searchText, entityTypeGUID);

            if (entities != null) {

                // Process the list of EntityDetail objects and produce a list of EntityDigest objects

                Map<String, RexEntityDigest> digestMap = new HashMap<>();

                for (int e=0; e < entities.size(); e++) {
                     EntityDetail entityDetail = entities.get(e);
                     String label = this.chooseLabelForEntity(entityDetail, typeExplorer);

                     RexEntityDigest entityDigest = new RexEntityDigest(entityDetail.getGUID(), label, 0, entityDetail.getMetadataCollectionName());

                     digestMap.put(entityDetail.getGUID(), entityDigest);

                }


                response = new RexSearchResponse(200, "", serverName, searchText, searchCategory, digestMap, null);

            } else {

                String excMsg = "Could not find any entities that matched "+searchText;
                response = new RexSearchResponse(400, excMsg, serverName, searchText, searchCategory, null, null);

            }

            return response;
        }
        catch (UserNotAuthorizedException e) {

            exceptionMessage = USERNAME_NOT_AUTHORIZED_TO_PERFORM_THE_REQUEST;
        }
        catch (RepositoryErrorException e) {

            exceptionMessage = REPOSITORY_COULD_NOT_BE_REACHED;
        }
        catch (InvalidParameterException e) {

            exceptionMessage = REQUEST_HAS_AN_INVALID_PARAMETER;
        }
        catch (TypeErrorException e) {

            exceptionMessage = THERE_WAS_A_PROBLEM_WITH_TYPE_INFORMATION_PLEASE_CHECK_AND_RETRY;
        }
        catch (PropertyErrorException e) {

            exceptionMessage = THERE_WAS_A_PROBLEM_WITH_PROPERTY_INFORMATION_PLEASE_CHECK_AND_RETRY;
        }
        catch (PagingErrorException e) {

            exceptionMessage = "There was a problem with Paging - please check and retry";
        }
        catch (FunctionNotSupportedException e) {

            exceptionMessage = THE_UI_TRIED_TO_USE_AN_UNSUPPORTED_FUNCTION;
        }
        // For any of the above exceptions, incorporate the exception message into a response object
        response = new RexSearchResponse(400, exceptionMessage, serverName, searchText, searchCategory, null, null);

        return response;

    }



    private List<EntityDetail> findEntities(String   userId,
                                            String   serverName,
                                            String   serverURLRoot,
                                            boolean  enterpriseOption,
                                            String   searchText,
                                            String   entityTypeGUID)
    throws
    UserNotAuthorizedException,
    RepositoryErrorException,
    InvalidParameterException,
    TypeErrorException,
    PropertyErrorException,
    PagingErrorException,
    FunctionNotSupportedException
    {

        String methodName = "findEntities";



        if (searchText == null) {
            // If no searchText is specified, there is no point in continuing...
            // We have a problem - the searchText has not been specified.
            // We cannot do a query...

            final String parameterName = "searchText";

            throw new InvalidParameterException(RexErrorCode.INVALID_VALUE.getMessageDefinition(searchText, methodName, serverName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        /*
         *  Switch between local and enterprise services clients depending
         *  on enterprise option...
         */
        MetadataCollectionServicesClient repositoryServicesClient;
        if (!enterpriseOption) {
            repositoryServicesClient = this.getLocalRepositoryServicesClient(serverName, serverURLRoot);
        }
        else {
            repositoryServicesClient = this.getEnterpriseRepositoryServicesClient(serverName, serverURLRoot);
        }


        return repositoryServicesClient.findEntitiesByPropertyValue(
                userId,
                entityTypeGUID,
                searchText,
                0,
                null,
                null,
                null,
                null,
                null,
                0);

    }



    /*
     *  This method searches for relationships that match property value - using search text.
     *
     *
     *  This method has a body because things like serverName etc are for the repo server - they need
     *  to be unpacked here and used to interrogate the repository's metadata collection interface.
     *
     *  The method used is POST because the parameters supplied by the UI to the VS are conveyed in
     *  the request body.
     */
    @PostMapping( path = "/api/instances/relationships/by-property-value")
    public RexSearchResponse relationshipSearch(@RequestBody  RexSearchBody    body)
    {

        RexSearchResponse response;
        String exceptionMessage;

        String searchCategory = "Relationship";

        String serverName;
        String serverURLRoot;
        boolean enterpriseOption;
        String relationshipTypeName;
        String searchText;

        try {
            serverName             = body.getServerName();
            serverURLRoot          = body.getServerURLRoot();
            enterpriseOption       = body.getEnterpriseOption();
            relationshipTypeName   = body.getTypeName();
            searchText             = body.getSearchText();
        }
        catch (Exception e) {
            exceptionMessage = INVALID_PARAMETER_IN_REX_REQUEST;
            // For any of the above exceptions, incorporate the exception message into a response object
            response = new RexSearchResponse(400, exceptionMessage, null, null, searchCategory, null, null);
            return response;
        }


        String userId = getUser();

        String relationshipTypeGUID = null;

        // Convert type name to typeGUID.
        try {

            TypeExplorer typeExplorer = getTypeExplorer(userId, serverName, serverURLRoot, enterpriseOption);
            relationshipTypeGUID = typeExplorer.getRelationshipTypeGUID(relationshipTypeName);

            List<Relationship> relationships = this.findRelationships(userId, serverName, serverURLRoot, enterpriseOption, searchText, relationshipTypeGUID);

            if (relationships != null) {

                // Process the list of Relationship objects and produce a list of RelationshipDigest objects

                Map<String, RexRelationshipDigest> digestMap = new HashMap<>();

                for (Relationship relationship : relationships) {
                    String label = this.chooseLabelForRelationship(relationship);

                    RexRelationshipDigest relationshipDigest = new RexRelationshipDigest(
                            relationship.getGUID(),
                            label,
                            relationship.getEntityOneProxy().getGUID(),
                            relationship.getEntityTwoProxy().getGUID(),
                            0,
                            0,
                            relationship.getMetadataCollectionName());

                    digestMap.put(relationship.getGUID(), relationshipDigest);

                }


                response = new RexSearchResponse(200, "", serverName, searchText, searchCategory, null, digestMap);

            } else {

                String excMsg = "Could not find any entities that matched "+searchText;
                response = new RexSearchResponse(400, excMsg, serverName, searchText, searchCategory, null, null);

            }

            return response;
        }
        catch (UserNotAuthorizedException e) {

            exceptionMessage = USERNAME_NOT_AUTHORIZED_TO_PERFORM_THE_REQUEST;
        }
        catch (RepositoryErrorException e) {

            exceptionMessage = REPOSITORY_COULD_NOT_BE_REACHED;
        }
        catch (InvalidParameterException e) {

            exceptionMessage = REQUEST_HAS_AN_INVALID_PARAMETER;
        }
        catch (TypeErrorException e) {

            exceptionMessage = THERE_WAS_A_PROBLEM_WITH_TYPE_INFORMATION_PLEASE_CHECK_AND_RETRY;
        }
        catch (PropertyErrorException e) {

            exceptionMessage = THERE_WAS_A_PROBLEM_WITH_PROPERTY_INFORMATION_PLEASE_CHECK_AND_RETRY;
        }
        catch (PagingErrorException e) {

            exceptionMessage = "There was a problem with Paging - please check and retry";
        }
        catch (FunctionNotSupportedException e) {

            exceptionMessage = THE_UI_TRIED_TO_USE_AN_UNSUPPORTED_FUNCTION;
        }
        // For any of the above exceptions, incorporate the exception message into a response object
        response = new RexSearchResponse(400, exceptionMessage, serverName, searchText, searchCategory, null, null);

        return response;

    }



    private List<Relationship> findRelationships(String   userId,
                                                 String   serverName,
                                                 String   serverURLRoot,
                                                 boolean  enterpriseOption,
                                                 String   searchText,
                                                 String   relationshipTypeGUID)
    throws
            UserNotAuthorizedException,
            RepositoryErrorException,
            InvalidParameterException,
            TypeErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException
    {

        String methodName = "findRelationships";

        if (searchText == null) {

            // If no searchText is specified, there is no point in continuing...
            // We have a problem - the searchText has not been specified.
            // We cannot do a query...

            final String parameterName = "searchText";

            throw new InvalidParameterException(RexErrorCode.INVALID_VALUE.getMessageDefinition(searchText, methodName, serverName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        /*
         *  Switch between local and enterprise services clients depending
         *  on enterprise option...
         */
        MetadataCollectionServicesClient repositoryServicesClient;
        if (!enterpriseOption) {
            repositoryServicesClient = this.getLocalRepositoryServicesClient(serverName, serverURLRoot);
        }
        else {
            repositoryServicesClient = this.getEnterpriseRepositoryServicesClient(serverName, serverURLRoot);
        }

        List<Relationship> relationshipList = repositoryServicesClient.findRelationshipsByPropertyValue(
                userId,
                relationshipTypeGUID,
                searchText,
                0,
                null,
                null,
                null,
                null,
                0);


        if (relationshipList != null) {

            return relationshipList;

        } else {

            // No relationships could be found - this is OK...

            return null;

        }

    }
}
