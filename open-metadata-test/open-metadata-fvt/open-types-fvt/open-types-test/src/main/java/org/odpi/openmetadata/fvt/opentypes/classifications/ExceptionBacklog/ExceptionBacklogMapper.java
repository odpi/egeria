/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.classifications.ExceptionBacklog;

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
 * Static mapping methods to map between ExceptionBacklog and the omrs equivalents.
 */
public class ExceptionBacklogMapper {
    private static final Logger log = LoggerFactory.getLogger( ExceptionBacklogMapper.class);
    private static final String className = ExceptionBacklogMapper.class.getName();
   /**
    * @param omrsClassification - the supplied omrs classification
    * @return equivalent ExceptionBacklog
    */
   static public ExceptionBacklog mapOmrsToBean(Classification omrsClassification){
        String classificationTypeName = omrsClassification.getName();
        if ("ExceptionBacklog".equals(classificationTypeName)) {
                ExceptionBacklog exceptionBacklog = new ExceptionBacklog();
                //set core attributes
                SystemAttributes systemAttributes = new SystemAttributes();

                systemAttributes.setStatus(omrsClassification.getStatus());
                systemAttributes.setCreatedBy(omrsClassification.getCreatedBy());
                systemAttributes.setUpdatedBy(omrsClassification.getUpdatedBy());
                systemAttributes.setCreateTime(omrsClassification.getCreateTime());
                systemAttributes.setUpdateTime(omrsClassification.getUpdateTime());
                systemAttributes.setVersion(omrsClassification.getVersion());
                exceptionBacklog.setSystemAttributes(systemAttributes);


                // Set properties
                InstanceProperties omrsClassificationProperties = omrsClassification.getProperties();
                if (omrsClassificationProperties !=null) {
                  omrsClassificationProperties.setEffectiveFromTime(exceptionBacklog.getEffectiveFromTime());
                  omrsClassificationProperties.setEffectiveToTime(exceptionBacklog.getEffectiveToTime());
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
                            if (exceptionBacklog.ATTRIBUTE_NAMES_SET.contains(name)) {
                               if (name.equals("notes")) {
                                  exceptionBacklog.setNotes((String)actualValue);
                               }
                               if (name.equals("steward")) {
                                  exceptionBacklog.setSteward((String)actualValue);
                               }
                               if (name.equals("process")) {
                                  exceptionBacklog.setProcess((String)actualValue);
                               }
                               if (name.equals("source")) {
                                  exceptionBacklog.setSource((String)actualValue);
                               }
                            } else {
                                // put out the omrs value object
                                if (null==exceptionBacklog.getExtraAttributes())  {
                                     exceptionBacklog.setExtraAttributes(new HashMap<String, Object>());
                                }
                               exceptionBacklog.getExtraAttributes().put(name, primitivePropertyValue);
                            }
                            break;
                        case ENUM:
                            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                            String symbolicName = enumPropertyValue.getSymbolicName();
                            if (exceptionBacklog.ENUM_NAMES_SET.contains(name)) {
                            } else {
                                // put out the omrs value object
                                if (null==exceptionBacklog.getExtraAttributes())  {
                                     exceptionBacklog.setExtraAttributes(new HashMap<String, Object>());
                                }
                                 exceptionBacklog.getExtraAttributes().put(name, enumPropertyValue);
                             }

                            break;
                        case MAP:
                            if (exceptionBacklog.MAP_NAMES_SET.contains(name)) {
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
                return exceptionBacklog;
            } else {
                // TODO wrong type
            }
            return null;
    }
    /**
     * Map (convert) the supplied ExceptionBacklog to an omrs Classification.
     * @param  exceptionBacklog  supplied ExceptionBacklog
     * @return  classificationDetail equivalent to exceptionBacklog
     */
    static public Classification mapBeanToOmrs(ExceptionBacklog exceptionBacklog) {
            Classification omrsClassification = new Classification();
            SystemAttributes systemAttributes = exceptionBacklog.getSystemAttributes();
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

            if (exceptionBacklog.getNotes()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(exceptionBacklog.getNotes());
                instanceProperties.setProperty("notes", primitivePropertyValue);
            }
            if (exceptionBacklog.getSteward()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(exceptionBacklog.getSteward());
                instanceProperties.setProperty("steward", primitivePropertyValue);
            }
            if (exceptionBacklog.getProcess()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(exceptionBacklog.getProcess());
                instanceProperties.setProperty("process", primitivePropertyValue);
            }
            if (exceptionBacklog.getSource()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(exceptionBacklog.getSource());
                instanceProperties.setProperty("source", primitivePropertyValue);
            }
            omrsClassification.setProperties(instanceProperties);
            // set the type in the classification
            OMRSArchiveAccessor archiveAccessor = OMRSArchiveAccessor.getInstance();
            TypeDef typeDef = archiveAccessor.getEntityDefByName("ExceptionBacklog");
            InstanceType template =  archiveAccessor.createTemplateFromTypeDef(typeDef);
            InstanceType instanceType = new InstanceType(template);
            omrsClassification.setType(instanceType);
            return omrsClassification;
    }

}
