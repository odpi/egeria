/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementOriginCategory;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ElementOrigin object holds information about the origin of the retrieved element. This means information about
 * the metadata server, the metadata collection ID and name, etc.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElementOrigin
{
    /**
     * The name of the server where the element was retrieved from.
     */
    private String sourceServer;

    /**
     * The id of the metadata collection.
     */
    private String metadataCollectionId;

    /**
     * The name of the metadata collection.
     */
    private String metadataCollectionName;

    /**
     * The license string for this instance.
     */
    private String instanceLicense;

    /**
     * The origin category of the element.
     */
    private ElementOriginCategory originCategory;


    /**
     * Default constructor.
     */
    public ElementOrigin()
    {
    }


    /**
     * Returns the name of the server.
     *
     * @return String - the name of the server
     */
    public String getSourceServer()
    {
        return sourceServer;
    }


    /**
     * Set up the name of the server.
     *
     * @param sourceServer - name of the server
     */
    public void setSourceServer(String sourceServer)
    {
        this.sourceServer = sourceServer;
    }


    /**
     * Returns the id of the metadata collection.
     *
     * @return String - the id of the metadata collection.
     */
    public String getMetadataCollectionId()
    {
        return metadataCollectionId;
    }


    /**
     * Set up the id of the metadata collection.
     *
     * @param metadataCollectionId - id of the metadata collection.
     */
    public void setMetadataCollectionId(String metadataCollectionId)
    {
        this.metadataCollectionId = metadataCollectionId;
    }


    /**
     * Returns the name of the metadata collection.
     *
     * @return String - the name of the metadata collection.
     */
    public String getMetadataCollectionName()
    {
        return metadataCollectionName;
    }


    /**
     * Set up the name of the metadata collection.
     *
     * @param metadataCollectionName - name of the metadata collection.
     */
    public void setMetadataCollectionName(String metadataCollectionName)
    {
        this.metadataCollectionName = metadataCollectionName;
    }


    /**
     * Returns the license for this instance.
     *
     * @return String - the license string.
     */
    public String getInstanceLicense()
    {
        return instanceLicense;
    }


    /**
     * Set up the license string for this instance.
     *
     * @param instanceLicense - the license string.
     */
    public void setInstanceLicense(String instanceLicense)
    {
        this.instanceLicense = instanceLicense;
    }


    /**
     * Returns the origin category.
     *
     * @return the value from the enum representing the origin category
     */
    public ElementOriginCategory getOriginCategory()
    {
        return originCategory;
    }


    /**
     * Set up the origin category of the element.
     *
     * @param originCategory - the origin category value
     */
    public void setOriginCategory(ElementOriginCategory originCategory)
    {
        this.originCategory = originCategory;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ElementOrigin{" +
                       "sourceServer='" + sourceServer + '\'' +
                       ", metadataCollectionId='" + metadataCollectionId + '\'' +
                       ", metadataCollectionName='" + metadataCollectionName + '\'' +
                       ", instanceLicense='" + instanceLicense + '\'' +
                       ", originCategory=" + originCategory +
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
        if (! (objectToCompare instanceof ElementOrigin that))
        {
            return false;
        }
        return Objects.equals(sourceServer, that.sourceServer) && Objects.equals(metadataCollectionId,
                                                                                 that.metadataCollectionId) && Objects.equals(
                metadataCollectionName, that.metadataCollectionName) && Objects.equals(instanceLicense,
                                                                                       that.instanceLicense) && originCategory == that.originCategory;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(sourceServer, metadataCollectionId, metadataCollectionName, instanceLicense, originCategory);
    }
}
