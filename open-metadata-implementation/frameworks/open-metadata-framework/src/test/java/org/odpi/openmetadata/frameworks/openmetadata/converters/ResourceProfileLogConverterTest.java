/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementVersions;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceProfileLogAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Verify that a resource profile log annotation read back from the repository carries the GUIDs of its log files.
 * They are not stored with the annotation - each is a ResourceProfileData relationship to a log file - so they
 * have to be filled in from the relationships when the bean is built.
 */
public class ResourceProfileLogConverterTest
{
    private final PropertyHelper propertyHelper = new PropertyHelper();


    @Test
    public void testLogGUIDsComeFromResourceProfileData() throws Exception
    {
        OpenMetadataElement annotation = newElement("annotation-guid", OpenMetadataType.RESOURCE_PROFILE_LOG_ANNOTATION.typeName, "Annotation::profile-log");

        annotation.getType().setSuperTypeNames(List.of(OpenMetadataType.DATA_FIELD_ANNOTATION.typeName,
                                                       OpenMetadataType.ANNOTATION.typeName,
                                                       OpenMetadataType.AUTHORED_REFERENCEABLE.typeName,
                                                       OpenMetadataType.REFERENCEABLE.typeName,
                                                       OpenMetadataType.OPEN_METADATA_ROOT.typeName));
        OpenMetadataElement logFile    = newElement("log-file-guid", OpenMetadataType.CSV_FILE.typeName, "CSVFile::profile-log.csv");

        RelatedMetadataElement resourceProfileData = new RelatedMetadataElement();

        resourceProfileData.setRelationshipGUID("relationship-guid");
        resourceProfileData.setType(newType(OpenMetadataType.RESOURCE_PROFILE_DATA_RELATIONSHIP.typeName));
        resourceProfileData.setVersions(newVersions());
        resourceProfileData.setStatus(ElementStatus.ACTIVE);
        resourceProfileData.setElement(logFile);
        resourceProfileData.setElementAtEnd1(false);

        OpenMetadataRootConverter<OpenMetadataRootElement> converter = new OpenMetadataRootConverter<>(propertyHelper, "test", "testServer");

        OpenMetadataRootElement bean = converter.getNewComplexBean(OpenMetadataRootElement.class, annotation, List.of(resourceProfileData), "test");

        assertTrue(bean.getProperties() instanceof ResourceProfileLogAnnotationProperties, "Wrong properties class: " + bean.getProperties());
        assertEquals(((ResourceProfileLogAnnotationProperties) bean.getProperties()).getResourceProfileLogGUIDs(), List.of("log-file-guid"));
    }


    private OpenMetadataElement newElement(String guid, String typeName, String qualifiedName)
    {
        OpenMetadataElement element = new OpenMetadataElement();

        element.setElementGUID(guid);
        element.setType(newType(typeName));
        element.setVersions(newVersions());
        element.setStatus(ElementStatus.ACTIVE);
        element.setElementProperties(propertyHelper.addStringProperty(null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName));

        return element;
    }


    private ElementType newType(String typeName)
    {
        ElementType type = new ElementType();

        type.setTypeName(typeName);

        return type;
    }


    private ElementVersions newVersions()
    {
        ElementVersions versions = new ElementVersions();

        versions.setCreateTime(new Date());

        return versions;
    }
}
