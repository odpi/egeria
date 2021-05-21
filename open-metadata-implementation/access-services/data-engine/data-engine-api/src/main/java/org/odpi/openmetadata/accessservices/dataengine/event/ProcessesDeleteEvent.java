/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The delete processes event of Data Engine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class ProcessesDeleteEvent extends DataEngineEventHeader {

    /**
     * The list of qualified names of the processes
     * -- GETTER --
     * Return the list of qualified names of the processes
     * @return String - list of qualified names of the processes
     * -- SETTER --
     * Set up the list of qualified names of the processes
     * @param qualifiedNames list of qualified names of the processes
     */
    private List<String> qualifiedNames;

    /**
     * The list of unique identifiers of the processes
     * -- GETTER --
     * Return the list of unique identifiers of the processes
     * @return String - list of unique identifiers of the processes
     * -- SETTER --
     * Set up the list of unique identifiers of the processes
     * @param guids list of unique identifiers of the processes
     */
    private List<String> guids;

    /**
     * The delete semantic
     * -- GETTER --
     * Return the delete semantic
     * @return String - unique identifier of the entity
     * -- SETTER --
     * Set up the delete semantic
     * @param deleteSemantic of the entity
     */
    private DeleteSemantic deleteSemantic = DeleteSemantic.HARD;
}
