/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.model;

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
public class Element {

    private String guid;
    private String typeDef;
    private String typeDefGUID;
    private String qualifiedName;
    private Map<String, Object> properties;
    private List<Element> parentElement;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTypeDef() {
        return typeDef;
    }

    public void setTypeDef(String typeDef) {
        this.typeDef = typeDef;
    }

    public String getTypeDefGUID() {
        return typeDefGUID;
    }

    public void setTypeDefGUID(String typeDefGUID) {
        this.typeDefGUID = typeDefGUID;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public List<Element> getParentElement() {
        return parentElement;
    }

    public void setParentElement(List<Element> parentElement) {
        this.parentElement = parentElement;
    }
}
