/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.DataFieldAnnotation;

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
 * Static mapping methods to map between DataFieldAnnotation and the omrs equivalents.
 */
public class DataFieldAnnotationMapper {
    private static final Logger log = LoggerFactory.getLogger( DataFieldAnnotationMapper.class);
    private static final String className = DataFieldAnnotationMapper.class.getName();
   /**
    * @param omrsEntityDetail the supplied EntityDetail
    * @return equivalent DataFieldAnnotation
    */
   static public DataFieldAnnotation mapOmrsEntityDetailToDataFieldAnnotation(EntityDetail omrsEntityDetail) throws InvalidParameterException {
        String entityTypeName = omrsEntityDetail.getType().getTypeDefName();
        if ("DataFieldAnnotation".equals(entityTypeName)) {
                DataFieldAnnotation dataFieldAnnotation = new DataFieldAnnotation();
                //set core attributes
                SystemAttributes systemAttributes = new SystemAttributes();
                systemAttributes.setStatus(omrsEntityDetail.getStatus());

                systemAttributes.setCreatedBy(omrsEntityDetail.getCreatedBy());
                systemAttributes.setUpdatedBy(omrsEntityDetail.getUpdatedBy());
                systemAttributes.setCreateTime(omrsEntityDetail.getCreateTime());
                systemAttributes.setUpdateTime(omrsEntityDetail.getUpdateTime());
                systemAttributes.setVersion(omrsEntityDetail.getVersion());
                systemAttributes.setGUID(omrsEntityDetail.getGUID());
                dataFieldAnnotation.setSystemAttributes(systemAttributes);

                // Set properties
                InstanceProperties omrsEntityDetailProperties = omrsEntityDetail.getProperties();
                if (omrsEntityDetailProperties!=null) {
                  omrsEntityDetailProperties.setEffectiveFromTime(dataFieldAnnotation.getEffectiveFromTime());
                  omrsEntityDetailProperties.setEffectiveToTime(dataFieldAnnotation.getEffectiveToTime());
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
                            if (dataFieldAnnotation.ATTRIBUTE_NAMES_SET.contains(name)) {
                               if (name.equals("annotationType")) {
                                   dataFieldAnnotation.setAnnotationType((String)actualValue);
                               }
                               if (name.equals("summary")) {
                                   dataFieldAnnotation.setSummary((String)actualValue);
                               }
                               if (name.equals("confidenceLevel")) {
                                   dataFieldAnnotation.setConfidenceLevel((Integer)actualValue);
                               }
                               if (name.equals("expression")) {
                                   dataFieldAnnotation.setExpression((String)actualValue);
                               }
                               if (name.equals("explanation")) {
                                   dataFieldAnnotation.setExplanation((String)actualValue);
                               }
                               if (name.equals("analysisStep")) {
                                   dataFieldAnnotation.setAnalysisStep((String)actualValue);
                               }
                               if (name.equals("jsonProperties")) {
                                   dataFieldAnnotation.setJsonProperties((String)actualValue);
                               }
                            } else {
                                // put out the omrs value object
                                if (null==dataFieldAnnotation.getExtraAttributes())  {
                                     dataFieldAnnotation.setExtraAttributes(new HashMap<String, Object>());
                                }
                               dataFieldAnnotation.getExtraAttributes().put(name, primitivePropertyValue);
                            }
                            break;
                        case ENUM:
                            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                            String symbolicName = enumPropertyValue.getSymbolicName();
                            if (dataFieldAnnotation.ENUM_NAMES_SET.contains(name)) {
                                 if (name.equals("annotationStatus")) {
                                      AnnotationStatus annotationStatus = AnnotationStatus.valueOf(symbolicName);
                                      dataFieldAnnotation.setAnnotationStatus(annotationStatus);
                                 }
                            } else {
                                // put out the omrs value object
                                if (null==dataFieldAnnotation.getExtraAttributes())  {
                                     dataFieldAnnotation.setExtraAttributes(new HashMap<String, Object>());
                                }
                                 dataFieldAnnotation.getExtraAttributes().put(name, enumPropertyValue);
                             }

                            break;
                        case MAP:
                            if (dataFieldAnnotation.MAP_NAMES_SET.contains(name)) {
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
                                       dataFieldAnnotation.setAdditionalProperties(actualMap);
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
                            if ( dataFieldAnnotation.classifications==null) {
                                 dataFieldAnnotation.classifications = new ArrayList<>();
                            }
                            dataFieldAnnotation.classifications.add(classificationBean);

                        } else {
                            if (null==dataFieldAnnotation.getExtraClassificationBeans())  {
                                 dataFieldAnnotation.setExtraClassificationBeans(new HashMap<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification>());
                            }
                            dataFieldAnnotation.getExtraClassificationBeans().put(omrsClassificationName,omrsClassification);
                        }
                    }
                 }
                 return dataFieldAnnotation;
            } else {
                // TODO wrong entity type for this guid
            }
            return null;
    }
    /**
     * Map (convert) the supplied DataFieldAnnotation to an entityDetails.
     * @param  dataFieldAnnotation  supplied DataFieldAnnotation
     * @return  entityDetails equivalent to dataFieldAnnotation
     */
    static public EntityDetail mapDataFieldAnnotationToOmrsEntityDetail(DataFieldAnnotation dataFieldAnnotation) {
            EntityDetail omrsEntityDetail = new EntityDetail();
            SystemAttributes systemAttributes = dataFieldAnnotation.getSystemAttributes();
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
            instanceProperties.setEffectiveFromTime(dataFieldAnnotation.getEffectiveFromTime());
            instanceProperties.setEffectiveToTime(dataFieldAnnotation.getEffectiveToTime());
            // primitives

            if (dataFieldAnnotation.getAnnotationType()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataFieldAnnotation.getAnnotationType());
                instanceProperties.setProperty("annotationType", primitivePropertyValue);
            }
            if (dataFieldAnnotation.getSummary()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataFieldAnnotation.getSummary());
                instanceProperties.setProperty("summary", primitivePropertyValue);
            }
            if (dataFieldAnnotation.getConfidenceLevel()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
                primitivePropertyValue.setPrimitiveValue(dataFieldAnnotation.getConfidenceLevel());
                instanceProperties.setProperty("confidenceLevel", primitivePropertyValue);
            }
            if (dataFieldAnnotation.getExpression()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataFieldAnnotation.getExpression());
                instanceProperties.setProperty("expression", primitivePropertyValue);
            }
            if (dataFieldAnnotation.getExplanation()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataFieldAnnotation.getExplanation());
                instanceProperties.setProperty("explanation", primitivePropertyValue);
            }
            if (dataFieldAnnotation.getAnalysisStep()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataFieldAnnotation.getAnalysisStep());
                instanceProperties.setProperty("analysisStep", primitivePropertyValue);
            }
            if (dataFieldAnnotation.getJsonProperties()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataFieldAnnotation.getJsonProperties());
                instanceProperties.setProperty("jsonProperties", primitivePropertyValue);
            }
            if (dataFieldAnnotation.getAnnotationStatus()!=null) {
                AnnotationStatus enumType = dataFieldAnnotation.getAnnotationStatus();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("annotationStatus", enumPropertyValue);
            }
            if (dataFieldAnnotation.getAdditionalProperties()!=null) {

                Map<String,String> map =dataFieldAnnotation.getAdditionalProperties();
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
            TypeDef typeDef = archiveAccessor.getEntityDefByName("DataFieldAnnotation");
            InstanceType template = archiveAccessor.createTemplateFromTypeDef(typeDef);
            InstanceType instanceType = new InstanceType(template);
            omrsEntityDetail.setType(instanceType);

            // map the classifications
            populateOmrsEntityWithClassificationBeans(omrsEntityDetail,(List<ClassificationBean>)dataFieldAnnotation.getClassificationBeans());
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
