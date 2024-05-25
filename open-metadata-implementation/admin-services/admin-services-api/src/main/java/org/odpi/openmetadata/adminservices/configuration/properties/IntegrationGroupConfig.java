/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IntegrationGroupConfig provides the properties to configure a dynamic integration group in an integration
 * daemon.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IntegrationGroupConfig extends OMAGServerClientConfig
{
    private String integrationGroupQualifiedName = null;


    /**
     * Default constructor
     */
    public IntegrationGroupConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IntegrationGroupConfig(IntegrationGroupConfig template)
    {
        super(template);

        if (template != null)
        {
            integrationGroupQualifiedName = template.getIntegrationGroupQualifiedName();
        }
    }


    /**
     * Return the name of the integration group.  This is the qualified name of the IntegrationGroup entity in the metadata repository that
     * represents the engine.
     *
     * @return String name
     */
    public String getIntegrationGroupQualifiedName()
    {
        return integrationGroupQualifiedName;
    }


    /**
     * Set up the name of the integration group.   This is the qualified name of the IntegrationGroup entity in the metadata repository that
     * represents the engine.
     *
     * @param integrationGroupQualifiedName String name
     */
    public void setIntegrationGroupQualifiedName(String integrationGroupQualifiedName)
    {
        this.integrationGroupQualifiedName = integrationGroupQualifiedName;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "IntegrationGroupConfig{" +
                ", integrationGroupQualifiedName='" + integrationGroupQualifiedName + '\'' +
                ", OMAGServerPlatformRootURL='" + getOMAGServerPlatformRootURL() + '\'' +
                ", OMAGServerName='" + getOMAGServerName() + '\'' +
                '}';
    }

    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        IntegrationGroupConfig that = (IntegrationGroupConfig) objectToCompare;
        return Objects.equals(integrationGroupQualifiedName, that.integrationGroupQualifiedName);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getIntegrationGroupQualifiedName());
    }
}
