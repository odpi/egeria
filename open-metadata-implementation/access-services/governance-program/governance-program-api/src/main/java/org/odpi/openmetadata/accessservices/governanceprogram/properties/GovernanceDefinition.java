/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GovernanceDefinition provides the base class for many of the definitions that define the data strategy
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
                @JsonSubTypes.Type(value = GovernanceDriver.class, name = "GovernanceDriver"),
                @JsonSubTypes.Type(value = GovernancePolicy.class, name = "GovernancePolicy"),
                @JsonSubTypes.Type(value = GovernanceControl.class, name = "GovernanceControl"),
                @JsonSubTypes.Type(value = LicenseType.class,       name = "LicenseType"),
                @JsonSubTypes.Type(value = CertificationType.class, name = "CertificationType")
        })
public abstract class GovernanceDefinition extends GovernanceDefinitionSummary
{
    private String                           description          = null;
    private String                           scope                = null;
    private GovernanceDefinitionStatus       status               = null;
    private String                           priority             = null;
    private List<String>                     implications         = null;
    private List<String>                     outcomes             = null;
    private List<ExternalReference>          externalReferences   = null;
    private Map<String, Object>              additionalProperties = null;
    private List<GovernanceDefinitionMetric> governanceMetrics    = null;
    private List<GovernanceZoneDefinition>   governanceZones      = null;


    /**
     * Default Constructor
     */
    public GovernanceDefinition()
    {
        super();
    }


    /**
     * Copy/clone Constructor
     *
     * @param template object being copied
     */
    public GovernanceDefinition(GovernanceDefinition template)
    {
        super(template);

        if (template != null)
        {
            this.description = template.getDescription();
            this.scope = template.getScope();
            this.status = template.getStatus();
            this.priority = template.getPriority();
            this.implications = template.getImplications();
            this.outcomes = template.getOutcomes();
            this.externalReferences = template.getExternalReferences();
            this.additionalProperties = template.getAdditionalProperties();
            this.governanceMetrics = template.getGovernanceMetrics();
            this.governanceZones = template.getGovernanceZones();
        }
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
     * Return the list of links to external documentation that are relevant to this governance definition.
     *
     * @return list of external references
     */
    public List<ExternalReference> getExternalReferences()
    {
        if (externalReferences == null)
        {
            return null;
        }
        else if (externalReferences.isEmpty())
        {
            return null;
        }
        else
        {
            return externalReferences;
        }
    }


    /**
     * Set up the list of links to external documentation that are relevant to this governance definition.
     *
     * @param externalReferences list of external references
     */
    public void setExternalReferences(List<ExternalReference> externalReferences)
    {
        this.externalReferences = externalReferences;
    }


    /**
     * Return the map of properties that are not explicitly provided as properties on this bean.
     *
     * @return map from string to object.
     */
    public Map<String, Object> getAdditionalProperties()
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
     * Set up the map of properties that are not explicitly provided as properties on this bean.
     *
     * @param additionalProperties map from string to object.
     */
    public void setAdditionalProperties(Map<String, Object> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
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
        return "GovernanceDefinition{" +
                "description='" + description + '\'' +
                ", scope='" + scope + '\'' +
                ", status=" + status +
                ", priority='" + priority + '\'' +
                ", implications=" + implications +
                ", outcomes=" + outcomes +
                ", externalReferences=" + externalReferences +
                ", additionalProperties=" + additionalProperties +
                ", governanceMetrics=" + governanceMetrics +
                ", governanceZones=" + governanceZones +
                ", GUID='" + getGUID() + '\'' +
                ", type='" + getType() + '\'' +
                ", documentId='" + getDocumentId() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", summary='" + getSummary() + '\'' +
                '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof GovernanceDefinition))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceDefinition that = (GovernanceDefinition) objectToCompare;
        return Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getScope(), that.getScope()) &&
                getStatus() == that.getStatus() &&
                Objects.equals(getPriority(), that.getPriority()) &&
                Objects.equals(getImplications(), that.getImplications()) &&
                Objects.equals(getOutcomes(), that.getOutcomes()) &&
                Objects.equals(getExternalReferences(), that.getExternalReferences()) &&
                Objects.equals(getAdditionalProperties(), that.getAdditionalProperties()) &&
                Objects.equals(getGovernanceMetrics(), that.getGovernanceMetrics()) &&
                Objects.equals(getGovernanceZones(), that.getGovernanceZones());
    }
}
