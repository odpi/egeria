/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The GovernanceMetricImplementation defines the query and data set that supports the measurements
 * for a GovernanceMetric.  The list of connections to the data set are also provided to enable
 * the client to switch to the Asset Consumer OMAS and query the values in the data set.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceMetricImplementation implements Serializable
{
    private static final long          serialVersionUID = 1L;

    private String       implementationGUID        = null;
    private String       implementationDescription = null;
    private String       query                     = null;
    private String       dataSetGUID               = null;
    private String       dataSetType               = null;
    private String       dataSetQualifiedName      = null;
    private String       dataSetDisplayName        = null;
    private List<String> dataSetConnectionGUIDs    = null;


    /**
     * Default constructor
     */
    public GovernanceMetricImplementation()
    {
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceMetricImplementation(GovernanceMetricImplementation  template)
    {
        if (template != null)
        {
            this.implementationGUID = template.getImplementationGUID();
            this.implementationDescription = template.getImplementationDescription();
            this.query = template.getQuery();
            this.dataSetGUID = template.getDataSetGUID();
            this.dataSetType = template.getDataSetType();
            this.dataSetQualifiedName = template.getDataSetQualifiedName();
            this.dataSetDisplayName = template.getDataSetDisplayName();
            this.dataSetConnectionGUIDs = template.getDataSetConnectionGUIDs();
        }
    }


    /**
     * Return the unique identifier of the relationship that links the governance metric to the measurement data set.
     *
     * @return string guid
     */
    public String getImplementationGUID()
    {
        return implementationGUID;
    }


    /**
     * Set up the unique identifier of the relationship that links the governance metric to the measurement data set.
     *
     * @param implementationGUID string guid
     */
    public void setImplementationGUID(String implementationGUID)
    {
        this.implementationGUID = implementationGUID;
    }


    /**
     * Return the description of how the data set supports the governance metric.  This is stored in the
     * GovernanceMeasurementsDataSet classification on the DataSet.
     *
     * @return string description
     */
    public String getImplementationDescription()
    {
        return implementationDescription;
    }


    /**
     * Set up the description of how the data set supports the governance metric.  This is stored in the
     * GovernanceMeasurementsDataSet classification on the DataSet.
     *
     * @param implementationDescription string description
     */
    public void setImplementationDescription(String implementationDescription)
    {
        this.implementationDescription = implementationDescription;
    }


    /**
     * Return the query to use on the data set.  If this is null then the contents of the data set should be
     * used directly.
     *
     * @return query string
     */
    public String getQuery()
    {
        return query;
    }


    /**
     * Set up the query to use on the data set.  If this is null then the contents of the data set should be
     * used directly.
     *
     * @param query string
     */
    public void setQuery(String query)
    {
        this.query = query;
    }


    /**
     * Return the unique identifier of the data set where the measurements are stored.
     *
     * @return string guid
     */
    public String getDataSetGUID()
    {
        return dataSetGUID;
    }


    /**
     * Set up the unique identifier of the data set where the measurements are stored.
     *
     * @param dataSetGUID string guid
     */
    public void setDataSetGUID(String dataSetGUID)
    {
        this.dataSetGUID = dataSetGUID;
    }


    /**
     * Return the type name of the data set.
     *
     * @return string name
     */
    public String getDataSetType()
    {
        return dataSetType;
    }


    /**
     * Set up the type name for the data set.
     *
     * @param dataSetType string name
     */
    public void setDataSetType(String dataSetType)
    {
        this.dataSetType = dataSetType;
    }


    /**
     * Return the unique name for the data set.
     *
     * @return string name
     */
    public String getDataSetQualifiedName()
    {
        return dataSetQualifiedName;
    }


    /**
     * Set up the unique name for the data set.
     *
     * @param dataSetQualifiedName string name
     */
    public void setDataSetQualifiedName(String dataSetQualifiedName)
    {
        this.dataSetQualifiedName = dataSetQualifiedName;
    }


    /**
     * Return the display name for the data set.
     *
     * @return string name
     */
    public String getDataSetDisplayName()
    {
        return dataSetDisplayName;
    }


    /**
     * Set up the display name for the data set.
     *
     * @param dataSetDisplayName string name
     */
    public void setDataSetDisplayName(String dataSetDisplayName)
    {
        this.dataSetDisplayName = dataSetDisplayName;
    }


    /**
     * Return the list of connection identifiers associated with the data set.  These GUIDs can be used
     * with the Asset Consumer OMAS to retrieve the measurement values.
     *
     * @return list of connection guids
     */
    public List<String> getDataSetConnectionGUIDs()
    {
        return dataSetConnectionGUIDs;
    }


    /**
     * Set up the ist of connection identifiers associated with the data set.  These GUIDs can be used
     * with the Asset Consumer OMAS to retrieve the measurement values.
     *
     * @param dataSetConnectionGUIDs list of connection guids
     */
    public void setDataSetConnectionGUIDs(List<String> dataSetConnectionGUIDs)
    {
        this.dataSetConnectionGUIDs = dataSetConnectionGUIDs;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernanceMetricImplementation{" +
                "implementationGUID='" + implementationGUID + '\'' +
                ", implementationDescription='" + implementationDescription + '\'' +
                ", query='" + query + '\'' +
                ", dataSetGUID='" + dataSetGUID + '\'' +
                ", dataSetType='" + dataSetType + '\'' +
                ", dataSetQualifiedName='" + dataSetQualifiedName + '\'' +
                ", dataSetDisplayName='" + dataSetDisplayName + '\'' +
                ", dataSetConnectionGUIDs=" + dataSetConnectionGUIDs +
                '}';
    }


    /**
     * Test the properties of the DataStrategy to determine if the supplied object is equal to this one.
     *
     * @param objectToCompare object
     * @return boolean evaluation
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof GovernanceMetricImplementation))
        {
            return false;
        }
        GovernanceMetricImplementation that = (GovernanceMetricImplementation) objectToCompare;
        return Objects.equals(getImplementationGUID(), that.getImplementationGUID()) &&
                Objects.equals(getImplementationDescription(), that.getImplementationDescription()) &&
                Objects.equals(getQuery(), that.getQuery()) &&
                Objects.equals(getDataSetGUID(), that.getDataSetGUID()) &&
                Objects.equals(getDataSetType(), that.getDataSetType()) &&
                Objects.equals(getDataSetQualifiedName(), that.getDataSetQualifiedName()) &&
                Objects.equals(getDataSetDisplayName(), that.getDataSetDisplayName()) &&
                Objects.equals(getDataSetConnectionGUIDs(), that.getDataSetConnectionGUIDs());
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getImplementationGUID());
    }
}
