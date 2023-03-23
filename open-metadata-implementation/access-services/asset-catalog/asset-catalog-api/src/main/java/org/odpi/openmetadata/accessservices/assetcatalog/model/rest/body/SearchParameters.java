/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;

import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SearchParameters provides a structure to make the assets's search results more precise.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SearchParameters implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The maximum number of elements that can be returned on a request
     * -- SETTER --
     * Set up the pageSize the result set to only include the specified number of entries
     * @param pageSize max number of elements that can be returned on a request.
     * -- GETTER --
     * Return the maximum page pageSize supported by this server.
     * @return max number of elements that can be returned on a request.
     */
    @PositiveOrZero
    private Integer pageSize = 0;

    /**
     * The start from of the result
     * -- SETTER --
     * Set up the start from of the result set for pagination
     * @param from start from of the result set
     * -- GETTER --
     * Return the  start from of the result set
     * @return the start from of result
     */
    @PositiveOrZero
    private Integer from = 0;

    /**
     * The number of the relationships out from the starting entity
     * -- GETTER --
     * Return the number of the relationships out from the starting entity that the query will traverse to gather results.
     * @return number of the relationship
     * -- SETTER --
     * Set up the number of the relationships out from the starting entity that the query will traverse to gather results.
     * @param level the number of the relationships out from the starting entity
     */
    @PositiveOrZero
    private Integer level = 1;

    /**
     * The name of the property that is to be used to sequence the results
     * -- SETTER --
     * Set up the name of the property that is to be used to sequence the results
     * @param sequencingProperty the name of the property that is to be used to sequence the results
     * -- GETTER --
     * Return the name of the property that is to be used to sequence the results
     * @return the name of the property that is to be used to sequence the results
     */
    private String sequencingProperty;

    /**
     * The enum defining how the results should be ordered
     * -- GETTER --
     * Return the enum defining how the results should be ordered
     * @return the enum defining how the results should be ordered
     * -- SETTER --
     * Set up the enum defining how the results should be ordered
     * @param sequencingOrder the enum defining how the results should be ordered
     */
    private SequencingOrder sequencingOrder;

    /**
     * The list of classifications that must be present on all returned entities
     * -- GETTER --
     * Returns the list of classifications that must be present on all returned entities.
     * @return list of classifications that must be present on all returned entities.
     * -- SETTER --
     * Set up the list of classifications that must be present on all returned entities.
     * @param limitResultsByClassification list of classifications that must be present on all returned entities.
     */
    private List<String> limitResultsByClassification;

    /**
     * The list of entity types to search for
     * -- SETTER --
     * Set up the list of entity types name to search for. Null means any types.
     * @param entityTypes the list of entity types to search for
     * -- GETTER --
     * Return the list of entity types name to search for. Null means any types.
     * @return the list of entity types to search for
     */
    private List<String> entityTypes;

    /**
     * The list of relationship types to include in the query results
     * -- GETTER --
     * Return the list of relationship types to include in the query results.
     * Null means include all relationships found, irrespective of their type.
     * @return list of relationship types to include in the query results
     * -- SETTER --
     * Set up the list of relationship types to include in the query results.
     * Null means include all relationships found, irrespective of their type.
     * @param relationshipTypeGUIDs List of relationship types to include in the query results
     */
    private List<String> relationshipTypeGUIDs;

    /**
     * The case sensitivity for the search criteria
     * -- GETTER --
     * Returns whether the search should be performed as a case-insensitive regular expression (true)
     * or as a case-sensitive regular expression (false)
     * @return false if it is performed a case-insensitive search and true otherwise
     * -- SETTER --
     * Indicates whether the search should be performed as a case-insensitive regular expression (true)
     * or as a case-sensitive regular expression (false)
     * @param caseInsensitive boolean to set the case sensitivity for the search criteria
     */
    private boolean caseInsensitive = Boolean.TRUE;

    private boolean exactMatch = Boolean.FALSE;
}
