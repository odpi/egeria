/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.automatedcuration.server;

import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsFileConnector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

/**
 * saveClientSideSecret and deleteClientSideSecret can only write through a YAML secrets file connector.  They used
 * to skip any other connector silently and report success, so a caller was told a secret had been saved when it had
 * not.  They now fail with OMAG-COMMON-400-034 instead.
 */
public class WritableSecretsStoreTest
{
    private static final String SECRETS_STORE_GUID = "5d4b7f3a-2c1e-4a8b-9f6d-0e1c2b3a4d5f";


    /**
     * A YAML secrets file connector is returned for writing.
     *
     * @throws Exception unexpected
     */
    @Test
    public void testYAMLSecretsFileConnectorIsWritable() throws Exception
    {
        YAMLSecretsFileConnector connector = new YAMLSecretsFileConnector();

        assertSame(AutomatedCurationRESTServices.getWritableSecretsStore(connector, SECRETS_STORE_GUID, "saveClientSideSecret"), connector);
    }


    /**
     * Any other connector is rejected, naming the asset and the connector's class.
     */
    @Test
    public void testOtherConnectorIsRejected()
    {
        ConnectorBase connector = new ConnectorBase() {};

        InvalidParameterException error = expectThrows(InvalidParameterException.class,
                                                       () -> AutomatedCurationRESTServices.getWritableSecretsStore(connector,
                                                                                                                   SECRETS_STORE_GUID,
                                                                                                                   "saveClientSideSecret"));

        assertTrue(error.getMessage().contains("OMAG-COMMON-400-034"), error.getMessage());
        assertTrue(error.getMessage().contains(SECRETS_STORE_GUID), error.getMessage());
        assertTrue(error.getMessage().contains(connector.getClass().getName()), error.getMessage());
    }


    /**
     * An asset with no connector is rejected too.
     */
    @Test
    public void testMissingConnectorIsRejected()
    {
        InvalidParameterException error = expectThrows(InvalidParameterException.class,
                                                       () -> AutomatedCurationRESTServices.getWritableSecretsStore(null,
                                                                                                                   SECRETS_STORE_GUID,
                                                                                                                   "deleteClientSideSecret"));

        assertTrue(error.getMessage().contains("deleteClientSideSecret"), error.getMessage());
        assertTrue(error.getMessage().contains("<none>"), error.getMessage());
    }
}
