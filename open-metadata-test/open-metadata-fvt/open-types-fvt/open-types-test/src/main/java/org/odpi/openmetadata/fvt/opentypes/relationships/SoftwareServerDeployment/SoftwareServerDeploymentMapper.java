/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.SoftwareServerDeployment;
import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.fvt.opentypes.relationships.SoftwareServerDeployment.SoftwareServerDeployment;

import java.util.*;

/**
 * Static mapping methods to map between SoftwareServerDeployment and the omrs Relationship.
 */
public class SoftwareServerDeploymentMapper {
       private static final Logger log = LoggerFactory.getLogger(SoftwareServerDeploymentMapper.class);
       private static final String className = SoftwareServerDeploymentMapper.class.getName();

       public static SoftwareServerDeployment mapOmrsRelationshipToSoftwareServerDeployment(Relationship omrsRelationship) {
                String methodName = "mapOmrsRelationshipToSoftwareServerDeployment";
               SoftwareServerDeployment softwareServerDeployment = new SoftwareServerDeployment(omrsRelationship);
               SystemAttributes systemAttributes = new SystemAttributes();
               systemAttributes.setStatus(omrsRelationship.getStatus());
               systemAttributes.setCreatedBy(omrsRelationship.getCreatedBy());
               systemAttributes.setUpdatedBy(omrsRelationship.getUpdatedBy());
               systemAttributes.setCreateTime(omrsRelationship.getCreateTime());
               systemAttributes.setUpdateTime(omrsRelationship.getUpdateTime());
               systemAttributes.setVersion(omrsRelationship.getVersion());
               systemAttributes.setGUID(omrsRelationship.getGUID());
               softwareServerDeployment.setGuid(omrsRelationship.getGUID());
               softwareServerDeployment.setSystemAttributes(systemAttributes);

               InstanceProperties omrsRelationshipProperties = omrsRelationship.getProperties();
               if (omrsRelationshipProperties !=null) {
                 omrsRelationshipProperties.setEffectiveFromTime(softwareServerDeployment.getEffectiveFromTime());
                 omrsRelationshipProperties.setEffectiveToTime(softwareServerDeployment.getEffectiveToTime());
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
                                                   if (SoftwareServerDeployment.ATTRIBUTE_NAMES_SET.contains(name)) {
                                                      if (name.equals("deploymentTime")) {
                                                           softwareServerDeployment.setDeploymentTime((java.util.Date)actualValue);
                                                      }
                                                      if (name.equals("deployer")) {
                                                           softwareServerDeployment.setDeployer((String)actualValue);
                                                      }
                                                   } else {
                                                       // put out the omrs value object
                                                       if (softwareServerDeployment.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            softwareServerDeployment.setExtraAttributes(extraAttributes);
                                                        }
                                                      softwareServerDeployment.getExtraAttributes().put(name, primitivePropertyValue);
                                                   }
                                                   break;
                                               case ENUM:
                                                   EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                                                   String symbolicName = enumPropertyValue.getSymbolicName();
                                                   if (SoftwareServerDeployment.ENUM_NAMES_SET.contains(name)) {
                                                        if (name.equals("serverStatus")) {
                                                              OperationalStatus serverStatus = OperationalStatus.valueOf(symbolicName);
                                                             softwareServerDeployment.setServerStatus(serverStatus);
                                                        }
                                                   } else {
                                                       // put out the omrs value object
                                                        if (softwareServerDeployment.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            softwareServerDeployment.setExtraAttributes(extraAttributes);
                                                        }

                                                        softwareServerDeployment.getExtraAttributes().put(name, enumPropertyValue);
                                                    }
                       
                                                   break;
                                               case MAP:
                                                    if (softwareServerDeployment.MAP_NAMES_SET.contains(name)) {
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
               return softwareServerDeployment;
       }
       public static Relationship mapSoftwareServerDeploymentToOmrsRelationship(SoftwareServerDeployment softwareServerDeployment) {
           Relationship omrsRelationship = Line.createOmrsRelationship(softwareServerDeployment);

           SystemAttributes systemAttributes = softwareServerDeployment.getSystemAttributes();
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
           entityOne.setGUID(softwareServerDeployment.getEntity1Guid());
           String type1 = softwareServerDeployment.getEntity1Type();
           InstanceType instancetype1 = new InstanceType();
           instancetype1.setTypeDefName(type1);
           entityOne.setType(instancetype1);
           //set proxy 2
           EntityProxy entityTwo = new EntityProxy();
           entityTwo.setGUID(softwareServerDeployment.getEntity2Guid());
           String type2 = softwareServerDeployment.getEntity2Type();
           InstanceType instancetype2 = new InstanceType();
           instancetype2.setTypeDefName(type2);
           entityTwo.setType(instancetype2);
           // set relationshipType
           InstanceType relationshipType = new InstanceType();
           relationshipType.setTypeDefGUID(softwareServerDeployment.getTypeDefGuid());
           relationshipType.setTypeDefName("SoftwareServerDeployment");
           omrsRelationship.setType(relationshipType);           
   
           omrsRelationship.setEntityOneProxy(entityOne);
           omrsRelationship.setEntityTwoProxy(entityTwo);           
           if (omrsRelationship.getGUID() == null) {
               omrsRelationship.setGUID(softwareServerDeployment.getGuid());
           }

           InstanceProperties instanceProperties = new InstanceProperties();
           // primitives

            if (softwareServerDeployment.getDeploymentTime()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
                primitivePropertyValue.setPrimitiveValue(softwareServerDeployment.getDeploymentTime());
                instanceProperties.setProperty("deploymentTime", primitivePropertyValue);
            }
            if (softwareServerDeployment.getDeployer()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(softwareServerDeployment.getDeployer());
                instanceProperties.setProperty("deployer", primitivePropertyValue);
            }
            if (softwareServerDeployment.getServerStatus()!=null) {
                OperationalStatus enumType = softwareServerDeployment.getServerStatus();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("serverStatus", enumPropertyValue);
            }
            omrsRelationship.setProperties(instanceProperties);

           return omrsRelationship;
       }
}
