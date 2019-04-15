/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.relationships.RelationshipAnnotation;
import org.odpi.openmetadata.fvt.opentypes.common.*;
import org.odpi.openmetadata.fvt.opentypes.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.fvt.opentypes.relationships.RelationshipAnnotation.RelationshipAnnotation;

import java.util.*;

/**
 * Static mapping methods to map between RelationshipAnnotation and the omrs Relationship.
 */
public class RelationshipAnnotationMapper {
       private static final Logger log = LoggerFactory.getLogger(RelationshipAnnotationMapper.class);
       private static final String className = RelationshipAnnotationMapper.class.getName();

       public static RelationshipAnnotation mapOmrsRelationshipToRelationshipAnnotation(Relationship omrsRelationship) {
                String methodName = "mapOmrsRelationshipToRelationshipAnnotation";
               RelationshipAnnotation relationshipAnnotation = new RelationshipAnnotation(omrsRelationship);
               SystemAttributes systemAttributes = new SystemAttributes();
               systemAttributes.setStatus(omrsRelationship.getStatus());
               systemAttributes.setCreatedBy(omrsRelationship.getCreatedBy());
               systemAttributes.setUpdatedBy(omrsRelationship.getUpdatedBy());
               systemAttributes.setCreateTime(omrsRelationship.getCreateTime());
               systemAttributes.setUpdateTime(omrsRelationship.getUpdateTime());
               systemAttributes.setVersion(omrsRelationship.getVersion());
               systemAttributes.setGUID(omrsRelationship.getGUID());
               relationshipAnnotation.setGuid(omrsRelationship.getGUID());
               relationshipAnnotation.setSystemAttributes(systemAttributes);

               InstanceProperties omrsRelationshipProperties = omrsRelationship.getProperties();
               if (omrsRelationshipProperties !=null) {
                 omrsRelationshipProperties.setEffectiveFromTime(relationshipAnnotation.getEffectiveFromTime());
                 omrsRelationshipProperties.setEffectiveToTime(relationshipAnnotation.getEffectiveToTime());
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
                                                   if (RelationshipAnnotation.ATTRIBUTE_NAMES_SET.contains(name)) {
                                                      if (name.equals("annotationType")) {
                                                           relationshipAnnotation.setAnnotationType((String)actualValue);
                                                      }
                                                      if (name.equals("summary")) {
                                                           relationshipAnnotation.setSummary((String)actualValue);
                                                      }
                                                      if (name.equals("confidenceLevel")) {
                                                           relationshipAnnotation.setConfidenceLevel((Integer)actualValue);
                                                      }
                                                      if (name.equals("expression")) {
                                                           relationshipAnnotation.setExpression((String)actualValue);
                                                      }
                                                      if (name.equals("explanation")) {
                                                           relationshipAnnotation.setExplanation((String)actualValue);
                                                      }
                                                      if (name.equals("analysisStep")) {
                                                           relationshipAnnotation.setAnalysisStep((String)actualValue);
                                                      }
                                                      if (name.equals("jsonProperties")) {
                                                           relationshipAnnotation.setJsonProperties((String)actualValue);
                                                      }
                                                   } else {
                                                       // put out the omrs value object
                                                       if (relationshipAnnotation.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            relationshipAnnotation.setExtraAttributes(extraAttributes);
                                                        }
                                                      relationshipAnnotation.getExtraAttributes().put(name, primitivePropertyValue);
                                                   }
                                                   break;
                                               case ENUM:
                                                   EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                                                   String symbolicName = enumPropertyValue.getSymbolicName();
                                                   if (RelationshipAnnotation.ENUM_NAMES_SET.contains(name)) {
                                                        if (name.equals("annotationStatus")) {
                                                              AnnotationStatus annotationStatus = AnnotationStatus.valueOf(symbolicName);
                                                             relationshipAnnotation.setAnnotationStatus(annotationStatus);
                                                        }
                                                   } else {
                                                       // put out the omrs value object
                                                        if (relationshipAnnotation.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            relationshipAnnotation.setExtraAttributes(extraAttributes);
                                                        }

                                                        relationshipAnnotation.getExtraAttributes().put(name, enumPropertyValue);
                                                    }
                       
                                                   break;
                                               case MAP:
                                                    if (relationshipAnnotation.MAP_NAMES_SET.contains(name)) {
                                                        MapPropertyValue mapPropertyValue = (MapPropertyValue) value;
                                                        InstanceProperties instancePropertyForMap = (InstanceProperties) mapPropertyValue.getMapValues();

                                                        if (name.equals("additionalProperties")) {

                                                             // Only support Map<String,String> as that is what is in the archive types at this time.
                                                             Map<String, String> actualMap = new HashMap();
                                                             Iterator iter = instancePropertyForMap.getPropertyNames();
                                                             while (iter.hasNext()) {
                                                                String mapkey = (String) iter.next();
                                                                PrimitivePropertyValue primitivePropertyMapValue = (PrimitivePropertyValue) instancePropertyForMap.getPropertyValue(mapkey);
                                                                String mapvalue = (String) primitivePropertyMapValue.getPrimitiveValue();
                                                                actualMap.put(mapkey, mapvalue);
                                                             }
                                                             relationshipAnnotation.setAdditionalProperties(actualMap);
                                                         }
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
               return relationshipAnnotation;
       }
       public static Relationship mapRelationshipAnnotationToOmrsRelationship(RelationshipAnnotation relationshipAnnotation) {
           Relationship omrsRelationship = Line.createOmrsRelationship(relationshipAnnotation);

           SystemAttributes systemAttributes = relationshipAnnotation.getSystemAttributes();
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
           entityOne.setGUID(relationshipAnnotation.getEntity1Guid());
           String type1 = relationshipAnnotation.getEntity1Type();
           InstanceType instancetype1 = new InstanceType();
           instancetype1.setTypeDefName(type1);
           entityOne.setType(instancetype1);
           //set proxy 2
           EntityProxy entityTwo = new EntityProxy();
           entityTwo.setGUID(relationshipAnnotation.getEntity2Guid());
           String type2 = relationshipAnnotation.getEntity2Type();
           InstanceType instancetype2 = new InstanceType();
           instancetype2.setTypeDefName(type2);
           entityTwo.setType(instancetype2);
           // set relationshipType
           InstanceType relationshipType = new InstanceType();
           relationshipType.setTypeDefGUID(relationshipAnnotation.getTypeDefGuid());
           relationshipType.setTypeDefName("RelationshipAnnotation");
           omrsRelationship.setType(relationshipType);           
   
           omrsRelationship.setEntityOneProxy(entityOne);
           omrsRelationship.setEntityTwoProxy(entityTwo);           
           if (omrsRelationship.getGUID() == null) {
               omrsRelationship.setGUID(relationshipAnnotation.getGuid());
           }

           InstanceProperties instanceProperties = new InstanceProperties();
           // primitives

            if (relationshipAnnotation.getAnnotationType()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(relationshipAnnotation.getAnnotationType());
                instanceProperties.setProperty("annotationType", primitivePropertyValue);
            }
            if (relationshipAnnotation.getSummary()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(relationshipAnnotation.getSummary());
                instanceProperties.setProperty("summary", primitivePropertyValue);
            }
            if (relationshipAnnotation.getConfidenceLevel()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
                primitivePropertyValue.setPrimitiveValue(relationshipAnnotation.getConfidenceLevel());
                instanceProperties.setProperty("confidenceLevel", primitivePropertyValue);
            }
            if (relationshipAnnotation.getExpression()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(relationshipAnnotation.getExpression());
                instanceProperties.setProperty("expression", primitivePropertyValue);
            }
            if (relationshipAnnotation.getExplanation()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(relationshipAnnotation.getExplanation());
                instanceProperties.setProperty("explanation", primitivePropertyValue);
            }
            if (relationshipAnnotation.getAnalysisStep()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(relationshipAnnotation.getAnalysisStep());
                instanceProperties.setProperty("analysisStep", primitivePropertyValue);
            }
            if (relationshipAnnotation.getJsonProperties()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(relationshipAnnotation.getJsonProperties());
                instanceProperties.setProperty("jsonProperties", primitivePropertyValue);
            }
            if (relationshipAnnotation.getAnnotationStatus()!=null) {
                AnnotationStatus enumType = relationshipAnnotation.getAnnotationStatus();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("annotationStatus", enumPropertyValue);
            }
            omrsRelationship.setProperties(instanceProperties);

           return omrsRelationship;
       }
}
