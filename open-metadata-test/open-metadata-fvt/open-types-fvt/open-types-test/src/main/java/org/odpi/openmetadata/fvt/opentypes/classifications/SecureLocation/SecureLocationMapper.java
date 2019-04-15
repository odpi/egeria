/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.classifications.SecureLocation;

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
 * Static mapping methods to map between SecureLocation and the omrs equivalents.
 */
public class SecureLocationMapper {
    private static final Logger log = LoggerFactory.getLogger( SecureLocationMapper.class);
    private static final String className = SecureLocationMapper.class.getName();
   /**
    * @param omrsClassification - the supplied omrs classification
    * @return equivalent SecureLocation
    */
   static public SecureLocation mapOmrsToBean(Classification omrsClassification){
        String classificationTypeName = omrsClassification.getName();
        if ("SecureLocation".equals(classificationTypeName)) {
                SecureLocation secureLocation = new SecureLocation();
                //set core attributes
                SystemAttributes systemAttributes = new SystemAttributes();

                systemAttributes.setStatus(omrsClassification.getStatus());
                systemAttributes.setCreatedBy(omrsClassification.getCreatedBy());
                systemAttributes.setUpdatedBy(omrsClassification.getUpdatedBy());
                systemAttributes.setCreateTime(omrsClassification.getCreateTime());
                systemAttributes.setUpdateTime(omrsClassification.getUpdateTime());
                systemAttributes.setVersion(omrsClassification.getVersion());
                secureLocation.setSystemAttributes(systemAttributes);


                // Set properties
                InstanceProperties omrsClassificationProperties = omrsClassification.getProperties();
                if (omrsClassificationProperties !=null) {
                  omrsClassificationProperties.setEffectiveFromTime(secureLocation.getEffectiveFromTime());
                  omrsClassificationProperties.setEffectiveToTime(secureLocation.getEffectiveToTime());
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
                            if (secureLocation.ATTRIBUTE_NAMES_SET.contains(name)) {
                               if (name.equals("description")) {
                                  secureLocation.setDescription((String)actualValue);
                               }
                               if (name.equals("level")) {
                                  secureLocation.setLevel((String)actualValue);
                               }
                            } else {
                                // put out the omrs value object
                                if (null==secureLocation.getExtraAttributes())  {
                                     secureLocation.setExtraAttributes(new HashMap<String, Object>());
                                }
                               secureLocation.getExtraAttributes().put(name, primitivePropertyValue);
                            }
                            break;
                        case ENUM:
                            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                            String symbolicName = enumPropertyValue.getSymbolicName();
                            if (secureLocation.ENUM_NAMES_SET.contains(name)) {
                            } else {
                                // put out the omrs value object
                                if (null==secureLocation.getExtraAttributes())  {
                                     secureLocation.setExtraAttributes(new HashMap<String, Object>());
                                }
                                 secureLocation.getExtraAttributes().put(name, enumPropertyValue);
                             }

                            break;
                        case MAP:
                            if (secureLocation.MAP_NAMES_SET.contains(name)) {
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
                return secureLocation;
            } else {
                // TODO wrong type
            }
            return null;
    }
    /**
     * Map (convert) the supplied SecureLocation to an omrs Classification.
     * @param  secureLocation  supplied SecureLocation
     * @return  classificationDetail equivalent to secureLocation
     */
    static public Classification mapBeanToOmrs(SecureLocation secureLocation) {
            Classification omrsClassification = new Classification();
            SystemAttributes systemAttributes = secureLocation.getSystemAttributes();
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

            if (secureLocation.getDescription()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(secureLocation.getDescription());
                instanceProperties.setProperty("description", primitivePropertyValue);
            }
            if (secureLocation.getLevel()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(secureLocation.getLevel());
                instanceProperties.setProperty("level", primitivePropertyValue);
            }
            omrsClassification.setProperties(instanceProperties);
            // set the type in the classification
            OMRSArchiveAccessor archiveAccessor = OMRSArchiveAccessor.getInstance();
            TypeDef typeDef = archiveAccessor.getEntityDefByName("SecureLocation");
            InstanceType template =  archiveAccessor.createTemplateFromTypeDef(typeDef);
            InstanceType instanceType = new InstanceType(template);
            omrsClassification.setType(instanceType);
            return omrsClassification;
    }

}
