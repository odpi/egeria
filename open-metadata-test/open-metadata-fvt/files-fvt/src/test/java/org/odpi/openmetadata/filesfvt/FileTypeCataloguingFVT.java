/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.filesfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * FileTypeCataloguingFVT checks that the folder cataloguer picks a catalog template by the kind of file it
 * has found, rather than cataloguing everything the same way.
 * <br>
 * The Files content pack ships around twenty templates for individual file types - CSV, JSON, Avro, Parquet,
 * spreadsheets, source code, keystores - and the point of having them is that a catalogued file arrives as
 * the type it actually is.  A CSV file catalogued as a plain {@code DataFile} is not wrong in the way a
 * missing asset is wrong; it is worse than that, because everything downstream that keys off the type - which
 * survey to run, which connector opens it, which governance rules apply - quietly stops applying.
 * <br>
 * {@link org.odpi.openmetadata.templatesfvt.ContentPackTemplateFVT} in templates-fvt creates an element from
 * every one of those templates directly.  What is untested there, and tested here, is the step before that:
 * something has to <em>choose</em> the template, and that choice is made by the cataloguer from the file
 * itself.
 * <br>
 * The tree under test carries one file per extension for that reason, and the assertion names the type each
 * should arrive as.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class FileTypeCataloguingFVT
{
    private static final RequestTypeDefinition CREATE_FILE_FOLDER  = RequestTypeDefinition.CREATE_FILE_FOLDER;
    private static final RequestTypeDefinition CATALOG_FILE_FOLDER = RequestTypeDefinition.CATALOG_FILE_FOLDER;


    @Test
    @DisplayName("The folder cataloguer catalogues each file as the type it is")
    void testFilesAreCataloguedAsTheirOwnType() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore();

        File   folder        = FilesFvtTestSupport.folderUnderTest("filetypes");
        String qualifiedName = FilesFvtTestSupport.folderAssetQualifiedName(folder);

        String folderAssetGUID = null;

        try
        {
            assertTrue(folder.isDirectory(), "The folder under test was not built: " + folder.getAbsolutePath());

            Map<String, String> createParameters = new HashMap<>(FilesFvtTestSupport.folderTemplatePlaceholders(folder));

            String createActionGUID = new AutomatedCurationClient().initiateGovernanceActionType(
                    FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER),
                    createParameters,
                    null);

            new EngineActionWaiter().waitForCompletion(createActionGUID,
                                                        FilesFvtTestSupport.governanceActionTypeQualifiedName(CREATE_FILE_FOLDER));

            OpenMetadataElement folderAsset = FilesFvtTestSupport.waitForElement(openMetadataStore,
                                                                                 qualifiedName,
                                                                                 "the folder asset to catalogue");

            assertNotNull(folderAsset, "No folder asset called " + qualifiedName + " arrived in the repository.");

            folderAssetGUID = folderAsset.getElementGUID();

            String catalogActionGUID = new AutomatedCurationClient().initiateGovernanceActionType(
                    FilesFvtTestSupport.governanceActionTypeQualifiedName(CATALOG_FILE_FOLDER),
                    new HashMap<>(),
                    List.of(FilesFvtTestSupport.newActionTarget(ActionTarget.NEW_ASSET.getName(), folderAssetGUID)));

            new EngineActionWaiter().waitForCompletion(catalogActionGUID,
                                                        FilesFvtTestSupport.governanceActionTypeQualifiedName(CATALOG_FILE_FOLDER));

            OMAGPlatformExtension.getIntegrationDaemonClient().refreshConnectors();

            /*
             * Wait for every file to be catalogued rather than for the first, so that the assertion below
             * reports what each one arrived as instead of failing on whichever had not appeared yet.
             */
            Map<String, String> catalogued = new TreeMap<>();

            FilesFvtTestSupport.waitFor("the folder cataloguer to catalogue the files in " + folder.getName(),
                                         "files.fvt.refresh.timeout.seconds",
                                         180,
                                         () ->
                                         {
                                             catalogued.clear();
                                             catalogued.putAll(getCataloguedTypes(openMetadataStore, folder));

                                             return catalogued.keySet().containsAll(FilesFvtTestSupport.FILE_TYPE_EXPECTATIONS.keySet());
                                         });

            for (Map.Entry<String, String> expectation : FilesFvtTestSupport.FILE_TYPE_EXPECTATIONS.entrySet())
            {
                String fileName     = expectation.getKey();
                String expectedType = expectation.getValue();

                assertTrue(catalogued.containsKey(fileName),
                           "The folder cataloguer did not catalogue " + fileName + ".  It catalogued: " + catalogued);

                assertEquals(expectedType, catalogued.get(fileName),
                             "The folder cataloguer catalogued " + fileName + " as a " + catalogued.get(fileName)
                                     + " rather than a " + expectedType + ", so it did not choose the catalog template"
                                     + " the content pack ships for that kind of file.  Everything downstream that keys"
                                     + " off the type - which survey runs, which connector opens it - stops applying.");
            }
        }
        finally
        {
            if (folderAssetGUID != null)
            {
                FilesFvtTestSupport.purgeElement(openMetadataStore, folderAssetGUID);
            }
        }
    }


    /**
     * Return the type each file in the folder was catalogued as, keyed by file name.
     *
     * @param openMetadataStore store to read through
     * @param folder folder whose files should have been catalogued
     * @return file name to type name
     */
    private Map<String, String> getCataloguedTypes(OpenMetadataStore openMetadataStore,
                                                   File              folder)
    {
        Map<String, String> types = new TreeMap<>();

        try
        {
            SearchProperties searchProperties = new SearchProperties();

            searchProperties.setConditions(new PropertyHelper().addStringProperty(null,
                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                   folder.getName(),
                                                                                   PropertyComparisonOperator.LIKE));

            QueryOptions queryOptions = new QueryOptions();

            queryOptions.setMetadataElementTypeName(OpenMetadataType.DATA_FILE.typeName);
            queryOptions.setPageSize(FilesFvtTestSupport.MAX_PAGE_SIZE);

            List<OpenMetadataElement> found = openMetadataStore.findMetadataElements(searchProperties, null, queryOptions);

            if (found != null)
            {
                for (OpenMetadataElement element : found)
                {
                    String elementQualifiedName = FilesFvtTestSupport.getStringProperty(element,
                                                                                         OpenMetadataProperty.QUALIFIED_NAME.name);

                    if (elementQualifiedName != null)
                    {
                        String fileName = elementQualifiedName.substring(elementQualifiedName.lastIndexOf('/') + 1);

                        types.put(fileName, element.getType().getTypeName());
                    }
                }
            }
        }
        catch (Exception error)
        {
            /*
             * Read while the cataloguer is working, so a failure here is ordinary - the caller is polling.
             */
        }

        return types;
    }
}
