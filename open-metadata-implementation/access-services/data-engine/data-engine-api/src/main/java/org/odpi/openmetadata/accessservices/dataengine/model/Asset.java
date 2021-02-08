/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Asset is a set of properties that describes an open metadata asset.  It is designed to convey the important properties
 * needed to make a security decision.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Asset {
    private static final long serialVersionUID = 1L;

    private String name;
    private String displayName;
    private String description;
    private String owner;
    private int ownerType;
    private List<String> zoneMembership;
    private Map<String, String> origin;
    private String typeGUID;
    private String typeName;
    private String GUID;
    private String qualifiedName;
    private Map<String, String> additionalProperties;
    private Map<String, Object> extendedProperties;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(int ownerType) {
        this.ownerType = ownerType;
    }

    public List<String> getZoneMembership() {
        return zoneMembership;
    }

    public void setZoneMembership(List<String> zoneMembership) {
        this.zoneMembership = zoneMembership;
    }

    public Map<String, String> getOrigin() {
        return origin;
    }

    public void setOrigin(Map<String, String> origin) {
        this.origin = origin;
    }

    public String getTypeGUID() {
        return typeGUID;
    }

    public void setTypeGUID(String typeGUID) {
        this.typeGUID = typeGUID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public Map<String, Object> getExtendedProperties() {
        return extendedProperties;
    }

    public void setExtendedProperties(Map<String, Object> extendedProperties) {
        this.extendedProperties = extendedProperties;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", ownerType=" + ownerType +
                ", zoneMembership=" + zoneMembership +
                ", origin=" + origin +
                ", typeGUID='" + typeGUID + '\'' +
                ", typeName='" + typeName + '\'' +
                ", GUID='" + GUID + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", extendedProperties=" + extendedProperties +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Asset asset = (Asset) o;

        return Objects.equals(name, asset.name) &&
                Objects.equals(displayName, asset.displayName) &&
                Objects.equals(description, asset.description) &&
                Objects.equals(owner, asset.owner) &&
                Objects.equals(ownerType, asset.ownerType) &&
                Objects.equals(zoneMembership, asset.zoneMembership) &&
                Objects.equals(origin, asset.origin) &&
                Objects.equals(typeGUID, asset.typeGUID) &&
                Objects.equals(typeName, asset.typeName) &&
                Objects.equals(GUID, asset.GUID) &&
                Objects.equals(qualifiedName, asset.qualifiedName) &&
                Objects.equals(additionalProperties, asset.additionalProperties) &&
                Objects.equals(extendedProperties, asset.extendedProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, displayName, description, owner, ownerType, zoneMembership,
                origin, typeGUID, typeName, GUID, qualifiedName, additionalProperties, extendedProperties);
    }
}
