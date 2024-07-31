/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceActionProcessProperties defines the properties of a governance action process.
 * A governance action process is a series of steps and decisions.  It is implemented as
 * an automated process.  Each step in the process calls a governance service running in a engine host.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionProcessProperties extends ReferenceableProperties
{
    private int    domainIdentifier     = 0;
    private String displayName          = null;
    private String description          = null;
    private String versionIdentifier    = null;
    private String formula              = null;
    private String formulaType          = null;


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
            domainIdentifier = template.getDomainIdentifier();

            displayName = template.getDisplayName();
            description = template.getDescription();

            versionIdentifier = template.getVersionIdentifier();

            formula = template.getFormula();
            formulaType = template.getFormulaType();
        }
    }


    /**
     * Return the identifier of the governance domain that this action belongs to (0=ALL/ANY).
     *
     * @return int
     */
    public int getDomainIdentifier()
    {
        return domainIdentifier;
    }


    /**
     * Set up the identifier of the governance domain that this action belongs to (0=ALL/ANY).
     *
     * @param domainIdentifier int
     */
    public void setDomainIdentifier(int domainIdentifier)
    {
        this.domainIdentifier = domainIdentifier;
    }


    /**
     * Returns the stored display name property for the process.
     * If no display name is available then null is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the stored display name property for the process.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Returns the stored description property for the process.
     * If no description is provided then null is returned.
     *
     * @return  String text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the stored description property for the process.
     *
     * @param description String text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Set up the version identifier of the process.
     *
     * @return string version name
     */
    public String getVersionIdentifier()
    {
        return versionIdentifier;
    }


    /**
     * Set up the version identifier of the process.
     *
     * @param versionIdentifier string version name
     */
    public void setVersionIdentifier(String versionIdentifier)
    {
        this.versionIdentifier = versionIdentifier;
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
     * Return the name of the language that the formula is implemented in.
     *
     * @return string name
     */
    public String getFormulaType()
    {
        return formulaType;
    }


    /**
     * Set up the name of the language that the formula is implemented in.
     *
     * @param formulaType string name
     */
    public void setFormulaType(String formulaType)
    {
        this.formulaType = formulaType;
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
                       "domainIdentifier=" + domainIdentifier +
                       ", displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", versionIdentifier='" + versionIdentifier + '\'' +
                       ", formula='" + formula + '\'' +
                       ", formulaType='" + formulaType + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        GovernanceActionProcessProperties that = (GovernanceActionProcessProperties) objectToCompare;
        return domainIdentifier == that.domainIdentifier &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(versionIdentifier, that.versionIdentifier) &&
                Objects.equals(formula, that.formula) &&
                Objects.equals(formulaType, that.formulaType);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), domainIdentifier, displayName, description, versionIdentifier,
                            formula, formulaType);
    }
}
