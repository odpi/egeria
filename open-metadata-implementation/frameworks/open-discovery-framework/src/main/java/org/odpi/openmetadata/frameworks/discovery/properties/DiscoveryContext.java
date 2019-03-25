/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DiscoveryContext extends PropertyBase
{
    private Connection       assetConnection = null;
    private List<Annotation> existingAnnotations = null;
    private List<Annotation> newAnnotations = null;


    /**
     * Default constructor
     */
    public DiscoveryContext()
    {
        super();
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public DiscoveryContext(DiscoveryContext template)
    {
        super(template);

        if (template != null)
        {
            assetConnection = template.getAssetConnection();
            existingAnnotations = template.getExistingAnnotations();
            newAnnotations = template.getNewAnnotations();
        }
    }


    /**
     * Return the connection for the connector to the asset.
     *
     * @return Connection object
     */
    public Connection getAssetConnection()
    {
        return assetConnection;
    }


    /**
     * Set up the connection for the connector to the asset.
     *
     * @param assetConnection Connection object
     */
    public void setAssetConnection(Connection assetConnection)
    {
        this.assetConnection = assetConnection;
    }


    /**
     * Return the list of current annotations associated with the asset.
     *
     * @return list of Annotation objects
     */
    public List<Annotation> getExistingAnnotations()
    {
        return existingAnnotations;
    }


    /**
     * Set up the list of current annotations associated with the asset.
     *
     * @param existingAnnotations list of Annotation objects
     */
    public void setExistingAnnotations(List<Annotation> existingAnnotations)
    {
        this.existingAnnotations = existingAnnotations;
    }


    /**
     * Return the list of annotation object created (so far) by this discovery service.
     *
     * @return list of Annotation objects
     */
    public synchronized List<Annotation> getNewAnnotations()
    {
        return newAnnotations;
    }


    /**
     * Set up the list of annotation object created (so far) by this discovery service.
     *
     * @param newAnnotations list of Annotation objects
     */
    public synchronized void setNewAnnotations(List<Annotation> newAnnotations)
    {
        this.newAnnotations = newAnnotations;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DiscoveryContext{" +
                "assetConnection=" + assetConnection +
                ", existingAnnotations=" + existingAnnotations +
                ", newAnnotations=" + newAnnotations +
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
        DiscoveryContext that = (DiscoveryContext) objectToCompare;
        return Objects.equals(getAssetConnection(), that.getAssetConnection()) &&
                Objects.equals(getExistingAnnotations(), that.getExistingAnnotations()) &&
                Objects.equals(getNewAnnotations(), that.getNewAnnotations());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getAssetConnection(), getExistingAnnotations(), getNewAnnotations());
    }
}
