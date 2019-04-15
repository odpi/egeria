/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.ReplacementTerm;
import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.fvt.opentypes.relationships.ReplacementTerm.ReplacementTerm;

import java.util.*;

/**
 * Static mapping methods to map between ReplacementTerm and the omrs Relationship.
 */
public class ReplacementTermMapper {
       private static final Logger log = LoggerFactory.getLogger(ReplacementTermMapper.class);
       private static final String className = ReplacementTermMapper.class.getName();

       public static ReplacementTerm mapOmrsRelationshipToReplacementTerm(Relationship omrsRelationship) {
                String methodName = "mapOmrsRelationshipToReplacementTerm";
               ReplacementTerm replacementTerm = new ReplacementTerm(omrsRelationship);
               SystemAttributes systemAttributes = new SystemAttributes();
               systemAttributes.setStatus(omrsRelationship.getStatus());
               systemAttributes.setCreatedBy(omrsRelationship.getCreatedBy());
               systemAttributes.setUpdatedBy(omrsRelationship.getUpdatedBy());
               systemAttributes.setCreateTime(omrsRelationship.getCreateTime());
               systemAttributes.setUpdateTime(omrsRelationship.getUpdateTime());
               systemAttributes.setVersion(omrsRelationship.getVersion());
               systemAttributes.setGUID(omrsRelationship.getGUID());
               replacementTerm.setGuid(omrsRelationship.getGUID());
               replacementTerm.setSystemAttributes(systemAttributes);

               InstanceProperties omrsRelationshipProperties = omrsRelationship.getProperties();
               if (omrsRelationshipProperties !=null) {
                 omrsRelationshipProperties.setEffectiveFromTime(replacementTerm.getEffectiveFromTime());
                 omrsRelationshipProperties.setEffectiveToTime(replacementTerm.getEffectiveToTime());
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
                                                   if (ReplacementTerm.ATTRIBUTE_NAMES_SET.contains(name)) {
                                                      if (name.equals("description")) {
                                                           replacementTerm.setDescription((String)actualValue);
                                                      }
                                                      if (name.equals("expression")) {
                                                           replacementTerm.setExpression((String)actualValue);
                                                      }
                                                      if (name.equals("steward")) {
                                                           replacementTerm.setSteward((String)actualValue);
                                                      }
                                                      if (name.equals("source")) {
                                                           replacementTerm.setSource((String)actualValue);
                                                      }
                                                   } else {
                                                       // put out the omrs value object
                                                       if (replacementTerm.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            replacementTerm.setExtraAttributes(extraAttributes);
                                                        }
                                                      replacementTerm.getExtraAttributes().put(name, primitivePropertyValue);
                                                   }
                                                   break;
                                               case ENUM:
                                                   EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                                                   String symbolicName = enumPropertyValue.getSymbolicName();
                                                   if (ReplacementTerm.ENUM_NAMES_SET.contains(name)) {
                                                        if (name.equals("status")) {
                                                              TermRelationshipStatus status = TermRelationshipStatus.valueOf(symbolicName);
                                                             replacementTerm.setStatus(status);
                                                        }
                                                   } else {
                                                       // put out the omrs value object
                                                        if (replacementTerm.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            replacementTerm.setExtraAttributes(extraAttributes);
                                                        }

                                                        replacementTerm.getExtraAttributes().put(name, enumPropertyValue);
                                                    }
                       
                                                   break;
                                               case MAP:
                                                    if (replacementTerm.MAP_NAMES_SET.contains(name)) {
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
               return replacementTerm;
       }
       public static Relationship mapReplacementTermToOmrsRelationship(ReplacementTerm replacementTerm) {
           Relationship omrsRelationship = Line.createOmrsRelationship(replacementTerm);

           SystemAttributes systemAttributes = replacementTerm.getSystemAttributes();
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
           entityOne.setGUID(replacementTerm.getEntity1Guid());
           String type1 = replacementTerm.getEntity1Type();
           InstanceType instancetype1 = new InstanceType();
           instancetype1.setTypeDefName(type1);
           entityOne.setType(instancetype1);
           //set proxy 2
           EntityProxy entityTwo = new EntityProxy();
           entityTwo.setGUID(replacementTerm.getEntity2Guid());
           String type2 = replacementTerm.getEntity2Type();
           InstanceType instancetype2 = new InstanceType();
           instancetype2.setTypeDefName(type2);
           entityTwo.setType(instancetype2);
           // set relationshipType
           InstanceType relationshipType = new InstanceType();
           relationshipType.setTypeDefGUID(replacementTerm.getTypeDefGuid());
           relationshipType.setTypeDefName("ReplacementTerm");
           omrsRelationship.setType(relationshipType);           
   
           omrsRelationship.setEntityOneProxy(entityOne);
           omrsRelationship.setEntityTwoProxy(entityTwo);           
           if (omrsRelationship.getGUID() == null) {
               omrsRelationship.setGUID(replacementTerm.getGuid());
           }

           InstanceProperties instanceProperties = new InstanceProperties();
           // primitives

            if (replacementTerm.getDescription()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(replacementTerm.getDescription());
                instanceProperties.setProperty("description", primitivePropertyValue);
            }
            if (replacementTerm.getExpression()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(replacementTerm.getExpression());
                instanceProperties.setProperty("expression", primitivePropertyValue);
            }
            if (replacementTerm.getSteward()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(replacementTerm.getSteward());
                instanceProperties.setProperty("steward", primitivePropertyValue);
            }
            if (replacementTerm.getSource()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(replacementTerm.getSource());
                instanceProperties.setProperty("source", primitivePropertyValue);
            }
            if (replacementTerm.getStatus()!=null) {
                TermRelationshipStatus enumType = replacementTerm.getStatus();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("status", enumPropertyValue);
            }
            omrsRelationship.setProperties(instanceProperties);

           return omrsRelationship;
       }
}
