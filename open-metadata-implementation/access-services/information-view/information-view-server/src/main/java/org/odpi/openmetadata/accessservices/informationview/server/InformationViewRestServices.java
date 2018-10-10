/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.server;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.ReportCreator;
import org.odpi.openmetadata.accessservices.informationview.events.ReportRequestBody;


public class InformationViewRestServices {
    private static ReportCreator reportCreator;

    public static void setReportCreator(ReportCreator reportCreator) {
        InformationViewRestServices.reportCreator = reportCreator;
    }

    public void submitReport(String userId,
                             ReportRequestBody requestBody) {
        reportCreator.createReportModel(requestBody);
    }

}
