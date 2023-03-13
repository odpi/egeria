/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

public class GenericStubConverter<B> extends OpenMetadataAPIGenericConverter<B> {

    /**
     * Constructor captures the initial content
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName      name of this component
     * @param serverName       name of this server
     */
    public GenericStubConverter(OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
        super(repositoryHelper, serviceName, serverName);
    }
}
