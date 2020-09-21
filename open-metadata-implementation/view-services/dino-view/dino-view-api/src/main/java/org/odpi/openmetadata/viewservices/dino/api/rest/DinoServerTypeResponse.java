/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DinoServerTypeResponse extends DinoViewOMVSAPIResponse {

    private String serverTypeName = null;
    private String serverTypeDescription = null;

    /**
     * Default constructor
     */
    public DinoServerTypeResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DinoServerTypeResponse(DinoServerTypeResponse template)
    {
        super(template);

        if (template != null)
        {
            this.serverTypeName = template.getServerTypeName();
            this.serverTypeDescription = template.getServerTypeDescription();
        }
    }


    /**
     * Return the serverTypeName.
     *
     * @return string
     */
    public String getServerTypeName()
    {
        return serverTypeName;
    }


    /**
     * Set the serverTypeName.
     *
     * @param serverTypeName - string
     */
    public void setServerTypeName(String serverTypeName)
    {
        this.serverTypeName = serverTypeName;
    }

    /**
     * Return the serverTypeDescription.
     *
     * @return string
     */
    public String getServerTypeDescription()
    {
        return serverTypeDescription;
    }


    /**
     * Set the serverTypeDescription.
     *
     * @param serverTypeDescription - string
     */
    public void setServerTypeDescription(String serverTypeDescription)
    {
        this.serverTypeDescription = serverTypeDescription;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DinoServerListResponse{" +
                "serverTypeName=" + serverTypeName +
                ", serverTypeDescription=" + serverTypeDescription +
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





}
