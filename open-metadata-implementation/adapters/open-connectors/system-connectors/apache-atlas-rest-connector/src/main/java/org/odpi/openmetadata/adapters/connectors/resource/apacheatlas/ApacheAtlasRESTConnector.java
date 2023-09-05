/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas;

import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.ffdc.*;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.ffdc.NameConflictException;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasBusinessMetadataDef;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasClassificationDef;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasEntity;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasEntityDef;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasEntityHeader;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasEntityMutationResponse;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasEntityOperation;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasEntityWithExtInfo;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasGlossaryCategoryElement;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasGlossaryElement;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasGlossaryTermElement;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasInstanceStatus;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasRelationship;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasRelationshipDef;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasSearchResult;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasTypesDef;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.factory.RESTClientFactory;
import org.odpi.openmetadata.adapters.connectors.restclients.spring.SpringRESTClientConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.springframework.core.ParameterizedTypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * RESTClient is responsible for issuing calls to the OMAS REST APIs.
 */
public class ApacheAtlasRESTConnector extends ConnectorBase implements AuditLoggingComponent,
                                                                       VirtualConnectorExtension
{
    private AuditLog                  auditLog            = null;
    private String                    atlasServerName    = "Apache Atlas";
    private String                    targetRootURL      = null;
    private List<Connector>           embeddedConnectors = null;
    private String              connectorName   = "Apache Atlas REST Connector";
    private RESTClientConnector clientConnector = null;


    /**
     * Default Constructor used by the connector provider.
     */
    public ApacheAtlasRESTConnector()
    {
    }
    

    /* ==============================================================================
     * Standard methods that trigger activity.
     */

    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Return the component description that is used by this connector in the audit log.
     *
     * @return id, name, description, wiki page URL.
     */
    @Override
    public ComponentDescription getConnectorComponentDescription()
    {
        if ((this.auditLog != null) && (this.auditLog.getReport() != null))
        {
            return auditLog.getReport().getReportingComponent();
        }

        return null;
    }


    /**
     * Set up the list of connectors that this virtual connector will use to support its interface.
     * The connectors are initialized waiting to start.  When start() is called on the
     * virtual connector, it needs to pass start() to each of the embedded connectors. Similarly for
     * disconnect().
     *
     * @param embeddedConnectors  list of connectors
     */
    @Override
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors)
    {
        this.embeddedConnectors = embeddedConnectors;
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";
        
        if ((connectionProperties.getUserId() == null) || (connectionProperties.getClearPassword() == null))
        {
            throw new ConnectorCheckedException(ApacheAtlasErrorCode.NULL_USER.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }
        
        if (connectionProperties.getConnectionName() != null)
        {
            connectorName = connectionProperties.getConnectionName();
        }

        /*
         * Retrieve the configuration
         */
        EndpointProperties endpoint = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            targetRootURL = endpoint.getAddress();
        }

        if (targetRootURL == null)
        {
            throw new ConnectorCheckedException(ApacheAtlasErrorCode.NULL_URL.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();

        if (configurationProperties != null)
        {
            if (configurationProperties.get(ApacheAtlasRESTProvider.ATLAS_SERVER_NAME_CONFIGURATION_PROPERTY) != null)
            {
                atlasServerName = configurationProperties.get(ApacheAtlasRESTProvider.ATLAS_SERVER_NAME_CONFIGURATION_PROPERTY).toString();
            }
        }

        try
        {
            /*
             * Create the client that calls Apache Atlas.
             */
            RESTClientFactory  factory = new RESTClientFactory(atlasServerName,
                                                               targetRootURL,
                                                               connectionProperties.getUserId(),
                                                               connectionProperties.getClearPassword());

            this.clientConnector = factory.getClientConnector();
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      ApacheAtlasAuditCode.BAD_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                  error.getClass().getName(),
                                                                                                  targetRootURL,
                                                                                                  methodName,
                                                                                                  error.getMessage()),
                                      error);
            }

            throw new ConnectorCheckedException(ApacheAtlasErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }
    

    /**
     * Return the types registered to Apache Atlas.
     *
     * @return type definition list from Apache Atlas
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public AtlasTypesDef getAllTypes() throws PropertyServerException
    {
        final String methodName = "getAllTypes()";
        final String url = targetRootURL + "/api/atlas/v2/types/typedefs";

        return this.callGetRESTCallNoParams(methodName, AtlasTypesDef.class, url);
    }


    /**
     * Return the requested type from Apache Atlas.
     *
     * @param typeName name of type to retrieve
     * @return type definition from Apache Atlas
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public AtlasEntityDef getEntityType(String typeName) throws PropertyServerException
    {
        final String methodName = "getEntityTypes(" + typeName + "}";
        final String url = targetRootURL + "/api/atlas/v2/types/entitydef/name/" + typeName;

        return this.callNoLogGetRESTCallNoParams(methodName, AtlasEntityDef.class, url);
    }


    /**
     * Return the requested type from Apache Atlas.
     *
     * @param typeName name of type to retrieve
     * @return type definition from Apache Atlas
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public AtlasRelationshipDef getRelationshipType(String typeName) throws PropertyServerException
    {
        final String methodName = "getRelationshipTypes(" + typeName + "}";
        final String url = targetRootURL + "/api/atlas/v2/types/relationshipdef/name/" + typeName;

        return this.callNoLogGetRESTCallNoParams(methodName, AtlasRelationshipDef.class, url);
    }


    /**
     * Return the requested type from Apache Atlas.
     *
     * @param typeName name of type to retrieve
     * @return type definition from Apache Atlas
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public AtlasClassificationDef getClassificationType(String typeName) throws PropertyServerException
    {
        final String methodName = "getClassificationType(" + typeName + "}";
        final String url = targetRootURL + "/api/atlas/v2/types/classificationdef/name/" + typeName;

        return this.callGetRESTCallNoParams(methodName, AtlasClassificationDef.class, url);
    }


    /**
     * Return the requested type from Apache Atlas.
     *
     * @param typeName name of type to retrieve
     * @return type definition from Apache Atlas
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public AtlasBusinessMetadataDef getBusinessMetadataType(String typeName) throws PropertyServerException
    {
        final String methodName = "getBusinessMetadataType(" + typeName + "}";
        final String url = targetRootURL + "/api/atlas/v2/types/businessmetadatadef/name/" + typeName;

        return this.callGetRESTCallNoParams(methodName, AtlasBusinessMetadataDef.class, url);
    }


    /**
     * Create new types.  The registered types are returned from Apache Atlas.
     *
     * @param newTypeDefinitions gallery of new types
     * @return type definition list from Apache Atlas
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public AtlasTypesDef addNewTypes(AtlasTypesDef newTypeDefinitions) throws PropertyServerException
    {
        final String methodName = "addNewTypes()";
        final String url = targetRootURL + "/api/atlas/v2/types/typedefs";

        return this.callPostRESTCallNoParams(methodName, AtlasTypesDef.class, url, newTypeDefinitions);
    }


    /**
     * Add an entity.
     *
     * @param entity description of entity
     * @return description of the operation
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public String addEntity(AtlasEntity entity) throws PropertyServerException
    {
        final String methodName = "addEntity()";
        final String url = targetRootURL + "/api/atlas/v2/entity";

        AtlasEntityWithExtInfo atlasEntityWithExtInfo = new AtlasEntityWithExtInfo();

        atlasEntityWithExtInfo.setEntity(entity);

        AtlasEntityMutationResponse response = this.callPostRESTCallNoParams(methodName, AtlasEntityMutationResponse.class, url, atlasEntityWithExtInfo);

        if ((response != null) && (response.getMutatedEntities() != null))
        {
            List<AtlasEntityHeader> returnedEntities = response.getMutatedEntities().get(AtlasEntityOperation.CREATE);

            if ((returnedEntities != null) && (! returnedEntities.isEmpty()))
            {
                return returnedEntities.get(0).getGuid();
            }
        }

        return null;
    }


    /**
     * Update an entity's properties.
     *
     * @param entityWithExtInfo description of entity and any associated entities
     * @return updated entity
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public AtlasEntityWithExtInfo updateEntity(AtlasEntityWithExtInfo  entityWithExtInfo) throws PropertyServerException
    {
        final String methodName = "updateEntity()";
        final String url = targetRootURL + "/api/atlas/v2/entity";

        this.callPostRESTCallNoParams(methodName, AtlasEntityMutationResponse.class, url, entityWithExtInfo);

        return this.getEntityByGUID(entityWithExtInfo.getEntity().getGuid());
    }


    /**
     * Delete an entity.
     *
     * @param entityGUID unique identifier of entity and any associated entities
     * @return description of the operation
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public AtlasEntityMutationResponse deleteEntity(String entityGUID) throws PropertyServerException
    {
        final String methodName = "deleteEntity()";
        final String url = targetRootURL + "/api/atlas/v2/entity/guid/{0}";

        return this.callDeleteRESTCall(methodName, AtlasEntityMutationResponse.class, url, entityGUID);
    }


    /**
     * Add a relationship.
     *
     * @param atlasRelationship description of relationship
     * @return description of the operation
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public AtlasRelationship addRelationship(AtlasRelationship  atlasRelationship) throws PropertyServerException
    {
        final String methodName = "addRelationship()";
        final String url = targetRootURL + "/api/atlas/v2/relationship";

        return this.callPostRESTCallNoParams(methodName, AtlasRelationship.class, url, atlasRelationship);
    }


    /**
     * Remove a relationship.
     *
     * @param atlasRelationshipGUID relationship to delete
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public void clearRelationship(String  atlasRelationshipGUID) throws PropertyServerException
    {
        final String methodName = "clearRelationship()";
        final String url = targetRootURL + "/api/atlas/v2/relationship/" + atlasRelationshipGUID;

        this.callDeleteRESTCall(methodName, url);
    }


    /**
     * Check that the entity is ACTIVE - otherwise return null.
     *
     * @param retrievedEntity entity retrieved from the Apache Atlas repository
     * @return retrieved entity or null
     */
    private AtlasEntityWithExtInfo validateActiveEntity(AtlasEntityWithExtInfo retrievedEntity)
    {
        if ((retrievedEntity != null) &&
            (retrievedEntity.getEntity() != null) &&
            (retrievedEntity.getEntity().getStatus() == AtlasInstanceStatus.ACTIVE))
        {
            return retrievedEntity;
        }

        return null;
    }


    /**
     * Retrieve a single entity by GUID.
     *
     * @param guid unique identifier
     * @return description of requested entity
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public AtlasEntityWithExtInfo getEntityByGUID(String guid) throws PropertyServerException
    {
        final String methodName = "getEntity(" + guid + ")";
        final String url = targetRootURL + "/api/atlas/v2/entity/guid/" + guid;

        AtlasEntityWithExtInfo entity = this.callGetRESTCallNoParams(methodName, AtlasEntityWithExtInfo.class, url);

        return validateActiveEntity(entity);
    }


    /**
     * Retrieve the entity at the other end of the named relationship.
     *
     * @param startingEntity entity information detailing the entity proxy for the entity at the far end of a named relationship.
     * @param relationshipLabel name of relationship to traverse
     *
     * @return description of requested entity
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public AtlasEntityWithExtInfo getRelatedEntity(AtlasEntityWithExtInfo startingEntity,
                                                   String                 relationshipLabel) throws  PropertyServerException
    {
        if ((startingEntity != null) &&
            (startingEntity.getEntity() != null))
        {
            return getRelatedEntity(startingEntity.getEntity(), relationshipLabel);
        }

        return null;
    }


    /**
     * Retrieve the named relationship.
     *
     * @param startingEntity entity information detailing the entity proxy for the entity at the far end of a named relationship.
     * @param relationshipLabel name of relationship to traverse
     *
     * @return description of requested entity
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public String getRelationshipGUID(AtlasEntityWithExtInfo startingEntity,
                                      String                 relationshipLabel) throws  PropertyServerException
    {
        if ((startingEntity != null) && (startingEntity.getEntity() != null) && (startingEntity.getEntity().getRelationshipAttributes() != null))
        {
            Map<String, Object> relationshipAttributes = startingEntity.getEntity().getRelationshipAttributes();

            Object relatedObject = relationshipAttributes.get(relationshipLabel);

            if (relatedObject instanceof Map<?,?> relationship)
            {
                return relationship.get("relationshipGuid").toString();
            }
        }

        return null;
    }


    /**
     * Retrieve the entity at the other end of the named relationship.
     *
     * @param startingEntity entity information detailing the entity proxy for the entity at the far end of a named relationship.
     * @param relationshipLabel name of relationship to traverse
     *
     * @return description of requested entity
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public AtlasEntityWithExtInfo getRelatedEntity(AtlasEntity startingEntity,
                                                   String      relationshipLabel) throws  PropertyServerException
    {
        if ((startingEntity != null) && (startingEntity.getRelationshipAttributes() != null))
        {
            Map<String, Object> relationshipAttributes = startingEntity.getRelationshipAttributes();

            Object relatedObject = relationshipAttributes.get(relationshipLabel);

            if (relatedObject instanceof Map<?,?> relationship)
            {
                String relatedEntityGUID = relationship.get("guid").toString();

                if (relatedEntityGUID != null)
                {
                    return getEntityByGUID(relatedEntityGUID);
                }
            }
        }

        return null;
    }

    /**
     * Retrieve the entities at the other end of the named relationship and build a map of the relationships.
     *
     * @param startingEntity entity information detailing the entity proxy for the entity at the far end of a named relationship.
     * @param relationshipLabel name of relationship to traverse
     *
     * @return list of related entity GUIDs mapped to their relationship GUID
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public Map<String, String> getRelationships(AtlasEntityWithExtInfo startingEntity,
                                                String                 relationshipLabel) throws  PropertyServerException
    {
        Map<String, String> guidMap = new HashMap<>();

        if ((startingEntity != null) && (startingEntity.getEntity().getRelationshipAttributes() != null))
        {
            Map<String, Object> relationshipAttributes = startingEntity.getEntity().getRelationshipAttributes();

            Object relationshipObjects = relationshipAttributes.get(relationshipLabel);

            if (relationshipObjects instanceof List<?> relationshipList)
            {
                for (Object relationship : relationshipList)
                {
                    if (relationship instanceof Map<?,?> relationshipMap)
                    {
                        String relatedEntityGUID = relationshipMap.get("guid").toString();
                        String relationshipGUID = relationshipMap.get("relationshipGuid").toString();

                        if (relatedEntityGUID != null)
                        {
                            guidMap.put(relatedEntityGUID, relationshipGUID);
                        }
                    }
                }
            }
        }

        return guidMap;
    }



    /**
     * Retrieve the entities at the other end of the named relationship.
     *
     * @param startingEntity entity information detailing the entity proxy for the entity at the far end of a named relationship.
     * @param relationshipLabel name of relationship to traverse
     *
     * @return list of related entities
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public List<AtlasEntityWithExtInfo> getRelatedEntities(AtlasEntityWithExtInfo startingEntity,
                                                           String                 relationshipLabel) throws  PropertyServerException
    {
        List<AtlasEntityWithExtInfo> results = new ArrayList<>();

        if ((startingEntity != null) &&
            (startingEntity.getEntity() != null) &&
            (startingEntity.getEntity().getRelationshipAttributes() != null))
        {
            Map<String, Object> relationshipAttributes = startingEntity.getEntity().getRelationshipAttributes();

            Object relatedObjects = relationshipAttributes.get(relationshipLabel);

            if (relatedObjects instanceof List<?> relatedObjectList)
            {
                for (Object relatedObject : relatedObjectList)
                {
                    if (relatedObject instanceof Map<?,?> relatedMappedAttributes)
                    {
                        String relatedEntityGUID = relatedMappedAttributes.get("guid").toString();

                        if (relatedEntityGUID != null)
                        {
                            AtlasEntityWithExtInfo validatedEntity = validateActiveEntity(getEntityByGUID(relatedEntityGUID));

                            if (validatedEntity != null)
                            {
                                results.add(validatedEntity);
                            }
                        }
                    }
                }
            }
        }

        return results;
    }


    /**
     * Return a list of entities that are of the requested type.
     *
     * @param typeName name of the type to query
     * @param startFrom offset to start results
     * @param pageSize max number of results
     * @return list of matching entities. List may be empty if no matches
     * @throws PropertyServerException problem communicating with Apache Atlas
     */
    public List<AtlasEntityHeader> getEntitiesForType(String typeName,
                                                           int    startFrom,
                                                           int    pageSize) throws PropertyServerException
    {
        final String methodName = "getEntity(" + typeName + ")";
        final String url = targetRootURL + "/api/atlas/v2/search/dsl?typeName=" + typeName + "&offset=" + startFrom + "&limit=" + pageSize;


        AtlasSearchResult searchResult = this.callGetRESTCallNoParams(methodName, AtlasSearchResult.class, url);

        if ((searchResult != null) && (searchResult.getEntities() != null))
        {
            List<AtlasEntityHeader> results = new ArrayList<>();

            for (AtlasEntityHeader entityHeader : searchResult.getEntities())
            {
                if ((entityHeader != null) && (entityHeader.getStatus() == AtlasInstanceStatus.ACTIVE))
                {
                    results.add(entityHeader);
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Return a glossary based on the paging count.
     *
     * @param glossaryCount position of the glossary in the paging list.
     * @return glossary or null
     * @throws PropertyServerException problem with the request
     */
    @SuppressWarnings(value = "unchecked")
    public AtlasGlossaryElement getAtlasGlossary(int glossaryCount) throws PropertyServerException
    {
        final String methodName = "getAtlasGlossary(glossaryCount)";
        final String url = targetRootURL + "/api/atlas/v2/glossary?limit=1&offset=" + glossaryCount + "&sort=ASC";


        List<LinkedHashMap<String, Object>> glossaryElements = (ArrayList<LinkedHashMap<String, Object>>)this.callGetRESTCallNoParams(methodName, ArrayList.class, url);

        if ((glossaryElements != null) && (! glossaryElements.isEmpty()))
        {
            LinkedHashMap<String, Object> requestedGlossary = glossaryElements.get(0);

            return this.getAtlasGlossary(requestedGlossary.get("guid").toString());
        }

        return null;
    }


    /**
     * Return a glossary based on its guid.
     *
     * @param glossaryGUID unique identifier of the glossary.
     * @return glossary or null
     * @throws PropertyServerException problem with the request
     */
    public AtlasGlossaryElement getAtlasGlossary(String glossaryGUID) throws PropertyServerException
    {
        final String methodName = "getAtlasGlossary(glossaryGUID)";
        final String url = targetRootURL + "/api/atlas/v2/glossary/" + glossaryGUID;

        return this.callGetRESTCallNoParams(methodName, AtlasGlossaryElement.class, url);
    }


    /**
     * Create a new empty glossary.
     *
     * @param glossary glossary to create
     * @return glossaryGUID
     * @throws PropertyServerException problem with the request
     */
    public String createAtlasGlossary(AtlasGlossaryElement glossary) throws PropertyServerException
    {
        final String methodName = "createAtlasGlossary(glossary)";
        final String url = targetRootURL + "/api/atlas/v2/glossary";

        AtlasGlossaryElement newGlossary = this.callPostRESTCallNoParams(methodName, AtlasGlossaryElement.class, url, glossary);

        if (newGlossary != null)
        {
            return newGlossary.getGuid();
        }

        return null;
    }


    /**
     * Save a glossary with all of its links to terms and categories.
     *
     * @param glossary glossary to create/update
     * @return glossary or null
     * @throws PropertyServerException problem with the request
     */
    public AtlasGlossaryElement saveAtlasGlossary(AtlasGlossaryElement glossary) throws PropertyServerException
    {
        final String methodName = "saveAtlasGlossary(glossary)";
        final String url = targetRootURL + "/api/atlas/v2/glossary/{0}";

        return this.callPutRESTCall(methodName, AtlasGlossaryElement.class, url, glossary, glossary.getGuid());
    }



    /**
     * Delete a glossary with all of its links to terms and categories.
     *
     * @param glossary glossary to delete
     * @throws PropertyServerException problem with the request
     */
    public void deleteAtlasGlossary(AtlasGlossaryElement glossary) throws PropertyServerException
    {
        final String methodName = "deleteAtlasGlossary()";
        final String url = targetRootURL + "/api/atlas/v2/glossary/" + glossary.getGuid();

        this.callDeleteRESTCall(methodName, url);
    }


    /**
     * Return a glossary term based on its guid.
     *
     * @param glossaryTermGUID unique identifier of the glossary term.
     * @return glossary or null
     * @throws PropertyServerException problem with the request
     */
    public AtlasGlossaryTermElement getAtlasGlossaryTerm(String glossaryTermGUID) throws PropertyServerException
    {
        final String methodName = "getAtlasGlossaryTerm(glossaryTermGUID)";
        final String url = targetRootURL + "/api/atlas/v2/glossary/term/" + glossaryTermGUID;

        return this.callGetRESTCallNoParams(methodName, AtlasGlossaryTermElement.class, url);
    }


    /**
     * Create a new term linked to its glossary, other terms and categories.
     *
     * @param term term to create
     * @return glossaryTermGUID
     * @throws PropertyServerException problem with the request
     * @throws NameConflictException the name supplied clashes with another term
     */
    public String createAtlasGlossaryTerm(AtlasGlossaryTermElement term) throws PropertyServerException,
                                                                                NameConflictException
    {
        final String methodName = "createAtlasGlossaryTerm()";
        final String url = targetRootURL + "/api/atlas/v2/glossary/term";

        AtlasGlossaryTermElement newTerm;


        try
        {
            newTerm = this.callPostRESTCallNameConflict(methodName, AtlasGlossaryTermElement.class, url, term);
        }
        catch (PropertyServerException error)
        {
            if (error.getMessage().contains("org.springframework.web.client.HttpClientErrorException$Conflict"))
            {
                throw new NameConflictException(ApacheAtlasErrorCode.TERM_ALREADY_EXISTS.getMessageDefinition(term.getName()),
                                                this.getClass().getName(),
                                                methodName,
                                                "name",
                                                error);
            }

            throw error;
        }

        if (newTerm != null)
        {
            return newTerm.getGuid();
        }

        return null;
    }


    /**
     * Save a term with all of its links to other terms and categories.
     *
     * @param term term to create/update
     * @return term or null
     * @throws PropertyServerException problem with the request
     */
    public AtlasGlossaryTermElement saveAtlasGlossaryTerm(AtlasGlossaryTermElement term) throws PropertyServerException
    {
        final String methodName = "saveAtlasGlossaryTerm()";
        final String url = targetRootURL + "/api/atlas/v2/glossary/term/{0}";

        return this.callPutRESTCall(methodName, AtlasGlossaryTermElement.class, url, term, term.getGuid());
    }


    /**
     * Delete a term with all of its links to other terms and categories.
     *
     * @param term term to delete
     * @throws PropertyServerException problem with the request
     */
    public void deleteAtlasGlossaryTerm(AtlasGlossaryTermElement term) throws PropertyServerException
    {
        final String methodName = "deleteAtlasGlossaryTerm()";
        final String url = targetRootURL + "/api/atlas/v2/glossary/term/" + term.getGuid();

        this.callDeleteRESTCall(methodName, url);
    }


    /**
     * Return a category based on its guid.
     *
     * @param glossaryCategoryGUID unique identifier of the category.
     * @return glossary or null
     * @throws PropertyServerException problem with the request
     */
    public AtlasGlossaryCategoryElement getAtlasGlossaryCategory(String glossaryCategoryGUID) throws PropertyServerException
    {
        final String methodName = "getAtlasGlossaryCategory(glossaryCategoryGUID)";
        final String url = targetRootURL + "/api/atlas/v2/glossary/category/" + glossaryCategoryGUID;

        return this.callGetRESTCallNoParams(methodName, AtlasGlossaryCategoryElement.class, url);
    }


    /**
     * Create a new category linked to its glossary and potentially other categories and terms.
     *
     * @param category category to create
     * @return glossaryCategoryGUID
     * @throws PropertyServerException problem with the request
     * @throws NameConflictException the name supplied clashes with another term
     */
    public String createAtlasGlossaryCategory(AtlasGlossaryCategoryElement category) throws PropertyServerException,
                                                                                            NameConflictException
    {
        final String methodName = "createAtlasGlossaryCategory(category)";
        final String url = targetRootURL + "/api/atlas/v2/glossary/category";

        AtlasGlossaryCategoryElement newGlossaryCategory;


        try
        {
            newGlossaryCategory = this.callPostRESTCallNameConflict(methodName, AtlasGlossaryCategoryElement.class, url, category);
        }
        catch (PropertyServerException error)
        {
            if (error.getMessage().contains("org.springframework.web.client.HttpClientErrorException$Conflict"))
            {
                throw new NameConflictException(ApacheAtlasErrorCode.CATEGORY_ALREADY_EXISTS.getMessageDefinition(category.getName()),
                                                this.getClass().getName(),
                                                methodName,
                                                "name",
                                                error);
            }

            throw error;
        }
        if (newGlossaryCategory != null)
        {
            return newGlossaryCategory.getGuid();
        }

        return null;
    }


    /**
     * Save a category with all of its links to terms and categories.
     *
     * @param category category to create/update
     * @return glossary category or null
     * @throws PropertyServerException problem with the request
     */
    public AtlasGlossaryCategoryElement saveAtlasGlossaryCategory(AtlasGlossaryCategoryElement category) throws PropertyServerException
    {
        final String methodName = "saveAtlasGlossaryCategory(glossaryGUID)";
        final String url = targetRootURL + "/api/atlas/v2/glossary/category/{0}";

        return this.callPutRESTCall(methodName, AtlasGlossaryCategoryElement.class, url, category, category.getGuid());
    }


    /**
     * Delete a category with all of its links to terms and categories.
     *
     * @param category category to create/update
     * @throws PropertyServerException problem with the request
     */
    public void deleteAtlasGlossaryCategory(AtlasGlossaryCategoryElement category) throws PropertyServerException
    {
        final String methodName = "deleteAtlasGlossaryCategory(glossaryGUID)";
        final String url = targetRootURL + "/api/atlas/v2/glossary/category/" + category.getGuid();

        this.callDeleteRESTCall(methodName, url);
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private   <T> T callGetRESTCallNoParams(String    methodName,
                                            Class<T>  returnClass,
                                            String    urlTemplate) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, returnClass, urlTemplate);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, true, error);
        }

        return null;
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private   <T> T callNoLogGetRESTCallNoParams(String    methodName,
                                                 Class<T>  returnClass,
                                                 String    urlTemplate) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, returnClass, urlTemplate);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, false, error);
        }

        return null;
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private    <T> T callGetRESTCall(String    methodName,
                                     Class<T>  returnClass,
                                     String    urlTemplate,
                                     Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, returnClass, urlTemplate, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, true, error);
        }

        return null;
    }

    /**
     * Issue a GET REST call that returns a response object. It's working only with {@link SpringRESTClientConnector}
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param responseType class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private    <T> T callGetRESTCall(String                        methodName,
                                     ParameterizedTypeReference<T> responseType,
                                     String                        urlTemplate,
                                     Object...                     params) throws PropertyServerException
    {
        try
        {
            SpringRESTClientConnector clientConnector = (SpringRESTClientConnector) this.clientConnector;
            return clientConnector.callGetRESTCall(methodName, responseType, urlTemplate, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, true, error);
        }

        return null;
    }


    /**
     * Issue a GET REST call that returns a response object. It's working only with {@link SpringRESTClientConnector}
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param responseType class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private    <T> T callNoLogGetRESTCall(String                        methodName,
                                          ParameterizedTypeReference<T> responseType,
                                          String                        urlTemplate,
                                          Object...                     params) throws PropertyServerException
    {
        try
        {
            SpringRESTClientConnector clientConnector = (SpringRESTClientConnector) this.clientConnector;
            return clientConnector.callGetRESTCall(methodName, responseType, urlTemplate, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, false, error);
        }

        return null;
    }


    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private   <T> T callPostRESTCallNoParams(String    methodName,
                                             Class<T>  returnClass,
                                             String    urlTemplate,
                                             Object    requestBody) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPostRESTCallNoParams(methodName, returnClass, urlTemplate, requestBody);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, true, error);
        }

        return null;
    }


    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private   <T> T callNoLogPostRESTCallNoParams(String    methodName,
                                                   Class<T>  returnClass,
                                                   String    urlTemplate,
                                                   Object    requestBody) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPostRESTCallNoParams(methodName, returnClass, urlTemplate, requestBody);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, false, error);
        }

        return null;
    }


    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private   <T> T callPostRESTCallNameConflict(String    methodName,
                                                 Class<T>  returnClass,
                                                 String    urlTemplate,
                                                 Object    requestBody) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPostRESTCallNoParams(methodName, returnClass, urlTemplate, requestBody);
        }
        catch (Exception error)
        {
            /*
             * Avoid logging error to the audit log if this is just a name conflict
             */
            if (! error.getMessage().contains("org.springframework.web.client.HttpClientErrorException$Conflict"))
            {
                logRESTCallException(methodName, true, error);
            }

            throw new PropertyServerException(ApacheAtlasErrorCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                                   atlasServerName,
                                                                                                                   targetRootURL,
                                                                                                                   error.getMessage()),
                                              this.getClass().getName(),
                                              methodName,
                                              error);
        }
    }


    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private    <T> T callPostRESTCall(String    methodName,
                                      Class<T>  returnClass,
                                      String    urlTemplate,
                                      Object    requestBody,
                                      Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPostRESTCall(methodName, returnClass, urlTemplate, requestBody, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, true, error);
        }

        return null;
    }

    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters. It's working only with {@link SpringRESTClientConnector}
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param responseType class of the response for generic object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private    <T> T callPostRESTCall(String                        methodName,
                                      ParameterizedTypeReference<T> responseType,
                                      String                        urlTemplate,
                                      Object                        requestBody,
                                      Object...                     params) throws PropertyServerException
    {
        try
        {
            SpringRESTClientConnector clientConnector = (SpringRESTClientConnector) this.clientConnector;
            return clientConnector.callPostRESTCall(methodName, responseType, urlTemplate, requestBody, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, true, error);
        }

        return null;
    }

    /**
     * Issue a PUT REST call that returns a response object.  This is typically an update.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private    <T> T callPutRESTCall(String    methodName,
                                     Class<T>  returnClass,
                                     String    urlTemplate,
                                     Object    requestBody,
                                     Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPutRESTCall(methodName, returnClass, urlTemplate, requestBody, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, true, error);
        }

        return null;
    }

    /**
     * Issue a PUT REST call that returns a response object.  This is typically an update.
     * It's working only with {@link SpringRESTClientConnector}
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param responseType class of the response for generic object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private    <T> T callPutRESTCall(String                        methodName,
                                     ParameterizedTypeReference<T> responseType,
                                     String                        urlTemplate,
                                     Object                        requestBody,
                                     Object...                     params) throws PropertyServerException
    {
        try
        {
            SpringRESTClientConnector clientConnector = (SpringRESTClientConnector) this.clientConnector;
            return clientConnector.callPutRESTCall(methodName, responseType, urlTemplate, requestBody, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, true, error);
        }

        return null;
    }


    /**
     * Issue a Delete REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private    <T> T callDeleteRESTCall(String    methodName,
                                        Class<T>  returnClass,
                                        String    urlTemplate,
                                        Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callDeleteRESTCall(methodName, returnClass, urlTemplate, null, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, true, error);
        }

        return null;
    }


    /**
     * Issue a Delete REST call that returns a response object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private    void callDeleteRESTCall(String    methodName,
                                       String    urlTemplate) throws PropertyServerException
    {
        try
        {
            clientConnector.callDeleteRESTCallNoParams(methodName, Object.class, urlTemplate, null);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, true,  error);
        }
    }


    /**
     * Issue a Delete REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param responseType class of the response for generic object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private    <T> T callDeleteRESTCall(String                        methodName,
                                        ParameterizedTypeReference<T> responseType,
                                        String                        urlTemplate, Object... params) throws PropertyServerException
    {
        try
        {
            SpringRESTClientConnector clientConnector = (SpringRESTClientConnector) this.clientConnector;
            return clientConnector.callDeleteRESTCall(methodName, responseType, urlTemplate, null, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, true, error);
        }

        return null;
    }


    /**
     * Provide detailed logging for exceptions.
     *
     * @param methodName calling method
     * @param logMessage should log message
     * @param error resulting exception
     * @throws PropertyServerException wrapping exception
     */
    private void logRESTCallException(String    methodName,
                                      boolean   logMessage,
                                      Exception error) throws PropertyServerException
    {
        if ((auditLog != null) && (logMessage))
        {
            auditLog.logException(methodName,
                                  ApacheAtlasAuditCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                       atlasServerName,
                                                                                                       targetRootURL,
                                                                                                       error.getMessage()),
                                  error);
        }

        throw new PropertyServerException(ApacheAtlasErrorCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                               atlasServerName,
                                                                                                               targetRootURL,
                                                                                                               error.getMessage()),
                                          this.getClass().getName(),
                                          methodName,
                                          error);
    }
}
