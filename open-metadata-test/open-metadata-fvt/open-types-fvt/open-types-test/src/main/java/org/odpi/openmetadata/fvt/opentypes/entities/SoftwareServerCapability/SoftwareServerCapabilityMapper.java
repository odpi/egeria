/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.SoftwareServerCapability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

// omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.archivemanager.OMRSArchiveAccessor;
import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.classifications.ClassificationBeanFactory;
import org.odpi.openmetadata.fvt.opentypes.enums.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
/**
 * Static mapping methods to map between SoftwareServerCapability and the omrs equivalents.
 */
public class SoftwareServerCapabilityMapper {
    private static final Logger log = LoggerFactory.getLogger( SoftwareServerCapabilityMapper.class);
    private static final String className = SoftwareServerCapabilityMapper.class.getName();
   /**
    * @param omrsEntityDetail the supplied EntityDetail
    * @return equivalent SoftwareServerCapability
    */
   static public SoftwareServerCapability mapOmrsEntityDetailToSoftwareServerCapability(EntityDetail omrsEntityDetail) throws InvalidParameterException {
        String entityTypeName = omrsEntityDetail.getType().getTypeDefName();
        if ("SoftwareServerCapability".equals(entityTypeName)) {
                SoftwareServerCapability softwareServerCapability = new SoftwareServerCapability();
                //set core attributes
                SystemAttributes systemAttributes = new SystemAttributes();
                systemAttributes.setStatus(omrsEntityDetail.getStatus());

                systemAttributes.setCreatedBy(omrsEntityDetail.getCreatedBy());
                systemAttributes.setUpdatedBy(omrsEntityDetail.getUpdatedBy());
                systemAttributes.setCreateTime(omrsEntityDetail.getCreateTime());
                systemAttributes.setUpdateTime(omrsEntityDetail.getUpdateTime());
                systemAttributes.setVersion(omrsEntityDetail.getVersion());
                systemAttributes.setGUID(omrsEntityDetail.getGUID());
                softwareServerCapability.setSystemAttributes(systemAttributes);

                // Set properties
                InstanceProperties omrsEntityDetailProperties = omrsEntityDetail.getProperties();
                if (omrsEntityDetailProperties!=null) {
                  omrsEntityDetailProperties.setEffectiveFromTime(softwareServerCapability.getEffectiveFromTime());
                  omrsEntityDetailProperties.setEffectiveToTime(softwareServerCapability.getEffectiveToTime());
                  Iterator omrsPropertyIterator = omrsEntityDetailProperties.getPropertyNames();

                  while (omrsPropertyIterator.hasNext()) {
                    String name = (String) omrsPropertyIterator.next();
                    //TODO check if this is a property we expect or whether the type has been added to.
                    // this is a property we expect
                    InstancePropertyValue value = omrsEntityDetailProperties.getPropertyValue(name);

                    // supplied guid matches the expected type

                    Object actualValue;
                    switch (value.getInstancePropertyCategory()) {
                        case PRIMITIVE:
                            PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) value;
                            actualValue = primitivePropertyValue.getPrimitiveValue();
                            if (softwareServerCapability.ATTRIBUTE_NAMES_SET.contains(name)) {
                               if (name.equals("name")) {
                                   softwareServerCapability.setName((String)actualValue);
                               }
                               if (name.equals("description")) {
                                   softwareServerCapability.setDescription((String)actualValue);
                               }
                               if (name.equals("type")) {
                                   softwareServerCapability.setType((String)actualValue);
                               }
                               if (name.equals("version")) {
                                   softwareServerCapability.setVersion((String)actualValue);
                               }
                               if (name.equals("patchLevel")) {
                                   softwareServerCapability.setPatchLevel((String)actualValue);
                               }
                               if (name.equals("source")) {
                                   softwareServerCapability.setSource((String)actualValue);
                               }
                               if (name.equals("qualifiedName")) {
                                   softwareServerCapability.setQualifiedName((String)actualValue);
                               }
                            } else {
                                // put out the omrs value object
                                if (null==softwareServerCapability.getExtraAttributes())  {
                                     softwareServerCapability.setExtraAttributes(new HashMap<String, Object>());
                                }
                               softwareServerCapability.getExtraAttributes().put(name, primitivePropertyValue);
                            }
                            break;
                        case ENUM:
                            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                            String symbolicName = enumPropertyValue.getSymbolicName();
                            if (softwareServerCapability.ENUM_NAMES_SET.contains(name)) {
                            } else {
                                // put out the omrs value object
                                if (null==softwareServerCapability.getExtraAttributes())  {
                                     softwareServerCapability.setExtraAttributes(new HashMap<String, Object>());
                                }
                                 softwareServerCapability.getExtraAttributes().put(name, enumPropertyValue);
                             }

                            break;
                        case MAP:
                            if (softwareServerCapability.MAP_NAMES_SET.contains(name)) {
                                 MapPropertyValue mapPropertyValue = (MapPropertyValue) value;
                                 InstanceProperties instancePropertyForMap = (InstanceProperties) mapPropertyValue.getMapValues();

                                 if (name.equals("additionalProperties")) {

                                       // Only support Map<String,String> as that is what is in the archive types at this time.
                                       Map<String, String> actualMap = new HashMap();
                                       Iterator iter = instancePropertyForMap.getPropertyNames();
                                       while (iter.hasNext()) {
                                           String mapkey = (String) iter.next();
                                           PrimitivePropertyValue primitivePropertyMapValue = (PrimitivePropertyValue) instancePropertyForMap.getPropertyValue(mapkey);
                                           String mapvalue = (String) primitivePropertyMapValue.getPrimitiveValue();
                                           actualMap.put(mapkey, mapvalue);
                                       }
                                       softwareServerCapability.setAdditionalProperties(actualMap);
                                 }
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
                 // set classifications
                 List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> omrsclassifications = omrsEntityDetail.getClassifications() ;
                 if (omrsclassifications != null && !omrsclassifications.isEmpty()){
                    for (org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification omrsClassification:omrsclassifications) {
                        String omrsClassificationName = omrsClassification.getName();
                        ClassificationBean classificationBean = ClassificationBeanFactory.getClassificationBean(omrsClassificationName,omrsClassification);
                        if (classificationBean !=null) {
                            // this is a classification we know about.
                            if ( softwareServerCapability.classifications==null) {
                                 softwareServerCapability.classifications = new ArrayList<>();
                            }
                            softwareServerCapability.classifications.add(classificationBean);

                        } else {
                            if (null==softwareServerCapability.getExtraClassificationBeans())  {
                                 softwareServerCapability.setExtraClassificationBeans(new HashMap<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification>());
                            }
                            softwareServerCapability.getExtraClassificationBeans().put(omrsClassificationName,omrsClassification);
                        }
                    }
                 }
                 return softwareServerCapability;
            } else {
                // TODO wrong entity type for this guid
            }
            return null;
    }
    /**
     * Map (convert) the supplied SoftwareServerCapability to an entityDetails.
     * @param  softwareServerCapability  supplied SoftwareServerCapability
     * @return  entityDetails equivalent to softwareServerCapability
     */
    static public EntityDetail mapSoftwareServerCapabilityToOmrsEntityDetail(SoftwareServerCapability softwareServerCapability) {
            EntityDetail omrsEntityDetail = new EntityDetail();
            SystemAttributes systemAttributes = softwareServerCapability.getSystemAttributes();
            if (systemAttributes!=null) {
                   if (systemAttributes.getCreatedBy()!=null)
                        omrsEntityDetail.setCreatedBy(systemAttributes.getCreatedBy());
                   if (systemAttributes.getUpdatedBy()!=null)
                        omrsEntityDetail.setUpdatedBy(systemAttributes.getUpdatedBy());
                   if (systemAttributes.getCreateTime()!=null)
                        omrsEntityDetail.setCreateTime(systemAttributes.getCreateTime());
                   if (systemAttributes.getUpdateTime()!=null)
                        omrsEntityDetail.setUpdateTime(systemAttributes.getUpdateTime());
                   if (systemAttributes.getVersion()!=null)
                        omrsEntityDetail.setVersion(systemAttributes.getVersion());
                   if (systemAttributes.getGUID()!=null)
                        omrsEntityDetail.setGUID(systemAttributes.getGUID());
                   if (systemAttributes.getStatus()!=null) {
                        omrsEntityDetail.setStatus(systemAttributes.getStatus());
                   }
            }

            InstanceProperties instanceProperties = new InstanceProperties();
            instanceProperties.setEffectiveFromTime(softwareServerCapability.getEffectiveFromTime());
            instanceProperties.setEffectiveToTime(softwareServerCapability.getEffectiveToTime());
            // primitives

            if (softwareServerCapability.getName()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(softwareServerCapability.getName());
                instanceProperties.setProperty("name", primitivePropertyValue);
            }
            if (softwareServerCapability.getDescription()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(softwareServerCapability.getDescription());
                instanceProperties.setProperty("description", primitivePropertyValue);
            }
            if (softwareServerCapability.getType()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(softwareServerCapability.getType());
                instanceProperties.setProperty("type", primitivePropertyValue);
            }
            if (softwareServerCapability.getVersion()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(softwareServerCapability.getVersion());
                instanceProperties.setProperty("version", primitivePropertyValue);
            }
            if (softwareServerCapability.getPatchLevel()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(softwareServerCapability.getPatchLevel());
                instanceProperties.setProperty("patchLevel", primitivePropertyValue);
            }
            if (softwareServerCapability.getSource()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(softwareServerCapability.getSource());
                instanceProperties.setProperty("source", primitivePropertyValue);
            }
            if (softwareServerCapability.getQualifiedName()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(softwareServerCapability.getQualifiedName());
                instanceProperties.setProperty("qualifiedName", primitivePropertyValue);
            }
            if (softwareServerCapability.getAdditionalProperties()!=null) {

                Map<String,String> map =softwareServerCapability.getAdditionalProperties();
                MapPropertyValue mapPropertyValue = new MapPropertyValue();

                for (String key:map.keySet()) {
                   PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                   primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                   primitivePropertyValue.setPrimitiveValue(map.get(key));
                   mapPropertyValue.setMapValue(key,primitivePropertyValue);
                }

                instanceProperties.setProperty("additionalProperties", mapPropertyValue);
            }
            omrsEntityDetail.setProperties(instanceProperties);
            // set the type in the entity
            OMRSArchiveAccessor archiveAccessor = OMRSArchiveAccessor.getInstance();
            TypeDef typeDef = archiveAccessor.getEntityDefByName("SoftwareServerCapability");
            InstanceType template = archiveAccessor.createTemplateFromTypeDef(typeDef);
            InstanceType instanceType = new InstanceType(template);
            omrsEntityDetail.setType(instanceType);

            // map the classifications
            populateOmrsEntityWithClassificationBeans(omrsEntityDetail,(List<ClassificationBean>)softwareServerCapability.getClassificationBeans());
            return omrsEntityDetail;
    }

    private static void populateOmrsEntityWithClassificationBeans(EntityDetail omrsEntityDetail, List<ClassificationBean> classificationBeans) {
        if (classificationBeans!= null && classificationBeans.size()>0) {
            ArrayList<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> omrsClassifications = new ArrayList<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification>();
            for (ClassificationBean classificationBean : classificationBeans) {
                SystemAttributes systemAttributes = classificationBean.getSystemAttributes();
                org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification omrsClassification = new org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification();

                if (systemAttributes != null) {
                    if (systemAttributes.getCreatedBy() != null)
                        omrsClassification.setCreatedBy(systemAttributes.getCreatedBy());
                    if (systemAttributes.getUpdatedBy() != null)
                        omrsClassification.setUpdatedBy(systemAttributes.getUpdatedBy());
                    if (systemAttributes.getCreateTime() != null)
                        omrsClassification.setCreateTime(systemAttributes.getCreateTime());
                    if (systemAttributes.getUpdateTime() != null)
                        omrsClassification.setUpdateTime(systemAttributes.getUpdateTime());
                    if (systemAttributes.getVersion() != null)
                        omrsClassification.setVersion(systemAttributes.getVersion());
                }
                // copy over the classification name
                omrsClassification.setName(classificationBean.getClassificationName());
                // copy over the classification properties
                omrsClassification.setProperties( classificationBean.obtainInstanceProperties());
                omrsClassifications.add(omrsClassification);
            }
            omrsEntityDetail.setClassifications(omrsClassifications);
        }
    }
}
