/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ClassificationCondition class provides support for searching against a single classification, either based only
 * on its name or also combined with a set of property-based conditions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ClassificationCondition implements Serializable
{

    private static final long    serialVersionUID = 1L;

    private String name;
    private SearchProperties matchProperties;

    /**
     * Typical constructor
     */
    public ClassificationCondition()
    {
        super();
    }

    /**
     * Copy/clone constructor.
     *
     * @param templateProperties template object to copy.
     */
    public ClassificationCondition(ClassificationCondition templateProperties)
    {
        /*
         * An empty properties object is created in the private variable declaration so nothing to do.
         */
        if (templateProperties != null)
        {
            this.name = templateProperties.getName();
            this.matchProperties = new SearchProperties(templateProperties.getMatchProperties());
        }
    }

    /**
     * Retrieve the name of the classification.
     *
     * @return String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name of the classification.
     *
     * @param name of the classification
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Retrieve the (optional) properties-based conditions for the classification.
     *
     * @return SearchProperties
     */
    public SearchProperties getMatchProperties()
    {
        return matchProperties;
    }

    /**
     * Set the (optional) properties-based conditions for the classification.
     *
     * @param matchProperties for the classification
     */
    public void setMatchProperties(SearchProperties matchProperties)
    {
        this.matchProperties = matchProperties;
    }

}
