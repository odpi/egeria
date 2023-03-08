/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelatedAsset describes the relationship to other assets.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelatedAsset extends Asset
{
    private static final long     serialVersionUID = 1L;

    private String relationshipName = null;
    private String attributeName    = null;


    /**
     * Default constructor
     */
    public RelatedAsset()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RelatedAsset(RelatedAsset template)
    {
        super(template);

        if (template != null)
        {
            relationshipName = template.getRelationshipName();
            attributeName = template.getAttributeName();
        }
    }


    /**
     * Return the type of relationship to the asset.
     *
     * @return type name string
     */
    public String getRelationshipName()
    {
        return relationshipName;
    }


    /**
     * Set up the type of relationship to the asset.
     *
     * @param relationshipName type name string
     */
    public void setRelationshipName(String relationshipName)
    {
        this.relationshipName = relationshipName;
    }


    /**
     * Return the attribute name for the related asset.
     *
     * @return string name
     */
    public String getAttributeName()
    {
        return attributeName;
    }


    /**
     * Set up the attribute name for the related asset.
     *
     * @param attributeName string name
     */
    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelatedAsset{" +
                       "resourceName='" + getResourceName() + '\'' +
                       ", versionIdentifier='" + getVersionIdentifier() + '\'' +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", displaySummary='" + getDisplaySummary() + '\'' +
                       ", displayDescription='" + getDisplayDescription() + '\'' +
                       ", abbreviation='" + getAbbreviation() + '\'' +
                       ", usage='" + getUsage() + '\'' +
                       ", connectionDescription='" + getConnectionDescription() + '\'' +
                       ", resourceDescription='" + getResourceDescription() + '\'' +
                       ", owner='" + getOwner() + '\'' +
                       ", ownerTypeName='" + getOwnerTypeName() + '\'' +
                       ", ownerPropertyName='" + getOwnerPropertyName() + '\'' +
                       ", ownerType=" + getOwnerType() +
                       ", zoneMembership=" + getZoneMembership() +
                       ", assetOrigin=" + getAssetOrigin() +
                       ", referenceData=" + isReferenceData() +
                       ", URL='" + getURL() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", meanings=" + getMeanings() +
                       ", searchKeywords=" + getSearchKeywords() +
                       ", headerVersion=" + getHeaderVersion() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        RelatedAsset that = (RelatedAsset) objectToCompare;
        return Objects.equals(getRelationshipName(), that.getRelationshipName()) &&
                Objects.equals(getAttributeName(), that.getAttributeName());
    }



    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getRelationshipName(), getAttributeName());
    }
}
