/* SPDX-License-Identifier: Apache-2.0 */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GovernanceResponsibility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

// omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.archivemanager.OMRSArchiveAccessor;

// omas
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.classifications.ClassificationFactory;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.*;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;

/**
 * Static mapping methods to map between GovernanceResponsibility and the omrs equivalents.
 */
public class GovernanceResponsibilityMapper {
    private static final Logger log = LoggerFactory.getLogger( GovernanceResponsibilityMapper.class);
    private static final String className = GovernanceResponsibilityMapper.class.getName();
   /**
    * @param omrsEntityDetail the supplied EntityDetail
    * @return equivalent GovernanceResponsibility
    */
   static public org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GovernanceResponsibility.GovernanceResponsibility mapOmrsEntityDetailToGovernanceResponsibility(EntityDetail omrsEntityDetail) throws InvalidParameterException{
        String entityTypeName = omrsEntityDetail.getType().getTypeDefName();
        if ("GovernanceResponsibility".equals(entityTypeName)) {
                org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GovernanceResponsibility.GovernanceResponsibility governanceResponsibility = new GovernanceResponsibility();
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
                governanceResponsibility.setSystemAttributes(systemAttributes);


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
                            if (governanceResponsibility.ATTRIBUTE_NAMES_SET.contains(name)) {
                               if (name.equals("implementationDescription")) {
                                   governanceResponsibility.setImplementationDescription((String)actualValue);
                               }
                               if (name.equals("title")) {
                                   governanceResponsibility.setTitle((String)actualValue);
                               }
                               if (name.equals("summary")) {
                                   governanceResponsibility.setSummary((String)actualValue);
                               }
                               if (name.equals("description")) {
                                   governanceResponsibility.setDescription((String)actualValue);
                               }
                               if (name.equals("scope")) {
                                   governanceResponsibility.setScope((String)actualValue);
                               }
                               if (name.equals("priority")) {
                                   governanceResponsibility.setPriority((String)actualValue);
                               }
                               if (name.equals("qualifiedName")) {
                                   governanceResponsibility.setQualifiedName((String)actualValue);
                               }
                            } else {
                                // put out the omrs value object
                                if (null==governanceResponsibility.getExtraAttributes())  {
                                     governanceResponsibility.setExtraAttributes(new HashMap<String, Object>());
                                }
                               governanceResponsibility.getExtraAttributes().put(name, primitivePropertyValue);
                            }
                            break;
                        case ENUM:
                            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                            String symbolicName = enumPropertyValue.getSymbolicName();
                            if (governanceResponsibility.ENUM_NAMES_SET.contains(name)) {
                                 if (name.equals("status")) {
                                       org.odpi.openmetadata.accessservices.subjectarea.properties.enums.GovernanceDefinitionStatus status = org.odpi.openmetadata.accessservices.subjectarea.properties.enums.GovernanceDefinitionStatus.valueOf(symbolicName);
                                      governanceResponsibility.setStatus(status);
                                 }
                            } else {
                                // put out the omrs value object
                                if (null==governanceResponsibility.getExtraAttributes())  {
                                     governanceResponsibility.setExtraAttributes(new HashMap<String, Object>());
                                }
                                 governanceResponsibility.getExtraAttributes().put(name, enumPropertyValue);
                             }

                            break;
                        case MAP:
                            if (governanceResponsibility.MAP_NAMES_SET.contains(name)) {
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
                                       governanceResponsibility.setAdditionalProperties(actualMap);
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
                        org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification omasClassification = ClassificationFactory.getClassification(omrsClassificationName,omrsClassification);
                        if (omasClassification !=null) {
                            // this is a classification we know about.
                            if ( governanceResponsibility.classifications==null) {
                                 governanceResponsibility.classifications = new ArrayList<>();
                            }
                            governanceResponsibility.classifications.add(omasClassification);

                        } else {
                            if (null==governanceResponsibility.getExtraClassifications())  {
                                 governanceResponsibility.setExtraClassifications(new HashMap<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification>());
                            }
                            governanceResponsibility.getExtraClassifications().put(omrsClassificationName,omrsClassification);
                        }
                    }
                 }
                 return governanceResponsibility;
            } else {
                // TODO wrong entity type for this guid
            }
            return null;
    }
    /**
     * Map (convert) the supplied GovernanceResponsibility to an entityDetail.
     * @param  governanceResponsibility  supplied GovernanceResponsibility
     * @return  entityDetail equivalent to governanceResponsibility
     */
    static public EntityDetail mapGovernanceResponsibilityToOmrsEntityDetail(org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GovernanceResponsibility.GovernanceResponsibility governanceResponsibility) {
            EntityDetail omrsEntityDetail = new EntityDetail();
            SystemAttributes systemAttributes = governanceResponsibility.getSystemAttributes();
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
                        InstanceStatus instanceStatus = SubjectAreaUtils.convertStatusToStatusInstance(systemAttributes.getStatus());
                        omrsEntityDetail.setStatus(instanceStatus);
                   }
            }

            InstanceProperties instanceProperties = new InstanceProperties();
            // primitives

            if (governanceResponsibility.getImplementationDescription()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(governanceResponsibility.getImplementationDescription());
                instanceProperties.setProperty("implementationDescription", primitivePropertyValue);
            }
            if (governanceResponsibility.getTitle()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(governanceResponsibility.getTitle());
                instanceProperties.setProperty("title", primitivePropertyValue);
            }
            if (governanceResponsibility.getSummary()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(governanceResponsibility.getSummary());
                instanceProperties.setProperty("summary", primitivePropertyValue);
            }
            if (governanceResponsibility.getDescription()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(governanceResponsibility.getDescription());
                instanceProperties.setProperty("description", primitivePropertyValue);
            }
            if (governanceResponsibility.getScope()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(governanceResponsibility.getScope());
                instanceProperties.setProperty("scope", primitivePropertyValue);
            }
            if (governanceResponsibility.getPriority()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(governanceResponsibility.getPriority());
                instanceProperties.setProperty("priority", primitivePropertyValue);
            }
            if (governanceResponsibility.getQualifiedName()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(governanceResponsibility.getQualifiedName());
                instanceProperties.setProperty("qualifiedName", primitivePropertyValue);
            }
            if (governanceResponsibility.getStatus()!=null) {
                GovernanceDefinitionStatus enumType = governanceResponsibility.getStatus();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("status", enumPropertyValue);
            }
            if (governanceResponsibility.getAdditionalProperties()!=null) {

                Map<String,String> map =governanceResponsibility.getAdditionalProperties();
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
            TypeDef typeDef = archiveAccessor.getEntityDefByName("GovernanceResponsibility");
            InstanceType template = SubjectAreaUtils.createTemplateFromTypeDef(typeDef);
            InstanceType instanceType = new InstanceType(template);
            omrsEntityDetail.setType(instanceType);

            // map the classifications
            populateOmrsEntityWithBeanClassifications(omrsEntityDetail,(List<org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification>)governanceResponsibility.getClassifications());
            return omrsEntityDetail;
    }

    private static void populateOmrsEntityWithBeanClassifications(EntityDetail omrsEntityDetail, List<org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification> beanClassifications) {
        if (beanClassifications!= null && beanClassifications.size()>0) {
            ArrayList<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> omrsClassifications = new ArrayList<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification>();
            for (org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification beanClassification : beanClassifications) {
                SystemAttributes systemAttributes = beanClassification.getSystemAttributes();
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
                omrsClassification.setName(beanClassification.getClassificationName());
                // copy over the classification properties
                omrsClassification.setProperties( beanClassification.obtainInstanceProperties());
                omrsClassifications.add(omrsClassification);
            }
            omrsEntityDetail.setClassifications(omrsClassifications);
        }
    }
}
