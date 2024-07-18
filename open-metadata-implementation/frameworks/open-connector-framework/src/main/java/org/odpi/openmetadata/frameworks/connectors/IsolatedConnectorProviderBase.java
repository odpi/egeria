/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * IsolatedConnectorProviderBase provides extensions to ConnectorProviderBase that uses a custom class loader to
 * load the connector class in getConnector.  This custom class loader give preference to the classes in the
 * same JAR file as the connector provider's implementation over other classes on the class path.
 * Note:
 * this process assumes that the connector class is not already loaded through a reference in the connector providers
 * implementation - ie private static final String connectorClassName should be set to a literal string.
 *
 */
public class IsolatedConnectorProviderBase extends ConnectorProviderBase
{

    /**
     * Use a custom class loader to favour classes that are located in the connector implementation's JAR file.
     *
     * @return class
     * @throws ClassNotFoundException unable to locate a class by that name on the class path
     */
    protected Class<?> getClassForConnector() throws ClassNotFoundException
    {
        try
        {
            IsolatedConnectorClassLoader isolatedConnectorClassLoader = new IsolatedConnectorClassLoader(this.getJARFileURL(this.getClass()));

            // return Class.forName(getConnectorClassName(), true, isolatedConnectorClassLoader);

            return isolatedConnectorClassLoader.loadClass(getConnectorClassName());
        }
        catch (IOException error)
        {
            throw new ClassNotFoundException("Bad provider class name", error);
        }
    }


    /**
     * Extract the name of the JAR file from the provider's class.
     *
     * @param providerClass class for this connector provider
     * @return JAR file name in URL form
     * @throws MalformedURLException should not happen - this means the class was not loaded from a JAR file.
     */
    private String getJARFileURL(Class<?> providerClass) throws MalformedURLException
    {
        URL qualifiedClassURL = providerClass.getResource(providerClass.getSimpleName() + ".class");

        if (qualifiedClassURL != null)
        {
            String qualifiedClassName = qualifiedClassURL.toString();
            String noClassQualifiedName = qualifiedClassName.split("!")[0];
            String[] noJarQualifiedNameSplit = noClassQualifiedName.split("jar:file:");

            String noJarQualifiedName = noJarQualifiedNameSplit[noJarQualifiedNameSplit.length - 1];

            return noJarQualifiedName;
        }

        return null;
    }


    /**
     * Return the path name of the Connector Provider's JAR file.
     *
     * @return file name of this connector provider's jar file
     */
    private String getMyJARFilePath()
    {
        try
        {
            return byGetProtectionDomain(this.getClass());
        }
        catch (Exception error)
        {
            // Cannot get jar file path using byGetProtectionDomain because the runtime does not permit it
        }

        return byGetResource(this.getClass());
    }



    /**
     * This method
     * @param providerClass
     * @return
     * @throws URISyntaxException
     */
    String byGetProtectionDomain(Class<?> providerClass) throws URISyntaxException
    {
        URL url = providerClass.getProtectionDomain().getCodeSource().getLocation();
        return Paths.get(url.toURI()).toString();
    }

    String byGetResource(Class<?> providerClass)
    {
        final URL classResource = providerClass.getResource(providerClass.getSimpleName() + ".class");
        if (classResource == null)
        {
            throw new RuntimeException("class resource is null");
        }

        final String url = classResource.toString();
        if (url.startsWith("jar:file:"))
        {
            // extract 'file:......jarName.jar' part from the url string
            String path = url.replaceAll("^jar:(file:.*[.]jar)!/.*", "$1");
            try
            {
                return Paths.get(new URI(path)).toString();
            }
            catch (Exception error)
            {
                throw new RuntimeException("Invalid Jar File URL String", error);
            }
        }

        throw new RuntimeException("Invalid Jar File URL String");
    }
}
