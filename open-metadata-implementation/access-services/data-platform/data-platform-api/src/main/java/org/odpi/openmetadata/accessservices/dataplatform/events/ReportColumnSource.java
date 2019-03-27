/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
public class ReportColumnSource extends Source {

    private String name;
    private ReportSectionSource parentReportSection;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ReportSectionSource getParentReportSection() {
        return parentReportSection;
    }

    public void setParentReportSection(ReportSectionSource parentReportSection) {
        this.parentReportSection = parentReportSection;
    }

    @Override
    public String toString() {
        return "ReportColumnSource{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public String buildQualifiedName() {
        return parentReportSection != null ? parentReportSection.buildQualifiedName() + "." + this.getName() : this.getName();
    }
}
