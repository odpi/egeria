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
package org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Contributor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Contributor.Contributor;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.enums.*;
import org.odpi.openmetadata.accessservices.subjectarea.common.Status;
import org.odpi.openmetadata.accessservices.subjectarea.common.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.common.SystemAttributes;
import java.util.*;


/**
 * Static mapping methods to map between Contributor and the omrs Relationship.
 */
public class ContributorMapper {
       private static final Logger log = LoggerFactory.getLogger(ContributorMapper.class);
       private static final String className = ContributorMapper.class.getName();

       public static Contributor mapOmrsRelationshipToContributor(Relationship omrsRelationship)  throws InvalidParameterException {
     
               if ("Contributor".equals(omrsRelationship.getType().getTypeDefName())) {
                   Contributor contributor = new Contributor(omrsRelationship);

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
                   contributor.setSystemAttributes(systemAttributes);

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
                                                   if (Contributor.ATTRIBUTE_NAMES_SET.contains(name)) {
                                                   } else {
                                                       // put out the omrs value object
                                                       if (contributor.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            contributor.setExtraAttributes(extraAttributes);
                                                        }
                                                      contributor.getExtraAttributes().put(name, primitivePropertyValue);
                                                   }
                                                   break;
                                               case ENUM:
                                                   EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                                                   String symbolicName = enumPropertyValue.getSymbolicName();
                                                   if (Contributor.ENUM_NAMES_SET.contains(name)) {
                                                        if (name.equals("roleType")) {
                                                              org.odpi.openmetadata.accessservices.subjectarea.generated.enums.CrowdSourcingRole roleType = org.odpi.openmetadata.accessservices.subjectarea.generated.enums.CrowdSourcingRole.valueOf(symbolicName);
                                                             contributor.setRoleType(roleType);
                                                        }
                                                   } else {
                                                       // put out the omrs value object
                                                        if (contributor.getExtraAttributes() ==null) {
                                                            Map<String, Object> extraAttributes = new HashMap();
                                                            contributor.setExtraAttributes(extraAttributes);
                                                        }

                                                        contributor.getExtraAttributes().put(name, enumPropertyValue);
                                                    }
                       
                                                   break;
                                               case MAP:
                                                    MapPropertyValue mapPropertyValue = (MapPropertyValue) value;
                                                    InstanceProperties mapInstanceProperties  = mapPropertyValue.getMapValues();
                                                    if (Contributor.MAP_NAMES_SET.contains(name)) {
                                                       // put out the omrs value object
                                                      if (contributor.getExtraAttributes() ==null) {
                                                           Map<String, Object> extraAttributes = new HashMap();
                                                           contributor.setExtraAttributes(extraAttributes);
                                                      }
                                                      contributor.getExtraAttributes().put(name, mapPropertyValue);
                                                    }
                                                       break;
                                               case ARRAY:
                                               case STRUCT:
                                               case UNKNOWN:
                                                   // error
                                                   break;
                                           }
                       
                                       }   // end while
                                       return contributor;

                   } else {
                       // TODO wrong type for this guid
                   }
                   return null;
       }
       public static Relationship mapContributorToOmrsRelationship(Contributor contributor) {
           Relationship omrsRelationship = Line.createOmrsRelationship(contributor);

           SystemAttributes systemAttributes = contributor.getSystemAttributes();
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

            if (contributor.getRoleType()!=null) {
                CrowdSourcingRole enumType = contributor.getRoleType();
                EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
                enumPropertyValue.setOrdinal(enumType.ordinal());
                enumPropertyValue.setSymbolicName(enumType.name());
                instanceProperties.setProperty("roleType", enumPropertyValue);
            }
            omrsRelationship.setProperties(instanceProperties);

           return omrsRelationship;
       }
}
