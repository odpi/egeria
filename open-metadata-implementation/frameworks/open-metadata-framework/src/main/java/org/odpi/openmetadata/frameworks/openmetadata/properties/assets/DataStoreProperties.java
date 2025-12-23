/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DatabaseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.GraphStoreProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DocumentStoreProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.FileFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.metadatarepositories.CohortRegistryStoreProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.metadatarepositories.MetadataRepositoryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataStoreProperties provides the JavaBean for describing a data store.  This is a physical store of data.
 * It is saved in the catalog as an asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CohortRegistryStoreProperties.class, name = "CohortRegistryStoreProperties"),
        @JsonSubTypes.Type(value = DataFileProperties.class, name = "DataFileProperties"),
        @JsonSubTypes.Type(value = DatabaseProperties.class, name = "DatabaseProperties"),
        @JsonSubTypes.Type(value = DocumentStoreProperties.class, name = "DocumentStoreProperties"),
        @JsonSubTypes.Type(value = GraphStoreProperties.class, name = "GraphStoreProperties"),
        @JsonSubTypes.Type(value = MetadataRepositoryProperties.class, name = "MetadataRepositoryProperties"),
        @JsonSubTypes.Type(value = FileFolderProperties.class, name = "FileFolderProperties"),

              })
public class DataStoreProperties extends DataAssetProperties
{
    private String pathName      = null;
    private Date storeCreateTime = null;
    private Date storeUpdateTime = null;



    /**
     * Default constructor
     */
    public DataStoreProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_STORE.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DataStoreProperties(DataStoreProperties template)
    {
        super(template);

        if (template != null)
        {
            pathName        = template.getPathName();
            storeCreateTime = template.getStoreCreateTime();
            storeUpdateTime = template.getStoreUpdateTime();

        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DataStoreProperties(AssetProperties template)
    {
        super(template);
    }


    /**
     * Return the fully qualified physical location of the data store.  This should be suitable for the
     * network address of the Endpoint.
     *
     * @return string name
     */
    public String getPathName()
    {
        return pathName;
    }


    /**
     * Set up the fully qualified physical location of the data store.  This should be suitable for the
     * network address of the Endpoint.
     *
     * @param pathName string name
     */
    public void setPathName(String pathName)
    {
        this.pathName = pathName;
    }


    /**
     * Return the time that the data store was created.
     *
     * @return date
     */
    public Date getStoreCreateTime()
    {
        return storeCreateTime;
    }


    /**
     * Set up the time that the data store was created.
     *
     * @param storeCreateTime date
     */
    public void setStoreCreateTime(Date storeCreateTime)
    {
        this.storeCreateTime = storeCreateTime;
    }


    /**
     * Return the last known time the data store was modified.
     *
     * @return date
     */
    public Date getStoreUpdateTime()
    {
        return storeUpdateTime;
    }


    /**
     * Setup the last known time the data store was modified.
     *
     * @param storeUpdateTime date
     */
    public void setStoreUpdateTime(Date storeUpdateTime)
    {
        this.storeUpdateTime = storeUpdateTime;
    }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataStoreProperties{" +
                "pathName='" + pathName + '\'' +
                ", createTime=" + storeCreateTime +
                ", modifiedTime=" + storeUpdateTime +
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
        DataStoreProperties that = (DataStoreProperties) objectToCompare;
        return Objects.equals(pathName, that.pathName) &&
                       Objects.equals(storeCreateTime, that.storeCreateTime) &&
                       Objects.equals(storeUpdateTime, that.storeUpdateTime);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), pathName, storeCreateTime, storeUpdateTime);
    }
}
