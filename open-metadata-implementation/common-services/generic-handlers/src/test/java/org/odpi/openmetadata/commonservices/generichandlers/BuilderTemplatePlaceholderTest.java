/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.mockito.Mockito;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

/**
 * The builder converts the resolved value of a numeric template property back to a number.  When that value is not a
 * number, the request fails with an InvalidParameterException naming the property, rather than an unchecked
 * NumberFormatException that reached the caller as a server error.
 * <br>
 * PrimitivePropertyValue's setters reject a string in an int, long or date property, so values built through them
 * always resolve to digits.  The tests use a property value that reports its value as a string directly - as a value
 * built without those setters (by a repository connector, say) would - to reach the conversion.
 */
public class BuilderTemplatePlaceholderTest
{
    private static final String PROPERTY_NAME = "count";


    @Test
    public void testNumericPlaceholderIsConverted() throws InvalidParameterException
    {
        OMRSRepositoryHelper          repositoryHelper = Mockito.mock(OMRSRepositoryHelper.class);
        OpenMetadataAPIGenericBuilder builder          = getBuilder(repositoryHelper);

        builder.replacePropertiesWithPlaceholders(getTemplateProperties(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT),
                                                  Map.of("size", " 42 "));

        Mockito.verify(repositoryHelper).addIntPropertyToInstance(any(), any(), eq(PROPERTY_NAME), eq(42), any());
    }


    @Test
    public void testDatePlaceholderIsConverted() throws InvalidParameterException
    {
        OMRSRepositoryHelper          repositoryHelper = Mockito.mock(OMRSRepositoryHelper.class);
        OpenMetadataAPIGenericBuilder builder          = getBuilder(repositoryHelper);

        builder.replacePropertiesWithPlaceholders(getTemplateProperties(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE),
                                                  Map.of("size", "1761350400000"));

        Mockito.verify(repositoryHelper).addDatePropertyToInstance(any(), any(), eq(PROPERTY_NAME), eq(new Date(1761350400000L)), any());
    }


    @Test
    public void testIntPlaceholderThatIsNotANumber()
    {
        checkNotANumber(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT, "int");
    }


    @Test
    public void testLongPlaceholderThatIsNotANumber()
    {
        checkNotANumber(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG, "long");
    }


    @Test
    public void testDatePlaceholderThatIsNotANumber()
    {
        checkNotANumber(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE, "date");
    }


    private static void checkNotANumber(PrimitiveDefCategory category, String typeName)
    {
        OpenMetadataAPIGenericBuilder builder = getBuilder(Mockito.mock(OMRSRepositoryHelper.class));

        InvalidParameterException error = expectThrows(InvalidParameterException.class,
                                                       () -> builder.replacePropertiesWithPlaceholders(getTemplateProperties(category),
                                                                                                       Map.of("size", "large")));

        assertEquals(error.getReportedErrorMessageId(), "OMAG-GENERIC-HANDLERS-400-017");
        assertEquals(error.getParameterName(), "placeholderProperties");
        assertTrue(error.getReportedErrorMessage().contains(PROPERTY_NAME), error.getReportedErrorMessage());
        assertTrue(error.getReportedErrorMessage().contains(typeName), error.getReportedErrorMessage());
        assertTrue(error.getReportedErrorMessage().contains("large"), error.getReportedErrorMessage());
    }


    private static InstanceProperties getTemplateProperties(PrimitiveDefCategory category)
    {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue()
        {
            @Override
            public String valueAsString()
            {
                return "~{size}~";
            }
        };

        propertyValue.setPrimitiveDefCategory(category);

        InstanceProperties properties = new InstanceProperties();

        properties.setProperty(PROPERTY_NAME, propertyValue);

        return properties;
    }


    private static OpenMetadataAPIGenericBuilder getBuilder(OMRSRepositoryHelper repositoryHelper)
    {
        return new OpenMetadataAPIGenericBuilder("typeGUID",
                                                 "TestElement",
                                                 null,
                                                 null,
                                                 repositoryHelper,
                                                 "testService",
                                                 "testServer");
    }
}
