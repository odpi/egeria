/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.rest;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Actual response that is sent to the client. It contains the entities queried from the OMRS
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlossaryViewEntityDetailResponse extends GlossaryViewOMASAPIResponse {

    private static Map<String, List<GlossaryViewEntityDetail>> defaultMap = Collections.singletonMap("entities", null);
    private static Map<String, String> singularToPlural = new HashMap<>();
    static{
        singularToPlural.put("Glossary", "glossaries");
        singularToPlural.put("GlossaryCategory", "categories");
        singularToPlural.put("GlossaryTerm", "terms");
    }

    private List<GlossaryViewEntityDetail> glossaryViewEntityDetails = new ArrayList<>();

    @JsonAnyGetter
    public Map<String, List<GlossaryViewEntityDetail>> getGlossaryViewEntityDetails() {
        if(glossaryViewEntityDetails.isEmpty()){
            return defaultMap;
        }
        String key = singularToPlural.get( glossaryViewEntityDetails.get(0).getEntityType() ) ;
        if(key == null){
            return defaultMap;
        }
        return Collections.singletonMap(key, glossaryViewEntityDetails);
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
