/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.clientfvt;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * ClientLifecycleFVT drives every client that has the standard lifecycle surface through it: create,
 * retrieve by GUID, retrieve by name, search, update, delete.
 * <br>
 * One test case per client and element type, named after it, so a failure names the client that broke rather
 * than just "a client broke".  The list comes from {@link ClientCatalog}, which
 * {@link ClientCoverageFVT} keeps in step with the connector context.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ClientLifecycleFVT
{
    /**
     * The clients with a standard lifecycle surface, as "ClientName:Stem" pairs.
     *
     * @return test case names
     */
    static List<String> lifecycleClients()
    {
        return ClientCatalog.lifecycleClients();
    }


    /**
     * Take one client through create, retrieve, search, update and delete.
     *
     * @param clientAndStem "ClientName:Stem", e.g. "LocationClient:Location"
     * @throws Exception any failure - which is the finding
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("lifecycleClients")
    void clientLifecycle(String clientAndStem) throws Exception
    {
        String clientName = clientAndStem.substring(0, clientAndStem.indexOf(':'));
        String stem       = clientAndStem.substring(clientAndStem.indexOf(':') + 1);

        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        Object               client           = clientFrom(connectorContext, clientName);

        assertNotNull(client, clientName + " could not be obtained from the connector context");

        new ClientExerciser(client, stem).runLifecycle(ClientFvtTestSupport.newQualifiedName(stem));
    }


    /**
     * Fetch a client from the connector context by its class name.
     *
     * @param connectorContext live context
     * @param clientName simple class name of the client
     * @return the client, or null if the context has no getter for it
     * @throws Exception the getter threw
     */
    private static Object clientFrom(ConnectorContextBase connectorContext,
                                     String               clientName) throws Exception
    {
        for (Method method : ConnectorContextBase.class.getMethods())
        {
            if ((method.getParameterCount() == 0)
                        && method.getName().startsWith("get")
                        && method.getReturnType().getSimpleName().equals(clientName))
            {
                return method.invoke(connectorContext);
            }
        }

        return null;
    }
}
