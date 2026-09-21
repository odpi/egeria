/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.testng.Assert.assertTrue;

/**
 * OpenMetadataBeanContractTest holds the framework's bean packages to the contract every caller of them
 * relies on: a bean survives a round trip through JSON, its copy constructor copies all of it, and its
 * {@code equals} and {@code hashCode} account for everything it holds.
 * <br><br>
 * There are over seven hundred of these beans and, until this class, three test files in the whole module.
 * They were not untested because nobody thought they should be - they were untested because the
 * hand-written style of bean test, where each one needs a filled-in instance, a clone and a different
 * instance written out by hand, does not go seven hundred ways.  So this test builds those three instances
 * by reflection instead, through {@link BeanValueFactory}, and applies the same assertions to every bean
 * {@link FrameworkBeans} can find.  A bean added tomorrow is covered tomorrow.
 * <br><br>
 * What this catches that nothing else does:
 * <ul>
 *     <li>A property that does not survive JSON - a missing annotation, a getter and setter that disagree
 *     about a name, a type Jackson cannot restore. The element is written, sent, and comes back with the
 *     value missing rather than with an error.</li>
 *     <li>A copy constructor that forgets a field. Converters copy beans constantly, and a forgotten field
 *     is a value that disappears somewhere between being read and being returned.</li>
 *     <li>An {@code equals} that ignores a field, so two elements that differ compare equal - which shows
 *     up as a de-duplication step discarding something it should have kept.</li>
 *     <li>A {@code hashCode} that disagrees with {@code equals}, so equal beans hash differently and both
 *     survive in a set.</li>
 * </ul>
 * None of these fails at compile time, and none raises an error at runtime - each one loses a value
 * quietly. Its companion in
 * {@code open-metadata-test/open-metadata-api-checks} catches the same family of faults statically, where
 * that is possible; this one catches what only running the bean can show.
 * <br><br>
 * Failures are collected with a {@link SoftAssert} so that one run reports every bean at fault rather than
 * stopping at the first, which is what makes this usable when a change touches a whole package.
 */
public class OpenMetadataBeanContractTest
{
    /**
     * Two seeds, so that a bean filled in with one differs in every field from the same bean filled in with
     * the other.  They differ in parity as well as in value, because a boolean has only two values to pick
     * between.
     */
    private static final int SEED_A = 2;
    private static final int SEED_B = 7;

    /**
     * The model is large and grows, so the factory in {@link BeanValueFactory} will always trail it a
     * little.  This is the share of setters it is allowed to leave alone before that stops being a gap and
     * starts being a hole - at which point the factory needs a case adding, not this number raising.
     */
    private static final double MAX_SKIPPED_SETTER_FRACTION = 0.05;

    private final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * Every bean survives being written to JSON and read back.
     */
    @Test
    public void everyBeanRoundTripsThroughJSON()
    {
        List<Class<?>>   beans      = beansUnderTest();
        BeanValueFactory valueFactory = new BeanValueFactory(beanClassNames(beans));
        SoftAssert       softAssert   = new SoftAssert();

        for (Class<?> beanClass : beans)
        {
            try
            {
                Object bean      = valueFactory.filledBean(beanClass, SEED_A);
                String json      = objectMapper.writeValueAsString(bean);
                Object roundTrip = objectMapper.readValue(json, beanClass);

                softAssert.assertEquals(roundTrip,
                                        bean,
                                        beanClass.getSimpleName() + " does not survive a round trip through JSON" +
                                                " - a property is being lost or changed on the way. JSON was: " + json);
            }
            catch (Exception error)
            {
                softAssert.fail(beanClass.getSimpleName() + " could not be round tripped through JSON: " +
                                        error.getClass().getSimpleName() + ": " + error.getMessage());
            }
        }

        softAssert.assertAll();
    }


