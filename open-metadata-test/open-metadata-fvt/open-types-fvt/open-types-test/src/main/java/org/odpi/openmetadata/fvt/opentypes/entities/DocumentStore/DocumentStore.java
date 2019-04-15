/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.DocumentStore;

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
 * DocumentStore entity.
   Identifies a data store as one that contains documents.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class  DocumentStore implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(DocumentStore.class);
    private static final String className = DocumentStore.class.getName();
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
        "createTime",
        "modifiedTime",
        "name",
        "description",
        "owner",
        "ownerType",
        "zoneMembership",
        "latestChange",
        "qualifiedName",
        "additionalProperties",

    // Terminate the list
        null
    };
    public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
        "createTime",
        "modifiedTime",
        "name",
        "description",
        "owner",
        "zoneMembership",
        "latestChange",
        "qualifiedName",

     // Terminate the list
        null
    };
    public static final String[] ENUM_NAMES_SET_VALUES = new String[] {
         "ownerType",

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
        // type of identifier used for owner property.
        enumPropertyValue.setOrdinal(ownerType.ordinal());
        enumPropertyValue.setSymbolicName(ownerType.name());
        instanceProperties.setProperty("ownerType",enumPropertyValue);
        MapPropertyValue mapPropertyValue=null;
        // Additional properties for the element.
        mapPropertyValue = new MapPropertyValue();
        PrimitivePropertyValue primitivePropertyValue=null;
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(createTime);
        instanceProperties.setProperty("createTime",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(modifiedTime);
        instanceProperties.setProperty("modifiedTime",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(name);
        instanceProperties.setProperty("name",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(description);
        instanceProperties.setProperty("description",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(owner);
        instanceProperties.setProperty("owner",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(zoneMembership);
        instanceProperties.setProperty("zoneMembership",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(latestChange);
        instanceProperties.setProperty("latestChange",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(qualifiedName);
        instanceProperties.setProperty("qualifiedName",primitivePropertyValue);
        if (log.isDebugEnabled()) {
               log.debug("<== Method: " + methodName);
        }
        return instanceProperties;
    }

       private Date createTime;
       /**
        * {@literal Creation time of the data store. }
        * @return {@code Date }
        */
       public Date getCreateTime() {
           return this.createTime;
       }
       public void setCreateTime(Date createTime)  {
           this.createTime = createTime;
       }
       private Date modifiedTime;
       /**
        * {@literal Last known modification time. }
        * @return {@code Date }
        */
       public Date getModifiedTime() {
           return this.modifiedTime;
       }
       public void setModifiedTime(Date modifiedTime)  {
           this.modifiedTime = modifiedTime;
       }
       private String name;
       /**
        * {@literal Display name for the asset. }
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
        * {@literal Description of the asset. }
        * @return {@code String }
        */
       public String getDescription() {
           return this.description;
       }
       public void setDescription(String description)  {
           this.description = description;
       }
       private String owner;
       /**
        * {@literal Identifier of the person or process that owns the asset. }
        * @return {@code String }
        */
       public String getOwner() {
           return this.owner;
       }
       public void setOwner(String owner)  {
           this.owner = owner;
       }
       private AssetOwnerType ownerType;
       /**
        * {@literal Type of identifier used for owner property. }
        * @return {@code AssetOwnerType }
        */
       public AssetOwnerType getOwnerType() {
           return this.ownerType;
       }
       public void setOwnerType(AssetOwnerType ownerType)  {
           this.ownerType = ownerType;
       }
       private List<String> zoneMembership;
       /**
        * {@literal The list of zones that this asset belongs to. }
        * @return {@code {@code List<String> } }
        */
       public List<String> getZoneMembership() {
           return this.zoneMembership;
       }
       public void setZoneMembership(List<String> zoneMembership)  {
           this.zoneMembership = zoneMembership;
       }
       private String latestChange;
       /**
        * {@literal Description of the last change to the asset's metadata. }
        * @return {@code String }
        */
       public String getLatestChange() {
           return this.latestChange;
       }
       public void setLatestChange(String latestChange)  {
           this.latestChange = latestChange;
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
     * Return the date/time that this DocumentStore should start to be used (null means it can be used from creationTime).
     * @return Date the DocumentStore becomes effective.
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
     * Return the date/time that this DocumentStore should no longer be used.
     *
     * @return Date the DocumentStore stops being effective.
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

        sb.append("DocumentStore{");
        if (systemAttributes !=null) {
            sb.append("systemAttributes='").append(systemAttributes.toString()).append('\'');
        }
        sb.append("DocumentStore Attributes{");
    	sb.append("CreateTime=" +this.createTime);
    	sb.append("ModifiedTime=" +this.modifiedTime);
    	sb.append("Name=" +this.name);
    	sb.append("Description=" +this.description);
    	sb.append("Owner=" +this.owner);
    	sb.append("OwnerType=" +this.ownerType);
    	sb.append("ZoneMembership=" +this.zoneMembership);
    	sb.append("LatestChange=" +this.latestChange);
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

        DocumentStore that = (DocumentStore) o;
        if (this.createTime != null && !Objects.equals(this.createTime,that.getCreateTime())) {
             return false;
        }
        if (this.modifiedTime != null && !Objects.equals(this.modifiedTime,that.getModifiedTime())) {
             return false;
        }
        if (this.name != null && !Objects.equals(this.name,that.getName())) {
             return false;
        }
        if (this.description != null && !Objects.equals(this.description,that.getDescription())) {
             return false;
        }
        if (this.owner != null && !Objects.equals(this.owner,that.getOwner())) {
             return false;
        }
        if (this.ownerType != null && !Objects.equals(this.ownerType,that.getOwnerType())) {
             return false;
        }
        if (this.zoneMembership != null && !Objects.equals(this.zoneMembership,that.getZoneMembership())) {
             return false;
        }
        if (this.latestChange != null && !Objects.equals(this.latestChange,that.getLatestChange())) {
             return false;
        }
        if (this.qualifiedName != null && !Objects.equals(this.qualifiedName,that.getQualifiedName())) {
             return false;
        }
        if (this.additionalProperties != null && !Objects.equals(this.additionalProperties,that.getAdditionalProperties())) {
             return false;
        }

        // We view documentStores as logically equal by checking the properties that the OMAS knows about - i.e. without accounting for extra attributes and references from the org.odpi.openmetadata.accessservices.subjectarea.server.
        return Objects.equals(systemAttributes, that.systemAttributes) &&
                Objects.equals(classifications, that.classifications) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
         systemAttributes.hashCode(),
         classifications.hashCode()
          , this.createTime
          , this.modifiedTime
          , this.name
          , this.description
          , this.owner
          , this.ownerType
          , this.zoneMembership
          , this.latestChange
          , this.qualifiedName
          , this.additionalProperties
        );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
