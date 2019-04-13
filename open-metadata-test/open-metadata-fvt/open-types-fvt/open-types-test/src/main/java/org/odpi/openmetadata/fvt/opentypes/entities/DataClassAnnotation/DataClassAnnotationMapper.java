/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.DataClassAnnotation;

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
 * Static mapping methods to map between DataClassAnnotation and the omrs equivalents.
 */
public class DataClassAnnotationMapper {
    private static final Logger log = LoggerFactory.getLogger( DataClassAnnotationMapper.class);
    private static final String className = DataClassAnnotationMapper.class.getName();
   /**
    * @param omrsEntityDetail the supplied EntityDetail
    * @return equivalent DataClassAnnotation
    */
   static public DataClassAnnotation mapOmrsEntityDetailToDataClassAnnotation(EntityDetail omrsEntityDetail) throws InvalidParameterException {
        String entityTypeName = omrsEntityDetail.getType().getTypeDefName();
        if ("DataClassAnnotation".equals(entityTypeName)) {
                DataClassAnnotation dataClassAnnotation = new DataClassAnnotation();
                //set core attributes
                SystemAttributes systemAttributes = new SystemAttributes();
                systemAttributes.setStatus(omrsEntityDetail.getStatus());

                systemAttributes.setCreatedBy(omrsEntityDetail.getCreatedBy());
                systemAttributes.setUpdatedBy(omrsEntityDetail.getUpdatedBy());
                systemAttributes.setCreateTime(omrsEntityDetail.getCreateTime());
                systemAttributes.setUpdateTime(omrsEntityDetail.getUpdateTime());
                systemAttributes.setVersion(omrsEntityDetail.getVersion());
                systemAttributes.setGUID(omrsEntityDetail.getGUID());
                dataClassAnnotation.setSystemAttributes(systemAttributes);

                // Set properties
                InstanceProperties omrsEntityDetailProperties = omrsEntityDetail.getProperties();
                if (omrsEntityDetailProperties!=null) {
                  omrsEntityDetailProperties.setEffectiveFromTime(dataClassAnnotation.getEffectiveFromTime());
                  omrsEntityDetailProperties.setEffectiveToTime(dataClassAnnotation.getEffectiveToTime());
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
                            if (dataClassAnnotation.ATTRIBUTE_NAMES_SET.contains(name)) {
                               if (name.equals("matchingValues")) {
                                   dataClassAnnotation.setMatchingValues((Long)actualValue);
                               }
                               if (name.equals("nonMatchingValues")) {
                                   dataClassAnnotation.setNonMatchingValues((Long)actualValue);
                               }
                               if (name.equals("annotationType")) {
                                   dataClassAnnotation.setAnnotationType((String)actualValue);
                               }
                               if (name.equals("summary")) {
                                   dataClassAnnotation.setSummary((String)actualValue);
                               }
                               if (name.equals("confidenceLevel")) {
                                   dataClassAnnotation.setConfidenceLevel((Integer)actualValue);
                               }
                               if (name.equals("expression")) {
                                   dataClassAnnotation.setExpression((String)actualValue);
                               }
                               if (name.equals("explanation")) {
                                   dataClassAnnotation.setExplanation((String)actualValue);
                               }
                               if (name.equals("analysisStep")) {
                                   dataClassAnnotation.setAnalysisStep((String)actualValue);
                               }
                               if (name.equals("jsonProperties")) {
                                   dataClassAnnotation.setJsonProperties((String)actualValue);
                               }
                            } else {
                                // put out the omrs value object
                                if (null==dataClassAnnotation.getExtraAttributes())  {
                                     dataClassAnnotation.setExtraAttributes(new HashMap<String, Object>());
                                }
                               dataClassAnnotation.getExtraAttributes().put(name, primitivePropertyValue);
                            }
                            break;
                        case ENUM:
                            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                            String symbolicName = enumPropertyValue.getSymbolicName();
                            if (dataClassAnnotation.ENUM_NAMES_SET.contains(name)) {
                                 if (name.equals("annotationStatus")) {
                                      AnnotationStatus annotationStatus = AnnotationStatus.valueOf(symbolicName);
                                      dataClassAnnotation.setAnnotationStatus(annotationStatus);
                                 }
                            } else {
                                // put out the omrs value object
                                if (null==dataClassAnnotation.getExtraAttributes())  {
                                     dataClassAnnotation.setExtraAttributes(new HashMap<String, Object>());
                                }
                                 dataClassAnnotation.getExtraAttributes().put(name, enumPropertyValue);
                             }

                            break;
                        case MAP:
                            if (dataClassAnnotation.MAP_NAMES_SET.contains(name)) {
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
                                       dataClassAnnotation.setAdditionalProperties(actualMap);
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
                            if ( dataClassAnnotation.classifications==null) {
                                 dataClassAnnotation.classifications = new ArrayList<>();
                            }
                            dataClassAnnotation.classifications.add(classificationBean);

                        } else {
                            if (null==dataClassAnnotation.getExtraClassificationBeans())  {
                                 dataClassAnnotation.setExtraClassificationBeans(new HashMap<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification>());
                            }
                            dataClassAnnotation.getExtraClassificationBeans().put(omrsClassificationName,omrsClassification);
                        }
                    }
                 }
                 return dataClassAnnotation;
            } else {
                // TODO wrong entity type for this guid
            }
            return null;
    }
    /**
     * Map (convert) the supplied DataClassAnnotation to an entityDetails.
     * @param  dataClassAnnotation  supplied DataClassAnnotation
     * @return  entityDetails equivalent to dataClassAnnotation
     */
    static public EntityDetail mapDataClassAnnotationToOmrsEntityDetail(DataClassAnnotation dataClassAnnotation) {
            EntityDetail omrsEntityDetail = new EntityDetail();
            SystemAttributes systemAttributes = dataClassAnnotation.getSystemAttributes();
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
            instanceProperties.setEffectiveFromTime(dataClassAnnotation.getEffectiveFromTime());
            instanceProperties.setEffectiveToTime(dataClassAnnotation.getEffectiveToTime());
            // primitives

            if (dataClassAnnotation.getMatchingValues()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG);
                primitivePropertyValue.setPrimitiveValue(dataClassAnnotation.getMatchingValues());
                instanceProperties.setProperty("matchingValues", primitivePropertyValue);
            }
            if (dataClassAnnotation.getNonMatchingValues()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG);
                primitivePropertyValue.setPrimitiveValue(dataClassAnnotation.getNonMatchingValues());
                instanceProperties.setProperty("nonMatchingValues", primitivePropertyValue);
            }
            if (dataClassAnnotation.getAnnotationType()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataClassAnnotation.getAnnotationType());
                instanceProperties.setProperty("annotationType", primitivePropertyValue);
            }
            if (dataClassAnnotation.getSummary()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataClassAnnotation.getSummary());
                instanceProperties.setProperty("summary", primitivePropertyValue);
            }
            if (dataClassAnnotation.getConfidenceLevel()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
                primitivePropertyValue.setPrimitiveValue(dataClassAnnotation.getConfidenceLevel());
                instanceProperties.setProperty("confidenceLevel", primitivePropertyValue);
            }
            if (dataClassAnnotation.getExpression()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataClassAnnotation.getExpression());
                instanceProperties.setProperty("expression", primitivePropertyValue);
            }
            if (dataClassAnnotation.getExplanation()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataClassAnnotation.getExplanation());
                instanceProperties.setProperty("explanation", primitivePropertyValue);
            }
            if (dataClassAnnotation.getAnalysisStep()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataClassAnnotation.getAnalysisStep());
                instanceProperties.setProperty("analysisStep", primitivePropertyValue);
            }
            if (dataClassAnnotation.getJsonProperties()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataClassAnnotation.getJsonProperties());
                instanceProperties.setProperty("jsonProperties", primitivePropertyValue);
            }
            if (dataClassAnnotation.getAnnotationStatus()!=null) {
                AnnotationStatus enumType = dataClassAnnotation.getAnnotationStatus();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("annotationStatus", enumPropertyValue);
            }
            if (dataClassAnnotation.getAdditionalProperties()!=null) {

                Map<String,String> map =dataClassAnnotation.getAdditionalProperties();
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
            TypeDef typeDef = archiveAccessor.getEntityDefByName("DataClassAnnotation");
            InstanceType template = archiveAccessor.createTemplateFromTypeDef(typeDef);
            InstanceType instanceType = new InstanceType(template);
            omrsEntityDetail.setType(instanceType);

            // map the classifications
            populateOmrsEntityWithClassificationBeans(omrsEntityDetail,(List<ClassificationBean>)dataClassAnnotation.getClassificationBeans());
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
