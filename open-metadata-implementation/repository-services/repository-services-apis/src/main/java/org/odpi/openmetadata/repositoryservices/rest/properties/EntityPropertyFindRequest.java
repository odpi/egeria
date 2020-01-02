/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import java.util.List;
import java.util.Objects;

/**
 * EntityPropertyFindRequests restricts a find request to entities with specific classifications.
 */
public class EntityPropertyFindRequest extends PropertyMatchFindRequest
{
    private static final long    serialVersionUID = 1L;

    private List<String> limitResultsByClassification = null;


    /**
     * Default constructor
     */
    public EntityPropertyFindRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public EntityPropertyFindRequest(EntityPropertyFindRequest template)
    {
        super(template);

        if (template != null)
        {
            this.limitResultsByClassification = template.getLimitResultsByClassification();
        }
    }


    /**
     * Return the list of classifications that must be present in the results.
     *
     * @return list of classification names
     */
    public List<String> getLimitResultsByClassification()
    {
        if (limitResultsByClassification == null)
        {
            return null;
        }
        else if (limitResultsByClassification.isEmpty())
        {
            return null;
        }
        else
        {
            return limitResultsByClassification;
        }
    }


    /**
     * Set up the list of classifications that must be present in the results.
     *
     * @param limitResultsByClassification list of classifications
     */
    public void setLimitResultsByClassification(List<String> limitResultsByClassification)
    {
        this.limitResultsByClassification = limitResultsByClassification;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "EntityPropertyFindRequest{" +
                "limitResultsByClassification=" + limitResultsByClassification +
                ", matchProperties=" + getMatchProperties() +
                ", matchCriteria=" + getMatchCriteria() +
                ", typeGUID='" + getTypeGUID() + '\'' +
                ", sequencingProperty='" + getSequencingProperty() + '\'' +
                ", sequencingOrder=" + getSequencingOrder() +
                ", offset=" + getOffset() +
                ", pageSize=" + getPageSize() +
                ", limitResultsByStatus=" + getLimitResultsByStatus() +
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
        if (!(objectToCompare instanceof EntityPropertyFindRequest))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        EntityPropertyFindRequest
                that = (EntityPropertyFindRequest) objectToCompare;
        return Objects.equals(getLimitResultsByClassification(), that.getLimitResultsByClassification());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), getLimitResultsByClassification());
    }
}
