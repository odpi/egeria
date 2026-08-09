/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SavedQueryProperties describes a data set that is defined by a query that is run each time the data set's members are needed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SavedQueryProperties extends DataSetProperties
{
    private String queryURL         = null;
    private String queryRequestBody = null;


    /**
     * Default constructor
     */
    public SavedQueryProperties()
    {
        super();
        super.typeName = OpenMetadataType.SAVED_QUERY.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public SavedQueryProperties(SavedQueryProperties template)
    {
        super(template);

        if (template != null)
        {
            queryURL         = template.getQueryURL();
            queryRequestBody = template.getQueryRequestBody();
        }
    }


    /**
     * Return the REST URL used to issue the query.
     *
     * @return string url
     */
    public String getQueryURL()
    {
        return queryURL;
    }


    /**
     * Set up the REST URL used to issue the query.
     *
     * @param queryURL string url
     */
    public void setQueryURL(String queryURL)
    {
        this.queryURL = queryURL;
    }


    /**
     * Return the REST request body used to issue the query.
     *
     * @return string text (typically JSON)
     */
    public String getQueryRequestBody()
    {
        return queryRequestBody;
    }


    /**
     * Set up the REST request body used to issue the query.
     *
     * @param queryRequestBody string text (typically JSON)
     */
    public void setQueryRequestBody(String queryRequestBody)
    {
        this.queryRequestBody = queryRequestBody;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SavedQueryProperties{" +
                "queryURL='" + queryURL + '\'' +
                ", queryRequestBody='" + queryRequestBody + '\'' +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        SavedQueryProperties that = (SavedQueryProperties) objectToCompare;
        return Objects.equals(queryURL, that.queryURL) &&
                       Objects.equals(queryRequestBody, that.queryRequestBody);
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), queryURL, queryRequestBody);
    }
}
