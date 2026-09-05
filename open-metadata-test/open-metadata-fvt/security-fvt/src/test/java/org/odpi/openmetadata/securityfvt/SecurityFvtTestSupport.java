/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securityfvt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.function.Executable;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStorePurpose;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Shared helpers for the security-fvt suite: a client factory per kind of client, each built to act as a
 * named user; the small amount of metadata manipulation the element tests need; and the one assertion the
 * whole suite is built on, {@link #assertRefused}.
 * <br><br>
 * Every client is built against the generated user directory and names the token collection of the user
 * it acts as, so it presents that user's own bearer token.  The security connector never sees the token -
 * it sees a userId - but building the clients this way means the userId a test passes is one the platform
 * has actually authenticated, which is how a real deployment behaves.
 */
final class SecurityFvtTestSupport
{
    static final String SECRETS_STORE_PROVIDER      = "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider";
    static final String SECURITY_CONNECTOR_PROVIDER = "org.odpi.openmetadata.metadatasecurity.accessconnector.OpenMetadataAccessSecurityProvider";
    static final String PLATFORM_NAME               = "security-fvt Platform";
    static final int    MAX_PAGE_SIZE               = 100;

    /*
     * The message identifiers of OpenMetadataSecurityErrorCode that the tests assert on.  A refusal is only
     * counted as the refusal a test expects if it carries the right one of these - "not authorized" for the
     * wrong reason is a defect the suite is here to find, not a pass.
     */
    static final String UNAUTHORIZED_PLATFORM_ACCESS = "OPEN-METADATA-SECURITY-403-001";
    static final String UNAUTHORIZED_SERVER_ACCESS   = "OPEN-METADATA-SECURITY-403-002";
    static final String UNAUTHORIZED_SERVICE_ACCESS  = "OPEN-METADATA-SECURITY-403-003";
    static final String UNAUTHORIZED_SERVICE_OPERATION_ACCESS = "OPEN-METADATA-SECURITY-403-006";
    static final String UNAUTHORIZED_INSTANCE_CREATE = "OPEN-METADATA-SECURITY-403-008";
    static final String UNKNOWN_USER                 = "OPEN-METADATA-SECURITY-403-017";
    static final String UNAUTHORIZED_ELEMENT_ACCESS  = "OPEN-METADATA-SECURITY-403-020";

    private static final String SOURCE_NAME = "security-fvt";

    private static final PropertyHelper propertyHelper = new PropertyHelper();
    private static final ObjectMapper   YAML           = new ObjectMapper(new YAMLFactory());


    private SecurityFvtTestSupport()
    {
        // no instances
    }


    /**
     * A metadata element created by a test: its GUID and the unique name it can be searched for by.
     *
     * @param guid unique identifier
     * @param qualifiedName unique name
     */
    record CreatedElement(String guid, String qualifiedName)
    {
    }


    /**
     * Name of the secrets collection holding the token API for a user.
     *
     * @param userId user
     * @return collection name
     */
    static String tokenCollection(String userId)
    {
        return userId + "Token";
    }


    private static String secretsLocation()
    {
        return OMAGPlatformExtension.getUserDirectoryPath().toString();
    }


    /**
     * Return an Open Metadata Store client that authenticates as a user.
     *
     * @param userId user to act as
     * @return client
     * @throws Exception the client could not be created
     */
    static EgeriaOpenMetadataStoreClient storeClientAs(String userId) throws Exception
    {
        return new EgeriaOpenMetadataStoreClient(OMAGPlatformExtension.METADATA_STORE_NAME,
                                                 OMAGPlatformExtension.getPlatformURLRoot(),
                                                 SECRETS_STORE_PROVIDER,
                                                 secretsLocation(),
                                                 tokenCollection(userId),
                                                 MAX_PAGE_SIZE,
                                                 null);
    }


    /**
     * Return a platform services client that authenticates as a user.
     *
     * @param userId user to act as
     * @return client
     * @throws Exception the client could not be created
     */
    static PlatformServicesClient platformClientAs(String userId) throws Exception
    {
        return new PlatformServicesClient(PLATFORM_NAME,
                                          OMAGPlatformExtension.getPlatformURLRoot(),
                                          SECRETS_STORE_PROVIDER,
                                          secretsLocation(),
                                          tokenCollection(userId),
                                          userId,
                                          null);
    }


    /**
     * Return a server configuration client for a named server that authenticates as a user.
     *
     * @param userId user to act as
     * @param serverName server whose configuration is addressed
     * @return client
     * @throws Exception the client could not be created
     */
    static OMAGServerConfigurationClient configurationClientAs(String userId,
                                                               String serverName) throws Exception
    {
        return new OMAGServerConfigurationClient(serverName,
                                                 OMAGPlatformExtension.getPlatformURLRoot(),
                                                 SECRETS_STORE_PROVIDER,
                                                 secretsLocation(),
                                                 tokenCollection(userId),
                                                 userId,
                                                 null);
    }


    /**
     * Return a metadata access store configuration client for this suite's server that authenticates as
     * a user.
     *
     * @param userId user to act as
     * @return client
     * @throws Exception the client could not be created
     */
    static MetadataAccessStoreConfigurationClient storeConfigurationClientAs(String userId) throws Exception
    {
        return new MetadataAccessStoreConfigurationClient(OMAGPlatformExtension.METADATA_STORE_NAME,
                                                          OMAGPlatformExtension.getPlatformURLRoot(),
                                                          SECRETS_STORE_PROVIDER,
                                                          secretsLocation(),
                                                          tokenCollection(userId),
                                                          userId,
                                                          null);
    }


    /**
     * Build the connection for the access security connector with the generated user directory embedded
     * as its secrets store - the same shape the platform builds for itself from the platform.security.*
     * properties.
     *
     * @param displayName connection display name
     * @return virtual connection
     */
    static Connection securityConnection(String displayName)
    {
        Endpoint secretsEndpoint = new Endpoint();
        secretsEndpoint.setNetworkAddress(secretsLocation());

        ConnectorType secretsConnectorType = new ConnectorType();
        secretsConnectorType.setConnectorProviderClassName(SECRETS_STORE_PROVIDER);

        Map<String, Object> secretsConfiguration = new HashMap<>();
        secretsConfiguration.put(SecretsStoreConfigurationProperty.SECRETS_COLLECTION_NAME.getName(),
                                 OMAGPlatformExtension.USER_DIRECTORY_COLLECTION);

        Connection secretsConnection = new Connection();
        secretsConnection.setEndpoint(secretsEndpoint);
        secretsConnection.setConnectorType(secretsConnectorType);
        secretsConnection.setConfigurationProperties(secretsConfiguration);

        EmbeddedConnection embeddedConnection = new EmbeddedConnection();
        embeddedConnection.setDisplayName(SecretsStorePurpose.USER_DIRECTORY.getName());
        embeddedConnection.setEmbeddedConnection(secretsConnection);

        List<EmbeddedConnection> embeddedConnections = new ArrayList<>();
        embeddedConnections.add(embeddedConnection);

        ConnectorType securityConnectorType = new ConnectorType();
        securityConnectorType.setConnectorProviderClassName(SECURITY_CONNECTOR_PROVIDER);

        VirtualConnection securityConnection = new VirtualConnection();
        securityConnection.setDisplayName(displayName);
        securityConnection.setConnectorType(securityConnectorType);
        securityConnection.setEmbeddedConnections(embeddedConnections);

        return securityConnection;
    }


    /**
     * Assert that an action is refused by the security connector with a particular error, on behalf of a
     * particular user.
     * <br><br>
     * Three things are checked, and each is there because a weaker assertion would pass for the wrong
     * reason.  The exception must be {@code UserNotAuthorizedException} - not any exception, because a
     * server that was down would also "fail".  It must carry the expected message identifier - not any
     * refusal, because a user refused at the server door would also be refused a zone, and the test would
     * then be passing without the zone check having run.  And it must name the user that was refused -
     * because a client acting as one user and refused as another is a defect in the client, not a pass.
     *
     * @param expectedMessageId message identifier from OpenMetadataSecurityErrorCode
     * @param expectedUserId user the refusal should name, or null to skip that check
     * @param action the call that should be refused
     * @param description what was being attempted, for the failure message
     * @return the exception, for further assertions
     */
    static UserNotAuthorizedException assertRefused(String     expectedMessageId,
                                                    String     expectedUserId,
                                                    Executable action,
                                                    String     description)
    {
        UserNotAuthorizedException error = assertThrows(UserNotAuthorizedException.class,
                                                        action,
                                                        description + " should have been refused with a UserNotAuthorizedException");

        assertEquals(expectedMessageId, error.getReportedErrorMessageId(),
                     description + " was refused, but not for the expected reason.  Message: " + error.getReportedErrorMessage());

        if (expectedUserId != null)
        {
            assertEquals(expectedUserId, error.getUserId(),
                         description + " was refused, but the refusal names a different user.  Message: " + error.getReportedErrorMessage());
        }

        return error;
    }


    /**
     * Create a Collection element as a user, optionally placing it in governance zones and adding further
     * classifications.
     * <br><br>
     * The element is its own anchor, so that the security decisions about it are made on its own
     * classifications rather than an anchor's.
     *
     * @param client client acting as the user
     * @param userId user to create as
     * @param tag short label to make the element's names recognisable
     * @param zones zones for the ZoneMembership classification, or null for no zone classification
     * @param otherClassifications further classifications to set at creation, or null
     * @return the new element's GUID and qualified name
     * @throws Exception creation failed - including because it was refused
     */
    static CreatedElement createCollection(EgeriaOpenMetadataStoreClient     client,
                                           String                            userId,
                                           String                            tag,
                                           List<String>                      zones,
                                           Map<String, NewElementProperties> otherClassifications) throws Exception
    {
        String qualifiedName = "SecurityFVT:" + tag + ":" + UUID.randomUUID();

        ElementProperties properties = propertyHelper.addStringProperty(null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, "Security FVT " + tag);

        Map<String, NewElementProperties> initialClassifications = new HashMap<>();

        if (zones != null)
        {
            initialClassifications.put(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                       new NewElementProperties(propertyHelper.addStringArrayProperty(null,
                                                                                                      OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                                                                      zones)));
        }

        if (otherClassifications != null)
        {
            initialClassifications.putAll(otherClassifications);
        }

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        String guid = client.createMetadataElementInStore(userId,
                                                          OpenMetadataType.COLLECTION.typeName,
                                                          newElementOptions,
                                                          initialClassifications.isEmpty() ? null : initialClassifications,
                                                          new NewElementProperties(properties),
                                                          null);

        return new CreatedElement(guid, qualifiedName);
    }


    /**
     * Build the properties of an Ownership classification naming the given users as owners.
     *
     * @param ownerUserIds owners
     * @return classification properties
     */
    static NewElementProperties ownershipProperties(List<String> ownerUserIds)
    {
        ElementProperties properties = propertyHelper.addStringProperty(null, OpenMetadataProperty.OWNER.name, ownerUserIds.get(0));
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.OWNER_TYPE_NAME.name, OpenMetadataType.USER_IDENTITY.typeName);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.OWNER_PROPERTY_NAME.name, OpenMetadataProperty.USER_ID.name);
        properties = propertyHelper.addStringArrayProperty(properties, OpenMetadataProperty.USER_IDS.name, ownerUserIds);

        return new NewElementProperties(properties);
    }


    /**
     * Retrieve an element explicitly, by GUID.
     *
     * @param client client acting as the user
     * @param userId user to read as
     * @param guid element to read
     * @return the element
     * @throws Exception retrieval failed - including because it was refused
     */
    static OpenMetadataElement getElement(EgeriaOpenMetadataStoreClient client,
                                          String                        userId,
                                          String                        guid) throws Exception
    {
        return client.getMetadataElementByGUID(userId, guid, new GetOptions());
    }


    /**
     * Return the zones an element is classified with.
     *
     * @param element element
     * @return zone names, or null if it has no ZoneMembership classification
     */
    static List<String> zonesOf(OpenMetadataElement element)
    {
        final String methodName = "zonesOf";

        if (element.getClassifications() != null)
        {
            for (AttachedClassification classification : element.getClassifications())
            {
                if (OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                {
                    return propertyHelper.getStringArrayProperty(SOURCE_NAME,
                                                                 OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                                 classification.getClassificationProperties(),
                                                                 methodName);
                }
            }
        }

        return null;
    }


    /**
     * Search for an element by its unique name and report whether it came back.
     * <br><br>
     * A search never reports a refusal: an element the user may not see is left out of the results.  So
     * "not found" is the assertion the search-based tests make, and the tests pair it with a search by a
     * user who can see the element, to show the element is there to be found.
     *
     * @param client client acting as the user
     * @param userId user to search as
     * @param element element to look for
     * @return whether the element was among the results
     * @throws Exception the search itself failed
     */
    static boolean searchFinds(EgeriaOpenMetadataStoreClient client,
                               String                        userId,
                               CreatedElement                element) throws Exception
    {
        SearchOptions searchOptions = new SearchOptions();
        searchOptions.setStartFrom(0);
        searchOptions.setPageSize(MAX_PAGE_SIZE);

        List<OpenMetadataElement> results = client.findMetadataElementsWithString(userId, element.qualifiedName(), searchOptions);

        if (results != null)
        {
            for (OpenMetadataElement result : results)
            {
                if ((result != null) && (element.guid().equals(result.getElementGUID())))
                {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Update an element's description.
     *
     * @param client client acting as the user
     * @param userId user to update as
     * @param guid element to update
     * @param description new description
     * @throws Exception the update failed - including because it was refused
     */
    static void updateDescription(EgeriaOpenMetadataStoreClient client,
                                  String                        userId,
                                  String                        guid,
                                  String                        description) throws Exception
    {
        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.setMergeUpdate(true);

        client.updateMetadataElementInStore(userId,
                                            guid,
                                            updateOptions,
                                            propertyHelper.addStringProperty(null, OpenMetadataProperty.DESCRIPTION.name, description));
    }


    /**
     * Delete an element.
     *
     * @param client client acting as the user
     * @param userId user to delete as
     * @param guid element to delete
     * @throws Exception the delete failed - including because it was refused
     */
    static void deleteElement(EgeriaOpenMetadataStoreClient client,
                              String                        userId,
                              String                        guid) throws Exception
    {
        DeleteOptions deleteOptions = new DeleteOptions();
        deleteOptions.setDeleteMethod(DeleteMethod.SOFT_DELETE);
        deleteOptions.setCascadedDelete(true);

        client.deleteMetadataElementInStore(userId, guid, deleteOptions);
    }


    /**
     * Read an access control straight from the generated user directory file, so that a test can check
     * what the platform persisted rather than only what it reported back.
     *
     * @param controlName control to look up
     * @return the control's YAML node, or null if it is not in the file
     * @throws Exception the file could not be read
     */
    static JsonNode readStoredAccessControl(String controlName) throws Exception
    {
        JsonNode root    = YAML.readTree(Files.readString(OMAGPlatformExtension.getUserDirectoryPath()));
        JsonNode control = root.path("secretsCollections")
                               .path(OMAGPlatformExtension.USER_DIRECTORY_COLLECTION)
                               .path("securityAccessControls")
                               .path(controlName);

        if (control.isMissingNode() || control.isNull())
        {
            return null;
        }

        return control;
    }
}
