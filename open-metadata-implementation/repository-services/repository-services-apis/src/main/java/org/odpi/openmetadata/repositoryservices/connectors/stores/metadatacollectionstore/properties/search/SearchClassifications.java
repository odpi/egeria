/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The SearchClassifications class provides support for searching against one or more classifications using a variety of
 * comparison mechanisms.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SearchClassifications implements Serializable
{

    private static final long    serialVersionUID = 1L;

    private List<ClassificationCondition> conditions;
    private MatchCriteria matchCriteria;

    /**
     * Typical constructor
     */
    public SearchClassifications()
    {
        super();
        // Setup defaults
        matchCriteria = MatchCriteria.ALL;
    }

    /**
     * Copy/clone constructor.
     *
     * @param templateProperties template object to copy.
     */
    public SearchClassifications(SearchClassifications templateProperties)
    {
        /*
         * An empty properties object is created in the private variable declaration so nothing to do.
         */
        if (templateProperties != null)
        {
            this.matchCriteria = templateProperties.getMatchCriteria();
            this.conditions = new ArrayList<>();
            this.conditions.addAll(templateProperties.getConditions());
        }
    }

    /**
     * Retrieve the classification conditions against which to match.
     *
     * @return {@code List<ClassificationCondition>}
     */
    public List<ClassificationCondition> getConditions()
    {
        return conditions;
    }

    /**
     * Set the classification conditions against which to match.
     *
     * @param conditions against which to match
     */
    public void setConditions(List<ClassificationCondition> conditions)
    {
        this.conditions = conditions;
    }

    /**
     * Retrieve the criteria against which to match the classifications.
     *
     * @return MatchCriteria
     */
    public MatchCriteria getMatchCriteria()
    {
        return matchCriteria;
    }

    /**
     * Set the criteria against which to match the classifications.
     *
     * @param matchCriteria against which to match the classifications
     */
    public void setMatchCriteria(MatchCriteria matchCriteria)
    {
        this.matchCriteria = matchCriteria;
    }

}
