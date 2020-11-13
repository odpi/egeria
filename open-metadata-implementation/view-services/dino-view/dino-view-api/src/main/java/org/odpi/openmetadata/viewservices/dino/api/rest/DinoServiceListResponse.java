/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.rest;




import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;

import java.util.Arrays;
import java.util.List;

public class DinoServiceListResponse extends DinoViewOMVSAPIResponse {


    private List<RegisteredOMAGService> serviceList = null;

    /**
     * Default constructor
     */
    public DinoServiceListResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DinoServiceListResponse(DinoServiceListResponse template)
    {
        super(template);

        if (template != null)
        {
            this.serviceList = template.getServiceList();
        }
    }


    /**
     * Return the serviceList.
     *
     * @return bean
     */
    public List<RegisteredOMAGService> getServiceList()
    {
        return serviceList;
    }


    /**
     * Set the serverList.
     *
     * @param serviceList - bean
     */
    public void setServiceList(List<RegisteredOMAGService> serviceList)
    {
        this.serviceList = serviceList;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DinoServiceListResponse{" +
                "serviceList=" + serviceList +
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
