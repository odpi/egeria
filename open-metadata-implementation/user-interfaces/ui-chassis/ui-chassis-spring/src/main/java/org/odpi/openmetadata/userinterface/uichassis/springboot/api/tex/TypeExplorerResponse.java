/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.userinterface.uichassis.springboot.api.tex;

public class TypeExplorerResponse {

    private Integer      httpStatusCode;
    private String       exceptionText;
    private TypeExplorer typeExplorer;

    public TypeExplorerResponse(Integer statusCode, String exceptionText, TypeExplorer tex) {

        this.httpStatusCode = statusCode;
        this.exceptionText = exceptionText;
        this.typeExplorer = tex;
    }

    public Integer getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public String getExceptionText() {
        return this.exceptionText;
    }

    public TypeExplorer getTypeExplorer() {
        return this.typeExplorer;
    }

    public void setHttpStatusCode(Integer httpStatusCode)
    {
        this.httpStatusCode = httpStatusCode;
    }

    public void setExceptionText(String exceptionText)
    {
        this.exceptionText = exceptionText;
    }

    public void setTypeExplorer(TypeExplorer typeExplorer)
    {
        this.typeExplorer = typeExplorer;
    }
}
