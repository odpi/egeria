/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Node;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.INodeMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.classifications.ClassificationFactory;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchiveAccessor;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.*;

/**
 * This class provides a mapping between OMRS object EntityDetail and Subject Area OMAS object Node.
 * Sub-classes of Node are expected to provide a mapper that extends this class, to provide the mappings for the properties that
 * they support.
 */
abstract public class EntityDetailMapper<N extends Node> implements INodeMapper<N> {
    protected final OMRSRepositoryHelper repositoryHelper;
    protected final OpenMetadataAPIGenericHandler genericHandler;
    public EntityDetailMapper(OpenMetadataAPIGenericHandler genericHandler){
        this.genericHandler = genericHandler;
        this.repositoryHelper = genericHandler.getRepositoryHelper();
    }

    /**
     * map the EntityDetail to the Node
     * @param node to be mapped to (the target of the map)
     * @param omrsEntityDetail entityDetail to be mapped from (the source of the mapping)
     */
    protected void mapEntityDetailToNode(N node, EntityDetail omrsEntityDetail)  {

        node.setSystemAttributes(SubjectAreaUtils.createSystemAttributes(omrsEntityDetail));
        // Set properties
        InstanceProperties omrsEntityDetailProperties = omrsEntityDetail.getProperties();
        if (omrsEntityDetailProperties!=null) {
            mapEntityDetailPropertiesToNode(node, omrsEntityDetailProperties);
        }
        // set classifications
        mapOmrsClassificationsToNode(omrsEntityDetail, node);
        // set readonly
        if (omrsEntityDetail.getInstanceProvenanceType() != InstanceProvenanceType.LOCAL_COHORT) {
            node.setReadOnly(true);
        }
    }

