/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.SoftwareServerSupportedCapability;
import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.fvt.opentypes.relationships.SoftwareServerSupportedCapability.SoftwareServerSupportedCapability;

import java.util.*;

/**
 * Static mapping methods to map between SoftwareServerSupportedCapability and the omrs Relationship.
 */
public class SoftwareServerSupportedCapabilityMapper {
       private static final Logger log = LoggerFactory.getLogger(SoftwareServerSupportedCapabilityMapper.class);
       private static final String className = SoftwareServerSupportedCapabilityMapper.class.getName();

       public static SoftwareServerSupportedCapability mapOmrsRelationshipToSoftwareServerSupportedCapability(Relationship omrsRelationship) {
                String methodName = "mapOmrsRelationshipToSoftwareServerSupportedCapability";
               SoftwareServerSupportedCapability softwareServerSupportedCapability = new SoftwareServerSupportedCapability(omrsRelationship);
               SystemAttributes systemAttributes = new SystemAttributes();
               systemAttributes.setStatus(omrsRelationship.getStatus());
               systemAttributes.setCreatedBy(omrsRelationship.getCreatedBy());
               systemAttributes.setUpdatedBy(omrsRelationship.getUpdatedBy());
               systemAttributes.setCreateTime(omrsRelationship.getCreateTime());
               systemAttributes.setUpdateTime(omrsRelationship.getUpdateTime());
               systemAttributes.setVersion(omrsRelationship.getVersion());
               systemAttributes.setGUID(omrsRelationship.getGUID());
               softwareServerSupportedCapability.setGuid(omrsRelationship.getGUID());
               softwareServerSupportedCapability.setSystemAttributes(systemAttributes);

               InstanceProperties omrsRelationshipProperties = omrsRelationship.getProperties();
               if (omrsRelationshipProperties !=null) {
                 omrsRelationshipProperties.setEffectiveFromTime(softwareServerSupportedCapability.getEffectiveFromTime());
                 omrsRelationshipProperties.setEffectiveToTime(softwareServerSupportedCapability.getEffectiveToTime());
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
                                                   if (SoftwareServerSupportedCapability.ATTRIBUTE_NAMES_SET.contains(name)) {
                                                      if (name.equals("deploymentTime")) {
                                                           softwareServerSupportedCapability.setDeploymentTime((java.util.Date)actualValue);
                                                      }
                                                      if (name.equals("deployer")) {
                                                           softwareServerSupportedCapability.setDeployer((String)actualValue);
                                                      }
                                                   } else {
                                                       // put out the omrs value object
                                                       if (softwareServerSupportedCapability.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            softwareServerSupportedCapability.setExtraAttributes(extraAttributes);
                                                        }
                                                      softwareServerSupportedCapability.getExtraAttributes().put(name, primitivePropertyValue);
                                                   }
                                                   break;
                                               case ENUM:
                                                   EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                                                   String symbolicName = enumPropertyValue.getSymbolicName();
                                                   if (SoftwareServerSupportedCapability.ENUM_NAMES_SET.contains(name)) {
                                                        if (name.equals("serverCapabilityStatus")) {
                                                              OperationalStatus serverCapabilityStatus = OperationalStatus.valueOf(symbolicName);
                                                             softwareServerSupportedCapability.setServerCapabilityStatus(serverCapabilityStatus);
                                                        }
                                                   } else {
                                                       // put out the omrs value object
                                                        if (softwareServerSupportedCapability.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            softwareServerSupportedCapability.setExtraAttributes(extraAttributes);
                                                        }

                                                        softwareServerSupportedCapability.getExtraAttributes().put(name, enumPropertyValue);
                                                    }
                       
                                                   break;
                                               case MAP:
                                                    if (softwareServerSupportedCapability.MAP_NAMES_SET.contains(name)) {
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
               return softwareServerSupportedCapability;
       }
       public static Relationship mapSoftwareServerSupportedCapabilityToOmrsRelationship(SoftwareServerSupportedCapability softwareServerSupportedCapability) {
           Relationship omrsRelationship = Line.createOmrsRelationship(softwareServerSupportedCapability);

           SystemAttributes systemAttributes = softwareServerSupportedCapability.getSystemAttributes();
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
           entityOne.setGUID(softwareServerSupportedCapability.getEntity1Guid());
           String type1 = softwareServerSupportedCapability.getEntity1Type();
           InstanceType instancetype1 = new InstanceType();
           instancetype1.setTypeDefName(type1);
           entityOne.setType(instancetype1);
           //set proxy 2
           EntityProxy entityTwo = new EntityProxy();
           entityTwo.setGUID(softwareServerSupportedCapability.getEntity2Guid());
           String type2 = softwareServerSupportedCapability.getEntity2Type();
           InstanceType instancetype2 = new InstanceType();
           instancetype2.setTypeDefName(type2);
           entityTwo.setType(instancetype2);
           // set relationshipType
           InstanceType relationshipType = new InstanceType();
           relationshipType.setTypeDefGUID(softwareServerSupportedCapability.getTypeDefGuid());
           relationshipType.setTypeDefName("SoftwareServerSupportedCapability");
           omrsRelationship.setType(relationshipType);           
   
           omrsRelationship.setEntityOneProxy(entityOne);
           omrsRelationship.setEntityTwoProxy(entityTwo);           
           if (omrsRelationship.getGUID() == null) {
               omrsRelationship.setGUID(softwareServerSupportedCapability.getGuid());
           }

           InstanceProperties instanceProperties = new InstanceProperties();
           // primitives

            if (softwareServerSupportedCapability.getDeploymentTime()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
                primitivePropertyValue.setPrimitiveValue(softwareServerSupportedCapability.getDeploymentTime());
                instanceProperties.setProperty("deploymentTime", primitivePropertyValue);
            }
            if (softwareServerSupportedCapability.getDeployer()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(softwareServerSupportedCapability.getDeployer());
                instanceProperties.setProperty("deployer", primitivePropertyValue);
            }
            if (softwareServerSupportedCapability.getServerCapabilityStatus()!=null) {
                OperationalStatus enumType = softwareServerSupportedCapability.getServerCapabilityStatus();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("serverCapabilityStatus", enumPropertyValue);
            }
            omrsRelationship.setProperties(instanceProperties);

           return omrsRelationship;
       }
}
