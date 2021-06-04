/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.rest;



import org.odpi.openmetadata.viewservices.dino.api.properties.EngineDetails;
import org.odpi.openmetadata.viewservices.dino.api.properties.ServiceDetails;

import java.util.Arrays;


public class DinoEngineDetailsResponse extends DinoViewOMVSAPIResponse {


    private EngineDetails engineDetails = null;

    /**
     * Default constructor
     */
    public DinoEngineDetailsResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DinoEngineDetailsResponse(DinoEngineDetailsResponse template)
    {
        super(template);

        if (template != null)
        {
            this.engineDetails = template.getEngineDetails();
        }
    }


    /**
     * Return the engineDetails.
     *
     * @return bean
     */
    public EngineDetails getEngineDetails()
    {
        return engineDetails;
    }


    /**
     * Set the engineDetails.
     *
     * @param engineDetails - bean
     */
    public void setEngineDetails(EngineDetails engineDetails)
    {
        this.engineDetails = engineDetails;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DinoEngineDetailsResponse{" +
                "engineDetails=" + engineDetails +
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
