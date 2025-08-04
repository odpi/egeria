/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;

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
    private List<ElementStatus> limitResultsByStatus          = null;
    private SequencingOrder     sequencingOrder               = SequencingOrder.LAST_UPDATE_RECENT;
    private String              sequencingProperty            = null;


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
            anchorGUID           = template.getAnchorGUID();
            limitResultsByStatus = template.getLimitResultsByStatus();
            sequencingOrder      = template.getSequencingOrder();
            sequencingProperty   = template.getSequencingProperty();
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
     * Return the unique identifier of the glossary scope.
     *
     * @return string guid
     */
    public String getAnchorGUID()
    {
        return anchorGUID;
    }


    /**
     * Set up the unique identifier of the glossary scope.
     *
     * @param anchorGUID string
     */
    public void setAnchorGUID(String anchorGUID)
    {
        this.anchorGUID = anchorGUID;
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "QueryOptions{" +
                "anchorGUID='" + anchorGUID + '\'' +
                ", limitResultsByStatus=" + limitResultsByStatus +
                ", sequencingOrder=" + sequencingOrder +
                ", sequencingProperty='" + sequencingProperty + '\'' +
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
                Objects.equals(limitResultsByStatus, that.limitResultsByStatus) &&
                sequencingOrder == that.sequencingOrder &&
                Objects.equals(sequencingProperty, that.sequencingProperty);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), anchorGUID, limitResultsByStatus, sequencingOrder, sequencingProperty);
    }
}
