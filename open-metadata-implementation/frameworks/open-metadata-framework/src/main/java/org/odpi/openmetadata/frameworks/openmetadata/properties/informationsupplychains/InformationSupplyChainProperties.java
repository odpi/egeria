/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;
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
    private String              scope                = null;
    private List<String>        purposes             = null;
    private String              integrationStyle     = null;
    private Map<String, String> estimatedVolumetrics = null;


    /**
     * Default constructor
     */
    public InformationSupplyChainProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName);
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
            this.scope                = template.getScope();
            this.purposes             = template.getPurposes();
            this.integrationStyle     = template.getIntegrationStyle();
            this.estimatedVolumetrics = template.getEstimatedVolumetrics();
        }
    }


    /**
     * Return the scope for this information supply chain.
     *
     * @return String
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the scope for this information supply chain.
     *
     * @param scope String
     */
    public void setScope(String scope)
    {
        this.scope = scope;
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
     * Return the integration style adopted in this segment.
     *
     * @return string
     */
    public String getIntegrationStyle()
    {
        return integrationStyle;
    }


    /**
     * Return the integration style adopted in this segment.
     *
     * @param integrationStyle string
     */
    public void setIntegrationStyle(String integrationStyle)
    {
        this.integrationStyle = integrationStyle;
    }


    /**
     * Set up the estimated volumetrics (size, volume of data, frequency of interaction etc).
     *
     * @return map of names to values
     */
    public Map<String, String> getEstimatedVolumetrics()
    {
        return estimatedVolumetrics;
    }


    /**
     * Set up the estimated volumetrics (size, volume of data, frequency of interaction etc).
     *
     * @param estimatedVolumetrics map of names to values
     */
    public void setEstimatedVolumetrics(Map<String, String> estimatedVolumetrics)
    {
        this.estimatedVolumetrics = estimatedVolumetrics;
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
                "scope='" + scope + '\'' +
                ", purposes='" + purposes + '\'' +
                ", integrationStyle='" + integrationStyle + '\'' +
                ", estimatedVolumetrics=" + estimatedVolumetrics +
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
        return Objects.equals(scope, that.scope) &&
                Objects.equals(purposes, that.purposes) &&
                Objects.equals(integrationStyle, that.integrationStyle) &&
                Objects.equals(estimatedVolumetrics, that.estimatedVolumetrics);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), scope, purposes, integrationStyle, estimatedVolumetrics);
    }
}
