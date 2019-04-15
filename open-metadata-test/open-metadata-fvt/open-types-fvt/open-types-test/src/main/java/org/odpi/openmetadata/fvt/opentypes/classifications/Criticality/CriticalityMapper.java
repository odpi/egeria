/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.classifications.Criticality;

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
 * Static mapping methods to map between Criticality and the omrs equivalents.
 */
public class CriticalityMapper {
    private static final Logger log = LoggerFactory.getLogger( CriticalityMapper.class);
    private static final String className = CriticalityMapper.class.getName();
   /**
    * @param omrsClassification - the supplied omrs classification
    * @return equivalent Criticality
    */
   static public Criticality mapOmrsToBean(Classification omrsClassification){
        String classificationTypeName = omrsClassification.getName();
        if ("Criticality".equals(classificationTypeName)) {
                Criticality criticality = new Criticality();
                //set core attributes
                SystemAttributes systemAttributes = new SystemAttributes();

                systemAttributes.setStatus(omrsClassification.getStatus());
                systemAttributes.setCreatedBy(omrsClassification.getCreatedBy());
                systemAttributes.setUpdatedBy(omrsClassification.getUpdatedBy());
                systemAttributes.setCreateTime(omrsClassification.getCreateTime());
                systemAttributes.setUpdateTime(omrsClassification.getUpdateTime());
                systemAttributes.setVersion(omrsClassification.getVersion());
                criticality.setSystemAttributes(systemAttributes);


                // Set properties
                InstanceProperties omrsClassificationProperties = omrsClassification.getProperties();
                if (omrsClassificationProperties !=null) {
                  omrsClassificationProperties.setEffectiveFromTime(criticality.getEffectiveFromTime());
                  omrsClassificationProperties.setEffectiveToTime(criticality.getEffectiveToTime());
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
                            if (criticality.ATTRIBUTE_NAMES_SET.contains(name)) {
                               if (name.equals("confidence")) {
                                  criticality.setConfidence((Integer)actualValue);
                               }
                               if (name.equals("steward")) {
                                  criticality.setSteward((String)actualValue);
                               }
                               if (name.equals("source")) {
                                  criticality.setSource((String)actualValue);
                               }
                               if (name.equals("notes")) {
                                  criticality.setNotes((String)actualValue);
                               }
                            } else {
                                // put out the omrs value object
                                if (null==criticality.getExtraAttributes())  {
                                     criticality.setExtraAttributes(new HashMap<String, Object>());
                                }
                               criticality.getExtraAttributes().put(name, primitivePropertyValue);
                            }
                            break;
                        case ENUM:
                            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                            String symbolicName = enumPropertyValue.getSymbolicName();
                            if (criticality.ENUM_NAMES_SET.contains(name)) {
                                 if (name.equals("status")) {
                                       GovernanceClassificationStatus status = GovernanceClassificationStatus.valueOf(symbolicName);
                                      criticality.setStatus(status);
                                 }
                                 if (name.equals("level")) {
                                       CriticalityLevel level = CriticalityLevel.valueOf(symbolicName);
                                      criticality.setLevel(level);
                                 }
                            } else {
                                // put out the omrs value object
                                if (null==criticality.getExtraAttributes())  {
                                     criticality.setExtraAttributes(new HashMap<String, Object>());
                                }
                                 criticality.getExtraAttributes().put(name, enumPropertyValue);
                             }

                            break;
                        case MAP:
                            if (criticality.MAP_NAMES_SET.contains(name)) {
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
                return criticality;
            } else {
                // TODO wrong type
            }
            return null;
    }
    /**
     * Map (convert) the supplied Criticality to an omrs Classification.
     * @param  criticality  supplied Criticality
     * @return  classificationDetail equivalent to criticality
     */
    static public Classification mapBeanToOmrs(Criticality criticality) {
            Classification omrsClassification = new Classification();
            SystemAttributes systemAttributes = criticality.getSystemAttributes();
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

            if (criticality.getConfidence()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
                primitivePropertyValue.setPrimitiveValue(criticality.getConfidence());
                instanceProperties.setProperty("confidence", primitivePropertyValue);
            }
            if (criticality.getSteward()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(criticality.getSteward());
                instanceProperties.setProperty("steward", primitivePropertyValue);
            }
            if (criticality.getSource()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(criticality.getSource());
                instanceProperties.setProperty("source", primitivePropertyValue);
            }
            if (criticality.getNotes()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(criticality.getNotes());
                instanceProperties.setProperty("notes", primitivePropertyValue);
            }
            if (criticality.getStatus()!=null) {
                GovernanceClassificationStatus enumType = criticality.getStatus();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("status", enumPropertyValue);
            }
            if (criticality.getLevel()!=null) {
                CriticalityLevel enumType = criticality.getLevel();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("level", enumPropertyValue);
            }
            omrsClassification.setProperties(instanceProperties);
            // set the type in the classification
            OMRSArchiveAccessor archiveAccessor = OMRSArchiveAccessor.getInstance();
            TypeDef typeDef = archiveAccessor.getEntityDefByName("Criticality");
            InstanceType template =  archiveAccessor.createTemplateFromTypeDef(typeDef);
            InstanceType instanceType = new InstanceType(template);
            omrsClassification.setType(instanceType);
            return omrsClassification;
    }

}
