/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.userinterface.uichassis.springboot.api.rex;



public class RexEntityDetailResponse {

    private Integer                  httpStatusCode;
    private String                   exceptionText;
    private RexExpandedEntityDetail  expandedEntityDetail;

    public RexEntityDetailResponse(Integer statusCode, String exceptionText, RexExpandedEntityDetail expandedEntityDetail) {

        this.httpStatusCode       = statusCode;
        this.exceptionText        = exceptionText;
        this.expandedEntityDetail = expandedEntityDetail;
    }

    public Integer getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public String getExceptionText() {
        return this.exceptionText;
    }

    public RexExpandedEntityDetail getExpandedEntityDetail() {
        return this.expandedEntityDetail;
    }

    public void setHttpStatusCode(Integer httpStatusCode)
    {
        this.httpStatusCode = httpStatusCode;
    }

    public void setExceptionText(String exceptionText)
    {
        this.exceptionText = exceptionText;
    }

    public void setExpandedEntityDetail(RexExpandedEntityDetail expandedEntityDetail) { this.expandedEntityDetail = expandedEntityDetail; }
}
