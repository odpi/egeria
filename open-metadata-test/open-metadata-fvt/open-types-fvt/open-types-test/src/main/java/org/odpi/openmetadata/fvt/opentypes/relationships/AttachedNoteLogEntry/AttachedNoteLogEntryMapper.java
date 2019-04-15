/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.AttachedNoteLogEntry;
import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.fvt.opentypes.relationships.AttachedNoteLogEntry.AttachedNoteLogEntry;

import java.util.*;

/**
 * Static mapping methods to map between AttachedNoteLogEntry and the omrs Relationship.
 */
public class AttachedNoteLogEntryMapper {
       private static final Logger log = LoggerFactory.getLogger(AttachedNoteLogEntryMapper.class);
       private static final String className = AttachedNoteLogEntryMapper.class.getName();

       public static AttachedNoteLogEntry mapOmrsRelationshipToAttachedNoteLogEntry(Relationship omrsRelationship) {
                String methodName = "mapOmrsRelationshipToAttachedNoteLogEntry";
               AttachedNoteLogEntry attachedNoteLogEntry = new AttachedNoteLogEntry(omrsRelationship);
               SystemAttributes systemAttributes = new SystemAttributes();
               systemAttributes.setStatus(omrsRelationship.getStatus());
               systemAttributes.setCreatedBy(omrsRelationship.getCreatedBy());
               systemAttributes.setUpdatedBy(omrsRelationship.getUpdatedBy());
               systemAttributes.setCreateTime(omrsRelationship.getCreateTime());
               systemAttributes.setUpdateTime(omrsRelationship.getUpdateTime());
               systemAttributes.setVersion(omrsRelationship.getVersion());
               systemAttributes.setGUID(omrsRelationship.getGUID());
               attachedNoteLogEntry.setGuid(omrsRelationship.getGUID());
               attachedNoteLogEntry.setSystemAttributes(systemAttributes);

               InstanceProperties omrsRelationshipProperties = omrsRelationship.getProperties();
               if (omrsRelationshipProperties !=null) {
                 omrsRelationshipProperties.setEffectiveFromTime(attachedNoteLogEntry.getEffectiveFromTime());
                 omrsRelationshipProperties.setEffectiveToTime(attachedNoteLogEntry.getEffectiveToTime());
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
                                                   if (AttachedNoteLogEntry.ATTRIBUTE_NAMES_SET.contains(name)) {
                                                   } else {
                                                       // put out the omrs value object
                                                       if (attachedNoteLogEntry.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            attachedNoteLogEntry.setExtraAttributes(extraAttributes);
                                                        }
                                                      attachedNoteLogEntry.getExtraAttributes().put(name, primitivePropertyValue);
                                                   }
                                                   break;
                                               case ENUM:
                                                   EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                                                   String symbolicName = enumPropertyValue.getSymbolicName();
                                                   if (AttachedNoteLogEntry.ENUM_NAMES_SET.contains(name)) {
                                                   } else {
                                                       // put out the omrs value object
                                                        if (attachedNoteLogEntry.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            attachedNoteLogEntry.setExtraAttributes(extraAttributes);
                                                        }

                                                        attachedNoteLogEntry.getExtraAttributes().put(name, enumPropertyValue);
                                                    }
                       
                                                   break;
                                               case MAP:
                                                    if (attachedNoteLogEntry.MAP_NAMES_SET.contains(name)) {
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
               return attachedNoteLogEntry;
       }
       public static Relationship mapAttachedNoteLogEntryToOmrsRelationship(AttachedNoteLogEntry attachedNoteLogEntry) {
           Relationship omrsRelationship = Line.createOmrsRelationship(attachedNoteLogEntry);

           SystemAttributes systemAttributes = attachedNoteLogEntry.getSystemAttributes();
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
           entityOne.setGUID(attachedNoteLogEntry.getEntity1Guid());
           String type1 = attachedNoteLogEntry.getEntity1Type();
           InstanceType instancetype1 = new InstanceType();
           instancetype1.setTypeDefName(type1);
           entityOne.setType(instancetype1);
           //set proxy 2
           EntityProxy entityTwo = new EntityProxy();
           entityTwo.setGUID(attachedNoteLogEntry.getEntity2Guid());
           String type2 = attachedNoteLogEntry.getEntity2Type();
           InstanceType instancetype2 = new InstanceType();
           instancetype2.setTypeDefName(type2);
           entityTwo.setType(instancetype2);
           // set relationshipType
           InstanceType relationshipType = new InstanceType();
           relationshipType.setTypeDefGUID(attachedNoteLogEntry.getTypeDefGuid());
           relationshipType.setTypeDefName("AttachedNoteLogEntry");
           omrsRelationship.setType(relationshipType);           
   
           omrsRelationship.setEntityOneProxy(entityOne);
           omrsRelationship.setEntityTwoProxy(entityTwo);           
           if (omrsRelationship.getGUID() == null) {
               omrsRelationship.setGUID(attachedNoteLogEntry.getGuid());
           }

           InstanceProperties instanceProperties = new InstanceProperties();
           // primitives

            omrsRelationship.setProperties(instanceProperties);

           return omrsRelationship;
       }
}
