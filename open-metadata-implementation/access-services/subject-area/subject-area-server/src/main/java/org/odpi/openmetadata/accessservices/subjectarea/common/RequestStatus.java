/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/**
 * Request Status is the status that is supplied on a request. This will scope the request to the supplied status.
 *
 */
@JsonAutoDetect(getterVisibility= JsonAutoDetect.Visibility.PUBLIC_ONLY, setterVisibility= JsonAutoDetect.Visibility.PUBLIC_ONLY, fieldVisibility= JsonAutoDetect.Visibility.NONE)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum RequestStatus {
    /**
     * ACTIVE means that the request is for ACTIVE artifacts.
     */
    ACTIVE,
    /**
     * DELETED means that the request is for DELETE artifacts.
     */
    DELETED,
    /**
     * DRAFT means that the request is for DRAFT artifacts
     */
    DRAFT,
    /**
     * PROPOSED is the status used in request objects when requesting artifact creation.
     */
    PROPOSED,
    /**
     * ALL indicates that this request is for artifacts of any status.
     */
    ALL

}
