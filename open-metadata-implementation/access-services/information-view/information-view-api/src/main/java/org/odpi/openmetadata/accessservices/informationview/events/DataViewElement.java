/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.events;


import com.fasterxml.jackson.annotation.*;


import javax.validation.constraints.NotBlank;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DataViewColumn.class, name = "DataViewColumn"),
        @JsonSubTypes.Type(value = DataViewModel.class, name = "DataViewModel")})

@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property="id")
@JsonIdentityReference
public class DataViewElement {

    @NotBlank
    private String id;
    @NotBlank
    private String name;
    private String nativeClass;
    private String comment;
    private String description;
    private List<DataViewElement> elements;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNativeClass() {
        return nativeClass;
    }

    public void setNativeClass(String nativeClass) {
        this.nativeClass = nativeClass;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DataViewElement> getElements() {
        return elements;
    }

    public void setElements(List<DataViewElement> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", nativeClass='" + nativeClass + '\'' +
                ", comment='" + comment + '\'' +
                ", description='" + description + '\'' +
                ", elements=" + elements +
                '}';
    }
}
