/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Asset implements Serializable {
    private static final long serialVersionUID = 1L;

    private String displayName;
    private String description;
    private String owner;
    private OwnerType ownerType;
    private List<String> zoneMembership;
    private Map<String, String> origin;
    private String typeGUID;
    private String typeName;
    private String GUID;
    private String qualifiedName;
    private Map<String, String> additionalProperties;
    private Map<String, Object> extendedProperties;

    /**
     * Gets display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets display name.
     *
     * @param displayName the display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets owner.
     *
     * @param owner the owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Gets owner type.
     *
     * @return the owner type
     */
    public OwnerType getOwnerType() {
        return ownerType;
    }

    /**
     * Sets owner type.
     *
     * @param ownerType the owner type
     */
    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    /**
     * Gets zone membership.
     *
     * @return the zone membership
     */
    public List<String> getZoneMembership() {
        return zoneMembership;
    }

    /**
     * Sets zone membership.
     *
     * @param zoneMembership the zone membership
     */
    public void setZoneMembership(List<String> zoneMembership) {
        this.zoneMembership = zoneMembership;
    }

    /**
     * Gets origin.
     *
     * @return the origin
     */
    public Map<String, String> getOrigin() {
        return origin;
    }

    /**
     * Sets origin.
     *
     * @param origin the origin
     */
    public void setOrigin(Map<String, String> origin) {
        this.origin = origin;
    }

    /**
     * Gets type guid.
     *
     * @return the type guid
     */
    public String getTypeGUID() {
        return typeGUID;
    }

    /**
     * Sets type guid.
     *
     * @param typeGUID the type guid
     */
    public void setTypeGUID(String typeGUID) {
        this.typeGUID = typeGUID;
    }

    /**
     * Gets type name.
     *
     * @return the type name
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Sets type name.
     *
     * @param typeName the type name
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * Gets guid.
     *
     * @return the guid
     */
    public String getGUID() {
        return GUID;
    }

    /**
     * Sets guid.
     *
     * @param GUID the guid
     */
    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    /**
     * Gets qualified name.
     *
     * @return the qualified name
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    /**
     * Sets qualified name.
     *
     * @param qualifiedName the qualified name
     */
    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    /**
     * Gets additional properties.
     *
     * @return the additional properties
     */
    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    /**
     * Sets additional properties.
     *
     * @param additionalProperties the additional properties
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    /**
     * Gets extended properties.
     *
     * @return the extended properties
     */
    public Map<String, Object> getExtendedProperties() {
        return extendedProperties;
    }

    /**
     * Sets extended properties.
     *
     * @param extendedProperties the extended properties
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties) {
        this.extendedProperties = extendedProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return ownerType == asset.ownerType &&
                Objects.equals(displayName, asset.displayName) &&
                Objects.equals(description, asset.description) &&
                Objects.equals(owner, asset.owner) &&
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
        return Objects.hash(displayName, description, owner, ownerType, zoneMembership, origin, typeGUID, typeName, GUID, qualifiedName, additionalProperties, extendedProperties);
    }

    @Override
    public String toString() {
        return "Asset{" +
                "displayName='" + displayName + '\'' +
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
}
