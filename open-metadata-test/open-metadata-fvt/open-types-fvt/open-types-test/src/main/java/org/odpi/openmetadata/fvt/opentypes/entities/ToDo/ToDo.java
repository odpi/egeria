/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.ToDo;

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
 * ToDo entity.
   An action assigned to an individual.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class  ToDo implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(ToDo.class);
    private static final String className = ToDo.class.getName();
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
        "creationTime",
        "priority",
        "dueTime",
        "status",
        "completionTime",
        "qualifiedName",
        "additionalProperties",

    // Terminate the list
        null
    };
    public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
        "name",
        "description",
        "creationTime",
        "priority",
        "dueTime",
        "completionTime",
        "qualifiedName",

     // Terminate the list
        null
    };
    public static final String[] ENUM_NAMES_SET_VALUES = new String[] {
         "status",

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
        enumPropertyValue = new EnumPropertyValue();
        // how complete is the action?
        enumPropertyValue.setOrdinal(status.ordinal());
        enumPropertyValue.setSymbolicName(status.name());
        instanceProperties.setProperty("status",enumPropertyValue);
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
        primitivePropertyValue.setPrimitiveValue(creationTime);
        instanceProperties.setProperty("creationTime",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(priority);
        instanceProperties.setProperty("priority",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(dueTime);
        instanceProperties.setProperty("dueTime",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(completionTime);
        instanceProperties.setProperty("completionTime",primitivePropertyValue);
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
        * {@literal Name or title of the todo/action. }
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
        * {@literal Description of the required action. }
        * @return {@code String }
        */
       public String getDescription() {
           return this.description;
       }
       public void setDescription(String description)  {
           this.description = description;
       }
       private Date creationTime;
       /**
        * {@literal When the requested action was identified. }
        * @return {@code Date }
        */
       public Date getCreationTime() {
           return this.creationTime;
       }
       public void setCreationTime(Date creationTime)  {
           this.creationTime = creationTime;
       }
       private Integer priority;
       /**
        * {@literal How urgent is this action? }
        * @return {@code Integer }
        */
       public Integer getPriority() {
           return this.priority;
       }
       public void setPriority(Integer priority)  {
           this.priority = priority;
       }
       private Date dueTime;
       /**
        * {@literal When the requested action needs to be completed. }
        * @return {@code Date }
        */
       public Date getDueTime() {
           return this.dueTime;
       }
       public void setDueTime(Date dueTime)  {
           this.dueTime = dueTime;
       }
       private ToDoStatus status;
       /**
        * {@literal How complete is the action? }
        * @return {@code ToDoStatus }
        */
       public ToDoStatus getStatus() {
           return this.status;
       }
       public void setStatus(ToDoStatus status)  {
           this.status = status;
       }
       private Date completionTime;
       /**
        * {@literal When the requested action was completed. }
        * @return {@code Date }
        */
       public Date getCompletionTime() {
           return this.completionTime;
       }
       public void setCompletionTime(Date completionTime)  {
           this.completionTime = completionTime;
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
     * Return the date/time that this ToDo should start to be used (null means it can be used from creationTime).
     * @return Date the ToDo becomes effective.
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
     * Return the date/time that this ToDo should no longer be used.
     *
     * @return Date the ToDo stops being effective.
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

        sb.append("ToDo{");
        if (systemAttributes !=null) {
            sb.append("systemAttributes='").append(systemAttributes.toString()).append('\'');
        }
        sb.append("ToDo Attributes{");
    	sb.append("Name=" +this.name);
    	sb.append("Description=" +this.description);
    	sb.append("CreationTime=" +this.creationTime);
    	sb.append("Priority=" +this.priority);
    	sb.append("DueTime=" +this.dueTime);
    	sb.append("Status=" +this.status);
    	sb.append("CompletionTime=" +this.completionTime);
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

        ToDo that = (ToDo) o;
        if (this.name != null && !Objects.equals(this.name,that.getName())) {
             return false;
        }
        if (this.description != null && !Objects.equals(this.description,that.getDescription())) {
             return false;
        }
        if (this.creationTime != null && !Objects.equals(this.creationTime,that.getCreationTime())) {
             return false;
        }
        if (this.priority != null && !Objects.equals(this.priority,that.getPriority())) {
             return false;
        }
        if (this.dueTime != null && !Objects.equals(this.dueTime,that.getDueTime())) {
             return false;
        }
        if (this.status != null && !Objects.equals(this.status,that.getStatus())) {
             return false;
        }
        if (this.completionTime != null && !Objects.equals(this.completionTime,that.getCompletionTime())) {
             return false;
        }
        if (this.qualifiedName != null && !Objects.equals(this.qualifiedName,that.getQualifiedName())) {
             return false;
        }
        if (this.additionalProperties != null && !Objects.equals(this.additionalProperties,that.getAdditionalProperties())) {
             return false;
        }

        // We view toDos as logically equal by checking the properties that the OMAS knows about - i.e. without accounting for extra attributes and references from the org.odpi.openmetadata.accessservices.subjectarea.server.
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
          , this.creationTime
          , this.priority
          , this.dueTime
          , this.status
          , this.completionTime
          , this.qualifiedName
          , this.additionalProperties
        );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
