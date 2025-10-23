/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DocumentStoreProperties describes a store of documents.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MediaCollectionProperties extends DataSetProperties
{
    private Map<String, String> embeddedMetadata = null;


    /**
     * Default constructor
     */
    public MediaCollectionProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.MEDIA_COLLECTION.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MediaCollectionProperties(MediaCollectionProperties template)
    {
        super(template);

        if (template != null)
        {
            embeddedMetadata = template.getEmbeddedMetadata();
        }
    }


    /**
     * Return the embedded metadata discovered in the collection.
     *
     * @return map
     */
    public Map<String, String> getEmbeddedMetadata()
    {
        return embeddedMetadata;
    }


    /**
     * Set up the embedded metadata discovered in the collection.
     *
     * @param embeddedMetadata map
     */
    public void setEmbeddedMetadata(Map<String, String> embeddedMetadata)
    {
        this.embeddedMetadata = embeddedMetadata;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "MediaCollectionProperties{" +
                "embeddedMetadata=" + embeddedMetadata +
                "} " + super.toString();
    }



    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        MediaCollectionProperties that = (MediaCollectionProperties) objectToCompare;
        return Objects.equals(embeddedMetadata, that.embeddedMetadata) ;
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), embeddedMetadata);
    }
}
