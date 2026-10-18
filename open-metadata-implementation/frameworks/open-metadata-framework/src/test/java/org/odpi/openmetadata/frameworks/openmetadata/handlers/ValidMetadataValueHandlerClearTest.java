/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertThrows;

/**
 * ValidMetadataValueHandlerClearTest covers the three clear methods for valid metadata values.  Each one used to
 * delete the value it found and then throw "not found" anyway, and the map-value clear looked the value up
 * without its map name so it never found anything to delete.
 */
public class ValidMetadataValueHandlerClearTest
{
    private static final String USER_ID       = "testUser";
    private static final String TYPE_NAME     = "Asset";
    private static final String PROPERTY_NAME = "testProperty";

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private OpenMetadataClient        openMetadataClient;
    private ValidMetadataValueHandler handler;


    @BeforeMethod
    public void setUp() throws Exception
    {
        openMetadataClient = mock(OpenMetadataClient.class);

        when(openMetadataClient.getMaxPagingSize()).thenReturn(500);

        handler = new ValidMetadataValueHandler("testServer", null, "testService", openMetadataClient);
    }


    private OpenMetadataElement element(String guid, String mapName, String preferredValue)
    {
        OpenMetadataElement element = new OpenMetadataElement();

        ElementProperties properties = propertyHelper.addStringProperty(null, OpenMetadataProperty.USAGE.name, TYPE_NAME);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.PREFERRED_VALUE.name, preferredValue);

        if (mapName != null)
        {
            properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.NAMESPACE_PATH.name, mapName);
        }

        element.setElementGUID(guid);
        element.setElementProperties(properties);

        return element;
    }


    private void stored(OpenMetadataElement element, String mapName, String preferredValue) throws Exception
    {
        when(openMetadataClient.getMetadataElementByUniqueName(eq(USER_ID),
                                                               eq(OpenMetadataValidValues.constructValidValueQualifiedName(TYPE_NAME, PROPERTY_NAME, mapName, preferredValue)),
                                                               anyString(),
                                                               any())).thenReturn(element);
    }


    @Test
    public void clearingAValueThatExistsDeletesItWithoutAnError() throws Exception
    {
        stored(element("alpha-guid", null, "alpha"), null, "alpha");

        handler.clearValidMetadataValue(USER_ID, TYPE_NAME, PROPERTY_NAME, "alpha", new DeleteOptions());

        verify(openMetadataClient).deleteMetadataElementInStore(eq(USER_ID), eq("alpha-guid"), any());
    }


    @Test
    public void clearingAValueThatDoesNotExistIsAnError() throws Exception
    {
        assertThrows(InvalidParameterException.class,
                     () -> handler.clearValidMetadataValue(USER_ID, TYPE_NAME, PROPERTY_NAME, "missing", new DeleteOptions()));

        verify(openMetadataClient, never()).deleteMetadataElementInStore(anyString(), anyString(), any());
    }


    @Test
    public void clearingAMapValueUsesTheMapName() throws Exception
    {
        stored(element("v1-guid", "mapOne", "v1"), "mapOne", "v1");

        handler.clearValidMetadataMapValue(USER_ID, TYPE_NAME, PROPERTY_NAME, "mapOne", "v1", new DeleteOptions());

        verify(openMetadataClient).deleteMetadataElementInStore(eq(USER_ID), eq("v1-guid"), any());
    }


    @Test
    public void clearingAMapNameRemovesItsValues() throws Exception
    {
        stored(element("mapOne-guid", null, "mapOne"), null, "mapOne");

        when(openMetadataClient.getMetadataElementByUniqueName(eq(USER_ID),
                                                               eq(OpenMetadataValidValues.constructValidValueQualifiedName(null, PROPERTY_NAME, null, null)),
                                                               anyString(),
                                                               any())).thenReturn(element("parent-set-guid", null, null));

        RelatedMetadataElement mapValue = new RelatedMetadataElement();
        mapValue.setElement(element("v1-guid", "mapOne", "v1"));

        RelatedMetadataElement otherMapValue = new RelatedMetadataElement();
        otherMapValue.setElement(element("v2-guid", "mapTwo", "v2"));

        RelatedMetadataElementList members = new RelatedMetadataElementList();
        members.setElementList(List.of(mapValue, otherMapValue));

        when(openMetadataClient.getRelatedMetadataElements(eq(USER_ID), eq("parent-set-guid"), anyInt(), anyString(), any())).thenReturn(members);

        handler.clearValidMetadataMapName(USER_ID, TYPE_NAME, PROPERTY_NAME, "mapOne", new DeleteOptions());

        verify(openMetadataClient).deleteMetadataElementInStore(eq(USER_ID), eq("mapOne-guid"), any());
        verify(openMetadataClient).deleteMetadataElementInStore(eq(USER_ID), eq("v1-guid"), any());
        verify(openMetadataClient, never()).deleteMetadataElementInStore(eq(USER_ID), eq("v2-guid"), any());
    }
}
