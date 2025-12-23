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
    private String                    method                    = null;
    private DataClassAssignmentStatus dataClassAssignmentStatus = DataClassAssignmentStatus.PROPOSED;
    private int                       confidence                = 0;
    private int                       threshold                 = 0;
    private String                    steward                   = null;
    private String                    stewardTypeName           = null;
    private String                    stewardPropertyName       = null;
    private String                    source                    = null;


    /**
     * Default constructor
     */
    public DataClassAssignmentProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_CLASS_ASSIGNMENT_RELATIONSHIP.typeName;
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
            method                    = template.getMethod();
            dataClassAssignmentStatus = template.getDataClassAssignmentStatus();
            confidence                = template.getConfidence();
            threshold                 = template.getThreshold();
            steward                   = template.getSteward();
            stewardTypeName           = template.getStewardTypeName();
            stewardPropertyName       = template.getStewardPropertyName();
            source                    = template.getSource();
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
     * @param dataClassAssignmentStatus status enum
     */
    public void setDataClassAssignmentStatus(DataClassAssignmentStatus dataClassAssignmentStatus)
    {
        this.dataClassAssignmentStatus = dataClassAssignmentStatus;
    }


    /**
     * Returns whether this relationship should be used.
     *
     * @return status enum
     */
    public DataClassAssignmentStatus getDataClassAssignmentStatus()
    {
        return dataClassAssignmentStatus;
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

    /**
     * Return the type of the steward Id.
     *
     * @return string
     */
    public String getStewardTypeName()
    {
        return stewardTypeName;
    }


    /**
     * Set up the type of the steward id.
     *
     * @param stewardTypeName string
     */
    public void setStewardTypeName(String stewardTypeName)
    {
        this.stewardTypeName = stewardTypeName;
    }


    /**
     * Return the property name of the steward id.
     *
     * @return string
     */
    public String getStewardPropertyName()
    {
        return stewardPropertyName;
    }


    /**
     * Set up the property name of the steward id.
     *
     * @param stewardPropertyName string
     */
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
                ", status=" + dataClassAssignmentStatus +
                ", confidence=" + confidence +
                ", threshold=" + threshold +
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
                dataClassAssignmentStatus == that.dataClassAssignmentStatus &&
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
        return Objects.hash(super.hashCode(), method, dataClassAssignmentStatus, confidence, threshold, steward, stewardTypeName, stewardPropertyName, source);
    }
}
