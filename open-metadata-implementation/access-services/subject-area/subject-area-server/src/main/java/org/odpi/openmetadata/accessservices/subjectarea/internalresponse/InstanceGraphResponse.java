/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.internalresponse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * InstanceGraphResponse is a response structure used internally by the Subject Area OMAS REST API
 * to represent an omrs InstanceGraph
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InstanceGraphResponse extends SubjectAreaOMASAPIResponse
{
    private InstanceGraph instanceGraph = null;

    /**
     * Default constructor
     */
    public InstanceGraphResponse() {
        this.setResponseCategory(ResponseCategory.OmrsInstanceGraph);
    }
    public InstanceGraphResponse(InstanceGraph instanceGraph)
    {
        this();
        this.instanceGraph = instanceGraph;
    }


    /**
     * Return the InstanceGraph object.
     *
     * @return instanceGraph
     */
    public InstanceGraph getInstanceGraph()
    {
        return instanceGraph;
    }

    /**
     * Set up the InstanceGraph object.
     *
     * @param instanceGraph - instanceGraph object
     */
    public void setInstanceGraph(InstanceGraph instanceGraph)
    {
        this.instanceGraph = instanceGraph;
    }


    @Override
    public String toString()
    {
        return "InstanceGraphResponse{" +
                "instanceGraph=" + instanceGraph +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
