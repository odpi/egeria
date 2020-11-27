/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStore;
import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStoreRetrieveAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Set;

public class TestOMAGServerAdminStoreServices {
    /**
     * Validated the values of the enum.
     */
    @Test
    public void testgetOMAGServerConfigStoreRetrieveAll()
    {
        OMAGServerAdminStoreServices omagServerAdminStoreServices = new OMAGServerAdminStoreServices();
        OMAGServerConfigStoreRetrieveAll newStore = new OMAGServerConfigStoreRetrieveAll()
        {
            @Override
            public Set<OMAGServerConfig> retrieveAllServerConfigs()
            {
                return null;
            }

            @Override
            public void setServerName(String serverName)
            {

            }

            @Override
            public void saveServerConfig(OMAGServerConfig configuration) {

            }

            @Override
            public OMAGServerConfig retrieveServerConfig() {
                return null;
            }

            @Override
            public void removeServerConfig() {

            }
        };
        OMAGServerConfigStore oldStore =new OMAGServerConfigStore()
        {
            @Override
            public void setServerName(String serverName)
            {

            }

            @Override
            public void saveServerConfig(OMAGServerConfig configuration)
            {

            }

            @Override
            public OMAGServerConfig retrieveServerConfig()
            {
                return null;
            }

            @Override
            public void removeServerConfig()
            {

            }
        };


        try {
            omagServerAdminStoreServices.getOMAGServerConfigStoreRetrieveAll(oldStore,"method1");
            fail();
        } catch (OMAGConfigurationErrorException e) {
            // good
            System.err.println(e.getReportedErrorMessage());
        }
        try {
            omagServerAdminStoreServices.getOMAGServerConfigStoreRetrieveAll(newStore,"method2");
            // good
        } catch (OMAGConfigurationErrorException e) {
            fail();
        }
    }
}
