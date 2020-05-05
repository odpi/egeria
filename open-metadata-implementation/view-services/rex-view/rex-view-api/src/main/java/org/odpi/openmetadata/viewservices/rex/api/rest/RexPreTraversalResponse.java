/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.rex.api.rest;


import org.odpi.openmetadata.viewservices.rex.api.properties.RexPreTraversal;

import java.util.Arrays;

public class RexPreTraversalResponse extends RexViewOMVSAPIResponse {


    private RexPreTraversal rexPreTraversal;

    /**
     * Default constructor
     */
    public RexPreTraversalResponse()
    {
        super();
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RexPreTraversalResponse(RexPreTraversalResponse template)
    {
        super(template);

        if (template != null) {
            this.rexPreTraversal = template.getRexPreTraversal();
        }
    }


    /**
     * Return the rexTraversal result.
     *
     * @return bean
     */
    public RexPreTraversal getRexPreTraversal()
    {
        return rexPreTraversal;
    }


    /**
     * Set the rexTraversal result.
     *
     * @param rexPreTraversal - bean
     */
    public void setRexPreTraversal(RexPreTraversal rexPreTraversal)
    {
        this.rexPreTraversal = rexPreTraversal;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RexPreTraversalResponse{" +
                "rexPreTraversal=" + rexPreTraversal +
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
