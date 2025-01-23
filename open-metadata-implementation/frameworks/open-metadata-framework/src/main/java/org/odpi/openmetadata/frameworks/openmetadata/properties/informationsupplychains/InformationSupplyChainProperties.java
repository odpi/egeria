/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InformationSupplyChainProperties identifies a flow of data between systems in a digital landscape
 * that are of importance to the organization.  Each information supply chain is divided into segments that
 * identify stages in the data flow.  Typically, the segments show how the ownership changes along the
 * supply chain.  This may be caused by a change in technology, or the processing moves to a different
 * part of the organization.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationSupplyChainProperties extends ReferenceableProperties
{
    private String       displayName = null;
    private String       description = null;
    private String       version     = null;
    private List<String> purposes    = null;



    /**
     * Default constructor
     */
    public InformationSupplyChainProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public InformationSupplyChainProperties(InformationSupplyChainProperties template)
    {
        super(template);

        if (template != null)
        {
            this.displayName = template.getDisplayName();
            this.description = template.getDescription();
            this.version     = template.getVersion();
            this.purposes    = template.getPurposes();
        }
    }


    /**
     * Return the display name for this information supply chain (normally a shortened form of the qualified name).
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the display name for this information supply chain (normally a shortened form of the qualified name).
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description for this information supply chain.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description for this information supply chain.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the version identifier for this information supply chain.
     *
     * @return String
     */
    public String getVersion()
    {
        return version;
    }


    /**
     * Set up the version number for this information supply chain.
     *
     * @param version String
     */
    public void setVersion(String version)
    {
        this.version = version;
    }


    /**
     * Return the list of purposes for the information supply chain.
     *
     * @return list
     */
    public List<String> getPurposes()
    {
        return purposes;
    }


    /**
     * Set up the list of purposes for the information supply chain.
     *
     * @param purposes list
     */
    public void setPurposes(List<String> purposes)
    {
        this.purposes = purposes;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "InformationSupplyChainProperties{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", version='" + version + '\'' +
                ", purposes='" + purposes + '\'' +
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
        if (! (objectToCompare instanceof InformationSupplyChainProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(version, that.version) &&
                Objects.equals(purposes, that.purposes);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), displayName, description, version, purposes);
    }
}
