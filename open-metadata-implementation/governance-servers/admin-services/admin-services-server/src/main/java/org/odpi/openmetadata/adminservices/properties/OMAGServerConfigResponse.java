/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adminservices.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * OMAGServerConfigResponse is the response structure used on the OMAG REST API calls that returns a
 * Connection object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMAGServerConfigResponse extends OMAGAPIResponse
{
    private OMAGServerConfig serverConfig = null;

    /**
     * Default constructor
     */
    public OMAGServerConfigResponse()
    {
    }


    /**
     * Return the OMAGServerConfig object.
     *
     * @return OMAGServerConfig object
     */
    public OMAGServerConfig getOMAGServerConfig()
    {
        return serverConfig;
    }

    /**
     * Set up the OMAGServerConfig object.
     *
     * @param serverConfig - OMAGServerConfig object
     */
    public void setOMAGServerConfig(OMAGServerConfig serverConfig)
    {
        this.serverConfig = serverConfig;
    }

    @Override
    public String toString()
    {
        return "OMAGServerConfigResponse{" +
                "serverConfig=" + serverConfig +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