    /**
     * Every bean with a copy constructor is fully copied by it.
     */
    @Test
    public void everyCopyConstructorCopiesTheWholeBean()
    {
        List<Class<?>>   beans        = beansUnderTest();
        BeanValueFactory valueFactory = new BeanValueFactory(beanClassNames(beans));
        SoftAssert       softAssert   = new SoftAssert();
        List<String>     noCopy       = new ArrayList<>();

        for (Class<?> beanClass : beans)
        {
            Constructor<?> copyConstructor = FrameworkBeans.copyConstructor(beanClass);

            if (copyConstructor == null)
            {
                noCopy.add(beanClass.getSimpleName());
                continue;
            }

            try
            {
                Object bean = valueFactory.filledBean(beanClass, SEED_A);
                Object copy = copyConstructor.newInstance(bean);

                if (! copy.equals(bean))
                {
                    softAssert.fail(beanClass.getSimpleName() + "'s copy constructor does not copy all of it - a" +
                                            " value is lost every time a converter copies this bean. Fields the copy" +
                                            " did not carry: " + differingFields(bean, copy));
                }
            }
            catch (Exception error)
            {
                softAssert.fail(beanClass.getSimpleName() + " could not be copied: " +
                                        error.getClass().getSimpleName() + ": " + error.getMessage());
            }
        }

        softAssert.assertAll();

        /*
         * A bean with no copy constructor at all is not asserted on here - a few of the type-marker
         * subclasses have none because they add no state of their own, and that is reasonable.  It is
         * reported so that the number cannot grow unnoticed.
         */
        System.out.println("Beans with no copy constructor (" + noCopy.size() + "): " + noCopy);
    }


    /**
     * Every bean's equality accounts for everything it holds: a bean equals a copy of itself and shares its
     * hash code, and does not equal a bean filled in differently.
     */
    @Test
    public void everyBeanEqualityUsesTheWholeBean()
    {
        List<Class<?>>   beans             = beansUnderTest();
        BeanValueFactory valueFactory      = new BeanValueFactory(beanClassNames(beans));
        SoftAssert       softAssert        = new SoftAssert();

        for (Class<?> beanClass : beans)
        {
            try
            {
                Object bean      = valueFactory.filledBean(beanClass, SEED_A);
                Object same      = valueFactory.filledBean(beanClass, SEED_A);
                Object different = valueFactory.filledBean(beanClass, SEED_B);

                softAssert.assertEquals(same, bean,
                                        beanClass.getSimpleName() + ": two beans filled in identically are not equal");

                softAssert.assertEquals(same.hashCode(), bean.hashCode(),
                                        beanClass.getSimpleName() + ": two equal beans have different hash codes," +
                                                " so both survive in a set and de-duplication fails");

                if (holdsState(beanClass))
                {
                    softAssert.assertNotEquals(different, bean,
                                               beanClass.getSimpleName() + ": two beans filled in differently compare" +
                                                       " equal, so equals() is ignoring at least one of its fields");
                }

                /*
                 * A bean's toString has to name the bean.  Thirty-three of them named a different class
                 * outright - copied from a sibling and never corrected - so a log line reporting a
                 * ClassificationProperties announced itself as a ClassificationBeanProperties.  That is
                 * actively misleading rather than merely unhelpful, which is why this is asserted.
                 */
                softAssert.assertTrue(bean.toString().contains(beanClass.getSimpleName()),
                                      beanClass.getSimpleName() + ": toString() does not name this bean, so a log" +
                                              " line or a diagnostic reports it as something it is not. It says: " +
                                              bean.toString().split("\\{")[0]);
            }
            catch (Exception error)
            {
                softAssert.fail(beanClass.getSimpleName() + " could not be compared: " +
                                        error.getClass().getSimpleName() + ": " + error.getMessage());
            }
        }

        softAssert.assertAll();
    }


