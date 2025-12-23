/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.analytics.DeployedAnalyticsModelProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.DeployedAPIProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.DeployedConnectorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DeployedSoftwareComponentProperties defines the properties of a software component that is in a runnable state.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeployedAPIProperties.class, name = "DeployedAPIProperties"),
        @JsonSubTypes.Type(value = DeployedAnalyticsModelProperties.class, name = "DeployedAnalyticsModelProperties"),
        @JsonSubTypes.Type(value = DeployedConnectorProperties.class, name = "DeployedConnectorProperties"),
})
public class DeployedSoftwareComponentProperties extends ProcessProperties
{
    private String implementationLanguage = null;

    /**
     * Default constructor
     */
    public DeployedSoftwareComponentProperties()
    {
        super();
        super.typeName = OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName;
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public DeployedSoftwareComponentProperties(DeployedSoftwareComponentProperties template)
    {
        super(template);

        if (template != null)
        {
            implementationLanguage = template.getImplementationLanguage();
        }
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
        return "DeployedSoftwareComponentProperties{" +
                "implementationLanguage='" + implementationLanguage + '\'' +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        DeployedSoftwareComponentProperties that = (DeployedSoftwareComponentProperties) objectToCompare;
        return Objects.equals(implementationLanguage, that.implementationLanguage);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), implementationLanguage);
    }
}
