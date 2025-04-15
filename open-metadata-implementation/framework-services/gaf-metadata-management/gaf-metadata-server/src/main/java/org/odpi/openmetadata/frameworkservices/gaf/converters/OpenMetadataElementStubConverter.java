/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.converters;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * gafPropertiesConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a gafProperties bean.
 */
public class OpenMetadataElementStubConverter<B> extends OpenMetadataStoreConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName this server
     */
    public OpenMetadataElementStubConverter(OMRSRepositoryHelper repositoryHelper,
                                            String               serviceName,
                                            String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }
}
