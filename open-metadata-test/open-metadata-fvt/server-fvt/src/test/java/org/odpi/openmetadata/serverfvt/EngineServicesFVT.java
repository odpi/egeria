/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.engineservices.governanceaction.client.GovernanceActionEngineClient;
import org.odpi.openmetadata.engineservices.repositorygovernance.client.RepositoryGovernanceClient;
import org.odpi.openmetadata.engineservices.surveyaction.client.SurveyActionClient;
import org.odpi.openmetadata.engineservices.watchdogaction.client.WatchdogActionClient;
import org.odpi.openmetadata.commonservices.ffdc.properties.ConnectorReport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * EngineServicesFVT covers the four engine services - governance action, survey action, watchdog action and
 * repository governance - each of which publishes exactly one operation: validate a connector.
 * <br><br>
 * That operation is small but it is the one an operator reaches for first, because it answers the question
 * that every governance engine failure starts with: "is this connector even loadable on this platform?"  It
 * is called before a governance service is configured into an engine, precisely so that a bad class name is
 * caught while it is still a typo rather than after it has become an engine action stuck at {@code FAILED}.
 * <br><br>
 * The four services are near-identical copies of one another - four clients, four controllers, four URL
 * prefixes differing in one path segment - which is exactly the shape in which one copy drifts from the
 * other three and nobody notices.  So they are driven here through the same parameterized test rather than
 * four separately written ones: the point being made is that all four behave the same way, and writing them
 * out four times would let a difference hide as a difference in the test.
 * <br><br>
 * These run against the <b>engine host</b> this suite starts.  An engine service is hosted by an engine
 * host, and validating a connector needs the service to be running there - it does not need the engine to
 * have found its definitions, which is why this works on a platform with no content packs loaded.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class EngineServicesFVT
{
    /**
     * A connector provider that is certainly on the platform's classpath and just as certainly not a
     * governance service.  It loads, it produces a connector, and that connector is the wrong kind - which
     * is what makes it useful here.
     */
    private static final String WRONG_KIND_OF_CONNECTOR_PROVIDER =
            "org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider";

    /**
     * A class name that is certainly not on the classpath.
     */
    private static final String UNKNOWN_CONNECTOR_PROVIDER = "org.odpi.openmetadata.serverfvt.NoSuchConnectorProvider";


    /**
     * One entry per engine service, each holding a name for the test report and a way of calling that
     * service's single operation.
     *
     * @return the four engine services
     */
    static Stream<Object[]> engineServices()
    {
        return Stream.of(new Object[]{"governance-action", (ConnectorValidator) EngineServicesFVT::validateThroughGovernanceAction},
                         new Object[]{"survey-action", (ConnectorValidator) EngineServicesFVT::validateThroughSurveyAction},
                         new Object[]{"watchdog-action", (ConnectorValidator) EngineServicesFVT::validateThroughWatchdogAction},
                         new Object[]{"repository-governance", (ConnectorValidator) EngineServicesFVT::validateThroughRepositoryGovernance});
    }


    /**
     * The one operation each engine service publishes, as something the tests below can call without caring
     * which of the four clients is behind it.
     */
    @FunctionalInterface
    interface ConnectorValidator
    {
        /**
         * Ask an engine service to validate a connector provider.
         *
         * @param connectorProviderClassName class to validate
         * @return what the service made of it
         * @throws Exception the service refused or could not be reached
         */
        ConnectorReport validate(String connectorProviderClassName) throws Exception;
    }


    /**
     * Every engine service should refuse a connector of the wrong <em>kind</em>, and say what kind it
     * wanted.
     * <br><br>
     * This is the more interesting half of what {@code validateConnector} does.  Refusing a class that is
     * not on the classpath is easy; refusing one that loads perfectly well but is not a governance service
     * is the check that catches a real configuration mistake - naming, say, a repository connector where a
     * survey service was meant - and it can only be done by instantiating the provider and looking at what
     * it produces.
     * <br><br>
     * The assertion is on all three parts of the message, because all three are needed to act on it: which
     * class was supplied, what kind of connector that service required, and which engine service is
     * complaining.  A caller who has just configured four engine services needs the last of those to know
     * where to look.
     *
     * @param serviceName engine service being exercised, used in the test report
     * @param validator the call into that service
     * @throws Exception unexpected failure in the test
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("engineServices")
    void anEngineServiceRefusesAConnectorOfTheWrongKind(String             serviceName,
                                                        ConnectorValidator validator) throws Exception
    {
        Exception error = assertThrows(Exception.class,
                                       () -> validator.validate(WRONG_KIND_OF_CONNECTOR_PROVIDER),
                                       "The " + serviceName + " engine service should refuse a connector that is" +
                                               " loadable but is not a governance service");

        String message = String.valueOf(error.getMessage());

        assertTrue(message.contains(WRONG_KIND_OF_CONNECTOR_PROVIDER),
                   "The " + serviceName + " engine service should name the class that was supplied.  It said: " + message);
        assertTrue(message.contains("Connector"),
                   "The " + serviceName + " engine service should name the kind of connector it required," +
                           " because that is what tells the caller what to supply instead.  It said: " + message);
    }


    /**
     * Every engine service should refuse a connector provider that is not on the classpath, and the message
     * should name the class.
     * <br><br>
     * This is the case the operation exists for, so the quality of the message is the whole value of it.  A
     * refusal that does not name the class leaves the caller unable to tell a typo from a missing jar, which
     * is the one distinction they came here to make.
     *
     * @param serviceName engine service being exercised, used in the test report
     * @param validator the call into that service
     * @throws Exception unexpected failure in the test
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("engineServices")
    void anEngineServiceRefusesAnUnknownConnectorByName(String             serviceName,
                                                        ConnectorValidator validator) throws Exception
    {
        Exception error = assertThrows(Exception.class,
                                       () -> validator.validate(UNKNOWN_CONNECTOR_PROVIDER),
                                       "The " + serviceName + " engine service should refuse a class it cannot load");

        String message = String.valueOf(error.getMessage());

        assertFalse(message.isBlank(), "A refusal from the " + serviceName + " engine service should say something");
        assertTrue(message.contains(UNKNOWN_CONNECTOR_PROVIDER),
                   "The " + serviceName + " engine service should name the class it could not load.  It said: " + message);
    }


    /**
     * Every engine service should refuse a null connector provider name.
     *
     * @param serviceName engine service being exercised, used in the test report
     * @param validator the call into that service
     * @throws Exception unexpected failure in the test
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("engineServices")
    void anEngineServiceRefusesANullConnectorName(String             serviceName,
                                                  ConnectorValidator validator) throws Exception
    {
        Exception error = assertThrows(Exception.class,
                                       () -> validator.validate(null),
                                       "The " + serviceName + " engine service should refuse a null class name");

        assertNotNull(error.getMessage(),
                      "Refusing a null class name at the " + serviceName + " engine service should say what was wrong");
    }


    /**
     * Each engine service should identify itself in its refusal, rather than all four producing the same
     * anonymous message.
     * <br><br>
     * The four services are copies of one another, and the failure mode this guards against is a copy that
     * was made without updating the service name it reports - which would send an operator to the wrong
     * engine service with a correct-looking error message.  Checking that the four messages differ is the
     * only way to notice, because each one on its own looks perfectly reasonable.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void eachEngineServiceIdentifiesItselfInItsRefusal() throws Exception
    {
        List<String> messages = new ArrayList<>();

        for (Object[] engineService : engineServices().toList())
        {
            String             serviceName = (String) engineService[0];
            ConnectorValidator validator   = (ConnectorValidator) engineService[1];

            Exception error = assertThrows(Exception.class,
                                           () -> validator.validate(WRONG_KIND_OF_CONNECTOR_PROVIDER),
                                           "The " + serviceName + " engine service should refuse a connector of the" +
                                                   " wrong kind");

            String message = String.valueOf(error.getMessage());

            assertFalse(messages.contains(message),
                        "The " + serviceName + " engine service reported a refusal identical to another engine" +
                                " service's, so its message does not say which service refused: " + message);

            messages.add(message);
        }

        assertEquals(4, messages.size(), "All four engine services should have been asked");
    }


    /**
     * Ask the governance action engine service to validate a connector.
     *
     * @param connectorProviderClassName class to validate
     * @return the service's report
     * @throws Exception the service refused or could not be reached
     */
    private static ConnectorReport validateThroughGovernanceAction(String connectorProviderClassName) throws Exception
    {
        return new GovernanceActionEngineClient(OMAGPlatformExtension.getPlatformURLRoot(),
                                                OMAGPlatformExtension.ENGINE_HOST_NAME,
                                                ServerFvtTestSupport.SECRETS_STORE_PROVIDER,
                                                OMAGPlatformExtension.getUserDirectoryPath().toString(),
                                                OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                                OMAGPlatformExtension.USER_ID,
                                                null).validateConnector(connectorProviderClassName);
    }


    /**
     * Ask the survey action engine service to validate a connector.
     *
     * @param connectorProviderClassName class to validate
     * @return the service's report
     * @throws Exception the service refused or could not be reached
     */
    private static ConnectorReport validateThroughSurveyAction(String connectorProviderClassName) throws Exception
    {
        return new SurveyActionClient(OMAGPlatformExtension.getPlatformURLRoot(),
                                      OMAGPlatformExtension.ENGINE_HOST_NAME,
                                      ServerFvtTestSupport.SECRETS_STORE_PROVIDER,
                                      OMAGPlatformExtension.getUserDirectoryPath().toString(),
                                      OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                      OMAGPlatformExtension.USER_ID,
                                      null).validateConnector(connectorProviderClassName);
    }


    /**
     * Ask the watchdog action engine service to validate a connector.
     *
     * @param connectorProviderClassName class to validate
     * @return the service's report
     * @throws Exception the service refused or could not be reached
     */
    private static ConnectorReport validateThroughWatchdogAction(String connectorProviderClassName) throws Exception
    {
        return new WatchdogActionClient(OMAGPlatformExtension.getPlatformURLRoot(),
                                        OMAGPlatformExtension.ENGINE_HOST_NAME,
                                        ServerFvtTestSupport.SECRETS_STORE_PROVIDER,
                                        OMAGPlatformExtension.getUserDirectoryPath().toString(),
                                        OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                        OMAGPlatformExtension.USER_ID,
                                        null).validateConnector(connectorProviderClassName);
    }


    /**
     * Ask the repository governance engine service to validate a connector.
     *
     * @param connectorProviderClassName class to validate
     * @return the service's report
     * @throws Exception the service refused or could not be reached
     */
    private static ConnectorReport validateThroughRepositoryGovernance(String connectorProviderClassName) throws Exception
    {
        return new RepositoryGovernanceClient(OMAGPlatformExtension.getPlatformURLRoot(),
                                              OMAGPlatformExtension.ENGINE_HOST_NAME,
                                              ServerFvtTestSupport.SECRETS_STORE_PROVIDER,
                                              OMAGPlatformExtension.getUserDirectoryPath().toString(),
                                              OMAGPlatformExtension.CLIENT_TOKEN_COLLECTION,
                                              OMAGPlatformExtension.USER_ID,
                                              null).validateConnector(connectorProviderClassName);
    }
}
