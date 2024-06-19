/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMAGServerPlatformProperties captures the properties of a live OMAG Server Platform.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMAGServerPlatformProperties
{
    private String                      platformName                 = null;
    private String                      platformURLRoot              = null;
    private String                      platformOrigin               = null;
    private OMAGConnectorProperties     configurationStoreConnection = null;
    private OMAGConnectorProperties     platformSecurityConnection   = null;
    private List<RegisteredOMAGService> registeredOMAGServices       = null;
    private List<OMAGServerProperties>  omagServers                  = null;

    public OMAGServerPlatformProperties()
    {
    }


    public String getPlatformName()
    {
        return platformName;
    }

    public void setPlatformName(String platformName)
    {
        this.platformName = platformName;
    }

    public String getPlatformURLRoot()
    {
        return platformURLRoot;
    }

    public void setPlatformURLRoot(String platformURLRoot)
    {
        this.platformURLRoot = platformURLRoot;
    }

    public String getPlatformOrigin()
    {
        return platformOrigin;
    }

    public void setPlatformOrigin(String platformOrigin)
    {
        this.platformOrigin = platformOrigin;
    }

    public OMAGConnectorProperties getConfigurationStoreConnection()
    {
        return configurationStoreConnection;
    }

    public void setConfigurationStoreConnection(OMAGConnectorProperties configurationStoreConnection)
    {
        this.configurationStoreConnection = configurationStoreConnection;
    }

    public OMAGConnectorProperties getPlatformSecurityConnection()
    {
        return platformSecurityConnection;
    }

    public void setPlatformSecurityConnection(OMAGConnectorProperties platformSecurityConnection)
    {
        this.platformSecurityConnection = platformSecurityConnection;
    }

    public List<RegisteredOMAGService> getRegisteredOMAGServices()
    {
        return registeredOMAGServices;
    }

    public void setRegisteredOMAGServices(List<RegisteredOMAGService> registeredOMAGServices)
    {
        this.registeredOMAGServices = registeredOMAGServices;
    }

    public List<OMAGServerProperties> getOMAGServers()
    {
        return omagServers;
    }

    public void setOMAGServers(List<OMAGServerProperties> omagServers)
    {
        this.omagServers = omagServers;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OMAGServerPlatformProperties{" +
                "platformName='" + platformName + '\'' +
                ", platformURLRoot='" + platformURLRoot + '\'' +
                ", platformOrigin='" + platformOrigin + '\'' +
                ", configurationStoreConnection=" + configurationStoreConnection +
                ", platformSecurityConnection=" + platformSecurityConnection +
                ", registeredOMAGServices=" + registeredOMAGServices +
                ", omagServers=" + omagServers +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        OMAGServerPlatformProperties that = (OMAGServerPlatformProperties) objectToCompare;
        return Objects.equals(platformName, that.platformName) && Objects.equals(platformURLRoot, that.platformURLRoot) && Objects.equals(platformOrigin, that.platformOrigin) && Objects.equals(configurationStoreConnection, that.configurationStoreConnection) && Objects.equals(platformSecurityConnection, that.platformSecurityConnection) && Objects.equals(registeredOMAGServices, that.registeredOMAGServices) && Objects.equals(omagServers, that.omagServers);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(platformName, platformURLRoot, platformOrigin, configurationStoreConnection, platformSecurityConnection, registeredOMAGServices, omagServers);
    }
}
