/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataTypeDefPatch describes a change to an open metadata type definition.  It names the type and the
 * version it applies to, and carries only the parts of the type that are to change - a property left null is
 * left as it is.  New attributes are added to the type; an attribute that is already defined may be redefined as
 * long as its attribute type stays the same.  Attributes cannot be removed.
 * <br><br>
 * The version the patch applies to guards against two people changing the type at once: the patch is rejected
 * if the type has moved on from that version.  The version the patch creates, and its name, default to the next
 * version number if they are not supplied.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataTypeDefPatch extends OpenMetadataTypeDefElementHeader
{
    private String                               typeDefGUID                  = null;
    private String                               typeDefName                  = null;
    private long                                 applyToVersion               = 0L;
    private long                                 updateToVersion              = 0L;
    private String                               newVersionName               = null;
    private OpenMetadataTypeDefStatus            typeDefStatus                = null;
    private String                               description                  = null;
    private String                               descriptionGUID              = null;
    private OpenMetadataTypeDefLink              superType                    = null;
    private List<OpenMetadataTypeDefAttribute>   attributeDefinitions         = null;
    private Map<String, String>                  options                      = null;
    private List<ExternalStandardTypeMapping>    externalStandardTypeMappings = null;
    private List<ElementStatus>                  validElementStatusList       = null;
    private ElementStatus                        initialStatus                = null;
    private List<OpenMetadataTypeDefLink>        validEntityDefs              = null;  // ClassificationDefs
    private OpenMetadataRelationshipEndDef       endDef1                      = null;  // RelationshipDefs
    private OpenMetadataRelationshipEndDef       endDef2                      = null;  // RelationshipDefs
    private OpenMetadataRelationshipCategory     relationshipCategory         = null;  // RelationshipDefs


    /**
     * Default constructor
     */
    public OpenMetadataTypeDefPatch()
    {
        super();
    }


    /**
     * Copy/clone constructor copies the values from the supplied template.
     *
     * @param template OpenMetadataTypeDefPatch
     */
    public OpenMetadataTypeDefPatch(OpenMetadataTypeDefPatch template)
    {
        super(template);

        if (template != null)
        {
            this.typeDefGUID                  = template.getTypeDefGUID();
            this.typeDefName                  = template.getTypeDefName();
            this.applyToVersion               = template.getApplyToVersion();
            this.updateToVersion              = template.getUpdateToVersion();
            this.newVersionName               = template.getNewVersionName();
            this.typeDefStatus                = template.getTypeDefStatus();
            this.description                  = template.getDescription();
            this.descriptionGUID              = template.getDescriptionGUID();
            this.superType                    = template.getSuperType();
            this.attributeDefinitions         = template.getAttributeDefinitions();
            this.options                      = template.getOptions();
            this.externalStandardTypeMappings = template.getExternalStandardTypeMappings();
            this.validElementStatusList       = template.getValidElementStatusList();
            this.initialStatus                = template.getInitialStatus();
            this.validEntityDefs              = template.getValidEntityDefs();
            this.endDef1                      = template.getEndDef1();
            this.endDef2                      = template.getEndDef2();
            this.relationshipCategory         = template.getRelationshipCategory();
        }
    }


    /**
     * Return the unique identifier of the type definition to change.
     *
     * @return String guid
     */
    public String getTypeDefGUID()
    {
        return typeDefGUID;
    }


    /**
     * Set up the unique identifier of the type definition to change.
     *
     * @param typeDefGUID String guid
     */
    public void setTypeDefGUID(String typeDefGUID)
    {
        this.typeDefGUID = typeDefGUID;
    }


    /**
     * Return the unique name of the type definition to change.
     *
     * @return String name
     */
    public String getTypeDefName()
    {
        return typeDefName;
    }


    /**
     * Set up the unique name of the type definition to change.
     *
     * @param typeDefName String name
     */
    public void setTypeDefName(String typeDefName)
    {
        this.typeDefName = typeDefName;
    }


    /**
     * Return the version of the type definition that this patch applies to.
     *
     * @return long version number
     */
    public long getApplyToVersion()
    {
        return applyToVersion;
    }


    /**
     * Set up the version of the type definition that this patch applies to.
     *
     * @param applyToVersion long version number
     */
    public void setApplyToVersion(long applyToVersion)
    {
        this.applyToVersion = applyToVersion;
    }


    /**
     * Return the version of the type definition that this patch creates.  Zero means the next version.
     *
     * @return long version number
     */
    public long getUpdateToVersion()
    {
        return updateToVersion;
    }


    /**
     * Set up the version of the type definition that this patch creates.  Zero means the next version.
     *
     * @param updateToVersion long version number
     */
    public void setUpdateToVersion(long updateToVersion)
    {
        this.updateToVersion = updateToVersion;
    }


    /**
     * Return the name of the version of the type definition that this patch creates.
     *
     * @return String version name
     */
    public String getNewVersionName()
    {
        return newVersionName;
    }


    /**
     * Set up the name of the version of the type definition that this patch creates.
     *
     * @param newVersionName String version name
     */
    public void setNewVersionName(String newVersionName)
    {
        this.newVersionName = newVersionName;
    }


    /**
     * Return the new status of the type definition, or null to leave it as it is.
     *
     * @return status enum
     */
    public OpenMetadataTypeDefStatus getTypeDefStatus()
    {
        return typeDefStatus;
    }


    /**
     * Set up the new status of the type definition, or null to leave it as it is.
     *
     * @param typeDefStatus status enum
     */
    public void setTypeDefStatus(OpenMetadataTypeDefStatus typeDefStatus)
    {
        this.typeDefStatus = typeDefStatus;
    }


    /**
     * Return the new description of the type definition, or null to leave it as it is.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the new description of the type definition, or null to leave it as it is.
     *
     * @param description String description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the unique identifier of the new glossary term that describes the type definition, or null to leave
     * it as it is.
     *
     * @return String guid
     */
    public String getDescriptionGUID()
    {
        return descriptionGUID;
    }


    /**
     * Set up the unique identifier of the new glossary term that describes the type definition, or null to leave
     * it as it is.
     *
     * @param descriptionGUID String guid
     */
    public void setDescriptionGUID(String descriptionGUID)
    {
        this.descriptionGUID = descriptionGUID;
    }


    /**
     * Return the new supertype of the type definition, or null to leave it as it is.
     *
     * @return link to the supertype
     */
    public OpenMetadataTypeDefLink getSuperType()
    {
        return superType;
    }


    /**
     * Set up the new supertype of the type definition, or null to leave it as it is.
     *
     * @param superType link to the supertype
     */
    public void setSuperType(OpenMetadataTypeDefLink superType)
    {
        this.superType = superType;
    }


    /**
     * Return the attributes to add to, or redefine in, the type definition.
     *
     * @return list of attribute definitions
     */
    public List<OpenMetadataTypeDefAttribute> getAttributeDefinitions()
    {
        return attributeDefinitions;
    }


    /**
     * Set up the attributes to add to, or redefine in, the type definition.
     *
     * @param attributeDefinitions list of attribute definitions
     */
    public void setAttributeDefinitions(List<OpenMetadataTypeDefAttribute> attributeDefinitions)
    {
        this.attributeDefinitions = attributeDefinitions;
    }


    /**
     * Return the new options for the type definition, which replace the existing options, or null to leave them
     * as they are.
     *
     * @return map of options
     */
    public Map<String, String> getOptions()
    {
        return options;
    }


    /**
     * Set up the new options for the type definition, which replace the existing options, or null to leave them
     * as they are.
     *
     * @param options map of options
     */
    public void setOptions(Map<String, String> options)
    {
        this.options = options;
    }


    /**
     * Return the new mappings to external standards, which replace the existing mappings, or null to leave them
     * as they are.
     *
     * @return list of mappings
     */
    public List<ExternalStandardTypeMapping> getExternalStandardTypeMappings()
    {
        return externalStandardTypeMappings;
    }


    /**
     * Set up the new mappings to external standards, which replace the existing mappings, or null to leave them
     * as they are.
     *
     * @param externalStandardTypeMappings list of mappings
     */
    public void setExternalStandardTypeMappings(List<ExternalStandardTypeMapping> externalStandardTypeMappings)
    {
        this.externalStandardTypeMappings = externalStandardTypeMappings;
    }


    /**
     * Return the new list of statuses that an element of this type may have, or null to leave it as it is.
     *
     * @return list of statuses
     */
    public List<ElementStatus> getValidElementStatusList()
    {
        return validElementStatusList;
    }


    /**
     * Set up the new list of statuses that an element of this type may have, or null to leave it as it is.
     *
     * @param validElementStatusList list of statuses
     */
    public void setValidElementStatusList(List<ElementStatus> validElementStatusList)
    {
        this.validElementStatusList = validElementStatusList;
    }


    /**
     * Return the new status that an element of this type is given when it is created, or null to leave it as it is.
     *
     * @return status
     */
    public ElementStatus getInitialStatus()
    {
        return initialStatus;
    }


    /**
     * Set up the new status that an element of this type is given when it is created, or null to leave it as it is.
     *
     * @param initialStatus status
     */
    public void setInitialStatus(ElementStatus initialStatus)
    {
        this.initialStatus = initialStatus;
    }


    /**
     * Return the new list of entity types that a classification type can be attached to, or null to leave it as
     * it is.  Only used for classification types.
     *
     * @return list of links to entity types
     */
    public List<OpenMetadataTypeDefLink> getValidEntityDefs()
    {
        return validEntityDefs;
    }


    /**
     * Set up the new list of entity types that a classification type can be attached to, or null to leave it as
     * it is.  Only used for classification types.
     *
     * @param validEntityDefs list of links to entity types
     */
    public void setValidEntityDefs(List<OpenMetadataTypeDefLink> validEntityDefs)
    {
        this.validEntityDefs = validEntityDefs;
    }


    /**
     * Return the new definition of end 1 of a relationship type, or null to leave it as it is.
     *
     * @return end definition
     */
    public OpenMetadataRelationshipEndDef getEndDef1()
    {
        return endDef1;
    }


    /**
     * Set up the new definition of end 1 of a relationship type, or null to leave it as it is.
     *
     * @param endDef1 end definition
     */
    public void setEndDef1(OpenMetadataRelationshipEndDef endDef1)
    {
        this.endDef1 = endDef1;
    }


    /**
     * Return the new definition of end 2 of a relationship type, or null to leave it as it is.
     *
     * @return end definition
     */
    public OpenMetadataRelationshipEndDef getEndDef2()
    {
        return endDef2;
    }


    /**
     * Set up the new definition of end 2 of a relationship type, or null to leave it as it is.
     *
     * @param endDef2 end definition
     */
    public void setEndDef2(OpenMetadataRelationshipEndDef endDef2)
    {
        this.endDef2 = endDef2;
    }


    /**
     * Return the new category of a relationship type, or null to leave it as it is.  Only whether it is
     * MULTI_LINK is recorded; the difference between UNI_LINK and REVERSIBLE comes from the attribute names
     * at the ends.
     *
     * @return relationship category
     */
    public OpenMetadataRelationshipCategory getRelationshipCategory()
    {
        return relationshipCategory;
    }


    /**
     * Set up the new category of a relationship type, or null to leave it as it is.  Only whether it is
     * MULTI_LINK is recorded; the difference between UNI_LINK and REVERSIBLE comes from the attribute names
     * at the ends.
     *
     * @param relationshipCategory relationship category
     */
    public void setRelationshipCategory(OpenMetadataRelationshipCategory relationshipCategory)
    {
        this.relationshipCategory = relationshipCategory;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataTypeDefPatch{" +
                "typeDefGUID='" + typeDefGUID + '\'' +
                ", typeDefName='" + typeDefName + '\'' +
                ", applyToVersion=" + applyToVersion +
                ", updateToVersion=" + updateToVersion +
                ", newVersionName='" + newVersionName + '\'' +
                ", typeDefStatus=" + typeDefStatus +
                ", description='" + description + '\'' +
                ", descriptionGUID='" + descriptionGUID + '\'' +
                ", superType=" + superType +
                ", attributeDefinitions=" + attributeDefinitions +
                ", options=" + options +
                ", externalStandardTypeMappings=" + externalStandardTypeMappings +
                ", validElementStatusList=" + validElementStatusList +
                ", initialStatus=" + initialStatus +
                ", validEntityDefs=" + validEntityDefs +
                ", endDef1=" + endDef1 +
                ", endDef2=" + endDef2 +
                ", relationshipCategory=" + relationshipCategory +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        OpenMetadataTypeDefPatch that = (OpenMetadataTypeDefPatch) objectToCompare;
        return applyToVersion == that.applyToVersion &&
                       updateToVersion == that.updateToVersion &&
                       Objects.equals(typeDefGUID, that.typeDefGUID) &&
                       Objects.equals(typeDefName, that.typeDefName) &&
                       Objects.equals(newVersionName, that.newVersionName) &&
                       typeDefStatus == that.typeDefStatus &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(descriptionGUID, that.descriptionGUID) &&
                       Objects.equals(superType, that.superType) &&
                       Objects.equals(attributeDefinitions, that.attributeDefinitions) &&
                       Objects.equals(options, that.options) &&
                       Objects.equals(externalStandardTypeMappings, that.externalStandardTypeMappings) &&
                       Objects.equals(validElementStatusList, that.validElementStatusList) &&
                       initialStatus == that.initialStatus &&
                       Objects.equals(validEntityDefs, that.validEntityDefs) &&
                       Objects.equals(endDef1, that.endDef1) &&
                       Objects.equals(endDef2, that.endDef2) &&
                       relationshipCategory == that.relationshipCategory;
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), typeDefGUID, typeDefName, applyToVersion, updateToVersion, newVersionName, typeDefStatus,
                            description, descriptionGUID, superType, attributeDefinitions, options, externalStandardTypeMappings,
                            validElementStatusList, initialStatus, validEntityDefs, endDef1, endDef2, relationshipCategory);
    }
}
