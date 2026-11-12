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
 * DcatDataset describes a collection of data that is published or curated by a single agent (dcat:Dataset).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DcatDataset extends DcatResource
{
    private List<DcatDistribution> distributions = null;
    private String                 accrualPeriodicity = null;
    private List<String>           spatial = null;
    private List<DcatPeriodOfTime> temporal = null;
    private String                 spatialResolutionInMeters = null;
    private String                 temporalResolution = null;
    private List<String>           inSeries = null;


    /**
     * Default constructor
     */
    public DcatDataset()
    {
    }


    /**
     * Return the available representations of the dataset (dcat:distribution).
     *
     * @return list of distributions
     */
    public List<DcatDistribution> getDistributions()
    {
        return distributions;
    }


    /**
     * Set up the available representations of the dataset (dcat:distribution).
     *
     * @param distributions list of distributions
     */
    public void setDistributions(List<DcatDistribution> distributions)
    {
        this.distributions = distributions;
    }


    /**
     * Return the frequency at which the dataset is updated (dct:accrualPeriodicity).
     *
     * @return string
     */
    public String getAccrualPeriodicity()
    {
        return accrualPeriodicity;
    }


    /**
     * Set up the frequency at which the dataset is updated (dct:accrualPeriodicity).
     *
     * @param accrualPeriodicity string
     */
    public void setAccrualPeriodicity(String accrualPeriodicity)
    {
        this.accrualPeriodicity = accrualPeriodicity;
    }


    /**
     * Return the geographical areas covered by the dataset (dct:spatial).
     *
     * @return list of locations
     */
    public List<String> getSpatial()
    {
        return spatial;
    }


    /**
     * Set up the geographical areas covered by the dataset (dct:spatial).
     *
     * @param spatial list of locations
     */
    public void setSpatial(List<String> spatial)
    {
        this.spatial = spatial;
    }


    /**
     * Return the temporal periods covered by the dataset (dct:temporal).
     *
     * @return list of periods
     */
    public List<DcatPeriodOfTime> getTemporal()
    {
        return temporal;
    }


    /**
     * Set up the temporal periods covered by the dataset (dct:temporal).
     *
     * @param temporal list of periods
     */
    public void setTemporal(List<DcatPeriodOfTime> temporal)
    {
        this.temporal = temporal;
    }


    /**
     * Return the minimum spatial separation resolvable in the dataset (dcat:spatialResolutionInMeters).
     *
     * @return string number
     */
    public String getSpatialResolutionInMeters()
    {
        return spatialResolutionInMeters;
    }


    /**
     * Set up the minimum spatial separation resolvable in the dataset (dcat:spatialResolutionInMeters).
     *
     * @param spatialResolutionInMeters string number
     */
    public void setSpatialResolutionInMeters(String spatialResolutionInMeters)
    {
        this.spatialResolutionInMeters = spatialResolutionInMeters;
    }


    /**
     * Return the minimum time period resolvable in the dataset (dcat:temporalResolution).
     *
     * @return string duration
     */
    public String getTemporalResolution()
    {
        return temporalResolution;
    }


    /**
     * Set up the minimum time period resolvable in the dataset (dcat:temporalResolution).
     *
     * @param temporalResolution string duration
     */
    public void setTemporalResolution(String temporalResolution)
    {
        this.temporalResolution = temporalResolution;
    }


    /**
     * Return the IRIs of the dataset series that this dataset is part of (dcat:inSeries).
     *
     * @return list of IRIs
     */
    public List<String> getInSeries()
    {
        return inSeries;
    }


    /**
     * Set up the IRIs of the dataset series that this dataset is part of (dcat:inSeries).
     *
     * @param inSeries list of IRIs
     */
    public void setInSeries(List<String> inSeries)
    {
        this.inSeries = inSeries;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DcatDataset{" +
                       "distributions=" + distributions +
                       ", accrualPeriodicity=" + accrualPeriodicity +
                       ", spatial=" + spatial +
                       ", temporal=" + temporal +
                       ", spatialResolutionInMeters=" + spatialResolutionInMeters +
                       ", temporalResolution=" + temporalResolution +
                       ", inSeries=" + inSeries +
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
        DcatDataset that = (DcatDataset) objectToCompare;
        return Objects.equals(distributions, that.distributions) &&
                       Objects.equals(accrualPeriodicity, that.accrualPeriodicity) &&
                       Objects.equals(spatial, that.spatial) &&
                       Objects.equals(temporal, that.temporal) &&
                       Objects.equals(spatialResolutionInMeters, that.spatialResolutionInMeters) &&
                       Objects.equals(temporalResolution, that.temporalResolution) &&
                       Objects.equals(inSeries, that.inSeries);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), distributions, accrualPeriodicity, spatial, temporal, spatialResolutionInMeters, temporalResolution, inSeries);
    }
}
