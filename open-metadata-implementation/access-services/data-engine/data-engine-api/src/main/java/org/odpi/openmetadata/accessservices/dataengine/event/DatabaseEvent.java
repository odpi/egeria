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
import org.odpi.openmetadata.accessservices.dataengine.model.Database;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The database schema event of Data Engine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class DatabaseEvent extends DataEngineEventHeader {

    /**
     * Serial version UID
     * -- GETTER --
     * Gets the serial version UID
     * @return the serial version UID
     * -- SETTER --
     * Sets the serial version UID
     * @param serialVersionUID the serial version UID
     */
    private static final long serialVersionUID = 1L;


    /**
     * The database to be created
     * -- GETTER --
     * Return the database bean
     *
     * @return the database
     * -- SETTER --
     * Set up the database bean
     * @param database the database
     */
    private Database database;
}
