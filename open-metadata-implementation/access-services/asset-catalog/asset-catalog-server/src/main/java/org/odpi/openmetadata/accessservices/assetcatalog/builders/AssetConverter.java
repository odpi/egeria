/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.builders;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetElement;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetElements;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Classification;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Element;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Type;
import org.odpi.openmetadata.accessservices.assetcatalog.util.Constants;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ArrayPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.ADDITIONAL_PROPERTIES_PROPERTY_NAME;

/**
 * AssetConverter is a helper class that maps the OMRS objects to Asset Catalog model.
 */
public class AssetConverter {

    private String sourceName;
    private OMRSRepositoryHelper repositoryHelper;

    public AssetConverter(String sourceName, OMRSRepositoryHelper repositoryHelper) {
        this.sourceName = sourceName;
        this.repositoryHelper = repositoryHelper;
    }

    /**
     * Method used to convert the Entity Details to Asset Catalog OMAS Model - Asset Description object
     *
     * @param entityDetail entityDetails object
     * @return Asset Description object
     */
    public AssetDescription getAssetDescription(EntityDetail entityDetail) {
        AssetDescription assetDescription = new AssetDescription();
        assetDescription.setGuid(entityDetail.getGUID());

        assetDescription.setCreatedBy(entityDetail.getCreatedBy());
        assetDescription.setCreateTime(entityDetail.getCreateTime());
        assetDescription.setUpdatedBy(entityDetail.getUpdatedBy());
        assetDescription.setUpdateTime(entityDetail.getUpdateTime());
        assetDescription.setVersion(entityDetail.getVersion());

        if (entityDetail.getType() != null && entityDetail.getType().getTypeDefName() != null) {
            assetDescription.setType(convertInstanceType(entityDetail.getType()));
        }

        assetDescription.setUrl(entityDetail.getInstanceURL());
        if (entityDetail.getStatus() != null && entityDetail.getStatus().getName() != null) {
            assetDescription.setStatus(entityDetail.getStatus().getName());
        }

        assetDescription.setProperties(extractProperties(entityDetail.getProperties()));
        assetDescription.setAdditionalProperties(extractAdditionalProperties(entityDetail.getProperties()));

        if (CollectionUtils.isNotEmpty(entityDetail.getClassifications())) {
            assetDescription.setClassifications(convertClassifications(entityDetail.getClassifications()));
        }

        return assetDescription;
    }

    /**
     * Method used to covert a list of relationships from OMRS model to AC OMAS model
     *
     * @param relationships list of relationships - OMRS model
     * @return a list of AC OMAS relationships objects
     */
    public List<Relationship> convertRelationships(List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship> relationships) {
        if (relationships == null) {
            return Collections.emptyList();
        }

        return relationships.stream().map(this::convertRelationship).collect(Collectors.toList());
    }

    /**
     * Method used to convert the relationship from the OMRS model to AC OMAS model
     *
     * @param rel relationship in the OMRS model
     * @return a relationship in AC OMAS model
     */
    public Relationship convertRelationship(org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship rel) {
        Relationship relationship = new Relationship();

        relationship.setGuid(rel.getGUID());
        relationship.setCreatedBy(rel.getCreatedBy());
        relationship.setCreateTime(rel.getCreateTime());

        relationship.setUpdatedBy(rel.getUpdatedBy());
        relationship.setUpdateTime(rel.getUpdateTime());

        relationship.setVersion(rel.getVersion());
        if (rel.getStatus() != null && rel.getStatus().getName() != null) {
            relationship.setStatus(rel.getStatus().getName());
        }

        if (rel.getType() != null && rel.getType().getTypeDefName() != null) {
            relationship.setType(convertInstanceType(rel.getType()));
        }

        if (rel.getEntityOneProxy() != null) {
            relationship.setFromEntity(getElement(rel.getEntityOneProxy()));
        }
        if (rel.getEntityTwoProxy() != null) {
            relationship.setToEntity(getElement(rel.getEntityTwoProxy()));
        }

        return relationship;
    }

