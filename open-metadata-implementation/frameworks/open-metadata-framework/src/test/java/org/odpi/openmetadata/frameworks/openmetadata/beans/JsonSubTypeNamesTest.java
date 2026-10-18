/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.beans;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashSet;
import java.util.Set;

/**
 * JsonSubTypeNamesTest checks that every bean is registered with Jackson under its own class name.
 * <br><br>
 * Callers name the bean they are sending in the "class" property, and they use the class name because that is
 * what the documentation and the .http files show.  A registration under any other name compiles cleanly and
 * round trips through JSON (the same mapper writes and reads the wrong name), so only a caller finds it - as an
 * InvalidTypeIdException.  RetentionProperties was registered as "RetentionProperties.class" this way.
 * Older names that callers may still send belong in the registration's {@code names} aliases.
 */
public class JsonSubTypeNamesTest
{
    @Test
    public void everySubTypeIsRegisteredUnderItsClassName()
    {
        SoftAssert    softAssert = new SoftAssert();
        Set<Class<?>> checked    = new HashSet<>();

        for (Class<?> beanClass : FrameworkBeans.beanClasses())
        {
            for (Class<?> candidate = beanClass; candidate != null && candidate != Object.class; candidate = candidate.getSuperclass())
            {
                JsonSubTypes subTypes = candidate.getDeclaredAnnotation(JsonSubTypes.class);

                if ((subTypes != null) && (checked.add(candidate)))
                {
                    for (JsonSubTypes.Type subType : subTypes.value())
                    {
                        softAssert.assertEquals(subType.name(),
                                                subType.value().getSimpleName(),
                                                candidate.getSimpleName() + " registers " + subType.value().getSimpleName() +
                                                        " under the name \"" + subType.name() + "\" - callers sending the class name will be refused");
                    }
                }
            }
        }

        softAssert.assertAll();
    }
}
