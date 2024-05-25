/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.devops.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.devops.properties.PlatformDeploymentProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

/**
 * PlatformDeploymentElement contains the properties and header for a SoftwareServerPlatformDeployment relationship retrieved from the repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PlatformDeploymentElement implements MetadataElement
{
    private ElementHeader                elementHeader                = null;
    private PlatformDeploymentProperties platformDeploymentProperties = null;
    private ElementStub                  platformElement              = null;
    private ElementStub                  hostElement                  = null;


    /**
     * Default constructor
     */
    public PlatformDeploymentElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PlatformDeploymentElement(PlatformDeploymentElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            platformDeploymentProperties = template.getPlatformDeploymentProperties();
            platformElement = template.getPlatformElement();
            hostElement = template.getHostElement();
        }
    }


    /**
     * Return the element header associated with the relationship.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the relationship.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties from the software server platform deployment relationship.
     *
     * @return properties
     */
    public PlatformDeploymentProperties getPlatformDeploymentProperties()
    {
        return platformDeploymentProperties;
    }


    /**
     * Set up the properties from the software server platform deployment relationship.
     *
     * @param platformDeploymentProperties server asset use
     */
    public void setPlatformDeploymentProperties(PlatformDeploymentProperties platformDeploymentProperties)
    {
        this.platformDeploymentProperties = platformDeploymentProperties;
    }


    /**
     * Return the header of the linked software server platform.
     *
     * @return header
     */
    public ElementStub getPlatformElement()
    {
        return platformElement;
    }


    /**
     * Set up the header of the linked software server platform.
     *
     * @param platformElement  header
     */
    public void setPlatformElement(ElementStub platformElement)
    {
        this.platformElement = platformElement;
    }



    /**
     * Return the header of the linked host.
     *
     * @return header
     */
    public ElementStub getHostElement()
    {
        return platformElement;
    }


    /**
     * Set up the header of the linked host.
     *
     * @param platformElement  header
     */
    public void setHostElement(ElementStub platformElement)
    {
        this.platformElement = platformElement;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "PlatformDeploymentElement{" +
                       "elementHeader=" + elementHeader +
                       ", platformDeploymentProperties=" + platformDeploymentProperties +
                       ", platformElement=" + platformElement +
                       ", hostElement=" + hostElement +
                       '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        PlatformDeploymentElement that = (PlatformDeploymentElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(platformDeploymentProperties, that.platformDeploymentProperties) &&
                       Objects.equals(platformElement, that.platformElement);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, platformDeploymentProperties, platformElement, hostElement);
    }
}
