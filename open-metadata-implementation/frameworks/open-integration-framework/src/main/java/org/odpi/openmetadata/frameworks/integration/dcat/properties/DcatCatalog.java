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
 * DcatCatalog describes a curated collection of metadata about datasets and data services (dcat:Catalog).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DcatCatalog extends DcatDataset
{
    private String       homepage = null;
    private List<String> themeTaxonomies = null;
    private List<String> datasetIRIs = null;
    private List<String> serviceIRIs = null;
    private List<String> catalogIRIs = null;


    /**
     * Default constructor
     */
    public DcatCatalog()
    {
    }


    /**
     * Return the home page of the catalog (foaf:homepage).
     *
     * @return string URL
     */
    public String getHomepage()
    {
        return homepage;
    }


    /**
     * Set up the home page of the catalog (foaf:homepage).
     *
     * @param homepage string URL
     */
    public void setHomepage(String homepage)
    {
        this.homepage = homepage;
    }


    /**
     * Return the knowledge organization systems used to classify the catalog's resources (dcat:themeTaxonomy).
     *
     * @return list of IRIs
     */
    public List<String> getThemeTaxonomies()
    {
        return themeTaxonomies;
    }


    /**
     * Set up the knowledge organization systems used to classify the catalog's resources (dcat:themeTaxonomy).
     *
     * @param themeTaxonomies list of IRIs
     */
    public void setThemeTaxonomies(List<String> themeTaxonomies)
    {
        this.themeTaxonomies = themeTaxonomies;
    }


    /**
     * Return the IRIs (or document node identifiers) of the datasets listed in the catalog (dcat:dataset).
     *
     * @return list of identifiers
     */
    public List<String> getDatasetIRIs()
    {
        return datasetIRIs;
    }


    /**
     * Set up the IRIs (or document node identifiers) of the datasets listed in the catalog (dcat:dataset).
     *
     * @param datasetIRIs list of identifiers
     */
    public void setDatasetIRIs(List<String> datasetIRIs)
    {
        this.datasetIRIs = datasetIRIs;
    }


    /**
     * Return the IRIs (or document node identifiers) of the data services listed in the catalog (dcat:service).
     *
     * @return list of identifiers
     */
    public List<String> getServiceIRIs()
    {
        return serviceIRIs;
    }


    /**
     * Set up the IRIs (or document node identifiers) of the data services listed in the catalog (dcat:service).
     *
     * @param serviceIRIs list of identifiers
     */
    public void setServiceIRIs(List<String> serviceIRIs)
    {
        this.serviceIRIs = serviceIRIs;
    }


    /**
     * Return the IRIs (or document node identifiers) of the catalogs nested in the catalog (dcat:catalog).
     *
     * @return list of identifiers
     */
    public List<String> getCatalogIRIs()
    {
        return catalogIRIs;
    }


    /**
     * Set up the IRIs (or document node identifiers) of the catalogs nested in the catalog (dcat:catalog).
     *
     * @param catalogIRIs list of identifiers
     */
    public void setCatalogIRIs(List<String> catalogIRIs)
    {
        this.catalogIRIs = catalogIRIs;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DcatCatalog{" +
                       "homepage=" + homepage +
                       ", themeTaxonomies=" + themeTaxonomies +
                       ", datasetIRIs=" + datasetIRIs +
                       ", serviceIRIs=" + serviceIRIs +
                       ", catalogIRIs=" + catalogIRIs +
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
        DcatCatalog that = (DcatCatalog) objectToCompare;
        return Objects.equals(homepage, that.homepage) &&
                       Objects.equals(themeTaxonomies, that.themeTaxonomies) &&
                       Objects.equals(datasetIRIs, that.datasetIRIs) &&
                       Objects.equals(serviceIRIs, that.serviceIRIs) &&
                       Objects.equals(catalogIRIs, that.catalogIRIs);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), homepage, themeTaxonomies, datasetIRIs, serviceIRIs, catalogIRIs);
    }
}
