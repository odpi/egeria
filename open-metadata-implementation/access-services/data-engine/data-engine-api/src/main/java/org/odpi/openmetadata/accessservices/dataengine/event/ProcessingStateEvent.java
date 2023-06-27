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
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessingState;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
/**
 * The processing state event of Data Engine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProcessingStateEvent extends DataEngineEventHeader {

    /**
     * Serial version ID
     * -- GETTER --
     * Gets the serial version ID
     * @return the serial version ID
     * -- SETTER --
     * Sets the serial version ID
     * @param serialVersionUID the serial version ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * The processing state.
     * -- GETTER --
     * Return the processing state
     *
     * @return processing state
     * -- SETTER --
     * Set up the processing state
     * @param processingState processing state
     */
    private ProcessingState processingState;
}
