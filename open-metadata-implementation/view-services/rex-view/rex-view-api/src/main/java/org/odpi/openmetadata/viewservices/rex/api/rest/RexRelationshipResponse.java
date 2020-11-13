/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.rex.api.rest;


import org.odpi.openmetadata.viewservices.rex.api.properties.RexExpandedRelationship;

import java.util.Arrays;

public class RexRelationshipResponse extends RexViewOMVSAPIResponse {

    private RexExpandedRelationship expandedRelationship;

    /**
     * Default constructor
     */
    public RexRelationshipResponse()
    {
        super();
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RexRelationshipResponse(RexRelationshipResponse template)
    {
        super(template);

        if (template != null) {
            this.expandedRelationship = template.getExpandedRelationship();
        }
    }


    /**
     * Return the expandedRelationship result.
     *
     * @return bean
     */
    public RexExpandedRelationship getExpandedRelationship()
    {
        return expandedRelationship;
    }


    /**
     * Set the expandedRelationship result.
     *
     * @param expandedRelationship - bean
     */
    public void setExpandedRelationship(RexExpandedRelationship expandedRelationship)
    {
        this.expandedRelationship = expandedRelationship;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RexRelationshipResponse{" +
                "expandedRelationship=" + expandedRelationship +
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
