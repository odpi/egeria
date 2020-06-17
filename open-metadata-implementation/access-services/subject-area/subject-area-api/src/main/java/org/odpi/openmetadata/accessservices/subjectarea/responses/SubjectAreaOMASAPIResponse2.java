/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubjectAreaOMASAPIResponse2<R> extends FFDCResponseBase {
    private List<R> result = new ArrayList<>();

    public void addAllResults(Collection<? extends R> entities) {
        this.result.addAll(entities);
    }

    public void addResult(R entity){
        this.result.add(entity);
    }

    public void setExceptionInfo(OCFCheckedExceptionBase e, String className) {
        super.setRelatedHTTPCode(e.getReportedHTTPCode());
        super.setExceptionClassName(className);
        super.setActionDescription(e.getReportingActionDescription());
        super.setExceptionUserAction(e.getReportedUserAction());
        super.setExceptionErrorMessage(e.getReportedErrorMessage());
        super.setExceptionSystemAction(e.getReportedSystemAction());
        super.setExceptionProperties(e.getRelatedProperties());
    }

    @JsonIgnore
    public R getHead() {
        if (!result.isEmpty()) {
            return getResult().get(0);
        }

        return null;
    }

    public List<R> getResult() {
        return new ArrayList<>(result);
    }
}