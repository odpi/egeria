/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service.response;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vertice {

    private Id id;

    private String label;

    private Map<String, List<Edge>> inE;

    private Map<String, List<Edge>> outE;

    private Map<String, List<Property>> properties;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Map<String, List<Edge>> getInE() {
        return inE;
    }

    public void setInE(Map<String, List<Edge>> inE) {
        this.inE = inE;
    }

    public Map<String, List<Edge>> getOutE() {
        return outE;
    }

    public void setOutE(Map<String, List<Edge>> outE) {
        this.outE = outE;
    }

    public Map<String, List<Property>> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, List<Property>> properties) {
        this.properties = properties;
    }
}
