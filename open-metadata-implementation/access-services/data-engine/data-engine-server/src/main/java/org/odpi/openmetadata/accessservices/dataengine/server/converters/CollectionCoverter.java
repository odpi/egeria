/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

public class CollectionCoverter<B> extends OpenMetadataAPIGenericConverter<B> {
    /**
     * Constructor captures the initial content
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName      name of this component
     * @param serverName       name of this server
     */
    public CollectionCoverter(OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
        super(repositoryHelper, serviceName, serverName);
    }
}