    /**
     * Map EntityDetail properties to Node properties. This method calls out to methods that are overridden for the different Nodes.
     * @param node supplied Node to be updated
     * @param instanceProperties entity detail properties
     */
    private void mapEntityDetailPropertiesToNode(N node, InstanceProperties instanceProperties) {
        // copy over effectivity
        Date effectivityFromtime = null;
        Date effectivityTotime = null;
        if (instanceProperties.getEffectiveFromTime() != null) {
            effectivityFromtime = instanceProperties.getEffectiveFromTime();
            node.setEffectiveFromTime(effectivityFromtime.getTime());
        }
        if (instanceProperties.getEffectiveToTime() !=null) {
            effectivityTotime = instanceProperties.getEffectiveToTime();
            node.setEffectiveToTime(effectivityTotime.getTime());
        }

//        instanceProperties.setQualifiedName(removePropertyByNameFromInstanceProperties(instanceProperties, ));
//        instanceProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
//        instanceProperties.setDisplayName(this.removeName(instanceProperties));
//        instanceProperties.setDescription(this.removeDescription(instanceProperties));
//        instanceProperties.setTypeDescription(this.removeDeployedImplementationType(instanceProperties));
//        instanceProperties.setVersion(this.removeCapabilityVersion(instanceProperties));
//        instanceProperties.setPatchLevel(this.removePatchLevel(instanceProperties));
//        instanceProperties.setSource(this.removeSource(instanceProperties));
//
//        /*
//         * Any remaining properties are returned in the extended properties.  They are
//         * assumed to be defined in a subtype.
//         */
//        instanceProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
//        instanceProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));
//
//        bean.setSoftwareServerCapabilitiesProperties(instanceProperties);


        // copy over properties
        Iterator<String> omrsPropertyIterator = instanceProperties.getPropertyNames();
        NodeType nodeType = node.getNodeType();
        while (omrsPropertyIterator.hasNext()) {
            String propertyName = omrsPropertyIterator.next();
            //TODO check if this is a property we expect or whether the type has been added to.
            // this is a property we expect
            InstancePropertyValue value = instanceProperties.getPropertyValue(propertyName);

            // supplied guid matches the expected type

            Object actualValue;
            switch (value.getInstancePropertyCategory()) {
                case PRIMITIVE:
                    PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) value;
                    actualValue = primitivePropertyValue.getPrimitiveValue();
                    // All nodes are Referenceables at this time so they all have qualifiedName
                    if (propertyName.equals(OpenMetadataProperty.QUALIFIED_NAME.name)) {
                        if (actualValue != null) {
                            node.setQualifiedName((String) actualValue);
                        }
                    } else if (propertyName.equals(OpenMetadataProperty.DISPLAY_NAME.name) || propertyName.equals(OpenMetadataProperty.NAME.name)) {
                        if (actualValue!=null) {
                            node.setName((String) actualValue);
                        }
                    } else if (propertyName.equals(OpenMetadataProperty.DESCRIPTION.name)) {
                        if (actualValue!=null) {
                            node.setDescription((String) actualValue);
                        }
                        // if the node is Taxonomy or TaxonomyAndCanonicalGlossary then it can have a scope attribute
                    } else if (propertyName.equals(OpenMetadataType.SCOPE_PROPERTY_NAME) && (nodeType == NodeType.Taxonomy || nodeType == NodeType.TaxonomyAndCanonicalGlossary)) {
                        if (actualValue!=null) {
                            node.setDescription((String) actualValue);
                        }
                        // if the node is CanonicalGlossary or TaxonomyAndCanonicalGlossary then it can have an organisingPrinciple attribute
                    } else if (propertyName.equals(OpenMetadataType.ORGANIZING_PRINCIPLE_PROPERTY_NAME) && (nodeType == NodeType.CanonicalGlossary || nodeType == NodeType.TaxonomyAndCanonicalGlossary)) {
                        if (actualValue!=null) {
                            node.setDescription((String) actualValue);
                        }
                    } else if (!mapPrimitiveToNode(node, propertyName, actualValue)) {
                        // there are properties we are not aware of, as they have been added by a subtype, put them in the extended properties
                        if (null==node.getExtendedProperties())  {
                            node.setExtendedProperties(new HashMap<String, Object>());
                        }
                        node.getExtendedProperties().put(propertyName, actualValue);
                    }
                    break;
                case ENUM:
                    EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                    if (!mapEnumToNode(node, propertyName, enumPropertyValue)) {
                        // there are properties we are not aware of, as they have been added by a subtype, put them in the extended properties
                        if (null==node.getExtendedProperties())  {
                            node.setExtendedProperties(new HashMap<String, Object>());
                        }
                        node.getExtendedProperties().put(propertyName,  enumPropertyValue);
                    }

                    break;
                case MAP:
                    MapPropertyValue mapPropertyValue = (MapPropertyValue) value;
                    InstanceProperties instancePropertyForMap = mapPropertyValue.getMapValues();
                    // All nodes as Referenceables at this time so they all have additionalProperties.
                    if (propertyName.equals(OpenMetadataProperty.ADDITIONAL_PROPERTIES.name)) {
                        // Only support Map<String,String> at this time.
                        Map<String, String> actualMap = new HashMap<>();
                        Iterator<String> iter = instancePropertyForMap.getPropertyNames();
                        while (iter.hasNext()) {
                            String mapkey = iter.next();
                            PrimitivePropertyValue primitivePropertyMapValue = (PrimitivePropertyValue) instancePropertyForMap.getPropertyValue(mapkey);
                            String mapvalue = primitivePropertyMapValue.getPrimitiveValue().toString();
                            actualMap.put(mapkey, mapvalue);
                        }
                        node.setAdditionalProperties(actualMap);
                    } else if (!mapMapToNode(node, propertyName, mapPropertyValue)) {
                        // there are properties we are not aware of, as they have been added by a subtype, put them in the extended properties
                        if (null==node.getExtendedProperties())  {
                            node.setExtendedProperties(new HashMap<>());
                        }
                        node.getExtendedProperties().put(propertyName, mapPropertyValue);
                    }
                    break;
                case ARRAY:
                    if (null==node.getExtendedProperties())  {
                        node.setExtendedProperties(new HashMap<>());
                    }
                    node.getExtendedProperties().put(propertyName, value);
                    break;
                case STRUCT:
                    if (null==node.getExtendedProperties())  {
                        node.setExtendedProperties(new HashMap<>());
                    }
                    node.getExtendedProperties().put(propertyName, value);
                case UNKNOWN:
                    // Error ?
                    if (null==node.getExtendedProperties())  {
                        node.setExtendedProperties(new HashMap<>());
                    }
                    node.getExtendedProperties().put(propertyName, value);
                    break;
            }

        }   // end while
    }
    /**
     * Extract and delete the qualifiedName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @param propertyName name of the property to remove from the instanceProperties
     * @return string name or null
     */
    protected String removePropertyByNameFromInstanceProperties(InstanceProperties  instanceProperties, String propertyName)
    {
        final String methodName = "removeQualifiedName";

        if (instanceProperties != null)
        {
            return repositoryHelper.removeStringProperty(genericHandler.getServiceName(),
                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                         instanceProperties,
                                                         methodName);
        }

        return null;
    }
    /**
     * Map an omrs entityDetail primitive property to a Subject Area Node property.
     * The child class is expected to override this method if the type has primitive properties
     * @param node the node to be updated
     * @param propertyName the omrs property name
     * @param value the omrs primitive property value
     * @return true if it was a property we were expecting , otherwise false;
     */
    protected boolean mapPrimitiveToNode(N node, String propertyName, Object value) {
        return false;
    }
    /**
     * Map an omrs entityDetail enum property to a Subject Area Node property.
     * The child class is expected to override this method if the type has enum properties
     * @param node the node to be updated
     * @param propertyName the omrs property name
     * @param enumPropertyValue the omrs enum property value
     * @return true if it was a property we were expecting , otherwise false;
     */
    protected boolean mapEnumToNode(N node, String propertyName, EnumPropertyValue enumPropertyValue) {
        return false;
    }
    /**
     * Map an omrs entityDetail map property to a Subject Area Node property.
     * The child class is expected to override this method if the type has map properties
     * @param node the node to be updated
     * @param propertyName the omrs property name
     * @param mapPropertyValue the omrs map property value
     * @return true if it was a property we were expecting , otherwise false;
     */
    protected boolean mapMapToNode(N node, String propertyName, MapPropertyValue mapPropertyValue) {
        return false;
    }

    /**
     * Map the effectivity dates from node to the InstanceProperties
     * @param node source of the effectivity
     * @param instanceProperties instance properties to update
     */
    private void mapNodeEffectivityToInstanceProperties(N node, InstanceProperties instanceProperties) {

        Long effectiveFromTime = node.getEffectiveFromTime();
        Long effectiveToTime = node.getEffectiveToTime();
        if (effectiveFromTime != null ) {
            instanceProperties.setEffectiveFromTime(new Date(effectiveFromTime));
        }
        if (effectiveToTime != null) {
            instanceProperties.setEffectiveToTime(new Date(effectiveToTime));
        }
    }
    private void mapOmrsClassificationsToNode(EntityDetail omrsEntityDetail, N node) {
        List<Classification> omrsclassifications = omrsEntityDetail.getClassifications();
        if (CollectionUtils.isNotEmpty(omrsclassifications)) {
            ClassificationFactory classficationFactory = new ClassificationFactory(genericHandler);
            List<org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification>
                    existingClassifications = node.getClassifications();
            if (existingClassifications == null) {
                existingClassifications = new ArrayList<>();
            }
            for (Classification omrsClassification : omrsclassifications) {
                String omrsClassificationName = omrsClassification.getName();
                org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification omasClassification = classficationFactory.getOMASClassification(omrsClassificationName, omrsClassification);
                if (omasClassification != null) {
                    // this is a classification we know about.
                    updateNodeWithClassification(node, omasClassification);
                    // need to add this classification to the classifications
                    existingClassifications.add(omasClassification);
                }
            }
            node.setClassifications(existingClassifications);
        }
    }

    /**
     * Update Node with classification
     * This method should be overridden by types of node if they want to use the Classification as an in lined property
     * @param node node to update
     * @param omasClassification classification
     * @return flag true if the classification has been dealt with
     */
    abstract boolean updateNodeWithClassification(N node, org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification omasClassification);

    /**
     * This method is supplied a list of OMAS classifications and a supplied entityDetail object. The OMAS Classifications are converted to OMRS classifications and then then
     * used to classify the EntityDetail.
     * @param entityDetail supplied entityDetail object
     * @param omasClassifications supplied OMAS Classifications
     */
    private void populateOmrsEntityWithBeanClassifications(EntityDetail entityDetail, List<org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification> omasClassifications) {
        if (omasClassifications!= null && omasClassifications.size()>0) {
            ArrayList<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> omrsClassifications = new ArrayList<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification>();
            for (org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification omasClassification : omasClassifications) {

                ClassificationFactory classificationFactory = new ClassificationFactory(genericHandler);
                org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification omrsClassification = classificationFactory.getOMRSClassification(omasClassification);
                //classificationFactory.getOMASClassification(omrsClassificationName,omrsClassification);


                omrsClassifications.add(omrsClassification);
            }
            entityDetail.setClassifications(omrsClassifications);
        }
    }

    /**
     * Map a Node (a Subject Area OMAS) concept to an EntityDetail (an OMRS concept)
     *
     * Note that this does not map the readonly flag to the provenance, the caller needs
     * to handle this if required. Readonly flag is only for update / delete and restore processing
     * can proceed, in these cases the omrs entity should be looked up first, so this mapping would
     * not be called.
     *
     * @param node supplied node, which is a Subject Area Concept
     * @return EntityDetail, which is an OMRS concept
     */
    public EntityDetail toEntityDetail(N node) {
        String methodName = "mapNodeToEntityDetail";

        EntityDetail omrsEntityDetail = new EntityDetail();
        SystemAttributes systemAttributes = node.getSystemAttributes();
        if (systemAttributes!=null) {
            if (systemAttributes.getCreatedBy()!=null)
                omrsEntityDetail.setCreatedBy(systemAttributes.getCreatedBy());
            if (systemAttributes.getUpdatedBy()!=null)
                omrsEntityDetail.setUpdatedBy(systemAttributes.getUpdatedBy());
            if (systemAttributes.getCreateTime()!=null)
                omrsEntityDetail.setCreateTime(new Date(systemAttributes.getCreateTime()));
            if (systemAttributes.getUpdateTime()!=null)
                omrsEntityDetail.setUpdateTime(new Date(systemAttributes.getUpdateTime()));
            if (systemAttributes.getVersion()!=null)
                omrsEntityDetail.setVersion(systemAttributes.getVersion());
            if (systemAttributes.getGUID()!=null)
                omrsEntityDetail.setGUID(systemAttributes.getGUID());
            if (systemAttributes.getStatus()!=null) {
                InstanceStatus instanceStatus = SubjectAreaUtils.convertStatusToInstanceStatus(systemAttributes.getStatus());
                omrsEntityDetail.setStatus(instanceStatus);
            }
        }

        InstanceProperties instanceProperties = new InstanceProperties();
        mapNodeEffectivityToInstanceProperties(node, instanceProperties);
        //  map the Referencable node properties to instanceproperties
        if (node.getQualifiedName()!=null) {
            repositoryHelper.addStringPropertyToInstance(genericHandler.getServiceName(), instanceProperties, OpenMetadataProperty.QUALIFIED_NAME.name, node.getQualifiedName(), methodName);
        }
        if (node.getName()!=null) {
            if (node.getNodeType() == NodeType.Project || node.getNodeType() == NodeType.GlossaryProject) {
                SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, node.getName(), OpenMetadataProperty.NAME.name);
                repositoryHelper.addStringPropertyToInstance(genericHandler.getServiceName(), instanceProperties, OpenMetadataProperty.NAME.name, node.getName(), methodName);
            } else {
                SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, node.getName(), OpenMetadataProperty.DISPLAY_NAME.name);
                repositoryHelper.addStringPropertyToInstance(genericHandler.getServiceName(), instanceProperties, OpenMetadataProperty.DISPLAY_NAME.name, node.getName(), methodName);
            }
        }

        if (node.getDescription()!=null) {  SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, node.getDescription(), OpenMetadataProperty.DESCRIPTION.name);
           repositoryHelper.addStringPropertyToInstance(genericHandler.getServiceName(), instanceProperties, OpenMetadataProperty.DESCRIPTION.name, node.getDescription(), methodName);
        }
        // if there are additionalProperties then we should honour them and send them through to omrs.
        if (node.getAdditionalProperties()!=null) {
            populateAdditionalProperties(node, instanceProperties);
        }
        // TODO assume we do not need tp populate extended properties here. Or do we need to maintain them in the Node

        //  map the other node properties to instanceproperties
        mapNodeToInstanceProperties(node, instanceProperties);

        omrsEntityDetail.setProperties(instanceProperties);
        // set the type in the entity
        OpenMetadataTypesArchiveAccessor archiveAccessor = OpenMetadataTypesArchiveAccessor.getInstance();

        String typeName = getTypeName();
        TypeDef typeDef = archiveAccessor.getEntityDefByName(typeName);
        InstanceType template = SubjectAreaUtils.createTemplateFromTypeDef(typeDef);
        InstanceType instanceType = new InstanceType(template);
        omrsEntityDetail.setType(instanceType);

        // map the classifications

        List<org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification> omasClassifications = node.getClassifications();

        List<org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification> inlinedOmasClassifications = getInlinedClassifications(node);
        omasClassifications.addAll(inlinedOmasClassifications);

        if (!omasClassifications.isEmpty() ) {
            populateOmrsEntityWithBeanClassifications(omrsEntityDetail, omasClassifications);
        }
        return omrsEntityDetail;
    }

    @Override
    public abstract String getTypeName();

    /**
     * get the EntityTypeDef Guid
     * This method should be overridden to provide the appropriate guid for the type.
     *
     * @return the guid of the entity typedef
     */
    @Override
    public String getTypeDefGuid() {
        TypeDef typeDef = repositoryHelper.getTypeDefByName(genericHandler.getServiceName(),
                                                             getTypeName());
        String guid = null;
        if (typeDef != null) {
            guid = typeDef.getGUID();
        }
        return guid;
    }

    protected void populateAdditionalProperties(N node, InstanceProperties instanceProperties) {
        Map<String,String> map =node.getAdditionalProperties();
        MapPropertyValue mapPropertyValue = new MapPropertyValue();

        for (String key:map.keySet()) {
            PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
            primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
            primitivePropertyValue.setPrimitiveValue(map.get(key));
            mapPropertyValue.setMapValue(key,primitivePropertyValue);
        }

        instanceProperties.setProperty(OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, mapPropertyValue);
    }

    /**
     * A Classification either exists in the classifications associated with a node or as an inlined attribute (these are properties / attributes of a node that correspond to OMRS Classifications).
     * This method should be overridden by a child class if it has inlined attributes that correspond to an OMRS classifications.
     * @param node supplied node
     * @return inlined classifications.
     */
    abstract  List<org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification> getInlinedClassifications(N node);

    /**
     * Map the supplied Node to omrs InstanceProperties.
     * Subclasses are expected to override this method to provide logic to map their Node properties to instanceProperties
     * @param node supplied node
     * @param instanceProperties equivalent instance properties to the Node
     */
    protected void mapNodeToInstanceProperties(N node, InstanceProperties instanceProperties) {

    }
}
