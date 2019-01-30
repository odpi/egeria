/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;

import org.odpi.openmetadata.accessservices.assetlineage.contentmanager.ReportHandler;
import org.odpi.openmetadata.accessservices.assetlineage.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exceptions.ReportCreationException;
import org.odpi.openmetadata.accessservices.assetlineage.responses.VoidResponse;


public class AssetLineageRestServices {

    AssetLineageInstanceHandler  instanceHandler = new AssetLineageInstanceHandler();

    public VoidResponse submitReport(String serverName,
                                     String userId,
                                     ReportRequestBody requestBody) {

        VoidResponse  response = new VoidResponse();

        try {
            ReportHandler reportCreator = instanceHandler.getReportCreator(serverName);
            reportCreator.submitReportModel(requestBody);
        }
        catch (ReportCreationException | PropertyServerException e) {
            response.setExceptionClassName(e.getReportingClassName());
            response.setExceptionErrorMessage(e.getReportedErrorMessage());
            response.setRelatedHTTPCode(e.getReportedHTTPCode());
            response.setExceptionUserAction(e.getReportedUserAction());
        }

        return response;
    }

}
