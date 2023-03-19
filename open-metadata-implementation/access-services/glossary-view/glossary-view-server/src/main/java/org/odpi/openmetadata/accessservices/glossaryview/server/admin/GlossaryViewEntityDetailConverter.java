/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.server.admin;

import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

public class GlossaryViewEntityDetailConverter<B> extends OpenMetadataAPIGenericConverter<B> {

        public GlossaryViewEntityDetailConverter(OMRSRepositoryHelper repositoryHelper, String serviceName,
                                                 String serverName) {
            super(repositoryHelper, serviceName, serverName);
    }

}
