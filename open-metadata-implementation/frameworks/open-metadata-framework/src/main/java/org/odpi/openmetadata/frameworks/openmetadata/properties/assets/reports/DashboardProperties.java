/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DashboardProperties describes a collection of reports, along with the layout information needed to display them together.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DashboardProperties extends DataSetProperties
{
    private List<String> aliases             = null;
    private Long         refreshTimeInterval = null;
    private String       layoutStyle         = null;


    /**
     * Default constructor
     */
    public DashboardProperties()
    {
        super();
        super.typeName = OpenMetadataType.DASHBOARD.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DashboardProperties(DashboardProperties template)
    {
        super(template);

        if (template != null)
        {
            aliases             = template.getAliases();
            refreshTimeInterval = template.getRefreshTimeInterval();
            layoutStyle         = template.getLayoutStyle();
        }
    }


    /**
     * Return the alternative names for the dashboard.
     *
     * @return list
     */
    public List<String> getAliases()
    {
        return aliases;
    }


    /**
     * Set up the alternative names for the dashboard.
     *
     * @param aliases list
     */
    public void setAliases(List<String> aliases)
    {
        this.aliases = aliases;
    }


    /**
     * Return how frequently the dashboard should be refreshed - in minutes.
     *
     * @return long
     */
    public Long getRefreshTimeInterval()
    {
        return refreshTimeInterval;
    }


    /**
     * Set up how frequently the dashboard should be refreshed - in minutes.
     *
     * @param refreshTimeInterval long
     */
    public void setRefreshTimeInterval(Long refreshTimeInterval)
    {
        this.refreshTimeInterval = refreshTimeInterval;
    }


    /**
     * Return the style of layout used to arrange the reports in the dashboard.
     *
     * @return string
     */
    public String getLayoutStyle()
    {
        return layoutStyle;
    }


    /**
     * Set up the style of layout used to arrange the reports in the dashboard.
     *
     * @param layoutStyle string
     */
    public void setLayoutStyle(String layoutStyle)
    {
        this.layoutStyle = layoutStyle;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DashboardProperties{" +
                "aliases=" + aliases +
                ", refreshTimeInterval=" + refreshTimeInterval +
                ", layoutStyle='" + layoutStyle + '\'' +
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
        DashboardProperties that = (DashboardProperties) objectToCompare;
        return Objects.equals(aliases, that.aliases) &&
                Objects.equals(refreshTimeInterval, that.refreshTimeInterval) &&
                Objects.equals(layoutStyle, that.layoutStyle);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), aliases, refreshTimeInterval, layoutStyle);
    }
}
