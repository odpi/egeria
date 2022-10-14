/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector;



import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Property;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.*;

class GraphOMRSRelationshipMapper {

    private static final Logger log = LoggerFactory.getLogger(GraphOMRSRelationshipMapper.class);

    private String               repositoryName;
    private String               metadataCollectionId;
    private OMRSRepositoryHelper repositoryHelper;

    GraphOMRSRelationshipMapper(String               metadataCollectionId,
                                String               repositoryName,
                                OMRSRepositoryHelper repositoryHelper) {

        this.metadataCollectionId   = metadataCollectionId;
        this.repositoryName         = repositoryName;
        this.repositoryHelper       = repositoryHelper;
    }




    private Object getEdgeProperty(Edge edge, String propName)
    {
        Property ep = edge.property(propName);
        if (ep == null || !ep.isPresent())
            return null;
        else
            return ep.value();
    }

    private void addProperty(Edge edge, String propertyName, String qualifiedPropName, InstancePropertyValue ipv) {
        InstancePropertyCategory ipvCat = ipv.getInstancePropertyCategory();
        if (ipvCat == InstancePropertyCategory.PRIMITIVE) {
            // Primitives are stored directly in the graph
            PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
            Object primValue = ppv.getPrimitiveValue();
            if (primValue != null) {
                edge.property(getPropertyKeyRelationship(qualifiedPropName), primValue);
            } else {
                removeProperty(edge, qualifiedPropName);
            }
        } else {
            log.debug("non-primitive instance property {}", propertyName);
        }
    }

    private void removeProperty(Edge edge, String qualifiedPropName) {
        // no value has been specified - remove the property from the edge
        Property ep = edge.property(getPropertyKeyRelationship(qualifiedPropName));
        if (ep != null) {
            ep.remove();
        }
    }


    // Inbound methods - i.e. writing to store


    /*
     * This method is used to store a local relationship instance or to save a reference copy of a remote relationship instance.
     * The guid and type information must be preserved.
     * If the metadataCollectionId is null then
     */
    void mapRelationshipToEdge(Relationship relationship, Edge edge)
            throws RepositoryErrorException
    {
        final String methodName = "mapRelationshipToEdge";

        // Some properties are mandatory. If any of these are null then throw exception
        boolean missingAttribute = false;
        String  missingAttributeName = null;

        if (relationship.getGUID() != null)
            edge.property(PROPERTY_KEY_RELATIONSHIP_GUID, relationship.getGUID());
        else {
            missingAttribute = true;
            missingAttributeName = "guid";
        }

        InstanceType instanceType = relationship.getType();
        if (instanceType != null && instanceType.getTypeDefName() != null)
            edge.property(PROPERTY_KEY_RELATIONSHIP_TYPE_NAME, instanceType.getTypeDefName());
        else {
            missingAttribute = true;
            missingAttributeName = "type or typeName";
        }

        String relationshipMetadataCollectionId = relationship.getMetadataCollectionId();
        if (relationshipMetadataCollectionId != null)
            edge.property(PROPERTY_KEY_RELATIONSHIP_METADATACOLLECTION_ID, relationshipMetadataCollectionId);
        else {
            missingAttribute = true;
            missingAttributeName = "metadataCollectionId";
        }

        if (missingAttribute) {
            log.error("{} relationship is missing a core attribute {}", methodName, missingAttributeName);
            throw new RepositoryErrorException(GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR.getMessageDefinition(relationship.getGUID(), methodName,
                                                                                                                     this.getClass().getName(),
                                                                                                                     repositoryName),
                    this.getClass().getName(),
                    methodName);
        }

        edge.property(PROPERTY_KEY_RELATIONSHIP_VERSION, relationship.getVersion());

        // Other properties can be removed if set to null

        if (relationship.getReIdentifiedFromGUID() != null) {
            edge.property(PROPERTY_KEY_RELATIONSHIP_REIDENTIFIED_FROM_GUID, relationship.getReIdentifiedFromGUID());
        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_REIDENTIFIED_FROM_GUID);
            if (ep != null)
                ep.remove();
        }