    /**
     * The reflective driver still reaches most of the model.
     * <br><br>
     * The three tests above are only worth what the factory fills in.  A setter whose type the factory does
     * not make is left alone, and a bean whose fields are all left alone passes every assertion above
     * without testing anything.  This guards against that: if the share of untouched setters climbs, the
     * factory needs a case for the type that has started to matter.
     */
    @Test
    public void theReflectiveDriverStillReachesTheModel()
    {
        List<Class<?>>   beans        = beansUnderTest();
        BeanValueFactory valueFactory = new BeanValueFactory(beanClassNames(beans));

        for (Class<?> beanClass : beans)
        {
            try
            {
                valueFactory.filledBean(beanClass, SEED_A);
            }
            catch (Exception error)
            {
                // reported by the tests above
            }
        }

        int    populated = valueFactory.getPopulatedSetters();
        int    skipped   = valueFactory.getSkippedSetters();
        double fraction  = (double) skipped / (populated + skipped);

        System.out.println("Bean coverage: " + beans.size() + " beans, " + populated + " setters filled in, " +
                                   skipped + " left alone (" + Math.round(fraction * 100) + "%)");
        System.out.println("Types the value factory does not make: " + valueFactory.getSkippedTypes());

        assertTrue(fraction <= MAX_SKIPPED_SETTER_FRACTION,
                   "The value factory now leaves " + Math.round(fraction * 100) + "% of setters alone (" + skipped +
                           " of " + (populated + skipped) + "), which is above the " +
                           Math.round(MAX_SKIPPED_SETTER_FRACTION * 100) + "% this test is worth. Add a case to" +
                           " BeanValueFactory for the types it is skipping: " + valueFactory.getSkippedTypes());
    }



    /**
     * Does this bean hold any state at all, anywhere in its hierarchy?
     * <br><br>
     * A few do not - {@code OpenMetadataTypeDefElementHeader} is one, a base that exists to be inherited
     * from and declares nothing but a constant.  For those, two beans filled in with different values are
     * genuinely identical and genuinely equal, so asserting that they differ would be asserting something
     * false.
     *
     * @param beanClass bean to consider
     * @return true when it has at least one instance field
     */
    private boolean holdsState(Class<?> beanClass)
    {
        for (Class<?> declaringClass = beanClass;
             (declaringClass != null) && (declaringClass != Object.class);
             declaringClass = declaringClass.getSuperclass())
        {
            for (Field field : declaringClass.getDeclaredFields())
            {
                if (! Modifier.isStatic(field.getModifiers()))
                {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Report which of a bean's fields differ between two instances, so that a failure names the field to fix
     * rather than leaving it to be found by hand.  Declared fields are read directly, up the whole
     * hierarchy, because a field without a getter is exactly the kind that gets left out of a copy
     * constructor.
     *
     * @param expected the bean as it was built
     * @param actual the bean after whatever was done to it
     * @return the differing fields, named by the class that declares them
     */
    private String differingFields(Object expected, Object actual)
    {
        List<String> differences = new ArrayList<>();

        for (Class<?> declaringClass = expected.getClass();
             (declaringClass != null) && (declaringClass != Object.class);
             declaringClass = declaringClass.getSuperclass())
        {
            for (Field field : declaringClass.getDeclaredFields())
            {
                if (Modifier.isStatic(field.getModifiers()))
                {
                    continue;
                }

                try
                {
                    field.setAccessible(true);

                    if (! Objects.equals(field.get(expected), field.get(actual)))
                    {
                        differences.add(declaringClass.getSimpleName() + "." + field.getName());
                    }
                }
                catch (InaccessibleObjectException | ReflectiveOperationException error)
                {
                    differences.add(declaringClass.getSimpleName() + "." + field.getName() + " (unreadable)");
                }
            }
        }

        return differences.isEmpty() ? "none - the difference is in equals() itself, not in the fields"
                       : String.join(", ", differences);
    }

    /**
     * The beans this test covers, with a check that they were actually found.
     *
     * @return bean classes
     */
    private List<Class<?>> beansUnderTest()
    {
        List<Class<?>> beans = FrameworkBeans.beanClasses();

        assertTrue(beans.size() > 600,
                   "Expected to find the framework's beans - found " + beans.size() +
                           ". Has the source layout moved?");

        return beans;
    }


    /**
     * The names of the beans, so that the value factory knows which setter types it may build a nested bean
     * for.
     *
     * @param beans bean classes
     * @return fully qualified class names
     */
    private Set<String> beanClassNames(List<Class<?>> beans)
    {
        Set<String> names = new LinkedHashSet<>();

        for (Class<?> beanClass : beans)
        {
            names.add(beanClass.getName());
        }

        return names;
    }
}
