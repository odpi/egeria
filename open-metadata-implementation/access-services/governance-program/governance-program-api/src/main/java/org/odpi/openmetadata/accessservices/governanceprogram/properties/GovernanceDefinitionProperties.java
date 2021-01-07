/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GovernanceDefinitionProperties provides the base class for many of the definitions that define the data strategy
 * and governance program.  It includes many of the common fields:
 *
 * <ul>
 *     <li>GUID</li>
 *     <li>Type</li>
 *     <li>Document Id</li>
 *     <li>Title</li>
 *     <li>Summary</li>
 *     <li>Description</li>
 *     <li>Scope</li>
 *     <li>Status</li>
 *     <li>Priority</li>
 *     <li>Implications</li>
 *     <li>Outcomes</li>
 *     <li>AdditionalProperties</li>
 * </ul>
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
                @JsonSubTypes.Type(value = LicenseType.class,       name = "LicenseType"),
                @JsonSubTypes.Type(value = CertificationType.class, name = "CertificationType")
        })
public abstract class GovernanceDefinitionProperties extends ReferenceableProperties
{
    private static final long    serialVersionUID = 1L;

    private String                           title                = null;
    private String                           summary              = null;
    private String                           description          = null;
    private String                           scope                = null;
    private GovernanceDefinitionStatus       status               = null;
    private String                           priority             = null;
    private List<String>                     implications         = null;
    private List<String>                     outcomes             = null;

    private List<GovernanceDefinitionMetric> governanceMetrics    = null;
    private List<GovernanceZoneDefinition>   governanceZones      = null;


    /**
     * Default Constructor
     */
    public GovernanceDefinitionProperties()
    {
        super();
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
            this.title = template.getTitle();
            this.summary = template.getSummary();
            this.description = template.getDescription();
            this.scope = template.getScope();
            this.status = template.getStatus();
            this.priority = template.getPriority();
            this.implications = template.getImplications();
            this.outcomes = template.getOutcomes();

            this.governanceMetrics = template.getGovernanceMetrics();
            this.governanceZones = template.getGovernanceZones();
        }
    }


    /**
     * Return the title associated with this governance definition.
     *
     * @return String title
     */
    public String getTitle()
    {
        return title;
    }


    /**
     * Set up the title associated with this governance definition.
     *
     * @param title String title
     */
    public void setTitle(String title)
    {
        this.title = title;
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
     * Return the full description of the governance definition.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the full description of the governance definition.
     *
     * @param description String description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the organizational scope that this governance definition applies to.
     *
     * @return String organization, department or team name
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the organizational scope that this governance definition applies to.
     *
     * @param scope String organization, department or team name
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }


    /**
     * Return the status of this governance definition.  The meaning of the different values are defined in the
     * GovernanceDefinitionStatus enumeration.
     *
     * @return GovernanceDefinitionStatus enumeration
     */
    public GovernanceDefinitionStatus getStatus()
    {
        return status;
    }


    /**
     * Set up the status of this governance definition.  The meaning of the different values are defined in the
     * GovernanceDefinitionStatus enumeration.
     *
     * @param status GovernanceDefinitionStatus enumeration
     */
    public void setStatus(GovernanceDefinitionStatus status)
    {
        this.status = status;
    }


    /**
     * Return the priority of the governance definition.  This may be something like high, medium or low,
     * or maybe a time frame or more detailed explanation.
     *
     * @return String priority
     */
    public String getPriority()
    {
        return priority;
    }


    /**
     * Set up the priority of this governance definition.  This may be something like high, medium or low,
     * or maybe a time frame or more detailed explanation.
     *
     * @param priority String priority
     */
    public void setPriority(String priority)
    {
        this.priority = priority;
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
     * Return the list of outcomes that resulted from implementing this governance definition.
     *
     * @return list of outcome descriptions
     */
    public List<String> getOutcomes()
    {
        if (outcomes == null)
        {
            return null;
        }
        else if (outcomes.isEmpty())
        {
            return null;
        }
        else
        {
            return outcomes;
        }
    }


    /**
     * Set up the list of outcomes that resulted from implementing this governance definition.
     *
     * @param outcomes list of descriptions of outcomes
     */
    public void setOutcomes(List<String> outcomes)
    {
        this.outcomes = outcomes;
    }



    /**
     * Return the governance metrics that have been defined for this governance definition.
     *
     * @return metrics definition with rationale
     */
    public List<GovernanceDefinitionMetric> getGovernanceMetrics()
    {
        return governanceMetrics;
    }


    /**
     * Set up the governance metrics that have been defined for this governance definition.
     *
     * @param governanceMetrics metrics definition with rationale
     */
    public void setGovernanceMetrics(List<GovernanceDefinitionMetric> governanceMetrics)
    {
        this.governanceMetrics = governanceMetrics;
    }


    /**
     * Return the list of governance zones associates with this definition.
     *
     * @return list of governance zones
     */
    public List<GovernanceZoneDefinition> getGovernanceZones()
    {
        if (governanceZones == null)
        {
            return null;
        }
        else if (governanceZones.isEmpty())
        {
            return null;
        }
        else
        {
            return governanceZones;
        }
    }


    /**
     * Set up the list of governance zones associates with this definition.
     *
     * @param governanceZones list of governance zones
     */
    public void setGovernanceZones(List<GovernanceZoneDefinition> governanceZones)
    {
        this.governanceZones = governanceZones;
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
                "title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", scope='" + scope + '\'' +
                ", status=" + status +
                ", priority='" + priority + '\'' +
                ", implications=" + implications +
                ", outcomes=" + outcomes +
                ", governanceMetrics=" + governanceMetrics +
                ", governanceZones=" + governanceZones +
                ", typeName='" + getTypeName() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", extendedProperties=" + getExtendedProperties() +
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
        GovernanceDefinitionProperties that = (GovernanceDefinitionProperties) objectToCompare;
        return Objects.equals(title, that.title) &&
                       Objects.equals(summary, that.summary) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(scope, that.scope) &&
                       status == that.status &&
                       Objects.equals(priority, that.priority) &&
                       Objects.equals(implications, that.implications) &&
                       Objects.equals(outcomes, that.outcomes) &&
                       Objects.equals(governanceMetrics, that.governanceMetrics) &&
                       Objects.equals(governanceZones, that.governanceZones);
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), title, summary, description, scope, status, priority, implications, outcomes, governanceMetrics,
                            governanceZones);
    }
}
