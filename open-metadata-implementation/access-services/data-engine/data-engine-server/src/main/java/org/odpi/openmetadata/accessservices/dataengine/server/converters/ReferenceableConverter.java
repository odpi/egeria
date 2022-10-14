/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * FileFolderConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a FileFolderElement bean.
 */
public class ReferenceableConverter<B> extends OpenMetadataAPIGenericConverter<B> {

    public ReferenceableConverter(OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
        super(repositoryHelper, serviceName, serverName);
    }

}
