/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataViewRequestBody extends InformationViewHeader{

    private List<DataViewElement> elements;
    private List<Source> sources;
    private String nativeClass;
    private String endpointAddress = "";
    private String name;
    private String author;
    private String id;

    private Long createdTime;
    private String lastModifier;
    private Long lastModifiedTime;


    public List<DataViewElement> getElements() {
        return elements;
    }

    public void setElements(List<DataViewElement> elements) {
        this.elements = elements;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    public String getNativeClass() {
        return nativeClass;
    }

    public void setNativeClass(String nativeClass) {
        this.nativeClass = nativeClass;
    }

    public String getEndpointAddress() {
        return endpointAddress;
    }

    public void setEndpointAddress(String endpointAddress) {
        this.endpointAddress = endpointAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" +
                "elements=" + elements +
                ", sources=" + sources +
                ", nativeClass='" + nativeClass + '\'' +
                ", endpointAddress='" + endpointAddress + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", id='" + id + '\'' +
                ", createdTime=" + createdTime +
                ", lastModifier='" + lastModifier + '\'' +
                ", lastModifiedTime=" + lastModifiedTime +
                '}';
    }
}
