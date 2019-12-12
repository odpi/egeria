/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.tex.responses;

import org.odpi.openmetadata.viewservices.tex.properties.TypeExplorer;

/**
 * Response that contains the type explorer object itself
 */

public class TypeExplorerResponse extends TypeExplorerOMVSAPIResponse {


    private TypeExplorer typeExplorer;


    /**
     * constructor
     * @param statusCode http status code for the response
     * @param exceptionText text describing the exception.
     * @param typeExplorer type explorer object
     */
    public TypeExplorerResponse(Integer statusCode, String exceptionText,TypeExplorer typeExplorer) {
        super(statusCode,exceptionText);
        this.typeExplorer = typeExplorer;
        this.responseCategory = ResponseCategory.TypeExplorer;
    }

    /**
     * constructor for exceptions
     * @param statusCode http status code for the response
     * @param exceptionText text describing the exception.
     */
    public TypeExplorerResponse(Integer statusCode, String exceptionText) {
        super(statusCode,exceptionText);
    }


    public TypeExplorer getTypeExplorer() {
        return this.typeExplorer;
    }


    public void setTypeExplorer(TypeExplorer typeExplorer)
    {
        this.typeExplorer = typeExplorer;
    }
}
