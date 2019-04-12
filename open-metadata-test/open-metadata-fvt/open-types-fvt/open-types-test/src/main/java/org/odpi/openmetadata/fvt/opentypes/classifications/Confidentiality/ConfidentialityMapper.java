/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.classifications.Confidentiality;

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
 * Static mapping methods to map between Confidentiality and the omrs equivalents.
 */
public class ConfidentialityMapper {
    private static final Logger log = LoggerFactory.getLogger( ConfidentialityMapper.class);
    private static final String className = ConfidentialityMapper.class.getName();
   /**
    * @param omrsClassification - the supplied omrs classification
    * @return equivalent Confidentiality
    */
   static public Confidentiality mapOmrsToBean(Classification omrsClassification){
        String classificationTypeName = omrsClassification.getName();
        if ("Confidentiality".equals(classificationTypeName)) {
                Confidentiality confidentiality = new Confidentiality();
                //set core attributes
                SystemAttributes systemAttributes = new SystemAttributes();

                systemAttributes.setStatus(omrsClassification.getStatus());
                systemAttributes.setCreatedBy(omrsClassification.getCreatedBy());
                systemAttributes.setUpdatedBy(omrsClassification.getUpdatedBy());
                systemAttributes.setCreateTime(omrsClassification.getCreateTime());
                systemAttributes.setUpdateTime(omrsClassification.getUpdateTime());
                systemAttributes.setVersion(omrsClassification.getVersion());
                confidentiality.setSystemAttributes(systemAttributes);


                // Set properties
                InstanceProperties omrsClassificationProperties = omrsClassification.getProperties();
                if (omrsClassificationProperties !=null) {
                  omrsClassificationProperties.setEffectiveFromTime(confidentiality.getEffectiveFromTime());
                  omrsClassificationProperties.setEffectiveToTime(confidentiality.getEffectiveToTime());
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
                            if (confidentiality.ATTRIBUTE_NAMES_SET.contains(name)) {
                               if (name.equals("confidence")) {
                                  confidentiality.setConfidence((Integer)actualValue);
                               }
                               if (name.equals("steward")) {
                                  confidentiality.setSteward((String)actualValue);
                               }
                               if (name.equals("source")) {
                                  confidentiality.setSource((String)actualValue);
                               }
                               if (name.equals("notes")) {
                                  confidentiality.setNotes((String)actualValue);
                               }
                            } else {
                                // put out the omrs value object
                                if (null==confidentiality.getExtraAttributes())  {
                                     confidentiality.setExtraAttributes(new HashMap<String, Object>());
                                }
                               confidentiality.getExtraAttributes().put(name, primitivePropertyValue);
                            }
                            break;
                        case ENUM:
                            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                            String symbolicName = enumPropertyValue.getSymbolicName();
                            if (confidentiality.ENUM_NAMES_SET.contains(name)) {
                                 if (name.equals("status")) {
                                       GovernanceClassificationStatus status = GovernanceClassificationStatus.valueOf(symbolicName);
                                      confidentiality.setStatus(status);
                                 }
                                 if (name.equals("level")) {
                                       ConfidentialityLevel level = ConfidentialityLevel.valueOf(symbolicName);
                                      confidentiality.setLevel(level);
                                 }
                            } else {
                                // put out the omrs value object
                                if (null==confidentiality.getExtraAttributes())  {
                                     confidentiality.setExtraAttributes(new HashMap<String, Object>());
                                }
                                 confidentiality.getExtraAttributes().put(name, enumPropertyValue);
                             }

                            break;
                        case MAP:
                            if (confidentiality.MAP_NAMES_SET.contains(name)) {
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
                return confidentiality;
            } else {
                // TODO wrong type
            }
            return null;
    }
    /**
     * Map (convert) the supplied Confidentiality to an omrs Classification.
     * @param  confidentiality  supplied Confidentiality
     * @return  classificationDetail equivalent to confidentiality
     */
    static public Classification mapBeanToOmrs(Confidentiality confidentiality) {
            Classification omrsClassification = new Classification();
            SystemAttributes systemAttributes = confidentiality.getSystemAttributes();
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

            if (confidentiality.getConfidence()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
                primitivePropertyValue.setPrimitiveValue(confidentiality.getConfidence());
                instanceProperties.setProperty("confidence", primitivePropertyValue);
            }
            if (confidentiality.getSteward()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(confidentiality.getSteward());
                instanceProperties.setProperty("steward", primitivePropertyValue);
            }
            if (confidentiality.getSource()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(confidentiality.getSource());
                instanceProperties.setProperty("source", primitivePropertyValue);
            }
            if (confidentiality.getNotes()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(confidentiality.getNotes());
                instanceProperties.setProperty("notes", primitivePropertyValue);
            }
            if (confidentiality.getStatus()!=null) {
                GovernanceClassificationStatus enumType = confidentiality.getStatus();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("status", enumPropertyValue);
            }
            if (confidentiality.getLevel()!=null) {
                ConfidentialityLevel enumType = confidentiality.getLevel();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("level", enumPropertyValue);
            }
            omrsClassification.setProperties(instanceProperties);
            // set the type in the classification
            OMRSArchiveAccessor archiveAccessor = OMRSArchiveAccessor.getInstance();
            TypeDef typeDef = archiveAccessor.getEntityDefByName("Confidentiality");
            InstanceType template =  archiveAccessor.createTemplateFromTypeDef(typeDef);
            InstanceType instanceType = new InstanceType(template);
            omrsClassification.setType(instanceType);
            return omrsClassification;
    }

}
