/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.BusinessCapabilityControls;
import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.fvt.opentypes.relationships.BusinessCapabilityControls.BusinessCapabilityControls;

import java.util.*;

/**
 * Static mapping methods to map between BusinessCapabilityControls and the omrs Relationship.
 */
public class BusinessCapabilityControlsMapper {
       private static final Logger log = LoggerFactory.getLogger(BusinessCapabilityControlsMapper.class);
       private static final String className = BusinessCapabilityControlsMapper.class.getName();

       public static BusinessCapabilityControls mapOmrsRelationshipToBusinessCapabilityControls(Relationship omrsRelationship) {
                String methodName = "mapOmrsRelationshipToBusinessCapabilityControls";
               BusinessCapabilityControls businessCapabilityControls = new BusinessCapabilityControls(omrsRelationship);
               SystemAttributes systemAttributes = new SystemAttributes();
               systemAttributes.setStatus(omrsRelationship.getStatus());
               systemAttributes.setCreatedBy(omrsRelationship.getCreatedBy());
               systemAttributes.setUpdatedBy(omrsRelationship.getUpdatedBy());
               systemAttributes.setCreateTime(omrsRelationship.getCreateTime());
               systemAttributes.setUpdateTime(omrsRelationship.getUpdateTime());
               systemAttributes.setVersion(omrsRelationship.getVersion());
               systemAttributes.setGUID(omrsRelationship.getGUID());
               businessCapabilityControls.setGuid(omrsRelationship.getGUID());
               businessCapabilityControls.setSystemAttributes(systemAttributes);

               InstanceProperties omrsRelationshipProperties = omrsRelationship.getProperties();
               if (omrsRelationshipProperties !=null) {
                 omrsRelationshipProperties.setEffectiveFromTime(businessCapabilityControls.getEffectiveFromTime());
                 omrsRelationshipProperties.setEffectiveToTime(businessCapabilityControls.getEffectiveToTime());
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
                                                   if (BusinessCapabilityControls.ATTRIBUTE_NAMES_SET.contains(name)) {
                                                      if (name.equals("rationale")) {
                                                           businessCapabilityControls.setRationale((String)actualValue);
                                                      }
                                                   } else {
                                                       // put out the omrs value object
                                                       if (businessCapabilityControls.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            businessCapabilityControls.setExtraAttributes(extraAttributes);
                                                        }
                                                      businessCapabilityControls.getExtraAttributes().put(name, primitivePropertyValue);
                                                   }
                                                   break;
                                               case ENUM:
                                                   EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                                                   String symbolicName = enumPropertyValue.getSymbolicName();
                                                   if (BusinessCapabilityControls.ENUM_NAMES_SET.contains(name)) {
                                                   } else {
                                                       // put out the omrs value object
                                                        if (businessCapabilityControls.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            businessCapabilityControls.setExtraAttributes(extraAttributes);
                                                        }

                                                        businessCapabilityControls.getExtraAttributes().put(name, enumPropertyValue);
                                                    }
                       
                                                   break;
                                               case MAP:
                                                    if (businessCapabilityControls.MAP_NAMES_SET.contains(name)) {
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
               return businessCapabilityControls;
       }
       public static Relationship mapBusinessCapabilityControlsToOmrsRelationship(BusinessCapabilityControls businessCapabilityControls) {
           Relationship omrsRelationship = Line.createOmrsRelationship(businessCapabilityControls);

           SystemAttributes systemAttributes = businessCapabilityControls.getSystemAttributes();
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
           entityOne.setGUID(businessCapabilityControls.getEntity1Guid());
           String type1 = businessCapabilityControls.getEntity1Type();
           InstanceType instancetype1 = new InstanceType();
           instancetype1.setTypeDefName(type1);
           entityOne.setType(instancetype1);
           //set proxy 2
           EntityProxy entityTwo = new EntityProxy();
           entityTwo.setGUID(businessCapabilityControls.getEntity2Guid());
           String type2 = businessCapabilityControls.getEntity2Type();
           InstanceType instancetype2 = new InstanceType();
           instancetype2.setTypeDefName(type2);
           entityTwo.setType(instancetype2);
           // set relationshipType
           InstanceType relationshipType = new InstanceType();
           relationshipType.setTypeDefGUID(businessCapabilityControls.getTypeDefGuid());
           relationshipType.setTypeDefName("BusinessCapabilityControls");
           omrsRelationship.setType(relationshipType);           
   
           omrsRelationship.setEntityOneProxy(entityOne);
           omrsRelationship.setEntityTwoProxy(entityTwo);           
           if (omrsRelationship.getGUID() == null) {
               omrsRelationship.setGUID(businessCapabilityControls.getGuid());
           }

           InstanceProperties instanceProperties = new InstanceProperties();
           // primitives

            if (businessCapabilityControls.getRationale()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(businessCapabilityControls.getRationale());
                instanceProperties.setProperty("rationale", primitivePropertyValue);
            }
            omrsRelationship.setProperties(instanceProperties);

           return omrsRelationship;
       }
}
