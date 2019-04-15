/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.Like;

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
 * Like entity.
   Boolean type of rating expressing a favorable impression.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class  Like implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Like.class);
    private static final String className = Like.class.getName();
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

    // Terminate the list
        null
    };
    public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {

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


    InstanceProperties obtainInstanceProperties() {
        final String methodName = "obtainInstanceProperties";
        if (log.isDebugEnabled()) {
               log.debug("==> Method: " + methodName);
        }
        InstanceProperties instanceProperties = new InstanceProperties();
        EnumPropertyValue enumPropertyValue=null;
        MapPropertyValue mapPropertyValue=null;
        PrimitivePropertyValue primitivePropertyValue=null;
        if (log.isDebugEnabled()) {
               log.debug("<== Method: " + methodName);
        }
        return instanceProperties;
    }

    /**
     * Return the date/time that this Like should start to be used (null means it can be used from creationTime).
     * @return Date the Like becomes effective.
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
     * Return the date/time that this Like should no longer be used.
     *
     * @return Date the Like stops being effective.
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

        sb.append("Like{");
        if (systemAttributes !=null) {
            sb.append("systemAttributes='").append(systemAttributes.toString()).append('\'');
        }
        sb.append("Like Attributes{");

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

        Like that = (Like) o;

        // We view likes as logically equal by checking the properties that the OMAS knows about - i.e. without accounting for extra attributes and references from the org.odpi.openmetadata.accessservices.subjectarea.server.
        return Objects.equals(systemAttributes, that.systemAttributes) &&
                Objects.equals(classifications, that.classifications) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
         systemAttributes.hashCode(),
         classifications.hashCode()
        );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
