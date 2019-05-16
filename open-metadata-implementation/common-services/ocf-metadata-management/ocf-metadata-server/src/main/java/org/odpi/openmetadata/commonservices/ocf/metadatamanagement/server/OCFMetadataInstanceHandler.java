/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.server;

import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;

/**
 * ConnectedAssetInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ConnectedAssetAdmin class.
 */
public class OCFMetadataInstanceHandler extends OCFOMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    public OCFMetadataInstanceHandler()
    {
        super(CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceName());
    }
}
