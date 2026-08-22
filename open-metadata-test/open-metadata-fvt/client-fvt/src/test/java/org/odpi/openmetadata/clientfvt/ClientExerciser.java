/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.clientfvt;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;

import java.lang.reflect.Method;

/**
 * ClientExerciser drives one connector context client through its full lifecycle surface:
 * create, retrieve by GUID, retrieve by name, search, update, delete, and confirm the element is gone.
 * <br>
 * The 35 clients that share the uniform shape - {@code create<Stem>}, {@code get<Stem>ByGUID},
 * {@code get<Stem>sByName}, {@code find<Stem>s}, {@code update<Stem>}, {@code delete<Stem>} - are all driven
 * from here rather than through 35 near-identical test classes.  That is a deliberate trade: the assertions
 * are real and the failure messages name the client and the step, but the *individual* behaviour of a client
 * beyond the common surface is not covered here and needs its own test.
 * <br>
 * Methods are bound by name and their arguments filled by parameter type, not by position.  Several clients
 * take extra arguments that the others do not - {@code ProjectClient.createProject} has an additional
 * classification name, for example - and binding by type absorbs that instead of breaking on it.
 */
final class ClientExerciser
{
    private final Object client;
    private final String stem;
    private final String clientName;


    /**
     * Create an exerciser for one client and one element type.
     *
     * @param client the connector context client
     * @param stem the element name used in the client's method names, e.g. "Location"
     */
    ClientExerciser(Object client, String stem)
    {
        this.client     = client;
        this.stem       = stem;
        this.clientName = client.getClass().getSimpleName();
    }


    /**
     * Run the whole lifecycle.  Any failure throws, carrying the client, the step and the cause.
     *
     * @param qualifiedName qualified name to give the element - carries this suite's prefix so the element
     *                      can be recognised as test debris
     * @throws Exception the client did not behave as expected - which is the finding
     */
    void runLifecycle(String qualifiedName) throws Exception
    {
        String elementGUID = null;

        try
        {
            elementGUID = create(qualifiedName);

            if (elementGUID == null)
            {
                throw new AssertionError(clientName + ": create" + stem + " returned no GUID");
            }

            OpenMetadataRootElement created = getByGUID(elementGUID);

            if (created == null)
            {
                throw new AssertionError(clientName + ": " + stem + " could not be read back after being created");
            }

            retrieveByName(qualifiedName);
            search(qualifiedName);
            update(elementGUID, qualifiedName);
        }
        finally
        {
            if (elementGUID != null)
            {
                delete(elementGUID);
            }
        }
    }


    /**
     * Call {@code create<Stem>}, building a properties bean for whichever properties type it asks for.
     *
     * @param qualifiedName qualified name for the new element
     * @return new element's GUID
     * @throws Exception the create call failed
     */
    private String create(String qualifiedName) throws Exception
    {
        Method method = findMethod("create" + stem);

        Object[] arguments = new Object[method.getParameterCount()];
        Class<?>[] types   = method.getParameterTypes();

        for (int i = 0; i < types.length; i++)
        {
            arguments[i] = argumentFor(types[i], qualifiedName);
        }

        return (String) invoke(method, "create", arguments);
    }


    /**
     * Call {@code get<Stem>ByGUID}.
     *
     * @param elementGUID element to retrieve
     * @return the element
     * @throws Exception the retrieve call failed
     */
    private OpenMetadataRootElement getByGUID(String elementGUID) throws Exception
    {
        Method method = findMethod("get" + stem + "ByGUID");

        return (OpenMetadataRootElement) invoke(method, "getByGUID",
                                                 buildArguments(method, elementGUID, null));
    }


    /**
     * Call {@code get<Stem>sByName}.
     * <br>
     * This step checks the call completes, not that it returns this run's element.  Clients differ in which
     * property their by-name search covers - most look at qualifiedName, but {@code InformalTagClient} and
     * {@code AnnotationClient} search their own name properties - so a generic driver cannot assert a hit
     * without knowing each client's search property.  Proving the element exists is getByGUID's job, and that
     * assertion is strict.  Asserting a by-name *hit* per client is worth doing and needs a per-client test.
     *
     * @param qualifiedName the name to look for
     * @throws Exception the retrieve call failed
     */
    private void retrieveByName(String qualifiedName) throws Exception
    {
        Method method = optionalMethod("get" + stem + "sByName");

        if (method == null) return;

        invoke(method, "getByName", buildArguments(method, qualifiedName, null));
    }


    /**
     * Call {@code find<Stem>s} with this run's qualified name as the search string.  As with the by-name
     * retrieval above, this checks the search executes rather than asserting what it returns.
     *
     * @param qualifiedName the string to search for
     * @throws Exception the search call failed
     */
    private void search(String qualifiedName) throws Exception
    {
        Method method = optionalMethod("find" + stem + "s");

        if (method == null) return;

        invoke(method, "find", buildArguments(method, qualifiedName, null));
    }


