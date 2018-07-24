/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.informationview.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessTerm {

    private String guid;
    private String name;
    private String query;

    /**
     * Return the guid of the business term
     *
     * @return guid of the business term
     */
    public String getGuid() {
        return guid;
    }

    /**
     * set up the  guid of the business term
     *
     * @param guid - guid of the business term
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * Return the name of the business term
     *
     * @return name of the business term
     */
    public String getName() {
        return name;
    }

    /**
     * set up the name of the business term
     *
     * @param name - name of the business term
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the query of the business term
     *
     * @return query of the business term
     */
    public String getQuery() {
        return query;
    }

    /**
     * set up the query of the business term
     *
     * @param query - query for the business term
     */
    public void setQuery(String query) {
        this.query = query;
    }
}
