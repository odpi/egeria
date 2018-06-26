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
package org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

//omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;

//omas
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.enums.*;



/**
 * License is a relationship between an entity of type Referenceable and an entity of type LicenseType.
 * The ends of the relationship are stored as entity proxies, where there is a 'proxy' name by which the entity type is known.
 * The first entity proxy has licensed as the proxy name for entity type Referenceable.
 * The second entity proxy has licenses as the proxy name for entity type LicenseType.
 *
 * Each entity proxy also stores the entities guid.

 Link between an asset and its license.
 */
public class License extends Line {
    private static final Logger log = LoggerFactory.getLogger(License.class);
    private static final String className = License.class.getName();

   //public java.util.Set<String> propertyNames = new HashSet<>();
      public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "licenseGUID",
          "start",
          "end",
          "conditions",
          "licensedBy",
          "custodian",
          "licensee",
          "notes",

      // Terminate the list
          null
      };
      public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "licenseGUID",
          "start",
          "end",
          "conditions",
          "licensedBy",
          "custodian",
          "licensee",
          "notes",

       // Terminate the list
          null
      };
      public static final String[] ENUM_NAMES_SET_VALUES = new String[] {

           // Terminate the list
            null
      };
      public static final String[] MAP_NAMES_SET_VALUES = new String[] {

           // Terminate the list
           null
      };
      public static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES)));
      public static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES)));
      public static final java.util.Set<String> ENUM_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES)));
      public static final java.util.Set<String> MAP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES)));


    
    public License() {
        super("License");
        super.entity1Name = "licensed";
        super.entity1Type = "Referenceable";
        super.entity2Name = "licenses";
        super.entity2Type = "LicenseType";
    }

    public License(Relationship omrsRelationship) {
        super(omrsRelationship);

        if (!omrsRelationship.getEntityOnePropertyName().equals("licensed")){
            //error
        }
        if (!omrsRelationship.getEntityTwoPropertyName().equals("licenses")){
            //error
        }
        if (!omrsRelationship.getEntityOneProxy().getType().getTypeDefName().equals("Referenceable")){
            //error
        }
        if (!omrsRelationship.getEntityTwoProxy().getType().getTypeDefName().equals("LicenseType")){
            //error
        }
    }

    InstanceProperties obtainInstanceProperties() {
          final String methodName = "obtainInstanceProperties";
          if (log.isDebugEnabled()) {
                 log.debug("==> Method: " + methodName);
          }
          InstanceProperties instanceProperties = new InstanceProperties();
          EnumPropertyValue enumPropertyValue=null;
          MapPropertyValue mapPropertyValue=null;
          PrimitivePropertyValue primitivePropertyValue=null;
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("licenseGUID",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("start",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("end",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("conditions",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("licensedBy",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("custodian",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("licensee",primitivePropertyValue);
          primitivePropertyValue = new PrimitivePropertyValue();
          // TODO  description + change null to value
          primitivePropertyValue.setPrimitiveValue(null);
          instanceProperties.setProperty("notes",primitivePropertyValue);
          if (log.isDebugEnabled()) {
                 log.debug("<== Method: " + methodName);
          }
          return instanceProperties;
    }

         private String licenseGUID;
         /**
          * Unique identifier of the actual license.
          * @return String
          */
         public String getLicenseGUID() {
             return this.licenseGUID;
         }

         public void setLicenseGUID(Object licenseGUID) throws InvalidParameterException {
             // accept an object and cast to the appropriate type.

             final String methodName = "obtainInstanceProperties";
             try {
                   this.licenseGUID = (String)licenseGUID;
             } catch (ClassCastException e) {
                    SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.SET_ATTRIBUTE_WRONG_TYPE;
                    String errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(className,
                            methodName);
                    log.error(errorMessage,e);
                    throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());

             }
         }
         private Date start;
         /**
          * Start date for the license.
          * @return Date
          */
         public Date getStart() {
             return this.start;
         }

         public void setStart(Object start) throws InvalidParameterException {
             // accept an object and cast to the appropriate type.

             final String methodName = "obtainInstanceProperties";
             try {
                   this.start = (Date)start;
             } catch (ClassCastException e) {
                    SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.SET_ATTRIBUTE_WRONG_TYPE;
                    String errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(className,
                            methodName);
                    log.error(errorMessage,e);
                    throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());

             }
         }
         private Date end;
         /**
          * End date for the license.
          * @return Date
          */
         public Date getEnd() {
             return this.end;
         }

         public void setEnd(Object end) throws InvalidParameterException {
             // accept an object and cast to the appropriate type.

             final String methodName = "obtainInstanceProperties";
             try {
                   this.end = (Date)end;
             } catch (ClassCastException e) {
                    SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.SET_ATTRIBUTE_WRONG_TYPE;
                    String errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(className,
                            methodName);
                    log.error(errorMessage,e);
                    throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());

             }
         }
         private String conditions;
         /**
          * Any special conditions or endorsements over the basic license type.
          * @return String
          */
         public String getConditions() {
             return this.conditions;
         }

         public void setConditions(Object conditions) throws InvalidParameterException {
             // accept an object and cast to the appropriate type.

             final String methodName = "obtainInstanceProperties";
             try {
                   this.conditions = (String)conditions;
             } catch (ClassCastException e) {
                    SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.SET_ATTRIBUTE_WRONG_TYPE;
                    String errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(className,
                            methodName);
                    log.error(errorMessage,e);
                    throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());

             }
         }
         private String licensedBy;
         /**
          * Person or organization that owns the intellectual property.
          * @return String
          */
         public String getLicensedBy() {
             return this.licensedBy;
         }

         public void setLicensedBy(Object licensedBy) throws InvalidParameterException {
             // accept an object and cast to the appropriate type.

             final String methodName = "obtainInstanceProperties";
             try {
                   this.licensedBy = (String)licensedBy;
             } catch (ClassCastException e) {
                    SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.SET_ATTRIBUTE_WRONG_TYPE;
                    String errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(className,
                            methodName);
                    log.error(errorMessage,e);
                    throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());

             }
         }
         private String custodian;
         /**
          * The person, engine or organization tht will ensure the license is honored.
          * @return String
          */
         public String getCustodian() {
             return this.custodian;
         }

         public void setCustodian(Object custodian) throws InvalidParameterException {
             // accept an object and cast to the appropriate type.

             final String methodName = "obtainInstanceProperties";
             try {
                   this.custodian = (String)custodian;
             } catch (ClassCastException e) {
                    SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.SET_ATTRIBUTE_WRONG_TYPE;
                    String errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(className,
                            methodName);
                    log.error(errorMessage,e);
                    throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());

             }
         }
         private String licensee;
         /**
          * The person or organization that holds the license.
          * @return String
          */
         public String getLicensee() {
             return this.licensee;
         }

         public void setLicensee(Object licensee) throws InvalidParameterException {
             // accept an object and cast to the appropriate type.

             final String methodName = "obtainInstanceProperties";
             try {
                   this.licensee = (String)licensee;
             } catch (ClassCastException e) {
                    SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.SET_ATTRIBUTE_WRONG_TYPE;
                    String errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(className,
                            methodName);
                    log.error(errorMessage,e);
                    throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());

             }
         }
         private String notes;
         /**
          * Additional notes about the license.
          * @return String
          */
         public String getNotes() {
             return this.notes;
         }

         public void setNotes(Object notes) throws InvalidParameterException {
             // accept an object and cast to the appropriate type.

             final String methodName = "obtainInstanceProperties";
             try {
                   this.notes = (String)notes;
             } catch (ClassCastException e) {
                    SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.SET_ATTRIBUTE_WRONG_TYPE;
                    String errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage(className,
                            methodName);
                    log.error(errorMessage,e);
                    throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());

             }
         }
}
