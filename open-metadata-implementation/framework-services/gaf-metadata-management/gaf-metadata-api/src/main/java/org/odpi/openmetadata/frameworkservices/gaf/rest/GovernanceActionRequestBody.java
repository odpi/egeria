/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceActionRequestBody provides a structure for passing the properties for a new governance action.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@Deprecated
public class GovernanceActionRequestBody extends InitiateEngineActionRequestBody
{
    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "GovernanceActionRequestBody{" +
                       "qualifiedName='" + getQualifiedName() + '\'' +
                       ", domainIdentifier=" + getDomainIdentifier() +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", requestSourceGUIDs=" + getRequestSourceGUIDs() +
                       ", actionTargets=" + getActionTargets() +
                       ", receivedGuards=" + getReceivedGuards() +
                       ", startTime=" + getStartDate() +
                       ", requestType='" + getRequestType() + '\'' +
                       ", requestParameters=" + getRequestParameters() +
                       ", processName='" + getProcessName() + '\'' +
                       ", requestSourceName='" + getRequestSourceName() + '\'' +
                       ", originatorServiceName='" + getOriginatorServiceName() + '\'' +
                       ", originatorEngineName='" + getOriginatorEngineName() + '\'' +
                       '}';
    }
}
