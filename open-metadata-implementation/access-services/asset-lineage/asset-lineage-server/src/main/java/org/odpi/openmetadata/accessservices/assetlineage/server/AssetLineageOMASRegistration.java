/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;


import org.odpi.openmetadata.accessservices.assetlineage.admin.AssetLineageAdmin;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;

public class AssetLineageOMASRegistration {

    public AssetLineageOMASRegistration() {

        AccessServiceDescription myDescription = AccessServiceDescription.ASSET_LINEAGE_OMAS;
        AccessServiceRegistration myRegistration = new AccessServiceRegistration(myDescription.getAccessServiceCode(),
                myDescription.getAccessServiceName(),
                myDescription.getAccessServiceDescription(),
                myDescription.getAccessServiceWiki(),
                AccessServiceOperationalStatus.ENABLED,
                AssetLineageAdmin.class.getName()

        );
        OMAGAccessServiceRegistration.registerAccessService(myRegistration);
    }
}