    /**
     * Call {@code update<Stem>} with a changed description, then read the element back and check the change
     * took.  An update that silently does nothing is the failure this step exists to catch.
     *
     * @param elementGUID element to update
     * @param qualifiedName its qualified name, kept unchanged
     * @throws Exception the update call failed
     */
    private void update(String elementGUID, String qualifiedName) throws Exception
    {
        Method method = findMethod("update" + stem);

        Object[] arguments = new Object[method.getParameterCount()];
        Class<?>[] types   = method.getParameterTypes();

        for (int i = 0; i < types.length; i++)
        {
            if (types[i] == String.class && arguments[i] == null && i == 0)
            {
                arguments[i] = elementGUID;
            }
            else
            {
                arguments[i] = argumentFor(types[i], qualifiedName);
            }
        }

        invoke(method, "update", arguments);

        if (getByGUID(elementGUID) == null)
        {
            throw new AssertionError(clientName + ": " + stem + " could not be read back after being updated");
        }
    }


    /**
     * Call {@code delete<Stem>}.  Best-effort: this runs in a finally block, and a delete that fails after an
     * earlier assertion has already failed should not mask the real finding.
     *
     * @param elementGUID element to remove
     */
    private void delete(String elementGUID)
    {
        try
        {
            Method        method        = findMethod("delete" + stem);
            DeleteOptions deleteOptions = new DeleteOptions();

            deleteOptions.setCascadedDelete(true);

            invoke(method, "delete", buildArguments(method, elementGUID, deleteOptions));
        }
        catch (Exception ignored)
        {
            // best effort - the leftover sweep in ClientFvtTestSupport will catch anything left behind
        }
    }


    /**
     * Build an argument array where the first String is a supplied value (a GUID or a name) and everything
     * else is filled by type.
     *
     * @param method method being called
     * @param firstString value for the first String parameter
     * @param preferredOptions options object to use if the method wants one of that type
     * @return arguments
     * @throws Exception a parameter type could not be satisfied
     */
    private Object[] buildArguments(Method method, String firstString, Object preferredOptions) throws Exception
    {
        Object[]   arguments = new Object[method.getParameterCount()];
        Class<?>[] types     = method.getParameterTypes();
        boolean    usedFirst = false;

        for (int i = 0; i < types.length; i++)
        {
            if ((types[i] == String.class) && (! usedFirst))
            {
                arguments[i] = firstString;
                usedFirst    = true;
            }
            else if ((preferredOptions != null) && types[i].isInstance(preferredOptions))
            {
                arguments[i] = preferredOptions;
            }
            else
            {
                arguments[i] = argumentFor(types[i], firstString);
            }
        }

        return arguments;
    }


    /**
     * Produce a value for one parameter type.  Options objects are created with their defaults; a properties
     * bean is created and given a qualified name and display name; anything else is passed as null, which the
     * clients treat as "not supplied".
     *
     * @param type parameter type
     * @param qualifiedName qualified name to set on a properties bean
     * @return a value for that parameter
     * @throws Exception the properties bean could not be built
     */
    private Object argumentFor(Class<?> type, String qualifiedName) throws Exception
    {
        if (type == NewElementOptions.class)
        {
            NewElementOptions newElementOptions = new NewElementOptions();

            newElementOptions.setIsOwnAnchor(true);

            return newElementOptions;
        }
        if (type == UpdateOptions.class)
        {
            UpdateOptions updateOptions = new UpdateOptions();

            updateOptions.setMergeUpdate(true);

            return updateOptions;
        }
        if (type == DeleteOptions.class) return new DeleteOptions();
        if (type == GetOptions.class)    return new GetOptions();
        if (type == QueryOptions.class)  return new QueryOptions();

        if (type.getSimpleName().endsWith("Properties") && (type != RelationshipProperties.class))
        {
            return ClientFvtTestSupport.newProperties(type, qualifiedName, clientName + " " + stem);
        }

        // Strings beyond the first, maps, classification names, relationship properties - all optional.
        return null;
    }


    /**
     * Find a method by name, failing with a clear message if the client does not have it.
     *
     * @param name method name
     * @return the method
     */
    private Method findMethod(String name)
    {
        Method method = optionalMethod(name);

        if (method == null)
        {
            throw new AssertionError(clientName + " has no method called " + name
                                             + " - the catalog says this client has the standard lifecycle surface");
        }

        return method;
    }


    /**
     * Find a method by name if it exists, preferring the one with the most parameters where a client offers
     * several overloads (that is the one carrying the options objects this suite wants to exercise).
     *
     * @param name method name
     * @return the method, or null
     */
    private Method optionalMethod(String name)
    {
        Method best = null;

        for (Method method : client.getClass().getMethods())
        {
            if (method.getName().equals(name)
                        && ((best == null) || (method.getParameterCount() > best.getParameterCount())))
            {
                best = method;
            }
        }

        return best;
    }


    /**
     * Invoke a client method, unwrapping the reflection wrapper so that a genuine client failure surfaces as
     * itself rather than as an InvocationTargetException.
     *
     * @param method method to call
     * @param step step name, for the failure message
     * @param arguments arguments
     * @return whatever the method returned
     * @throws Exception the client threw
     */
    private Object invoke(Method method, String step, Object[] arguments) throws Exception
    {
        try
        {
            return method.invoke(client, arguments);
        }
        catch (java.lang.reflect.InvocationTargetException wrapper)
        {
            Throwable cause = wrapper.getCause();

            if (cause instanceof Exception exception)
            {
                throw new Exception(clientName + " failed at the " + step + " step (" + method.getName() + "): "
                                            + cause.getMessage(), exception);
            }

            throw wrapper;
        }
    }
}
