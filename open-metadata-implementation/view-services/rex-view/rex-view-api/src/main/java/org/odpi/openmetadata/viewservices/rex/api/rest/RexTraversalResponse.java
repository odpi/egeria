/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.rex.api.rest;


import org.odpi.openmetadata.viewservices.rex.api.properties.RexTraversal;

import java.util.Arrays;

public class RexTraversalResponse extends RexViewOMVSAPIResponse {


    private RexTraversal  rexTraversal;

    /**
     * Default constructor
     */
    public RexTraversalResponse()
    {
        super();
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RexTraversalResponse(RexTraversalResponse template)
    {
        super(template);

        if (template != null) {
            this.rexTraversal = template.getRexTraversal();
        }
    }


    /**
     * Return the rexTraversal result.
     *
     * @return bean
     */
    public RexTraversal getRexTraversal()
    {
        return rexTraversal;
    }


    /**
     * Set the rexTraversal result.
     *
     * @param rexTraversal - bean
     */
    public void setRexTraversal(RexTraversal rexTraversal)
    {
        this.rexTraversal = rexTraversal;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RexTraversalResponse{" +
                "rexTraversal=" + rexTraversal +
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
