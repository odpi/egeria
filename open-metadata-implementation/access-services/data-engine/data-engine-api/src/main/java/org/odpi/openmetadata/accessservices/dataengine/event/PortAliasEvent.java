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
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The port alias event of Data Engine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PortAliasEvent extends DataEngineEventHeader {

    /**
     * The process qualified name
     * -- GETTER --
     * Returns the process qualified name
     * @return the process qualified name
     * -- SETTER --
     * Sets up the process qualified name
     * @param processQualifiedName the process qualified name
     */
    private String processQualifiedName;

    /**
     * The port alias
     * -- GETTER --
     * Returns the port alias
     * @return the port alias
     * -- SETTER --
     * Sets up the port alias
     * @param port the port alias
     */
    private PortAlias portAlias;

}
