/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.DataClassAssignment;
import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.fvt.opentypes.relationships.DataClassAssignment.DataClassAssignment;

import java.util.*;

/**
 * Static mapping methods to map between DataClassAssignment and the omrs Relationship.
 */
public class DataClassAssignmentMapper {
       private static final Logger log = LoggerFactory.getLogger(DataClassAssignmentMapper.class);
       private static final String className = DataClassAssignmentMapper.class.getName();

       public static DataClassAssignment mapOmrsRelationshipToDataClassAssignment(Relationship omrsRelationship) {
                String methodName = "mapOmrsRelationshipToDataClassAssignment";
               DataClassAssignment dataClassAssignment = new DataClassAssignment(omrsRelationship);
               SystemAttributes systemAttributes = new SystemAttributes();
               systemAttributes.setStatus(omrsRelationship.getStatus());
               systemAttributes.setCreatedBy(omrsRelationship.getCreatedBy());
               systemAttributes.setUpdatedBy(omrsRelationship.getUpdatedBy());
               systemAttributes.setCreateTime(omrsRelationship.getCreateTime());
               systemAttributes.setUpdateTime(omrsRelationship.getUpdateTime());
               systemAttributes.setVersion(omrsRelationship.getVersion());
               systemAttributes.setGUID(omrsRelationship.getGUID());
               dataClassAssignment.setGuid(omrsRelationship.getGUID());
               dataClassAssignment.setSystemAttributes(systemAttributes);

               InstanceProperties omrsRelationshipProperties = omrsRelationship.getProperties();
               if (omrsRelationshipProperties !=null) {
                 omrsRelationshipProperties.setEffectiveFromTime(dataClassAssignment.getEffectiveFromTime());
                 omrsRelationshipProperties.setEffectiveToTime(dataClassAssignment.getEffectiveToTime());
                 Iterator omrsPropertyIterator = omrsRelationshipProperties.getPropertyNames();
                 while (omrsPropertyIterator.hasNext()) {
                    String name = (String) omrsPropertyIterator.next();
                    //TODO check if this is a property we expect or whether the type has been added to.
                    // this is a property we expect
                    InstancePropertyValue value = omrsRelationshipProperties.getPropertyValue(name);
                    // supplied guid matches the expected type
                    Object actualValue;
                    switch (value.getInstancePropertyCategory()) {
                                               case PRIMITIVE:
                                                   PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) value;
                                                   actualValue = primitivePropertyValue.getPrimitiveValue();
                                                   if (DataClassAssignment.ATTRIBUTE_NAMES_SET.contains(name)) {
                                                      if (name.equals("method")) {
                                                           dataClassAssignment.setMethod((String)actualValue);
                                                      }
                                                      if (name.equals("partialMatch")) {
                                                           dataClassAssignment.setPartialMatch((Boolean)actualValue);
                                                      }
                                                      if (name.equals("confidence")) {
                                                           dataClassAssignment.setConfidence((Integer)actualValue);
                                                      }
                                                      if (name.equals("threshold")) {
                                                           dataClassAssignment.setThreshold((Float)actualValue);
                                                      }
                                                      if (name.equals("valueFrequency")) {
                                                           dataClassAssignment.setValueFrequency((Long)actualValue);
                                                      }
                                                      if (name.equals("steward")) {
                                                           dataClassAssignment.setSteward((String)actualValue);
                                                      }
                                                      if (name.equals("source")) {
                                                           dataClassAssignment.setSource((String)actualValue);
                                                      }
                                                   } else {
                                                       // put out the omrs value object
                                                       if (dataClassAssignment.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            dataClassAssignment.setExtraAttributes(extraAttributes);
                                                        }
                                                      dataClassAssignment.getExtraAttributes().put(name, primitivePropertyValue);
                                                   }
                                                   break;
                                               case ENUM:
                                                   EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                                                   String symbolicName = enumPropertyValue.getSymbolicName();
                                                   if (DataClassAssignment.ENUM_NAMES_SET.contains(name)) {
                                                        if (name.equals("status")) {
                                                              DataClassAssignmentStatus status = DataClassAssignmentStatus.valueOf(symbolicName);
                                                             dataClassAssignment.setStatus(status);
                                                        }
                                                   } else {
                                                       // put out the omrs value object
                                                        if (dataClassAssignment.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            dataClassAssignment.setExtraAttributes(extraAttributes);
                                                        }

                                                        dataClassAssignment.getExtraAttributes().put(name, enumPropertyValue);
                                                    }
                       
                                                   break;
                                               case MAP:
                                                    if (dataClassAssignment.MAP_NAMES_SET.contains(name)) {
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
               return dataClassAssignment;
       }
       public static Relationship mapDataClassAssignmentToOmrsRelationship(DataClassAssignment dataClassAssignment) {
           Relationship omrsRelationship = Line.createOmrsRelationship(dataClassAssignment);

           SystemAttributes systemAttributes = dataClassAssignment.getSystemAttributes();
           if (systemAttributes!=null) {
               if (systemAttributes.getCreatedBy()!=null)
                   omrsRelationship.setCreatedBy(systemAttributes.getCreatedBy());
               if (systemAttributes.getUpdatedBy()!=null)
                   omrsRelationship.setUpdatedBy(systemAttributes.getUpdatedBy());
               if (systemAttributes.getCreateTime()!=null)
                   omrsRelationship.setCreateTime(systemAttributes.getCreateTime());
               if (systemAttributes.getUpdateTime()!=null)
                   omrsRelationship.setUpdateTime(systemAttributes.getUpdateTime());
               if (systemAttributes.getVersion()!=null)
                   omrsRelationship.setVersion(systemAttributes.getVersion());
                if (systemAttributes.getGUID()!=null)
                   omrsRelationship.setGUID(systemAttributes.getGUID());
               if (systemAttributes.getStatus()!=null)
                   omrsRelationship.setStatus(systemAttributes.getStatus());
           }
           //set proxy 1
           EntityProxy entityOne = new EntityProxy();
           entityOne.setGUID(dataClassAssignment.getEntity1Guid());
           String type1 = dataClassAssignment.getEntity1Type();
           InstanceType instancetype1 = new InstanceType();
           instancetype1.setTypeDefName(type1);
           entityOne.setType(instancetype1);
           //set proxy 2
           EntityProxy entityTwo = new EntityProxy();
           entityTwo.setGUID(dataClassAssignment.getEntity2Guid());
           String type2 = dataClassAssignment.getEntity2Type();
           InstanceType instancetype2 = new InstanceType();
           instancetype2.setTypeDefName(type2);
           entityTwo.setType(instancetype2);
           // set relationshipType
           InstanceType relationshipType = new InstanceType();
           relationshipType.setTypeDefGUID(dataClassAssignment.getTypeDefGuid());
           relationshipType.setTypeDefName("DataClassAssignment");
           omrsRelationship.setType(relationshipType);           
   
           omrsRelationship.setEntityOneProxy(entityOne);
           omrsRelationship.setEntityTwoProxy(entityTwo);           
           if (omrsRelationship.getGUID() == null) {
               omrsRelationship.setGUID(dataClassAssignment.getGuid());
           }

           InstanceProperties instanceProperties = new InstanceProperties();
           // primitives

            if (dataClassAssignment.getMethod()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataClassAssignment.getMethod());
                instanceProperties.setProperty("method", primitivePropertyValue);
            }
            if (dataClassAssignment.getPartialMatch()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN);
                primitivePropertyValue.setPrimitiveValue(dataClassAssignment.getPartialMatch());
                instanceProperties.setProperty("partialMatch", primitivePropertyValue);
            }
            if (dataClassAssignment.getConfidence()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
                primitivePropertyValue.setPrimitiveValue(dataClassAssignment.getConfidence());
                instanceProperties.setProperty("confidence", primitivePropertyValue);
            }
            if (dataClassAssignment.getThreshold()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_FLOAT);
                primitivePropertyValue.setPrimitiveValue(dataClassAssignment.getThreshold());
                instanceProperties.setProperty("threshold", primitivePropertyValue);
            }
            if (dataClassAssignment.getValueFrequency()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG);
                primitivePropertyValue.setPrimitiveValue(dataClassAssignment.getValueFrequency());
                instanceProperties.setProperty("valueFrequency", primitivePropertyValue);
            }
            if (dataClassAssignment.getSteward()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataClassAssignment.getSteward());
                instanceProperties.setProperty("steward", primitivePropertyValue);
            }
            if (dataClassAssignment.getSource()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(dataClassAssignment.getSource());
                instanceProperties.setProperty("source", primitivePropertyValue);
            }
            if (dataClassAssignment.getStatus()!=null) {
                DataClassAssignmentStatus enumType = dataClassAssignment.getStatus();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("status", enumPropertyValue);
            }
            omrsRelationship.setProperties(instanceProperties);

           return omrsRelationship;
       }
}
