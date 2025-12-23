/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AuthoredReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * DataAssetProperties is a java bean used to create assets associated with data.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DataStoreProperties.class, name = "DataStoreProperties"),
        @JsonSubTypes.Type(value = DataSetProperties.class, name = "DataSetProperties"),
        @JsonSubTypes.Type(value = DataFeedProperties.class, name = "DataFeedProperties"),
        @JsonSubTypes.Type(value = ReportTypeProperties.class, name = "ReportTypeProperties"),
})
public class DataAssetProperties extends AssetProperties
{
    private List<String>  authors       = null;
    private ContentStatus contentStatus = null;
    private String        userDefinedContentStatus = null;


    /**
     * Default constructor
     */
    public DataAssetProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_ASSET.typeName;
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public DataAssetProperties(DataAssetProperties template)
    {
        super(template);

        if (template != null)
        {
            authors                  = template.getAuthors();
            contentStatus            = template.getContentStatus();
            userDefinedContentStatus = template.getUserDefinedContentStatus();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataAssetProperties(AssetProperties template)
    {
        super(template);
    }


    /**
     * Return the list of authors for this element.
     *
     * @return list
     */
    public List<String> getAuthors()
    {
        return authors;
    }

    /**
     * Set up the list of authors for this element.
     *
     * @param authors list
     */
    public void setAuthors(List<String> authors)
    {
        this.authors = authors;
    }


    /**
     * Return the status of the content.
     *
     * @return status enum
     */
    public ContentStatus getContentStatus()
    {
        return contentStatus;
    }


    /**
     * Set up the status of the content.
     *
     * @param contentStatus status enum
     */
    public void setContentStatus(ContentStatus contentStatus)
    {
        this.contentStatus = contentStatus;
    }


    /**
     * Return additionally defined content statuses.
     *
     * @return string
     */
    public String getUserDefinedContentStatus()
    {
        return userDefinedContentStatus;
    }


    /**
     * Set up additionally defined content statuses.
     *
     * @param userDefinedContentStatus string
     */
    public void setUserDefinedContentStatus(String userDefinedContentStatus)
    {
        this.userDefinedContentStatus = userDefinedContentStatus;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataAssetProperties{" +
                "authors=" + authors +
                ", contentStatus=" + contentStatus +
                ", userDefinedContentStatus='" + userDefinedContentStatus + '\'' +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        DataAssetProperties that = (DataAssetProperties) objectToCompare;
        return  authors == that.authors &&
                contentStatus == that.contentStatus &&
                Objects.equals(userDefinedContentStatus, that.userDefinedContentStatus);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), authors, contentStatus, userDefinedContentStatus);
    }
}
