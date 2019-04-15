/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceDefinitionMetric;
import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.fvt.opentypes.relationships.GovernanceDefinitionMetric.GovernanceDefinitionMetric;

import java.util.*;

/**
 * Static mapping methods to map between GovernanceDefinitionMetric and the omrs Relationship.
 */
public class GovernanceDefinitionMetricMapper {
       private static final Logger log = LoggerFactory.getLogger(GovernanceDefinitionMetricMapper.class);
       private static final String className = GovernanceDefinitionMetricMapper.class.getName();

       public static GovernanceDefinitionMetric mapOmrsRelationshipToGovernanceDefinitionMetric(Relationship omrsRelationship) {
                String methodName = "mapOmrsRelationshipToGovernanceDefinitionMetric";
               GovernanceDefinitionMetric governanceDefinitionMetric = new GovernanceDefinitionMetric(omrsRelationship);
               SystemAttributes systemAttributes = new SystemAttributes();
               systemAttributes.setStatus(omrsRelationship.getStatus());
               systemAttributes.setCreatedBy(omrsRelationship.getCreatedBy());
               systemAttributes.setUpdatedBy(omrsRelationship.getUpdatedBy());
               systemAttributes.setCreateTime(omrsRelationship.getCreateTime());
               systemAttributes.setUpdateTime(omrsRelationship.getUpdateTime());
               systemAttributes.setVersion(omrsRelationship.getVersion());
               systemAttributes.setGUID(omrsRelationship.getGUID());
               governanceDefinitionMetric.setGuid(omrsRelationship.getGUID());
               governanceDefinitionMetric.setSystemAttributes(systemAttributes);

               InstanceProperties omrsRelationshipProperties = omrsRelationship.getProperties();
               if (omrsRelationshipProperties !=null) {
                 omrsRelationshipProperties.setEffectiveFromTime(governanceDefinitionMetric.getEffectiveFromTime());
                 omrsRelationshipProperties.setEffectiveToTime(governanceDefinitionMetric.getEffectiveToTime());
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
                                                   if (GovernanceDefinitionMetric.ATTRIBUTE_NAMES_SET.contains(name)) {
                                                      if (name.equals("rationale")) {
                                                           governanceDefinitionMetric.setRationale((String)actualValue);
                                                      }
                                                   } else {
                                                       // put out the omrs value object
                                                       if (governanceDefinitionMetric.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            governanceDefinitionMetric.setExtraAttributes(extraAttributes);
                                                        }
                                                      governanceDefinitionMetric.getExtraAttributes().put(name, primitivePropertyValue);
                                                   }
                                                   break;
                                               case ENUM:
                                                   EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                                                   String symbolicName = enumPropertyValue.getSymbolicName();
                                                   if (GovernanceDefinitionMetric.ENUM_NAMES_SET.contains(name)) {
                                                   } else {
                                                       // put out the omrs value object
                                                        if (governanceDefinitionMetric.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            governanceDefinitionMetric.setExtraAttributes(extraAttributes);
                                                        }

                                                        governanceDefinitionMetric.getExtraAttributes().put(name, enumPropertyValue);
                                                    }
                       
                                                   break;
                                               case MAP:
                                                    if (governanceDefinitionMetric.MAP_NAMES_SET.contains(name)) {
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
               return governanceDefinitionMetric;
       }
       public static Relationship mapGovernanceDefinitionMetricToOmrsRelationship(GovernanceDefinitionMetric governanceDefinitionMetric) {
           Relationship omrsRelationship = Line.createOmrsRelationship(governanceDefinitionMetric);

           SystemAttributes systemAttributes = governanceDefinitionMetric.getSystemAttributes();
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
           entityOne.setGUID(governanceDefinitionMetric.getEntity1Guid());
           String type1 = governanceDefinitionMetric.getEntity1Type();
           InstanceType instancetype1 = new InstanceType();
           instancetype1.setTypeDefName(type1);
           entityOne.setType(instancetype1);
           //set proxy 2
           EntityProxy entityTwo = new EntityProxy();
           entityTwo.setGUID(governanceDefinitionMetric.getEntity2Guid());
           String type2 = governanceDefinitionMetric.getEntity2Type();
           InstanceType instancetype2 = new InstanceType();
           instancetype2.setTypeDefName(type2);
           entityTwo.setType(instancetype2);
           // set relationshipType
           InstanceType relationshipType = new InstanceType();
           relationshipType.setTypeDefGUID(governanceDefinitionMetric.getTypeDefGuid());
           relationshipType.setTypeDefName("GovernanceDefinitionMetric");
           omrsRelationship.setType(relationshipType);           
   
           omrsRelationship.setEntityOneProxy(entityOne);
           omrsRelationship.setEntityTwoProxy(entityTwo);           
           if (omrsRelationship.getGUID() == null) {
               omrsRelationship.setGUID(governanceDefinitionMetric.getGuid());
           }

           InstanceProperties instanceProperties = new InstanceProperties();
           // primitives

            if (governanceDefinitionMetric.getRationale()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(governanceDefinitionMetric.getRationale());
                instanceProperties.setProperty("rationale", primitivePropertyValue);
            }
            omrsRelationship.setProperties(instanceProperties);

           return omrsRelationship;
       }
}
