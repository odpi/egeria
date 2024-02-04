/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.LineageVerticesAndEdges;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class"
)
@EqualsAndHashCode
public class LineageResponse extends FFDCResponseBase implements Serializable {

    private static final long serialVersionUID = 1L;

    public LineageResponse(){}

    private LineageVerticesAndEdges lineageVerticesAndEdges;

    public LineageResponse(LineageVerticesAndEdges lineageVerticesAndEdges) {
        this.lineageVerticesAndEdges = lineageVerticesAndEdges;
    }

    public LineageVerticesAndEdges getLineageVerticesAndEdges() {
        return lineageVerticesAndEdges;
    }

    public void setLineageVerticesAndEdges(LineageVerticesAndEdges lineageVerticesAndEdges) {
        this.lineageVerticesAndEdges = lineageVerticesAndEdges;
    }
}
