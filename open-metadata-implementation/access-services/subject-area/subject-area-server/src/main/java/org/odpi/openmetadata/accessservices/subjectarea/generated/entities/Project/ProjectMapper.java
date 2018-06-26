/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

// omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.archivemanager.OMRSArchiveAccessor;

// omas
import org.odpi.openmetadata.accessservices.subjectarea.common.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.enums.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.ClassificationFactory;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Confidence.Confidence;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Confidentiality.Confidentiality;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Criticality.Criticality;
import org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Retention.Retention;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.GovernanceActions;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;

/**
 * Static mapping methods to map between Project and the omrs equivalents.
 */
public class ProjectMapper {
    private static final Logger log = LoggerFactory.getLogger( ProjectMapper.class);
    private static final String className = ProjectMapper.class.getName();
   /**
    * @param - omrsEntityDetail - the supplied EntityDetail
    * @return - equivalent Project
    */
   static public org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Project.Project mapOmrsEntityDetailToProject(EntityDetail omrsEntityDetail) throws InvalidParameterException{
        String entityTypeName = omrsEntityDetail.getType().getTypeDefName();
        if ("Project".equals(entityTypeName)) {
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Project.Project project = new Project();
                //set core attributes
                SystemAttributes systemAttributes = new SystemAttributes();

                InstanceStatus instanceStatus =  omrsEntityDetail.getStatus();
                Status omas_status = SubjectAreaUtils.convertInstanceStatusToStatus(instanceStatus);
                systemAttributes.setStatus(omas_status);

                systemAttributes.setCreatedBy(omrsEntityDetail.getCreatedBy());
                systemAttributes.setUpdatedBy(omrsEntityDetail.getUpdatedBy());
                systemAttributes.setCreateTime(omrsEntityDetail.getCreateTime());
                systemAttributes.setUpdateTime(omrsEntityDetail.getUpdateTime());
                systemAttributes.setVersion(omrsEntityDetail.getVersion());
                systemAttributes.setGUID(omrsEntityDetail.getGUID());
                project.setSystemAttributes(systemAttributes);


                // Set properties
                InstanceProperties omrsEntityDetailProperties = omrsEntityDetail.getProperties();
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
                            if (project.ATTRIBUTE_NAMES_SET.contains(name)) {
                               if (name.equals("name")) {
                                   project.setName(actualValue);
                               }
                               if (name.equals("description")) {
                                   project.setDescription(actualValue);
                               }
                               if (name.equals("startDate")) {
                                   project.setStartDate(actualValue);
                               }
                               if (name.equals("plannedEndDate")) {
                                   project.setPlannedEndDate(actualValue);
                               }
                               if (name.equals("status")) {
                                   project.setStatus(actualValue);
                               }
                               if (name.equals("qualifiedName")) {
                                   project.setQualifiedName(actualValue);
                               }
                            } else {
                                // put out the omrs value object
                                if (null==project.getExtraAttributes())  {
                                     project.setExtraAttributes(new HashMap<String, Object>());
                                }
                               project.getExtraAttributes().put(name, primitivePropertyValue);
                            }
                            break;
                        case ENUM:
                            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                            String symbolicName = enumPropertyValue.getSymbolicName();
                            if (project.ENUM_NAMES_SET.contains(name)) {
                            } else {
                                // put out the omrs value object
                                if (null==project.getExtraAttributes())  {
                                     project.setExtraAttributes(new HashMap<String, Object>());
                                }
                                 project.getExtraAttributes().put(name, enumPropertyValue);
                             }

                            break;
                        case MAP:
                            if (project.MAP_NAMES_SET.contains(name)) {
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
                                       project.setAdditionalProperties(actualMap);
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

                 // set classifications
                 List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> omrsclassifications = omrsEntityDetail.getClassifications() ;
                 if (omrsclassifications != null && !omrsclassifications.isEmpty()){
                    for (org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification omrsClassification:omrsclassifications) {
                        String omrsClassificationName = omrsClassification.getName();
                        org.odpi.openmetadata.accessservices.subjectarea.common.Classification omasClassification = ClassificationFactory.getClassification(omrsClassificationName);
                        if (omasClassification !=null) {
                            // this is a classification we know about.
                            while (omrsClassification.getProperties().getPropertyNames().hasNext()) {
                                String omrsAttrName = omrsClassification.getProperties().getPropertyNames().next();
                                InstancePropertyValue instancePropertyValue = omrsClassification.getProperties().getPropertyValue(omrsAttrName);
                                omasClassification.obtainInstanceProperties().setProperty(omrsAttrName,instancePropertyValue);
                                project.classifications.add(omasClassification);
                            }
                        } else {
                            if (null==project.getExtraClassifications())  {
                                 project.setExtraClassifications(new HashMap<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification>());
                            }
                            project.getExtraClassifications().put(omrsClassificationName,omrsClassification);
                        }
                    }
                 }
                 return project;
            } else {
                // TODO wrong entity type for this guid
            }
            return null;
    }
    /**
     * Map (convert) the supplied Project to an entityDetail.
     * @param -  project  supplied Project
     * @return -  entityDetail equivalent to project
     */
    static public EntityDetail mapProjectToOmrsEntityDetail(org.odpi.openmetadata.accessservices.subjectarea.generated.entities.Project.Project project) {
            EntityDetail omrsEntityDetail = new EntityDetail();
            SystemAttributes systemAttributes = project.getSystemAttributes();
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
                   if (systemAttributes.getStatus()!=null) {
                        InstanceStatus instanceStatus = SubjectAreaUtils.convertStatusToStatusInstance(systemAttributes.getStatus());
                        omrsEntityDetail.setStatus(instanceStatus);
                   }
            }

            InstanceProperties instanceProperties = new InstanceProperties();
            // primitives

            if (project.getName()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(project.getName());
                instanceProperties.setProperty("name", primitivePropertyValue);
            }
            if (project.getDescription()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(project.getDescription());
                instanceProperties.setProperty("description", primitivePropertyValue);
            }
            if (project.getStartDate()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
                primitivePropertyValue.setPrimitiveValue(project.getStartDate());
                instanceProperties.setProperty("startDate", primitivePropertyValue);
            }
            if (project.getPlannedEndDate()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
                primitivePropertyValue.setPrimitiveValue(project.getPlannedEndDate());
                instanceProperties.setProperty("plannedEndDate", primitivePropertyValue);
            }
            if (project.getStatus()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(project.getStatus());
                instanceProperties.setProperty("status", primitivePropertyValue);
            }
            if (project.getQualifiedName()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(project.getQualifiedName());
                instanceProperties.setProperty("qualifiedName", primitivePropertyValue);
            }
            if (project.getAdditionalProperties()!=null) {

                Map<String,String> map =project.getAdditionalProperties();
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
            TypeDef typeDef = archiveAccessor.getEntityDefByName("Project");
            InstanceType template = SubjectAreaUtils.createTemplateFromTypeDef(typeDef);
            InstanceType instanceType = new InstanceType(template);
            omrsEntityDetail.setType(instanceType);

            // map the classifications
            populateOmrsEntityWithOmasClassifications(omrsEntityDetail,(List<org.odpi.openmetadata.accessservices.subjectarea.common.Classification>)project.getClassifications());
            return omrsEntityDetail;
    }

    private static void populateOmrsEntityWithOmasClassifications(EntityDetail omrsEntityDetail, List<org.odpi.openmetadata.accessservices.subjectarea.common.Classification> omasClassifications) {
        if (omasClassifications!= null && omasClassifications.size()>0) {
            ArrayList<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> omrsClassifications = new ArrayList<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification>();
            for (org.odpi.openmetadata.accessservices.subjectarea.common.Classification omasClassification : omasClassifications) {
                SystemAttributes systemAttributes = omasClassification.getSystemAttributes();
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
                // copy over the other attributes.
                Iterator iter = omasClassification.obtainInstanceProperties().getPropertyNames();
                while (iter.hasNext()) {
                    String name = (String) iter.next();
                    InstancePropertyValue instancePropertyValue = omasClassification.obtainInstanceProperties().getPropertyValue(name);
                    omrsClassification.getProperties().setProperty(name, instancePropertyValue);
                }
                omrsClassifications.add(omrsClassification);
            }
            omrsEntityDetail.setClassifications(omrsClassifications);
        }
    }
}
