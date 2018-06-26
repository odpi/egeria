/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Status is the state of an OMAS artifact. It does not include processing workflow states like published and approved.
 * Status allows API requests to target subsets of OMAS artifacts based on their status.
 */
@JsonAutoDetect(getterVisibility= JsonAutoDetect.Visibility.PUBLIC_ONLY, setterVisibility= JsonAutoDetect.Visibility.PUBLIC_ONLY, fieldVisibility= JsonAutoDetect.Visibility.NONE)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum Status {

    /**
     * Status that does not match any of the other defined status values.
     */
    OTHER,

    /**
     * "Proposed instance to store.
     */
    PROPOSED,
    /**
     * DRAFT means that this artifact requires further processing before being accepted as ACTIVE.
     * For example an organization might require an approval before moving this artifact to ACTIVE.
     */
    DRAFT,
    /**
     * Complete draft of instance waiting for approval.
     */
    PREPARED,
    /**
     * ACTIVE means that this artifact is current.
     */
    ACTIVE,
    /**
     * DELETED means that this artifact has been soft deleted. An artifact may have been in ACTIVE
     * or DRAFT state prior to being DELETED.
     */
    DELETED
}
