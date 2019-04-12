/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.DataProfileAnnotation;

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
 * Static mapping methods to map between DataProfileAnnotation and the omrs equivalents.
 */
public class DataProfileAnnotationMapper {
    private static final Logger log = LoggerFactory.getLogger( DataProfileAnnotationMapper.class);
    private static final String className = DataProfileAnnotationMapper.class.getName();
   /**
    * @param omrsEntityDetail the supplied EntityDetail
    * @return equivalent DataProfileAnnotation
    */
   static public DataProfileAnnotation mapOmrsEntityDetailToDataProfileAnnotation(EntityDetail omrsEntityDetail) throws InvalidParameterException {
        String entityTypeName = omrsEntityDetail.getType().getTypeDefName();
        if ("DataProfileAnnotation".equals(entityTypeName)) {
                DataProfileAnnotation dataProfileAnnotation = new DataProfileAnnotation();
                //set core attributes
                SystemAttributes systemAttributes = new SystemAttributes();
                systemAttributes.setStatus(omrsEntityDetail.getStatus());

                systemAttributes.setCreatedBy(omrsEntityDetail.getCreatedBy());
                systemAttributes.setUpdatedBy(omrsEntityDetail.getUpdatedBy());
                systemAttributes.setCreateTime(omrsEntityDetail.getCreateTime());
                systemAttributes.setUpdateTime(omrsEntityDetail.getUpdateTime());
                systemAttributes.setVersion(omrsEntityDetail.getVersion());
                systemAttributes.setGUID(omrsEntityDetail.getGUID());
                dataProfileAnnotation.setSystemAttributes(systemAttributes);

                // Set properties
                InstanceProperties omrsEntityDetailProperties = omrsEntityDetail.getProperties();
                if (omrsEntityDetailProperties!=null) {
                  omrsEntityDetailProperties.setEffectiveFromTime(dataProfileAnnotation.getEffectiveFromTime());
                  omrsEntityDetailProperties.setEffectiveToTime(dataProfileAnnotation.getEffectiveToTime());
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
                            if (dataProfileAnnotation.ATTRIBUTE_NAMES_SET.contains(name)) {
                               if (name.equals("length")) {
                                   dataProfileAnnotation.setLength((Integer)actualValue);
                               }
                               if (name.equals("inferredDataType")) {
                                   dataProfileAnnotation.setInferredDataType((String)actualValue);
                               }
                               if (name.equals("inferredFormat")) {
                                   dataProfileAnnotation.setInferredFormat((String)actualValue);
                               }
                               if (name.equals("inferredLength")) {
                                   dataProfileAnnotation.setInferredLength((Integer)actualValue);
                               }
                               if (name.equals("inferredPrecision")) {
                                   dataProfileAnnotation.setInferredPrecision((Integer)actualValue);
                               }
                               if (name.equals("inferredScale")) {
                                   dataProfileAnnotation.setInferredScale((Integer)actualValue);
                               }
                               if (name.equals("valueRangeFrom")) {
                                   dataProfileAnnotation.setValueRangeFrom((String)actualValue);
                               }
                               if (name.equals("valueRangeTo")) {
                                   dataProfileAnnotation.setValueRangeTo((String)actualValue);
                               }
                               if (name.equals("averageValue")) {
                                   dataProfileAnnotation.setAverageValue((String)actualValue);
                               }
                               if (name.equals("annotationType")) {
                                   dataProfileAnnotation.setAnnotationType((String)actualValue);
                               }
                               if (name.equals("summary")) {
                                   dataProfileAnnotation.setSummary((String)actualValue);
                               }
                               if (name.equals("confidenceLevel")) {
                                   dataProfileAnnotation.setConfidenceLevel((Integer)actualValue);
                               }
                               if (name.equals("expression")) {
                                   dataProfileAnnotation.setExpression((String)actualValue);
                               }
                               if (name.equals("explanation")) {
                                   dataProfileAnnotation.setExplanation((String)actualValue);
                               }
                               if (name.equals("analysisStep")) {
                                   dataProfileAnnotation.setAnalysisStep((String)actualValue);
                               }
                               if (name.equals("jsonProperties")) {
                                   dataProfileAnnotation.setJsonProperties((String)actualValue);
                               }
                            } else {
                                // put out the omrs value object
                                if (null==dataProfileAnnotation.getExtraAttributes())  {
                                     dataProfileAnnotation.setExtraAttributes(new HashMap<String, Object>());
                                }
                               dataProfileAnnotation.getExtraAttributes().put(name, primitivePropertyValue);
                            }
                            break;
                        case ENUM:
                            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                            String symbolicName = enumPropertyValue.getSymbolicName();
                            if (dataProfileAnnotation.ENUM_NAMES_SET.contains(name)) {
                                 if (name.equals("annotationStatus")) {
                                      AnnotationStatus annotationStatus = AnnotationStatus.valueOf(symbolicName);
                                      dataProfileAnnotation.setAnnotationStatus(annotationStatus);
                                 }
                            } else {
                                // put out the omrs value object
                                if (null==dataProfileAnnotation.getExtraAttributes())  {
                                     dataProfileAnnotation.setExtraAttributes(new HashMap<String, Object>());
                                }
                                 dataProfileAnnotation.getExtraAttributes().put(name, enumPropertyValue);
                             }

                            break;
                        case MAP:
                            if (dataProfileAnnotation.MAP_NAMES_SET.contains(name)) {
                                 MapPropertyValue mapPropertyValue = (MapPropertyValue) value;
                                 InstanceProperties instancePropertyForMap = (InstanceProperties) mapPropertyValue.getMapValues();

                                 if (name.equals("profileProperties")) {

                                       // Only support Map<String,String> as that is what is in the archive types at this time.
                                       Map<String, String> actualMap = new HashMap();
                                       Iterator iter = instancePropertyForMap.getPropertyNames();
                                       while (iter.hasNext()) {
                                           String mapkey = (String) iter.next();
                                           PrimitivePropertyValue primitivePropertyMapValue = (PrimitivePropertyValue) instancePropertyForMap.getPropertyValue(mapkey);
                                           String mapvalue = (String) primitivePropertyMapValue.getPrimitiveValue();
                                           actualMap.put(mapkey, mapvalue);
                                       }
                                       dataProfileAnnotation.setProfileProperties(actualMap);
                                 }
                                 if (name.equals("profileFlags")) {

                                       // Only support Map<String,String> as that is what is in the archive types at this time.
                                       Map<String, String> actualMap = new HashMap();
                                       Iterator iter = instancePropertyForMap.getPropertyNames();
                                       while (iter.hasNext()) {
                                           String mapkey = (String) iter.next();
                                           PrimitivePropertyValue primitivePropertyMapValue = (PrimitivePropertyValue) instancePropertyForMap.getPropertyValue(mapkey);
                                           String mapvalue = (String) primitivePropertyMapValue.getPrimitiveValue();
                                           actualMap.put(mapkey, mapvalue);
                                       }
                                       dataProfileAnnotation.setProfileFlags(actualMap);
                                 }
                                 if (name.equals("profileCounts")) {

                                       // Only support Map<String,String> as that is what is in the archive types at this time.
                                       Map<String, String> actualMap = new HashMap();
                                       Iterator iter = instancePropertyForMap.getPropertyNames();
                                       while (iter.hasNext()) {
                                           String mapkey = (String) iter.next();
                                           PrimitivePropertyValue primitivePropertyMapValue = (PrimitivePropertyValue) instancePropertyForMap.getPropertyValue(mapkey);
                                           String mapvalue = (String) primitivePropertyMapValue.getPrimitiveValue();
                                           actualMap.put(mapkey, mapvalue);
                                       }
                                       dataProfileAnnotation.setProfileCounts(actualMap);
                                 }
                                 if (name.equals("valueCount")) {

                                       // Only support Map<String,String> as that is what is in the archive types at this time.
                                       Map<String, String> actualMap = new HashMap();
                                       Iterator iter = instancePropertyForMap.getPropertyNames();
                                       while (iter.hasNext()) {
                                           String mapkey = (String) iter.next();
                                           PrimitivePropertyValue primitivePropertyMapValue = (PrimitivePropertyValue) instancePropertyForMap.getPropertyValue(mapkey);
                                           String mapvalue = (String) primitivePropertyMapValue.getPrimitiveValue();
                                           actualMap.put(mapkey, mapvalue);
                                       }
                                       dataProfileAnnotation.setValueCount(actualMap);
                                 }
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
                                       dataProfileAnnotation.setAdditionalProperties(actualMap);
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
                            if ( dataProfileAnnotation.classifications==null) {
                                 dataProfileAnnotation.classifications = new ArrayList<>();
                            }
                            dataProfileAnnotation.classifications.add(classificationBean);

                        } else {
                            if (null==dataProfileAnnotation.getExtraClassificationBeans())  {
                                 dataProfileAnnotation.setExtraClassificationBeans(new HashMap<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification>());
                            }
                            dataProfileAnnotation.getExtraClassificationBeans().put(omrsClassificationName,omrsClassification);
                        }
                    }
                 }
                 return dataProfileAnnotation;
            } else {
                // TODO wrong entity type for this guid
            }
            return null;
    }
    /**
     * Map (convert) the supplied DataProfileAnnotation to an entityDetails.
     * @param  dataProfileAnnotation  supplied DataProfileAnnotation
     * @return  entityDetails equivalent to dataProfileAnnotation
     */
    static public EntityDetail mapDataProfileAnnotationToOmrsEntityDetail(DataProfileAnnotation dataProfileAnnotation) {
            EntityDetail omrsEntityDetail = new EntityDetail();
            SystemAttributes systemAttributes = dataProfileAnnotation.getSystemAttributes();
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
            instanceProperties.setEffectiveFromTime(dataProfileAnnotation.getEffectiveFromTime());
            instanceProperties.setEffectiveToTime(dataProfileAnnotation.getEffectiveToTime());
            // primitives

            if (dataProfileAnnotation.getLength()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
                primitivePropertyValue.setPrimitiveValue(dataProfileAnnotation.getLength());
                instanceProperties.setProperty("length", primitivePropertyValue);
            }
            if (dataProfileAnnotation.getInferredDataType()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataProfileAnnotation.getInferredDataType());
                instanceProperties.setProperty("inferredDataType", primitivePropertyValue);
            }
            if (dataProfileAnnotation.getInferredFormat()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataProfileAnnotation.getInferredFormat());
                instanceProperties.setProperty("inferredFormat", primitivePropertyValue);
            }
            if (dataProfileAnnotation.getInferredLength()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
                primitivePropertyValue.setPrimitiveValue(dataProfileAnnotation.getInferredLength());
                instanceProperties.setProperty("inferredLength", primitivePropertyValue);
            }
            if (dataProfileAnnotation.getInferredPrecision()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
                primitivePropertyValue.setPrimitiveValue(dataProfileAnnotation.getInferredPrecision());
                instanceProperties.setProperty("inferredPrecision", primitivePropertyValue);
            }
            if (dataProfileAnnotation.getInferredScale()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
                primitivePropertyValue.setPrimitiveValue(dataProfileAnnotation.getInferredScale());
                instanceProperties.setProperty("inferredScale", primitivePropertyValue);
            }
            if (dataProfileAnnotation.getValueRangeFrom()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataProfileAnnotation.getValueRangeFrom());
                instanceProperties.setProperty("valueRangeFrom", primitivePropertyValue);
            }
            if (dataProfileAnnotation.getValueRangeTo()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataProfileAnnotation.getValueRangeTo());
                instanceProperties.setProperty("valueRangeTo", primitivePropertyValue);
            }
            if (dataProfileAnnotation.getAverageValue()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataProfileAnnotation.getAverageValue());
                instanceProperties.setProperty("averageValue", primitivePropertyValue);
            }
            if (dataProfileAnnotation.getAnnotationType()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataProfileAnnotation.getAnnotationType());
                instanceProperties.setProperty("annotationType", primitivePropertyValue);
            }
            if (dataProfileAnnotation.getSummary()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataProfileAnnotation.getSummary());
                instanceProperties.setProperty("summary", primitivePropertyValue);
            }
            if (dataProfileAnnotation.getConfidenceLevel()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
                primitivePropertyValue.setPrimitiveValue(dataProfileAnnotation.getConfidenceLevel());
                instanceProperties.setProperty("confidenceLevel", primitivePropertyValue);
            }
            if (dataProfileAnnotation.getExpression()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataProfileAnnotation.getExpression());
                instanceProperties.setProperty("expression", primitivePropertyValue);
            }
            if (dataProfileAnnotation.getExplanation()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataProfileAnnotation.getExplanation());
                instanceProperties.setProperty("explanation", primitivePropertyValue);
            }
            if (dataProfileAnnotation.getAnalysisStep()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataProfileAnnotation.getAnalysisStep());
                instanceProperties.setProperty("analysisStep", primitivePropertyValue);
            }
            if (dataProfileAnnotation.getJsonProperties()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataProfileAnnotation.getJsonProperties());
                instanceProperties.setProperty("jsonProperties", primitivePropertyValue);
            }
            if (dataProfileAnnotation.getAnnotationStatus()!=null) {
                AnnotationStatus enumType = dataProfileAnnotation.getAnnotationStatus();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("annotationStatus", enumPropertyValue);
            }
            if (dataProfileAnnotation.getProfileProperties()!=null) {

                Map<String,String> map =dataProfileAnnotation.getProfileProperties();
                MapPropertyValue mapPropertyValue = new MapPropertyValue();

                for (String key:map.keySet()) {
                   PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                   primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                   primitivePropertyValue.setPrimitiveValue(map.get(key));
                   mapPropertyValue.setMapValue(key,primitivePropertyValue);
                }

                instanceProperties.setProperty("profileProperties", mapPropertyValue);
            }
            if (dataProfileAnnotation.getProfileFlags()!=null) {

                Map<String,String> map =dataProfileAnnotation.getProfileFlags();
                MapPropertyValue mapPropertyValue = new MapPropertyValue();

                for (String key:map.keySet()) {
                   PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                   primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                   primitivePropertyValue.setPrimitiveValue(map.get(key));
                   mapPropertyValue.setMapValue(key,primitivePropertyValue);
                }

                instanceProperties.setProperty("profileFlags", mapPropertyValue);
            }
            if (dataProfileAnnotation.getProfileCounts()!=null) {

                Map<String,String> map =dataProfileAnnotation.getProfileCounts();
                MapPropertyValue mapPropertyValue = new MapPropertyValue();

                for (String key:map.keySet()) {
                   PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                   primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                   primitivePropertyValue.setPrimitiveValue(map.get(key));
                   mapPropertyValue.setMapValue(key,primitivePropertyValue);
                }

                instanceProperties.setProperty("profileCounts", mapPropertyValue);
            }
            if (dataProfileAnnotation.getValueCount()!=null) {

                Map<String,String> map =dataProfileAnnotation.getValueCount();
                MapPropertyValue mapPropertyValue = new MapPropertyValue();

                for (String key:map.keySet()) {
                   PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                   primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                   primitivePropertyValue.setPrimitiveValue(map.get(key));
                   mapPropertyValue.setMapValue(key,primitivePropertyValue);
                }

                instanceProperties.setProperty("valueCount", mapPropertyValue);
            }
            if (dataProfileAnnotation.getAdditionalProperties()!=null) {

                Map<String,String> map =dataProfileAnnotation.getAdditionalProperties();
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
            TypeDef typeDef = archiveAccessor.getEntityDefByName("DataProfileAnnotation");
            InstanceType template = archiveAccessor.createTemplateFromTypeDef(typeDef);
            InstanceType instanceType = new InstanceType(template);
            omrsEntityDetail.setType(instanceType);

            // map the classifications
            populateOmrsEntityWithClassificationBeans(omrsEntityDetail,(List<ClassificationBean>)dataProfileAnnotation.getClassificationBeans());
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