    /**
     * Convert a list of Classifications from the OMRS model to Asset Catalog OMAS model
     *
     * @param classificationsFromEntity - list of classification in the repository services model
     * @return list of classifications in the AC OMAS model
     */
    public List<Classification> convertClassifications
    (List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> classificationsFromEntity) {

        if (classificationsFromEntity == null || classificationsFromEntity.isEmpty()) {
            return new ArrayList<>();
        }

        List<Classification> classifications = new ArrayList<>(classificationsFromEntity.size());
        for (org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification classificationEntity : classificationsFromEntity) {
            Classification classification = new Classification();
            classification.setName(classificationEntity.getName());
            if (classificationEntity.getClassificationOrigin() != null && classificationEntity.getClassificationOrigin().getDescription() != null) {
                classification.setOrigin(classificationEntity.getClassificationOrigin().getDescription());
            }
            classification.setOriginGUID(classificationEntity.getClassificationOriginGUID());

            classification.setCreatedBy(classificationEntity.getCreatedBy());
            classification.setCreateTime(classificationEntity.getCreateTime());

            classification.setUpdatedBy(classificationEntity.getUpdatedBy());
            classification.setUpdateTime(classificationEntity.getUpdateTime());

            classification.setVersion(classificationEntity.getVersion());
            if (classificationEntity.getStatus() != null) {
                classification.setStatus(classificationEntity.getStatus().getName());
            }
            if (classificationEntity.getType() != null) {
                classification.setType(convertInstanceType(classificationEntity.getType()));
            }

            classification.setProperties(extractProperties(classificationEntity.getProperties()));

            classifications.add(classification);
        }

        return classifications;
    }

    /**
     * Add an element in the context. If the context is null, the current element becomes the root of the context
     *
     * @param assetElement the context
     * @param entityDetail entity details of the new element
     */
    public void addElement(AssetElement assetElement, EntityDetail entityDetail) {
        List<Element> context = assetElement.getContext();
        AssetElements element = buildAssetElements(entityDetail);

        if (context != null) {
            Element leaf = lastElementAdded(context.get(context.size() - 1));
            leaf.setParentElement(element);
        } else {
            List<Element> elements = new ArrayList<>();
            elements.add(element);
            assetElement.setContext(elements);
        }
    }

    /**
     * Returns the last node added in the context
     *
     * @param assetElement given context
     * @return the last element
     */
    public Element getLastNode(AssetElement assetElement) {
        List<Element> context = assetElement.getContext();

        return CollectionUtils.isNotEmpty(context) ? lastElementAdded(context.get(context.size() - 1)) : null;
    }

    /**
     * Method use to add the parent element to the current element in the built context tree
     *
     * @param parentElement parent element from the context
     * @param element       a child element
     */
    public void addChildElement(Element parentElement, Element element) {
        if (parentElement != null) {
            parentElement.setParentElement(element);
        }
    }

    /**
     * Method use to add to the context of the given entity
     *
     * @param assetElement asset element that contains the current context
     * @param entityDetail entity details
     */
    public void addContextElement(AssetElement assetElement, EntityDetail entityDetail) {
        List<Element> context = assetElement.getContext();
        if (context == null) {
            context = new ArrayList<>();
        }
        context.add(buildAssetElements(entityDetail));
        assetElement.setContext(context);
    }

    /**
     * Method used to convert TypeDef to Type object from the Asset Catalog OMAS
     *
     * @param openType type definition
     * @return the Type object
     */
    public Type convertType(TypeDef openType) {
        Type type = new Type();
        type.setName(openType.getName());
        type.setDescription(openType.getDescription());
        type.setVersion(openType.getVersion());
        type.setSuperType(openType.getSuperType().getName());
        return type;
    }

