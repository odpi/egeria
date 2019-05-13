/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Actual response that is sent to the client. It contains the entities queried from the OMRS
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlossaryViewEntityDetailResponse extends GlossaryViewOMASAPIResponse {

    private List<GlossaryViewEntityDetail> glossaryViewEntityDetails = new ArrayList<>();

    public List<GlossaryViewEntityDetail> getGlossaryViewEntityDetails() {
        return glossaryViewEntityDetails;
    }

    public void addEntityDetails(List<GlossaryViewEntityDetail> glossaryViewEntityDetails) {
        if(this.glossaryViewEntityDetails == null){
            this.glossaryViewEntityDetails = new ArrayList<>();
        }
        this.glossaryViewEntityDetails.addAll(glossaryViewEntityDetails);
    }

    public void addEntityDetail(GlossaryViewEntityDetail glossaryViewEntityDetail){
        if(this.glossaryViewEntityDetails == null){
            this.glossaryViewEntityDetails = new ArrayList<>();
        }
        this.glossaryViewEntityDetails.add(glossaryViewEntityDetail);
    }

}
