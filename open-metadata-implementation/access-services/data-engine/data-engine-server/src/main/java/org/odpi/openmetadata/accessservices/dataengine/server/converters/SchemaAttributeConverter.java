/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * LicenseConverter transfers the relevant properties from some Open Metadata Repository Services (OMRS)
 * EntityDetail object into an License bean.
 */
public class SchemaAttributeConverter<B> extends OpenMetadataAPIGenericConverter<B> {

    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName      name of this component
     * @param serverName       local server name
     */
    public SchemaAttributeConverter(OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
        super(repositoryHelper, serviceName, serverName);
    }
}
