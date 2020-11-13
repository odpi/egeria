/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.userinterface.uichassis.springboot.api.rex;


public class RexRelationshipResponse {

    private Integer       httpStatusCode;
    private String        exceptionText;
    private RexExpandedRelationship  expandedRelationship;

    public RexRelationshipResponse(Integer statusCode, String exceptionText, RexExpandedRelationship expandedRelationship) {

        this.httpStatusCode       = statusCode;
        this.exceptionText        = exceptionText;
        this.expandedRelationship = expandedRelationship;

    }

    public Integer getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public String getExceptionText() {
        return this.exceptionText;
    }

    public RexExpandedRelationship getExpandedRelationship() {
        return this.expandedRelationship;
    }

    public void setHttpStatusCode(Integer httpStatusCode)
    {
        this.httpStatusCode = httpStatusCode;
    }

    public void setExceptionText(String exceptionText)
    {
        this.exceptionText = exceptionText;
    }

    public void setExpandedRelationship(RexExpandedRelationship expandedRelationship) { this.expandedRelationship = expandedRelationship;  }
}
