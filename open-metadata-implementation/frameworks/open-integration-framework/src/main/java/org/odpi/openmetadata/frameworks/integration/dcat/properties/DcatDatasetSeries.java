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
 * DcatDatasetSeries describes a collection of datasets that are published separately but share characteristics
 * that group them (dcat:DatasetSeries).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DcatDatasetSeries extends DcatDataset
{
    private List<String> seriesMembers = null;


    /**
     * Default constructor
     */
    public DcatDatasetSeries()
    {
    }


    /**
     * Return the IRIs of the datasets in the series (dcat:seriesMember, or derived from dcat:inSeries).
     *
     * @return list of IRIs
     */
    public List<String> getSeriesMembers()
    {
        return seriesMembers;
    }


    /**
     * Set up the IRIs of the datasets in the series (dcat:seriesMember, or derived from dcat:inSeries).
     *
     * @param seriesMembers list of IRIs
     */
    public void setSeriesMembers(List<String> seriesMembers)
    {
        this.seriesMembers = seriesMembers;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DcatDatasetSeries{" +
                       "seriesMembers=" + seriesMembers +
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
        DcatDatasetSeries that = (DcatDatasetSeries) objectToCompare;
        return Objects.equals(seriesMembers, that.seriesMembers);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), seriesMembers);
    }
}
