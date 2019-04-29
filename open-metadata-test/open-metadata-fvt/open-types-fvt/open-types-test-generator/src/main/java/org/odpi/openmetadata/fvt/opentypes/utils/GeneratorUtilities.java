/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.fvt.opentypes.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;


/**
 * Generator utility methods.
 */
public class GeneratorUtilities {
    static public <T> T[] concatenate(T[] a, T[] b) {
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }

    public static String lowercase1stLetter(String end2Name) {
        return end2Name.substring(0, 1).toLowerCase() + end2Name.substring(1);
    }

    public static String uppercase1stLetter(String end1Name) {
        return end1Name.substring(0, 1).toUpperCase() + end1Name.substring(1);
    }

    public static String getRegexToken(String token) {
        return "[<]['$']['$']" + token + "['$']['$'][>]";
    }
    public static String getRegexStartAttrToken() {
        return "[<]['$']['A']['t']['t']['r']['$']['$']";
    }
    public static String getRegexEndAttrToken() {
        return "['$']['A']['t']['t']['r']['$']['$'][>]";
    }


    public static boolean recursivelyDelete(File path) throws FileNotFoundException {
        if (!path.exists()) throw new FileNotFoundException(path.getAbsolutePath());
        boolean ret = true;
        if (path.isDirectory()) {
            for (File f : path.listFiles()) {
                ret = ret && GeneratorUtilities.recursivelyDelete(f);
            }
        }
        return ret && path.delete();
    }

    public static File writeFolder(String outputFolder) {

        // create folder
        File newFolder = new File(outputFolder);

        if (!newFolder.exists()) {
            //System.out.println("creating new folder: " + newFolder.getName());
            newFolder.mkdirs();
        }
        return newFolder;
    }

    public static void createEmptyFolder(String generationFolder) throws FileNotFoundException {
        File folder = GeneratorUtilities.writeFolder(generationFolder);
        if (folder.exists()) {
            // clear out any existing generated files
            GeneratorUtilities.recursivelyDelete(folder);
            // make sure we create the required folder
            GeneratorUtilities.writeFolder(generationFolder);
        }
    }

    public static String getReferencePackage(String myType, String otherType) {
        return myType + "To" + otherType;
    }
    public static boolean isTopLevelGlossaryObject(String entityName) {
        boolean isTopLevelGlossaryObject = false;
        if (entityName.equals("Glossary") ||
                entityName.equals("GlossaryTerm") ||
                entityName.equals("GlossaryCategory" )
                ) {
            isTopLevelGlossaryObject = true;
        }
        return isTopLevelGlossaryObject;
    }

    /**
     * delete files in the folder apart from the exclude file name.
     * @param folder- folder under which to delete files
     * @param excludeFile this file name should not be deleted
     */
    public static void deleteFilesInFolder(String folder, String excludeFile) {
        System.err.println("folder="+folder);
        File[] files = new File(folder).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                if (!file.getName().equals(excludeFile)) {
                    file.delete();
                }
            }
        }
    }
    /**
     * delete files in the folder
     * @param folder- folder under which to delete files
     */
    public static void deleteFilesInFolder(String folder) {
        System.err.println("folder="+folder);
        File[] files = new File(folder).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }
}
