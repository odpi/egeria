/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.adminservices.configurationstore.encryptedfile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.proto.KeyTemplate;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStoreConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

public class EncryptedFileBasedServerConfigStoreConnector extends OMAGServerConfigStoreConnectorBase {

    private static final String KEYSTORE_FOLDER_PREFIX = "keystore";
    private static final String KEY_FILE_EXTENSION = ".key";
    private static final int    RANDOM_NAME_LENGTH = 32;

    private static final String      DEFAULT_FILENAME_TEMPLATE = "omag.server.{0}.config";
    private static final KeyTemplate KEY_TEMPLATE              = AeadKeyTemplates.CHACHA20_POLY1305;

    private String configStoreName  = null;

    private static final Logger log = LoggerFactory.getLogger(EncryptedFileBasedServerConfigStoreConnector.class);

    private static SecureRandom rng = null;

    /**
     * Default constructor
     */
    public EncryptedFileBasedServerConfigStoreConnector() {
        // Nothing to do...
    }


    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        EndpointProperties endpoint = connectionProperties.getEndpoint();
        String configStoreTemplateName = null;
        if (endpoint != null) {
            configStoreTemplateName = endpoint.getAddress();
        }
        if (configStoreTemplateName == null) {
            configStoreTemplateName = DEFAULT_FILENAME_TEMPLATE;
        }

        configStoreName = super.getStoreName(configStoreTemplateName, serverName);

