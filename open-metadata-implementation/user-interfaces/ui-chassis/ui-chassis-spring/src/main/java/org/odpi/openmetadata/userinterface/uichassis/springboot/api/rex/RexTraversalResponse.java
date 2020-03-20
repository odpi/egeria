/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.userinterface.uichassis.springboot.api.rex;



public class RexTraversalResponse {

    private Integer       httpStatusCode;
    private String        exceptionText;
    private RexTraversal  rexTraversal;

    public RexTraversalResponse(Integer statusCode, String exceptionText, RexTraversal rexTraversal) {

        this.httpStatusCode = statusCode;
        this.exceptionText = exceptionText;
        this.rexTraversal = rexTraversal;
    }

    public Integer getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public String getExceptionText() {
        return this.exceptionText;
    }

    public RexTraversal getRexTraversal() {
        return this.rexTraversal;
    }

    public void setHttpStatusCode(Integer httpStatusCode)
    {
        this.httpStatusCode = httpStatusCode;
    }

    public void setExceptionText(String exceptionText)
    {
        this.exceptionText = exceptionText;
    }

    public void setRexTraversal(RexTraversal rexTraversal)
    {
        this.rexTraversal = rexTraversal;
    }
}
