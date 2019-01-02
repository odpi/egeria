/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;


import com.fasterxml.jackson.annotation.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReferenceableHeader provides the common properties found in objects that inherit from Referenceable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Asset.class, name = "Asset"),
        @JsonSubTypes.Type(value = Collection.class, name = "Collection"),
        @JsonSubTypes.Type(value = ExternalReference.class, name = "ExternalReference")
})
public abstract class ReferenceableHeader extends CommonHeader
{
    private String               qualifiedName        = null;


    /**
     * Default constructor
     */
    public ReferenceableHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor
     */
    public ReferenceableHeader(ReferenceableHeader   template)
    {
        super(template);

        if (template != null)
        {
            this.qualifiedName = template.getQualifiedName();
        }
    }


    /**
     * Return the unique name for this asset.
     *
     * @return string name
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up the unique name for this asset.
     *
     * @param qualifiedName string name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */



    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */

}