        try {
            AeadConfig.register();
        } catch (GeneralSecurityException e) {

            throw new ConnectorCheckedException(DocStoreErrorCode.INIT_ERROR.getMessageDefinition(e.getClass().getName(), e.getMessage()),
                                                this.getClass().getName(),
                                                methodName, e);
        }

    }

    /**
     * Save the server configuration.
     *
     * @param omagServerConfig - configuration properties to save
     */
    public void saveServerConfig(OMAGServerConfig omagServerConfig) {

        File configStoreFile = new File(configStoreName);
        File keystore = getKeystore();

        try {

            if (omagServerConfig == null) {
                removeServerConfig();
            } else {

                log.debug("Generating new encryption key for secure storage.");
                KeysetHandle keysetHandle = KeysetHandle.generateNew(KEY_TEMPLATE);
                CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(
                        keystore));

                log.debug("Writing encrypted server config store properties: {}", omagServerConfig);
                ObjectMapper objectMapper = new ObjectMapper();
                String configStoreFileContents = objectMapper.writeValueAsString(omagServerConfig);
                Aead aead = keysetHandle.getPrimitive(Aead.class);
                byte[] ciphertext = aead.encrypt(configStoreFileContents.getBytes(StandardCharsets.UTF_8), null);
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
    public OMAGServerConfig  retrieveServerConfig() {

        File configStoreFile = new File(configStoreName);
        File keystore = getKeystore();
        OMAGServerConfig newConfigProperties = null;

        try {

            log.debug("Retrieving encryption key");
            KeysetHandle keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withFile(keystore));
            Aead aead = keysetHandle.getPrimitive(Aead.class);

            log.debug("Retrieving server configuration properties");
            byte[] ciphertext = FileUtils.readFileToByteArray(configStoreFile);
            byte[] decrypted = aead.decrypt(ciphertext, null);
            String configStoreFileContents = new String(decrypted, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            newConfigProperties = objectMapper.readValue(configStoreFileContents, OMAGServerConfig.class);

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
        File keystore = getKeystore();
        try {
            Files.delete(keystore.toPath());
        } catch (IOException e) {
            log.error("Unable to delete keystore.", e);
        }
        File configStoreFile = new File(configStoreName);
        try {
            Files.delete(configStoreFile.toPath());
        } catch (IOException e) {
            log.error("Unable to delete server config file: {}", configStoreFile.getName(), e);
        }
    }

    /**
     * Close the config file
     */
    @Override
    public void disconnect() {
        log.debug("Closing Config Store.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EncryptedFileBasedServerConfigStoreConnector)) return false;
        EncryptedFileBasedServerConfigStoreConnector that = (EncryptedFileBasedServerConfigStoreConnector) obj;
        return Objects.equals(getConfigStoreName(), that.getConfigStoreName())
                && Objects.equals(getKeystore(), that.getKeystore())
                && Objects.equals(retrieveServerConfig(), that.retrieveServerConfig());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(getConfigStoreName(), getKeystore(), retrieveServerConfig());
    }

    private String getConfigStoreName() { return configStoreName; }

    private File getKeystore() {

        // Start by trying to identify any pre-existing keystore directory
        File pwd = new File(".");
        File[] keystoreDirs = pwd.listFiles((dir, name) -> name.startsWith(KEYSTORE_FOLDER_PREFIX));
        File secureFile;

        if (keystoreDirs == null || keystoreDirs.length == 0) {

            // If no directory was found with the prefix, we need to create one and the key file within it
            secureFile = createKeyStore();

        } else if (keystoreDirs.length == 1) {

            if (!keystoreDirs[0].isDirectory()) {
                throw new IllegalStateException("Expecting the file '" + keystoreDirs[0].getAbsolutePath() + "' to be a directory.");
            }

            // If we found a single directory, then we need to search for a key file within it
            File secureDir = keystoreDirs[0];
            File[] keyFiles = secureDir.listFiles((dir, name) -> name.endsWith(KEY_FILE_EXTENSION));

            if (keyFiles == null || keyFiles.length == 0) {
                // If for some reason we have a directory but no keys, remove the directory and start over
                try {
                    Files.delete(secureDir.toPath());
                } catch (IOException e) {
                    log.error("Unable to remove empty secure directory.", e);
                }
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

    private File createKeyStore() {

        // If no directory was found with the prefix, we need to create one
        String secureDirectory = KEYSTORE_FOLDER_PREFIX + "_" + getRandomString();
        File secureDir = new File(secureDirectory);
        // ... and we need to create a key file within it
        String keysetStoreName = getRandomString() + KEY_FILE_EXTENSION;
        String secureLocation = secureDirectory + File.separator + keysetStoreName;
        File secureFile = new File(secureLocation);

        try {
            // We should secure the file and its containing directory to only be accessible by the OS-level owner
            FileUtils.touch(secureFile);
            if (secureFile.setReadable(false, false)) {
                log.debug("Keystore file marked as un-readable.");
            } else {
                log.warn("Unable to mark keystore file as un-readable.");
            }
            if (secureFile.setReadable(true)) {
                log.debug("Keystore file marked as readable only by owner.");
            } else {
                log.warn("Unable to mark keystore file as readable only by owner.");
            }
            if (secureDir.setExecutable(false, false)) {
                log.debug("Secure directory marked as non-executable.");
            } else {
                log.warn("Unable to mark secure directory as non-executable.");
            }
            if (secureDir.setExecutable(true)) {
                log.debug("Secure directory marked as executable only by owner.");
            } else {
                log.warn("Unable to mark secure directory as executable only by owner.");
            }
            if (secureDir.setReadable(false, false)) {
                log.debug("Secure directory marked as non-readable.");
            } else {
                log.warn("Unable to mark secure directory as non-readable.");
            }
            if (secureDir.setReadable(true)) {
                log.debug("Secure directory marked as readable only by owner.");
            } else {
                log.warn("Unable to mark secure directory as readable only by owner.");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create secure location for storing encryption key.", e);
        }

        return secureFile;

    }

    private static String getRandomString() {
        if (rng == null) {
            rng = new SecureRandom();
        }
        byte[] bytes = new byte[RANDOM_NAME_LENGTH];
        rng.nextBytes(bytes);
        byte[] encodedBytes = Base64.getUrlEncoder().withoutPadding().encode(bytes);
        return new String(encodedBytes, 0, RANDOM_NAME_LENGTH);
    }

}
