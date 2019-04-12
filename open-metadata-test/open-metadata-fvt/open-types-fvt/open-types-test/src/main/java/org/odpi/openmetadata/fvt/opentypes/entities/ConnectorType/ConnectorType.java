/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.ConnectorType;

import java.io.Serializable;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.fvt.opentypes.common.SystemAttributes;
import org.odpi.openmetadata.fvt.opentypes.common.ClassificationBean;
import org.odpi.openmetadata.fvt.opentypes.enums.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

// omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;

/**
 * ConnectorType entity.
   A set of properties describing a type of connector.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class  ConnectorType implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(ConnectorType.class);
    private static final String className = ConnectorType.class.getName();
    private SystemAttributes systemAttributes = null;
    private Date effectiveFromTime = null;
    private Date effectiveToTime = null;
    List<ClassificationBean> classifications = null;

    private Map<String, Object> extraAttributes =null;
    private Map<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> extraClassificationBeans =null;


    /**
     * Get the system attributes
     * @return SystemAttributes if populated, null otherwise.
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
        primitivePropertyValue.setPrimitiveValue(displayName);
        instanceProperties.setProperty("displayName",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(description);
        instanceProperties.setProperty("description",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(connectorProviderClassName);
        instanceProperties.setProperty("connectorProviderClassName",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(recognizedAdditionalProperties);
        instanceProperties.setProperty("recognizedAdditionalProperties",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(recognizedSecuredProperties);
        instanceProperties.setProperty("recognizedSecuredProperties",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(qualifiedName);
        instanceProperties.setProperty("qualifiedName",primitivePropertyValue);
        if (log.isDebugEnabled()) {
               log.debug("<== Method: " + methodName);
        }
        return instanceProperties;
    }

       private String displayName;
       /**
        * {@literal Consumable name for the connector type, suitable for reports and user interfaces. }
        * @return {@code String }
        */
       public String getDisplayName() {
           return this.displayName;
       }
       public void setDisplayName(String displayName)  {
           this.displayName = displayName;
       }
       private String description;
       /**
        * {@literal Description of the connector type. }
        * @return {@code String }
        */
       public String getDescription() {
           return this.description;
       }
       public void setDescription(String description)  {
           this.description = description;
       }
       private String connectorProviderClassName;
       /**
        * {@literal Name of the Java class that implements this connector type's open connector framework (OCF) connector provider. }
        * @return {@code String }
        */
       public String getConnectorProviderClassName() {
           return this.connectorProviderClassName;
       }
       public void setConnectorProviderClassName(String connectorProviderClassName)  {
           this.connectorProviderClassName = connectorProviderClassName;
       }
       private List<String> recognizedAdditionalProperties;
       /**
        * {@literal List of additional connection property names supported by the connector implementation. }
        * @return {@code {@code List<String> } }
        */
       public List<String> getRecognizedAdditionalProperties() {
           return this.recognizedAdditionalProperties;
       }
       public void setRecognizedAdditionalProperties(List<String> recognizedAdditionalProperties)  {
           this.recognizedAdditionalProperties = recognizedAdditionalProperties;
       }
       private List<String> recognizedSecuredProperties;
       /**
        * {@literal List of secured connection property names supported by the connector implementation. }
        * @return {@code {@code List<String> } }
        */
       public List<String> getRecognizedSecuredProperties() {
           return this.recognizedSecuredProperties;
       }
       public void setRecognizedSecuredProperties(List<String> recognizedSecuredProperties)  {
           this.recognizedSecuredProperties = recognizedSecuredProperties;
       }
       private String qualifiedName;
       /**
        * {@literal Unique identifier for the entity. }
        * @return {@code String }
        */
       public String getQualifiedName() {
           return this.qualifiedName;
       }
       public void setQualifiedName(String qualifiedName)  {
           this.qualifiedName = qualifiedName;
       }
       private Map<String,String> additionalProperties;
       /**
        * {@literal Additional properties for the element. }
        * @return {@code {@code Map<String,String> } }
        */
       public Map<String,String> getAdditionalProperties() {
           return this.additionalProperties;
       }
       public void setAdditionalProperties(Map<String,String> additionalProperties)  {
           this.additionalProperties = additionalProperties;
       }
    /**
     * Return the date/time that this ConnectorType should start to be used (null means it can be used from creationTime).
     * @return Date the ConnectorType becomes effective.
     */
    public Date getEffectiveFromTime()
    {
        return effectiveFromTime;
    }

    public void setEffectiveFromTime(Date effectiveFromTime)
    {
        this.effectiveFromTime = effectiveFromTime;
    }
    /**
     * Return the date/time that this ConnectorType should no longer be used.
     *
     * @return Date the ConnectorType stops being effective.
     */
    public Date getEffectiveToTime()
    {
        return effectiveToTime;
    }
    public void setEffectiveToTime(Date effectiveToTime)
    {
        this.effectiveToTime = effectiveToTime;
    }

    public void setExtraAttributes(Map<String, Object> extraAttributes) {
        this.extraAttributes = extraAttributes;
    }

    public void setClassificationBeans(List<ClassificationBean> classifications) {
        this.classifications = classifications;
    }

    /**
     * Get the extra attributes - ones that are in addition to the standard types.
     * @return map of attributes, null if there are none
     */
    public Map<String, Object> getExtraAttributes() {
        return extraAttributes;
    }

     /**
     * ClassificationBeans
     * @return List of ClassificationBeans, null if there are none
     */
    public List<ClassificationBean> getClassificationBeans() {
        return classifications;
    }
    /**
      * Extra classifications are classifications that are not in the open metadata model - we include the OMRS ClassificationBeans.
      * @return Map of classifications with the classification Name as the map key
      */
    public Map<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> getExtraClassificationBeans() {
        return extraClassificationBeans;
    }

    public void setExtraClassificationBeans(Map<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> extraClassificationBeans) {
        this.extraClassificationBeans = extraClassificationBeans;
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
            for (ClassificationBean classification:classifications) {
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
