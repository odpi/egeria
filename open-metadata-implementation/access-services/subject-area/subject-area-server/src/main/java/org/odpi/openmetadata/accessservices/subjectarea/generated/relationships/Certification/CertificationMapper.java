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
package org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Certification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Certification.Certification;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.enums.*;
import org.odpi.openmetadata.accessservices.subjectarea.common.Status;
import org.odpi.openmetadata.accessservices.subjectarea.common.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.common.SystemAttributes;
import java.util.*;


/**
 * Static mapping methods to map between Certification and the omrs Relationship.
 */
public class CertificationMapper {
       private static final Logger log = LoggerFactory.getLogger(CertificationMapper.class);
       private static final String className = CertificationMapper.class.getName();

       public static Certification mapOmrsRelationshipToCertification(Relationship omrsRelationship)  throws InvalidParameterException {
     
               if ("Certification".equals(omrsRelationship.getType().getTypeDefName())) {
                   Certification certification = new Certification(omrsRelationship);

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
                   certification.setSystemAttributes(systemAttributes);

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
                                                   if (Certification.ATTRIBUTE_NAMES_SET.contains(name)) {
                                                      if (name.equals("certificateGUID")) {
                                                          certification.setCertificateGUID(actualValue);
                                                      }
                                                      if (name.equals("start")) {
                                                          certification.setStart(actualValue);
                                                      }
                                                      if (name.equals("end")) {
                                                          certification.setEnd(actualValue);
                                                      }
                                                      if (name.equals("conditions")) {
                                                          certification.setConditions(actualValue);
                                                      }
                                                      if (name.equals("certifiedBy")) {
                                                          certification.setCertifiedBy(actualValue);
                                                      }
                                                      if (name.equals("custodian")) {
                                                          certification.setCustodian(actualValue);
                                                      }
                                                      if (name.equals("recipient")) {
                                                          certification.setRecipient(actualValue);
                                                      }
                                                      if (name.equals("notes")) {
                                                          certification.setNotes(actualValue);
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
                                                    MapPropertyValue mapPropertyValue = (MapPropertyValue) value;
                                                    InstanceProperties mapInstanceProperties  = mapPropertyValue.getMapValues();
                                                    if (Certification.MAP_NAMES_SET.contains(name)) {
                                                       // put out the omrs value object
                                                      if (certification.getExtraAttributes() ==null) {
                                                           Map<String, Object> extraAttributes = new HashMap();
                                                           certification.setExtraAttributes(extraAttributes);
                                                      }
                                                      certification.getExtraAttributes().put(name, mapPropertyValue);
                                                    }
                                                       break;
                                               case ARRAY:
                                               case STRUCT:
                                               case UNKNOWN:
                                                   // error
                                                   break;
                                           }
                       
                                       }   // end while
                                       return certification;

                   } else {
                       // TODO wrong type for this guid
                   }
                   return null;
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
               if (systemAttributes.getStatus()!=null) {
                   InstanceStatus instanceStatus = SubjectAreaUtils.convertStatusToStatusInstance(systemAttributes.getStatus());
                   omrsRelationship.setStatus(instanceStatus);
               }
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
