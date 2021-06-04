/* SPDX-License-Identifier: Apache-2.0 */
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

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Asset extends Referenceable {
    private static final long serialVersionUID = 1L;
    private String displayName;
    private String description;
    private String owner;
    private OwnerType ownerType;
    private List<String> zoneMembership;
    private String GUID;
    private String originOrganizationGUID;
    private String originBusinessCapabilityGUID;
    private Map<String, String> otherOriginValues;

    /**
     * Returns the stored display name property for the asset.
     * If no display name is available then null is returned.
     *
     * @return String name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Set up the stored display name property for the asset.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the stored description property for the asset.
     * If no description is provided then null is returned.
     *
     * @return description String text
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set up the stored description property associated with the asset.
     *
     * @param description String text
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the name of the owner for this asset.
     *
     * @return owner String
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Set up the name of the owner for this asset.
     *
     * @param owner String name
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Return the type of owner stored in the owner property.
     *
     * @return OwnerType enum
     */
    public OwnerType getOwnerType() {
        return ownerType;
    }

    /**
     * Set up the owner type for this asset.
     *
     * @param ownerType OwnerType enum
     */
    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    /**
     * Return the names of the zones that this asset is a member of.
     *
     * @return list of zone names
     */
    public List<String> getZoneMembership() {
        return zoneMembership;
    }

    /**
     * Set up the names of the zones that this asset is a member of.
     *
     * @param zoneMembership list of zone names
     */
    public void setZoneMembership(List<String> zoneMembership) {
        this.zoneMembership = zoneMembership;
    }

    /**
     * Return the unique identifier for the organization that originated this asset.
     *
     * @return string guid
     */
    public String getOriginOrganizationGUID() {
        return originOrganizationGUID;
    }

    /**
     * Set up the unique identifier for the organization that originated this asset.
     *
     * @param originOrganizationGUID string guid
     */
    public void setOriginOrganizationGUID(String originOrganizationGUID) {
        this.originOrganizationGUID = originOrganizationGUID;
    }

    /**
     * Return the unique identifier of the business capability that originated this asset.
     *
     * @return string guid
     */
    public String getOriginBusinessCapabilityGUID() {
        return originBusinessCapabilityGUID;
    }

    /**
     * Set up the unique identifier of the business capability that originated this asset.
     *
     * @param originBusinessCapabilityGUID string guid
     */
    public void setOriginBusinessCapabilityGUID(String originBusinessCapabilityGUID) {
        this.originBusinessCapabilityGUID = originBusinessCapabilityGUID;
    }

    /**
     * Return the properties that characterize where this asset is from.
     *
     * @return map of name value pairs, all strings
     */
    public Map<String, String> getOtherOriginValues() {
        return otherOriginValues;
    }

    /**
     * Set up the properties that characterize where this asset is from.
     *
     * @param otherOriginValues map of name value pairs, all strings
     */
    public void setOtherOriginValues(Map<String, String> otherOriginValues) {
        this.otherOriginValues = otherOriginValues;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Asset asset = (Asset) o;
        return Objects.equals(displayName, asset.displayName) &&
                Objects.equals(description, asset.description) &&
                Objects.equals(owner, asset.owner) &&
                ownerType == asset.ownerType &&
                Objects.equals(zoneMembership, asset.zoneMembership) &&
                Objects.equals(GUID, asset.GUID) &&
                Objects.equals(originOrganizationGUID, asset.originOrganizationGUID) &&
                Objects.equals(originBusinessCapabilityGUID, asset.originBusinessCapabilityGUID) &&
                Objects.equals(otherOriginValues, asset.otherOriginValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), displayName, description, owner, ownerType, zoneMembership, GUID, originOrganizationGUID,
                originBusinessCapabilityGUID, otherOriginValues);
    }

    @Override
    public String toString() {
        return "Asset{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", ownerType=" + ownerType +
                ", zoneMembership=" + zoneMembership +
                ", GUID='" + GUID + '\'' +
                ", originOrganizationGUID='" + originOrganizationGUID + '\'' +
                ", originBusinessCapabilityGUID='" + originBusinessCapabilityGUID + '\'' +
                ", otherOriginValues=" + otherOriginValues +
                '}';
    }
}
