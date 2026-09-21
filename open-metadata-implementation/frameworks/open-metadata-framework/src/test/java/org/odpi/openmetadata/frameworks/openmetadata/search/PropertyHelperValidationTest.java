/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.fail;

/**
 * PropertyHelperValidationTest covers the parameter validation that every service call passes through, and
 * the paging rules behind it.
 * <br><br>
 * The paging rules are the reason this class exists.  {@code pageSize} of zero does not mean "no results" -
 * it means "as many as the server allows", and {@link PropertyHelper#validatePaging} substitutes the
 * server's maximum.  That convention is what makes {@code defaultValue = "0"} correct on the REST APIs, and
 * it is easy to mistake for a defect and "fix", at which point every caller that left the page size alone
 * starts getting nothing back.  Pinning it here makes that change fail a test instead of a deployment.
 */
public class PropertyHelperValidationTest
{
    private static final String METHOD_NAME    = "testMethod";
    private static final int    MAX_PAGE_SIZE  = 1000;

    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * A page size of zero means "as much as there is", and is answered with the server's maximum.
     *
     * @throws InvalidParameterException not expected
     */
    @Test
    public void aPageSizeOfZeroMeansTheServerMaximum() throws InvalidParameterException
    {
        assertEquals(propertyHelper.validatePaging(0, 0, MAX_PAGE_SIZE, METHOD_NAME), MAX_PAGE_SIZE);

        QueryOptions queryOptions = new QueryOptions();

        assertEquals(queryOptions.getPageSize(), 0, "a page size of zero should be the default");
        assertEquals(propertyHelper.validatePaging(queryOptions, MAX_PAGE_SIZE, METHOD_NAME), MAX_PAGE_SIZE);
    }


    /**
     * Null query options are the same as default ones - a caller that supplies nothing gets the server
     * maximum rather than a failure.
     *
     * @throws InvalidParameterException not expected
     */
    @Test
    public void nullQueryOptionsMeanTheServerMaximum() throws InvalidParameterException
    {
        assertEquals(propertyHelper.validatePaging(null, MAX_PAGE_SIZE, METHOD_NAME), MAX_PAGE_SIZE);
    }


    /**
     * A page size the server can honour is returned unchanged.
     *
     * @throws InvalidParameterException not expected
     */
    @Test
    public void aPageSizeWithinTheMaximumIsUsedAsItIs() throws InvalidParameterException
    {
        assertEquals(propertyHelper.validatePaging(0, 50, MAX_PAGE_SIZE, METHOD_NAME), 50);
        assertEquals(propertyHelper.validatePaging(100, 1, MAX_PAGE_SIZE, METHOD_NAME), 1);
        assertEquals(propertyHelper.validatePaging(0, MAX_PAGE_SIZE, MAX_PAGE_SIZE, METHOD_NAME), MAX_PAGE_SIZE,
                     "a page size equal to the maximum is allowed - the check is greater than, not greater or equal");
    }


    /**
     * A server whose maximum page size is itself zero has no limit, so nothing is refused for being too big
     * and a page size of zero stays zero - there is no maximum to substitute.
     *
     * @throws InvalidParameterException not expected
     */
    @Test
    public void aServerMaximumOfZeroMeansNoLimit() throws InvalidParameterException
    {
        assertEquals(propertyHelper.validatePaging(0, 0, 0, METHOD_NAME), 0);
        assertEquals(propertyHelper.validatePaging(0, 5_000_000, 0, METHOD_NAME), 5_000_000);
    }


    /**
     * A page size beyond the server's maximum is refused rather than trimmed.  Trimming it would answer a
     * caller who asked for 5000 results with 1000 and no indication that the rest exist.
     */
    @Test
    public void aPageSizeBeyondTheMaximumIsRefused()
    {
        assertThrows(InvalidParameterException.class,
                     () -> propertyHelper.validatePaging(0, MAX_PAGE_SIZE + 1, MAX_PAGE_SIZE, METHOD_NAME));
    }


