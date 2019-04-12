/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.DataClass;

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
 * DataClass entity.
   A logical data type specification.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class  DataClass implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(DataClass.class);
    private static final String className = DataClass.class.getName();
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
        "name",
        "description",
        "classCode",
        "userDefined",
        "namespace",
        "specification",
        "specificationDetails",
        "dataType",
        "defaultThreshold",
        "example",
        "qualifiedName",
        "additionalProperties",

    // Terminate the list
        null
    };
    public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
        "name",
        "description",
        "classCode",
        "userDefined",
        "namespace",
        "specification",
        "specificationDetails",
        "dataType",
        "defaultThreshold",
        "example",
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
        primitivePropertyValue.setPrimitiveValue(name);
        instanceProperties.setProperty("name",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(description);
        instanceProperties.setProperty("description",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(classCode);
        instanceProperties.setProperty("classCode",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(userDefined);
        instanceProperties.setProperty("userDefined",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(namespace);
        instanceProperties.setProperty("namespace",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(specification);
        instanceProperties.setProperty("specification",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(specificationDetails);
        instanceProperties.setProperty("specificationDetails",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(dataType);
        instanceProperties.setProperty("dataType",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(defaultThreshold);
        instanceProperties.setProperty("defaultThreshold",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(example);
        instanceProperties.setProperty("example",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(qualifiedName);
        instanceProperties.setProperty("qualifiedName",primitivePropertyValue);
        if (log.isDebugEnabled()) {
               log.debug("<== Method: " + methodName);
        }
        return instanceProperties;
    }

       private String name;
       /**
        * {@literal Display name of the data class. }
        * @return {@code String }
        */
       public String getName() {
           return this.name;
       }
       public void setName(String name)  {
           this.name = name;
       }
       private String description;
       /**
        * {@literal Description of the data class. }
        * @return {@code String }
        */
       public String getDescription() {
           return this.description;
       }
       public void setDescription(String description)  {
           this.description = description;
       }
       private String classCode;
       /**
        * {@literal Name of processing class that can identify the data class. }
        * @return {@code String }
        */
       public String getClassCode() {
           return this.classCode;
       }
       public void setClassCode(String classCode)  {
           this.classCode = classCode;
       }
       private Boolean userDefined;
       /**
        * {@literal Defined by owning organization rather than vendor. }
        * @return {@code Boolean }
        */
       public Boolean getUserDefined() {
           return this.userDefined;
       }
       public void setUserDefined(Boolean userDefined)  {
           this.userDefined = userDefined;
       }
       private String namespace;
       /**
        * {@literal Logical group for this data class. }
        * @return {@code String }
        */
       public String getNamespace() {
           return this.namespace;
       }
       public void setNamespace(String namespace)  {
           this.namespace = namespace;
       }
       private String specification;
       /**
        * {@literal Parsing string used to identify values of this data class. }
        * @return {@code String }
        */
       public String getSpecification() {
           return this.specification;
       }
       public void setSpecification(String specification)  {
           this.specification = specification;
       }
       private String specificationDetails;
       /**
        * {@literal Additional properties used in the specification. }
        * @return {@code String }
        */
       public String getSpecificationDetails() {
           return this.specificationDetails;
       }
       public void setSpecificationDetails(String specificationDetails)  {
           this.specificationDetails = specificationDetails;
       }
       private String dataType;
       /**
        * {@literal Typical data type used to store this value. }
        * @return {@code String }
        */
       public String getDataType() {
           return this.dataType;
       }
       public void setDataType(String dataType)  {
           this.dataType = dataType;
       }
       private Float defaultThreshold;
       /**
        * {@literal Match threshold that a data field is expected to achieve to be assigned this data class. }
        * @return {@code Float }
        */
       public Float getDefaultThreshold() {
           return this.defaultThreshold;
       }
       public void setDefaultThreshold(Float defaultThreshold)  {
           this.defaultThreshold = defaultThreshold;
       }
       private String example;
       /**
        * {@literal Example of a data value that matches this data class. }
        * @return {@code String }
        */
       public String getExample() {
           return this.example;
       }
       public void setExample(String example)  {
           this.example = example;
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
     * Return the date/time that this DataClass should start to be used (null means it can be used from creationTime).
     * @return Date the DataClass becomes effective.
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
     * Return the date/time that this DataClass should no longer be used.
     *
     * @return Date the DataClass stops being effective.
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

        sb.append("DataClass{");
        if (systemAttributes !=null) {
            sb.append("systemAttributes='").append(systemAttributes.toString()).append('\'');
        }
        sb.append("DataClass Attributes{");
    	sb.append("Name=" +this.name);
    	sb.append("Description=" +this.description);
    	sb.append("ClassCode=" +this.classCode);
    	sb.append("UserDefined=" +this.userDefined);
    	sb.append("Namespace=" +this.namespace);
    	sb.append("Specification=" +this.specification);
    	sb.append("SpecificationDetails=" +this.specificationDetails);
    	sb.append("DataType=" +this.dataType);
    	sb.append("DefaultThreshold=" +this.defaultThreshold);
    	sb.append("Example=" +this.example);
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

        DataClass that = (DataClass) o;
        if (this.name != null && !Objects.equals(this.name,that.getName())) {
             return false;
        }
        if (this.description != null && !Objects.equals(this.description,that.getDescription())) {
             return false;
        }
        if (this.classCode != null && !Objects.equals(this.classCode,that.getClassCode())) {
             return false;
        }
        if (this.userDefined != null && !Objects.equals(this.userDefined,that.getUserDefined())) {
             return false;
        }
        if (this.namespace != null && !Objects.equals(this.namespace,that.getNamespace())) {
             return false;
        }
        if (this.specification != null && !Objects.equals(this.specification,that.getSpecification())) {
             return false;
        }
        if (this.specificationDetails != null && !Objects.equals(this.specificationDetails,that.getSpecificationDetails())) {
             return false;
        }
        if (this.dataType != null && !Objects.equals(this.dataType,that.getDataType())) {
             return false;
        }
        if (this.defaultThreshold != null && !Objects.equals(this.defaultThreshold,that.getDefaultThreshold())) {
             return false;
        }
        if (this.example != null && !Objects.equals(this.example,that.getExample())) {
             return false;
        }
        if (this.qualifiedName != null && !Objects.equals(this.qualifiedName,that.getQualifiedName())) {
             return false;
        }
        if (this.additionalProperties != null && !Objects.equals(this.additionalProperties,that.getAdditionalProperties())) {
             return false;
        }

        // We view dataClasss as logically equal by checking the properties that the OMAS knows about - i.e. without accounting for extra attributes and references from the org.odpi.openmetadata.accessservices.subjectarea.server.
        return Objects.equals(systemAttributes, that.systemAttributes) &&
                Objects.equals(classifications, that.classifications) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
         systemAttributes.hashCode(),
         classifications.hashCode()
          , this.name
          , this.description
          , this.classCode
          , this.userDefined
          , this.namespace
          , this.specification
          , this.specificationDetails
          , this.dataType
          , this.defaultThreshold
          , this.example
          , this.qualifiedName
          , this.additionalProperties
        );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
