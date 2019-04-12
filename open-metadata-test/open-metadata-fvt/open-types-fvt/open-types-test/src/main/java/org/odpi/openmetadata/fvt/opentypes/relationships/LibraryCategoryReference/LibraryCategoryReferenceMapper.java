/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.LibraryCategoryReference;
import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.fvt.opentypes.relationships.LibraryCategoryReference.LibraryCategoryReference;

import java.util.*;

/**
 * Static mapping methods to map between LibraryCategoryReference and the omrs Relationship.
 */
public class LibraryCategoryReferenceMapper {
       private static final Logger log = LoggerFactory.getLogger(LibraryCategoryReferenceMapper.class);
       private static final String className = LibraryCategoryReferenceMapper.class.getName();

       public static LibraryCategoryReference mapOmrsRelationshipToLibraryCategoryReference(Relationship omrsRelationship) {
                String methodName = "mapOmrsRelationshipToLibraryCategoryReference";
               LibraryCategoryReference libraryCategoryReference = new LibraryCategoryReference(omrsRelationship);
               SystemAttributes systemAttributes = new SystemAttributes();
               systemAttributes.setStatus(omrsRelationship.getStatus());
               systemAttributes.setCreatedBy(omrsRelationship.getCreatedBy());
               systemAttributes.setUpdatedBy(omrsRelationship.getUpdatedBy());
               systemAttributes.setCreateTime(omrsRelationship.getCreateTime());
               systemAttributes.setUpdateTime(omrsRelationship.getUpdateTime());
               systemAttributes.setVersion(omrsRelationship.getVersion());
               systemAttributes.setGUID(omrsRelationship.getGUID());
               libraryCategoryReference.setGuid(omrsRelationship.getGUID());
               libraryCategoryReference.setSystemAttributes(systemAttributes);

               InstanceProperties omrsRelationshipProperties = omrsRelationship.getProperties();
               if (omrsRelationshipProperties !=null) {
                 omrsRelationshipProperties.setEffectiveFromTime(libraryCategoryReference.getEffectiveFromTime());
                 omrsRelationshipProperties.setEffectiveToTime(libraryCategoryReference.getEffectiveToTime());
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
                                                   if (LibraryCategoryReference.ATTRIBUTE_NAMES_SET.contains(name)) {
                                                      if (name.equals("identifier")) {
                                                           libraryCategoryReference.setIdentifier((String)actualValue);
                                                      }
                                                      if (name.equals("description")) {
                                                           libraryCategoryReference.setDescription((String)actualValue);
                                                      }
                                                      if (name.equals("steward")) {
                                                           libraryCategoryReference.setSteward((String)actualValue);
                                                      }
                                                      if (name.equals("lastVerified")) {
                                                           libraryCategoryReference.setLastVerified((java.util.Date)actualValue);
                                                      }
                                                   } else {
                                                       // put out the omrs value object
                                                       if (libraryCategoryReference.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            libraryCategoryReference.setExtraAttributes(extraAttributes);
                                                        }
                                                      libraryCategoryReference.getExtraAttributes().put(name, primitivePropertyValue);
                                                   }
                                                   break;
                                               case ENUM:
                                                   EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                                                   String symbolicName = enumPropertyValue.getSymbolicName();
                                                   if (LibraryCategoryReference.ENUM_NAMES_SET.contains(name)) {
                                                   } else {
                                                       // put out the omrs value object
                                                        if (libraryCategoryReference.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            libraryCategoryReference.setExtraAttributes(extraAttributes);
                                                        }

                                                        libraryCategoryReference.getExtraAttributes().put(name, enumPropertyValue);
                                                    }
                       
                                                   break;
                                               case MAP:
                                                    if (libraryCategoryReference.MAP_NAMES_SET.contains(name)) {
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
               return libraryCategoryReference;
       }
       public static Relationship mapLibraryCategoryReferenceToOmrsRelationship(LibraryCategoryReference libraryCategoryReference) {
           Relationship omrsRelationship = Line.createOmrsRelationship(libraryCategoryReference);

           SystemAttributes systemAttributes = libraryCategoryReference.getSystemAttributes();
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
           entityOne.setGUID(libraryCategoryReference.getEntity1Guid());
           String type1 = libraryCategoryReference.getEntity1Type();
           InstanceType instancetype1 = new InstanceType();
           instancetype1.setTypeDefName(type1);
           entityOne.setType(instancetype1);
           //set proxy 2
           EntityProxy entityTwo = new EntityProxy();
           entityTwo.setGUID(libraryCategoryReference.getEntity2Guid());
           String type2 = libraryCategoryReference.getEntity2Type();
           InstanceType instancetype2 = new InstanceType();
           instancetype2.setTypeDefName(type2);
           entityTwo.setType(instancetype2);
           // set relationshipType
           InstanceType relationshipType = new InstanceType();
           relationshipType.setTypeDefGUID(libraryCategoryReference.getTypeDefGuid());
           relationshipType.setTypeDefName("LibraryCategoryReference");
           omrsRelationship.setType(relationshipType);           
   
           omrsRelationship.setEntityOneProxy(entityOne);
           omrsRelationship.setEntityTwoProxy(entityTwo);           
           if (omrsRelationship.getGUID() == null) {
               omrsRelationship.setGUID(libraryCategoryReference.getGuid());
           }

           InstanceProperties instanceProperties = new InstanceProperties();
           // primitives

            if (libraryCategoryReference.getIdentifier()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(libraryCategoryReference.getIdentifier());
                instanceProperties.setProperty("identifier", primitivePropertyValue);
            }
            if (libraryCategoryReference.getDescription()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(libraryCategoryReference.getDescription());
                instanceProperties.setProperty("description", primitivePropertyValue);
            }
            if (libraryCategoryReference.getSteward()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(libraryCategoryReference.getSteward());
                instanceProperties.setProperty("steward", primitivePropertyValue);
            }
            if (libraryCategoryReference.getLastVerified()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
                primitivePropertyValue.setPrimitiveValue(libraryCategoryReference.getLastVerified());
                instanceProperties.setProperty("lastVerified", primitivePropertyValue);
            }
            omrsRelationship.setProperties(instanceProperties);

           return omrsRelationship;
       }
}
