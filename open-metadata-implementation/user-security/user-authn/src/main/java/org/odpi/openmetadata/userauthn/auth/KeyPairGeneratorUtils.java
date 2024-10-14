/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn.auth;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 * Work with encrypted key pairs.
 */
final class KeyPairGeneratorUtils
{
    /**
     * Generate an RSA key pair to use in token authentication.
     *
     * @return RSA key pair
     */
    static KeyPair generateRsaKey()
    {
        KeyPair keyPair;
        try
        {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex)
        {
            throw new IllegalStateException(ex);
        }

        return keyPair;
    }
}
