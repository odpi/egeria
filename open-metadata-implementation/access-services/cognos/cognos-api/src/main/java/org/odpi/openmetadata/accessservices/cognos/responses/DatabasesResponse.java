/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.responses;

import java.util.List;

import org.odpi.openmetadata.accessservices.cognos.model.ResponseContainerDatabase;

public class DatabasesResponse extends CognosOMASAPIResponse{

    public void setDatabasesList(List<ResponseContainerDatabase> databases) {
        this.setData(databases);
    }

}

