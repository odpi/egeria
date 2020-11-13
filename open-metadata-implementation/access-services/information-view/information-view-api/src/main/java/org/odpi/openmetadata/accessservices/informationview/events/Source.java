/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.events;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EndpointSource.class, name = "EndpointSource"),
        @JsonSubTypes.Type(value = TableSource.class, name = "TableSource"),
        @JsonSubTypes.Type(value = DatabaseColumnSource.class, name = "DatabaseColumnSource"),
        @JsonSubTypes.Type(value = ReportColumnSource.class, name = "ReportColumnSource"),
        @JsonSubTypes.Type(value = ReportSection.class, name = "ReportSection"),
        @JsonSubTypes.Type(value = ReportSectionSource.class, name = "ReportSectionSource"),
        @JsonSubTypes.Type(value = DataViewSource.class, name = "DataViewSource"),
        @JsonSubTypes.Type(value = DataViewColumnSource.class, name = "DataViewColumnSource"),
        @JsonSubTypes.Type(value = DatabaseSource.class, name = "DatabaseSource"),
        @JsonSubTypes.Type(value = SoftwareServerCapabilitySource.class, name = "SoftwareServerCapabilitySource")})

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class , property="guid")
@JsonIdentityReference
public abstract class Source {


    protected Map<String, String> additionalProperties;
    protected String qualifiedName;
    protected String guid;


    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }


    @Override
    public String toString() {
        return "{" +
                "additionalProperties=" + additionalProperties +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", guid='" + guid + '\'' +
                '}';
    }
}
