/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.classifications.CloudTenant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

// omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.archivemanager.OMRSArchiveAccessor;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.enums.*;

/**
 * Static mapping methods to map between CloudTenant and the omrs equivalents.
 */
public class CloudTenantMapper {
    private static final Logger log = LoggerFactory.getLogger( CloudTenantMapper.class);
    private static final String className = CloudTenantMapper.class.getName();
   /**
    * @param omrsClassification - the supplied omrs classification
    * @return equivalent CloudTenant
    */
   static public CloudTenant mapOmrsToBean(Classification omrsClassification){
        String classificationTypeName = omrsClassification.getName();
        if ("CloudTenant".equals(classificationTypeName)) {
                CloudTenant cloudTenant = new CloudTenant();
                //set core attributes
                SystemAttributes systemAttributes = new SystemAttributes();

                systemAttributes.setStatus(omrsClassification.getStatus());
                systemAttributes.setCreatedBy(omrsClassification.getCreatedBy());
                systemAttributes.setUpdatedBy(omrsClassification.getUpdatedBy());
                systemAttributes.setCreateTime(omrsClassification.getCreateTime());
                systemAttributes.setUpdateTime(omrsClassification.getUpdateTime());
                systemAttributes.setVersion(omrsClassification.getVersion());
                cloudTenant.setSystemAttributes(systemAttributes);


                // Set properties
                InstanceProperties omrsClassificationProperties = omrsClassification.getProperties();
                if (omrsClassificationProperties !=null) {
                  omrsClassificationProperties.setEffectiveFromTime(cloudTenant.getEffectiveFromTime());
                  omrsClassificationProperties.setEffectiveToTime(cloudTenant.getEffectiveToTime());
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
                            if (cloudTenant.ATTRIBUTE_NAMES_SET.contains(name)) {
                               if (name.equals("tenantName")) {
                                  cloudTenant.setTenantName((String)actualValue);
                               }
                               if (name.equals("type")) {
                                  cloudTenant.setType((String)actualValue);
                               }
                            } else {
                                // put out the omrs value object
                                if (null==cloudTenant.getExtraAttributes())  {
                                     cloudTenant.setExtraAttributes(new HashMap<String, Object>());
                                }
                               cloudTenant.getExtraAttributes().put(name, primitivePropertyValue);
                            }
                            break;
                        case ENUM:
                            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                            String symbolicName = enumPropertyValue.getSymbolicName();
                            if (cloudTenant.ENUM_NAMES_SET.contains(name)) {
                            } else {
                                // put out the omrs value object
                                if (null==cloudTenant.getExtraAttributes())  {
                                     cloudTenant.setExtraAttributes(new HashMap<String, Object>());
                                }
                                 cloudTenant.getExtraAttributes().put(name, enumPropertyValue);
                             }

                            break;
                        case MAP:
                            if (cloudTenant.MAP_NAMES_SET.contains(name)) {
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
                }
                return cloudTenant;
            } else {
                // TODO wrong type
            }
            return null;
    }
    /**
     * Map (convert) the supplied CloudTenant to an omrs Classification.
     * @param  cloudTenant  supplied CloudTenant
     * @return  classificationDetail equivalent to cloudTenant
     */
    static public Classification mapBeanToOmrs(CloudTenant cloudTenant) {
            Classification omrsClassification = new Classification();
            SystemAttributes systemAttributes = cloudTenant.getSystemAttributes();
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
                        omrsClassification.setStatus(systemAttributes.getStatus());
                   }
            }

            InstanceProperties instanceProperties = new InstanceProperties();
            // primitives

            if (cloudTenant.getTenantName()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(cloudTenant.getTenantName());
                instanceProperties.setProperty("tenantName", primitivePropertyValue);
            }
            if (cloudTenant.getType()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(cloudTenant.getType());
                instanceProperties.setProperty("type", primitivePropertyValue);
            }
            omrsClassification.setProperties(instanceProperties);
            // set the type in the classification
            OMRSArchiveAccessor archiveAccessor = OMRSArchiveAccessor.getInstance();
            TypeDef typeDef = archiveAccessor.getEntityDefByName("CloudTenant");
            InstanceType template =  archiveAccessor.createTemplateFromTypeDef(typeDef);
            InstanceType instanceType = new InstanceType(template);
            omrsClassification.setType(instanceType);
            return omrsClassification;
    }

}
