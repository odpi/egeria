/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.adminservices.configurationstore.encryptedfile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.proto.KeyTemplate;
import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.userinterface.adminservices.configStore.UIServerConfigStoreConnectorBase;
import org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptedFileBasedUIServerConfigStoreConnector extends UIServerConfigStoreConnectorBase {

    private static final String KEYSTORE_FOLDER_PREFIX = "keystore";
    private static final String KEY_FILE_EXTENSION = ".key";
    private static final int RANDOM_NAME_LENGTH = 32;

    private static final String defaultFilename = "ui.server.config";
    private static final KeyTemplate keyTemplate = AeadKeyTemplates.CHACHA20_POLY1305;

    private String configStoreName  = null;
    private String keysetStoreName = null;

    private static final Logger log = LoggerFactory.getLogger(EncryptedFileBasedUIServerConfigStoreConnector.class);

    private static SecureRandom rng = null;

    /**
     * Default constructor
     */
    public EncryptedFileBasedUIServerConfigStoreConnector() { }


    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties) {

        super.initialize(connectorInstanceId, connectionProperties);
        EndpointProperties endpoint = connectionProperties.getEndpoint();
        if (endpoint != null) {
            configStoreName = endpoint.getAddress();
        }
        if (configStoreName == null) {
            configStoreName = defaultFilename;
        }
        try {
            AeadConfig.register();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Unable to initialize encryption library configuration.", e);
        }

    }


    /**
     * Save the server configuration.
     *
     * @param omagServerConfig - configuration properties to save
     */
    public void saveServerConfig(UIServerConfig omagServerConfig) {

        File configStoreFile = new File(configStoreName);
        File keystore = getKeystore();

        try {

            if (omagServerConfig == null) {
                log.debug("Deleting server config store properties: " + omagServerConfig);
                configStoreFile.delete();
                keystore.delete();
            } else {

                log.debug("Generating new encryption key for secure storage.");
                KeysetHandle keysetHandle = KeysetHandle.generateNew(keyTemplate);
                CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(
                        keystore));

                log.debug("Writing encrypted server config store properties: " + omagServerConfig);
                ObjectMapper objectMapper = new ObjectMapper();
                String configStoreFileContents = objectMapper.writeValueAsString(omagServerConfig);
                Aead aead = keysetHandle.getPrimitive(Aead.class);
                byte[] ciphertext = aead.encrypt(configStoreFileContents.getBytes(Charset.forName("UTF-8")), null);
                FileUtils.writeByteArrayToFile(configStoreFile, ciphertext, false);

            }

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Unable to generate new encryption key.", e);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write new encryption key or config file.", e);
        }

    }


    /**
     * Retrieve the configuration saved from a previous run of the server.
     *
     * @return server configuration
     */
    public UIServerConfig  retrieveServerConfig() {

        File configStoreFile = new File(configStoreName);
        File keystore = getKeystore();
        UIServerConfig newConfigProperties = null;

        try {

            log.debug("Retrieving encryption key");
            KeysetHandle keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withFile(keystore));
            Aead aead = keysetHandle.getPrimitive(Aead.class);

            log.debug("Retrieving server configuration properties");
            byte[] ciphertext = FileUtils.readFileToByteArray(configStoreFile);
            byte[] decrypted = aead.decrypt(ciphertext, null);
            String configStoreFileContents = new String(decrypted, Charset.forName("UTF-8"));
            ObjectMapper objectMapper = new ObjectMapper();
            newConfigProperties = objectMapper.readValue(configStoreFileContents, UIServerConfig.class);

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Unable to read encryption key.", e);
        } catch (IOException e) {
            log.debug("New server config store", e);
        }

        return newConfigProperties;

    }

    /**
     * Remove the server configuration.
     */
    public void removeServerConfig() {
        File keystore = new File(keysetStoreName);
        keystore.delete();
        File configStoreFile = new File(configStoreName);
        configStoreFile.delete();
    }

    /**
     * Close the config file
     */
    public void disconnect() {
        log.debug("Closing Config Store.");
    }

    private File getKeystore() {

        // Start by trying to identify any pre-existing keystore directory
        File pwd = new File(".");
        File[] keystoreDirs = pwd.listFiles((dir, name) -> name.startsWith(KEYSTORE_FOLDER_PREFIX));
        File secureFile = null;

        if (keystoreDirs.length == 0) {

            // If no directory was found with the prefix, we need to create one
            String secureDirectory = KEYSTORE_FOLDER_PREFIX + "_" + getRandomString();
            File secureDir = new File(secureDirectory);
            // ... and we need to create a key file within it
            keysetStoreName = getRandomString() + KEY_FILE_EXTENSION;
            String secureLocation = secureDirectory + File.separator + keysetStoreName;
            secureFile = new File(secureLocation);
            try {
                // We should secure the file and its containing directory to only be accessible by the OS-level owner
                FileUtils.touch(secureFile);
                secureFile.setReadable(false, false);
                secureFile.setReadable(true);
                secureDir.setExecutable(false, false);
                secureDir.setExecutable(true);
                secureDir.setReadable(false, false);
                secureDir.setReadable(true);
            } catch (IOException e) {
                throw new IllegalStateException("Unable to create secure location for storing encryption key.", e);
            }

        } else if (keystoreDirs.length == 1) {

            if (!keystoreDirs[0].isDirectory()) {
                throw new IllegalStateException("Expecting the file '" + keystoreDirs[0].getAbsolutePath() + "' to be a directory.");
            }

            // If we found a single directory, then we need to search for a key file within it
            File secureDir = keystoreDirs[0];
            File[] keyFiles = secureDir.listFiles((dir, name) -> name.endsWith(KEY_FILE_EXTENSION));

            if (keyFiles.length == 0) {
                // If for some reason we have a directory but no keys, remove the directory and start over
                secureDir.delete();
                return getKeystore();
            } else if (keyFiles.length == 1) {
                // If we have precisely one key file, use it
                secureFile = keyFiles[0];
            } else {
                log.error("Multiple key files found -- unable to determine which to use for decryption: {}", keyFiles);
                throw new IllegalStateException("Multiple key files found -- unable to determine which to use for decryption.");
            }

        } else {
            log.error("Multiple keystore directories found -- unable to determine which to use for decryption: {}", keystoreDirs);
            throw new IllegalStateException("Multiple keystore directories found -- unable to determine which to use for decryption.");
        }

        return secureFile;

    }

    private String getRandomString() {
        if (rng == null) {
            rng = new SecureRandom();
        }
        byte[] bytes = new byte[RANDOM_NAME_LENGTH];
        rng.nextBytes(bytes);
        byte[] encodedBytes = Base64.getUrlEncoder().withoutPadding().encode(bytes);
        return new String(encodedBytes, 0, RANDOM_NAME_LENGTH);
    }

}
