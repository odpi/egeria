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
package org.odpi.openmetadata.accessservices.subjectarea.generated.entities.ConnectorType;

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
 * ConnectorType entity in the Subject Area OMAS.
   A set of properties describing a type of connector.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class  ConnectorType implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(ConnectorType.class);
    private static final String className = ConnectorType.class.getName();
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
        "displayName",
        "description",
        "connectorProviderClassName",
        "recognizedAdditionalProperties",
        "recognizedSecuredProperties",
        "qualifiedName",
        "additionalProperties",

    // Terminate the list
        null
    };
    public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
        "displayName",
        "description",
        "connectorProviderClassName",
        "recognizedAdditionalProperties",
        "recognizedSecuredProperties",
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
        instanceProperties.setProperty("displayName",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("description",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("connectorProviderClassName",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("recognizedAdditionalProperties",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        // TODO  description + change null to value
        primitivePropertyValue.setPrimitiveValue(null);
        instanceProperties.setProperty("recognizedSecuredProperties",primitivePropertyValue);
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

       private String displayName;
       /**
        * Consumable name for the connector type, suitable for reports and user interfaces.
        * @return String
        */
       public String getDisplayName() {
           return this.displayName;
       }

       public void setDisplayName(Object displayName) throws InvalidParameterException {
           // accept an object and cast to the appropriate type.

           final String methodName = "obtainInstanceProperties";
           try {
                 this.displayName = (String)displayName;
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
       private String description;
       /**
        * Description of the connector type.
        * @return String
        */
       public String getDescription() {
           return this.description;
       }

       public void setDescription(Object description) throws InvalidParameterException {
           // accept an object and cast to the appropriate type.

           final String methodName = "obtainInstanceProperties";
           try {
                 this.description = (String)description;
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
       private String connectorProviderClassName;
       /**
        * Name of the Java class that implements this connector type's open connector framework (OCF) connector provider.
        * @return String
        */
       public String getConnectorProviderClassName() {
           return this.connectorProviderClassName;
       }

       public void setConnectorProviderClassName(Object connectorProviderClassName) throws InvalidParameterException {
           // accept an object and cast to the appropriate type.

           final String methodName = "obtainInstanceProperties";
           try {
                 this.connectorProviderClassName = (String)connectorProviderClassName;
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
       private List<String> recognizedAdditionalProperties;
       /**
        * List of additional connection property names supported by the connector implementation.
        * @return List<String>
        */
       public List<String> getRecognizedAdditionalProperties() {
           return this.recognizedAdditionalProperties;
       }

       public void setRecognizedAdditionalProperties(Object recognizedAdditionalProperties) throws InvalidParameterException {
           // accept an object and cast to the appropriate type.

           final String methodName = "obtainInstanceProperties";
           try {
                 this.recognizedAdditionalProperties = (List<String>)recognizedAdditionalProperties;
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
       private List<String> recognizedSecuredProperties;
       /**
        * List of secured connection property names supported by the connector implementation.
        * @return List<String>
        */
       public List<String> getRecognizedSecuredProperties() {
           return this.recognizedSecuredProperties;
       }

       public void setRecognizedSecuredProperties(Object recognizedSecuredProperties) throws InvalidParameterException {
           // accept an object and cast to the appropriate type.

           final String methodName = "obtainInstanceProperties";
           try {
                 this.recognizedSecuredProperties = (List<String>)recognizedSecuredProperties;
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

        sb.append("ConnectorType{");
        if (systemAttributes !=null) {
            sb.append("systemAttributes='").append(systemAttributes.toString()).append('\'');
        }
        sb.append("ConnectorType Attributes{");
    	sb.append("DisplayName=" +this.displayName);
    	sb.append("Description=" +this.description);
    	sb.append("ConnectorProviderClassName=" +this.connectorProviderClassName);
    	sb.append("RecognizedAdditionalProperties=" +this.recognizedAdditionalProperties);
    	sb.append("RecognizedSecuredProperties=" +this.recognizedSecuredProperties);
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

        ConnectorType that = (ConnectorType) o;
        if (this.displayName != null && !Objects.equals(this.displayName,that.getDisplayName())) {
             return false;
        }
        if (this.description != null && !Objects.equals(this.description,that.getDescription())) {
             return false;
        }
        if (this.connectorProviderClassName != null && !Objects.equals(this.connectorProviderClassName,that.getConnectorProviderClassName())) {
             return false;
        }
        if (this.recognizedAdditionalProperties != null && !Objects.equals(this.recognizedAdditionalProperties,that.getRecognizedAdditionalProperties())) {
             return false;
        }
        if (this.recognizedSecuredProperties != null && !Objects.equals(this.recognizedSecuredProperties,that.getRecognizedSecuredProperties())) {
             return false;
        }
        if (this.qualifiedName != null && !Objects.equals(this.qualifiedName,that.getQualifiedName())) {
             return false;
        }
        if (this.additionalProperties != null && !Objects.equals(this.additionalProperties,that.getAdditionalProperties())) {
             return false;
        }

        // We view connectorTypes as logically equal by checking the properties that the OMAS knows about - i.e. without accounting for extra attributes and references from the org.odpi.openmetadata.accessservices.subjectarea.server.
        return Objects.equals(systemAttributes, that.systemAttributes) &&
                Objects.equals(classifications, that.classifications) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
         systemAttributes.hashCode(),
         classifications.hashCode()
          , this.displayName
          , this.description
          , this.connectorProviderClassName
          , this.recognizedAdditionalProperties
          , this.recognizedSecuredProperties
          , this.qualifiedName
          , this.additionalProperties
        );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
