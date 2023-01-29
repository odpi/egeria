/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.*;


public class GraphOMRSEntityMapper {

    private static final Logger log = LoggerFactory.getLogger(GraphOMRSEntityMapper.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectReader OBJECT_READER = OBJECT_MAPPER.reader();
    private static final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writer();

    private final String                          repositoryName;
    private final OMRSRepositoryHelper            repositoryHelper;
    private final GraphOMRSClassificationMapper   classificationMapper;

    GraphOMRSEntityMapper(String               metadataCollectionId,
                          String               repositoryName,
                          OMRSRepositoryHelper repositoryHelper) {

        this.repositoryName         = repositoryName;
        this.repositoryHelper       = repositoryHelper;

        this.classificationMapper   = new GraphOMRSClassificationMapper(metadataCollectionId, repositoryName, repositoryHelper);
    }



    private Object getVertexProperty(Vertex vertex, String propName)
    {
        VertexProperty vp = vertex.property(propName);
        if (vp == null || !vp.isPresent())
            return null;
        else
            return vp.value();
    }

    /*
     * method to add/set property on vertex.
     * qualifiedPropName is the non-prefixed name - qualified by typename if a TDA; or simple core property name
     */
    private void addProperty(Vertex vertex, String propertyName, String qualifiedPropName, InstancePropertyValue ipv) {
        InstancePropertyCategory ipvCat = ipv.getInstancePropertyCategory();
        if (ipvCat == InstancePropertyCategory.PRIMITIVE) {
            // Primitives are stored directly in the graph
            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
            Object primValue = ppv.getPrimitiveValue();
            if (primValue != null) {
                vertex.property(getPropertyKeyEntity(qualifiedPropName), primValue);
            } else {
                removeProperty(vertex, qualifiedPropName);
            }
        } else {
            log.debug("non-primitive instance property {}", propertyName);
        }
    }

    /*
     * method to remove property from vertex.
     * qualifiedPropName is the non-prefixed name - qualified by typename if a TDA; or simple core property name
     */
    private void removeProperty(Vertex vertex, String qualifiedPropName) {
        // no value has been specified - remove the property from the vertex
        VertexProperty vp = vertex.property(getPropertyKeyEntity(qualifiedPropName));
        if (vp != null) {
            vp.remove();
        }
    }

    /*
     * method to remove property from vertex. Easier to use for core properties where a prefixed name is available.
     * prefixedPropName is the prefixed name - i.e. it includes the 've', 'er' or 'vc' prefix.
     */
    private void removeCoreProperty(Vertex vertex, String prefixedPropName) {
        // no value has been specified - remove the property from the vertex
        VertexProperty vp = vertex.property(prefixedPropName);
        if (vp != null) {
            vp.remove();
        }
    }


    // Inbound methods - i.e. writing to store

    void mapEntityDetailToVertex(EntityDetail entity, Vertex vertex)
            throws RepositoryErrorException
    {
        final String methodName = "mapEntityDetailToVertex";

        mapEntitySummaryToVertex(entity, vertex);

        vertex.property(PROPERTY_KEY_ENTITY_IS_PROXY, false);

        InstanceProperties instanceProperties = entity.getProperties();
        if (instanceProperties != null) {

            // First write properties as json - useful for handling collections and possibly for full text/string matching???
            String jsonString;
            try {
                jsonString = OBJECT_WRITER.writeValueAsString(instanceProperties);
                log.debug("{} entity has serialized properties {}", methodName, jsonString);
                vertex.property("instanceProperties", jsonString);
            } catch (Exception exc) {
                log.error("{} Caught exception from entity mapper", methodName);
                throw new RepositoryErrorException(GraphOMRSErrorCode.ENTITY_PROPERTIES_ERROR.getMessageDefinition(entity.getGUID(), methodName,
                                                                                                                   this.getClass().getName(),
                                                                                                                   repositoryName),
                        this.getClass().getName(),
                        methodName, exc);
            }

            // Secondly add primitive properties to support searches

            String typeName = entity.getType().getTypeDefName();
            TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeName);

            // For the type of the entity, walk its type hierarchy and construct a map of short prop name -> qualified prop name.
            GraphOMRSMapperUtils mapperUtils = new GraphOMRSMapperUtils();
            Map<String,String> qualifiedPropertyNames = mapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);

            // This is a full property update - any defined properties that are not in the instanceProperties are cleared.

            List<TypeDefAttribute> propertiesDef = repositoryHelper.getAllPropertiesForTypeDef(repositoryName, typeDef, methodName);

            for (TypeDefAttribute propertyDef : propertiesDef) {
                String propertyName = propertyDef.getAttributeName();
                String qualifiedPropName = qualifiedPropertyNames.get(propertyName);
                // Get the specified value for this property from instanceProperties - uses non-qualified name
                InstancePropertyValue ipv = instanceProperties.getPropertyValue(propertyName);
                if (ipv != null) {
                    // a value has been specified

                    // This uses the qualified property name - so that graph index property keys are finer grained which speeds up index enablement and
                    // will also make lookups faster.
                    addProperty(vertex, propertyName, qualifiedPropName, ipv);
                }
                else {
                    removeProperty(vertex, qualifiedPropName);
                }
            }
        }
    }




    void mapEntityProxyToVertex(EntityProxy entity, Vertex vertex)
            throws RepositoryErrorException
    {
        final String methodName = "mapEntityProxyToVertex";

        mapEntitySummaryToVertex(entity, vertex);

        vertex.property(PROPERTY_KEY_ENTITY_IS_PROXY, true);

        InstanceProperties uniqueProperties = entity.getUniqueProperties();
        if (uniqueProperties != null) {
            // First approach was to write properties as json - could be useful for text/string matching???
            String jsonString;
            try {
                jsonString = OBJECT_WRITER.writeValueAsString(uniqueProperties);
                log.debug("{} entity proxy has serialized unique properties {}", methodName, jsonString);
                vertex.property("instanceProperties", jsonString);
            } catch (Exception exc) {
                log.error("{} caught exception {}", methodName, exc.getMessage());
                throw new RepositoryErrorException(GraphOMRSErrorCode.ENTITY_PROPERTIES_ERROR.getMessageDefinition(entity.getGUID(), methodName,
                                                                                                                   this.getClass().getName(),
                                                                                                                   repositoryName),
                        this.getClass().getName(),
                        methodName, exc);
            }

            // Secondly write all primitive properties as-is to support searches

            String typeName = entity.getType().getTypeDefName();
            TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeName);

            // For the type of the entity, walk its type hierarchy and construct a map of short prop name -> qualified prop name.
            GraphOMRSMapperUtils mapperUtils = new GraphOMRSMapperUtils();
            Map<String,String> qualifiedPropertyNames = mapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);

            // This is a full property update - any defined properties that are not in the instanceProperties are cleared.
            List<TypeDefAttribute> propertiesDef = repositoryHelper.getAllPropertiesForTypeDef(repositoryName, typeDef, methodName);
            for (TypeDefAttribute propertyDef : propertiesDef) {
                String propertyName = propertyDef.getAttributeName();
                String qualifiedPropName = qualifiedPropertyNames.get(propertyName);
                // Get the specified value for this property from instanceProperties - uses non-qualified name
                InstancePropertyValue ipv = uniqueProperties.getPropertyValue(propertyName);
                if (ipv != null) {
                    // a value has been specified

                    // This uses the qualified property name - so that graph index property keys are finer grained which speeds up index enablement and
                    // will also make lookups faster.
                    addProperty(vertex, propertyName, qualifiedPropName, ipv);

                } else {
                    // no value has been specified - remove the property from the vertex
                    removeProperty(vertex, qualifiedPropName);

                }
            }
        }
    }



    // mapEntitySummaryToVertex
    // Classifications are saved in the graph as separate vertices that are linked to the entity using Classifier edges.
    // There are no classification details stored in the EntitySummary (or its corresponding vertex).

    private void mapEntitySummaryToVertex(EntitySummary entity, Vertex vertex)
            throws RepositoryErrorException
    {
        String methodName = "mapEntitySummaryToVertex";


        // Some properties are mandatory. If any of these are null then throw exception
        boolean missingAttribute = false;
        String missingAttributeName = null;

        if (entity.getGUID() != null)
            vertex.property(PROPERTY_KEY_ENTITY_GUID, entity.getGUID());
        else {
            log.debug("{} missing attribute: guid", methodName);
            missingAttribute = true;
            missingAttributeName = "guid";
        }

        InstanceType instanceType = entity.getType();
        if (instanceType != null && instanceType.getTypeDefName() != null)                               // ** name mapping
            vertex.property(PROPERTY_KEY_ENTITY_TYPE_NAME, instanceType.getTypeDefName());
        else {
            log.debug("{} missing attribute: type name", methodName);
            missingAttribute = true;
            missingAttributeName = "type or typeDefName";
        }

        if (entity.getMetadataCollectionId() != null)
            vertex.property(PROPERTY_KEY_ENTITY_METADATACOLLECTION_ID, entity.getMetadataCollectionId());
        else {
            log.debug("{} missing attribute: metadataCollectionId", methodName);
            missingAttribute = true;
            missingAttributeName = "metadataCollectionId";
        }


        if (missingAttribute) {
            log.error("{} entity is missing core attribute {}", methodName, missingAttributeName);
            throw new RepositoryErrorException(GraphOMRSErrorCode.ENTITY_PROPERTIES_ERROR.getMessageDefinition(entity.getGUID(), methodName,
                    this.getClass().getName(),
                    repositoryName),
                    this.getClass().getName(),
                    methodName);
        }

        // Version always accepted as-is
        vertex.property(PROPERTY_KEY_ENTITY_VERSION, entity.getVersion());

        // Other properties can be removed if set to null

        if (entity.getReIdentifiedFromGUID() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_REIDENTIFIED_FROM_GUID, entity.getReIdentifiedFromGUID());
        }
        else {
            removeCoreProperty(vertex, PROPERTY_KEY_ENTITY_REIDENTIFIED_FROM_GUID);
        }

        if (entity.getMetadataCollectionName() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_METADATACOLLECTION_NAME, entity.getMetadataCollectionName());
        }
        else {
            VertexProperty vp = vertex.property(PROPERTY_KEY_ENTITY_METADATACOLLECTION_NAME);
            if (vp != null)
                vp.remove();
        }

        if (entity.getCreatedBy() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_CREATED_BY, entity.getCreatedBy());
        }
        else {
            removeCoreProperty(vertex, PROPERTY_KEY_ENTITY_CREATED_BY);
        }

        if (entity.getCreateTime() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_CREATE_TIME, entity.getCreateTime());
        }
        else {
            removeCoreProperty(vertex, PROPERTY_KEY_ENTITY_CREATE_TIME);
        }

        if (entity.getUpdatedBy() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_UPDATED_BY, entity.getUpdatedBy());
        }
        else {
            removeCoreProperty(vertex, PROPERTY_KEY_ENTITY_UPDATED_BY);
        }

        if (entity.getUpdateTime() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_UPDATE_TIME, entity.getUpdateTime());
        }
        else {
            removeCoreProperty(vertex, PROPERTY_KEY_ENTITY_UPDATE_TIME);
        }

        if (entity.getInstanceProvenanceType() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_INSTANCE_PROVENANCE_TYPE, entity.getInstanceProvenanceType().getOrdinal());     // ** ordinal mapping
        }
        else {
            removeCoreProperty(vertex, PROPERTY_KEY_ENTITY_INSTANCE_PROVENANCE_TYPE);
        }

        if (entity.getStatus() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_CURRENT_STATUS, entity.getStatus().getOrdinal());                              // ** ordinal mapping
        }
        else {
            removeCoreProperty(vertex, PROPERTY_KEY_ENTITY_CURRENT_STATUS);
        }

        if (entity.getStatusOnDelete() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_STATUS_ON_DELETE, entity.getStatusOnDelete().getOrdinal());            // ** ordinal mapping
        }
        else {
            removeCoreProperty(vertex, PROPERTY_KEY_ENTITY_STATUS_ON_DELETE);
        }

        if (entity.getInstanceURL() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_INSTANCE_URL, entity.getInstanceURL());
        }
        else {
            removeCoreProperty(vertex, PROPERTY_KEY_ENTITY_INSTANCE_URL);
        }

        if (entity.getInstanceLicense() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_INSTANCE_LICENSE, entity.getInstanceLicense());
        }
        else {
            removeCoreProperty(vertex, PROPERTY_KEY_ENTITY_INSTANCE_LICENSE);
        }

        // maintainedBy is a List<String>. This could be implemented using multi-properties for Entity and Classification instances,
        // but not for Relationship instances. So it is instead implemented as a String (that serializes the list of strings) so it
        // can be indexed even on Relationships. Queries can use textRegex to search/retrieve.
        if (entity.getMaintainedBy() != null) {
            List<String> maintainers = entity.getMaintainedBy();
            String jsonString;
            try {
                jsonString = OBJECT_WRITER.writeValueAsString(maintainers);
                log.debug("{} entity maintainedBy serialized to {}", methodName, jsonString);
                vertex.property(PROPERTY_KEY_ENTITY_MAINTAINED_BY, jsonString);
            }
            catch (Exception exc) {
                log.error("{} caught exception {}", methodName, exc.getMessage());
                throw new RepositoryErrorException(GraphOMRSErrorCode.ENTITY_PROPERTIES_ERROR.getMessageDefinition(entity.getGUID(), methodName,
                                                                                                                   this.getClass().getName(),
                                                                                                                   repositoryName),
                        this.getClass().getName(),
                        methodName,
                        exc);
            }
        }
        else {
            removeCoreProperty(vertex, PROPERTY_KEY_ENTITY_MAINTAINED_BY);
        }

        if (entity.getReplicatedBy() != null) {
            vertex.property(PROPERTY_KEY_ENTITY_REPLICATED_BY, entity.getReplicatedBy());
        }
        else {
            removeCoreProperty(vertex, PROPERTY_KEY_ENTITY_REPLICATED_BY);
        }


        // mappingProperties is a Map<String, Serializable>. This is implemented as a String (that serializes the map) so it
        // can be indexed (even on Relationships). Queries could use textRegex to search/retrieve, although it is not
        // anticipated that it will be used for search, more for correlation.
        if (entity.getMappingProperties() != null) {
            Map<String, Serializable> mappingProperties = entity.getMappingProperties();
            String jsonString;
            try {
                jsonString = OBJECT_WRITER.writeValueAsString(mappingProperties);
                log.debug("{} entity maintainedBy serialized to {}", methodName, jsonString);
                vertex.property(PROPERTY_KEY_ENTITY_MAPPING_PROPERTIES, jsonString);
            }
            catch (Exception exc) {
                log.error("{} caught exception {}", methodName, exc.getMessage());
                throw new RepositoryErrorException(GraphOMRSErrorCode.ENTITY_PROPERTIES_ERROR.getMessageDefinition(entity.getGUID(), methodName,
                                                                                                                   this.getClass().getName(),
                                                                                                                   repositoryName),
                        this.getClass().getName(),
                        methodName, exc);
            }
        }
        else {
            removeCoreProperty(vertex, PROPERTY_KEY_ENTITY_MAPPING_PROPERTIES);
        }


    }



    // Outbound methods - i.e. reading from store



    void mapVertexToEntityDetail(Vertex vertex, EntityDetail entity)
            throws
            RepositoryErrorException,
            EntityProxyOnlyException

    {
        String methodName = "mapVertexToEntityDetail";

        Boolean isProxy = ((Boolean) getVertexProperty( vertex, PROPERTY_KEY_ENTITY_IS_PROXY));
        if (isProxy) {

            log.error("{} an EntityProxy cannot be retrieved as an EntityDetail {}", methodName, entity.getGUID());

            throw new EntityProxyOnlyException(GraphOMRSErrorCode.ENTITY_PROXY_ONLY.getMessageDefinition(entity.getGUID(), methodName,
                                                                                                         this.getClass().getName(),
                                                                                                         repositoryName),
                    this.getClass().getName(),
                    methodName);
        }


        mapVertexToEntitySummary(vertex, entity);

        // properties
        String stringProps = (String) getVertexProperty(vertex, "instanceProperties");

        if (stringProps != null) {
            try {
                InstanceProperties instanceProperties = OBJECT_READER.readValue(stringProps, InstanceProperties.class);
                log.debug("{} entity has deserialized properties {}", methodName, instanceProperties);
                entity.setProperties(instanceProperties);
            } catch (Exception exc) {
                log.error("{} caught exception {}", methodName, exc.getMessage());
                throw new RepositoryErrorException(GraphOMRSErrorCode.ENTITY_PROPERTIES_ERROR.getMessageDefinition(entity.getGUID(), methodName,
                                                                                                                   this.getClass().getName(),
                                                                                                                   repositoryName),
                        this.getClass().getName(),
                        methodName, exc);
            }
        }

    }


    // This method is not concerned with how the proxy flag is set - it will render the vertex as a proxy whether it was created as such or as an EntityDetail
    void mapVertexToEntityProxy(Vertex vertex, EntityProxy entity)
            throws RepositoryErrorException
    {
        String methodName = "mapVertexToEntityProxy";

        mapVertexToEntitySummary(vertex, entity);

        InstanceType type = entity.getType();
        TypeDef typeDef;
        try {
            typeDef = repositoryHelper.getTypeDef(repositoryName,
                    "entity-type-guid",
                    "entity-type-name",
                    type.getTypeDefGUID(),
                    type.getTypeDefName(),
                    methodName);

        }
        catch (Exception e) {
            log.error("{} caught exception {}", methodName, e.getMessage());

            throw new RepositoryErrorException(GraphOMRSErrorCode.ENTITY_TYPE_ERROR.getMessageDefinition(type.getTypeDefName(), methodName,
                                                                                                         this.getClass().getName(),
                                                                                                         repositoryName),
                    this.getClass().getName(),
                    methodName, e);
        }

        // properties
        String stringProps = (String) getVertexProperty(vertex, "instanceProperties");

        if (stringProps != null) {
            try {
                InstanceProperties instanceProperties = OBJECT_READER.readValue(stringProps, InstanceProperties.class);
                log.debug("{} entity has deserialized properties {}", methodName, instanceProperties);
                List<TypeDefAttribute> propertiesDefinition = repositoryHelper.getAllPropertiesForTypeDef(repositoryName, typeDef, methodName);
                InstanceProperties uniqueAttributes = new InstanceProperties();

                if (propertiesDefinition != null)
                {
                    for (TypeDefAttribute typeDefAttribute : propertiesDefinition)
                    {
                        if (typeDefAttribute != null)
                        {
                            String propertyName = typeDefAttribute.getAttributeName();

                            if ((typeDefAttribute.isUnique()) && (propertyName != null))
                            {
                                InstancePropertyValue propertyValue = instanceProperties.getPropertyValue(
                                        propertyName);

                                if (propertyValue != null)
                                {
                                    uniqueAttributes.setProperty(propertyName, propertyValue);
                                }
                            }
                        }
                    }
                }

                if (uniqueAttributes.getPropertyCount() > 0)
                {
                    entity.setUniqueProperties(uniqueAttributes);
                }

            } catch (Exception exc) {
                log.error("{} caught exception {}", methodName, exc.getMessage());
                throw new RepositoryErrorException(GraphOMRSErrorCode.ENTITY_PROPERTIES_ERROR.getMessageDefinition(entity.getGUID(), methodName,
                                                                                                                   this.getClass().getName(),
                                                                                                                   repositoryName),
                        this.getClass().getName(),
                        methodName, exc);
            }
        }
        else {
            log.debug("{} vertex has no instance properties", methodName);
        }

    }


    void mapVertexToEntitySummary(Vertex vertex, EntitySummary entity)
            throws RepositoryErrorException
    {
        String methodName = "mapVertexToEntitySummary";


        entity.setGUID((String) getVertexProperty( vertex, PROPERTY_KEY_ENTITY_GUID));
        entity.setReIdentifiedFromGUID((String) getVertexProperty( vertex, PROPERTY_KEY_ENTITY_REIDENTIFIED_FROM_GUID));
        entity.setMetadataCollectionId((String) getVertexProperty( vertex, PROPERTY_KEY_ENTITY_METADATACOLLECTION_ID));
        entity.setMetadataCollectionName((String) getVertexProperty( vertex, PROPERTY_KEY_ENTITY_METADATACOLLECTION_NAME));
        entity.setCreatedBy((String) getVertexProperty( vertex,  PROPERTY_KEY_ENTITY_CREATED_BY));
        entity.setCreateTime((Date)getVertexProperty( vertex, PROPERTY_KEY_ENTITY_CREATE_TIME));
        entity.setUpdatedBy((String) getVertexProperty( vertex,  PROPERTY_KEY_ENTITY_UPDATED_BY));
        entity.setUpdateTime((Date)getVertexProperty( vertex, PROPERTY_KEY_ENTITY_UPDATE_TIME));
        entity.setVersion((long) getVertexProperty( vertex, PROPERTY_KEY_ENTITY_VERSION));
        entity.setInstanceLicense((String) getVertexProperty( vertex, PROPERTY_KEY_ENTITY_INSTANCE_LICENSE));
        entity.setInstanceURL((String) getVertexProperty( vertex,  PROPERTY_KEY_ENTITY_INSTANCE_URL));

        // Retrieve the type name from the vertex, use the RH to retrieve the type
        // then use the necessary type fields to construct a TypeDefSummary and pass
        // that to the RH to create a new InstanceType...
        try {
            String typeName = (String) getVertexProperty( vertex, PROPERTY_KEY_ENTITY_TYPE_NAME);
            TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeName);
            InstanceType instanceType = repositoryHelper.getNewInstanceType(repositoryName, typeDef);
            entity.setType(instanceType);

        } catch (TypeErrorException e) {
            log.error("{} caught exception {}", methodName, e.getMessage());

            throw new RepositoryErrorException(GraphOMRSErrorCode.ENTITY_PROPERTIES_ERROR.getMessageDefinition(entity.getGUID(), methodName,
                                                                                                               this.getClass().getName(),
                                                                                                               repositoryName),
                    this.getClass().getName(),
                    methodName, e);
        }

        GraphOMRSMapperUtils mapperUtils = new GraphOMRSMapperUtils();

        Integer provenanceOrdinal = (Integer) getVertexProperty( vertex, PROPERTY_KEY_ENTITY_INSTANCE_PROVENANCE_TYPE);
        InstanceProvenanceType instanceProvenanceType = mapperUtils.mapProvenanceOrdinalToEnum(provenanceOrdinal);
        entity.setInstanceProvenanceType(instanceProvenanceType);

        Integer statusOrdinal = (Integer) getVertexProperty( vertex, PROPERTY_KEY_ENTITY_CURRENT_STATUS);
        InstanceStatus instanceStatus = mapperUtils.mapStatusOrdinalToEnum(statusOrdinal);
        entity.setStatus(instanceStatus);

        Integer statusOnDeleteOrdinal = (Integer) getVertexProperty( vertex, PROPERTY_KEY_ENTITY_STATUS_ON_DELETE);
        InstanceStatus statusOnDelete = mapperUtils.mapStatusOrdinalToEnum(statusOnDeleteOrdinal);
        entity.setStatusOnDelete(statusOnDelete);


        String maintainedByString = (String) getVertexProperty(vertex, PROPERTY_KEY_ENTITY_MAINTAINED_BY);
        if (maintainedByString != null) {
            try {
                List<String> maintainedByList = (List<String>) OBJECT_READER.readValue(maintainedByString, List.class);
                log.debug("{} entity has deserialized maintainedBy {}", methodName, maintainedByList);
                entity.setMaintainedBy(maintainedByList);
            } catch (Exception exc) {
                log.error("{} caught exception {}", methodName, exc.getMessage());
                throw new RepositoryErrorException(GraphOMRSErrorCode.ENTITY_PROPERTIES_ERROR.getMessageDefinition(entity.getGUID(), methodName,
                                                                                                                   this.getClass().getName(),
                                                                                                                   repositoryName),
                        this.getClass().getName(),
                        methodName, exc);
            }
        }

        entity.setReplicatedBy((String) getVertexProperty( vertex,  PROPERTY_KEY_ENTITY_REPLICATED_BY));


        String mappingPropertiesString = (String) getVertexProperty(vertex, PROPERTY_KEY_ENTITY_MAPPING_PROPERTIES);
        if (mappingPropertiesString != null) {
            try {
                TypeReference<Map<String, Serializable>> typeReference = new TypeReference<Map<String, Serializable>>() {};
                Map<String, Serializable> mappingPropertiesMap = OBJECT_MAPPER.readValue(mappingPropertiesString, typeReference);
                log.debug("{} entity has deserialized mappingProperties {}", methodName, mappingPropertiesMap);
                entity.setMappingProperties(mappingPropertiesMap);
            } catch (Exception exc) {
                log.error("{} caught exception {}", methodName, exc.getMessage());
                throw new RepositoryErrorException(GraphOMRSErrorCode.ENTITY_PROPERTIES_ERROR.getMessageDefinition(entity.getGUID(), methodName,
                                                                                                                   this.getClass().getName(),
                                                                                                                   repositoryName),
                        this.getClass().getName(),
                        methodName, exc);
            }
        }


        // Get the classifications
        List<Classification> classifications = new ArrayList<>();
        Iterator<Edge> classifierEdges = vertex.edges(Direction.OUT, "Classifier");
        while (classifierEdges.hasNext()) {
            Edge classifierEdge = classifierEdges.next();
            Classification classification = new Classification();
            Vertex classificationVertex = classifierEdge.inVertex();
            classificationMapper.mapVertexToClassification(classificationVertex, classification);
            log.debug("{} entity has classification: {} ", methodName, classification.getName());
            classifications.add(classification);
        }
        if (classifications.isEmpty()) {
            classifications = null;
        }
        entity.setClassifications(classifications);

    }




    Boolean isProxy(Vertex vertex) {
        Boolean isProxy;
        isProxy = (Boolean) getVertexProperty(vertex, PROPERTY_KEY_ENTITY_IS_PROXY);
        return isProxy;
    }

    public void setProxy(Vertex vertex) {
        vertex.property(PROPERTY_KEY_ENTITY_IS_PROXY, true);
    }

    void clearProxy(Vertex vertex) {
        vertex.property(PROPERTY_KEY_ENTITY_IS_PROXY, false);
    }


    String getEntityGUID(Vertex vertex) {
        return  (String) getVertexProperty(vertex, PROPERTY_KEY_ENTITY_GUID);
    }

    String getEntityMetadataCollectionId(Vertex vertex) {
        return (String) getVertexProperty(vertex, PROPERTY_KEY_ENTITY_METADATACOLLECTION_ID);
    }


}
