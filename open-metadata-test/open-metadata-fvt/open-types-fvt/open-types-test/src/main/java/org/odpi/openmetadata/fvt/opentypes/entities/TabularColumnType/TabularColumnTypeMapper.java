/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.TabularColumnType;

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
 * Static mapping methods to map between TabularColumnType and the omrs equivalents.
 */
public class TabularColumnTypeMapper {
    private static final Logger log = LoggerFactory.getLogger( TabularColumnTypeMapper.class);
    private static final String className = TabularColumnTypeMapper.class.getName();
   /**
    * @param omrsEntityDetail the supplied EntityDetail
    * @return equivalent TabularColumnType
    */
   static public TabularColumnType mapOmrsEntityDetailToTabularColumnType(EntityDetail omrsEntityDetail) throws InvalidParameterException {
        String entityTypeName = omrsEntityDetail.getType().getTypeDefName();
        if ("TabularColumnType".equals(entityTypeName)) {
                TabularColumnType tabularColumnType = new TabularColumnType();
                //set core attributes
                SystemAttributes systemAttributes = new SystemAttributes();
                systemAttributes.setStatus(omrsEntityDetail.getStatus());

                systemAttributes.setCreatedBy(omrsEntityDetail.getCreatedBy());
                systemAttributes.setUpdatedBy(omrsEntityDetail.getUpdatedBy());
                systemAttributes.setCreateTime(omrsEntityDetail.getCreateTime());
                systemAttributes.setUpdateTime(omrsEntityDetail.getUpdateTime());
                systemAttributes.setVersion(omrsEntityDetail.getVersion());
                systemAttributes.setGUID(omrsEntityDetail.getGUID());
                tabularColumnType.setSystemAttributes(systemAttributes);

                // Set properties
                InstanceProperties omrsEntityDetailProperties = omrsEntityDetail.getProperties();
                if (omrsEntityDetailProperties!=null) {
                  omrsEntityDetailProperties.setEffectiveFromTime(tabularColumnType.getEffectiveFromTime());
                  omrsEntityDetailProperties.setEffectiveToTime(tabularColumnType.getEffectiveToTime());
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
                            if (tabularColumnType.ATTRIBUTE_NAMES_SET.contains(name)) {
                               if (name.equals("dataType")) {
                                   tabularColumnType.setDataType((String)actualValue);
                               }
                               if (name.equals("defaultValue")) {
                                   tabularColumnType.setDefaultValue((String)actualValue);
                               }
                               if (name.equals("displayName")) {
                                   tabularColumnType.setDisplayName((String)actualValue);
                               }
                               if (name.equals("versionNumber")) {
                                   tabularColumnType.setVersionNumber((String)actualValue);
                               }
                               if (name.equals("author")) {
                                   tabularColumnType.setAuthor((String)actualValue);
                               }
                               if (name.equals("usage")) {
                                   tabularColumnType.setUsage((String)actualValue);
                               }
                               if (name.equals("encodingStandard")) {
                                   tabularColumnType.setEncodingStandard((String)actualValue);
                               }
                               if (name.equals("qualifiedName")) {
                                   tabularColumnType.setQualifiedName((String)actualValue);
                               }
                            } else {
                                // put out the omrs value object
                                if (null==tabularColumnType.getExtraAttributes())  {
                                     tabularColumnType.setExtraAttributes(new HashMap<String, Object>());
                                }
                               tabularColumnType.getExtraAttributes().put(name, primitivePropertyValue);
                            }
                            break;
                        case ENUM:
                            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                            String symbolicName = enumPropertyValue.getSymbolicName();
                            if (tabularColumnType.ENUM_NAMES_SET.contains(name)) {
                            } else {
                                // put out the omrs value object
                                if (null==tabularColumnType.getExtraAttributes())  {
                                     tabularColumnType.setExtraAttributes(new HashMap<String, Object>());
                                }
                                 tabularColumnType.getExtraAttributes().put(name, enumPropertyValue);
                             }

                            break;
                        case MAP:
                            if (tabularColumnType.MAP_NAMES_SET.contains(name)) {
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
                                       tabularColumnType.setAdditionalProperties(actualMap);
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
                            if ( tabularColumnType.classifications==null) {
                                 tabularColumnType.classifications = new ArrayList<>();
                            }
                            tabularColumnType.classifications.add(classificationBean);

                        } else {
                            if (null==tabularColumnType.getExtraClassificationBeans())  {
                                 tabularColumnType.setExtraClassificationBeans(new HashMap<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification>());
                            }
                            tabularColumnType.getExtraClassificationBeans().put(omrsClassificationName,omrsClassification);
                        }
                    }
                 }
                 return tabularColumnType;
            } else {
                // TODO wrong entity type for this guid
            }
            return null;
    }
    /**
     * Map (convert) the supplied TabularColumnType to an entityDetails.
     * @param  tabularColumnType  supplied TabularColumnType
     * @return  entityDetails equivalent to tabularColumnType
     */
    static public EntityDetail mapTabularColumnTypeToOmrsEntityDetail(TabularColumnType tabularColumnType) {
            EntityDetail omrsEntityDetail = new EntityDetail();
            SystemAttributes systemAttributes = tabularColumnType.getSystemAttributes();
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
            instanceProperties.setEffectiveFromTime(tabularColumnType.getEffectiveFromTime());
            instanceProperties.setEffectiveToTime(tabularColumnType.getEffectiveToTime());
            // primitives

            if (tabularColumnType.getDataType()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(tabularColumnType.getDataType());
                instanceProperties.setProperty("dataType", primitivePropertyValue);
            }
            if (tabularColumnType.getDefaultValue()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(tabularColumnType.getDefaultValue());
                instanceProperties.setProperty("defaultValue", primitivePropertyValue);
            }
            if (tabularColumnType.getDisplayName()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(tabularColumnType.getDisplayName());
                instanceProperties.setProperty("displayName", primitivePropertyValue);
            }
            if (tabularColumnType.getVersionNumber()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(tabularColumnType.getVersionNumber());
                instanceProperties.setProperty("versionNumber", primitivePropertyValue);
            }
            if (tabularColumnType.getAuthor()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(tabularColumnType.getAuthor());
                instanceProperties.setProperty("author", primitivePropertyValue);
            }
            if (tabularColumnType.getUsage()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(tabularColumnType.getUsage());
                instanceProperties.setProperty("usage", primitivePropertyValue);
            }
            if (tabularColumnType.getEncodingStandard()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(tabularColumnType.getEncodingStandard());
                instanceProperties.setProperty("encodingStandard", primitivePropertyValue);
            }
            if (tabularColumnType.getQualifiedName()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(tabularColumnType.getQualifiedName());
                instanceProperties.setProperty("qualifiedName", primitivePropertyValue);
            }
            if (tabularColumnType.getAdditionalProperties()!=null) {

                Map<String,String> map =tabularColumnType.getAdditionalProperties();
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
            TypeDef typeDef = archiveAccessor.getEntityDefByName("TabularColumnType");
            InstanceType template = archiveAccessor.createTemplateFromTypeDef(typeDef);
            InstanceType instanceType = new InstanceType(template);
            omrsEntityDetail.setType(instanceType);

            // map the classifications
            populateOmrsEntityWithClassificationBeans(omrsEntityDetail,(List<ClassificationBean>)tabularColumnType.getClassificationBeans());
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
