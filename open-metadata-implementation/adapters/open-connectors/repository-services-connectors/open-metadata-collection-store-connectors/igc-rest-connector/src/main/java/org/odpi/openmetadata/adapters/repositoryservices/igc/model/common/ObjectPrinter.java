/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Basic utility class to provide a standard mechanism through which to print out all of an asset's
 * details in a standard way that can be easily JSON-esque formatted.
 */
public abstract class ObjectPrinter {

    /**
     * Recurse up the class hierarchy to retrieve all fields that might hold data on an object.
     *
     * @param clazz starting point for recursive retrieval of Fields
     * @return ArrayList of all Fields
     */
    protected ArrayList<Field> getAllFields(Class clazz) {
        ArrayList<Field> al;
        Class superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            al = getAllFields(superClazz);
        } else {
            al = new ArrayList<Field>();
        }
        al.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return al;
    }

    /**
     * Provides a string representation of the object that can be easily read and formatted in JSON-esque fashion.
     *
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"" + getClass().getName() + "\"");
        sb.append(": { ");
        try {
            for (Field f : getAllFields(getClass())) {
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                sb.append("\"" + f.getName() + "\"");
                sb.append(": ");
                Object value = f.get(this);
                if (value == null) {
                    sb.append("\"null\"");
                } else if (Reference.isSimpleType(value)) {
                    sb.append("\"" + value + "\"");
                } else {
                    sb.append(value);
                }
                sb.append(", ");
            }
            // Get rid of the extra comma left at the end
            sb.deleteCharAt(sb.length() - 2);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        sb.append("}}");
        return sb.toString();
    }

}
