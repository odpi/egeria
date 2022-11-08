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
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The lineage mappings event of Data Engine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LineageMappingsEvent extends DataEngineEventHeader {

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
     * The lineage mappings
     * -- GETTER --
     * Return the lineage mappings
     * @return the lineage mappings
     * -- SETTER --
     * Set up the lineage mappings
     * @param lineageMappings the lineage mappings
     */
    private List<LineageMapping> lineageMappings;

}
