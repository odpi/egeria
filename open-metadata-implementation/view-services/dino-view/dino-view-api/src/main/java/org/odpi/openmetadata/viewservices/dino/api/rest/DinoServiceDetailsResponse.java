/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.rest;



import org.odpi.openmetadata.viewservices.dino.api.properties.ServiceDetails;

import java.util.Arrays;


public class DinoServiceDetailsResponse extends DinoViewOMVSAPIResponse {


    private ServiceDetails serviceDetails = null;

    /**
     * Default constructor
     */
    public DinoServiceDetailsResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DinoServiceDetailsResponse(DinoServiceDetailsResponse template)
    {
        super(template);

        if (template != null)
        {
            this.serviceDetails = template.getServiceDetails();
        }
    }


    /**
     * Return the serviceDetails.
     *
     * @return bean
     */
    public ServiceDetails getServiceDetails()
    {
        return serviceDetails;
    }


    /**
     * Set the serviceDetails.
     *
     * @param serviceDetails - bean
     */
    public void setServiceDetails(ServiceDetails serviceDetails)
    {
        this.serviceDetails = serviceDetails;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DinoServiceDetailsResponse{" +
                "serviceDetails=" + serviceDetails +
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