        if (relationship.getMetadataCollectionName() != null) {
            edge.property(PROPERTY_KEY_RELATIONSHIP_METADATACOLLECTION_NAME, relationship.getMetadataCollectionName());
        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_METADATACOLLECTION_NAME);
            if (ep != null)
                ep.remove();
        }

        if (relationship.getCreatedBy() != null) {
            edge.property(PROPERTY_KEY_RELATIONSHIP_CREATED_BY, relationship.getCreatedBy());
        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_CREATED_BY);
            if (ep != null)
                ep.remove();
        }

        if (relationship.getCreateTime() != null) {
            edge.property(PROPERTY_KEY_RELATIONSHIP_CREATE_TIME, relationship.getCreateTime());
        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_CREATE_TIME);
            if (ep != null)
                ep.remove();
        }

        if (relationship.getUpdatedBy() != null) {
            edge.property(PROPERTY_KEY_RELATIONSHIP_UPDATED_BY, relationship.getUpdatedBy());
        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_UPDATED_BY);
            if (ep != null)
                ep.remove();
        }

        if (relationship.getUpdateTime() != null) {
            edge.property(PROPERTY_KEY_RELATIONSHIP_UPDATE_TIME, relationship.getUpdateTime());
        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_UPDATE_TIME);
            if (ep != null)
                ep.remove();
        }


        edge.property("version", relationship.getVersion());


        if (relationship.getInstanceProvenanceType() != null) {
            edge.property(PROPERTY_KEY_RELATIONSHIP_INSTANCE_PROVENANCE_TYPE, relationship.getInstanceProvenanceType().getOrdinal());   // ** ordinal mapping
        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_INSTANCE_PROVENANCE_TYPE);
            if (ep != null)
                ep.remove();
        }


        if (relationship.getStatus() != null) {
            edge.property(PROPERTY_KEY_RELATIONSHIP_CURRENT_STATUS, relationship.getStatus().getOrdinal());                           // ** ordinal mapping
        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_CURRENT_STATUS);
            if (ep != null)
                ep.remove();
        }

        if (relationship.getStatusOnDelete() != null) {
            edge.property(PROPERTY_KEY_RELATIONSHIP_STATUS_ON_DELETE, relationship.getStatusOnDelete().getOrdinal());         // ** ordinal mapping
        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_STATUS_ON_DELETE);
            if (ep != null)
                ep.remove();
        }


        if (relationship.getInstanceURL() != null) {
            edge.property(PROPERTY_KEY_RELATIONSHIP_INSTANCE_URL, relationship.getInstanceURL());
        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_INSTANCE_URL);
            if (ep != null)
                ep.remove();
        }

        if (relationship.getInstanceLicense() != null) {
            edge.property(PROPERTY_KEY_RELATIONSHIP_INSTANCE_LICENSE, relationship.getInstanceLicense());
        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_INSTANCE_LICENSE);
            if (ep != null)
                ep.remove();
        }

        // maintainedBy property

        List<String> maintainedByList = relationship.getMaintainedBy();
        if (maintainedByList != null && !maintainedByList.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString;
            try {
                jsonString = objectMapper.writeValueAsString(maintainedByList);
                edge.property(PROPERTY_KEY_RELATIONSHIP_MAINTAINED_BY, jsonString);

            } catch (Exception exc) {

                throw new RepositoryErrorException(GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR.getMessageDefinition(methodName,
                                                                                                                         this.getClass().getName(),
                                                                                                                         repositoryName),
                        this.getClass().getName(),
                        methodName);
            }

        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_MAINTAINED_BY);
            if (ep != null)
                ep.remove();
        }

        if (relationship.getReplicatedBy() != null) {
            edge.property(PROPERTY_KEY_RELATIONSHIP_REPLICATED_BY, relationship.getReplicatedBy());
        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_REPLICATED_BY);
            if (ep != null)
                ep.remove();
        }


        // mappingProperties property

        Map<String, Serializable> mappingProperties = relationship.getMappingProperties();
        if (mappingProperties != null && !mappingProperties.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString;
            try {
                jsonString = objectMapper.writeValueAsString(mappingProperties);
                edge.property(PROPERTY_KEY_RELATIONSHIP_MAPPING_PROPERTIES, jsonString);

            } catch (Exception exc) {

                throw new RepositoryErrorException(GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR.getMessageDefinition(methodName,
                                                                                                                         this.getClass().getName(),
                                                                                                                         repositoryName),
                        this.getClass().getName(),
                        methodName, exc);
            }

        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_MAPPING_PROPERTIES);
            if (ep != null)
                ep.remove();
        }





        // relationship type-specific properties

        InstanceProperties instanceProperties = relationship.getProperties();
        if (instanceProperties != null) {
            // First approach is to write properties as json - useful for handling collections and possibly for full text/string matching???
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString;

            try {
                jsonString = objectMapper.writeValueAsString(instanceProperties);
                edge.property("relationshipProperties", jsonString);

            } catch (Exception exc) {
                throw new RepositoryErrorException(GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR.getMessageDefinition(methodName,
                                                                                                                         this.getClass().getName(),
                                                                                                                         repositoryName),
                        this.getClass().getName(),
                        methodName, exc);
            }


            // Secondly write all primitive properties as-is to support searches

            String typeName = relationship.getType().getTypeDefName();
            TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeName);


            // There is no need to qualify property names for relationships, as they do not have inheritance but for consistency and future proofing
            // they are still qualified by type name.
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
                    addProperty(edge, propertyName, qualifiedPropName, ipv);
                }
                else {
                    removeProperty(edge, qualifiedPropName);
                }
            }
        }
        else {
            log.debug("{} relationship has no properties", methodName);
        }


    }



    // Outbound methods - i.e. reading from store



    void mapEdgeToRelationship(Edge edge, Relationship relationship)
            throws
            RepositoryErrorException
    {
        final String methodName = "mapEdgeToRelationship";

        relationship.setGUID((String) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_GUID));
        relationship.setReIdentifiedFromGUID((String) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_REIDENTIFIED_FROM_GUID));
        relationship.setMetadataCollectionId((String) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_METADATACOLLECTION_ID));
        relationship.setMetadataCollectionName((String) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_METADATACOLLECTION_NAME));
        relationship.setCreatedBy((String) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_CREATED_BY));
        relationship.setCreateTime((Date) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_CREATE_TIME));
        relationship.setUpdatedBy((String) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_UPDATED_BY));
        relationship.setUpdateTime((Date) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_UPDATE_TIME));
        relationship.setVersion((long) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_VERSION));
        relationship.setInstanceLicense((String) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_INSTANCE_LICENSE));
        relationship.setInstanceURL((String) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_INSTANCE_URL));

        // Retrieve the type name from the edge, use the RH to retrieve the type
        // then use the necessary type fields to construct a TypeDefSummary and pass
        // that to the RH to create a new InstanceType...
        String typeName = (String) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_TYPE_NAME);
        try {
            TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeName);
            InstanceType instanceType = repositoryHelper.getNewInstanceType(repositoryName, typeDef);
            relationship.setType(instanceType);

        } catch (TypeErrorException e) {
            log.error("{} caught TypeErrorException {}", methodName, e.getMessage());

            throw new RepositoryErrorException(GraphOMRSErrorCode.RELATIONSHIP_TYPE_NOT_KNOWN.getMessageDefinition(typeName, methodName,
                                                                                                                   this.getClass().getName(),
                                                                                                                   repositoryName),
                    this.getClass().getName(),
                    methodName, e);

        }

        GraphOMRSMapperUtils mapperUtils = new GraphOMRSMapperUtils();

        Integer provenanceOrdinal = (Integer) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_INSTANCE_PROVENANCE_TYPE);
        InstanceProvenanceType instanceProvenanceType = mapperUtils.mapProvenanceOrdinalToEnum(provenanceOrdinal);
        relationship.setInstanceProvenanceType(instanceProvenanceType);

        Integer statusOrdinal = (Integer) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_CURRENT_STATUS);
        InstanceStatus instanceStatus = mapperUtils.mapStatusOrdinalToEnum(statusOrdinal);
        relationship.setStatus(instanceStatus);

        Integer statusOnDeleteOrdinal = (Integer) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_STATUS_ON_DELETE);
        InstanceStatus statusOnDelete = mapperUtils.mapStatusOrdinalToEnum(statusOnDeleteOrdinal);
        relationship.setStatusOnDelete(statusOnDelete);

        // maintainedBy
        String maintainedByString = (String) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_MAINTAINED_BY);
        if (maintainedByString != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                List<String> maintainedByList = (List<String>) objectMapper.readValue(maintainedByString, List.class);
                log.debug("{} edge has deserialized maintainedBy list {}", methodName, maintainedByList);
                relationship.setMaintainedBy(maintainedByList);

            } catch (Exception exc) {

                throw new RepositoryErrorException(GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR.getMessageDefinition(relationship.getGUID(),
                                                                                                                         methodName,
                                                                                                                         this.getClass().getName(),
                                                                                                                         repositoryName),
                        this.getClass().getName(),
                        methodName, exc);
            }
        }

        relationship.setReplicatedBy((String) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_REPLICATED_BY));


        // mappingProperties
        String mappingPropertiesString = (String) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_MAPPING_PROPERTIES);
        if (mappingPropertiesString != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                TypeReference<Map<String, Serializable>> typeReference = new TypeReference<Map<String, Serializable>>() {};
                Map<String, Serializable> mappingPropertiesMap = objectMapper.readValue(mappingPropertiesString, typeReference);
                log.debug("{} edge has deserialized mappingProperties {}", methodName, mappingPropertiesMap);
                relationship.setMappingProperties(mappingPropertiesMap);

            } catch (Exception exc) {

                throw new RepositoryErrorException(GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR.getMessageDefinition(relationship.getGUID(),
                                                                                                                         methodName,
                                                                                                                         this.getClass().getName(),
                                                                                                                         repositoryName),
                        this.getClass().getName(),
                        methodName, exc);
            }
        }


        // relationshipProperties
        String stringProps = (String) (getEdgeProperty(edge, "relationshipProperties"));
        if (stringProps != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                InstanceProperties instanceProperties = objectMapper.readValue(stringProps, InstanceProperties.class);
                log.debug("{} relationship has deserialized properties {}", methodName, instanceProperties);
                relationship.setProperties(instanceProperties);

            } catch (Exception exc) {
                throw new RepositoryErrorException(GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR.getMessageDefinition(relationship.getGUID(),
                                                                                                                         methodName,
                                                                                                                         this.getClass().getName(),
                                                                                                                         repositoryName),
                        this.getClass().getName(),
                        methodName, exc);
            }
        }
        else {
            log.debug("{} relationship has no properties", methodName);
        }
    }


    String getRelationshipMetadataCollectionId(Edge edge) {
        return (String) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_METADATACOLLECTION_ID);
    }

}
