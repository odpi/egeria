/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DataClassAssignmentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SemanticAssignmentProperties links an element to a glossary term to indicate that the glossary term describes its meaning.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataClassAssignmentProperties extends RelationshipBeanProperties
{
    private String                    method              = null;
    private DataClassAssignmentStatus status              = DataClassAssignmentStatus.PROPOSED;
    private int                       confidence          = 0;
    private int                       threshold           = 0;
    private long                      valueFrequency      = 0L;
    private String                    steward             = null;
    private String                    stewardTypeName     = null;
    private String                    stewardPropertyName = null;
    private String                    source              = null;


    /**
     * Default constructor
     */
    public DataClassAssignmentProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DATA_CLASS_ASSIGNMENT_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public DataClassAssignmentProperties(DataClassAssignmentProperties template)
    {
        super(template);

        if (template != null)
        {
            method              = template.getMethod();
            status              = template.getStatus();
            confidence          = template.getConfidence();
            threshold           = template.getThreshold();
            valueFrequency      = template.getValueFrequency();
            steward             = template.getSteward();
            stewardTypeName     = template.getStewardTypeName();
            stewardPropertyName = template.getStewardPropertyName();
            source              = template.getSource();
        }
    }


    /**
     * Set up the method used to identify data class.
     *
     * @param method String name
     */
    public void setMethod(String method)
    {
        this.method = method;
    }


    /**
     * Returns the method used to identify data class.
     *
     * @return String name
     */
    public String getMethod()
    {
        return method;
    }


    /**
     * Set up whether this relationship should be used.
     *
     * @param status status enum
     */
    public void setStatus(DataClassAssignmentStatus status)
    {
        this.status = status;
    }


    /**
     * Returns whether this relationship should be used.
     *
     * @return status enum
     */
    public DataClassAssignmentStatus getStatus()
    {
        return status;
    }


    /**
     * Return the level of confidence that the relationship is correct.  0 means unassigned. Typical assigned values are usually between 1-100
     * as a percentage scale.
     *
     * @return int
     */
    public int getConfidence()
    {
        return confidence;
    }


    /**
     * Set up the level of confidence that the relationship is correct.  0 means unassigned. Typical assigned values are usually between 1-100
     * as a percentage scale.
     *
     * @param confidence int
     */
    public void setConfidence(int confidence)
    {
        this.confidence = confidence;
    }


    /**
     * Return the threshold result used to determine that the data class matched.
     *
     * @return int
     */
    public int getThreshold()
    {
        return threshold;
    }


    /**
     * Set up the threshold result used to determine that the data class matched.
     *
     * @param threshold int
     */
    public void setThreshold(int threshold)
    {
        this.threshold = threshold;
    }


    /**
     * Return how often does the data class specification match the data values.
     *
     * @return count
     */
    public long getValueFrequency()
    {
        return valueFrequency;
    }


    /**
     * Set up how often does the data class specification match the data values.
     *
     * @param valueFrequency count
     */
    public void setValueFrequency(long valueFrequency)
    {
        this.valueFrequency = valueFrequency;
    }


    /**
     * Set up the id of the steward who assigned the relationship (or approved the discovered value).
     *
     * @param steward user id or name of steward
     */
    public void setSteward(String steward)
    {
        this.steward = steward;
    }


    /**
     * Returns the id of the steward who assigned the relationship (or approved the discovered value).
     *
     * @return user id or name of steward
     */
    public String getSteward()
    {
        return steward;
    }

    public String getStewardTypeName()
    {
        return stewardTypeName;
    }

    public void setStewardTypeName(String stewardTypeName)
    {
        this.stewardTypeName = stewardTypeName;
    }

    public String getStewardPropertyName()
    {
        return stewardPropertyName;
    }

    public void setStewardPropertyName(String stewardPropertyName)
    {
        this.stewardPropertyName = stewardPropertyName;
    }

    /**
     * Set up the id of the source of the knowledge of the relationship.
     *
     * @param source String id
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Returns the id of the source of the knowledge of the relationship.
     *
     * @return String id
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataClassAssignmentProperties{" +
                "method='" + method + '\'' +
                ", status=" + status +
                ", confidence=" + confidence +
                ", threshold=" + threshold +
                ", valueFrequency=" + valueFrequency +
                ", steward='" + steward + '\'' +
                ", stewardTypeName='" + stewardTypeName + '\'' +
                ", stewardPropertyName='" + stewardPropertyName + '\'' +
                ", source='" + source + '\'' +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof DataClassAssignmentProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return confidence == that.confidence &&
                Objects.equals(method, that.method) &&
                Objects.equals(valueFrequency, that.valueFrequency) &&
                status == that.status &&
                Objects.equals(threshold, that.threshold) &&
                Objects.equals(steward, that.steward) &&
                Objects.equals(stewardTypeName, that.stewardTypeName) &&
                Objects.equals(stewardPropertyName, that.stewardPropertyName) &&
                Objects.equals(source, that.source);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), method, status, confidence, threshold, valueFrequency, steward, stewardTypeName, stewardPropertyName, source);
    }
}
