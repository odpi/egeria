/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.adminservices.configurationstore.encryptedfile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.proto.KeyTemplate;
import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStoreRetrieveAll;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.adminservices.store.OMAGServerConfigStoreConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * EncryptedFileBasedServerConfigStoreConnector is the OCF connector for the encrypted file based server
 * configuration store.
 */
public class EncryptedFileBasedServerConfigStoreConnector extends OMAGServerConfigStoreConnectorBase implements OMAGServerConfigStoreRetrieveAll {

    private static final String KEYSTORE_FOLDER_BASEDIR = "data/platform/keys";
    private static final String KEYSTORE_FOLDER_PREFIX = "keystore_";
    private static final String KEY_FILE_EXTENSION = ".key";
    private static final int    RANDOM_NAME_LENGTH = 32;

    private static final String      KEY_ENV_VAR               = "EGERIA_CONFIG_KEYS";
    private static final String      DEFAULT_FILENAME_TEMPLATE = "data/servers/{0}/config/{0}.config";
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


    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        String configStoreTemplateName = getStoreTemplateName();

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
     * Get the store template name
     * @return the store template name
     */
    private String getStoreTemplateName()
    {
        EndpointProperties endpoint = connectionProperties.getEndpoint();
        String configStoreTemplateName = null;
        if (endpoint != null) {
            configStoreTemplateName = endpoint.getAddress();
        }
        if (configStoreTemplateName == null) {
            configStoreTemplateName = DEFAULT_FILENAME_TEMPLATE;
        }
        return configStoreTemplateName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveServerConfig(OMAGServerConfig omagServerConfig) {

        final String methodName = "saveServerConfig";
        File configStoreFile = getConfigStoreFile();

        try {

            if (omagServerConfig == null) {
                // If no server config was provided, treat this as a delete
                removeServerConfig();
            } else {

                log.debug("Writing encrypted server configuration.");
                Aead aead = getAead(true);
                if (aead != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String configStoreFileContents = objectMapper.writeValueAsString(omagServerConfig);
                    byte[] ciphertext = aead.encrypt(configStoreFileContents.getBytes(StandardCharsets.UTF_8), null);
                    FileUtils.writeByteArrayToFile(configStoreFile, ciphertext, false);
                } else {
                    throw new OCFRuntimeException(DocStoreErrorCode.AEAD_UNAVAILABLE.getMessageDefinition(),
                            this.getClass().getName(),
                            methodName);
                }

            }

        } catch (GeneralSecurityException | IOException e) {
            throw new OCFRuntimeException(DocStoreErrorCode.WRITE_ERROR.getMessageDefinition(e.getClass().getName(), e.getMessage()),
                    this.getClass().getName(),
                    methodName, e);
        }

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public OMAGServerConfig  retrieveServerConfig() {

        final String methodName = "retrieveServerConfig";
        OMAGServerConfig newConfigProperties = null;

        boolean isEnvVar  = isEnvBasedKeystore();
        boolean isKeyFile = isFileBasedKeystore();

        File configStoreFile = getConfigStoreFile();
        if (configStoreFile.exists()) {

            // If we have a configuration file, first try to read it as clear-text (unencrypted)...
            try {
                log.debug("Attempting to retrieve clear-text server configuration properties");
                String configStoreFileContents = FileUtils.readFileToString(configStoreFile, "UTF-8");
                ObjectMapper objectMapper = new ObjectMapper();
                newConfigProperties = objectMapper.readValue(configStoreFileContents, OMAGServerConfig.class);
                // Assuming we are able to read it (unencrypted), immediately auto-encrypt it
                log.info("Found unencrypted configuration document -- automatically encrypting it.");
                saveServerConfig(newConfigProperties);
            } catch (IOException e) {
                // If reading it as clear-text fails, and we have no keyset defined, then the configuration document
                // is probably encrypted and we have no way of decrypting it
                if (!(isEnvVar || isKeyFile)) {
                    throw new OCFRuntimeException(DocStoreErrorCode.NO_KEYSTORE.getMessageDefinition(),
                            this.getClass().getName(),
                            methodName, e);
                } else {
                    // Ensure that nothing has been set to the config properties
                    newConfigProperties = null;
                }
            }

            // If we are here, without a newConfigProperties set, we have an existing configuration file AND
            // a keyset, so attempt to decrypt the configuration file using that keyset
            if (newConfigProperties == null) {
                try {
                    log.debug("Retrieving encrypted server configuration properties");
                    Aead aead = getAead(false);
                    if (aead != null) {
                        byte[] ciphertext = FileUtils.readFileToByteArray(configStoreFile);
                        byte[] decrypted = aead.decrypt(ciphertext, null);
                        String configStoreFileContents = new String(decrypted, StandardCharsets.UTF_8);
                        ObjectMapper objectMapper = new ObjectMapper();
                        newConfigProperties = objectMapper.readValue(configStoreFileContents, OMAGServerConfig.class);
                    } else {
                        // If we have a configuration file, but no key anywhere to use to decrypt it, throw an error immediately
                        throw new OCFRuntimeException(DocStoreErrorCode.NO_KEYSTORE.getMessageDefinition(),
                                this.getClass().getName(),
                                methodName);
                    }
                } catch (GeneralSecurityException | IOException e) {
                    throw new OCFRuntimeException(DocStoreErrorCode.READ_ERROR.getMessageDefinition(e.getClass().getName(), e.getMessage()),
                            this.getClass().getName(),
                            methodName, e);
                }
            }

        }

        return newConfigProperties;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeServerConfig() {
        final String methodName = "removeServerConfig";
        File keystore = getFileBasedKeystore(false);
        if (keystore.exists()) {
            try {
                Files.delete(keystore.toPath());
            } catch (IOException e) {
                throw new OCFRuntimeException(DocStoreErrorCode.KEYSTORE_DELETE_ERROR.getMessageDefinition(e.getClass().getName(), e.getMessage()),
                        this.getClass().getName(),
                        methodName, e);
            }
        }
        File configStoreFile = getConfigStoreFile();
        if (configStoreFile.exists()) {
            try {
                Files.delete(configStoreFile.toPath());
            } catch (IOException e) {
                throw new OCFRuntimeException(DocStoreErrorCode.CONFIG_DELETE_ERROR.getMessageDefinition(configStoreFile.getName(), e.getClass().getName(), e.getMessage()),
                        this.getClass().getName(),
                        methodName, e);
            }
        }
    }
    @Override
    public Set<OMAGServerConfig> retrieveAllServerConfigs() {
        final String methodName = "retrieveAllServerConfigs";
        Set<OMAGServerConfig> omagServerConfigSet = new HashSet<>();
        try (Stream<Path> list = Files.list(Paths.get(".")))
        {
            // we need to use the configStoreTemplateName to pick up any files that match this shape.
            // this template might have inserts in

            String templateString = getStoreTemplateName();;
              Set<String> fileNames = list.map(x -> x.toString())
                    .filter(f -> isFileNameAConfig(f, templateString)).collect(Collectors.toSet());
            for (String fileName:fileNames) {
                configStoreName=fileName;
                OMAGServerConfig config = retrieveServerConfig();
                omagServerConfigSet.add(config);
            }
        } catch (IOException e) {
            // the below message does not put out the file it is currently a
            throw new OCFRuntimeException(DocStoreErrorCode.CONFIG_RETRIEVE_ALL_ERROR.getMessageDefinition(e.getClass().getName(), e.getMessage(), configStoreName),
                                          this.getClass().getName(),
                                          methodName, e);
        }
        return omagServerConfigSet;
    }

    /**
     * Check whether the file name is an OMAG Server configuration name by checking it against the template.
     * @param fileNameToCheck filename to check
     * @param templateString
     * @return true if the supplied file name is a valid configuration file name
     */
    static boolean isFileNameAConfig(String fileNameToCheck, String templateString) {
        boolean isConfig= false;
        // the file name comes through starting ./ on Mac. Remove this, so the compare to the template will work.
        if (fileNameToCheck.startsWith("./") && fileNameToCheck.length() >2 ) {
            fileNameToCheck = fileNameToCheck.substring(2);
        }

        MessageFormat mf = new MessageFormat(templateString);
        try {
            mf.parse(fileNameToCheck);
            isConfig = true;
        } catch (ParseException e) {
            // the template did not successfully parse the file name, so is not a config file.
        }
        return isConfig;
    }

    /**
     * {@inheritDoc}
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
                && Objects.equals(getFileBasedKeystore(false), that.getFileBasedKeystore(false))
                && Objects.equals(retrieveServerConfig(), that.retrieveServerConfig());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(getConfigStoreName(), getFileBasedKeystore(false), retrieveServerConfig());
    }

    private String getConfigStoreName() { return configStoreName; }

    private File getConfigStoreFile() {
        return new File(getConfigStoreName());
    }



    /**
     * Retrieve the Authenticated Encryption with Associated Data handler.
     * @param generateIfNotExists indicates whether to generate a handler if it does not exist (true) or not (false)
     * @return Aead
     * @throws GeneralSecurityException on any error
     */
    private Aead getAead(boolean generateIfNotExists) throws GeneralSecurityException {
        KeysetHandle keysetHandle = getKeysetHandleFromEnv();
        if (keysetHandle == null) {
            keysetHandle = getKeysetHandleFromFile(generateIfNotExists);
        }
        if (keysetHandle != null) {
            return keysetHandle.getPrimitive(Aead.class);
        } else {
            return null;
        }
    }

    /**
     * Indicates whether there is any environment variable-based key store defined.
     * @return boolean true if the environment variable EGERIA_CONFIG_KEYS is defined, otherwise false
     */
    private boolean isEnvBasedKeystore() {
        return getEnvKeystore() != null;
    }

    /**
     * Retrieves the value of the environment variable defining a key store.
     * @return String of JSON in Google Tink form
     */
    private String getEnvKeystore() {
        return System.getenv(KEY_ENV_VAR);
    }

    /**
     * Retrieves the keyset handle for the environment variable-based key store.
     * @return KeysetHandle
     */
    private KeysetHandle getKeysetHandleFromEnv() {

        final String methodName = "getEncryptionKeyFromEnv";
        KeysetHandle keysetHandle = null;

        log.debug("Attempting to retrieve encryption key from {} environment variable.", KEY_ENV_VAR);
        String encryptionKey = getEnvKeystore();
        if (encryptionKey == null) {
            log.debug("Environment variable {} not set -- skipping.", KEY_ENV_VAR);
        } else {
            try {
                keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withString(encryptionKey));
            } catch (GeneralSecurityException | IOException e) {
                throw new OCFRuntimeException(DocStoreErrorCode.INVALID_KEYSTORE.getMessageDefinition(e.getClass().getName(), e.getMessage()),
                        this.getClass().getName(),
                        methodName, e);
            }
        }

        return keysetHandle;

    }

    /**
     * Indicates whether there is a file-based key store defined.
     * @return boolean true if there is a single directory starting with 'keystore_' and containing a single file with
     *          a '.key' extension within it, otherwise false
     */
    private boolean isFileBasedKeystore() {
        File file = getSecureFile();
        return file != null;
    }

    /**
     * Retrieves the file in which a file-based key store is stored.
     * @return File
     */
    private File getSecureFile() {

        final String methodName = "getFileKeystore";

        File secureFile = null;
        File secureDir = getSecureDirectory();
        if (secureDir != null) {
            File[] keyFiles = secureDir.listFiles((dir, name) -> name.endsWith(KEY_FILE_EXTENSION));
            if (keyFiles == null || keyFiles.length == 0) {
                // If there are no files in the directory, try removing the directory so we can generate a new one
                try {
                    Files.delete(secureDir.toPath());
                } catch (IOException e) {
                    throw new OCFRuntimeException(DocStoreErrorCode.KEYSTORE_EMPTY.getMessageDefinition(e.getClass().getName(), e.getMessage()),
                            this.getClass().getName(),
                            methodName, e);
                }
            } else if (keyFiles.length == 1) {
                // If we have precisely one key file, use it
                secureFile = keyFiles[0];
            } else {
                // Otherwise throw an error that there are multiple
                throw new OCFRuntimeException(DocStoreErrorCode.MULTIPLE_FILES.getMessageDefinition(),
                        this.getClass().getName(),
                        methodName);
            }
        }

        return secureFile;

    }

    /**
     * Retrieves the secure directory in which keys can be stored.
     * @return File
     */
    private File getSecureDirectory() {

        final String methodName = "getSecureDirectory";

        // Start by trying to identify any pre-existing keystore directory
        File pwd = new File(KEYSTORE_FOLDER_BASEDIR);
        File[] keystoreDirs = pwd.listFiles((dir, name) -> name.startsWith(KEYSTORE_FOLDER_PREFIX));
        File secureDir;
        if (keystoreDirs == null || keystoreDirs.length == 0) {
            // If there are no directories found, just return null immediately
            return null;
        } else if (keystoreDirs.length == 1) {
            if (!keystoreDirs[0].isDirectory()) {
                throw new OCFRuntimeException(DocStoreErrorCode.KEYSTORE_NOT_DIRECTORY.getMessageDefinition(keystoreDirs[0].getAbsolutePath()),
                        this.getClass().getName(),
                        methodName);
            }
            // If we find a single directory, and it is not a file, then use that
            secureDir = keystoreDirs[0];
        } else {
            // Otherwise throw an error that multiple directories exist
            throw new OCFRuntimeException(DocStoreErrorCode.MULTIPLE_DIRECTORIES.getMessageDefinition(),
                    this.getClass().getName(),
                    methodName);
        }

        return secureDir;

    }

    /**
     * Retrieves the keyset handle for a file-based key store.
     * @param generateIfNotExists indicates whether to generate a new handle if none already exists (true) or not (false).
     * @return KeysetHandle
     */
    private KeysetHandle getKeysetHandleFromFile(boolean generateIfNotExists) {

        final String methodName = "getEncryptionKeyFromFile";
        KeysetHandle keysetHandle;
        File secureFile = getFileBasedKeystore(generateIfNotExists);

        log.debug("Attempting to retrieve encryption key from secure local file.");
        try {
            keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withFile(secureFile));
        } catch (GeneralSecurityException | IOException e) {
            throw new OCFRuntimeException(DocStoreErrorCode.INVALID_KEYSTORE.getMessageDefinition(e.getClass().getName(), e.getMessage()),
                    this.getClass().getName(),
                    methodName, e);
        }

        return keysetHandle;

    }

    /**
     * Retrieves the file containing the secure key store.
     * @param generateIfNotExists indicates whether the file should be generated if it does not exist (true) or not (false).
     * @return File
     */
    private File getFileBasedKeystore(boolean generateIfNotExists) {
        File secureFile = getSecureFile();
        if (secureFile == null && generateIfNotExists) {
            secureFile = createKeyStore();
        }
        return secureFile;
    }

    /**
     * Generate a new, secure key store file within a generated, secure directory.
     * @return File
     */
    private File createKeyStore() {

        final String methodName = "createKeyStore";
        // If no directory was found with the prefix, we need to create one
        String secureDirectory = KEYSTORE_FOLDER_BASEDIR + '/' + KEYSTORE_FOLDER_PREFIX + getRandomString();
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
            throw new OCFRuntimeException(DocStoreErrorCode.INVALID_DIRECTORY.getMessageDefinition(),
                    this.getClass().getName(),
                    methodName, e);
        }

        log.info("Generating and storing new encryption key for secure file-based configuration.");
        try {
            KeysetHandle keysetHandle = KeysetHandle.generateNew(KEY_TEMPLATE);
            CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(secureFile));
        } catch (GeneralSecurityException | IOException e) {
            throw new OCFRuntimeException(DocStoreErrorCode.INVALID_FILE.getMessageDefinition(),
                    this.getClass().getName(),
                    methodName, e);
        }

        return secureFile;

    }

    /**
     * Securely generate a new random string.
     * @return String
     */
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
