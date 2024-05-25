/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.devops.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.devops.properties.ServerAssetUseProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ServerAssetUseElement contains the properties and header for a ServerAssetUse relationship retrieved from the repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ServerAssetUseElement
{
    private ServerAssetUseProperties serverAssetUse = null;
    private AssetElement             asset          = null;
    private ElementStub              capabilityStub = null;


    /**
     * Default constructor
     */
    public ServerAssetUseElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ServerAssetUseElement(ServerAssetUseElement template)
    {
        if (template != null)
        {
            serverAssetUse = template.getServerAssetUse();
            asset = template.getAsset();
            capabilityStub = template.getCapabilityStub();
        }
    }


    /**
     * Return the properties from the server asset use relationship.
     *
     * @return server asset use
     */
    public ServerAssetUseProperties getServerAssetUse()
    {
        return serverAssetUse;
    }


    /**
     * Set up the properties from the server asset use relationship.
     *
     * @param serverAssetUse server asset use
     */
    public void setServerAssetUse(ServerAssetUseProperties serverAssetUse)
    {
        this.serverAssetUse = serverAssetUse;
    }


    /**
     * Return the properties of the asset.
     *
     * @return properties
     */
    public AssetElement getAsset()
    {
        return asset;
    }


    /**
     * Set up the asset properties.
     *
     * @param asset  properties
     */
    public void setAsset(AssetElement asset)
    {
        this.asset = asset;
    }


    /**
     * Return the stub information of the software server capability.
     *
     * @return header and unique properties
     */
    public ElementStub getCapabilityStub()
    {
        return capabilityStub;
    }


    /**
     * Set up the stub information of the software server capability.
     *
     * @param capabilityStub header and unique properties
     */
    public void setCapabilityStub(ElementStub capabilityStub)
    {
        this.capabilityStub = capabilityStub;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ServerAssetUseElement{" +
                       "serverAssetUse=" + serverAssetUse +
                       ", asset=" + asset +
                       ", capabilityStub=" + capabilityStub +
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
        ServerAssetUseElement that = (ServerAssetUseElement) objectToCompare;
        return Objects.equals(serverAssetUse, that.serverAssetUse) &&
                       Objects.equals(asset, that.asset) &&
                       Objects.equals(capabilityStub, that.capabilityStub);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), serverAssetUse, asset, capabilityStub);
    }
}
