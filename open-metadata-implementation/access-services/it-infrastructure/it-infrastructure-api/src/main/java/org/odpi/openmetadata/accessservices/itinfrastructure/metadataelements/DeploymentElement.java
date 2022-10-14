/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.DeploymentProperties;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * DeploymentElement contains the properties and header for a DeployedOn relationship retrieved from the repository.
 * The related asset element is the other end of the relationship relative to the query.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DeploymentElement implements MetadataElement, Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader        elementHeader        = null;
    private DeploymentProperties deploymentProperties = null;
    private AssetElement         assetElement         = null;


    /**
     * Default constructor
     */
    public DeploymentElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DeploymentElement(DeploymentElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            deploymentProperties = template.getDeploymentProperties();
            assetElement = template.getAssetElement();
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
     * Return the properties from the DeployedOn relationship.
     *
     * @return properties
     */
    public DeploymentProperties getDeploymentProperties()
    {
        return deploymentProperties;
    }


    /**
     * Set up the properties from the DeployedOn relationship.
     *
     * @param deploymentProperties properties
     */
    public void setDeploymentProperties(DeploymentProperties deploymentProperties)
    {
        this.deploymentProperties = deploymentProperties;
    }


    /**
     * Return the related asset.
     *
     * @return header
     */
    public AssetElement getAssetElement()
    {
        return assetElement;
    }


    /**
     * Set up the related asset.
     *
     * @param assetElement asset
     */
    public void setAssetElement(AssetElement assetElement)
    {
        this.assetElement = assetElement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DeploymentElement{" +
                       "elementHeader=" + elementHeader +
                       ", deploymentProperties=" + deploymentProperties +
                       ", assetElement=" + assetElement +
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
        DeploymentElement that = (DeploymentElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(deploymentProperties, that.deploymentProperties) &&
                       Objects.equals(assetElement, that.assetElement);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elementHeader, deploymentProperties, assetElement);
    }
}
