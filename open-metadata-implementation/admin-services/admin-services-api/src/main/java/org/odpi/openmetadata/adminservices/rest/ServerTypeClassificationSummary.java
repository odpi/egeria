/* SPDX-License-Identifier: Apache 2.0 */
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
 * ServerTypeClassificationSummary provides an object for returning information about a
 * server type that is configured on an OMAG Server Platform.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ServerTypeClassificationSummary extends AdminServicesAPIResponse
{
    private static final long     serialVersionUID    = 1L;

    private String                          serverTypeName;
    private String                          serverTypeDescription;
    private ServerTypeClassificationSummary serverSuperType;
    private String                          serverTypeWiki;


    /**
     * Default constructor
     */
    public ServerTypeClassificationSummary()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ServerTypeClassificationSummary(ServerTypeClassificationSummary template)
    {
        if (template != null)
        {
            this.serverTypeName        = template.getServerTypeName();
            this.serverTypeDescription = template.getServerTypeDescription();
            this.serverSuperType       = template.getServerSuperType();
            this.serverTypeWiki        = template.getServerTypeWiki();
        }
    }


    /**
     * Enum constructor
     *
     * @param enumClassification object to copy
     */
    public ServerTypeClassificationSummary(ServerTypeClassification enumClassification)
    {
        if (enumClassification != null)
        {
            this.serverTypeName        = enumClassification.getServerTypeName();
            this.serverTypeDescription = enumClassification.getServerTypeDescription();
            this.serverTypeWiki        = enumClassification.getServerTypeWiki();

            if (enumClassification.getSuperType() != null)
            {
                this.serverSuperType = new ServerTypeClassificationSummary(enumClassification.getSuperType());
            }
        }
    }


    /**
     * Return the default name for this server type.
     *
     * @return String default name
     */
    public String getServerTypeName()
    {
        return serverTypeName;
    }


    /**
     * Set up the default name for this server type
     *
     * @param serverTypeName name
     */
    public void setServerTypeName(String serverTypeName)
    {
        this.serverTypeName = serverTypeName;
    }


    /**
     * Return the description of this server type.
     *
     * @return text description
     */
    public String getServerTypeDescription()
    {
        return serverTypeDescription;
    }


    /**
     * Set up the description of this server type.
     *
     * @param serverTypeDescription text description
     */
    public void setServerTypeDescription(String serverTypeDescription)
    {
        this.serverTypeDescription = serverTypeDescription;
    }



    /**
     * Return the description of the supertype (if any)
     *
     * @return classification summary or null
     */
    public ServerTypeClassificationSummary getServerSuperType()
    {
        return serverSuperType;
    }


    /**
     * Set up the description of the supertype (if any).
     *
     * @param serverSuperType classification summary or null
     */
    public void setServerSuperType(ServerTypeClassificationSummary serverSuperType)
    {
        this.serverSuperType = serverSuperType;
    }



    /**
     * Return the URL for the wiki page describing this server type.
     *
     * @return String URL name for the wiki page
     */
    public String getServerTypeWiki()
    {
        return serverTypeWiki;
    }


    /**
     * Set up the URL for the wiki page describing this server type.
     *
     * @param serverTypeWiki URL
     */
    public void setServerTypeWiki(String serverTypeWiki)
    {
        this.serverTypeWiki = serverTypeWiki;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ServerTypeClassificationSummary{" +
                "serverTypeName='" + serverTypeName + '\'' +
                ", serverTypeDescription='" + serverTypeDescription + '\'' +
                ", serverSuperType=" + serverSuperType +
                ", serverTypeWiki='" + serverTypeWiki + '\'' +
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
        ServerTypeClassificationSummary that = (ServerTypeClassificationSummary) objectToCompare;
        return Objects.equals(getServerTypeName(), that.getServerTypeName()) &&
                       Objects.equals(getServerTypeDescription(), that.getServerTypeDescription()) &&
                       Objects.equals(getServerSuperType(), that.getServerSuperType()) &&
                       Objects.equals(getServerTypeWiki(), that.getServerTypeWiki());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getServerTypeName(), getServerTypeDescription(), getServerSuperType(), getServerTypeWiki());
    }
}

