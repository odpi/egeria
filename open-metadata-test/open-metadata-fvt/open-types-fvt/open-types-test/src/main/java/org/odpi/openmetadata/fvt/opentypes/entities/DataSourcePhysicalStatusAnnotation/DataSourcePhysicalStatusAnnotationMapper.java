/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.DataSourcePhysicalStatusAnnotation;

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
 * Static mapping methods to map between DataSourcePhysicalStatusAnnotation and the omrs equivalents.
 */
public class DataSourcePhysicalStatusAnnotationMapper {
    private static final Logger log = LoggerFactory.getLogger( DataSourcePhysicalStatusAnnotationMapper.class);
    private static final String className = DataSourcePhysicalStatusAnnotationMapper.class.getName();
   /**
    * @param omrsEntityDetail the supplied EntityDetail
    * @return equivalent DataSourcePhysicalStatusAnnotation
    */
   static public DataSourcePhysicalStatusAnnotation mapOmrsEntityDetailToDataSourcePhysicalStatusAnnotation(EntityDetail omrsEntityDetail) throws InvalidParameterException {
        String entityTypeName = omrsEntityDetail.getType().getTypeDefName();
        if ("DataSourcePhysicalStatusAnnotation".equals(entityTypeName)) {
                DataSourcePhysicalStatusAnnotation dataSourcePhysicalStatusAnnotation = new DataSourcePhysicalStatusAnnotation();
                //set core attributes
                SystemAttributes systemAttributes = new SystemAttributes();
                systemAttributes.setStatus(omrsEntityDetail.getStatus());

                systemAttributes.setCreatedBy(omrsEntityDetail.getCreatedBy());
                systemAttributes.setUpdatedBy(omrsEntityDetail.getUpdatedBy());
                systemAttributes.setCreateTime(omrsEntityDetail.getCreateTime());
                systemAttributes.setUpdateTime(omrsEntityDetail.getUpdateTime());
                systemAttributes.setVersion(omrsEntityDetail.getVersion());
                systemAttributes.setGUID(omrsEntityDetail.getGUID());
                dataSourcePhysicalStatusAnnotation.setSystemAttributes(systemAttributes);

                // Set properties
                InstanceProperties omrsEntityDetailProperties = omrsEntityDetail.getProperties();
                if (omrsEntityDetailProperties!=null) {
                  omrsEntityDetailProperties.setEffectiveFromTime(dataSourcePhysicalStatusAnnotation.getEffectiveFromTime());
                  omrsEntityDetailProperties.setEffectiveToTime(dataSourcePhysicalStatusAnnotation.getEffectiveToTime());
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
                            if (dataSourcePhysicalStatusAnnotation.ATTRIBUTE_NAMES_SET.contains(name)) {
                               if (name.equals("createTime")) {
                                   dataSourcePhysicalStatusAnnotation.setCreateTime((java.util.Date)actualValue);
                               }
                               if (name.equals("modifiedTime")) {
                                   dataSourcePhysicalStatusAnnotation.setModifiedTime((java.util.Date)actualValue);
                               }
                               if (name.equals("size")) {
                                   dataSourcePhysicalStatusAnnotation.setSize((Integer)actualValue);
                               }
                               if (name.equals("encoding")) {
                                   dataSourcePhysicalStatusAnnotation.setEncoding((String)actualValue);
                               }
                               if (name.equals("annotationType")) {
                                   dataSourcePhysicalStatusAnnotation.setAnnotationType((String)actualValue);
                               }
                               if (name.equals("summary")) {
                                   dataSourcePhysicalStatusAnnotation.setSummary((String)actualValue);
                               }
                               if (name.equals("confidenceLevel")) {
                                   dataSourcePhysicalStatusAnnotation.setConfidenceLevel((Integer)actualValue);
                               }
                               if (name.equals("expression")) {
                                   dataSourcePhysicalStatusAnnotation.setExpression((String)actualValue);
                               }
                               if (name.equals("explanation")) {
                                   dataSourcePhysicalStatusAnnotation.setExplanation((String)actualValue);
                               }
                               if (name.equals("analysisStep")) {
                                   dataSourcePhysicalStatusAnnotation.setAnalysisStep((String)actualValue);
                               }
                               if (name.equals("jsonProperties")) {
                                   dataSourcePhysicalStatusAnnotation.setJsonProperties((String)actualValue);
                               }
                            } else {
                                // put out the omrs value object
                                if (null==dataSourcePhysicalStatusAnnotation.getExtraAttributes())  {
                                     dataSourcePhysicalStatusAnnotation.setExtraAttributes(new HashMap<String, Object>());
                                }
                               dataSourcePhysicalStatusAnnotation.getExtraAttributes().put(name, primitivePropertyValue);
                            }
                            break;
                        case ENUM:
                            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                            String symbolicName = enumPropertyValue.getSymbolicName();
                            if (dataSourcePhysicalStatusAnnotation.ENUM_NAMES_SET.contains(name)) {
                                 if (name.equals("annotationStatus")) {
                                      AnnotationStatus annotationStatus = AnnotationStatus.valueOf(symbolicName);
                                      dataSourcePhysicalStatusAnnotation.setAnnotationStatus(annotationStatus);
                                 }
                            } else {
                                // put out the omrs value object
                                if (null==dataSourcePhysicalStatusAnnotation.getExtraAttributes())  {
                                     dataSourcePhysicalStatusAnnotation.setExtraAttributes(new HashMap<String, Object>());
                                }
                                 dataSourcePhysicalStatusAnnotation.getExtraAttributes().put(name, enumPropertyValue);
                             }

                            break;
                        case MAP:
                            if (dataSourcePhysicalStatusAnnotation.MAP_NAMES_SET.contains(name)) {
                                 MapPropertyValue mapPropertyValue = (MapPropertyValue) value;
                                 InstanceProperties instancePropertyForMap = (InstanceProperties) mapPropertyValue.getMapValues();

                                 if (name.equals("dataSourceProperties")) {

                                       // Only support Map<String,String> as that is what is in the archive types at this time.
                                       Map<String, String> actualMap = new HashMap();
                                       Iterator iter = instancePropertyForMap.getPropertyNames();
                                       while (iter.hasNext()) {
                                           String mapkey = (String) iter.next();
                                           PrimitivePropertyValue primitivePropertyMapValue = (PrimitivePropertyValue) instancePropertyForMap.getPropertyValue(mapkey);
                                           String mapvalue = (String) primitivePropertyMapValue.getPrimitiveValue();
                                           actualMap.put(mapkey, mapvalue);
                                       }
                                       dataSourcePhysicalStatusAnnotation.setDataSourceProperties(actualMap);
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
                                       dataSourcePhysicalStatusAnnotation.setAdditionalProperties(actualMap);
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
                            if ( dataSourcePhysicalStatusAnnotation.classifications==null) {
                                 dataSourcePhysicalStatusAnnotation.classifications = new ArrayList<>();
                            }
                            dataSourcePhysicalStatusAnnotation.classifications.add(classificationBean);

                        } else {
                            if (null==dataSourcePhysicalStatusAnnotation.getExtraClassificationBeans())  {
                                 dataSourcePhysicalStatusAnnotation.setExtraClassificationBeans(new HashMap<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification>());
                            }
                            dataSourcePhysicalStatusAnnotation.getExtraClassificationBeans().put(omrsClassificationName,omrsClassification);
                        }
                    }
                 }
                 return dataSourcePhysicalStatusAnnotation;
            } else {
                // TODO wrong entity type for this guid
            }
            return null;
    }
    /**
     * Map (convert) the supplied DataSourcePhysicalStatusAnnotation to an entityDetails.
     * @param  dataSourcePhysicalStatusAnnotation  supplied DataSourcePhysicalStatusAnnotation
     * @return  entityDetails equivalent to dataSourcePhysicalStatusAnnotation
     */
    static public EntityDetail mapDataSourcePhysicalStatusAnnotationToOmrsEntityDetail(DataSourcePhysicalStatusAnnotation dataSourcePhysicalStatusAnnotation) {
            EntityDetail omrsEntityDetail = new EntityDetail();
            SystemAttributes systemAttributes = dataSourcePhysicalStatusAnnotation.getSystemAttributes();
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
            instanceProperties.setEffectiveFromTime(dataSourcePhysicalStatusAnnotation.getEffectiveFromTime());
            instanceProperties.setEffectiveToTime(dataSourcePhysicalStatusAnnotation.getEffectiveToTime());
            // primitives

            if (dataSourcePhysicalStatusAnnotation.getCreateTime()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
                primitivePropertyValue.setPrimitiveValue(dataSourcePhysicalStatusAnnotation.getCreateTime());
                instanceProperties.setProperty("createTime", primitivePropertyValue);
            }
            if (dataSourcePhysicalStatusAnnotation.getModifiedTime()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
                primitivePropertyValue.setPrimitiveValue(dataSourcePhysicalStatusAnnotation.getModifiedTime());
                instanceProperties.setProperty("modifiedTime", primitivePropertyValue);
            }
            if (dataSourcePhysicalStatusAnnotation.getSize()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
                primitivePropertyValue.setPrimitiveValue(dataSourcePhysicalStatusAnnotation.getSize());
                instanceProperties.setProperty("size", primitivePropertyValue);
            }
            if (dataSourcePhysicalStatusAnnotation.getEncoding()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataSourcePhysicalStatusAnnotation.getEncoding());
                instanceProperties.setProperty("encoding", primitivePropertyValue);
            }
            if (dataSourcePhysicalStatusAnnotation.getAnnotationType()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataSourcePhysicalStatusAnnotation.getAnnotationType());
                instanceProperties.setProperty("annotationType", primitivePropertyValue);
            }
            if (dataSourcePhysicalStatusAnnotation.getSummary()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataSourcePhysicalStatusAnnotation.getSummary());
                instanceProperties.setProperty("summary", primitivePropertyValue);
            }
            if (dataSourcePhysicalStatusAnnotation.getConfidenceLevel()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
                primitivePropertyValue.setPrimitiveValue(dataSourcePhysicalStatusAnnotation.getConfidenceLevel());
                instanceProperties.setProperty("confidenceLevel", primitivePropertyValue);
            }
            if (dataSourcePhysicalStatusAnnotation.getExpression()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataSourcePhysicalStatusAnnotation.getExpression());
                instanceProperties.setProperty("expression", primitivePropertyValue);
            }
            if (dataSourcePhysicalStatusAnnotation.getExplanation()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataSourcePhysicalStatusAnnotation.getExplanation());
                instanceProperties.setProperty("explanation", primitivePropertyValue);
            }
            if (dataSourcePhysicalStatusAnnotation.getAnalysisStep()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataSourcePhysicalStatusAnnotation.getAnalysisStep());
                instanceProperties.setProperty("analysisStep", primitivePropertyValue);
            }
            if (dataSourcePhysicalStatusAnnotation.getJsonProperties()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataSourcePhysicalStatusAnnotation.getJsonProperties());
                instanceProperties.setProperty("jsonProperties", primitivePropertyValue);
            }
            if (dataSourcePhysicalStatusAnnotation.getAnnotationStatus()!=null) {
                AnnotationStatus enumType = dataSourcePhysicalStatusAnnotation.getAnnotationStatus();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("annotationStatus", enumPropertyValue);
            }
            if (dataSourcePhysicalStatusAnnotation.getDataSourceProperties()!=null) {

                Map<String,String> map =dataSourcePhysicalStatusAnnotation.getDataSourceProperties();
                MapPropertyValue mapPropertyValue = new MapPropertyValue();

                for (String key:map.keySet()) {
                   PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                   primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                   primitivePropertyValue.setPrimitiveValue(map.get(key));
                   mapPropertyValue.setMapValue(key,primitivePropertyValue);
                }

                instanceProperties.setProperty("dataSourceProperties", mapPropertyValue);
            }
            if (dataSourcePhysicalStatusAnnotation.getAdditionalProperties()!=null) {

                Map<String,String> map =dataSourcePhysicalStatusAnnotation.getAdditionalProperties();
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
            TypeDef typeDef = archiveAccessor.getEntityDefByName("DataSourcePhysicalStatusAnnotation");
            InstanceType template = archiveAccessor.createTemplateFromTypeDef(typeDef);
            InstanceType instanceType = new InstanceType(template);
            omrsEntityDetail.setType(instanceType);

            // map the classifications
            populateOmrsEntityWithClassificationBeans(omrsEntityDetail,(List<ClassificationBean>)dataSourcePhysicalStatusAnnotation.getClassificationBeans());
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
