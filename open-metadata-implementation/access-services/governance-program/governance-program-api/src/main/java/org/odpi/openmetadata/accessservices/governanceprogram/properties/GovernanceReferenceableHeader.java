/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GovernanceHeader provides the base class for many of the definitions that define the data strategy
 * and governance program.  It includes many of the common fields:
 *
 * <ul>
 *     <li>GUID</li>
 *     <li>Type</li>
 *     <li>Document Id</li>
 *     <li>Title</li>
 *     <li>Summary</li>
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
                @JsonSubTypes.Type(value = GovernanceDefinition.class, name = "GovernanceDefinition"),
                @JsonSubTypes.Type(value = GovernanceOfficer.class, name = "GovernanceOfficer")
        })
public abstract class GovernanceReferenceableHeader extends GovernanceHeader
{
    private List<ExternalReference>    externalReferences   = null;
    private Map<String, String>        additionalProperties = null;

    /**
     * Default Constructor
     */
    public GovernanceReferenceableHeader()
    {
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public GovernanceReferenceableHeader(GovernanceReferenceableHeader template)
    {
        if (template != null)
        {
            this.externalReferences = template.getExternalReferences();
            this.additionalProperties = template.getAdditionalProperties();
        }
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
    public Map<String, String> getAdditionalProperties()
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
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "GovernanceReferenceableHeader{" +
                "externalReferences=" + externalReferences +
                ", additionalProperties=" + additionalProperties +
                ", GUID='" + getGUID() + '\'' +
                ", type='" + getType() + '\'' +
                ", title='" + getTitle() + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceReferenceableHeader that = (GovernanceReferenceableHeader) objectToCompare;
        return Objects.equals(externalReferences, that.externalReferences) &&
                Objects.equals(additionalProperties, that.additionalProperties);
    }
}
