/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securityfvt;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.platformchassis.springboot.OMAGServerPlatform;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * OMAGPlatformExtension stands up the environment the security-fvt suite runs against: one OMAG Server
 * Platform, in this JVM, with the Open Metadata Access Security Connector installed both as the platform
 * security connector and as the server security connector of one in-memory metadata access store.  It is
 * started once for the whole run and shut down when the run finishes.
 * <br><br>
 * <b>The user directory is generated, and it is the test fixture.</b>  Every decision the security
 * connector makes comes from that one YAML file: which account a user has and of what type, which security
 * roles and groups it carries, and the access controls - one per platform service, one for the server, one
 * for the store service and one per governance zone - that say who may do what.  The tests are written
 * against the accounts and controls defined in {@link #writeUserDirectory}, so that method is the first
 * thing to read when a test here is unclear.  The file is written fresh on every run rather than kept in
 * {@code src/test/resources}, partly because the token API it contains has to name the port this run's
 * platform was allocated, and partly because the access control tests write to it.
 * <br><br>
 * The roles, groups and zone names are all prefixed {@code secfvt} so that nothing here can be confused
 * with the shipped {@code egeria-user-directory.omsecrets}.  In particular no named lists are defined: the
 * shipped directory expands role names into user lists through {@code namedLists}, but a user account can
 * also carry its roles and groups directly, and that is the simpler arrangement for a fixture that has to
 * be read alongside the tests.
 * <br><br>
 * The connector's own configuration is left at its defaults, which is why the platform-level access
 * controls are named {@code admin-services}, {@code platform-services} and {@code server-operations}: those
 * are the default values of {@code serverAdministratorControlName}, {@code serverOperatorsControlName} and
 * {@code serverInvestigatorsControlName}.  The server control is named after the server, and the service
 * control after the service, because that is how the connector looks them up.
 */
public class OMAGPlatformExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource
{
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(OMAGPlatformExtension.class);
    private static final String                     STORE_KEY = OMAGPlatformExtension.class.getName();

    /*
     * The one server, and the one service on it, that the element-level and service-level tests use.
     */
    public static final String METADATA_STORE_NAME = "secFvtMetadataStore";
    public static final String STORE_SERVICE_NAME  = AccessServiceDescription.OMF_METADATA_MANAGEMENT.getServiceName();

    /*
     * Users.  Every one of them has the same password: this suite is about authorization, and auth-fvt is
     * where passwords are tested.
     */
    public static final String PASSWORD = "secfvtsecret";

    /** Holds every platform role.  Configures servers; also the user that configures this suite's server. */
    public static final String ADMIN_USER_ID        = "secfvtadmin";
    /** Operator and investigator, but not administrator: may start servers but not configure them. */
    public static final String OPERATOR_USER_ID     = "secfvtoperator";
    /** Investigator only: may look at the platform but not change anything on it. */
    public static final String INVESTIGATOR_USER_ID = "secfvtinvestigator";
    /** A member of the stewards group, which the zones grant their DEFAULT operations to. */
    public static final String STEWARD_USER_ID      = "secfvtsteward";
    /** Also a steward, but with a defaultZones setting on the account. */
    public static final String CURATOR_USER_ID      = "secfvtcurator";
    /** An ordinary employee with no roles, no groups and no zone settings. */
    public static final String EMPLOYEE_USER_ID     = "secfvtemployee";
    /** A digital (non-personal) account, admitted to the server and service but not an employee. */
    public static final String DIGITAL_USER_ID      = "secfvtnpa";
    /** A contractor: admitted to the server, refused the store service. */
    public static final String CONTRACTOR_USER_ID   = "secfvtcontractor";
    /** An external user: refused at the server. */
    public static final String EXTERNAL_USER_ID     = "secfvtexternal";
    /** An account that exists but is disabled. */
    public static final String DISABLED_USER_ID     = "secfvtdisabled";
    /** The metadata access store's own userId. */
    public static final String SERVER_USER_ID       = "secfvtservernpa";
    /** Not in the directory at all. */
    public static final String UNKNOWN_USER_ID      = "secfvtnobody";

    /*
     * Security roles and groups.  A role and a group are treated identically by the connector; the
     * platform-level controls use roles and the zones use a group simply so that both paths are covered.
     */
    public static final String ADMINISTRATORS_ROLE = "secfvtAdministrators";
    public static final String OPERATORS_ROLE      = "secfvtOperators";
    public static final String INVESTIGATORS_ROLE  = "secfvtInvestigators";
    public static final String STEWARDS_GROUP      = "secfvtStewards";

    /*
     * Governance zones.  Each has an access control below except UNLISTED_ZONE, which deliberately has none.
     */
    /** READ is open to every account; everything else is for stewards. */
    public static final String OPEN_ZONE       = "secfvt-open";
    /** Every operation is for stewards. */
    public static final String RESTRICTED_ZONE = "secfvt-restricted";
    /** Every operation is for the dynamic group of employee accounts. */
    public static final String EMPLOYEES_ZONE  = "secfvt-employees";
    /** Every operation is for the element's owners, or for stewards. */
    public static final String OWNED_ZONE      = "secfvt-owned";
    /** READ open to all; UPDATE_PROPERTIES only for someone who has not maintained the element before. */
    public static final String FOUR_EYES_ZONE  = "secfvt-four-eyes";
    /** A zone with no access control at all. */
    public static final String UNLISTED_ZONE   = "secfvt-unlisted";

    /*
     * The dynamic group names the connector recognises, at their default values.
     */
    static final String ALL_USERS_GROUP      = "allUsers";
    static final String EMPLOYEE_USERS_GROUP = "employeeUsers";
    static final String CONTRACT_USERS_GROUP = "contractUsers";
    static final String DIGITAL_USERS_GROUP  = "digitalUsers";
    static final String INSTANCE_OWNER_GROUP = "instanceOwner";
    static final String NEW_MAINTAINER_GROUP = "newMaintainer";

    static final String USER_DIRECTORY_COLLECTION = "secFvtUserDirectory";
    static final String DATA_DIRECTORY            = "build/security-fvt-data";
    private static final String USER_DIRECTORY_FILE = DATA_DIRECTORY + "/security-fvt-user-directory.omsecrets";

    private static volatile boolean               started        = false;
    private static          Exception             startupFailure = null;
    private static ConfigurableApplicationContext platformContext;
    private static String                         platformURLRoot;
    private static Path                           userDirectoryPath;


    /**
     * Return the base URL of the running platform, including its allocated port.
     *
     * @return url root
     */
    public static String getPlatformURLRoot()
    {
        return platformURLRoot;
    }


    /**
     * Return the path of the generated user directory, which both security connectors read and which the
     * access control tests inspect.
     *
     * @return path to the user directory YAML
     */
    public static Path getUserDirectoryPath()
    {
        return userDirectoryPath;
    }


    /**
     * Start the environment once for the whole run.
     *
     * @param context junit context
     * @throws Exception problem starting the platform or the server
     */
    @Override
    public void beforeAll(ExtensionContext context) throws Exception
    {
        synchronized (OMAGPlatformExtension.class)
        {
            /*
             * If the environment failed to start for an earlier test class, say so directly rather than
             * letting every class fail with a different and less useful message.
             */
            if (startupFailure != null)
            {
                throw new IllegalStateException("The security-fvt environment failed to start: " + startupFailure.getMessage(),
                                                startupFailure);
            }

            if (started)
            {
                return;
            }

            context.getRoot().getStore(NAMESPACE).put(STORE_KEY, this);

            try
            {
                int port = allocateFreePort();

                platformURLRoot   = "http://localhost:" + port;
                userDirectoryPath = writeUserDirectory(platformURLRoot);

                startPlatform(port);
                confirmPlatformIsAnswering();
                configureMetadataStore();
                startServer(METADATA_STORE_NAME);

                started = true;
            }
            catch (Exception error)
            {
                startupFailure = error;
                throw error;
            }
        }
    }


    /**
     * Find a free port for the platform so that two checkouts, or two suites, can run at once.
     *
     * @return port number
     */
    private int allocateFreePort()
    {
        try (ServerSocket socket = new ServerSocket(0))
        {
            return socket.getLocalPort();
        }
        catch (IOException error)
        {
            throw new IllegalStateException("Could not allocate a port for the security-fvt platform", error);
        }
    }


    /**
     * Write the user directory this run uses.  See the class comment: this is the fixture every test is
     * written against.
     *
     * @param urlRoot the platform's URL, needed by the token API each client authenticates through
     * @return path of the file
     * @throws IOException the file could not be written
     */
    private Path writeUserDirectory(String urlRoot) throws IOException
    {
        Path target = Paths.get(USER_DIRECTORY_FILE).toAbsolutePath();

        Files.createDirectories(target.getParent());

        StringBuilder yaml = new StringBuilder();

        yaml.append("# SPDX-License-Identifier: Apache-2.0\n");
        yaml.append("# Copyright Contributors to the Egeria project.\n");
        yaml.append("#\n");
        yaml.append("# Generated by security-fvt's OMAGPlatformExtension - do not edit.  The token APIs below name the\n");
        yaml.append("# port this run's platform was allocated, so this file is only valid for the run that wrote it.\n");
        yaml.append("#\n");
        yaml.append("secretsCollections:\n");
        yaml.append("  ").append(USER_DIRECTORY_COLLECTION).append(":\n");
        yaml.append("    refreshTimeInterval: 0\n");

        /*
         * ---- Users -------------------------------------------------------------------------------------
         */
        yaml.append("    users:\n");

        appendUser(yaml, ADMIN_USER_ID, "Security FVT Administrator", UserAccountType.EMPLOYEE, UserAccountStatus.AVAILABLE,
                   List.of(ADMINISTRATORS_ROLE, OPERATORS_ROLE, INVESTIGATORS_ROLE), null, null);
        appendUser(yaml, OPERATOR_USER_ID, "Security FVT Operator", UserAccountType.EMPLOYEE, UserAccountStatus.AVAILABLE,
                   List.of(OPERATORS_ROLE, INVESTIGATORS_ROLE), null, null);
        appendUser(yaml, INVESTIGATOR_USER_ID, "Security FVT Investigator", UserAccountType.EMPLOYEE, UserAccountStatus.AVAILABLE,
                   List.of(INVESTIGATORS_ROLE), null, null);
        appendUser(yaml, STEWARD_USER_ID, "Security FVT Steward", UserAccountType.EMPLOYEE, UserAccountStatus.AVAILABLE,
                   null, List.of(STEWARDS_GROUP), null);
        appendUser(yaml, CURATOR_USER_ID, "Security FVT Curator", UserAccountType.EMPLOYEE, UserAccountStatus.AVAILABLE,
                   null, List.of(STEWARDS_GROUP), List.of(RESTRICTED_ZONE));
        appendUser(yaml, EMPLOYEE_USER_ID, "Security FVT Employee", UserAccountType.EMPLOYEE, UserAccountStatus.AVAILABLE,
                   null, null, null);
        appendUser(yaml, DIGITAL_USER_ID, "Security FVT Automated Process", UserAccountType.DIGITAL, UserAccountStatus.AVAILABLE,
                   null, null, null);
        appendUser(yaml, CONTRACTOR_USER_ID, "Security FVT Contractor", UserAccountType.CONTRACTOR, UserAccountStatus.AVAILABLE,
                   null, null, null);
        appendUser(yaml, EXTERNAL_USER_ID, "Security FVT External User", UserAccountType.EXTERNAL, UserAccountStatus.AVAILABLE,
                   null, null, null);
        appendUser(yaml, DISABLED_USER_ID, "Security FVT Disabled Employee", UserAccountType.EMPLOYEE, UserAccountStatus.DISABLED,
                   null, null, null);
        appendUser(yaml, SERVER_USER_ID, "Security FVT Metadata Store", UserAccountType.DIGITAL, UserAccountStatus.AVAILABLE,
                   null, null, null);

        /*
         * ---- Access controls ---------------------------------------------------------------------------
         */
        yaml.append("    securityAccessControls:\n");

        /*
         * Platform-level controls, at the connector's default names.
         */
        appendControl(yaml, "admin-services", "Administration Services", "ServiceAccessControl",
                      lists("DEFAULT", List.of(ADMINISTRATORS_ROLE)));
        appendControl(yaml, "platform-services", "Platform Services", "ServiceAccessControl",
                      lists("DEFAULT", List.of(OPERATORS_ROLE)));
        appendControl(yaml, "server-operations", "Server Operations", "ServiceAccessControl",
                      lists("DEFAULT", List.of(INVESTIGATORS_ROLE)));

        /*
         * The server control, named after the server: external accounts are kept out.
         */
        appendControl(yaml, METADATA_STORE_NAME, "Security FVT Metadata Store", "ServiceAccessControl",
                      lists("DEFAULT", List.of(EMPLOYEE_USERS_GROUP, CONTRACT_USERS_GROUP, DIGITAL_USERS_GROUP)));

        /*
         * The service control, named after the service: contractors get through the server door but not
         * this one.
         */
        appendControl(yaml, STORE_SERVICE_NAME, "Open Metadata Store", "ServiceAccessControl",
                      lists("DEFAULT", List.of(EMPLOYEE_USERS_GROUP, DIGITAL_USERS_GROUP)));

        /*
         * Zones.
         */
        String zoneType = OpenMetadataType.GOVERNANCE_ZONE.typeName;

        appendControl(yaml, OPEN_ZONE, "Open Zone", zoneType,
                      lists("READ", List.of(ALL_USERS_GROUP),
                            "DEFAULT", List.of(STEWARDS_GROUP)));
        appendControl(yaml, RESTRICTED_ZONE, "Restricted Zone", zoneType,
                      lists("DEFAULT", List.of(STEWARDS_GROUP)));
        appendControl(yaml, EMPLOYEES_ZONE, "Employees Zone", zoneType,
                      lists("DEFAULT", List.of(EMPLOYEE_USERS_GROUP)));
        appendControl(yaml, OWNED_ZONE, "Owned Zone", zoneType,
                      lists("DEFAULT", List.of(INSTANCE_OWNER_GROUP, STEWARDS_GROUP)));
        appendControl(yaml, FOUR_EYES_ZONE, "Four Eyes Zone", zoneType,
                      lists("READ", List.of(ALL_USERS_GROUP),
                            "UPDATE_PROPERTIES", List.of(NEW_MAINTAINER_GROUP),
                            "DEFAULT", List.of(STEWARDS_GROUP)));
        /*
         * A zone named after a user is that user's personal zone.  This control exists so that the zone is
         * secured for everybody else; the user gets in by name, not by being on this list.
         */
        appendControl(yaml, EMPLOYEE_USER_ID, "Employee's Personal Zone", zoneType,
                      lists("DEFAULT", List.of(STEWARDS_GROUP)));

        /*
         * ---- Token collections -------------------------------------------------------------------------
         *
         * One per account that logs on.  Each client in the suite names the collection for the user it acts
         * as, and the REST client connector posts that user's credentials to the token API for its bearer
         * token - the same path every real Egeria client takes.
         */
        for (String userId : List.of(ADMIN_USER_ID, OPERATOR_USER_ID, INVESTIGATOR_USER_ID, STEWARD_USER_ID, CURATOR_USER_ID,
                                     EMPLOYEE_USER_ID, DIGITAL_USER_ID, CONTRACTOR_USER_ID, EXTERNAL_USER_ID, SERVER_USER_ID))
        {
            appendTokenCollection(yaml, userId, urlRoot);
        }

        Files.writeString(target, yaml.toString());

        return target;
    }


    /**
     * Build an ordered map of one operation name to its security list, for {@link #appendControl}.
     * Ordered so that the generated file reads in the order the control was written here.
     *
     * @param operation operation name
     * @param securityList roles, groups and users permitted the operation
     * @return ordered map
     */
    private static Map<String, List<String>> lists(String       operation,
                                                   List<String> securityList)
    {
        Map<String, List<String>> result = new LinkedHashMap<>();

        result.put(operation, securityList);

        return result;
    }


    /**
     * Build an ordered map of two operation names to their security lists.
     *
     * @param operation1 first operation name
     * @param securityList1 its security list
     * @param operation2 second operation name
     * @param securityList2 its security list
     * @return ordered map
     */
    private static Map<String, List<String>> lists(String       operation1,
                                                   List<String> securityList1,
                                                   String       operation2,
                                                   List<String> securityList2)
    {
        Map<String, List<String>> result = lists(operation1, securityList1);

        result.put(operation2, securityList2);

        return result;
    }


    /**
     * Build an ordered map of three operation names to their security lists.
     *
     * @param operation1 first operation name
     * @param securityList1 its security list
     * @param operation2 second operation name
     * @param securityList2 its security list
     * @param operation3 third operation name
     * @param securityList3 its security list
     * @return ordered map
     */
    private static Map<String, List<String>> lists(String       operation1,
                                                   List<String> securityList1,
                                                   String       operation2,
                                                   List<String> securityList2,
                                                   String       operation3,
                                                   List<String> securityList3)
    {
        Map<String, List<String>> result = lists(operation1, securityList1, operation2, securityList2);

        result.put(operation3, securityList3);

        return result;
    }


    /**
     * Append one user account to the directory.
     *
     * @param yaml buffer
     * @param userId account name
     * @param userName display name
     * @param type account type - this is what the dynamic groups such as employeeUsers are built from
     * @param status account status
     * @param roles security roles, or null
     * @param groups security groups, or null
     * @param defaultZones zones to add to any element the user creates, or null
     */
    private static void appendUser(StringBuilder     yaml,
                                   String            userId,
                                   String            userName,
                                   UserAccountType   type,
                                   UserAccountStatus status,
                                   List<String>      roles,
                                   List<String>      groups,
                                   List<String>      defaultZones)
    {
        yaml.append("      ").append(userId).append(":\n");
        yaml.append("        userAccountStatus: ").append(status.name()).append('\n');
        yaml.append("        userAccountType: ").append(type.name()).append('\n');
        yaml.append("        userName: ").append(userName).append('\n');

        appendList(yaml, "        ", "securityRoles", roles);
        appendList(yaml, "        ", "securityGroups", groups);

        if (defaultZones != null)
        {
            yaml.append("        otherProperties:\n");
            appendList(yaml, "          ", "defaultZones", defaultZones);
        }

        yaml.append("        secrets:\n");
        yaml.append("          clearPassword: ").append(PASSWORD).append('\n');
    }


    /**
     * Append one access control to the directory.
     *
     * @param yaml buffer
     * @param controlName the name the connector looks the control up by
     * @param displayName display name
     * @param controlTypeName ServiceAccessControl or GovernanceZone
     * @param associatedSecurityLists operation name to the roles, groups and users permitted it
     */
    private static void appendControl(StringBuilder             yaml,
                                      String                    controlName,
                                      String                    displayName,
                                      String                    controlTypeName,
                                      Map<String, List<String>> associatedSecurityLists)
    {
        yaml.append("      ").append(controlName).append(":\n");
        yaml.append("        controlDisplayName: ").append(displayName).append('\n');
        yaml.append("        controlTypeName: ").append(controlTypeName).append('\n');
        yaml.append("        associatedSecurityList:\n");

        for (Map.Entry<String, List<String>> entry : associatedSecurityLists.entrySet())
        {
            appendList(yaml, "          ", entry.getKey(), entry.getValue());
        }
    }


    /**
     * Append a YAML list under a key.
     *
     * @param yaml buffer
     * @param indent indentation of the key
     * @param key key name
     * @param values list values, or null to append nothing
     */
    private static void appendList(StringBuilder yaml,
                                   String        indent,
                                   String        key,
                                   List<String>  values)
    {
        if ((values != null) && (! values.isEmpty()))
        {
            yaml.append(indent).append(key).append(":\n");

            for (String value : values)
            {
                yaml.append(indent).append("  - ").append(value).append('\n');
            }
        }
    }


    /**
     * Append the secrets collection a client uses to obtain a bearer token for one user.
     *
     * @param yaml buffer
     * @param userId user the token is for
     * @param urlRoot platform url
     */
    private static void appendTokenCollection(StringBuilder yaml,
                                              String        userId,
                                              String        urlRoot)
    {
        yaml.append("  ").append(SecurityFvtTestSupport.tokenCollection(userId)).append(":\n");
        yaml.append("    refreshTimeInterval: 0\n");
        yaml.append("    tokenAPI:\n");
        yaml.append("      httpRequestType: POST\n");
        yaml.append("      url: ").append(urlRoot).append("/api/token\n");
        yaml.append("      contentType: application/json\n");
        yaml.append("      requestBody:\n");
        yaml.append("        userId: ").append(userId).append('\n');
        yaml.append("        password: ").append(PASSWORD).append('\n');
    }


    /**
     * Start the platform with authentication switched on and the access security connector installed as
     * the platform security connector.
     *
     * @param port port to listen on
     */
    private void startPlatform(int port)
    {
        Map<String, Object> properties = new HashMap<>();

        properties.put("server.port", Integer.toString(port));
        properties.put("platform.name", "security-fvt OMAG Server Platform");
        properties.put("platform.description", "Hermetic, in-process platform started by the security-fvt functional verification test suite.");
        properties.put("platform.organization.name", "Egeria security-fvt");
        properties.put("platform.configstore.provider", "org.odpi.openmetadata.adapters.adminservices.configurationstore.file.FileBasedServerConfigStoreProvider");
        properties.put("platform.configstore.endpoint", DATA_DIRECTORY + "/servers/{0}/config/{0}.config");
        properties.put("startup.server.list", "");
        properties.put("startup.user", ADMIN_USER_ID);
        properties.put("cors.allowed-origins", "*");

        /*
         * The platform security connector.  The same connector class, and the same secrets collection, as
         * the server security connector configured in configureMetadataStore() - so one directory answers
         * "may this user configure a server?" and "may this user read this element?" alike.
         */
        properties.put("authentication.source", "platform");
        properties.put("platform.security.provider", SecurityFvtTestSupport.SECURITY_CONNECTOR_PROVIDER);
        properties.put("platform.security.name", SecurityFvtTestSupport.PLATFORM_NAME);
        properties.put("platform.security.secrets.provider", SecurityFvtTestSupport.SECRETS_STORE_PROVIDER);
        properties.put("platform.security.secrets.location", userDirectoryPath.toString());
        properties.put("platform.security.secrets.collection", USER_DIRECTORY_COLLECTION);

        properties.put("authn.header.name.list", "");
        properties.put("app.description", "security-fvt - open metadata security functional verification tests");
        properties.put("app.title", "Egeria security-fvt");
        properties.put("scan.packages", "org.odpi.openmetadata.*");
        properties.put("springdoc.api-docs.enabled", "false");
        properties.put("management.health.cassandra.enabled", "false");
        properties.put("management.health.redis.enabled", "false");
        properties.put("management.health.ldap.enabled", "false");
        properties.put("logging.level.root", "WARN");
        properties.put("logging.level.org.springframework", "ERROR");
        properties.put("logging.level.org.odpi.openmetadata", "WARN");
        properties.put("logging.level.org.odpi.openmetadata.platformchassis.springboot", "INFO");

        /*
         * Refused requests are the expected outcome of most tests here, and each one logs an error
         * server-side.  Keep that expected noise out of the build output.
         */
        properties.put("logging.level.org.odpi.openmetadata.userauthn", "OFF");
        properties.put("logging.level.org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler", "OFF");

        SpringApplicationBuilder builder = new SpringApplicationBuilder(OMAGServerPlatform.class);

        builder.web(WebApplicationType.SERVLET);
        builder.properties(properties);

        platformContext = builder.run();
    }


    /**
     * Check that the platform this suite just started is the one answering on the port it reported, before
     * any test runs - so that a port collision reads as a port collision rather than as every security
     * check failing at once.
     *
     * @throws Exception the platform is not answering as itself
     */
    private void confirmPlatformIsAnswering() throws Exception
    {
        java.net.http.HttpRequest request =
                java.net.http.HttpRequest.newBuilder()
                                         .uri(java.net.URI.create(platformURLRoot + "/open-metadata/platform-services/server-platform/origin"))
                                         .GET()
                                         .build();

        java.net.http.HttpResponse<String> response =
                java.net.http.HttpClient.newHttpClient()
                                        .send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200)
        {
            throw new IllegalStateException(
                    "The platform started on " + platformURLRoot + " but its origin endpoint answered " +
                            response.statusCode() + " rather than 200, so something other than this suite's " +
                            "platform is responding on that port.  Body: " + response.body());
        }
    }


    /**
     * Configure the metadata access store: an in-memory repository, the Open Metadata Store service and
     * no others, and the access security connector as its server security connector.
     * <br><br>
     * This is done as the administrator, which is itself the first exercise of the platform security
     * connector: creating a configuration document requires the {@code admin-services} control.
     *
     * @throws Exception configuration failed
     */
    private void configureMetadataStore() throws Exception
    {
        MetadataAccessStoreConfigurationClient configurationClient = SecurityFvtTestSupport.storeConfigurationClientAs(ADMIN_USER_ID);

        configurationClient.clearOMAGServerConfig();
        configurationClient.setServerUserId(SERVER_USER_ID);
        configurationClient.setBasicServerProperties("Egeria security-fvt",
                                                     "Metadata access store whose security connector the security-fvt suite tests.",
                                                     SERVER_USER_ID,
                                                     SecurityFvtTestSupport.SECRETS_STORE_PROVIDER,
                                                     userDirectoryPath.toString(),
                                                     SecurityFvtTestSupport.tokenCollection(SERVER_USER_ID),
                                                     platformURLRoot,
                                                     SecurityFvtTestSupport.MAX_PAGE_SIZE);
        configurationClient.setInMemLocalRepository();
        configurationClient.setLocalMetadataCollectionName(METADATA_STORE_NAME + " repository");
        configurationClient.addConsoleAuditLogDestination(new ArrayList<>());
        configurationClient.configureAccessServiceNoTopics(AccessServiceDescription.OMF_METADATA_MANAGEMENT.getServiceURLMarker());

        /*
         * Last, so that the calls above do not depend on the connector they are configuring.  From here on
         * every change to this server's configuration is also checked by the server security connector.
         */
        configurationClient.setServerSecurityConnection(SecurityFvtTestSupport.securityConnection("security-fvt server security connection"));
    }


    /**
     * Start a server and confirm the platform reports it as active.
     *
     * @param serverName server to start
     * @throws Exception the server did not start
     */
    private void startServer(String serverName) throws Exception
    {
        PlatformServicesClient platformServicesClient = SecurityFvtTestSupport.platformClientAs(ADMIN_USER_ID);

        platformServicesClient.activateWithStoredConfig(serverName);

        if (! platformServicesClient.getActiveServers().contains(serverName))
        {
            throw new IllegalStateException("Server " + serverName + " was activated on platform " + platformURLRoot +
                                                    " but does not appear in its list of active servers");
        }

        System.out.println("security-fvt: started " + serverName);
    }


    /**
     * Shut the platform down at the end of the whole run.
     */
    @Override
    public void close()
    {
        if (platformContext != null)
        {
            platformContext.close();
            platformContext = null;
            started = false;
        }
    }
}
