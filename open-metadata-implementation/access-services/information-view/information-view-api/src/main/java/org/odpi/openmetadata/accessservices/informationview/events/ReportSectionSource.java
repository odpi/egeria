/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.events;

public class ReportSectionSource extends Source {

    private ReportSource reportSource;
    private ReportSectionSource parentReportSection;
    private String name;

    public ReportSource getReportSource() {
        return reportSource;
    }

    public void setReportSource(ReportSource reportSource) {
        this.reportSource = reportSource;
    }

    public ReportSectionSource getParentReportSection() {
        return parentReportSection;
    }

    public void setParentReportSection(ReportSectionSource parentReportSection) {
        this.parentReportSection = parentReportSection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "reportSource=" + reportSource +
                ", parentReportSection=" + parentReportSection +
                ", name='" + name + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", guid='" + guid + '\'' +
                '}';
    }
}