    /**
     * Create an Asset Element from the Entity Details
     *
     * @param entityDetail entityDetails
     * @return an AssetElement object
     */
    public AssetElements buildAssetElements(EntityDetail entityDetail) {
        if (entityDetail == null) {
            return null;
        }

        AssetElements element = new AssetElements();
        element.setGuid(entityDetail.getGUID());
        element.setType(convertInstanceType(entityDetail.getType()));
        element.setProperties(extractProperties(entityDetail.getProperties()));
        element.setAdditionalProperties(extractAdditionalProperties(entityDetail.getProperties()));
        if (CollectionUtils.isNotEmpty(entityDetail.getClassifications())) {
            element.setClassifications(convertClassifications(entityDetail.getClassifications()));
        }

        return element;
    }

    private Type convertInstanceType(InstanceType instanceType) {
        Type type = new Type();
        type.setName(instanceType.getTypeDefName());
        type.setDescription(instanceType.getTypeDefDescription());
        type.setVersion(instanceType.getTypeDefVersion());
        return type;
    }

    private Element getElement(EntityProxy entityProxy) {
        String method = "getAsset";
        Element asset = new Element();

        asset.setGuid(entityProxy.getGUID());
        if (entityProxy.getUniqueProperties() != null) {
            asset.setName(repositoryHelper.getStringProperty(sourceName, Constants.NAME, entityProxy.getUniqueProperties(), method));
        }
        asset.setCreatedBy(entityProxy.getCreatedBy());
        asset.setCreateTime(entityProxy.getCreateTime());
        asset.setUpdatedBy(entityProxy.getUpdatedBy());
        asset.setUpdateTime(entityProxy.getUpdateTime());
        asset.setStatus(entityProxy.getStatus().getName());
        asset.setVersion(entityProxy.getVersion());
        asset.setType(convertInstanceType(entityProxy.getType()));
        if (CollectionUtils.isNotEmpty(entityProxy.getClassifications())) {
            asset.setClassifications(convertClassifications(entityProxy.getClassifications()));
        }

        return asset;
    }

    private Element lastElementAdded(Element tree) {
        Element innerElement = tree.getParentElement();
        if (innerElement == null) {
            return tree;
        }
        return lastElementAdded(innerElement);
    }

    private Map<String, String> extractProperties(InstanceProperties instanceProperties) {
        Map<String, Object> instancePropertiesAsMap = repositoryHelper.getInstancePropertiesAsMap(instanceProperties);
        Map<String, String> properties = new HashMap<>();
        String methodName = "extractProperties";

        if (MapUtils.isNotEmpty(instancePropertiesAsMap)) {
            instancePropertiesAsMap.forEach((key, value) -> {
                if (!key.equals(ADDITIONAL_PROPERTIES_PROPERTY_NAME)) {
                    if (value instanceof ArrayPropertyValue) {
                        List<String> stringArrayProperty = repositoryHelper.getStringArrayProperty(sourceName, key, instanceProperties, methodName);
                        properties.put(key, listToString(stringArrayProperty));
                    } else if (value instanceof MapPropertyValue) {
                        Map<String, Object> mapProperty = repositoryHelper.getMapFromProperty(sourceName, key, instanceProperties, methodName);
                        properties.put(key, mapToString(mapProperty));
                    } else {
                        properties.put(key, String.valueOf(value));
                    }
                }
            });
        }

        return properties;
    }

    private Map<String, String> extractAdditionalProperties(InstanceProperties instanceProperties) {
        String methodName = "extractAdditionalProperties";

        return MapUtils.emptyIfNull(repositoryHelper.removeStringMapFromProperty(sourceName,
                ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                instanceProperties, methodName));
    }

    private String listToString(List<String> list) {
        return String.join(",", list);
    }

    private String mapToString(Map<String, Object> map) {
        return map.keySet().stream().map(key -> key + ": " + map.get(key))
                .collect(Collectors.joining(", "));
    }
}
