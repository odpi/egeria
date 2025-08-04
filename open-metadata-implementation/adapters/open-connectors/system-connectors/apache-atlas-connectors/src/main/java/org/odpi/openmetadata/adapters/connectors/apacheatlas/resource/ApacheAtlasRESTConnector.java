/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource;

import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ffdc.ApacheAtlasAuditCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ffdc.ApacheAtlasErrorCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ffdc.NameConflictException;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.*;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.factory.RESTClientFactory;
import org.odpi.openmetadata.adapters.connectors.restclients.spring.SpringRESTClientConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyValue;
import org.springframework.core.ParameterizedTypeReference;

import java.util.*;


/**
 * ApacheAtlasRESTConnector is responsible for issuing calls to the Apache Atlas REST APIs.  It covers basic calls such as retrieving and adding
 * types, entities, relationships, labels, classifications and business metadata.
 */
public class ApacheAtlasRESTConnector extends ConnectorBase implements AuditLoggingComponent
{
    public static final String OPEN_METADATA_TYPE_PREFIX = "OpenMetadata";
    public static final String SERVICE_TYPE              = "open_metadata_ecosystem";


    private AuditLog                  auditLog            = null;
    private String                    atlasServerName    = "Apache Atlas";
    private String                    targetRootURL      = null;
    private String                    connectorName      = "Apache Atlas REST Connector";
    private RESTClientConnector       clientConnector    = null;

    /*
     * List of types already defined in Apache Atlas.
     */
    private Map<String, Long>         atlasTypes         = new HashMap<>();


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
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";
        
        if ((connectionBean.getUserId() == null) || (connectionBean.getClearPassword() == null))
        {
            throw new ConnectorCheckedException(ApacheAtlasErrorCode.NULL_USER.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }
        
        if (connectionBean.getDisplayName() != null)
        {
            connectorName = connectionBean.getDisplayName();
        }

        /*
         * Retrieve the configuration
         */
        Endpoint endpoint = connectionBean.getEndpoint();

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

        Map<String, Object> configurationProperties = connectionBean.getConfigurationProperties();

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
                                                               connectionBean.getUserId(),
                                                               connectionBean.getClearPassword(),
                                                               secretsStoreConnectorMap,
                                                               auditLog);

            this.clientConnector = factory.getClientConnector();

            /*
             * Check for connectivity to Apache Atlas
             */
            refreshAtlasTypesList();
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


    /*
     *===========================================================================
     * Specialized methods
     */


    /**
     * Return the version of this Apache Atlas.
     *
     * @return atlas version information
     * @throws PropertyServerException problem communicating with Apache Atlas
     */
    public AtlasVersion getAtlasVersion() throws PropertyServerException
    {
        final String methodName = "getAtlasVersion()";
        final String url = targetRootURL + "/api/atlas/admin/version";

        return this.callGetRESTCallNoParams(methodName, AtlasVersion.class, url);
    }


    /**
     * Return the metrics of this Apache Atlas.
     *
     * @return atlas metrics information
     * @throws PropertyServerException problem communicating with Apache Atlas
     */
    public AtlasMetrics getAtlasMetrics() throws PropertyServerException
    {
        final String methodName = "getAtlasMetrics()";
        final String url = targetRootURL + "/api/atlas/admin/metrics";

        return this.callGetRESTCallNoParams(methodName, AtlasMetrics.class, url);
    }


    /**
     * Retain a list of defined types to avoid define the same type multiple times.
     *
     * @throws PropertyServerException unable to connect to Apache Atlas
     */
    private synchronized void refreshAtlasTypesList() throws PropertyServerException
    {
        atlasTypes = new HashMap<>();

        addTypeDefinitions(this.getAllTypes());
    }

    private enum TypeStatus { ADD, UPDATE, IGNORE };


    /**
     * Is the named type already defined in Apache Atlas?  Is the version correct?
     *
     * @param atlasTypeName name of type to check for
     * @param version type version to test for
     * @return result
     */
    public synchronized TypeStatus isTypeDefinedInAtlas(String atlasTypeName,
                                                        long   version)
    {
        if (! atlasTypes.containsKey(atlasTypeName))
        {
            return TypeStatus.ADD;
        }

        if (version > atlasTypes.get(atlasTypeName))
        {
            return TypeStatus.UPDATE;
        }

        return TypeStatus.IGNORE;
    }


