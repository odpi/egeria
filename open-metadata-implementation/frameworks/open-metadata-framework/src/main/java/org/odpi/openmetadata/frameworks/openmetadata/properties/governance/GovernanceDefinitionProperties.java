/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AuthoredReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GovernanceDefinitionProperties provides the base class for many of the definitions that define the data strategy
 * and governance program.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = GovernanceDriverProperties.class, name = "GovernanceDriverProperties"),
                @JsonSubTypes.Type(value = GovernancePolicyProperties.class, name = "GovernancePolicyProperties"),
                @JsonSubTypes.Type(value = GovernanceControlProperties.class, name = "GovernanceControlProperties"),
        })
public class GovernanceDefinitionProperties extends AuthoredReferenceableProperties
{
    private String       summary          = null;
    private String       scope            = null;
    private String       usage            = null;
    private int          domainIdentifier = 0;
    private String       importance       = null;
    private List<String> implications     = null;
    private List<String> outcomes         = null;
    private List<String> results          = null;


    /**
     * Default Constructor
     */
    public GovernanceDefinitionProperties()
    {
        super();
        super.typeName = OpenMetadataType.GOVERNANCE_DEFINITION.typeName;
    }


    /**
     * Copy/clone Constructor
     *
     * @param template object being copied
     */
    public GovernanceDefinitionProperties(GovernanceDefinitionProperties template)
    {
        super(template);

        if (template != null)
        {
            this.summary           = template.getSummary();
            this.scope             = template.getScope();
            this.usage             = template.getUsage();
            this.domainIdentifier  = template.getDomainIdentifier();
            this.importance        = template.getImportance();
            this.implications      = template.getImplications();
            this.outcomes          = template.getOutcomes();
            this.results           = template.getResults();
        }
    }


    /**
     * Return the summary for this governance definition. This should cover its essence.  Think of it as
     * the executive summary.
     *
     * @return String short description
     */
    public String getSummary()
    {
        return summary;
    }


    /**
     * Set up the summary of the governance definition.  This should cover its essence.  Think of it as
     * the executive summary.
     *
     * @param summary String description
     */
    public void setSummary(String summary)
    {
        this.summary = summary;
    }


    /**
     * Return the organizational scope that this governance definition applies to.
     *
     * @return String organization, department, or team name
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the organizational scope that this governance definition applies to.
     *
     * @param scope String organization, department, or team name
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }


    /**
     * Return details of the usage of this governance definition.
     *
     * @return text
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Set up the details of the usage of this governance definition.
     *
     * @param usage text
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    /**
     * Return the identifier of the governance domain that this definition belongs to (0=all).
     *
     * @return int
     */
    public int getDomainIdentifier()
    {
        return domainIdentifier;
    }


    /**
     * Set up the identifier of the governance domain that this definition belongs to (0=all).
     *
     * @param domainIdentifier int
     */
    public void setDomainIdentifier(int domainIdentifier)
    {
        this.domainIdentifier = domainIdentifier;
    }


    /**
     * Return the priority of the governance definition.  This may be something like high, medium or low,
     * or maybe a time frame or more detailed explanation.
     *
     * @return String priority
     */
    public String getImportance()
    {
        return importance;
    }


    /**
     * Set up the priority of this governance definition.  This may be something like high, medium or low,
     * or maybe a time frame or more detailed explanation.
     *
     * @param importance String priority
     */
    public void setImportance(String importance)
    {
        this.importance = importance;
    }


    /**
     * Return the list of implications for the organization that this governance definition brings.
     * This is often the first enumeration of the changes that that need to be implemented to bring
     * the governance definition into effect.
     *
     * @return list of descriptions
     */
    public List<String> getImplications()
    {
        return implications;
    }


    /**
     * Set up the list of implications for the organization that this governance definition brings.
     * This is often the first enumeration of the changes that that need to be implemented to bring
     * the governance definition into effect.
     *
     * @param implications list of descriptions
     */
    public void setImplications(List<String> implications)
    {
        this.implications = implications;
    }


    /**
     * Return the list of expected outcomes from implementing this governance definition.
     *
     * @return list of outcome descriptions
     */
    public List<String> getOutcomes()
    {
        return outcomes;
    }


    /**
     * Set up the list of expected outcomes from implementing this governance definition.
     *
     * @param outcomes list of descriptions of outcomes
     */
    public void setOutcomes(List<String> outcomes)
    {
        this.outcomes = outcomes;
    }


    /**
     * Return the list of actual results from implementing this governance definition.
     *
     * @return list of result descriptions
     */
    public List<String> getResults()
    {
        return results;
    }


    /**
     * Set up the list of actual results from implementing this governance definition.
     *
     * @param results list of description of results
     */
    public void setResults(List<String> results)
    {
        this.results = results;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "GovernanceDefinitionProperties{" +
                "summary='" + summary + '\'' +
                ", scope='" + scope + '\'' +
                ", usage='" + usage + '\'' +
                ", domainIdentifier=" + domainIdentifier +
                ", importance='" + importance + '\'' +
                ", implications=" + implications +
                ", outcomes=" + outcomes +
                ", results=" + results +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        GovernanceDefinitionProperties that = (GovernanceDefinitionProperties) objectToCompare;
        return domainIdentifier == that.domainIdentifier &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(scope, that.scope) &&
                Objects.equals(usage, that.usage) &&
                Objects.equals(importance, that.importance) &&
                Objects.equals(implications, that.implications) &&
                Objects.equals(outcomes, that.outcomes) &&
                Objects.equals(results, that.results);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), summary, scope, usage, domainIdentifier, importance, implications, outcomes, results);
    }
}
