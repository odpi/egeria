/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularDataSetReport;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TabularDataSetReportResponse is the response structure used on REST API calls that returns a
 * TabularDataSetReport object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TabularDataSetReportResponse extends FFDCResponseBase
{
    private TabularDataSetReport tabularDataSetReport = null;

    /**
     * Default constructor
     */
    public TabularDataSetReportResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TabularDataSetReportResponse(TabularDataSetReportResponse template)
    {
        super(template);

        if (template != null)
        {
            this.tabularDataSetReport = template.getTabularDataSetReport();
        }
    }


    /**
     * Return the component description information that the connector uses to register with the audit log.
     *
     * @return component description structure
     */
    public TabularDataSetReport getTabularDataSetReport()
    {
        return tabularDataSetReport;
    }


    /**
     * Set up the component description information that the connector uses to register with the audit log.
     *
     * @param tabularDataSetReport component description structure
     */
    public void setTabularDataSetReport(TabularDataSetReport tabularDataSetReport)
    {
        this.tabularDataSetReport = tabularDataSetReport;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TabularDataSetReportResponse{" +
                "tabularDataSetReport=" + tabularDataSetReport +
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
        TabularDataSetReportResponse that = (TabularDataSetReportResponse) objectToCompare;
        return Objects.equals(tabularDataSetReport, that.tabularDataSetReport);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), tabularDataSetReport);
    }
}