    /**
     * Negative paging values are refused, and the exception names the parameter at fault so the caller is
     * told which of the two it was.
     */
    @Test
    public void negativePagingValuesAreRefused()
    {
        try
        {
            propertyHelper.validatePaging(-1, 10, MAX_PAGE_SIZE, METHOD_NAME);
            fail("a negative startFrom should be refused");
        }
        catch (InvalidParameterException error)
        {
            assertEquals(error.getParameterName(), "startFrom");
        }

        try
        {
            propertyHelper.validatePaging(0, -1, MAX_PAGE_SIZE, METHOD_NAME);
            fail("a negative pageSize should be refused");
        }
        catch (InvalidParameterException error)
        {
            assertEquals(error.getParameterName(), "pageSize");
        }
    }


    /**
     * A null or empty user id, guid, mandatory name or search string is refused, and the exception names the
     * parameter the caller passed it as rather than the helper's own parameter name.
     */
    @Test
    public void nullAndEmptyMandatoryValuesAreRefused()
    {
        assertThrows(InvalidParameterException.class, () -> propertyHelper.validateUserId(null, METHOD_NAME));
        assertThrows(InvalidParameterException.class, () -> propertyHelper.validateUserId("", METHOD_NAME));

        assertThrows(InvalidParameterException.class, () -> propertyHelper.validateGUID(null, "assetGUID", METHOD_NAME));
        assertThrows(InvalidParameterException.class, () -> propertyHelper.validateGUID("", "assetGUID", METHOD_NAME));

        assertThrows(InvalidParameterException.class, () -> propertyHelper.validateMandatoryName(null, "name", METHOD_NAME));
        assertThrows(InvalidParameterException.class, () -> propertyHelper.validateMandatoryName("", "name", METHOD_NAME));

        assertThrows(InvalidParameterException.class, () -> propertyHelper.validateSearchString(null, "searchString", METHOD_NAME));
        assertThrows(InvalidParameterException.class, () -> propertyHelper.validateSearchString("", "searchString", METHOD_NAME));

        assertThrows(InvalidParameterException.class, () -> propertyHelper.validateObject(null, "properties", METHOD_NAME));

        try
        {
            propertyHelper.validateGUID(null, "assetGUID", METHOD_NAME);
            fail("a null guid should be refused");
        }
        catch (InvalidParameterException error)
        {
            assertEquals(error.getParameterName(), "assetGUID",
                         "the exception should name the caller's parameter, not the helper's");
        }
    }


    /**
     * An empty text field is allowed where an empty name is not.  A description may legitimately be blank,
     * so {@code validateText} only refuses null - the asymmetry with the methods above is deliberate.
     *
     * @throws InvalidParameterException not expected
     */
    @Test
    public void anEmptyTextFieldIsAllowedButANullOneIsNot() throws InvalidParameterException
    {
        propertyHelper.validateText("", "description", METHOD_NAME);
        propertyHelper.validateText("something", "description", METHOD_NAME);

        assertThrows(InvalidParameterException.class, () -> propertyHelper.validateText(null, "description", METHOD_NAME));
    }


    /**
     * A guid is checked for shape as well as for being present: only letters, digits, dashes and spaces are
     * allowed.  Anything built from a qualified name - which carries colons, dots and underscores - is
     * refused, so a caller that passes a name where a guid was wanted is told so rather than being sent to
     * the repository to find nothing.
     *
     * @throws InvalidParameterException not expected
     */
    @Test
    public void aGUIDIsCheckedForShape() throws InvalidParameterException
    {
        propertyHelper.validateGUID("3f8a5b21-4c7e-4a19-9d2b-6e0c1f7a8d34", "assetGUID", METHOD_NAME);
        propertyHelper.validateGUID("simpleIdentifier99", "assetGUID", METHOD_NAME);

        assertThrows(InvalidParameterException.class,
                     () -> propertyHelper.validateGUID("Egeria:Asset:widget-1", "assetGUID", METHOD_NAME));
        assertThrows(InvalidParameterException.class,
                     () -> propertyHelper.validateGUID("widget_1", "assetGUID", METHOD_NAME));
        assertThrows(InvalidParameterException.class,
                     () -> propertyHelper.validateGUID("widget.1", "assetGUID", METHOD_NAME));
    }
}
