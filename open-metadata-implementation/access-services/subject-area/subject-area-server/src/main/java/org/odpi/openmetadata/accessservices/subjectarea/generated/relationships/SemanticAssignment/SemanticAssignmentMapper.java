/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SemanticAssignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SemanticAssignment.SemanticAssignment;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.enums.*;
import org.odpi.openmetadata.accessservices.subjectarea.common.Status;
import org.odpi.openmetadata.accessservices.subjectarea.common.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.common.SystemAttributes;
import java.util.*;


/**
 * Static mapping methods to map between SemanticAssignment and the omrs Relationship.
 */
public class SemanticAssignmentMapper {
       private static final Logger log = LoggerFactory.getLogger(SemanticAssignmentMapper.class);
       private static final String className = SemanticAssignmentMapper.class.getName();

       public static SemanticAssignment mapOmrsRelationshipToSemanticAssignment(Relationship omrsRelationship)  throws InvalidParameterException {
     
               if ("SemanticAssignment".equals(omrsRelationship.getType().getTypeDefName())) {
                   SemanticAssignment semanticAssignment = new SemanticAssignment(omrsRelationship);

                   SystemAttributes systemAttributes = new SystemAttributes();
                   InstanceStatus instanceStatus =  omrsRelationship.getStatus();
                   Status omas_status = SubjectAreaUtils.convertInstanceStatusToStatus(instanceStatus);
                   systemAttributes.setStatus(omas_status);
                   systemAttributes.setCreatedBy(omrsRelationship.getCreatedBy());
                   systemAttributes.setUpdatedBy(omrsRelationship.getUpdatedBy());
                   systemAttributes.setCreateTime(omrsRelationship.getCreateTime());
                   systemAttributes.setUpdateTime(omrsRelationship.getUpdateTime());
                   systemAttributes.setVersion(omrsRelationship.getVersion());
                   systemAttributes.setGUID(omrsRelationship.getGUID());
                   semanticAssignment.setSystemAttributes(systemAttributes);

                   InstanceProperties omrsRelationshipProperties = omrsRelationship.getProperties();
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
                                                   if (SemanticAssignment.ATTRIBUTE_NAMES_SET.contains(name)) {
                                                      if (name.equals("description")) {
                                                          semanticAssignment.setDescription(actualValue);
                                                      }
                                                      if (name.equals("expression")) {
                                                          semanticAssignment.setExpression(actualValue);
                                                      }
                                                      if (name.equals("confidence")) {
                                                          semanticAssignment.setConfidence(actualValue);
                                                      }
                                                      if (name.equals("steward")) {
                                                          semanticAssignment.setSteward(actualValue);
                                                      }
                                                      if (name.equals("source")) {
                                                          semanticAssignment.setSource(actualValue);
                                                      }
                                                   } else {
                                                       // put out the omrs value object
                                                       if (semanticAssignment.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            semanticAssignment.setExtraAttributes(extraAttributes);
                                                        }
                                                      semanticAssignment.getExtraAttributes().put(name, primitivePropertyValue);
                                                   }
                                                   break;
                                               case ENUM:
                                                   EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                                                   String symbolicName = enumPropertyValue.getSymbolicName();
                                                   if (SemanticAssignment.ENUM_NAMES_SET.contains(name)) {
                                                        if (name.equals("status")) {
                                                              org.odpi.openmetadata.accessservices.subjectarea.generated.enums.TermRelationshipStatus status = org.odpi.openmetadata.accessservices.subjectarea.generated.enums.TermRelationshipStatus.valueOf(symbolicName);
                                                             semanticAssignment.setStatus(status);
                                                        }
                                                   } else {
                                                       // put out the omrs value object
                                                        if (semanticAssignment.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            semanticAssignment.setExtraAttributes(extraAttributes);
                                                        }

                                                        semanticAssignment.getExtraAttributes().put(name, enumPropertyValue);
                                                    }
                       
                                                   break;
                                               case MAP:
                                                    MapPropertyValue mapPropertyValue = (MapPropertyValue) value;
                                                    InstanceProperties mapInstanceProperties  = mapPropertyValue.getMapValues();
                                                    if (SemanticAssignment.MAP_NAMES_SET.contains(name)) {
                                                       // put out the omrs value object
                                                      if (semanticAssignment.getExtraAttributes() ==null) {
                                                           Map<String, Object> extraAttributes = new HashMap();
                                                           semanticAssignment.setExtraAttributes(extraAttributes);
                                                      }
                                                      semanticAssignment.getExtraAttributes().put(name, mapPropertyValue);
                                                    }
                                                       break;
                                               case ARRAY:
                                               case STRUCT:
                                               case UNKNOWN:
                                                   // error
                                                   break;
                                           }
                       
                                       }   // end while
                                       return semanticAssignment;

                   } else {
                       // TODO wrong type for this guid
                   }
                   return null;
       }
       public static Relationship mapSemanticAssignmentToOmrsRelationship(SemanticAssignment semanticAssignment) {
           Relationship omrsRelationship = Line.createOmrsRelationship(semanticAssignment);

           SystemAttributes systemAttributes = semanticAssignment.getSystemAttributes();
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
               if (systemAttributes.getStatus()!=null) {
                   InstanceStatus instanceStatus = SubjectAreaUtils.convertStatusToStatusInstance(systemAttributes.getStatus());
                   omrsRelationship.setStatus(instanceStatus);
               }
           }

           InstanceProperties instanceProperties = new InstanceProperties();
           // primitives

            if (semanticAssignment.getDescription()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(semanticAssignment.getDescription());
                instanceProperties.setProperty("description", primitivePropertyValue);
            }
            if (semanticAssignment.getExpression()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(semanticAssignment.getExpression());
                instanceProperties.setProperty("expression", primitivePropertyValue);
            }
            if (semanticAssignment.getConfidence()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
                primitivePropertyValue.setPrimitiveValue(semanticAssignment.getConfidence());
                instanceProperties.setProperty("confidence", primitivePropertyValue);
            }
            if (semanticAssignment.getSteward()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(semanticAssignment.getSteward());
                instanceProperties.setProperty("steward", primitivePropertyValue);
            }
            if (semanticAssignment.getSource()!=null) {
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(semanticAssignment.getSource());
                instanceProperties.setProperty("source", primitivePropertyValue);
            }
            if (semanticAssignment.getStatus()!=null) {
                TermRelationshipStatus enumType = semanticAssignment.getStatus();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("status", enumPropertyValue);
            }
            omrsRelationship.setProperties(instanceProperties);

           return omrsRelationship;
       }
}
