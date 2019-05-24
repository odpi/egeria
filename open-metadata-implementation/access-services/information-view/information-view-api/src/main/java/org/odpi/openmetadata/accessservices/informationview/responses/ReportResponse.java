/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.responses;

import org.odpi.openmetadata.accessservices.informationview.events.DeployedReport;

public class ReportResponse extends InformationViewOMASAPIResponse{

    private DeployedReport report;

    public DeployedReport getReport() {
        return report;
    }

    public void setReport(DeployedReport report) {
        this.report = report;
    }

    @Override
    public String toString() {
        return "{" +
                "report=" + report +
                '}';
    }
}
