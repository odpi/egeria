/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


package org.odpi.openmetadata.accessservices.cognos.responses;

import java.util.Arrays;


import org.odpi.openmetadata.accessservices.cognos.model.ResponseContainerSchemaTables;

public class SchemaTablesResponse extends CognosOMASAPIResponse {
	
    public void setTableList(ResponseContainerSchemaTables tables) {
        this.setData(Arrays.asList(tables));
    }
}
