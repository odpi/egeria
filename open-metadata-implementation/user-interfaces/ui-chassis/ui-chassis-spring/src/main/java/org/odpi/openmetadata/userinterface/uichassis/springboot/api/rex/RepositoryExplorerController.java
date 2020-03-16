/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.rex;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
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

import javax.servlet.http.HttpServletRequest;
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

    // TODO - this is artificially qualified as rexTypeExplorer for now - it should really (eventually) be
    // refactored so that there is ONE controller for both tex and rex - or rex should be using the tex
    // REST api....for now keep them separate.
    //
    @PostMapping( path = "/api/types/rexTypeExplorer")
    public TypeExplorerResponse rexTypeExplorer(@RequestBody Map<String,String> body, HttpServletRequest request)
    {

        String serverName        = body.get("serverName");
        String serverURLRoot     = body.get("serverURLRoot");
        boolean enterpriseOption        = false;
        if (body.get("enterpriseOption") != null)
            enterpriseOption            = body.get("enterpriseOption").equals("true");


        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // Look up types in server and construct TEX
        TypeExplorerResponse texResp;
        String exceptionMessage;

        try {

            TypeExplorer tex = this.getTypeExplorer(userId, serverName, serverURLRoot, enterpriseOption);

            if (tex != null) {

                texResp = new TypeExplorerResponse(200, "", tex);

            } else {

                texResp = new TypeExplorerResponse(400, "Could not retrieve type information", null);
            }
            return texResp;
        }
        catch (ConnectionCheckedException | ConnectorCheckedException e) {

            exceptionMessage = "Connector error occurred, please check the server name and URL root parameters";
        }
        catch (UserNotAuthorizedException e) {

            exceptionMessage = "Sorry - this username was not authorized to perform the request";
        }
        catch (RepositoryErrorException e) {

            exceptionMessage = "The repository could not be reached, please check the server name and URL root and verify that the server is running ";
        }
        catch (InvalidParameterException e) {

            exceptionMessage = "The request to load type information reported an invalid parameter, please check the server name and URL root parameters";
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
            ConnectionCheckedException,
            ConnectorCheckedException,
            UserNotAuthorizedException,
            RepositoryErrorException,
            InvalidParameterException
    {

        try {

            this.getMetadataCollection(userId, serverName, serverURLRoot, enterpriseOption);

            TypeExplorer tex = new TypeExplorer();

            TypeDefGallery typeDefGallery = metadataCollection.getAllTypes(userId);

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
        catch (ConnectionCheckedException |
               ConnectorCheckedException  |
               UserNotAuthorizedException |
               RepositoryErrorException   |
               InvalidParameterException e ) {
            throw e;
        }

    }



    private void getMetadataCollection(String  userId,
                                       String  serverName,
                                       String  serverURLRoot,
                                       boolean enterpriseOption)
        throws
            ConnectionCheckedException,
            ConnectorCheckedException,
            RepositoryErrorException

    {

        // TODO - act on the Enterprise Option once it is supported.

        OMRSRepositoryConnector repositoryConnector;
        try {

            repositoryConnector = this.getRepositoryConnector(serverName, serverURLRoot);

        }
        catch (ConnectionCheckedException | ConnectorCheckedException e) {
            throw e;
        }

        if (repositoryConnector != null)  {

            try {

                metadataCollection = repositoryConnector.getMetadataCollection();
                metadataCollectionId = metadataCollection.getMetadataCollectionId(userId);

            }
            catch (RepositoryErrorException exception) {

                String tokens[] = exception.getErrorMessage().split(" on its REST API after it registered with the cohort");

                if (tokens.length > 0) {
                    String frontOfMessageTokens[] = tokens[0].split("returned a metadata collection identifier of ");

                    if (frontOfMessageTokens.length > 1) {
                        metadataCollectionId = frontOfMessageTokens[1];
                        repositoryConnector.setMetadataCollectionId(metadataCollectionId);
                        try {
                            metadataCollection = repositoryConnector.getMetadataCollection();

                        }
                        catch (RepositoryErrorException e) {
                            throw e;
                        }
                    }
                }
            }
        }

        /*
         * Perform integrity checks on metadataCollection
         */
        try {

            boolean error = false;
            if (metadataCollectionId == null) {
                error = true;
            }
            else if (!(metadataCollectionId.equals(metadataCollection.getMetadataCollectionId(userId)))) {
                error = true;
            }

            if (!error) {
                // Successfully located metadataCollection and id matches
                return;
            }
            else {
                final String methodName = "getMetadataCollection";

                OMRSErrorCode errorCode = OMRSErrorCode.NULL_METADATA_COLLECTION;
                String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }

        }
        catch (RepositoryErrorException e) {
            throw e;
        }

    }


    private OMRSRepositoryConnector getRepositoryConnector(String serverName, String serverURLRoot)

            throws
            ConnectionCheckedException,
            ConnectorCheckedException
    {
        try
        {
            ConnectorConfigurationFactory factory = new ConnectorConfigurationFactory();

            /*
             * We do not have an explicit repositoryName here so set repositoryName to serverName
             */
            Connection connection = factory.getDefaultLocalRepositoryRemoteConnection(serverName, serverURLRoot);

            ConnectorBroker connectorBroker = new ConnectorBroker();

            Connector connector = connectorBroker.getConnector(connection);

            OMRSRepositoryConnector  repositoryConnector = (OMRSRepositoryConnector)connector;

            repositoryConnector.setRepositoryName(serverName);
            repositoryConnector.setServerName(serverName);

            /*
             * The metadataCollectionId parameter is not used by the REST connector - but it needs to be non-null and
             * preferably informative so it is meaningful in any error messages and audit log entries.
             */

            repositoryConnector.setMetadataCollectionId("Metadata Collection for repository "+serverName);

            repositoryConnector.start();

            return repositoryConnector;

        }
        catch (ConnectionCheckedException | ConnectorCheckedException e)
        {
            throw e;
        }
    }

    /*
     * This method retrieves the stats affecting a proposed traversal of an instance sub-graph starting from an entity.
     * The response is packaged in a RexPreTraversal object which contains types and counts for relationships and
     * neighboring entities.
     */
    @PostMapping(path = "/api/instances/rex-pre-traversal")
    public RexPreTraversalResponse rexPreTraversal(@RequestBody RexTraversalRequestBody body, HttpServletRequest request)
    {

        /*
         * This may change so that we actually always do the traversal - as in the method below - and return
         * the stats - and then when the user says 'this is the subgraph I want' we filter here in the VS in
         * memory and return the subgraph - so we may wire the pre-traverse to the traversal API and wire
         * the traverse to a post-traversal filtering method (which is purely local to the VS). Make sense?? TODO
         * This is because the VS is always going to ask the MDC to explore the whole (unfiltered) neighborhood
         * so we may as well get it over with. We just don't want to burden the browser/user session with a whole
         * lot of bulked out results if they are being more selective....
         * Rather than implementing in memory in the VS - we could just go again to the backend - refine later if needed.
         * In fact you HAVE to do that because you have no session state in the mid-tier. TODO - update comment!!
         */


        // If a filter typeGUID was not selected in the UI then it will not appear in the body.
        String serverName               = body.getServerName();
        String serverURLRoot            = body.getServerURLRoot();
        Boolean enterpriseOption        = false;    // TODO not used for now

        String entityGUID               = body.getEntityGUID();
        Integer depth                   = body.getDepth();

        // Pre-traversal the filters are always empty
        List<String> entityTypeGUIDs           = null;
        List<String> relationshipTypeGUIDs     = null;
        List<String> classificationNames       = null;


        String userId = getUser(request);

        // Look up types in server and construct TEX
        RexPreTraversalResponse rexPreTraversalResponse;
        String exceptionMessage;


        // No need for a gen on a preTraversal so set up dummy value;
        Integer gen = -1;

        try {

            InstanceGraph instGraph = this.getTraversal(userId, serverName, serverURLRoot, enterpriseOption, entityGUID, entityTypeGUIDs, relationshipTypeGUIDs, classificationNames, depth);

            if (instGraph != null) {

                // Parse the RexTraversal into a RexPreTraversal for the PreTraversalResponse
                RexPreTraversal rexPreTraversal = new RexPreTraversal();


                rexPreTraversal.setEntityGUID(entityGUID);
                rexPreTraversal.setDepth(depth);

                // Process entities
                List<EntityDetail> entities = instGraph.getEntities();
                Map<String,RexTypeStats> entityCountsByType = new HashMap<>();
                Map<String,RexTypeStats> classificationCountsByType = new HashMap<>();
                if (entities != null) {
                    for (EntityDetail ent : entities) {
                        // Process entity type information
                        InstanceType instanceType = ent.getType();
                        String typeGUID = instanceType.getTypeDefGUID();
                        String typeName = instanceType.getTypeDefName();
                        if (entityCountsByType.get(typeName) == null) {
                            // First sight of an instance of this type
                            RexTypeStats stats = new RexTypeStats(typeGUID, 1);
                            entityCountsByType.put(typeName, stats);
                        } else {
                            // Add to the count of instances of this type
                            Integer existingCount = entityCountsByType.get(typeName).getCount();
                            entityCountsByType.get(typeName).setCount(existingCount + 1);
                        }
                        // Process entity classification information
                        List<Classification> classifications = ent.getClassifications();
                        if (classifications != null) {
                            for (Classification classification : classifications) {
                                String classificationName = classification.getName();
                                if (classificationCountsByType.get(classificationName) == null) {
                                    // First sight of an instance of this type
                                    RexTypeStats stats = new RexTypeStats(null, 1);
                                    classificationCountsByType.put(classificationName, stats);
                                } else {
                                    // Add to the count of instances of this type
                                    Integer existingCount = classificationCountsByType.get(classificationName).getCount();
                                    classificationCountsByType.get(classificationName).setCount(existingCount + 1);
                                }
                            }
                        }
                    }
                }
                // Process relationships
                List<Relationship> relationships = instGraph.getRelationships();
                Map<String,RexTypeStats> relationshipCountsByType = new HashMap<>();
                if (relationships != null) {
                    for (Relationship rel : relationships) {
                        InstanceType instanceType = rel.getType();
                        String typeGUID = instanceType.getTypeDefGUID();
                        String typeName = instanceType.getTypeDefName();
                        if (relationshipCountsByType.get(typeName) == null) {
                            // First sight of an instance of this type
                            RexTypeStats stats = new RexTypeStats(typeGUID, 1);
                            relationshipCountsByType.put(typeName, stats);
                        } else {
                            // Add to the count of instances of this type
                            Integer existingCount = relationshipCountsByType.get(typeName).getCount();
                            relationshipCountsByType.get(typeName).setCount(existingCount + 1);
                        }
                    }
                }
                // Update the rexPreTraversal
                rexPreTraversal.setEntityInstanceCounts(entityCountsByType);
                rexPreTraversal.setRelationshipInstanceCounts(relationshipCountsByType);
                rexPreTraversal.setClassificationInstanceCounts(classificationCountsByType);

                rexPreTraversalResponse = new RexPreTraversalResponse(200, "", rexPreTraversal);

            } else {

                String excMsg = "Could not retrieve subgraph for entity with guid"+entityGUID;
                rexPreTraversalResponse = new RexPreTraversalResponse(400, excMsg, null);

            }
            return rexPreTraversalResponse;
        }
        catch (ConnectionCheckedException | ConnectorCheckedException e) {

            exceptionMessage = "Connector error occurred, please check the server name and URL root parameters";
        }
        catch (UserNotAuthorizedException e) {

            exceptionMessage = "Sorry - this username was not authorized to perform the request";
        }
        catch (RepositoryErrorException e) {

            exceptionMessage = "The repository could not be reached, please check the server name and URL root and verify that the server is running ";
        }
        catch (InvalidParameterException e) {

            exceptionMessage = "The request to load type information reported an invalid parameter, please check the server name and URL root parameters";
        }
        catch (EntityNotKnownException e) {

            exceptionMessage = "The system could not find an entity with the GUID specified - please check the GUID ad try again";
        }
        catch (EntityProxyOnlyException e) {

            exceptionMessage = "The system could only find an entity proxy using the GUID specified - please check the GUID ad try again";
        }
        catch (TypeErrorException e) {

            exceptionMessage = "There was a problem with Type information - please check and retry";  // TODO - needs work
        }
        catch (PropertyErrorException e) {

            exceptionMessage = "There was a problem with Property information - please check and retry";  // TODO - needs work
        }
        catch (FunctionNotSupportedException e) {

            exceptionMessage = "The UI tried to use an unsupported function";  // TODO - needs work
        }
        // For any of the above exceptions, incorporate the exception message into a response object
        rexPreTraversalResponse = new RexPreTraversalResponse(400, exceptionMessage, null);

        return rexPreTraversalResponse;

    }

    @PostMapping(path = "/api/instances/rex-traversal")
    public RexTraversalResponse rexTraversal(@RequestBody RexTraversalRequestBody body, HttpServletRequest request)
    {



        // If a filter typeGUID was not selected in the UI then it will not appear in the body.
        String serverName                 = body.getServerName();
        String serverURLRoot              = body.getServerURLRoot();
        Boolean enterpriseOption          = false;   // TODO not used for now

        String entityGUID                 = body.getEntityGUID();
        Integer gen                       = body.getGen();
        Integer depth                     = body.getDepth();

        List<String> entityTypeGUIDs       = body.getEntityTypeGUIDs();
        List<String> relationshipTypeGUIDs = body.getRelationshipTypeGUIDs();
        List<String> classificationNames   = body.getClassificationNames();

        String userId = getUser(request);

        // Look up types in server and construct TEX
        RexTraversalResponse rexTraversalResponse;
        String exceptionMessage;

        TypeDefGallery typeDefGallery = null;

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

                // gex stores the instance graph and root GUID and depth.
                // Reformat the results into a RexTraversalResponse including digests
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


                        RexEntityDigest red = new RexEntityDigest(entGUID, entLabel, gen,entityDetail.getMetadataCollectionName());
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
                        int idx = 0;  // TODO - need to spot matching relationships and inc indx.....

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
                if (entityTypeGUIDs != null && !entityTypeGUIDs.isEmpty())
                for (String entityTypeGUID : entityTypeGUIDs) {
                    // Convert from typeGIUD to typeName
                    String entityTypeName = typeExplorer.getEntityTypeName(entityTypeGUID);
                    entityTypeNames.add(entityTypeName);
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

        catch (ConnectionCheckedException | ConnectorCheckedException e) {

            exceptionMessage = "Connector error occurred, please check the server name and URL root parameters";
        }
        catch (UserNotAuthorizedException e) {

            exceptionMessage = "Sorry - this username was not authorized to perform the request";
        }
        catch (RepositoryErrorException e) {

            exceptionMessage = "The repository could not be reached, please check the server name and URL root and verify that the server is running ";
        }
        catch (InvalidParameterException e) {

            exceptionMessage = "The request to load type information reported an invalid parameter, please check the server name and URL root parameters";
        }
        catch (EntityNotKnownException e) {

            exceptionMessage = "The system could not find an entity with the GUID specified - please check the GUID ad try again";
        }
        catch (EntityProxyOnlyException e) {

            exceptionMessage = "The system could only find an entity proxy using the GUID specified - please check the GUID ad try again";
        }
        catch (TypeErrorException e) {

            exceptionMessage = "There was a problem with Type information - please check and retry";  // TODO - needs work
        }
        catch (PropertyErrorException e) {

            exceptionMessage = "There was a problem with Property information - please check and retry";  // TODO - needs work
        }
        catch (FunctionNotSupportedException e) {

            exceptionMessage = "The UI tried to use an unsupported function";  // TODO - needs work
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
        // TODO this does not yet have the ability to look for more specialized labels at more specific types (lower in the hierarchy). That needs a strategy.
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
                        entityDetail.getProperties().getInstanceProperties().get("tagName") != null &&
                        entityDetail.getProperties().getInstanceProperties().get("tagName").valueAsString() != null) {

                    label = entityDetail.getProperties().getInstanceProperties().get("tagName").valueAsString();
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
                        entityDetail.getProperties().getInstanceProperties().get("dataFieldName") != null &&
                        entityDetail.getProperties().getInstanceProperties().get("dataFieldName").valueAsString() != null) {

                    label = entityDetail.getProperties().getInstanceProperties().get("dataFieldName").valueAsString();
                }
                break;

            case "Annotation":
            case "AnnotationReview":
                if (instanceTypeName != null) {
                    label = instanceTypeName;  // use the local type name for anything under these types
                }
                break;

            case "LastAttachment":
                if (entityDetail.getProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties().get("attachmentType") != null &&
                        entityDetail.getProperties().getInstanceProperties().get("attachmentType").valueAsString() != null) {

                    label = entityDetail.getProperties().getInstanceProperties().get("attachmentType").valueAsString();
                }
                break;

            default:
                // TODO - throughout this labeller method (and the others for proxy and relationship) you can refactor the predicates to be lighter-weight...
                // Anything that is left should be a Referenceable.
                // If it has a displayName use that.
                // Else if it has a name use that.
                // Otherwise if it has a qualifiedName use up to the last TRUNCATED_STRING_LENGTH chars of that.
                // If there is not qualifiedName drop through and use GUID (default)
                if (entityDetail.getProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties().get("displayName") != null &&
                        entityDetail.getProperties().getInstanceProperties().get("displayName").valueAsString() != null) {

                    label = entityDetail.getProperties().getInstanceProperties().get("displayName").valueAsString();
                }
                else if (entityDetail.getProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties().get("name") != null &&
                        entityDetail.getProperties().getInstanceProperties().get("name").valueAsString() != null) {

                    label = entityDetail.getProperties().getInstanceProperties().get("name").valueAsString();
                }
                else if (entityDetail.getProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties() != null &&
                        entityDetail.getProperties().getInstanceProperties().get("qualifiedName") != null) {

                    String fullQN = entityDetail.getProperties().getInstanceProperties().get("qualifiedName").valueAsString();

                    int lengthQN = fullQN.length();
                    if (lengthQN > TRUNCATED_STRING_LENGTH) {
                        // TODO - consider tokenizing and taking meaningful segment(s)
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
        // TODO this does not yet have the ability to look for more specialized labels at more specific types (lower in the hierarchy). That needs a strategy.
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
                        entityProxy.getUniqueProperties().getInstanceProperties().get("tagName") != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get("tagName").valueAsString() != null) {

                    label = entityProxy.getUniqueProperties().getInstanceProperties().get("tagName").valueAsString();

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
                        entityProxy.getUniqueProperties().getInstanceProperties().get("dataFieldName") != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get("dataFieldName").valueAsString() != null) {

                    label = entityProxy.getUniqueProperties().getInstanceProperties().get("dataFieldName").valueAsString();

                }
                break;

            case "Annotation":
            case "AnnotationReview":
                if (instanceTypeName != null) {
                    label = instanceTypeName;  // use the local type name for anything under these types

                }
                break;

            default:
                // Anything that is left should be a Referenceable.
                // If it has a displayName use that.
                // Otherwise if it has a qualifiedName use up to the last TRUNCATED_STRING_LENGTH chars of that.
                // If there is not qualifiedName drop through and use GUID (default)
                if (entityProxy.getUniqueProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get("displayName") != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get("displayName").valueAsString() != null) {

                    label = entityProxy.getUniqueProperties().getInstanceProperties().get("displayName").valueAsString();

                }
                else if (entityProxy.getUniqueProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get("name") != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get("name").valueAsString() != null) {

                    label = entityProxy.getUniqueProperties().getInstanceProperties().get("name").valueAsString();

                }
                else if (entityProxy.getUniqueProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties() != null &&
                        entityProxy.getUniqueProperties().getInstanceProperties().get("qualifiedName") != null) {

                    String fullQN = entityProxy.getUniqueProperties().getInstanceProperties().get("qualifiedName").valueAsString();

                    int lengthQN = fullQN.length();
                    if (lengthQN > TRUNCATED_STRING_LENGTH) {
                        // TODO - consider tokenizing and taking meaningful segment(s)
                        String tailQN = "..." + fullQN.substring(lengthQN-TRUNCATED_STRING_LENGTH, lengthQN);
                        label = tailQN;
                    } else {
                        label = fullQN;
                    }
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
    // TODO - may want to remove the depth 0 entity only part of this - you should never hit a traversal with depth 0.
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
            ConnectionCheckedException,
            ConnectorCheckedException,
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


        // TODO - add status filters

        // If no entityGUID is specified or depth is not positive, there is no point in continuing...
        if (entityGUID == null || depth < 0 ) {
            // We have a problem - the entityGUID has not been specified or the depth is negative.
            // In either case we cannot do a query...

            OMRSErrorCode errorCode = OMRSErrorCode.NO_GUID;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityGUID, methodName, serverName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }

        try {

            this.getMetadataCollection(userId, serverName, serverURLRoot, enterpriseOption);

            if (depth >0) {

                InstanceGraph instGraph = metadataCollection.getEntityNeighborhood(
                        userId,
                        entityGUID,
                        entityTypeGUIDs,
                        relationshipTypeGUIDs,
                        null,   // TODO List< InstanceStatus > limitResultsByStatus,
                        classificationNames,
                        null,
                        depth);


                if (instGraph != null) {

                    return instGraph;
                }
                else {

                    return null;
                }
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

                    OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_NOT_KNOWN;

                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityGUID, methodName, serverName);

                    throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                }
            }
        }
        catch (ConnectionCheckedException |
                ConnectorCheckedException  |
                UserNotAuthorizedException |
                EntityProxyOnlyException |
                RepositoryErrorException   |
                EntityNotKnownException |
                TypeErrorException |
                PropertyErrorException |
                FunctionNotSupportedException |
                InvalidParameterException e ) {
            throw e;
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
    //@RequestMapping(method = RequestMethod.GET, path = "/api/instances/entity")
    public RexEntityDetailResponse getEntityDetail(@RequestBody  RexEntityRequestBody   body,
                                                   HttpServletRequest                  request)
    {

        String serverName               = body.getServerName();
        String serverURLRoot            = body.getServerURLRoot();
        Boolean enterpriseOption        = body.getEnterpriseOption();
        String entityGUID               = body.getEntityGUID();

        RexEntityDetailResponse response;
        String exceptionMessage;

        String userId = getUser(request);

        try {

            EntityDetail entityDetail = this.getEntityDetail(userId, serverName, serverURLRoot, enterpriseOption, entityGUID);

            if (entityDetail != null) {

                TypeExplorer typeExplorer = getTypeExplorer(userId, serverName, serverURLRoot, enterpriseOption);
                String label = this.chooseLabelForEntity(entityDetail, typeExplorer);

                RexExpandedEntityDetail rexExpEntityDetail = new RexExpandedEntityDetail(entityDetail, label, serverName);

                response = new RexEntityDetailResponse(200, "", rexExpEntityDetail);

            } else {

                String excMsg = "Could not retrieve entity with guid"+entityGUID;
                response = new RexEntityDetailResponse(400, excMsg, null);

            }

            return response;
        }
        catch (ConnectionCheckedException | ConnectorCheckedException e) {

            exceptionMessage = "Connector error occurred, please check the server name and URL root parameters";
        }
        catch (UserNotAuthorizedException e) {

            exceptionMessage = "Sorry - this username was not authorized to perform the request";
        }
        catch (RepositoryErrorException e) {

            exceptionMessage = "The repository could not be reached, please check the server name and URL root and verify that the server is running ";
        }
        catch (InvalidParameterException e) {

            exceptionMessage = "The request to load type information reported an invalid parameter, please check the server name and URL root parameters";
        }
        catch (EntityNotKnownException e) {

            exceptionMessage = "The system could not find an entity with the GUID specified - please check the GUID ad try again";
        }
        catch (EntityProxyOnlyException e) {

            exceptionMessage = "The system could only find an entity proxy using the GUID specified - please check the GUID ad try again";
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
    ConnectionCheckedException,
    ConnectorCheckedException,
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

            OMRSErrorCode errorCode = OMRSErrorCode.NO_GUID;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityGUID, methodName, serverName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction());

        }

        try {

            this.getMetadataCollection(userId, serverName, serverURLRoot, enterpriseOption);


            EntityDetail entityDetail = metadataCollection.getEntityDetail(
                    userId,
                    entityGUID);

            if (entityDetail != null) {

                return entityDetail;

            } else {

                // Entity could not be found - should have already had an exception but just to be sure...

                OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_NOT_KNOWN;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityGUID, methodName, serverName);

                throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                                                  this.getClass().getName(),
                                                  methodName,
                                                  errorMessage,
                                                  errorCode.getSystemAction(),
                                                  errorCode.getUserAction());
            }
        }
        catch (ConnectionCheckedException |
                ConnectorCheckedException  |
                UserNotAuthorizedException |
                EntityProxyOnlyException |
                RepositoryErrorException   |
                EntityNotKnownException |
                InvalidParameterException e ) {
            throw e;
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
    public RexRelationshipResponse getRelationship(@RequestBody  RexRelationshipRequestBody   body,
                                                   HttpServletRequest                         request)
    {


        String serverName               = body.getServerName();
        String serverURLRoot            = body.getServerURLRoot();
        boolean enterpriseOption        = body.getEnterpriseOption();
        String relationshipGUID         = body.getRelationshipGUID();

        RexRelationshipResponse response;
        String exceptionMessage;

        String userId = getUser(request);

        try {

            Relationship relationship = this.getRelationship(userId, serverName, serverURLRoot, enterpriseOption, relationshipGUID);

            if (relationship != null) {

                // Create digests for both ends

                TypeExplorer typeExplorer = getTypeExplorer(userId, serverName, serverURLRoot, enterpriseOption);
                EntityProxy entity1 = relationship.getEntityOneProxy();
                EntityProxy entity2 = relationship.getEntityTwoProxy();
                String label1 = this.chooseLabelForEntityProxy(entity1, typeExplorer);
                String label2 = this.chooseLabelForEntityProxy(entity2, typeExplorer);

                RexEntityDigest digest1 = new RexEntityDigest(entity1.getGUID(),label1,0, entity1.getMetadataCollectionName());
                RexEntityDigest digest2 = new RexEntityDigest(entity2.getGUID(),label2,0, entity2.getMetadataCollectionName());

                String label = this.chooseLabelForRelationship(relationship);

                RexExpandedRelationship rexExpRelationship = new RexExpandedRelationship(relationship, label, digest1, digest2, serverName);

                response = new RexRelationshipResponse(200, "", rexExpRelationship);

            }
            else {

                String excMsg = "Could not retrieve relationship with guid"+relationshipGUID;
                response = new RexRelationshipResponse(400, excMsg, null);

            }

            return response;
        }
        catch (ConnectionCheckedException | ConnectorCheckedException e) {

            exceptionMessage = "Connector error occurred, please check the server name and URL root parameters";
        }
        catch (UserNotAuthorizedException e) {

            exceptionMessage = "Sorry - this username was not authorized to perform the request";
        }
        catch (RepositoryErrorException e) {

            exceptionMessage = "The repository could not be reached, please check the server name and URL root and verify that the server is running ";
        }
        catch (InvalidParameterException e) {

            exceptionMessage = "The request to load type information reported an invalid parameter, please check the server name and URL root parameters";
        }
        catch (RelationshipNotKnownException e) {

            exceptionMessage = "The system could not find an relationship with the GUID specified - please check the GUID ad try again";
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
    ConnectionCheckedException,
    ConnectorCheckedException,
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

            OMRSErrorCode errorCode = OMRSErrorCode.NO_GUID;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationshipGUID, methodName, serverName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction());

        }

        try {

            this.getMetadataCollection(userId, serverName, serverURLRoot, enterpriseOption);

            Relationship relationship = metadataCollection.getRelationship(
                    userId,
                    relationshipGUID);

            if (relationship != null) {

                return relationship;

            } else {

                // Entity could not be found - should have already had an exception but just to be sure...

                OMRSErrorCode errorCode = OMRSErrorCode.RELATIONSHIP_NOT_KNOWN;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationshipGUID, methodName, serverName);

                throw new RelationshipNotKnownException(errorCode.getHTTPErrorCode(),
                                                  this.getClass().getName(),
                                                  methodName,
                                                  errorMessage,
                                                  errorCode.getSystemAction(),
                                                  errorCode.getUserAction());
            }
        }
        catch (ConnectionCheckedException |
                ConnectorCheckedException  |
                UserNotAuthorizedException |
                RepositoryErrorException   |
                RelationshipNotKnownException |
                InvalidParameterException e ) {
            throw e;
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
    public RexSearchResponse entitySearch(@RequestBody  RexSearchBody   body,
                                          HttpServletRequest            request)
    {


        String serverName               = body.getServerName();
        String serverURLRoot            = body.getServerURLRoot();
        Boolean enterpriseOption        = body.getEnterpriseOption();
        String entityTypeName           = body.getTypeName();
        String searchText               = body.getSearchText();

        String searchCategory = "Entity";


        RexSearchResponse response;
        String exceptionMessage;

        String userId = getUser(request);

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
        catch (ConnectionCheckedException | ConnectorCheckedException e) {

            exceptionMessage = "Connector error occurred, please check the server name and URL root parameters";
        }
        catch (UserNotAuthorizedException e) {

            exceptionMessage = "Sorry - this username was not authorized to perform the request";
        }
        catch (RepositoryErrorException e) {

            exceptionMessage = "The repository could not be reached, please check the server name and URL root and verify that the server is running ";
        }
        catch (InvalidParameterException e) {

            exceptionMessage = "The request to load type information reported an invalid parameter, please check the server name and URL root parameters";
        }
        catch (TypeErrorException e) {

            exceptionMessage = "There was a problem with Type information - please check and retry";  // TODO - needs work
        }
        catch (PropertyErrorException e) {

            exceptionMessage = "There was a problem with Property information - please check and retry";  // TODO - needs work
        }
        catch (PagingErrorException e) {

            exceptionMessage = "There was a problem with Paging - please check and retry";  // TODO - needs work
        }
        catch (FunctionNotSupportedException e) {

            exceptionMessage = "The UI tried to use an unsupported function";  // TODO - needs work
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
            // TODO - check exception list
    ConnectionCheckedException,
    ConnectorCheckedException,
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
            // In either case we cannot do a query...

            OMRSErrorCode errorCode = OMRSErrorCode.NO_GUID;   // TODO - correct error code please!!

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(searchText, methodName, serverName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction());

        }

        try {

            this.getMetadataCollection(userId, serverName, serverURLRoot, enterpriseOption);

            List<EntityDetail> entityList = metadataCollection.findEntitiesByPropertyValue(
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


            if (entityList != null) {

                return entityList;

            } else {

                // No entities could be found - this is OK...

                return null;

            }
        }
        catch (ConnectionCheckedException |
                ConnectorCheckedException  |
                UserNotAuthorizedException |
                RepositoryErrorException   |
                PagingErrorException |
                InvalidParameterException e ) {
            throw e;
        }

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
    public RexSearchResponse relationshipSearch(@RequestBody  RexSearchBody    body,
                                                HttpServletRequest             request)
    {

        String serverName               = body.getServerName();
        String serverURLRoot            = body.getServerURLRoot();
        Boolean enterpriseOption        = body.getEnterpriseOption();
        String relationshipTypeName     = body.getTypeName();
        String searchText               = body.getSearchText();

        String searchCategory = "Relationship";


        RexSearchResponse response;
        String exceptionMessage;

        String userId = getUser(request);

        String relationshipTypeGUID = null;

        // Convert type name to typeGUID.
        try {

            TypeExplorer typeExplorer = getTypeExplorer(userId, serverName, serverURLRoot, enterpriseOption);
            relationshipTypeGUID = typeExplorer.getRelationshipTypeGUID(relationshipTypeName);

            List<Relationship> relationships = this.findRelationships(userId, serverName, serverURLRoot, enterpriseOption, searchText, relationshipTypeGUID);

            if (relationships != null) {

                // Process the list of Relationship objects and produce a list of RelationshipDigest objects

                Map<String, RexRelationshipDigest> digestMap = new HashMap<>();

                for (int e=0; e < relationships.size(); e++) {
                    Relationship relationship = relationships.get(e);
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
        catch (ConnectionCheckedException | ConnectorCheckedException e) {

            exceptionMessage = "Connector error occurred, please check the server name and URL root parameters";
        }
        catch (UserNotAuthorizedException e) {

            exceptionMessage = "Sorry - this username was not authorized to perform the request";
        }
        catch (RepositoryErrorException e) {

            exceptionMessage = "The repository could not be reached, please check the server name and URL root and verify that the server is running ";
        }
        catch (InvalidParameterException e) {

            exceptionMessage = "The request to load type information reported an invalid parameter, please check the server name and URL root parameters";
        }
        catch (TypeErrorException e) {

            exceptionMessage = "There was a problem with Type information - please check and retry";  // TODO - needs work
        }
        catch (PropertyErrorException e) {

            exceptionMessage = "There was a problem with Property information - please check and retry";  // TODO - needs work
        }
        catch (PagingErrorException e) {

            exceptionMessage = "There was a problem with Paging - please check and retry";  // TODO - needs work
        }
        catch (FunctionNotSupportedException e) {

            exceptionMessage = "The UI tried to use an unsupported function";  // TODO - needs work
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
    // TODO - check exception list
            ConnectionCheckedException,
            ConnectorCheckedException,
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
            // In either case we cannot do a query...

            OMRSErrorCode errorCode = OMRSErrorCode.NO_GUID;   // TODO - correct error code please!!

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(searchText, methodName, serverName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction());

        }

        try {

            this.getMetadataCollection(userId, serverName, serverURLRoot, enterpriseOption);

            List<Relationship> relationshipList = metadataCollection.findRelationshipsByPropertyValue(
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
        catch (ConnectionCheckedException |
                ConnectorCheckedException  |
                UserNotAuthorizedException |
                RepositoryErrorException   |
                PagingErrorException |
                InvalidParameterException e ) {
            throw e;
        }

    }
}