    /**
     * Make updates to the atlas types map based on the type changes described in the supplied Apache Atlas type definitions
     *
     * @param atlasTypesDef type definitions returned from Apache Atlas
     */
    public synchronized void addTypeDefinitions(AtlasTypesDef atlasTypesDef)
    {
        if (atlasTypesDef != null)
        {
            if (atlasTypesDef.getEnumDefs() != null)
            {
                for (AtlasEnumDef atlasEnumDef : atlasTypesDef.getEnumDefs())
                {
                    atlasTypes.put(atlasEnumDef.getName(), atlasEnumDef.getVersion());
                }
            }

            if (atlasTypesDef.getStructDefs() != null)
            {
                for (AtlasStructDef atlasStructDef : atlasTypesDef.getStructDefs())
                {
                    atlasTypes.put(atlasStructDef.getName(), atlasStructDef.getVersion());
                }
            }

            if (atlasTypesDef.getClassificationDefs() != null)
            {
                for (AtlasClassificationDef atlasClassificationDef : atlasTypesDef.getClassificationDefs())
                {
                    atlasTypes.put(atlasClassificationDef.getName(), atlasClassificationDef.getVersion());
                }
            }

            if (atlasTypesDef.getEntityDefs() != null)
            {
                for (AtlasEntityDef atlasEntityDef : atlasTypesDef.getEntityDefs())
                {
                    atlasTypes.put(atlasEntityDef.getName(), atlasEntityDef.getVersion());
                }
            }

            if (atlasTypesDef.getRelationshipDefs() != null)
            {
                for (AtlasRelationshipDef atlasRelationshipDef : atlasTypesDef.getRelationshipDefs())
                {
                    atlasTypes.put(atlasRelationshipDef.getName(), atlasRelationshipDef.getVersion());
                }
            }

            if (atlasTypesDef.getBusinessMetadataDefs() != null)
            {
                for (AtlasBusinessMetadataDef atlasBusinessMetadataDef : atlasTypesDef.getBusinessMetadataDefs())
                {
                    atlasTypes.put(atlasBusinessMetadataDef.getName(), atlasBusinessMetadataDef.getVersion());
                }
            }
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
     * Return the list of entity types as a list encoded as a single string with escaped type names.
     * This format is used with business metadata type definitions.
     *
     * @return list of entity type names with escape characters or null
     */
    public String getTypeNamesAsString(Set<String> typeNames)
    {
        if (typeNames != null)
        {
            StringBuilder resultString = new StringBuilder("[");
            boolean       firstElement = true;

            for (String typeName : typeNames)
            {
                if (typeName != null)
                {
                    if (! firstElement)
                    {
                        resultString.append(",");
                    }
                    firstElement = false;

                    resultString.append("\"").append(typeName).append("\"");
                }
            }

            resultString.append("]");

            return resultString.toString();
        }

        return null;
    }


    /**
     * Return the list of entity types that are top level - eg Referenceable.
     *
     * @return set of entity type names
     * @throws PropertyServerException error calling Atlas
     */
    public Set<String> getTopLevelEntityTypes() throws PropertyServerException
    {
        AtlasTypesDef allTypes = this.getAllTypes();

        if ((allTypes != null) && (allTypes.getEntityDefs() != null))
        {
            Set<String> typeNames = new HashSet<>();

            for (AtlasEntityDef entityDef : allTypes.getEntityDefs())
            {
                if ((entityDef != null) && ((entityDef.getSuperTypes() == null) || (entityDef.getSuperTypes().isEmpty())))
                {
                    typeNames.add(entityDef.getName());
                }
            }

            if (! typeNames.isEmpty())
            {
                return typeNames;
            }
        }

        return null;
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
     *
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public void addNewTypes(AtlasTypesDef newTypeDefinitions) throws PropertyServerException
    {
        final String methodName = "addNewTypes()";
        final String url = targetRootURL + "/api/atlas/v2/types/typedefs";

        AtlasTypesDef newTypes = this.callPostRESTCallNoParams(methodName, AtlasTypesDef.class, url, newTypeDefinitions);

        this.addTypeDefinitions(newTypes);
    }


    /**
     * Update existing types.  The registered types are returned from Apache Atlas.
     *
     * @param newTypeDefinitions gallery of new types
     *
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public void updateTypes(AtlasTypesDef newTypeDefinitions) throws PropertyServerException
    {
        final String methodName = "updateTypes()";
        final String url = targetRootURL + "/api/atlas/v2/types/typedefs";

        AtlasTypesDef newTypes = this.callPutRESTCallNoParams(methodName, AtlasTypesDef.class, url, newTypeDefinitions);

        this.addTypeDefinitions(newTypes);
    }


    /**
     * Copy the contents of an open metadata element into Apache Atlas and return the
     * unique identifier of the new Apache Atlas element.  Note: this method assumes the type has been defined.
     *
     * @param openMetadataElement element to copy into Apache Atlas
     * @return unique identifier of the resulting Atlas entity
     * @throws PropertyServerException problem with communicating with Apache Atlas
     */
    public String addOpenMetadataElement(OpenMetadataElement openMetadataElement) throws PropertyServerException
    {
        AtlasEntity         atlasEntity           = new AtlasEntity();

        atlasEntity.setTypeName(OPEN_METADATA_TYPE_PREFIX + openMetadataElement.getType().getTypeName());
        atlasEntity.setIncomplete(false);
        atlasEntity.setStatus(AtlasInstanceStatus.ACTIVE);
        atlasEntity.setVersion(1L);

        if (openMetadataElement.getElementProperties() != null)
        {
            Map<String, Object> attributes = new HashMap<>();

            for (String attributeName : openMetadataElement.getElementProperties().getPropertyValueMap().keySet())
            {
                PropertyValue propertyValue = openMetadataElement.getElementProperties().getPropertyValueMap().get(attributeName);

                attributes.put(attributeName, propertyValue.valueAsObject());
            }

            if (! attributes.isEmpty())
            {
                atlasEntity.setAttributes(attributes);
            }
        }

        return this.addEntity(atlasEntity);
    }


    /**
     * Update the contents of an Apache Atlas entity using the contents of an open metadata element.
     *
     * @param atlasGUID unique identifier of the atlas entity to update
     * @param openMetadataElement entity from the open metadata ecosystem to copy into Apache Atlas
     * @throws PropertyServerException problem with communicating with Apache Atlas
     */
    public void   updateOpenMetadataElement(String              atlasGUID,
                                            OpenMetadataElement openMetadataElement) throws PropertyServerException
    {
        AtlasEntityWithExtInfo atlasEntity = this.getEntityByGUID(atlasGUID);

        if (openMetadataElement.getElementProperties() != null)
        {
            Map<String, Object> attributes = new HashMap<>();

            for (String attributeName : openMetadataElement.getElementProperties().getPropertyValueMap().keySet())
            {
                PropertyValue propertyValue = openMetadataElement.getElementProperties().getPropertyValueMap().get(attributeName);

                attributes.put(attributeName, propertyValue.valueAsObject());
            }

            if (! attributes.isEmpty())
            {
                atlasEntity.getEntity().setAttributes(attributes);
            }
        }

        this.updateEntity(atlasEntity);
    }


    /**
     * Create an open metadata ecosystem relationship between Apache Atlas entities.
     * The associated relationship may or may not exist in Apache Atlas.
     * If the relationship exists, it is updated.  If it does not exist, it is created.
     * Note: this method assumes the type has been defined.
     * Also note that this logic is assuming uni-link relationships.
     * todo - add support for multi-link relationships
     *
     * @param relationshipTypeName relationship from the open metadata ecosystem
     * @param relationshipProperties properties from open metadata ecosystem
     * @param end1AtlasGUID unique identifier of Atlas entity at end 1 of the relationship
     * @param end2AtlasGUID unique identifier of Atlas entity at end 2 of the relationship
     * @throws PropertyServerException problem with communicating with Apache Atlas
     */
    public void setupRelatedMetadataEntities(String            relationshipTypeName,
                                             ElementProperties relationshipProperties,
                                             String            end1AtlasGUID,
                                             String            end2AtlasGUID) throws PropertyServerException
    {
        String atlasRelationshipTypeName = OPEN_METADATA_TYPE_PREFIX + relationshipTypeName;

        AtlasEntityWithExtInfo end1EntityWithExt = this.getEntityByGUID(end1AtlasGUID);
        AtlasEntity atlasEntity = end1EntityWithExt.getEntity();

        AtlasRelationship   atlasRelationship = new AtlasRelationship();

        atlasRelationship.setTypeName(atlasRelationshipTypeName);

        AtlasObjectId end1 = new AtlasObjectId();

        end1.setGuid(end1AtlasGUID);
        atlasRelationship.setEnd1(end1);

        AtlasObjectId end2 = new AtlasObjectId();

        end2.setGuid(end2AtlasGUID);
        atlasRelationship.setEnd2(end2);

        if (relationshipProperties != null)
        {
            Map<String, Object> attributes = new HashMap<>();

            for (String attributeName : relationshipProperties.getPropertyValueMap().keySet())
            {
                PropertyValue propertyValue = relationshipProperties.getPropertyValueMap().get(attributeName);

                attributes.put(attributeName, propertyValue.valueAsObject());
            }

            if (! attributes.isEmpty())
            {
                atlasRelationship.setAttributes(attributes);
            }
        }

        if (atlasEntity.getRelationshipAttributes() != null)
        {
            Map<String, Object> relationships = atlasEntity.getRelationshipAttributes();

            boolean shouldAddRelationship = true;

            for (Object relationshipObject : relationships.values())
            {
                if (relationshipObject instanceof Map<?,?> relationshipDetails)
                {
                    if (atlasRelationshipTypeName.equals(relationshipDetails.get("relationshipType")))
                    {
                        if (end2AtlasGUID.equals(relationshipDetails.get("guid")))
                        {
                            shouldAddRelationship = false;
                            break;
                        }
                    }
                }
            }

            if (shouldAddRelationship)
            {
                this.addRelationship(atlasRelationship);
            }
            else
            {
                this.updateRelationship(atlasRelationship);
            }
        }
        else
        {
            /*
             * No relationships to this entity so relationship must be new
             */
            this.addRelationship(atlasRelationship);
        }
    }


    /**
     * Take an open metadata type definition and add it to Apache Atlas's type system.
     *
     * @param openMetadataTypeDef type definition from open metadata
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public void addOpenMetadataType(OpenMetadataTypeDef openMetadataTypeDef) throws PropertyServerException
    {
        if (openMetadataTypeDef instanceof OpenMetadataEntityDef entityDef)
        {
            addOpenMetadataEntityType(entityDef);
        }
        else if (openMetadataTypeDef instanceof OpenMetadataRelationshipDef relationshipDef)
        {
            addOpenMetadataRelationshipType(relationshipDef);
        }
        else if (openMetadataTypeDef instanceof OpenMetadataClassificationDef classificationDef)
        {
            addOpenMetadataClassificationType(classificationDef);
        }
    }


    /**
     * Convert an open metadata entity type to an Atlas type and add it to Apache Atlas.
     *
     * @param openMetadataEntityDef entity from open metadata
     *
     * @throws PropertyServerException unable to add type to Apache Atlas
     */
    public void addOpenMetadataEntityType(OpenMetadataEntityDef openMetadataEntityDef) throws PropertyServerException
    {
        if (openMetadataEntityDef != null)
        {
            TypeStatus typeStatus = this.isTypeDefinedInAtlas(openMetadataEntityDef.getName(), openMetadataEntityDef.getVersion());

            if (typeStatus != TypeStatus.IGNORE)
            {
                AtlasTypesDef        typesDef   = new AtlasTypesDef();
                List<AtlasEntityDef> entityDefs = new ArrayList<>();
                AtlasEntityDef       entityDef  = new AtlasEntityDef();

                entityDef.setName(OPEN_METADATA_TYPE_PREFIX + openMetadataEntityDef.getName());
                entityDef.setDescription(openMetadataEntityDef.getDescription());
                entityDef.setServiceType(ApacheAtlasRESTConnector.SERVICE_TYPE);
                entityDef.setTypeVersion(openMetadataEntityDef.getVersionName());
                entityDef.setVersion(openMetadataEntityDef.getVersion());

                entityDef.setAttributeDefs(this.getAtlasAttributeDefs(openMetadataEntityDef.getAttributeDefinitions()));

                entityDefs.add(entityDef);
                typesDef.setEntityDefs(entityDefs);

                if (typeStatus == TypeStatus.ADD)
                {
                    this.addNewTypes(typesDef);
                }
                else
                {
                    this.updateTypes(typesDef);
                }
            }
        }
    }


    /**
     * Convert an open metadata relationship type to an Atlas type and add it to Apache Atlas.
     *
     * @param openMetadataRelationshipDef relationship from open metadata
     * @throws PropertyServerException unable to add type to Apache Atlas
     */
    public void addOpenMetadataRelationshipType(OpenMetadataRelationshipDef openMetadataRelationshipDef) throws PropertyServerException
    {
        if (openMetadataRelationshipDef != null)
        {
            TypeStatus typeStatus = this.isTypeDefinedInAtlas(openMetadataRelationshipDef.getName(), openMetadataRelationshipDef.getVersion());

            if (typeStatus != TypeStatus.IGNORE)
            {
                AtlasTypesDef              typesDef         = new AtlasTypesDef();
                List<AtlasRelationshipDef> relationshipDefs = new ArrayList<>();
                AtlasRelationshipDef       relationshipDef  = new AtlasRelationshipDef();
                AtlasRelationshipEndDef    end1             = new AtlasRelationshipEndDef();
                AtlasRelationshipEndDef    end2             = new AtlasRelationshipEndDef();

                relationshipDef.setName(OPEN_METADATA_TYPE_PREFIX + openMetadataRelationshipDef.getName());
                relationshipDef.setDescription(openMetadataRelationshipDef.getDescription());
                relationshipDef.setServiceType(SERVICE_TYPE);
                relationshipDef.setTypeVersion(openMetadataRelationshipDef.getVersionName());
                relationshipDef.setVersion(openMetadataRelationshipDef.getVersion());
                relationshipDef.setRelationshipCategory(AtlasRelationshipCategory.ASSOCIATION);
                relationshipDef.setPropagateTags(AtlasPropagateTags.NONE);

                end1.setType(relationshipDef.getEndDef1().getType());
                end1.setName(relationshipDef.getEndDef2().getName()); // labels are stored the opposite to Egeria.
                end1.setContainer(false);
                end1.setCardinality(this.getAtlasCardinality(openMetadataRelationshipDef.getEndDef1().getAttributeCardinality()));
                end1.setLegacyAttribute(false);

                end2.setType(relationshipDef.getEndDef2().getType());
                end2.setName(relationshipDef.getEndDef1().getName()); // labels are stored the opposite to Egeria.
                end2.setContainer(false);
                end2.setCardinality(this.getAtlasCardinality(openMetadataRelationshipDef.getEndDef1().getAttributeCardinality()));
                end2.setLegacyAttribute(false);

                relationshipDef.setEndDef1(end1);
                relationshipDef.setEndDef2(end2);

                relationshipDef.setAttributeDefs(this.getAtlasAttributeDefs(openMetadataRelationshipDef.getAttributeDefinitions()));

                relationshipDefs.add(relationshipDef);
                typesDef.setRelationshipDefs(relationshipDefs);

                if (typeStatus == TypeStatus.ADD)
                {
                    this.addNewTypes(typesDef);
                }
                else
                {
                    this.updateTypes(typesDef);
                }
            }
        }
    }


    /**
     * Convert the open metadata cardinality for an end of a relationship def from the open metadata enum to the Apache Atlas enum.
     *
     * @param cardinality open metadata enum
     * @return apache Atlas enum
     */
    private AtlasCardinality getAtlasCardinality(OpenMetadataRelationshipEndCardinality cardinality)
    {
        if (cardinality == OpenMetadataRelationshipEndCardinality.AT_MOST_ONE)
        {
            return AtlasCardinality.SINGLE;
        }

        return AtlasCardinality.LIST;
    }


    /**
     * Add a classification definition from open metadata as a type of business metadata in Apache Atlas.
     *
     * @param openMetadataClassificationDef classification type definition
     */
    public void addOpenMetadataClassificationType(OpenMetadataClassificationDef openMetadataClassificationDef) throws PropertyServerException
    {
        if (openMetadataClassificationDef != null)
        {
            TypeStatus typeStatus = this.isTypeDefinedInAtlas(openMetadataClassificationDef.getName(), openMetadataClassificationDef.getVersion());

            if (typeStatus != TypeStatus.IGNORE)
            {
                AtlasTypesDef                  typesDef             = new AtlasTypesDef();
                List<AtlasBusinessMetadataDef> businessMetadataDefs = new ArrayList<>();
                AtlasBusinessMetadataDef       businessMetadataDef  = new AtlasBusinessMetadataDef();

                businessMetadataDef.setName(OPEN_METADATA_TYPE_PREFIX + openMetadataClassificationDef.getName());
                businessMetadataDef.setDescription(openMetadataClassificationDef.getDescription());
                businessMetadataDef.setServiceType(ApacheAtlasRESTConnector.SERVICE_TYPE);
                businessMetadataDef.setTypeVersion(openMetadataClassificationDef.getVersionName());
                businessMetadataDef.setVersion(openMetadataClassificationDef.getVersion());
                businessMetadataDef.setAttributeDefs(this.getAtlasAttributeDefs(openMetadataClassificationDef.getAttributeDefinitions(),
                                                                                openMetadataClassificationDef.getValidEntityDefs()));

                businessMetadataDefs.add(businessMetadataDef);
                typesDef.setBusinessMetadataDefs(businessMetadataDefs);

                if (typeStatus == TypeStatus.ADD)
                {
                    this.addNewTypes(typesDef);
                }
                else
                {
                    this.updateTypes(typesDef);
                }
            }
        }
    }


    /**
     * Add a classification definition to Apache Atlas.  This classification can be added to any top-level atlas entity type.
     *
     * @param classificationName name of the classification
     * @param classificationDescription description of the classification
     * @param classificationProperties name/descriptions of string properties that will be added to the definition
     * @throws PropertyServerException unable to connect to Apache Atlas
     */
    public void addClassificationType(String              classificationName,
                                      String              classificationDescription,
                                      Map<String, String> classificationProperties) throws PropertyServerException
    {
        if (classificationName != null)
        {
            TypeStatus typeStatus = this.isTypeDefinedInAtlas(classificationName, 1);

            if (typeStatus != TypeStatus.IGNORE)
            {
                AtlasTypesDef                typesDef           = new AtlasTypesDef();
                List<AtlasClassificationDef> classificationDefs = new ArrayList<>();
                AtlasClassificationDef       classificationDef  = new AtlasClassificationDef();

                classificationDef.setName(OPEN_METADATA_TYPE_PREFIX + classificationName);
                classificationDef.setDescription(classificationDescription);
                classificationDef.setServiceType(ApacheAtlasRESTConnector.SERVICE_TYPE);
                classificationDef.setTypeVersion("V1.0");
                classificationDef.setVersion(1L);
                classificationDef.setEntityTypes(this.getTopLevelEntityTypes());

                if (classificationProperties != null)
                {
                    List<AtlasAttributeDef> atlasAttributeDefs = new ArrayList<>();

                    for (String propertyName : classificationProperties.keySet())
                    {
                        atlasAttributeDefs.add(this.getStringAttributeDef(propertyName,
                                                                          classificationProperties.get(propertyName)));
                    }

                    if (! atlasAttributeDefs.isEmpty())
                    {
                        classificationDef.setAttributeDefs(atlasAttributeDefs);
                    }
                }
                classificationDefs.add(classificationDef);
                typesDef.setClassificationDefs(classificationDefs);

                if (typeStatus == TypeStatus.ADD)
                {
                    this.addNewTypes(typesDef);
                }
                else
                {
                    this.updateTypes(typesDef);
                }
            }
        }
    }


    /**
     * Add an enum definition from open metadata as an enum definition in Apache Atlas.
     *
     * @param openMetadataEnumDef enum type definition
     */
    public void addOpenMetadataEnumType(OpenMetadataEnumDef openMetadataEnumDef) throws PropertyServerException
    {
        if (openMetadataEnumDef != null)
        {
            TypeStatus typeStatus = this.isTypeDefinedInAtlas(openMetadataEnumDef.getName(), openMetadataEnumDef.getVersion());

            if (typeStatus != TypeStatus.IGNORE)
            {
                AtlasTypesDef                  typesDef             = new AtlasTypesDef();
                List<AtlasEnumDef>             enumDefs = new ArrayList<>();
                AtlasEnumDef                   enumDef  = new AtlasEnumDef();

                enumDef.setName(OPEN_METADATA_TYPE_PREFIX + openMetadataEnumDef.getName());
                enumDef.setDescription(openMetadataEnumDef.getDescription());
                enumDef.setServiceType(ApacheAtlasRESTConnector.SERVICE_TYPE);
                enumDef.setTypeVersion(openMetadataEnumDef.getVersionName());
                enumDef.setVersion(openMetadataEnumDef.getVersion());
                enumDef.setElementDefs(this.getElementDefs(openMetadataEnumDef.getElementDefs()));
                enumDef.setDefaultValue(openMetadataEnumDef.getDefaultValue().getValue());

                enumDefs.add(enumDef);
                typesDef.setEnumDefs(enumDefs);

                if (typeStatus == TypeStatus.ADD)
                {
                    this.addNewTypes(typesDef);
                }
                else
                {
                    this.updateTypes(typesDef);
                }
            }
        }
    }


    /**
     * Return the list of valid enum values for Apache Atlas base on the valid values defined in open metadata.
     *
     * @param openMetadataEnumElementDefs list of valid values from open metadata
     * @return list of valid values for Apache Atlas
     */
    private List<AtlasElementDef> getElementDefs(List<OpenMetadataEnumElementDef> openMetadataEnumElementDefs)
    {
        if (openMetadataEnumElementDefs != null)
        {
            List<AtlasElementDef> elementDefs = new ArrayList<>();

            for (OpenMetadataEnumElementDef openMetadataEnumElementDef : openMetadataEnumElementDefs)
            {
                if (openMetadataEnumElementDef != null)
                {
                    AtlasElementDef elementDef = new AtlasElementDef();

                    elementDef.setOrdinal(openMetadataEnumElementDef.getOrdinal());
                    elementDef.setDescription(openMetadataEnumElementDef.getDescription());
                    elementDef.setValue(openMetadataEnumElementDef.getValue());

                    elementDefs.add(elementDef);
                }
            }

            if (! elementDefs.isEmpty())
            {
                return elementDefs;
            }
        }

        return null;
    }


    /**
     * A classification def defines the list of entity types that the classification can be attached to.
     * This method converts the open metadata list of entity types into the Atlas list of entity types.
     *
     * @param openMetadataTypeDefLinks list of valid entity types from open metadata
     * @return set of entity type names for an Atlas classification def
     */
    private Set<String> getValidEntityDefs(List<OpenMetadataTypeDefLink> openMetadataTypeDefLinks)
    {
        if (openMetadataTypeDefLinks != null)
        {
            Set<String> validEntityTypeNames = new HashSet<>();

            for (OpenMetadataTypeDefLink openMetadataTypeDefLink : openMetadataTypeDefLinks)
            {
                if ((openMetadataTypeDefLink != null) && (openMetadataTypeDefLink.getName() != null))
                {
                    validEntityTypeNames.add(openMetadataTypeDefLink.getName());
                }
            }

            if (! validEntityTypeNames.isEmpty())
            {
                return validEntityTypeNames;
            }
        }

        return null;
    }


    /**
     * Convert the list of attributes from the open metadata types to Atlas Attributes.
     * This is used for entity, classifications and relationship definitions.
     *
     * @param openMetadataTypeDefAttributes open metadata attributes
     * @return list of atlas attributes
     * @throws PropertyServerException problem calling Apache Atlas
     */
    private List<AtlasAttributeDef> getAtlasAttributeDefs(List<OpenMetadataTypeDefAttribute> openMetadataTypeDefAttributes) throws PropertyServerException
    {
        if (openMetadataTypeDefAttributes != null)
        {
            List<AtlasAttributeDef> atlasAttributeDefs = new ArrayList<>();

            for (OpenMetadataTypeDefAttribute attribute : openMetadataTypeDefAttributes)
            {
                if (attribute != null)
                {
                    AtlasAttributeDef atlasAttributeDef = this.getAtlasAttributeDef(attribute);

                    if (atlasAttributeDef != null)
                    {
                        atlasAttributeDefs.add(atlasAttributeDef);
                    }
                }
            }

            if (! atlasAttributeDefs.isEmpty())
            {
                return atlasAttributeDefs;
            }
        }

        return null;
    }


    /**
     * Convert the list of attributes from the open metadata types to Atlas Attributes for business metadata.
     *
     * @param openMetadataTypeDefAttributes open metadata attributes
     * @return list of atlas attributes
     * @throws PropertyServerException problem calling Apache Atlas
     */
    private List<AtlasAttributeDef> getAtlasAttributeDefs(List<OpenMetadataTypeDefAttribute> openMetadataTypeDefAttributes,
                                                          List<OpenMetadataTypeDefLink>      validEntityTypes) throws PropertyServerException
    {
        if (openMetadataTypeDefAttributes != null)
        {
            List<AtlasAttributeDef> atlasAttributeDefs = new ArrayList<>();

            for (OpenMetadataTypeDefAttribute attribute : openMetadataTypeDefAttributes)
            {
                if (attribute != null)
                {
                    AtlasAttributeDef atlasAttributeDef = this.getAtlasAttributeDef(attribute,
                                                                                    100,
                                                                                    this.getTypeNamesAsString(this.getValidEntityDefs(validEntityTypes)));

                    if (atlasAttributeDef != null)
                    {
                        atlasAttributeDefs.add(atlasAttributeDef);
                    }
                }
            }

            if (! atlasAttributeDefs.isEmpty())
            {
                return atlasAttributeDefs;
            }
        }

        return null;
    }


    /**
     * Convert an open metadata attribute to an Apache Atlas attribute. This is used for entity, classifications and relationship definitions.
     *
     * @param openMetadataTypeDefAttribute open metadata attribute
     * @return atlas attribute
     * @throws PropertyServerException problem calling Apache Atlas
     */
    private AtlasAttributeDef getAtlasAttributeDef(OpenMetadataTypeDefAttribute openMetadataTypeDefAttribute) throws PropertyServerException
    {
        if (openMetadataTypeDefAttribute != null)
        {
            return this.getStandardAttributeDef(openMetadataTypeDefAttribute.getAttributeName(),
                                                openMetadataTypeDefAttribute.getAttributeDescription(),
                                                openMetadataTypeDefAttribute.getAttributeType());
        }

        return null;
    }


    /**
     * Return a single attribute definition for a business metadata type.
     *
     * @param openMetadataTypeDefAttribute details of the attribute
     * @param maxStringLength maximum length of string
     * @param applicableEntityTypes list of type names that this attribute can be attached to
     * @return atlas attribute definition
     * @throws PropertyServerException problem calling Apache Atlas
     */
    public AtlasAttributeDef getAtlasAttributeDef(OpenMetadataTypeDefAttribute openMetadataTypeDefAttribute,
                                                  int                          maxStringLength,
                                                  String                       applicableEntityTypes) throws PropertyServerException
    {
        if (openMetadataTypeDefAttribute != null)
        {
            AtlasAttributeDef attributeDef = getStandardAttributeDef(openMetadataTypeDefAttribute.getAttributeName(),
                                                                     openMetadataTypeDefAttribute.getAttributeDescription(),
                                                                     openMetadataTypeDefAttribute.getAttributeType());

            Map<String, String> options = new HashMap<>();

            if ("string".equals(openMetadataTypeDefAttribute.getAttributeType().getName()))
            {
                options.put(AtlasBusinessMetadataDef.ATTR_MAX_STRING_LENGTH, Integer.toString(maxStringLength));
            }

            options.put(AtlasBusinessMetadataDef.ATTR_OPTION_APPLICABLE_ENTITY_TYPES, applicableEntityTypes);

            attributeDef.setOptions(options);

            return attributeDef;
        }

        return null;
    }


    /**
     * Create an attribute of the requested type.  Note enums are represented as strings in Apache Atlas.
     *
     * @param propertyName name of the attribute
     * @param propertyDescription description of the attribute
     * @param openMetadataAttributeTypeDef type of the attribute
     * @return attribute definition
     */
    public AtlasAttributeDef getStandardAttributeDef(String                       propertyName,
                                                     String                       propertyDescription,
                                                     OpenMetadataAttributeTypeDef openMetadataAttributeTypeDef)
    {
        if (openMetadataAttributeTypeDef instanceof OpenMetadataEnumDef)
        {
            return this.getStandardAttributeDef(propertyName,
                                                propertyDescription,
                                                "string");
        }

        return this.getStandardAttributeDef(propertyName,
                                            propertyDescription,
                                            openMetadataAttributeTypeDef.getName());
    }


    /**
     * Create an Apache Atlas attribute of the requested type.
     *
     * @param propertyName name of the attribute
     * @param propertyDescription description of the attribute
     * @param typeName name of the Apache Atlas type to use
     * @return attribute definition
     */
    public AtlasAttributeDef getStandardAttributeDef(String  propertyName,
                                                     String  propertyDescription,
                                                     String  typeName)
    {
        AtlasAttributeDef  attributeDef  = new AtlasAttributeDef();

        attributeDef.setName(propertyName);
        attributeDef.setDescription(propertyDescription);
        attributeDef.setTypeName(typeName);
        attributeDef.setOptional(false);
        attributeDef.setCardinality(AtlasCardinality.SINGLE);
        attributeDef.setValuesMinCount(0);
        attributeDef.setValuesMaxCount(1);
        attributeDef.setUnique(false);
        attributeDef.setIndexable(true);
        attributeDef.setIncludeInNotification(false);
        attributeDef.setSearchWeight(10);

        return attributeDef;
    }


    /**
     * Create an attribute of type string
     *
     * @param propertyName name of the attribute
     * @param propertyDescription description of the attribute
     * @return attribute definition
     */
    public AtlasAttributeDef getStringAttributeDef(String propertyName,
                                                   String propertyDescription)
    {
        return getStandardAttributeDef(propertyName, propertyDescription, "string");
    }


    /**
     * Create an attribute of type string for a business metadata element.
     *
     * @param propertyName name of the attribute
     * @param maxStringLength maximum string length
     * @param applicableEntityTypes list of entity types that this attribute can be attached to
     * @param propertyDescription description of the attribute
     * @return attribute definition
     */
    public AtlasAttributeDef getStringAttributeDef(String propertyName,
                                                   String propertyDescription,
                                                   int    maxStringLength,
                                                   String applicableEntityTypes)
    {
        AtlasAttributeDef  attributeDef  = getStandardAttributeDef(propertyName, propertyDescription, "string");

        Map<String, String> options = new HashMap<>();

        options.put(AtlasBusinessMetadataDef.ATTR_MAX_STRING_LENGTH, Integer.toString(maxStringLength));
        options.put(AtlasBusinessMetadataDef.ATTR_OPTION_APPLICABLE_ENTITY_TYPES, applicableEntityTypes);

        attributeDef.setOptions(options);

        return attributeDef;
    }


    /**
     * Create an attribute of type boolean
     *
     * @param propertyName name of the attribute
     * @param propertyDescription description of the attribute
     * @return attribute definition
     */
    public AtlasAttributeDef getBooleanAttributeDef(String propertyName,
                                                    String propertyDescription)
    {
        return getStandardAttributeDef(propertyName, propertyDescription, "boolean");
    }


    /**
     * Create an attribute of type boolean for a business metadata element.
     *
     * @param propertyName name of the attribute
     * @param propertyDescription description of the attribute
     * @param applicableEntityTypes list of entity types that this attribute can be attached to
     * @return attribute definition
     */
    public AtlasAttributeDef getBooleanAttributeDef(String propertyName,
                                                    String propertyDescription,
                                                    String applicableEntityTypes)
    {
        AtlasAttributeDef  attributeDef  = getStandardAttributeDef(propertyName, propertyDescription, "boolean");;

        Map<String, String> options = new HashMap<>();

        options.put(AtlasBusinessMetadataDef.ATTR_OPTION_APPLICABLE_ENTITY_TYPES, applicableEntityTypes);

        attributeDef.setOptions(options);

        return attributeDef;
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
     * Add labels to an entity. This call overrides existing labels.
     *
     * @param entityGUID unique identifier of entity and any associated entities
     * @param labels list of labels to assign to the entity
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public void addLabelsToEntity(String       entityGUID,
                                  List<String> labels) throws PropertyServerException
    {
        final String methodName = "addLabelsToEntity()";
        final String url = targetRootURL + "/api/atlas/v2/entity/guid/{0}/labels";

        this.callPostRESTCall(methodName, Object.class, url, labels, entityGUID);
    }



    /**
     * Add business metadata to an entity. This call overrides specified attributes, leaving the rest intact.
     *
     * @param entityGUID unique identifier of entity and any associated entities
     * @param businessMetadataType name of business metadata type that the attributes are added to
     * @param attributes map of attribute names to attribute values to assign to the entity
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public void addBusinessMetadataToEntity(String              entityGUID,
                                            String              businessMetadataType,
                                            Map<String, Object> attributes) throws PropertyServerException
    {
        final String methodName = "addBusinessMetadataToEntity()";
        final String url = targetRootURL + "/api/atlas/v2/entity/guid/{0}/businessmetadata";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(businessMetadataType, attributes);

        this.callPostRESTCall(methodName, Object.class, url, requestBody, entityGUID);
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
     * Retrieve a single relationship by GUID.
     *
     * @param guid unique identifier
     * @return description of requested entity
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public AtlasRelationship getRelationshipByGUID(String guid) throws PropertyServerException
    {
        final String methodName = "getRelationshipByGUID(" + guid + ")";
        final String url = targetRootURL + "/api/atlas/v2/relationship/guid/" + guid;

        AtlasRelationship relationship = this.callGetRESTCallNoParams(methodName, AtlasRelationship.class, url);

        return validateActiveRelationship(relationship);
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
     * Add a relationship.
     *
     * @param atlasRelationship description of relationship
     * @return description of the operation
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public AtlasRelationship updateRelationship(AtlasRelationship  atlasRelationship) throws PropertyServerException
    {
        final String methodName = "updateRelationship()";
        final String url = targetRootURL + "/api/atlas/v2/relationship";

        return this.callPutRESTCallNoParams(methodName, AtlasRelationship.class, url, atlasRelationship);
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
     * Check that the relationship is ACTIVE - otherwise return null.
     *
     * @param retrievedRelationship relationship retrieved from the Apache Atlas repository
     * @return retrieved entity or null
     */
    private AtlasRelationship validateActiveRelationship(AtlasRelationship retrievedRelationship)
    {
        if ((retrievedRelationship != null) && (retrievedRelationship.getStatus() == AtlasInstanceStatus.ACTIVE))
        {
            return retrievedRelationship;
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
        final String methodName = "getEntityByGUID(" + guid + ")";
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
     * Retrieve the entity at the other end of the named relationship.
     *
     * @param startingEntity entity information detailing the entity proxy for the entity at the far end of a named relationship.
     * @param relationshipLabel name of relationship to traverse
     *
     * @return description of requested entity
     * @throws PropertyServerException problem connecting to Apache Atlas
     */
    public AtlasEntityWithExtInfo getRelatedEntity(AtlasEntityHeader startingEntity,
                                                   String            relationshipLabel) throws  PropertyServerException
    {
        AtlasEntityWithExtInfo atlasEntity = this.getEntityByGUID(startingEntity.getGuid());

        if (atlasEntity != null)
        {
            return this.getRelatedEntity(atlasEntity, relationshipLabel);
        }

        return null;
    }


    /**
     * Retrieve the entities at the other end of the named relationship and build a map of the relationships.
     *
     * @param startingEntity entity information detailing the entity proxy for the entity at the far end of a named relationship.
     * @param relationshipLabel name of relationship to traverse
     *
     * @return Map of related entity GUIDs mapped to their relationship GUID
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
    private   <T> T callPutRESTCallNoParams(String    methodName,
                                            Class<T>  returnClass,
                                            String    urlTemplate,
                                            Object    requestBody) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPutRESTCall(methodName, returnClass, urlTemplate, requestBody);
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
