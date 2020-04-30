/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataplatform.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated
public class DatabaseSource extends Source {

    private static final long     serialVersionUID = 1L;

    private String name;
    private EndpointSource endpointSource;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EndpointSource getEndpointSource() {
        return endpointSource;
    }

    public void setEndpointSource(EndpointSource endpointSource) {
        this.endpointSource = endpointSource;
    }


    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", endpointSource=" + endpointSource +
                '}';
    }
}