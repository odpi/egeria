/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.clientfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * MessageURLFVT checks that the link to further reading that a message definition carries survives the
 * journey from the server that raised the message to the caller that has to act on it.
 * <br>
 * A message definition may supply a url - a link to the page describing the component or concept the message
 * is about.  The value is set on the exception as {@code reportedURL}, travels in the {@code exceptionURL}
 * of the REST response, and is put back on the exception that the client rebuilds.  That last step is the
 * one worth guarding: the client rebuilds an exception purely from the fields of the REST response, so if
 * the url were dropped anywhere along the chain - the message set, the exception, the response bean, or
 * either side of {@code RESTExceptionHandler} - the rebuilt exception would simply have a null url and
 * nothing else would look wrong.  A non-null url on an exception that was raised in the server is therefore
 * proof that the whole chain is intact.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class MessageURLFVT
{
    /**
     * A syntactically valid identifier that no element in the repository will have.  It has to be
     * well-formed, because the point is to get past the client's own parameter validation and fail in the
     * server - a malformed value would prove nothing about the REST round trip.
     */
    private static final String UNKNOWN_GUID = "11111111-2222-3333-4444-555555555555";

    /**
     * The prefixes that a link to further reading is allowed to use.
     */
    private static final String DOCS_SITE  = "https://egeria-project.org/";
    private static final String REPOSITORY = "https://github.com/odpi/egeria/";


    /**
     * Ask for an element that does not exist.  The server detects this, raises a message that has a link to
     * further reading, and returns it over REST.  The exception the client is handed should still carry the
     * link.
     *
     * @throws Exception an unexpected failure - which is the finding
     */
    @Test
    void serverSideFailureCarriesItsLinkBackToTheClient() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        try
        {
            openMetadataStore.getMetadataElementByGUID(UNKNOWN_GUID);

            fail("Retrieving unknown element " + UNKNOWN_GUID + " did not fail, so there is no exception to check");
        }
        catch (InvalidParameterException error)
        {
            /*
             * The message names the server and the access service that detected the problem, which is how
             * this test knows the exception came back over REST rather than being raised by the client.
             */
            assertNotNull(error.getReportedErrorMessage(), "The exception has no message text");
            assertTrue(error.getReportedErrorMessage().contains(OMAGPlatformExtension.SERVER_NAME),
                       "The exception was not raised by the server, so it does not exercise the REST round " +
                               "trip.  Its message was: " + error.getReportedErrorMessage());

            assertDefinitionAndLink(error, "an element that the server could not find");
        }
    }


    /**
     * Fail a call in the client itself, by asking for an element with no identifier at all.  The link comes
     * from the message definition directly rather than from a REST response, so this covers the path that
     * does not cross the wire.
     *
     * @throws Exception an unexpected failure - which is the finding
     */
    @Test
    void clientSideFailureCarriesItsLink() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        try
        {
            openMetadataStore.getMetadataElementByGUID("");

            fail("Retrieving an element with no identifier did not fail, so there is no exception to check");
        }
        catch (InvalidParameterException error)
        {
            assertDefinitionAndLink(error, "a request with no identifier");
        }
    }


    /**
     * The two failures are reported by different components, so they must not be handed the same link.  This
     * is what distinguishes a link that genuinely travels with its own message from one that is being
     * defaulted or echoed from somewhere else.
     *
     * @throws Exception an unexpected failure - which is the finding
     */
    @Test
    void differentMessagesCarryDifferentLinks() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        String serverSideMessageId = null;
        String serverSideURL       = null;
        String clientSideMessageId = null;
        String clientSideURL       = null;

        try
        {
            openMetadataStore.getMetadataElementByGUID(UNKNOWN_GUID);
        }
        catch (InvalidParameterException error)
        {
            serverSideMessageId = error.getReportedErrorMessageId();
            serverSideURL       = error.getReportedURL();
        }

        try
        {
            openMetadataStore.getMetadataElementByGUID("");
        }
        catch (InvalidParameterException error)
        {
            clientSideMessageId = error.getReportedErrorMessageId();
            clientSideURL       = error.getReportedURL();
        }

        assertNotNull(serverSideURL, "The server side failure came back without a link to further reading");
        assertNotNull(clientSideURL, "The client side failure has no link to further reading");

        assertNotEquals(serverSideMessageId, clientSideMessageId,
                        "The two failures reported the same message, so they cannot show that a link " +
                                "follows its own message");
        assertNotEquals(serverSideURL, clientSideURL,
                        "Two different messages were given the same link (" + serverSideURL + "), which " +
                                "suggests the link is not travelling with the message that it belongs to");
    }


    /**
     * Check that an exception was built from a message definition and that it kept the definition's link to
     * further reading.
     *
     * @param error the exception to check
     * @param situation what the caller did, used in the assertion messages
     */
    private void assertDefinitionAndLink(InvalidParameterException error,
                                         String                    situation)
    {
        String messageId = error.getReportedErrorMessageId();
        String url       = error.getReportedURL();

        assertNotNull(messageId,
                      "The exception reported for " + situation + " did not come from a message definition, " +
                              "so it cannot carry a link to further reading");

        assertNotNull(url,
                      "Message " + messageId + ", reported for " + situation + ", reached the caller without " +
                              "the link to further reading that its message definition supplies");

        assertTrue(url.startsWith(DOCS_SITE) || url.startsWith(REPOSITORY),
                   "Message " + messageId + " has a link to further reading that points outside the Egeria " +
                           "documentation site and repository: " + url);

        assertEquals(url.trim(), url,
                     "The link to further reading on message " + messageId + " has stray whitespace: [" + url + "]");
    }
}
