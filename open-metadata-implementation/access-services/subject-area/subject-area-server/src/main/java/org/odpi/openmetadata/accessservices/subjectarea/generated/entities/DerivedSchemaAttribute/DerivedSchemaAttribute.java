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
package org.odpi.openmetadata.accessservices.subjectarea.generated.entities.DerivedSchemaAttribute;

import java.io.Serializable;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

// omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;

// omas
import org.odpi.openmetadata.accessservices.subjectarea.common.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.common.Reference;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.GovernanceActions;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.enums.*;
import org.odpi.openmetadata.accessservices.subjectarea.common.Classification;

/**
 * DerivedSchemaAttribute entity in the Subject Area OMAS.
   An attribute that is made up of values from another attribute.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class  DerivedSchemaAttribute implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(DerivedSchemaAttribute.class);
    private static final String className = DerivedSchemaAttribute.class.getName();
    private SystemAttributes systemAttributes = null;
    List<Classification> classifications = null;

    private Map<String, Object> extraAttributes =null;
    private Map<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> extraClassifications =null;


    /**
     * Get the system attributes
     * @return
     */
    public SystemAttributes getSystemAttributes() {
        return systemAttributes;
    }

    public void setSystemAttributes(SystemAttributes systemAttributes) {
        this.systemAttributes = systemAttributes;
    }

    // attributes
    public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
        "formula",
        "name",
        "position",
        "cardinality",
        "defaultValueOverride",
        "qualifiedName",
        "additionalProperties",

    // Terminate the list
        null
    };
    public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
        "formula",
        "name",
        "position",
        "cardinality",
        "defaultValueOverride",
        "qualifiedName",

     // Terminate the list
        null
    };
    public static final String[] ENUM_NAMES_SET_VALUES = new String[] {

         // Terminate the list
          null
    };
    public static final String[] MAP_NAMES_SET_VALUES = new String[] {
         "additionalProperties",

         // Terminate the list
         null
    };
    public static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES)));
    public static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES)));
    public static final java.util.Set<String> ENUM_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES)));
    public static final java.util.Set<String> MAP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES)));


    InstanceProperties obtainInstanceProperties() {
        final String methodName = "obtainInstanceProperties";
        if (log.isDebugEnabled()) {
               log.debug("==> Method: " + methodName);
        }
        InstanceProperties instanceProperties = new InstanceProperties();
        EnumPropertyValue enumPropertyValue=null;
        MapPropertyValue mapPropertyValue=null;
        // Additional properties for the element.
        mapPropertyValue = new MapPropertyValue();
        PrimitivePropertyValue primitivePropertyValue=null;
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("formula",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("name",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("position",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("cardinality",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("defaultValueOverride",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("qualifiedName",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("additionalProperties",primitivePropertyValue);
        if (log.isDebugEnabled()) {
               log.debug("<== Method: " + methodName);
        }
        return instanceProperties;
    }

       private String formula;
       /**
        * Transformation used to create the derived data.
        * @return String
        */
       public String getFormula() {
           return this.formula;
       }

       public void setFormula(Object formula) throws InvalidParameterException {
           // accept an object and cast to the appropriate type.

           final String methodName = "obtainInstanceProperties";
           try {
                 this.formula = (String)formula;
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
       private String name;
       /**
        * Name of the attribute.
        * @return String
        */
       public String getName() {
           return this.name;
       }

       public void setName(Object name) throws InvalidParameterException {
           // accept an object and cast to the appropriate type.

           final String methodName = "obtainInstanceProperties";
           try {
                 this.name = (String)name;
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
       private Integer position;
       /**
        * Position in the schema, starting at zero.
        * @return Integer
        */
       public Integer getPosition() {
           return this.position;
       }

       public void setPosition(Object position) throws InvalidParameterException {
           // accept an object and cast to the appropriate type.

           final String methodName = "obtainInstanceProperties";
           try {
                 this.position = (Integer)position;
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
       private String cardinality;
       /**
        * Number of occurrences of this attribute allowed.
        * @return String
        */
       public String getCardinality() {
           return this.cardinality;
       }

       public void setCardinality(Object cardinality) throws InvalidParameterException {
           // accept an object and cast to the appropriate type.

           final String methodName = "obtainInstanceProperties";
           try {
                 this.cardinality = (String)cardinality;
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
       private String defaultValueOverride;
       /**
        * Initial value for the attribute (overriding the default value of its type.
        * @return String
        */
       public String getDefaultValueOverride() {
           return this.defaultValueOverride;
       }

       public void setDefaultValueOverride(Object defaultValueOverride) throws InvalidParameterException {
           // accept an object and cast to the appropriate type.

           final String methodName = "obtainInstanceProperties";
           try {
                 this.defaultValueOverride = (String)defaultValueOverride;
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
       private String qualifiedName;
       /**
        * Unique identifier for the entity.
        * @return String
        */
       public String getQualifiedName() {
           return this.qualifiedName;
       }

       public void setQualifiedName(Object qualifiedName) throws InvalidParameterException {
           // accept an object and cast to the appropriate type.

           final String methodName = "obtainInstanceProperties";
           try {
                 this.qualifiedName = (String)qualifiedName;
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
       private Map<String,String> additionalProperties;
       /**
        * Additional properties for the element.
        * @return Map<String,String>
        */
       public Map<String,String> getAdditionalProperties() {
           return this.additionalProperties;
       }

       public void setAdditionalProperties(Object additionalProperties) throws InvalidParameterException {
           // accept an object and cast to the appropriate type.

           final String methodName = "obtainInstanceProperties";
           try {
                 this.additionalProperties = (Map<String,String>)additionalProperties;
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


    public void setExtraAttributes(Map<String, Object> extraAttributes) {
        this.extraAttributes = extraAttributes;
    }

    public void setClassifications(List<Classification> classifications) {
        this.classifications = classifications;
    }

    /**
     * Get the extra attributes - ones that are in addition to the standard types.
     * @return
     */
    public Map<String, Object> getExtraAttributes() {
        return extraAttributes;
    }

     /**
     * Classifications
     * @return
     */
    public List<Classification> getClassifications() {
        return classifications;
    }
    /**
      * Extra classifications are classifications that are not in the open metadata model - we include the OMRS Classifications.
      */
    public Map<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> getExtraClassifications() {
        return extraClassifications;
    }

    public void setExtraClassifications(Map<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> extraClassifications) {
        this.extraClassifications = extraClassifications;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("DerivedSchemaAttribute{");
        if (systemAttributes !=null) {
            sb.append("systemAttributes='").append(systemAttributes.toString()).append('\'');
        }
        sb.append("DerivedSchemaAttribute Attributes{");
    	sb.append("Formula=" +this.formula);
    	sb.append("Name=" +this.name);
    	sb.append("Position=" +this.position);
    	sb.append("Cardinality=" +this.cardinality);
    	sb.append("DefaultValueOverride=" +this.defaultValueOverride);
    	sb.append("QualifiedName=" +this.qualifiedName);
    	sb.append("AdditionalProperties=" +this.additionalProperties);

        sb.append('}');
        if (classifications != null) {
        sb.append(", classifications=[");
            for (Classification classification:classifications) {
                sb.append(classification.toString()).append(", ");
            }
            sb.append(" ],");
        }
        sb.append(", extraAttributes=[");
        if (extraAttributes !=null) {
            for (String attrname: extraAttributes.keySet()) {
                sb.append(attrname).append(":");
                sb.append(extraAttributes.get(attrname)).append(", ");
            }
        }
        sb.append(" ]");

        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }

        DerivedSchemaAttribute that = (DerivedSchemaAttribute) o;
        if (this.formula != null && !Objects.equals(this.formula,that.getFormula())) {
             return false;
        }
        if (this.name != null && !Objects.equals(this.name,that.getName())) {
             return false;
        }
        if (this.position != null && !Objects.equals(this.position,that.getPosition())) {
             return false;
        }
        if (this.cardinality != null && !Objects.equals(this.cardinality,that.getCardinality())) {
             return false;
        }
        if (this.defaultValueOverride != null && !Objects.equals(this.defaultValueOverride,that.getDefaultValueOverride())) {
             return false;
        }
        if (this.qualifiedName != null && !Objects.equals(this.qualifiedName,that.getQualifiedName())) {
             return false;
        }
        if (this.additionalProperties != null && !Objects.equals(this.additionalProperties,that.getAdditionalProperties())) {
             return false;
        }

        // We view derivedSchemaAttributes as logically equal by checking the properties that the OMAS knows about - i.e. without accounting for extra attributes and references from the org.odpi.openmetadata.accessservices.subjectarea.server.
        return Objects.equals(systemAttributes, that.systemAttributes) &&
                Objects.equals(classifications, that.classifications) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
         systemAttributes.hashCode(),
         classifications.hashCode()
          , this.formula
          , this.name
          , this.position
          , this.cardinality
          , this.defaultValueOverride
          , this.qualifiedName
          , this.additionalProperties
        );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
