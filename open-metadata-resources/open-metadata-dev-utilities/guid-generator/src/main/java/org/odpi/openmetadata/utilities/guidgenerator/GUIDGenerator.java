/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.utilities.guidgenerator;

import java.util.*;

/**
 * ServerOps provides a utility for starting and stopping servers on an OMAG Server Platform.
 */
public class GUIDGenerator
{

    /**
     * Main program that controls the operation of the utility.  The parameters are passed space separated.
     * The parameters are used to override the utility's default values. If mode is set to "interactive"
     * the caller is prompted for a command and one to many server names.
     *
     * @param args 1. service platform URL root, 2. client userId, 3. mode 4. server name 5. server name ...
     */
    public static void main(String[] args)
    {
        System.out.println("==============================================");
        System.out.println(" GUID Generator: " + new Date() + " [" + new Date().getTime() + "L]");
        System.out.println("==============================================");

        for (int i=0; i<10 ; i++)
        {
            System.out.println(UUID.randomUUID());
        }

        System.exit(0);
    }
}
