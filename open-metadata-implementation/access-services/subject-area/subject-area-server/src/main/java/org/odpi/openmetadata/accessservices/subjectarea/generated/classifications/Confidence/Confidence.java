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
package org.odpi.openmetadata.accessservices.subjectarea.generated.classifications.Confidence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;

import org.odpi.openmetadata.accessservices.subjectarea.common.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;

import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import org.odpi.openmetadata.accessservices.subjectarea.generated.enums.*;

/**
 * Defines the level of confidence that should be placed in the accuracy of related data items.
 */
public class Confidence extends Classification {
    private static final Logger log = LoggerFactory.getLogger( Confidence.class);
    private static final String className =  Confidence.class.getName();
    protected String classificationName = "Confidence";
    private Map<String, Object> extraAttributes;

 public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
        "status",
        "confidence",
        "steward",
        "source",
        "notes",
        "level",

    // Terminate the list
        null
    };
    public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
        "confidence",
        "steward",
        "source",
        "notes",

     // Terminate the list
        null
    };
    public static final String[] ENUM_NAMES_SET_VALUES = new String[] {
         "status",
         "level",

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

    @Override
    public InstanceProperties obtainInstanceProperties() {
        final String methodName = "obtainInstanceProperties";
        if (log.isDebugEnabled()) {
               log.debug("==> Method: " + methodName);
        }
        InstanceProperties instanceProperties = new InstanceProperties();
        EnumPropertyValue enumPropertyValue=null;
        enumPropertyValue = new EnumPropertyValue();
        // status of this classification.
        enumPropertyValue.setOrdinal(status.ordinal());
        enumPropertyValue.setSymbolicName(status.name());
        instanceProperties.setProperty("status",enumPropertyValue);
        enumPropertyValue = new EnumPropertyValue();
        // level of confidence in the quality of this data.
        enumPropertyValue.setOrdinal(level.ordinal());
        enumPropertyValue.setSymbolicName(level.name());
        instanceProperties.setProperty("level",enumPropertyValue);
        MapPropertyValue mapPropertyValue=null;
        PrimitivePropertyValue primitivePropertyValue=null;
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("status",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("confidence",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("steward",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("source",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("notes",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("level",primitivePropertyValue);
        if (log.isDebugEnabled()) {
               log.debug("<== Method: " + methodName);
        }
        return instanceProperties;
    }

       private GovernanceClassificationStatus status;
       /**
        * Status of this classification.
        * @return GovernanceClassificationStatus
        */
       public GovernanceClassificationStatus getStatus() {
           return this.status;
       }

       public void setStatus(Object status) throws InvalidParameterException {
           // accept an object and cast to the appropriate type.

           final String methodName = "obtainInstanceProperties";
           try {
                 this.status = (GovernanceClassificationStatus)status;
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
       private Integer confidence;
       /**
        * Level of confidence in the classification (0=none -> 100=excellent).
        * @return Integer
        */
       public Integer getConfidence() {
           return this.confidence;
       }

       public void setConfidence(Object confidence) throws InvalidParameterException {
           // accept an object and cast to the appropriate type.

           final String methodName = "obtainInstanceProperties";
           try {
                 this.confidence = (Integer)confidence;
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
       private String steward;
       /**
        * Person responsible for maintaining this classification.
        * @return String
        */
       public String getSteward() {
           return this.steward;
       }

       public void setSteward(Object steward) throws InvalidParameterException {
           // accept an object and cast to the appropriate type.

           final String methodName = "obtainInstanceProperties";
           try {
                 this.steward = (String)steward;
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
       private String source;
       /**
        * Source of the classification.
        * @return String
        */
       public String getSource() {
           return this.source;
       }

       public void setSource(Object source) throws InvalidParameterException {
           // accept an object and cast to the appropriate type.

           final String methodName = "obtainInstanceProperties";
           try {
                 this.source = (String)source;
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
        * Information relating to the classification.
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
       private ConfidenceLevel level;
       /**
        * Level of confidence in the quality of this data.
        * @return ConfidenceLevel
        */
       public ConfidenceLevel getLevel() {
           return this.level;
       }

       public void setLevel(Object level) throws InvalidParameterException {
           // accept an object and cast to the appropriate type.

           final String methodName = "obtainInstanceProperties";
           try {
                 this.level = (ConfidenceLevel)level;
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

    /**
      * Get the extra attributes - ones that are in addition to the standard types.
      * @return extra attributes
      */
    public Map<String, Object> getExtraAttributes() {
          return extraAttributes;
    }
    public void setExtraAttributes(Map<String, Object> extraAttributes) {
          this.extraAttributes = extraAttributes;
    }
}
