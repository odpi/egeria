/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceMetric stores information about the way activity associated with a governance definition is to be measured.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceMetric implements Serializable
{
    private static final long   serialVersionUID = 1L;

    private String              guid                 = null;
    private List<String>        classifications      = null;
    private String              qualifiedName        = null;
    private String              displayName          = null;
    private String              description          = null;
    private String              measurement          = null;
    private String              target               = null;
    private Map<String, Object> additionalProperties = null;


    /**
     * Default constructor
     */
    public GovernanceMetric()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public GovernanceMetric(GovernanceMetric template)
    {
        if (template != null)
        {
            /*
             * Copy the values from the supplied template.
             */
            guid                 = template.getGUID();
            classifications      = template.getClassifications();
            qualifiedName        = template.getQualifiedName();
            displayName          = template.getDisplayName();
            description          = template.getDescription();
            measurement          = template.getMeasurement();
            target               = template.getTarget();
            additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the unique identifier for the governance metric.
     *
     * @return String unique id
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Set up the unique identifier for the governance metric.
     *
     * @param guid String unique identifier
     */
    public void setGUID(String guid)
    {
        this.guid = guid;
    }


    /**
     * Return the list of classifications associated with the governance metric.
     *
     * @return Classifications  list of classifications
     */
    public List<String> getClassifications()
    {
        if (classifications == null)
        {
            return null;
        }
        else if (classifications.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(classifications);
        }
    }


    /**
     * Set up the classifications associated with the governance metric.
     *
     * @param classifications list of classifications
     */
    public void setClassifications(List<String> classifications)
    {
        this.classifications = classifications;
    }


    /**
     * Set up the fully qualified name.
     *
     * @param qualifiedName String name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Returns the fully qualified name.
     *
     * @return qualifiedName
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Return the display name of this governance metric.
     *
     * @return String display name.
     */
    public String getDisplayName() { return displayName; }


    /**
     * Set up the display name of this governance metric.
     *
     * @param displayName - string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description of this governance metric.
     *
     * @return String resource description
     */
    public String getDescription() { return description; }


    /**
     * Set up the description of this governance metric.
     * @param description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the description of the measurements needed to support this metric.
     *
     * @return String measurement description
     */
    public String getMeasurement() { return measurement; }


    /**
     * Set up the description of the measurements needed to support this metric.
     *
     * @param measurement String measurement description
     */
    public void setMeasurement(String measurement)
    {
        this.measurement = measurement;
    }


    /**
     * Return the description of the targets that the organization is aiming to achieve.
     *
     * @return String target description.
     */
    public String getTarget() { return target; }


    /**
     * Set up the description of the targets that the organization is aiming to achieve.
     *
     * @param target String target description
     */
    public void setTarget(String target)
    {
        this.target = target;
    }


    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String,Object> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String,Object> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ExternalReference{" +
                "guid='" + guid + '\'' +
                ", classifications=" + classifications +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", measurement='" + measurement + '\'' +
                ", target='" + target + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", GUID='" + getGUID() +
                '}';
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
        if (!(objectToCompare instanceof GovernanceMetric))
        {
            return false;
        }
        GovernanceMetric that = (GovernanceMetric) objectToCompare;
        return Objects.equals(guid, that.guid) &&
                Objects.equals(getClassifications(), that.getClassifications()) &&
                Objects.equals(getQualifiedName(), that.getQualifiedName()) &&
                Objects.equals(getAdditionalProperties(), that.getAdditionalProperties()) &&
                Objects.equals(getMeasurement(), that.getMeasurement()) &&
                Objects.equals(getTarget(), that.getTarget()) &&
                Objects.equals(getDisplayName(), that.getDisplayName()) &&
                Objects.equals(getDescription(), that.getDescription());
    }


    /**
     * Uses the guid to create a hashcode.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guid);
    }
}