/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.clientfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextClientBase;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ClientCoverageFVT is the guard that keeps the rest of this suite honest.
 * <br>
 * Unlike type-fvt, whose coverage follows the type system automatically, the client tests here have to be
 * written by hand: every client has its own method names, its own properties bean and its own idea of what
 * a sensible instance looks like.  That means coverage can silently fall behind the code - a client gets
 * added to {@link ConnectorContextBase}, nobody remembers this module, and the suite carries on passing
 * while testing less than it claims to.
 * <br>
 * This class closes that gap.  It reflects over every client the connector context hands out and checks that
 * {@link ClientCatalog} accounts for each one - either as a client under test, or as a deliberate exclusion
 * with a stated reason.  A new client therefore cannot be added without this suite failing and saying so.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ClientCoverageFVT
{
    /**
     * Every client the connector context exposes must appear in the catalog.
     */
    @Test
    void everyConnectorContextClientIsAccountedFor()
    {
        Set<String>  contextClients = clientTypesOnTheConnectorContext();
        List<String> unaccountedFor = new ArrayList<>();

        for (String clientName : contextClients)
        {
            if (! ClientCatalog.accountsFor(clientName))
            {
                unaccountedFor.add(clientName);
            }
        }

        assertTrue(unaccountedFor.isEmpty(),
                   "The connector context exposes clients that ClientCatalog does not mention: " + unaccountedFor
                           + ".  Add each one to ClientCatalog - as a client under test, or as an exclusion with a"
                           + " reason - and give it coverage in the matching test class.");
    }


    /**
     * The catalog must not name a client that no longer exists.  An entry for a client that has been renamed
     * or removed is dead weight that hides nothing, and is invisible unless something checks.
     */
    @Test
    void everyCatalogEntryStillNamesARealClient()
    {
        Set<String>  contextClients = clientTypesOnTheConnectorContext();
        List<String> stale          = new ArrayList<>();

        for (String clientName : ClientCatalog.allNamedClients())
        {
            if (! contextClients.contains(clientName))
            {
                stale.add(clientName);
            }
        }

        assertTrue(stale.isEmpty(),
                   "ClientCatalog names clients the connector context no longer has: " + stale
                           + ".  Remove the entries.");
    }


    /**
     * Every client the catalog says is under test must actually be reachable from a live connector context -
     * a getter that returns null would leave its tests silently doing nothing.
     *
     * @throws Exception problem building the connector context
     */
    @Test
    void everyClientUnderTestCanBeObtained() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();

        for (Method method : ConnectorContextBase.class.getMethods())
        {
            if (! isClientGetter(method)) continue;

            String clientName = method.getReturnType().getSimpleName();

            if (! ClientCatalog.isUnderTest(clientName)) continue;

            Object client = method.invoke(connectorContext);

            assertNotNull(client, clientName + " is listed as under test but " + method.getName()
                                  + "() returned null on a live connector context");
        }
    }


    /**
     * Return the simple names of every client type the connector context hands out through a no-argument
     * getter.  {@code OpenMetadataStore} and {@code OpenMetadataTypesClient} are included: they do not extend
     * {@link ConnectorContextClientBase} but they are still clients a connector is given and uses.
     *
     * @return client type names
     */
    private static Set<String> clientTypesOnTheConnectorContext()
    {
        Set<String> names = new TreeSet<>();

        for (Method method : ConnectorContextBase.class.getMethods())
        {
            if (isClientGetter(method))
            {
                names.add(method.getReturnType().getSimpleName());
            }
        }

        return names;
    }


    /**
     * A client getter is a public, no-argument {@code getXxx()} whose return type is one of the connector
     * context client classes - which is what distinguishes it from the context's own accessors for strings
     * such as {@code getMyUserId()}.
     *
     * @param method method to test
     * @return true if this method hands out a client
     */
    private static boolean isClientGetter(Method method)
    {
        if ((method.getParameterCount() != 0)
                    || (! Modifier.isPublic(method.getModifiers()))
                    || (! method.getName().startsWith("get")))
        {
            return false;
        }

        Class<?> returnType = method.getReturnType();

        return ConnectorContextClientBase.class.isAssignableFrom(returnType)
                       || "OpenMetadataStore".equals(returnType.getSimpleName())
                       || "OpenMetadataTypesClient".equals(returnType.getSimpleName());
    }
}
