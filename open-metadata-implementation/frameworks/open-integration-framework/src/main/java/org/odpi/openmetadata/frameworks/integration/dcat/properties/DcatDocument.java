/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DcatDocument holds the resources described in a single DCAT document.  The resources are held in three lists:
 * the catalogs, the datasets (including dataset series) and the data services.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DcatDocument
{
    private List<DcatCatalog>     catalogs = null;
    private List<DcatDataset>     datasets = null;
    private List<DcatDataService> dataServices = null;


    /**
     * Default constructor
     */
    public DcatDocument()
    {
    }


    /**
     * Return the catalogs described in the document.
     *
     * @return list of catalogs
     */
    public List<DcatCatalog> getCatalogs()
    {
        return catalogs;
    }


    /**
     * Set up the catalogs described in the document.
     *
     * @param catalogs list of catalogs
     */
    public void setCatalogs(List<DcatCatalog> catalogs)
    {
        this.catalogs = catalogs;
    }


    /**
     * Return the datasets and dataset series described in the document.
     *
     * @return list of datasets
     */
    public List<DcatDataset> getDatasets()
    {
        return datasets;
    }


    /**
     * Set up the datasets and dataset series described in the document.
     *
     * @param datasets list of datasets
     */
    public void setDatasets(List<DcatDataset> datasets)
    {
        this.datasets = datasets;
    }


    /**
     * Return the data services described in the document.
     *
     * @return list of data services
     */
    public List<DcatDataService> getDataServices()
    {
        return dataServices;
    }


    /**
     * Set up the data services described in the document.
     *
     * @param dataServices list of data services
     */
    public void setDataServices(List<DcatDataService> dataServices)
    {
        this.dataServices = dataServices;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DcatDocument{" +
                       "catalogs=" + catalogs +
                       ", datasets=" + datasets +
                       ", dataServices=" + dataServices +
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
        DcatDocument that = (DcatDocument) objectToCompare;
        return Objects.equals(catalogs, that.catalogs) &&
                       Objects.equals(datasets, that.datasets) &&
                       Objects.equals(dataServices, that.dataServices);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(catalogs, datasets, dataServices);
    }
}
