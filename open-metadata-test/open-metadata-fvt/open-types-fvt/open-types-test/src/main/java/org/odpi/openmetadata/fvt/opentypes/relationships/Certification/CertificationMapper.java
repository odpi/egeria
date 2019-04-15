/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.Certification;
import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.fvt.opentypes.relationships.Certification.Certification;

import java.util.*;

/**
 * Static mapping methods to map between Certification and the omrs Relationship.
 */
public class CertificationMapper {
       private static final Logger log = LoggerFactory.getLogger(CertificationMapper.class);
       private static final String className = CertificationMapper.class.getName();

       public static Certification mapOmrsRelationshipToCertification(Relationship omrsRelationship) {
                String methodName = "mapOmrsRelationshipToCertification";
               Certification certification = new Certification(omrsRelationship);
               SystemAttributes systemAttributes = new SystemAttributes();
               systemAttributes.setStatus(omrsRelationship.getStatus());
               systemAttributes.setCreatedBy(omrsRelationship.getCreatedBy());
               systemAttributes.setUpdatedBy(omrsRelationship.getUpdatedBy());
               systemAttributes.setCreateTime(omrsRelationship.getCreateTime());
               systemAttributes.setUpdateTime(omrsRelationship.getUpdateTime());
               systemAttributes.setVersion(omrsRelationship.getVersion());
               systemAttributes.setGUID(omrsRelationship.getGUID());
               certification.setGuid(omrsRelationship.getGUID());
               certification.setSystemAttributes(systemAttributes);

               InstanceProperties omrsRelationshipProperties = omrsRelationship.getProperties();
               if (omrsRelationshipProperties !=null) {
                 omrsRelationshipProperties.setEffectiveFromTime(certification.getEffectiveFromTime());
                 omrsRelationshipProperties.setEffectiveToTime(certification.getEffectiveToTime());
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
                                                   if (Certification.ATTRIBUTE_NAMES_SET.contains(name)) {
                                                      if (name.equals("certificateGUID")) {
                                                           certification.setCertificateGUID((String)actualValue);
                                                      }
                                                      if (name.equals("start")) {
                                                           certification.setStart((java.util.Date)actualValue);
                                                      }
                                                      if (name.equals("end")) {
                                                           certification.setEnd((java.util.Date)actualValue);
                                                      }
                                                      if (name.equals("conditions")) {
                                                           certification.setConditions((String)actualValue);
                                                      }
                                                      if (name.equals("certifiedBy")) {
                                                           certification.setCertifiedBy((String)actualValue);
                                                      }
                                                      if (name.equals("custodian")) {
                                                           certification.setCustodian((String)actualValue);
                                                      }
                                                      if (name.equals("recipient")) {
                                                           certification.setRecipient((String)actualValue);
                                                      }
                                                      if (name.equals("notes")) {
                                                           certification.setNotes((String)actualValue);
                                                      }
                                                   } else {
                                                       // put out the omrs value object
                                                       if (certification.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            certification.setExtraAttributes(extraAttributes);
                                                        }
                                                      certification.getExtraAttributes().put(name, primitivePropertyValue);
                                                   }
                                                   break;
                                               case ENUM:
                                                   EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                                                   String symbolicName = enumPropertyValue.getSymbolicName();
                                                   if (Certification.ENUM_NAMES_SET.contains(name)) {
                                                   } else {
                                                       // put out the omrs value object
                                                        if (certification.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            certification.setExtraAttributes(extraAttributes);
                                                        }

                                                        certification.getExtraAttributes().put(name, enumPropertyValue);
                                                    }
                       
                                                   break;
                                               case MAP:
                                                    if (certification.MAP_NAMES_SET.contains(name)) {
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
               return certification;
       }
       public static Relationship mapCertificationToOmrsRelationship(Certification certification) {
           Relationship omrsRelationship = Line.createOmrsRelationship(certification);

           SystemAttributes systemAttributes = certification.getSystemAttributes();
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
           entityOne.setGUID(certification.getEntity1Guid());
           String type1 = certification.getEntity1Type();
           InstanceType instancetype1 = new InstanceType();
           instancetype1.setTypeDefName(type1);
           entityOne.setType(instancetype1);
           //set proxy 2
           EntityProxy entityTwo = new EntityProxy();
           entityTwo.setGUID(certification.getEntity2Guid());
           String type2 = certification.getEntity2Type();
           InstanceType instancetype2 = new InstanceType();
           instancetype2.setTypeDefName(type2);
           entityTwo.setType(instancetype2);
           // set relationshipType
           InstanceType relationshipType = new InstanceType();
           relationshipType.setTypeDefGUID(certification.getTypeDefGuid());
           relationshipType.setTypeDefName("Certification");
           omrsRelationship.setType(relationshipType);           
   
           omrsRelationship.setEntityOneProxy(entityOne);
           omrsRelationship.setEntityTwoProxy(entityTwo);           
           if (omrsRelationship.getGUID() == null) {
               omrsRelationship.setGUID(certification.getGuid());
           }

           InstanceProperties instanceProperties = new InstanceProperties();
           // primitives

            if (certification.getCertificateGUID()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(certification.getCertificateGUID());
                instanceProperties.setProperty("certificateGUID", primitivePropertyValue);
            }
            if (certification.getStart()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
                primitivePropertyValue.setPrimitiveValue(certification.getStart());
                instanceProperties.setProperty("start", primitivePropertyValue);
            }
            if (certification.getEnd()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
                primitivePropertyValue.setPrimitiveValue(certification.getEnd());
                instanceProperties.setProperty("end", primitivePropertyValue);
            }
            if (certification.getConditions()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(certification.getConditions());
                instanceProperties.setProperty("conditions", primitivePropertyValue);
            }
            if (certification.getCertifiedBy()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(certification.getCertifiedBy());
                instanceProperties.setProperty("certifiedBy", primitivePropertyValue);
            }
            if (certification.getCustodian()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(certification.getCustodian());
                instanceProperties.setProperty("custodian", primitivePropertyValue);
            }
            if (certification.getRecipient()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(certification.getRecipient());
                instanceProperties.setProperty("recipient", primitivePropertyValue);
            }
            if (certification.getNotes()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(certification.getNotes());
                instanceProperties.setProperty("notes", primitivePropertyValue);
            }
            omrsRelationship.setProperties(instanceProperties);

           return omrsRelationship;
       }
}
