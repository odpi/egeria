/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector;



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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector.GraphOMRSConstants.*;


public class GraphOMRSRelationshipMapper {

    private static final Logger log = LoggerFactory.getLogger(GraphOMRSRelationshipMapper.class);

    private String               repositoryName;
    private String               metadataCollectionId;
    private OMRSRepositoryHelper repositoryHelper;

    public GraphOMRSRelationshipMapper(String               metadataCollectionId,
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


    // Inbound methods - i.e. writing to store




    public void mapRelationshipToEdge(Relationship relationship, Edge edge)
            throws RepositoryErrorException
    {
        final String methodName = "mapRelationshipToEdge";

        // Some properties are mandatory. If any of these are null then throw exception
        boolean missingAttribute = false;

        if (relationship.getGUID() != null)
            edge.property(PROPERTY_KEY_RELATIONSHIP_GUID, relationship.getGUID());
        else
            missingAttribute = true;

        InstanceType instanceType = relationship.getType();
        if (instanceType != null && instanceType.getTypeDefName() != null)
            edge.property(PROPERTY_KEY_RELATIONSHIP_TYPE_NAME, instanceType.getTypeDefName());
        else
            missingAttribute = true;

        if (this.metadataCollectionId != null)
            edge.property(PROPERTY_KEY_RELATIONSHIP_METADATACOLLECTION_ID, this.metadataCollectionId);
        else
            missingAttribute = true;

        if (this.repositoryName != null)
            edge.property(PROPERTY_KEY_RELATIONSHIP_METADATACOLLECTION_NAME, this.repositoryName);
        else
            missingAttribute = true;

        edge.property(PROPERTY_KEY_RELATIONSHIP_VERSION, relationship.getVersion());


        if (missingAttribute) {
            log.error("{} relationship is missing a core attribute{}", methodName);
            GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationship.getGUID(), methodName,
                    this.getClass().getName(),
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }


        // Other properties can be removed if set to null

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
            edge.property(PROPERTY_KEY_RELATIONSHIP_PROVENANCE_TYPE, relationship.getInstanceProvenanceType().getOrdinal());   // ** ordinal mapping
        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_PROVENANCE_TYPE);
            if (ep != null)
                ep.remove();
        }


        if (relationship.getStatus() != null) {
            edge.property(PROPERTY_KEY_RELATIONSHIP_STATUS, relationship.getStatus().getOrdinal());                           // ** ordinal mapping
        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_STATUS);
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

            } catch (Throwable exc) {
                GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                        this.getClass().getName(),
                        repositoryName);

                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }

        }
        else {
            Property ep = edge.property(PROPERTY_KEY_RELATIONSHIP_MAINTAINED_BY);
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

            } catch (Throwable exc) {
                GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                        this.getClass().getName(),
                        repositoryName);

                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }


            // Secondly write all primitive properties as-is to support searches

            String typeName = relationship.getType().getTypeDefName();
            TypeDef typeDef = repositoryHelper.getTypeDefByName(repositoryName, typeName);


            // There is no need to qualify property names for relationships, as they do not have inheritance but for consistency and future proofing
            // they are still qualified by type name.
            // For the type of the entity, walk its type hierarchy and construct a map of short prop name -> qualified prop name.
            Map<String,String> qualifiedPropertyNames = GraphOMRSMapperUtils.getQualifiedPropertyNamesForTypeDef(typeDef, repositoryName, repositoryHelper);

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
                    InstancePropertyCategory ipvCat = ipv.getInstancePropertyCategory();
                    if (ipvCat == InstancePropertyCategory.PRIMITIVE) {
                        // Primitives are stored directly in the graph
                        PrimitivePropertyValue ppv = (PrimitivePropertyValue) ipv;
                        Object primValue = ppv.getPrimitiveValue();
                        edge.property(qualifiedPropName, primValue);
                    } else {
                        log.debug("{} non-primitive instance property {}", propertyName);
                    }

                } else {
                    // no value has been specified - remove the property from the edge
                    Property ep = edge.property(qualifiedPropName);
                    if (ep != null) {
                        ep.remove();
                    }
                }
            }
        }
        else {
            log.debug("{} relationship has no properties", methodName);
        }


    }



    // Outbound methods - i.e. reading from store



    public void mapEdgeToRelationship(Edge edge, Relationship relationship)
            throws
            RepositoryErrorException
    {
        final String methodName = "mapEdgeToRelationship";

        relationship.setGUID((String) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_GUID));
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
            TypeDefSummary typeDefSummary = new TypeDefSummary(TypeDefCategory.RELATIONSHIP_DEF, typeName, typeDef.getName(), typeDef.getVersion(), typeDef.getVersionName());
            InstanceType instanceType = repositoryHelper.getNewInstanceType(repositoryName, typeDefSummary);
            relationship.setType(instanceType);

        } catch (TypeErrorException e) {
            log.error("{} caught TypeErrorException {}", methodName, e.getMessage());
            GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.RELATIONSHIP_TYPE_NOT_KNOWN;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(typeName, methodName,
                    this.getClass().getName(),
                    repositoryName);

            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }

        Integer provenanceOrdinal = (Integer) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_PROVENANCE_TYPE);
        InstanceProvenanceType instanceProvenanceType = GraphOMRSMapperUtils.mapProvenanceOrdinalToEnum(provenanceOrdinal);
        relationship.setInstanceProvenanceType(instanceProvenanceType);

        Integer statusOrdinal = (Integer) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_STATUS);
        InstanceStatus instanceStatus = GraphOMRSMapperUtils.mapStatusOrdinalToEnum(statusOrdinal);
        relationship.setStatus(instanceStatus);

        Integer statusOnDeleteOrdinal = (Integer) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_STATUS_ON_DELETE);
        InstanceStatus statusOnDelete = GraphOMRSMapperUtils.mapStatusOrdinalToEnum(statusOnDeleteOrdinal);
        relationship.setStatusOnDelete(statusOnDelete);

        // maintainedBy
        String maintainedByString = (String) getEdgeProperty(edge, PROPERTY_KEY_RELATIONSHIP_MAINTAINED_BY);
        if (maintainedByString != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                List<String> maintainedByList = (List<String>) objectMapper.readValue(maintainedByString, List.class);
                log.debug("{} edge has deserialized maintainedBy list {}", methodName, maintainedByList);
                relationship.setMaintainedBy(maintainedByList);

            } catch (Throwable exc) {
                GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationship.getGUID(), methodName,
                        this.getClass().getName(),
                        repositoryName);

                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
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

            } catch (Throwable exc) {
                GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.RELATIONSHIP_PROPERTIES_ERROR;

                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(relationship.getGUID(), methodName,
                        this.getClass().getName(),
                        repositoryName);

                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }
        }
        else {
            log.debug("{} relationship has no properties", methodName);
        }
    }



}
