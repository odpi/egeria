/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.events;


import java.io.Serializable;

/**
 * OMRSEventErrorCode is a merging of the OMRSRegistryEventErrorCode, OMRSTypeDefEventErrorCode and
 * OMRSInstanceEventErrorCode that is used in OMRSEvent.  Detailed description of the values can be found
 * in the source enums.
 */
public enum OMRSEventErrorCode implements Serializable
{
    CONFLICTING_COLLECTION_ID,
    CONFLICTING_TYPEDEFS,
    CONFLICTING_ATTRIBUTE_TYPEDEFS,
    CONFLICTING_INSTANCES,
    CONFLICTING_TYPE,
    BAD_REMOTE_CONNECTION,
    TYPEDEF_PATCH_MISMATCH,
    INVALID_EVENT_FORMAT,
    INVALID_REGISTRY_EVENT,
    INVALID_TYPEDEF_EVENT,
    INVALID_INSTANCE_EVENT;

    private static final long serialVersionUID = 1L;
}
