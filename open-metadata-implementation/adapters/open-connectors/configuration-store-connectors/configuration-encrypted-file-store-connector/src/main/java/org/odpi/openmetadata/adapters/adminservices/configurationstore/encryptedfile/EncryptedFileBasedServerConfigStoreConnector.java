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
import java.util.*;
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
    private static final int RANDOM_NAME_LENGTH = 32;

    private static final String KEY_ENV_VAR = "EGERIA_CONFIG_KEYS";
    private static final String INSERT_FOR_FILENAME_TEMPLATE = "{0}";
    private static final String DEFAULT_FILENAME_TEMPLATE = "data/servers/" + INSERT_FOR_FILENAME_TEMPLATE + "/config/" + INSERT_FOR_FILENAME_TEMPLATE + ".config";
    private static final KeyTemplate KEY_TEMPLATE = AeadKeyTemplates.CHACHA20_POLY1305;
    private static final Logger log = LoggerFactory.getLogger(EncryptedFileBasedServerConfigStoreConnector.class);
    private static SecureRandom rng = null;
    private String configStoreName = null;

    /**
     * Default constructor
     */
    public EncryptedFileBasedServerConfigStoreConnector() {
        // Nothing to do...
    }

    /**
     * Securely generate a new random string.
     *
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws ConnectorCheckedException {
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
     *
     * @return the store template name
     */
    private String getStoreTemplateName() {
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
    public OMAGServerConfig retrieveServerConfig() {

        final String methodName = "retrieveServerConfig";
        OMAGServerConfig newConfigProperties = null;

        boolean isEnvVar = isEnvBasedKeystore();
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
        String templateString = getStoreTemplateName();
        Set<String> fileNames = getFileNames(templateString, methodName);
        for (String fileName : fileNames) {
            configStoreName = fileName;
            OMAGServerConfig config = retrieveServerConfig();
            omagServerConfigSet.add(config);
        }

        return omagServerConfigSet;
    }

    /**
     * Get filenames from the file system that match the store template.
     * Only supports 1 or 2 inserts in the template and they need to be in different url segments.
     * When a file is matched on the file system, the match for the insert is the serverName.
     *
     * @param methodName callers name for diagnostics
     * @return set of filenames fro the file System that match the template
     */
    protected Set<String> getFileNames(String templateString, String methodName) {
        if (!isTemplateValid(templateString)) {
            // bad template supplied - error
            throw new OCFRuntimeException(DocStoreErrorCode.CONFIG_RETRIEVE_ALL_ERROR_INVALID_TEMPLATE.getMessageDefinition(templateString),
                                          this.getClass().getName(),
                                          methodName);
        }

        Set<String> fileNames = new HashSet<>();

        int firstIndex = templateString.indexOf(INSERT_FOR_FILENAME_TEMPLATE);
        int secondIndex = -1;
        if (firstIndex != -1 && templateString.length() > firstIndex + 3) {
            String textAfter1stIndex = templateString.substring(firstIndex + 3);
            secondIndex = textAfter1stIndex.indexOf(INSERT_FOR_FILENAME_TEMPLATE);
        }
        if (log.isDebugEnabled()) {
            log.debug("templateString " + templateString +",firstIndex="+ firstIndex+",secondIndex="+ secondIndex);
        }

        try {
            if (firstIndex != -1 && secondIndex == -1) {
                // only one insert
                String firstPartOfTemplate = templateString.substring(0, firstIndex);
                String secondPartOfTemplate = templateString.substring(firstIndex + 3);

                //  we need to know if the insert is part of a folder name or part of a file name

                //      - go back to the last slash
                int lastSlashIndex = firstPartOfTemplate.lastIndexOf('/');
                //      look for the next slash
                int nextSlashIndex = -1;
                if (templateString.length() > lastSlashIndex+1) {
                    nextSlashIndex = templateString.substring(lastSlashIndex + 1).indexOf("/");

                }

                Stream<Path> listOfFolders = Files.list(Paths.get(firstPartOfTemplate.substring(0, lastSlashIndex+1)));
                String pre = templateString.substring(0, firstIndex);

                if (nextSlashIndex == -1) {
                    // get its contents then pattern match the content before and after the insert in the file name
                    String post = templateString.substring(firstIndex + 3);
                    fileNames = listOfFolders.map(x -> x.toString())
                            .filter(f -> doesStringStartAndEndMatch(f, pre, post)).collect(Collectors.toSet());
                } else {
                    // amend  next slash index to be from the start of the template string.
                    nextSlashIndex = lastSlashIndex+nextSlashIndex+1;
                    // we are looking for folders
                    String restOfFolderName = templateString.substring(firstIndex + 3, nextSlashIndex);
                    Set<String> folderNames = listOfFolders.map(x -> x.toString())
                            .filter(f -> doesStringStartAndEndMatch(f, pre, restOfFolderName)).collect(Collectors.toSet());
                    // remove post and add secondPartOfTemplate then we have the matching filenames
                    for (String folderName : folderNames) {
                        String fileName = folderName.substring(0, folderName.length() - restOfFolderName.length()) + secondPartOfTemplate;

                        File f =  new File(fileName);
                        if (f.exists() && !f.isDirectory()) {
                            fileNames.add(fileName);
                        }
                    }
                }
            } else {
                secondIndex = firstIndex + 3 + secondIndex;
                // 2 inserts - the first must be a folder name. hopefully the file name is not pathological with 2 inserts in the same segment.
                String firstPartOfTemplate = templateString.substring(0, firstIndex);
                String secondPartOfTemplate = templateString.substring(firstIndex + 3, secondIndex);
                String thirdPartOfTemplate = templateString.substring(secondIndex + 3);
                // take the serverName from the first insert and then look for its presence in the second insert position.
                // we need to find the parent folder name of the folder with the insert in and list those folders so we can match on them
                //      - go back to the last slash

                int lastSlashIndex = firstPartOfTemplate.lastIndexOf('/');
                //      look for the next slash
                int nextSlashIndex = -1;
                if (templateString.length() > lastSlashIndex+1) {
                    nextSlashIndex = templateString.substring(lastSlashIndex + 1).indexOf("/");
                    nextSlashIndex = nextSlashIndex + lastSlashIndex + 1;
                }
                Stream<Path> listOfFolders = Files.list(Paths.get(templateString.substring(0, lastSlashIndex )));

                String pre = templateString.substring(0, firstIndex);
                String restOfFolderName = "";
                if (nextSlashIndex > firstIndex) {
                    restOfFolderName = templateString.substring(firstIndex + 3, nextSlashIndex);
                }

                final String post = restOfFolderName;
                int postLength = post.length();
                Set<String> matchedFolderNames = listOfFolders.map(x -> x.toString())
                        .filter(f -> doesStringStartAndEndMatch(f, pre, post)).collect(Collectors.toSet());
                // for each folder name we need to amend to bring the folder name up to the file name.
                // find the last / in the whole string and see if it is further in that the folder we have just matched, if so there are
                // folder(s) we need to add to the folder Names we have matched

                int lastSlashIndexFromWholeTemplate = templateString.lastIndexOf('/');
                // extract the serverName from the matchedFolderName
                Set<String> serverNames = new HashSet<>();

                if (lastSlashIndexFromWholeTemplate >= nextSlashIndex) {
                    for (String matchedFolderName : matchedFolderNames) {
                        String serverName = matchedFolderName.substring(firstIndex , matchedFolderName.length() - postLength);
                        if (log.isDebugEnabled()) {
                            log.debug("serverName " + serverName);
                        }
                        serverNames.add(serverName);
                    }
                }

                for (String serverName : serverNames) {
                    String fileName = firstPartOfTemplate + serverName + secondPartOfTemplate + serverName + thirdPartOfTemplate;
                    if (log.isDebugEnabled()) {
                        log.debug("getFileNames with 2 inserts testing fileName " + fileName );
                    }
                    File f =  new File(fileName);
                    if (log.isDebugEnabled()) {
                        log.debug("see if fileName " + fileName + " exists" );
                    }
                    if (f.exists() && !f.isDirectory()) {
                        if (log.isDebugEnabled()) {
                            log.debug("fileName " + fileName + " exists");
                        }
                        fileNames.add(fileName);
                    }
                }
            }
        } catch (IOException e) {
            throw new OCFRuntimeException(DocStoreErrorCode.CONFIG_RETRIEVE_ALL_ERROR.getMessageDefinition(e.getClass().getName(), e.getMessage(), configStoreName),
                                          this.getClass().getName(),
                                          methodName, e);
        }
        return fileNames;
    }

    /**
     * check whether the supplied string starts and ends with the pre and post strings
     *
     * @param stringToTest string to test
     * @param pre          must start with this string - can be empty
     * @param post         must end with this starting - can be empty
     * @return whether the folder name starts and ends with the supplied strings.
     */
    private boolean doesStringStartAndEndMatch(String stringToTest, String pre, String post) {
        if (log.isDebugEnabled()) {
            log.debug("doesStringStartAndEndMatch " + stringToTest +",pre="+ pre+",post="+ post);
        }
        boolean isMatch = false;
        if (stringToTest.startsWith(pre) && stringToTest.endsWith(post)) {
            isMatch = true;
        }
        return isMatch;
    }

    /**
     * Check whether the template string is valid.
     *
     * @param templateString string to check
     * @return a flag indicating whether valid.
     */
    private boolean isTemplateValid(String templateString) {
        boolean isValid = true;
        int lastIndex = 0;
        int count = 0;
        int indexOfSecondInsert= -1;
        while (lastIndex != -1) {
            lastIndex = templateString.indexOf(INSERT_FOR_FILENAME_TEMPLATE, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += INSERT_FOR_FILENAME_TEMPLATE.length();
                if (count ==2) {
                    indexOfSecondInsert = lastIndex;
                }
            }
        }
        if (count == 0 || count > 2) {
            isValid = false;
        } else if (count == 2 && templateString.lastIndexOf('/') > indexOfSecondInsert) {
            // do not allow 2 folder inserts
            isValid = false;
        } else if (templateString.contains(INSERT_FOR_FILENAME_TEMPLATE + INSERT_FOR_FILENAME_TEMPLATE)) {
            // it does not make sense to have 2 inserts next to each other in the template
            isValid = false;
        }
        return isValid;
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

    private String getConfigStoreName() {
        return configStoreName;
    }

    private File getConfigStoreFile() {
        return new File(getConfigStoreName());
    }

    /**
     * Retrieve the Authenticated Encryption with Associated Data handler.
     *
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
     *
     * @return boolean true if the environment variable EGERIA_CONFIG_KEYS is defined, otherwise false
     */
    private boolean isEnvBasedKeystore() {
        return getEnvKeystore() != null;
    }

    /**
     * Retrieves the value of the environment variable defining a key store.
     *
     * @return String of JSON in Google Tink form
     */
    private String getEnvKeystore() {
        return System.getenv(KEY_ENV_VAR);
    }

    /**
     * Retrieves the keyset handle for the environment variable-based key store.
     *
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
     *
     * @return boolean true if there is a single directory starting with 'keystore_' and containing a single file with
     * a '.key' extension within it, otherwise false
     */
    private boolean isFileBasedKeystore() {
        File file = getSecureFile();
        return file != null;
    }

    /**
     * Retrieves the file in which a file-based key store is stored.
     *
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
     *
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
     *
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
     *
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
     *
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

}
