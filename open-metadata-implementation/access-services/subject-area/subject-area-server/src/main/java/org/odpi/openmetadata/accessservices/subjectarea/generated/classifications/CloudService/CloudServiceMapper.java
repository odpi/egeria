/* SPDX-License-Identifier: Apache-2.0 */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.CloudService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Task;

// omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.archivemanager.OMRSArchiveAccessor;

// omrs bean
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.classifications.ClassificationFactory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;

/**
 * Static mapping methods to map between CloudService and the omrs equivalents.
 */
public class CloudServiceMapper {
    private static final Logger log = LoggerFactory.getLogger( CloudServiceMapper.class);
    private static final String className = CloudServiceMapper.class.getName();
   /**
    * @param omrsClassification - the supplied omrs classification
    * @return equivalent CloudService
    */
   static public org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.CloudService mapOmrsToOmas(Classification omrsClassification) throws InvalidParameterException{
        String classificationTypeName = omrsClassification.getName();
        if ("CloudService".equals(classificationTypeName)) {
                org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.CloudService cloudService = new org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.CloudService();
                //set core attributes
                SystemAttributes systemAttributes = new SystemAttributes();

                InstanceStatus instanceStatus =  omrsClassification.getStatus();
                Status omrsBeanStatus = SubjectAreaUtils.convertInstanceStatusToStatus(instanceStatus);
                systemAttributes.setStatus(omrsBeanStatus);

                systemAttributes.setCreatedBy(omrsClassification.getCreatedBy());
                systemAttributes.setUpdatedBy(omrsClassification.getUpdatedBy());
                systemAttributes.setCreateTime(omrsClassification.getCreateTime());
                systemAttributes.setUpdateTime(omrsClassification.getUpdateTime());
                systemAttributes.setVersion(omrsClassification.getVersion());
                cloudService.setSystemAttributes(systemAttributes);


                // Set properties
                InstanceProperties omrsClassificationProperties = omrsClassification.getProperties();
                Iterator omrsPropertyIterator = omrsClassificationProperties.getPropertyNames();
                while (omrsPropertyIterator.hasNext()) {
                    String name = (String) omrsPropertyIterator.next();
                    //TODO check if this is a property we expect or whether the type has been added to.
                    // this is a property we expect
                    InstancePropertyValue value = omrsClassificationProperties.getPropertyValue(name);

                    // supplied guid matches the expected type

                    Object actualValue;
                    switch (value.getInstancePropertyCategory()) {
                        case PRIMITIVE:
                            PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) value;
                            actualValue = primitivePropertyValue.getPrimitiveValue();
                            if (cloudService.ATTRIBUTE_NAMES_SET.contains(name)) {
                               if (name.equals("offeringName")) {
                                  cloudService.setOfferingName((String)actualValue);
                               }
                               if (name.equals("type")) {
                                  cloudService.setType((String)actualValue);
                               }
                            } else {
                                // put out the omrs value object
                                if (null==cloudService.getExtraAttributes())  {
                                     cloudService.setExtraAttributes(new HashMap<String, Object>());
                                }
                               cloudService.getExtraAttributes().put(name, primitivePropertyValue);
                            }
                            break;
                        case ENUM:
                            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                            String symbolicName = enumPropertyValue.getSymbolicName();
                            if (cloudService.ENUM_NAMES_SET.contains(name)) {
                            } else {
                                // put out the omrs value object
                                if (null==cloudService.getExtraAttributes())  {
                                     cloudService.setExtraAttributes(new HashMap<String, Object>());
                                }
                                 cloudService.getExtraAttributes().put(name, enumPropertyValue);
                             }

                            break;
                        case MAP:
                            if (cloudService.MAP_NAMES_SET.contains(name)) {
                                 MapPropertyValue mapPropertyValue = (MapPropertyValue) value;
                                 InstanceProperties instancePropertyForMap = (InstanceProperties) mapPropertyValue.getMapValues();

                               }
                               break;
                        case ARRAY:
                        case STRUCT:
                        case UNKNOWN:
                            // error
                            break;
                    }

                }   // end while

                 return cloudService;
            } else {
                // TODO wrong type
            }
            return null;
    }
    /**
     * Map (convert) the supplied CloudService to an classificationDetail.
     * @param  cloudService  supplied CloudService
     * @return  classificationDetail equivalent to cloudService
     */
    static public Classification mapBeanToOmrs(org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.CloudService cloudService) {
            Classification omrsClassification = new Classification();
            SystemAttributes systemAttributes = cloudService.getSystemAttributes();
            if (systemAttributes!=null) {
                   if (systemAttributes.getCreatedBy()!=null)
                        omrsClassification.setCreatedBy(systemAttributes.getCreatedBy());
                   if (systemAttributes.getUpdatedBy()!=null)
                        omrsClassification.setUpdatedBy(systemAttributes.getUpdatedBy());
                   if (systemAttributes.getCreateTime()!=null)
                        omrsClassification.setCreateTime(systemAttributes.getCreateTime());
                   if (systemAttributes.getUpdateTime()!=null)
                        omrsClassification.setUpdateTime(systemAttributes.getUpdateTime());
                   if (systemAttributes.getVersion()!=null)
                        omrsClassification.setVersion(systemAttributes.getVersion());
                   if (systemAttributes.getStatus()!=null) {
                        InstanceStatus instanceStatus = SubjectAreaUtils.convertStatusToStatusInstance(systemAttributes.getStatus());
                        omrsClassification.setStatus(instanceStatus);
                   }
            }

            InstanceProperties instanceProperties = new InstanceProperties();
            // primitives

            if (cloudService.getOfferingName()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(cloudService.getOfferingName());
                instanceProperties.setProperty("offeringName", primitivePropertyValue);
            }
            if (cloudService.getType()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(cloudService.getType());
                instanceProperties.setProperty("type", primitivePropertyValue);
            }
            omrsClassification.setProperties(instanceProperties);
            // set the type in the classification
            OMRSArchiveAccessor archiveAccessor = OMRSArchiveAccessor.getInstance();
            TypeDef typeDef = archiveAccessor.getEntityDefByName("CloudService");
            InstanceType template = SubjectAreaUtils.createTemplateFromTypeDef(typeDef);
            InstanceType instanceType = new InstanceType(template);
            omrsClassification.setType(instanceType);
            return omrsClassification;
    }

}
