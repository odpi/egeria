/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.tex.responses;

import java.util.Set;

public  class MetadataServersResponse extends TypeExplorerOMVSAPIResponse {

    private Set<String> metadataServerNames;


    /**
     * constructor
     * @param statusCode http status code for the response
     * @param exceptionText text describing the exception.
     */
    public MetadataServersResponse(Integer statusCode, String exceptionText, Set<String> metadataServerNames) {
        super(statusCode,exceptionText);
        this.metadataServerNames =metadataServerNames;
        this.responseCategory = ResponseCategory.MetadataServerNames;
    }

    public Set<String> getMetadataServerNames() {
        return metadataServerNames;
    }

    public void setMetadataServerNames(Set<String> metadataServerNames) {
        this.metadataServerNames = metadataServerNames;
    }
}
