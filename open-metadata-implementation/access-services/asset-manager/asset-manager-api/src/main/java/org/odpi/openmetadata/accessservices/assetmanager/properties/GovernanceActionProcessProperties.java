/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Process properties defines the properties of a process.  A process is a series of steps and decisions in operation
 * in the organization.  It is typically an automated process but may be performed by a person.
 * Only set the implementationLanguage if the process is automated.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionProcessProperties extends ReferenceableProperties
{
    private static final long   serialVersionUID = 1L;

    private String              displayName                  = null;
    private String              summary                      = null;
    private String              description                  = null;
    private String              abbreviation                 = null;
    private String              usage                        = null;
    private String              technicalName                = null;
    private String              technicalDescription         = null;
    private String              formula                      = null;
    private String              implementationLanguage       = null;

    /**
     * Default constructor
     */
    public GovernanceActionProcessProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public GovernanceActionProcessProperties(GovernanceActionProcessProperties template)
    {
        super(template);

        if (template != null)
        {
            displayName = template.getDisplayName();
            summary = template.getSummary();
            description = template.getDescription();
            abbreviation = template.getAbbreviation();
            usage = template.getUsage();

            technicalName = template.getTechnicalName();
            technicalDescription = template.getTechnicalDescription();

            formula = template.getFormula();
            implementationLanguage = template.getImplementationLanguage();
        }
    }


    /**
     * Returns the stored display name property for the technical element.
     * If no display name is available then null is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the stored display name property for the technical element.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the short (1-2 sentence) description of the technical element.
     *
     * @return string text
     */
    public String getSummary()
    {
        return summary;
    }


    /**
     * Set up the short (1-2 sentence) description of the technical element.
     *
     * @param summary string text
     */
    public void setSummary(String summary)
    {
        this.summary = summary;
    }


    /**
     * Returns the stored description property for the technical element.
     * If no description is provided then null is returned.
     *
     * @return  String text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the stored description property for the technical element.
     *
     * @param description String text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the abbreviation used for this technical element.
     *
     * @return string text
     */
    public String getAbbreviation()
    {
        return abbreviation;
    }


    /**
     * Set up the abbreviation used for this technical element.
     *
     * @param abbreviation string text
     */
    public void setAbbreviation(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }


    /**
     * Return details of the expected usage of this technical element.
     *
     * @return string text
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Set up details of the expected usage of this technical element.
     *
     * @param usage string text
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    /**
     * Returns the stored name property for the asset.  This is the technical name of the asset rather than the name
     * that it is commonly known as.  If no technical name is available then null is returned.
     *
     * @return String name
     */
    public String getTechnicalName()
    {
        return technicalName;
    }


    /**
     * Set up the stored name property for the asset. This is the technical name of the asset rather than the name
     * that it is commonly known as.
     *
     * @param technicalName String name
     */
    public void setTechnicalName(String technicalName)
    {
        this.technicalName = technicalName;
    }


    /**
     * Returns the stored technical description property for the asset.
     * If no technical description is provided then null is returned.
     *
     * @return String text
     */
    public String getTechnicalDescription()
    {
        return technicalDescription;
    }


    /**
     * Set up the stored technical description property associated with the asset.
     *
     * @param description String text
     */
    public void setTechnicalDescription(String description)
    {
        this.technicalDescription = description;
    }


    /**
     * Return the description of the processing performed by this process.
     *
     * @return string description
     */
    public String getFormula() { return formula; }


    /**
     * Set up the description of the processing performed by this process.
     *
     * @param formula string description
     */
    public void setFormula(String formula)
    {
        this.formula = formula;
    }


    /**
     * Return the name of the programming language that this process is implemented in.
     *
     * @return string name
     */
    public String getImplementationLanguage()
    {
        return implementationLanguage;
    }


    /**
     * Set up the name of the programming language that this process is implemented in.
     *
     * @param implementationLanguage string name
     */
    public void setImplementationLanguage(String implementationLanguage)
    {
        this.implementationLanguage = implementationLanguage;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GovernanceActionProcessProperties{" +
                       "displayName='" + displayName + '\'' +
                       ", summary='" + summary + '\'' +
                       ", description='" + description + '\'' +
                       ", abbreviation='" + abbreviation + '\'' +
                       ", usage='" + usage + '\'' +
                       ", technicalName='" + technicalName + '\'' +
                       ", technicalDescription='" + technicalDescription + '\'' +
                       ", formula='" + formula + '\'' +
                       ", implementationLanguage='" + implementationLanguage + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceActionProcessProperties that = (GovernanceActionProcessProperties) objectToCompare;
        return Objects.equals(displayName, that.displayName) &&
                       Objects.equals(summary, that.summary) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(abbreviation, that.abbreviation) &&
                       Objects.equals(usage, that.usage) &&
                       Objects.equals(technicalName, that.technicalName) &&
                       Objects.equals(technicalDescription, that.technicalDescription) &&
                       Objects.equals(formula, that.formula) &&
                       Objects.equals(implementationLanguage, that.implementationLanguage);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), displayName, summary, description, abbreviation, usage, technicalName, technicalDescription,
                            formula, implementationLanguage);
    }
}
