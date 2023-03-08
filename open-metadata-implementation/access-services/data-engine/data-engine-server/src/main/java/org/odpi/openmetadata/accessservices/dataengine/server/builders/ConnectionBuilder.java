/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.builders;

import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

public class ConnectionBuilder extends ReferenceableBuilder {

    public ConnectionBuilder(String qualifiedName, String typeId, String typeName, OMRSRepositoryHelper repositoryHelper,
                                String serviceName, String serverName) {
        super(qualifiedName, typeId, typeName, repositoryHelper, serviceName, serverName);
    }
}
