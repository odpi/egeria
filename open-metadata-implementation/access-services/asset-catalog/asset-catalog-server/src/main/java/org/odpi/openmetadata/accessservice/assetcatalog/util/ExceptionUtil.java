/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.util;

import org.odpi.openmetadata.accessservice.assetcatalog.model.responses.AssetCatalogOMASAPIResponse;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSCheckedExceptionBase;
import org.springframework.stereotype.Service;

@Service
public class ExceptionUtil {

    public void captureException(AssetCatalogOMASAPIResponse response, OMRSCheckedExceptionBase e) {
        response.setRelatedHTTPCode(e.getReportedHTTPCode());
        response.setExceptionClassName(e.getClass().getName());
        response.setExceptionErrorMessage(e.getErrorMessage());
        response.setExceptionSystemAction(e.getReportedSystemAction());
        response.setExceptionUserAction(e.getReportedUserAction());
    }

}
