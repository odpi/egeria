/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.rex.api.rest;


import org.odpi.openmetadata.viewservices.rex.api.properties.RexExpandedEntityDetail;

import java.util.Arrays;

public class RexEntityDetailResponse extends RexViewOMVSAPIResponse {

    private RexExpandedEntityDetail expandedEntityDetail;

    /**
     * Default constructor
     */
    public RexEntityDetailResponse()
    {
        super();
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RexEntityDetailResponse(RexEntityDetailResponse template)
    {
        super(template);

        if (template != null) {
            this.expandedEntityDetail = template.getExpandedEntityDetail();
        }
    }


    /**
     * Return the expandedEntityDetail result.
     *
     * @return bean
     */
    public RexExpandedEntityDetail getExpandedEntityDetail()
    {
        return expandedEntityDetail;
    }


    /**
     * Set the expandedEntityDetail result.
     *
     * @param expandedEntityDetail - bean
     */
    public void setExpandedEntityDetail(RexExpandedEntityDetail expandedEntityDetail)
    {
        this.expandedEntityDetail = expandedEntityDetail;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RexEntityDetailResponse{" +
                "expandedEntityDetail=" + expandedEntityDetail +
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


