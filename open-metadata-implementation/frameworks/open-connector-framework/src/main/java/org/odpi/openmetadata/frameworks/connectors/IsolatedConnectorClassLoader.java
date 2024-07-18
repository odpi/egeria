/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors;


import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

/**
 * IsolatedConnectorClassLoader is used by a connector provider to create a connector instance that uses class
 * implementations from its own JAR file rather than any class implementations that may have already been loaded.
 */
public class IsolatedConnectorClassLoader extends URLClassLoader
{
    private final ClassLoader defaultClassLoader;
    private final JarFile     jarfile;
    private final String jarFileName;
    private final String jarFileSpec;


    public IsolatedConnectorClassLoader(String jarFileName) throws IOException
    {
        this(jarFileName,
             Thread.currentThread().getContextClassLoader().getParent(),
             Thread.currentThread().getContextClassLoader());
    }


    /**
     * Creates a new class loader of the specified name and using the
     * specified parent class loader for delegation.
     *
     * @param jarFileName name of the jar file to load from
     * @param jdkClassLoader the parent class loader for JRE classes
     * @param defaultClassLoader the class loader to use if can not find class in JAR.
     */
    protected IsolatedConnectorClassLoader(String       jarFileName,
                                           ClassLoader jdkClassLoader,
                                           ClassLoader defaultClassLoader) throws IOException
    {
        super(new URL[]{}, jdkClassLoader);
        this.defaultClassLoader = defaultClassLoader;
        this. jarFileName = jarFileName;
        this.jarfile = new JarFile(jarFileName);
        super.addURL(new File(jarFileName).toURI().toURL());

        this.jarFileSpec = "jar:file:" + jarFileName + "!/";
        super.addURL(new URL(jarFileSpec));
    }


    /**
     * This loads classes from the JDK, then the JAR file, then the default class loader.
     *
     * @param name The <a href="#binary-name">binary name</a> of the class
     *
     * @return loaded class
     * @throws ClassNotFoundException not found on the class path
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException
    {
        try
        {
            return super.findClass(name);
        }
        catch (ClassNotFoundException notFound)
        {
            return defaultClassLoader.loadClass(name);
        }
    }
}
