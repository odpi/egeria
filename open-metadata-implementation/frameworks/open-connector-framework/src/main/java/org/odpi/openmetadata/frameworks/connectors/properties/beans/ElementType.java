/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.ElementOrigin;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ElementType bean extends the ElementType from the properties package with a default constructor and
 * setter methods.  This means it can be used for REST calls and other JSON based functions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ElementType extends org.odpi.openmetadata.frameworks.connectors.properties.ElementType
{
    /**
     * Default constructor
     */
    public ElementType()
    {
        super(null);
        elementOrigin = ElementOrigin.CONFIGURATION;
    }


    /**
     * Copy/clone constructor
     *
     * @param templateType type to clone
     */
    public ElementType(ElementType templateType)
    {
        super(templateType);
    }


    /**
     * Set up the unique identifier for the element's type.
     *
     * @param elementTypeId String identifier
     */
    public void setElementTypeId(String elementTypeId)
    {
        super.elementTypeId = elementTypeId;
    }


    /**
     * Set up the name of this element's type
     *
     * @param elementTypeName String name
     */
    public void setElementTypeName(String elementTypeName)
    {
        super.elementTypeName = elementTypeName;
    }


    /**
     * Set up the version number for this element's type
     *
     * @param elementTypeVersion version number for the element type.
     */
    public void setElementTypeVersion(long elementTypeVersion)
    {
        super.elementTypeVersion = elementTypeVersion;
    }


    /**
     *
     * @param elementTypeDescription set up the description for this element's type
     */
    public void setElementTypeDescription(String elementTypeDescription)
    {
        super.elementTypeDescription = elementTypeDescription;
    }


    /**
     * the URL of the server where the element was retrieved from.  Typically this is
     * a server where the OMAS interfaces are activated.  If no URL is known for the server then null is returned.
     *
     * @param elementAccessServiceURL URL of the server
     */
    public void setElementAccessServiceURL(String elementAccessServiceURL)
    {
        super.elementSourceServer = elementAccessServiceURL;
    }


    /**
     * Set up the details of this element's origin.
     *
     * @param elementOrigin see ElementOrigin enum
     */
    public void setElementOrigin(ElementOrigin elementOrigin)
    {
        super.elementOrigin = elementOrigin;
    }


    /**
     * Set up the OMRS identifier for the metadata collection that is managed by the repository
     * where the element originates (its home repository).
     *
     * @param elementHomeMetadataCollectionId String unique identifier for the home metadata repository
     */
    public void setElementHomeMetadataCollectionId(String elementHomeMetadataCollectionId)
    {
        super.elementHomeMetadataCollectionId = elementHomeMetadataCollectionId;
    }
}
