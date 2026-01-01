/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SequencingOrder;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * QueryOptions carries the date/time for a query along with other common search parameters.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class QueryOptions extends PagingOptions
{
    private String              anchorGUID                    = null;
    private String              anchorDomainName              = null;
    private String              anchorScopeGUID               = null;
    private List<ElementStatus> limitResultsByStatus          = null;
    private SequencingOrder     sequencingOrder               = SequencingOrder.LAST_UPDATE_RECENT;
    private String              sequencingProperty            = null;
    private List<String>        skipClassifiedElements        = null;
    private List<String>        includeOnlyClassifiedElements = null;
    private List<String>        governanceZoneFilter          = null;


    /**
     * Default constructor
     */
    public QueryOptions()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public QueryOptions(QueryOptions template)
    {
        super(template);

        if (template != null)
        {
            anchorGUID                    = template.getAnchorGUID();
            anchorScopeGUID               = template.getAnchorScopeGUID();
            anchorDomainName              = template.getAnchorDomainName();
            limitResultsByStatus          = template.getLimitResultsByStatus();
            sequencingOrder               = template.getSequencingOrder();
            sequencingProperty            = template.getSequencingProperty();
            skipClassifiedElements        = template.getSkipClassifiedElements();
            includeOnlyClassifiedElements = template.getIncludeOnlyClassifiedElements();
            governanceZoneFilter          = template.getGovernanceZoneFilter();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public QueryOptions(GetOptions template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public QueryOptions(BasicOptions template)
    {
        super(template);
    }


    /**
     * Return the unique identifier of the anchor to limit the search results to.
     *
     * @return string guid
     */
    public String getAnchorGUID()
    {
        return anchorGUID;
    }


    /**
     * Set up the unique identifier of the anchor to limit the search results to.
     *
     * @param anchorGUID string
     */
    public void setAnchorGUID(String anchorGUID)
    {
        this.anchorGUID = anchorGUID;
    }


    /**
     * Return the unique identifier of the anchor scope to link the search results to.
     *
     * @return string
     */
    public String getAnchorScopeGUID()
    {
        return anchorScopeGUID;
    }


    /**
     * Set up the unique identifier of the anchor scope to link the search results to.
     *
     * @param anchorScopeGUID string
     */
    public void setAnchorScopeGUID(String anchorScopeGUID)
    {
        this.anchorScopeGUID = anchorScopeGUID;
    }


    /**
     * Return the type name of the anchor's domain.
     *
     * @return string
     */
    public String getAnchorDomainName()
    {
        return anchorDomainName;
    }


    /**
     * Set up the type name of the anchor's domain.
     *
     * @param anchorDomainName string
     */
    public void setAnchorDomainName(String anchorDomainName)
    {
        this.anchorDomainName = anchorDomainName;
    }


    /**
     * Return the optional list of statuses that should be used to restrict the elements returned by their status.
     * By default, the elements in all non-DELETED statuses are returned.  However, it is possible
     * to specify a list of statuses (eg ACTIVE, DELETED) to restrict the results to.  Null means all
     * status values except DELETED.
     *
     * @return list of statuses or null
     */
    public List<ElementStatus> getLimitResultsByStatus()
    {
        return limitResultsByStatus;
    }


    /**
     * Set up the optional list of statuses that should be used to restrict the elements returned by their status.
     * By default, the elements in all non-DELETED statuses are returned.  However, it is possible
     * to specify a list of statuses (eg ACTIVE, DELETED) to restrict the results to.  Null means all
     * status values except DELETED.
     *
     * @param limitResultsByStatus list of statuses or null
     */
    public void setLimitResultsByStatus(List<ElementStatus> limitResultsByStatus)
    {
        this.limitResultsByStatus = limitResultsByStatus;
    }


    /**
     * Return the property that should be used to order the results.
     *
     * @return property name
     */
    public String getSequencingProperty()
    {
        return sequencingProperty;
    }


    /**
     * Set up the property that should be used to order the results.
     *
     * @param sequencingProperty property name
     */
    public void setSequencingProperty(String sequencingProperty)
    {
        this.sequencingProperty = sequencingProperty;
    }


    /**
     * Return the order that results should be returned in.
     *
     * @return enum
     */
    public SequencingOrder getSequencingOrder()
    {
        return sequencingOrder;
    }


    /**
     * Set up the order that results should be returned in.
     *
     * @param sequencingOrder enum
     */
    public void setSequencingOrder(SequencingOrder sequencingOrder)
    {
        this.sequencingOrder = sequencingOrder;
    }


    /**
     * Return the list of governance zones to restrict the elements (if any) returned to members of these
     * zones.  If the value is null (the default) then all matching elements are returned, subject to
     * the usual security restrictions.  Be aware that governance zones are only set on anchor elements.
     * Any element retrieved that is anchored to another element will not be returned if this filter is set.
     *
     * @return list of governance zone names
     */
    public List<String> getGovernanceZoneFilter()
    {
        return governanceZoneFilter;
    }



    /**
     * Return the list of classification names that should not be found on any returned elements. The default
     * is null which means that the classifications of an element do not add additional filters for the results.
     *
     * @return list of classification names or null.
     */
    public List<String> getSkipClassifiedElements()
    {
        return skipClassifiedElements;
    }


    /**
     * Set up the list of classification names that should not be found on any returned elements. The default
     * is null which means that the classifications of an element do not add additional filters for the results.
     *
     * @param skipClassifiedElements list of classification names or null.
     */
    public void setSkipClassifiedElements(List<String> skipClassifiedElements)
    {
        this.skipClassifiedElements = skipClassifiedElements;
    }


    /**
     * Return the list of classification names that must be found on any returned elements. The default
     * is null which means that the classifications of an element do not add additional filters for the results.
     *
     * @return list of classification names or null.
     */
    public List<String> getIncludeOnlyClassifiedElements()
    {
        return includeOnlyClassifiedElements;
    }


    /**
     * Set up the list of classification names that must be found on any returned elements. The default
     * is null which means that the classifications of an element do not add additional filters for the results.
     *
     * @param includeOnlyClassifiedElements list of classification names or null.
     */
    public void setIncludeOnlyClassifiedElements(List<String> includeOnlyClassifiedElements)
    {
        this.includeOnlyClassifiedElements = includeOnlyClassifiedElements;
    }


    /**
     * Set up the list of governance zones to restrict the elements (if any) returned to members of these
     * zones.  If the value is null (the default) then all matching elements are returned, subject to
     * the usual security restrictions.  Be aware that governance zones are only set on anchor elements.
     * Any element retrieved that is anchored to another element will not be returned if this filter is set.
     *
     * @param governanceZoneFilter list of governance zone names
     */
    public void setGovernanceZoneFilter(List<String> governanceZoneFilter)
    {
        this.governanceZoneFilter = governanceZoneFilter;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "QueryOptions{" +
                "anchorGUID='" + anchorGUID + '\'' +
                ", anchorDomainName='" + anchorDomainName + '\'' +
                ", anchorScopeGUID='" + anchorScopeGUID + '\'' +
                ", limitResultsByStatus=" + limitResultsByStatus +
                ", sequencingOrder=" + sequencingOrder +
                ", sequencingProperty='" + sequencingProperty + '\'' +
                ", skipClassifiedElements=" + skipClassifiedElements +
                ", includeOnlyClassifiedElements=" + includeOnlyClassifiedElements +
                ", governanceZoneFilter=" + governanceZoneFilter +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        QueryOptions that = (QueryOptions) objectToCompare;
        return Objects.equals(anchorGUID, that.anchorGUID) &&
                Objects.equals(anchorDomainName, that.anchorDomainName) &&
                Objects.equals(anchorScopeGUID, that.anchorScopeGUID) &&
                Objects.equals(limitResultsByStatus, that.limitResultsByStatus) &&
                sequencingOrder == that.sequencingOrder &&
                Objects.equals(sequencingProperty, that.sequencingProperty) &&
                Objects.equals(skipClassifiedElements, that.skipClassifiedElements) &&
                Objects.equals(includeOnlyClassifiedElements, that.includeOnlyClassifiedElements) &&
                Objects.equals(governanceZoneFilter, that.governanceZoneFilter);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), anchorGUID, anchorDomainName, anchorScopeGUID, limitResultsByStatus, sequencingOrder, sequencingProperty, skipClassifiedElements, includeOnlyClassifiedElements, governanceZoneFilter);
    }
}
