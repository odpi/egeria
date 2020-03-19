/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.userinterface.uichassis.springboot.api.rex;



public class RexPreTraversalResponse {

    private Integer          httpStatusCode;
    private String           exceptionText;
    private RexPreTraversal  rexPreTraversal;

    public RexPreTraversalResponse(Integer statusCode, String exceptionText, RexPreTraversal rexPreTraversal) {

        this.httpStatusCode = statusCode;
        this.exceptionText = exceptionText;
        this.rexPreTraversal = rexPreTraversal;
    }

    public Integer getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public String getExceptionText() {
        return this.exceptionText;
    }

    public RexPreTraversal getRexPreTraversal() {
        return this.rexPreTraversal;
    }

    public void setHttpStatusCode(Integer httpStatusCode)
    {
        this.httpStatusCode = httpStatusCode;
    }

    public void setExceptionText(String exceptionText)
    {
        this.exceptionText = exceptionText;
    }

    public void setRexPreTraversal(RexPreTraversal rexPreTraversal)
    {
        this.rexPreTraversal = rexPreTraversal;
    }
}
