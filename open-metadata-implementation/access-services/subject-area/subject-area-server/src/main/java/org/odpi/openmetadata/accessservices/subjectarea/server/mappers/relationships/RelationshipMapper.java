/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.RelationshipEnd;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.IRelationshipMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.graph.NodeTypeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Static mapping methods to map between the relationship and the equivalent generated OMRSRelationshipBean
 */
public abstract class RelationshipMapper<R extends Relationship> implements IRelationshipMapper<R> {
    protected final OpenMetadataAPIGenericHandler genericHandler;
    protected final OMRSRepositoryHelper repositoryHelper;

    public RelationshipMapper(OpenMetadataAPIGenericHandler genericHandler) {
        this.genericHandler = genericHandler;
        this.repositoryHelper = genericHandler.getRepositoryHelper();
    }

    public R map(org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship omrsRelationship) {
        R omasRelationship = getRelationshipInstance();
        omasRelationship.setSystemAttributes(SubjectAreaUtils.createSystemAttributes(omrsRelationship));
        EntityProxy proxy1 = omrsRelationship.getEntityOneProxy();
        if (proxy1 != null) {
            RelationshipEnd end1 = omasRelationship.getEnd1();
            String guid1 = proxy1.getGUID();
            if (guid1 != null) {
                end1.setNodeGuid(guid1);
            }
            if (proxy1.getUniqueProperties() != null) {
                Map<String, InstancePropertyValue> map = proxy1.getUniqueProperties().getInstanceProperties();
                PrimitivePropertyValue qualifiedNamePropertyValue = (PrimitivePropertyValue) map.get("qualifiedName");
                if (qualifiedNamePropertyValue != null) {
                    end1.setNodeQualifiedName(qualifiedNamePropertyValue.getPrimitiveValue().toString());
                }
            }
        }
        EntityProxy proxy2 = omrsRelationship.getEntityTwoProxy();
        if (proxy2 != null) {
            RelationshipEnd end2 = omasRelationship.getEnd2();
            String guid2 = proxy2.getGUID();
            if (guid2 != null) {
                end2.setNodeGuid(guid2);
            }
            if (proxy2.getUniqueProperties() != null) {
                Map<String, InstancePropertyValue> map  = proxy2.getUniqueProperties().getInstanceProperties();
                PrimitivePropertyValue qualifiedNamePropertyValue = (PrimitivePropertyValue) map.get("qualifiedName");
                if (qualifiedNamePropertyValue != null) {
                    end2.setNodeQualifiedName(qualifiedNamePropertyValue.getPrimitiveValue().toString());
                }
            }
        }
        // set readonly
        if (omrsRelationship.getInstanceProvenanceType() != InstanceProvenanceType.LOCAL_COHORT) {
            omasRelationship.setReadOnly(true);
        }

        // Set properties
        InstanceProperties relationshipProperties = omrsRelationship.getProperties();
        if (relationshipProperties != null) {
            mapRelationshipEffectivityToInstanceProperties(omasRelationship, relationshipProperties);
            Iterator<String> omrsPropertyIterator = relationshipProperties.getPropertyNames();
            while (omrsPropertyIterator.hasNext()) {
                String propertyName = omrsPropertyIterator.next();
                // this is a property we expect
                InstancePropertyValue value = relationshipProperties.getPropertyValue(propertyName);

                // supplied guid matches the expected type

                Object actualValue;
                switch (value.getInstancePropertyCategory()) {
                    case PRIMITIVE:
                        PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) value;
                        actualValue = primitivePropertyValue.getPrimitiveValue();

                        if (!mapPrimitiveToRelationship(omasRelationship, propertyName, actualValue)) {
                            // there are properties we are not aware of, as they have been added by a subtype, put them in the additionalProperties
                            if (null == omasRelationship.getAdditionalProperties()) {
                                omasRelationship.setAdditionalProperties(new HashMap<String, String>());
                            }
                            omasRelationship.getAdditionalProperties().put(propertyName, actualValue.toString());
                        }
                        break;
                    case ENUM:
                        EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                        if (!mapEnumToRelationship(omasRelationship, propertyName, enumPropertyValue)) {
                            if (null == omasRelationship.getAdditionalProperties()) {
                                omasRelationship.setAdditionalProperties(new HashMap<String, String>());
                            }
                            omasRelationship.getAdditionalProperties().put(propertyName, enumPropertyValue.valueAsString());
                        }

                        break;
                    case MAP:
                        MapPropertyValue mapPropertyValue = (MapPropertyValue) value;
                        InstanceProperties instancePropertyForMap = mapPropertyValue.getMapValues();
                        if (!mapMapToRelationship(omasRelationship, propertyName, instancePropertyForMap)) {
                            if (null == omasRelationship.getAdditionalProperties()) {
                                omasRelationship.setAdditionalProperties(new HashMap<String, String>());
                            }
                            omasRelationship.getAdditionalProperties().put(propertyName, mapPropertyValue.valueAsString());
                        }
                        break;
                    case ARRAY:
                    case STRUCT:
                    case UNKNOWN:
                        // error
                        break;
                }

            }   // end while
        }
        return omasRelationship;
    }

    /**
     * Map the instance properties to the relationship
     * This method should be overridden by subclasses to map the properties to the relationship.
     *
     * @param relationship       relationship to be updated
     * @param instanceProperties properties to use for the update
     */
    protected void mapInstancePropertiesToRelationship(R relationship, InstanceProperties instanceProperties) {
    }

    /**
     * map the effectivity dates from the relationship to the InstanceProperties
     *
     * @param relationship       relationship
     * @param instanceProperties instance properties to update
     */
    private void mapRelationshipEffectivityToInstanceProperties(R relationship, InstanceProperties instanceProperties) {
        Long fromTime = relationship.getEffectiveFromTime();
        Long toTime = relationship.getEffectiveToTime();
        if (fromTime != null) {
            instanceProperties.setEffectiveFromTime(new Date(fromTime));
        }
        if (toTime != null) {
            instanceProperties.setEffectiveToTime(new Date(toTime));
        }
    }

    /**
     * Map omasRelationship to the omrs relationship equivalent
     *
     * @param omasRelationship supplied omasRelationship
     * @return omrs relationship equivalent
     */
    public org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship map(R omasRelationship) throws InvalidParameterException {


        org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship omrsRelationship = new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship();
        InstanceProperties instanceProperties = new InstanceProperties();
        omrsRelationship.setProperties(instanceProperties);
        mapRelationshipToInstanceProperties(omasRelationship, instanceProperties);
        String proxy1Guid = omasRelationship.getEnd1().getNodeGuid();
        String proxy1NodeTypeName = omasRelationship.getEnd1().getNodeTypeName();
        String proxy1TypeName = NodeTypeMapper.mapNodeTypeNameToEntityTypeName(proxy1NodeTypeName);
        InstanceType typeEnd1 = new InstanceType();
        typeEnd1.setTypeDefName(proxy1TypeName);
        String proxy1TypeGuid = new InvalidParameterHandler().validateTypeName(proxy1TypeName,
                                                                                proxy1TypeName,
                                                                                  genericHandler.getServiceName(),
                                                                                  "map end1 " +omasRelationship.getRelationshipType(),
                                                                                genericHandler.getRepositoryHelper());
        typeEnd1.setTypeDefGUID(proxy1TypeGuid);

        String proxy2Guid = omasRelationship.getEnd2().getNodeGuid();
        String proxy2NodeTypeName = omasRelationship.getEnd2().getNodeTypeName();
        String proxy2TypeName = NodeTypeMapper.mapNodeTypeNameToEntityTypeName(proxy2NodeTypeName);
        String proxy2TypeGuid =  new InvalidParameterHandler().validateTypeName(proxy2TypeName,
                                                                                proxy2TypeName,
                                                                                genericHandler.getServiceName(),
                                                                                "map end2 " +omasRelationship.getRelationshipType(),
                                                                                genericHandler.getRepositoryHelper());

        InstanceType typeEnd2 = new InstanceType();
        typeEnd2.setTypeDefName(proxy2TypeName);
        typeEnd2.setTypeDefGUID(proxy2TypeGuid);

        EntityProxy proxy1 = new EntityProxy();
        proxy1.setGUID(proxy1Guid);

        omrsRelationship.setEntityOneProxy(proxy1);
        EntityProxy proxy2 = new EntityProxy();
        proxy2.setGUID(proxy2Guid);
        omrsRelationship.setEntityTwoProxy(proxy2);
        String typeName = getTypeName();
        TypeDef typedef = repositoryHelper.getTypeDefByName(genericHandler.getServiceName(), typeName);
        InstanceType type = new InstanceType();
        type.setTypeDefName(typedef.getName());
        type.setTypeDefGUID(typedef.getGUID());
        omrsRelationship.setType(type);

        if (omasRelationship.getSystemAttributes() != null) {
            SubjectAreaUtils.populateSystemAttributesForInstanceAuditHeader(omasRelationship.getSystemAttributes(), omrsRelationship);
            omrsRelationship.setGUID(omasRelationship.getSystemAttributes().getGUID());
        }
        return omrsRelationship;
    }

    /**
     * get type name
     *
     * @return the omrs type name.
     */
    public abstract String getTypeName();

    abstract R getRelationshipInstance();

    /**
     * get the relationshipTypeDef Guid
     * This method should be overridden to provide the appropriate guid for the type.
     *
     * @return the guid of the relationship typedef
     */
    @Override
    public String getTypeDefGuid() {
        String guid = null;
        TypeDef typeDef = repositoryHelper.getTypeDefByName(genericHandler.getServiceName(),
                                                            getTypeName());
        if (typeDef != null) {
            guid = typeDef.getGUID();
        }
        return guid;
    }

    /**
     * Map an omrs primitive property to a Subject Area relationship property.
     * The child class is expected to override this method if the type has primitive properties
     *
     * @param relationship the relationship to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if it was a property we were expecting , otherwise false;
     */
    protected boolean mapPrimitiveToRelationship(R relationship, String propertyName, Object value) {
        return false;
    }


    /**
     * Map an omrs enum property to a Subject Area relationship property.
     * The child class is expected to override this method if the type has enum properties
     *
     * @param relationship      the relationship to be updated
     * @param propertyName      the omrs property name
     * @param enumPropertyValue the omrs enum property value
     * @return true if it was a property we were expecting , otherwise false;
     */
    protected boolean mapEnumToRelationship(R relationship, String propertyName, EnumPropertyValue enumPropertyValue) {
        return false;
    }

    /**
     * Map an omrs  map property to a Subject Area relationship property.
     * The child class is expected to override this method if the type has map properties
     *
     * @param omasRelationship       the relationship to be updated
     * @param propertyName           the omrs property name
     * @param instancePropertyForMap the omrs map property value
     * @return true if it was a property we were expecting , otherwise false;
     */
    protected boolean mapMapToRelationship(R omasRelationship, String propertyName, InstanceProperties instancePropertyForMap) {
        return false;
    }

    /**
     * Map the supplied relationship to omrs InstanceProperties.
     * This method should be overridden to populate the instance properties
     *
     * @param omasRelationship   supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    protected void mapRelationshipToInstanceProperties(R omasRelationship, InstanceProperties instanceProperties) {

    }
}
