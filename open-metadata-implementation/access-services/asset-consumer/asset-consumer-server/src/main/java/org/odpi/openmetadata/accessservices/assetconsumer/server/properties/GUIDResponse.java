/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.server.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GUIDResponse is the response structure used on the Asset Consumer OMAS REST API calls that return a
 * unique identifier (guid) object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GUIDResponse extends AssetConsumerOMASAPIResponse
{
    private String   guid = null;


    /**
     * Default constructor
     */
    public GUIDResponse()
    {
    }


    /**
     * Return the guid result.
     *
     * @return unique identifier
     */
    public String getGUID()
    {
        return guid;
    }

    /**
     * Set up the guid result.
     *
     * @param guid - unique identifier
     */
    public void setGUID(String guid)
    {
        this.guid = guid;
    }


    @Override
    public String toString()
    {
        return "GUIDResponse{" +
                "guid='" + guid + '\'' +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                '}';
    }
}
