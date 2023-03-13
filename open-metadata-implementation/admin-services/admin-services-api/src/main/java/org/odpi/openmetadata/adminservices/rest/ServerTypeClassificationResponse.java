/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.registration.ServerTypeClassification;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ServerClassificationResponse provides an object for returning information about a
 * server type that is configured on an OMAG Server Platform.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ServerTypeClassificationResponse extends AdminServicesAPIResponse
{
    private static final long     serialVersionUID    = 1L;

    private ServerTypeClassificationSummary serverTypeClassification;

    /**
     * Default constructor
     */
    public ServerTypeClassificationResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ServerTypeClassificationResponse(ServerTypeClassificationResponse template)
    {
        if (template != null)
        {
            this.serverTypeClassification = template.getServerTypeClassification();
        }
    }



    /**
     * Return the classification for this server type.
     *
     * @return server type classification structure
     */
    public ServerTypeClassificationSummary getServerTypeClassification()
    {
        return serverTypeClassification;
    }


    /**
     * Set up the classification for this server type.
     *
     * @param serverTypeClassification server type classification structure
     */
    public void setServerTypeClassification(ServerTypeClassificationSummary serverTypeClassification)
    {
        this.serverTypeClassification = serverTypeClassification;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ServerTypeClassificationResponse{" +
                "serverTypeClassification=" + serverTypeClassification +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ServerTypeClassificationResponse that = (ServerTypeClassificationResponse) objectToCompare;
        return Objects.equals(serverTypeClassification, that.serverTypeClassification);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getServerTypeClassification());
    }
}

