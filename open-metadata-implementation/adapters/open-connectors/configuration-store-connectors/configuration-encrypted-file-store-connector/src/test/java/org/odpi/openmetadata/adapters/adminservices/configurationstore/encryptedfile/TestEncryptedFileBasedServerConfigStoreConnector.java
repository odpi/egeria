/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.adminservices.configurationstore.encryptedfile;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestEncryptedFileBasedServerConfigStoreConnector
{
    @Test
    void  testIsFileNameAConfig()
    {
        EncryptedFileBasedServerConfigStoreConnector connector = new EncryptedFileBasedServerConfigStoreConnector();

        assertFalse(connector.isFileNameAConfig("","omag.server.{0}.config"));
        assertFalse(connector.isFileNameAConfig("omag..server.aaa.config","omag.server.{0}.config"));
        assertTrue(connector.isFileNameAConfig("aaaabbbaaaa","aaaa{0}aaaa"));
        assertTrue(connector.isFileNameAConfig("aaa.config","{0}.config"));
        assertTrue(connector.isFileNameAConfig("data/servers/aaa/config/aaa.config","data/servers/{0}/config/{0}.config"));
        assertTrue(connector.isFileNameAConfig("configaaa","config{0}"));
        assertTrue(connector.isFileNameAConfig("aaa","{0}"));
    }
}
