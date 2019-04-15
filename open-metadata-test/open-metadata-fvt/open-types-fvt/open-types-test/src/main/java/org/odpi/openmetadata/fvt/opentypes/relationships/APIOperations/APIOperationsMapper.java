/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.APIOperations;
import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.fvt.opentypes.relationships.APIOperations.APIOperations;

import java.util.*;

/**
 * Static mapping methods to map between APIOperations and the omrs Relationship.
 */
public class APIOperationsMapper {
       private static final Logger log = LoggerFactory.getLogger(APIOperationsMapper.class);
       private static final String className = APIOperationsMapper.class.getName();

       public static APIOperations mapOmrsRelationshipToAPIOperations(Relationship omrsRelationship) {
                String methodName = "mapOmrsRelationshipToAPIOperations";
               APIOperations aPIOperations = new APIOperations(omrsRelationship);
               SystemAttributes systemAttributes = new SystemAttributes();
               systemAttributes.setStatus(omrsRelationship.getStatus());
               systemAttributes.setCreatedBy(omrsRelationship.getCreatedBy());
               systemAttributes.setUpdatedBy(omrsRelationship.getUpdatedBy());
               systemAttributes.setCreateTime(omrsRelationship.getCreateTime());
               systemAttributes.setUpdateTime(omrsRelationship.getUpdateTime());
               systemAttributes.setVersion(omrsRelationship.getVersion());
               systemAttributes.setGUID(omrsRelationship.getGUID());
               aPIOperations.setGuid(omrsRelationship.getGUID());
               aPIOperations.setSystemAttributes(systemAttributes);

               InstanceProperties omrsRelationshipProperties = omrsRelationship.getProperties();
               if (omrsRelationshipProperties !=null) {
                 omrsRelationshipProperties.setEffectiveFromTime(aPIOperations.getEffectiveFromTime());
                 omrsRelationshipProperties.setEffectiveToTime(aPIOperations.getEffectiveToTime());
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
                                                   if (APIOperations.ATTRIBUTE_NAMES_SET.contains(name)) {
                                                   } else {
                                                       // put out the omrs value object
                                                       if (aPIOperations.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            aPIOperations.setExtraAttributes(extraAttributes);
                                                        }
                                                      aPIOperations.getExtraAttributes().put(name, primitivePropertyValue);
                                                   }
                                                   break;
                                               case ENUM:
                                                   EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                                                   String symbolicName = enumPropertyValue.getSymbolicName();
                                                   if (APIOperations.ENUM_NAMES_SET.contains(name)) {
                                                   } else {
                                                       // put out the omrs value object
                                                        if (aPIOperations.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            aPIOperations.setExtraAttributes(extraAttributes);
                                                        }

                                                        aPIOperations.getExtraAttributes().put(name, enumPropertyValue);
                                                    }
                       
                                                   break;
                                               case MAP:
                                                    if (aPIOperations.MAP_NAMES_SET.contains(name)) {
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
               return aPIOperations;
       }
       public static Relationship mapAPIOperationsToOmrsRelationship(APIOperations aPIOperations) {
           Relationship omrsRelationship = Line.createOmrsRelationship(aPIOperations);

           SystemAttributes systemAttributes = aPIOperations.getSystemAttributes();
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
           entityOne.setGUID(aPIOperations.getEntity1Guid());
           String type1 = aPIOperations.getEntity1Type();
           InstanceType instancetype1 = new InstanceType();
           instancetype1.setTypeDefName(type1);
           entityOne.setType(instancetype1);
           //set proxy 2
           EntityProxy entityTwo = new EntityProxy();
           entityTwo.setGUID(aPIOperations.getEntity2Guid());
           String type2 = aPIOperations.getEntity2Type();
           InstanceType instancetype2 = new InstanceType();
           instancetype2.setTypeDefName(type2);
           entityTwo.setType(instancetype2);
           // set relationshipType
           InstanceType relationshipType = new InstanceType();
           relationshipType.setTypeDefGUID(aPIOperations.getTypeDefGuid());
           relationshipType.setTypeDefName("APIOperations");
           omrsRelationship.setType(relationshipType);           
   
           omrsRelationship.setEntityOneProxy(entityOne);
           omrsRelationship.setEntityTwoProxy(entityTwo);           
           if (omrsRelationship.getGUID() == null) {
               omrsRelationship.setGUID(aPIOperations.getGuid());
           }

           InstanceProperties instanceProperties = new InstanceProperties();
           // primitives

            omrsRelationship.setProperties(instanceProperties);

           return omrsRelationship;
       }
}
