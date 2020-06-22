/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.classifications;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * A Classification
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY, setterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY, fieldVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Classification implements Serializable {
    protected static final long serialVersionUID = 1L;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    //system attributes
    private SystemAttributes systemAttributes = null;
    private Date effectiveFromTime = null;
    private Date effectiveToTime = null;
    private Map<String, String> additionalProperties;

    protected String classificationName = null;

    /**
     * Get the core attributes
     *
     * @return core attributes
     */
    public SystemAttributes getSystemAttributes() {
        return systemAttributes;
    }

    public void setSystemAttributes(SystemAttributes systemAttributes) {
        this.systemAttributes = systemAttributes;
    }

    /**
     * Return the date/time that this node should start to be used (null means it can be used from creationTime).
     *
     * @return Date the node becomes effective.
     */
    public Date getEffectiveFromTime() {
        return effectiveFromTime;
    }

    public void setEffectiveFromTime(Date effectiveFromTime) {
        this.effectiveFromTime = effectiveFromTime;
    }

    /**
     * Return the date/time that this node should no longer be used.
     *
     * @return Date the node stops being effective.
     */
    public Date getEffectiveToTime() {
        return effectiveToTime;
    }

    public void setEffectiveToTime(Date effectiveToTime) {
        this.effectiveToTime = effectiveToTime;
    }

    public String getClassificationName() {
        return this.classificationName;
    }

    /**
     * Get the additional properties - ones that are in addition to the standard types.
     *
     * @return additional properties
     */
    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}