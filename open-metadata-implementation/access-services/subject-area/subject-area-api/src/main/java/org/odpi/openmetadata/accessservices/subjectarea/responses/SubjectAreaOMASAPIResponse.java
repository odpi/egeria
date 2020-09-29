/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GenericResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * @param <R> type for response results
 * SubjectAreaOMASAPIResponse provides a common header for Subject Area OMAS managed rest to its REST API.
 * It manages information about exceptions.  If no exception has been raised exceptionClassName is null.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true, value = {"head"})
public class SubjectAreaOMASAPIResponse<R> extends FFDCResponseBase implements GenericResponse<R> {
    @JsonProperty("result")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "class", visible = true)
    private final List<R> result = new ArrayList<>();

    @JsonCreator
    public SubjectAreaOMASAPIResponse() { /* default constructor */ }

    @Override
    public void addAllResults(Collection<? extends R> results) {
        this.result.addAll(results);
    }

    @Override
    public void addResult(R result){
        this.result.add(result);
    }

    /**
     * Set a standard exceptional info for the response
     *
     * @param e exception {@link OCFCheckedExceptionBase}
     * @param className name of the class being called.
     **/
    public void setExceptionInfo(OCFCheckedExceptionBase e, String className) {
        super.setRelatedHTTPCode(e.getReportedHTTPCode());
        super.setExceptionClassName(className);
        super.setActionDescription(e.getReportingActionDescription());
        super.setExceptionUserAction(e.getReportedUserAction());
        super.setExceptionErrorMessage(e.getReportedErrorMessage());
        super.setExceptionSystemAction(e.getReportedSystemAction());
        super.setExceptionProperties(e.getRelatedProperties());
    }

    @Override
    public List<R> results() {
        return new ArrayList<>(result);
    }
}