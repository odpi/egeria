/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SoftwareCapabilityProperties describe the properties needed to describe a specific software server's capability.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = APIManagerProperties.class, name = "APIManagerProperties"),
        @JsonSubTypes.Type(value = ApplicationProperties.class, name = "ApplicationProperties"),
        @JsonSubTypes.Type(value = AuthorizationManagerProperties.class, name = "AuthorizationManagerProperties"),
        @JsonSubTypes.Type(value = DataManagerProperties.class, name = "DataManagerProperties"),
        @JsonSubTypes.Type(value = EngineProperties.class, name = "EngineProperties"),
        @JsonSubTypes.Type(value = EventBrokerProperties.class, name = "EventBrokerProperties"),
        @JsonSubTypes.Type(value = GovernanceEngineProperties.class, name = "GovernanceEngineProperties"),
        @JsonSubTypes.Type(value = IntegrationGroupProperties.class, name = "IntegrationGroupProperties"),
        @JsonSubTypes.Type(value = InventoryCatalogProperties.class, name = "InventoryCatalogProperties"),
        @JsonSubTypes.Type(value = NetworkGatewayProperties.class, name = "NetworkGatewayProperties"),
        @JsonSubTypes.Type(value = UserAuthenticationManagerProperties.class, name = "UserAuthenticationManagerProperties"),

})
public class SoftwareCapabilityProperties extends ReferenceableProperties
{
    private String deployedImplementationType = null;
    private String patchLevel                 = null;
    private String source                     = null;


    /**
     * Default constructor.
     */
    public SoftwareCapabilityProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SOFTWARE_CAPABILITY.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public SoftwareCapabilityProperties(SoftwareCapabilityProperties template)
    {
        super(template);

        if (template != null)
        {
            deployedImplementationType = template.getDeployedImplementationType();
            patchLevel                 = template.getPatchLevel();
            source                     = template.getSource();
        }
    }


    /**
     * Return the description of the type of software capability this is.
     *
     * @return string description
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Set up the description of the type of software capability this is.
     *
     * @param deployedImplementationType string
     */
    public void setDeployedImplementationType(String deployedImplementationType)
    {
        this.deployedImplementationType = deployedImplementationType;
    }


    /**
     * Return the patch level of the software capability.
     *
     * @return patch level string
     */
    public String getPatchLevel()
    {
        return patchLevel;
    }


    /**
     * Set up the patch level of the software capability.
     *
     * @param patchLevel string
     */
    public void setPatchLevel(String patchLevel)
    {
        this.patchLevel = patchLevel;
    }


    /**
     * Return the source of the software capability implementation.
     *
     * @return string url
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Set up the source of the software capability implementation.
     *
     * @param source string url
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SoftwareCapabilityProperties{" +
                "deployedImplementationType='" + deployedImplementationType + '\'' +
                ", patchLevel='" + patchLevel + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SoftwareCapabilityProperties that = (SoftwareCapabilityProperties) objectToCompare;
        return Objects.equals(getDeployedImplementationType(), that.getDeployedImplementationType()) &&
                Objects.equals(getPatchLevel(), that.getPatchLevel()) &&
                Objects.equals(getSource(), that.getSource());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDeployedImplementationType(), getPatchLevel(), getSource());
    }
}
