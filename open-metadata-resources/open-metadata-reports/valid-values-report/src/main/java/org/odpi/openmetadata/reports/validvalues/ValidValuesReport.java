/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.validvalues;


import org.odpi.openmetadata.accessservices.digitalarchitecture.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.accessservices.digitalarchitecture.client.ReferenceDataManager;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ValidValueElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.http.HttpHelper;
import org.odpi.openmetadata.reports.EgeriaReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * ValidValuesReport extracts the valid values defined in the open metadata ecosystem.
 * It connects to a Metadata Access Server running the Digital Architecture OMAS.
 */
public class ValidValuesReport
{
    private final String       serverName;
    private final String       platformURLRoot;
    private final String       clientUserId;
    private final String       validValueRootName;
    private final EgeriaReport report;
    private final PropertyHelper propertyHelper = new PropertyHelper();

    /**
     * Set up the parameters for the sample.
     *
     * @param serverName server to call
     * @param platformURLRoot location of server
     * @param clientUserId userId to access the server
     * @param validValueRootName the qualified name of the root of the valid values of interest
     * @throws IOException problem writing file
     */
    private ValidValuesReport(String serverName,
                              String platformURLRoot,
                              String clientUserId,
                              String validValueRootName) throws IOException
    {
        final String reportFileName  = "valid-values-report-" + validValueRootName + ".md";

        this.serverName = serverName;
        this.platformURLRoot = platformURLRoot;
        this.clientUserId = clientUserId;
        this.validValueRootName = validValueRootName;

        report = new EgeriaReport(reportFileName);
    }


    /**
     * This runs the report.
     */
    private void run()
    {
        final String methodName = "run";

        int indentLevel = 0;

        try
        {
            /*
             * These clients are from the Digital Architecture OMAS.
             */
            ReferenceDataManager    referenceDataManager = new ReferenceDataManager(serverName, platformURLRoot, 100);
            OpenMetadataStoreClient openMetadataStoreClient = new OpenMetadataStoreClient(serverName, platformURLRoot, 100);

            /*
             * This outputs the report title
             */
            final String reportTitle = "Valid Values Report for: ";

            report.printReportTitle(indentLevel, reportTitle + validValueRootName);

            int detailIndentLevel = indentLevel + 1;

            report.printReportSubheading(detailIndentLevel, "Retrieved from " + serverName + "@" + platformURLRoot+ " on " + new Date());

            int startFrom = 0;
            int maxPageSize = 100;
            List<ValidValueInformation> validValueInformationList = new ArrayList<>();

            List<ValidValueElement> validValueElements = referenceDataManager.getValidValueByName(clientUserId, validValueRootName, startFrom, maxPageSize);

            while (validValueElements != null)
            {
                for (ValidValueElement validValueElement : validValueElements)
                {
                    this.gatherValidValues(validValueElement,
                                           validValueInformationList,
                                           1,
                                           referenceDataManager);
                }

                startFrom = startFrom + maxPageSize;

                validValueElements = referenceDataManager.getValidValueByName(clientUserId, validValueRootName, startFrom, maxPageSize);
            }

            for (ValidValueInformation validValueInformation : validValueInformationList)
            {
                report.printReportSubheading(validValueInformation.indentLevel,  validValueInformation.element.getValidValueProperties().getQualifiedName() );

                if (validValueInformation.element.getValidValueProperties().getPreferredValue() != null)
                {
                    report.printReportLine(validValueInformation.indentLevel + 1, "Preferred value", validValueInformation.element.getValidValueProperties().getPreferredValue());
                    report.printReportLine(validValueInformation.indentLevel + 1, "Description", validValueInformation.element.getValidValueProperties().getDescription());

                    startFrom = 0;
                    RelatedMetadataElementList relatedElements = openMetadataStoreClient.getRelatedMetadataElements(clientUserId,
                                                                                                                      validValueInformation.element.getElementHeader().getGUID(),
                                                                                                                      0,
                                                                                                                      null,
                                                                                                                      null,
                                                                                                                      null,
                                                                                                                      null,
                                                                                                                      SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                      false,
                                                                                                                      false,
                                                                                                                      new Date(),
                                                                                                                      startFrom,
                                                                                                                      500);

                    while ((relatedElements != null) && (relatedElements.getElementList() != null))
                    {
                        for (RelatedMetadataElement relatedElement : relatedElements.getElementList())
                        {
                            if (! propertyHelper.isTypeOf(relatedElement, OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName))
                            {
                                report.printReportLine(validValueInformation.indentLevel + 2, "Relationship type", relatedElement.getType().getTypeName());
                                if (relatedElement.getRelationshipProperties() != null)
                                {
                                    report.printReportSubheading(validValueInformation.indentLevel + 3, "Relationship properties:");

                                    for (String propertyName : relatedElement.getRelationshipProperties().getPropertiesAsStrings().keySet())
                                    {
                                        report.printReportLine(validValueInformation.indentLevel + 4, propertyName, relatedElement.getRelationshipProperties().getPropertiesAsStrings().get(propertyName));
                                    }
                                }

                                if (relatedElement.getElement().getElementProperties() != null)
                                {
                                    report.printReportSubheading(validValueInformation.indentLevel + 4, "Related" + relatedElement.getElement().getType().getTypeName() + "Element properties:");

                                    for (String propertyName : relatedElement.getElement().getElementProperties().getPropertiesAsStrings().keySet())
                                    {
                                        report.printReportLine(validValueInformation.indentLevel + 4, propertyName, relatedElement.getElement().getElementProperties().getPropertiesAsStrings().get(propertyName));
                                    }
                                }

                                if ((propertyHelper.isTypeOf(relatedElement, OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName)) ||
                                    (propertyHelper.isTypeOf(relatedElement, OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeName)))
                                {
                                    report.printReportSubheading(validValueInformation.indentLevel + 4, "Assigned reference values: ");

                                    int refDataStartFrom = 0;

                                    RelatedMetadataElementList relatedRefElements = openMetadataStoreClient.getRelatedMetadataElements(clientUserId,
                                                                                                                                       relatedElement.getElement().getElementGUID(),
                                                                                                                                       0,
                                                                                                                                       OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                                                                       null,
                                                                                                                                       null,
                                                                                                                                       null,
                                                                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                                       false,
                                                                                                                                       false,
                                                                                                                                       new Date(),
                                                                                                                                       refDataStartFrom,
                                                                                                                                       500);

                                    while ((relatedRefElements != null) && (relatedRefElements.getElementList() != null))
                                    {
                                        for (RelatedMetadataElement relatedRefElement : relatedRefElements.getElementList())
                                        {
                                            if (relatedRefElement != null)
                                            {
                                                String propertyType = propertyHelper.getStringProperty(reportTitle,
                                                                                                        OpenMetadataProperty.PROPERTY_TYPE.name,
                                                                                                        relatedRefElement.getRelationshipProperties(),
                                                                                                        methodName);

                                                report.printReportSubheading(validValueInformation.indentLevel + 5, "Assigned specification property: " + propertyType);

                                                if (relatedRefElement.getElement().getElementProperties() != null)
                                                {
                                                    for (String propertyName : relatedRefElement.getElement().getElementProperties().getPropertiesAsStrings().keySet())
                                                    {
                                                        report.printReportLine(validValueInformation.indentLevel + 6, propertyName, relatedRefElement.getElement().getElementProperties().getPropertiesAsStrings().get(propertyName));
                                                    }
                                                }
                                            }
                                        }

                                        refDataStartFrom = refDataStartFrom + 500;
                                        relatedRefElements = openMetadataStoreClient.getRelatedMetadataElements(clientUserId,
                                                                                                                validValueInformation.element.getElementHeader().getGUID(),
                                                                                                                0,
                                                                                                                OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                                                null,
                                                                                                                null,
                                                                                                                null,
                                                                                                                SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                false,
                                                                                                                false,
                                                                                                                new Date(),
                                                                                                                refDataStartFrom,
                                                                                                                500);
                                    }
                                }
                            }
                        }

                        startFrom = startFrom + 500;
                        relatedElements = openMetadataStoreClient.getRelatedMetadataElements(clientUserId,
                                                                                             validValueInformation.element.getElementHeader().getGUID(),
                                                                                             0,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                                                             false,
                                                                                             false,
                                                                                             new Date(),
                                                                                             startFrom,
                                                                                             500);
                    }
                }
            }

            report.closeReport();
        }
        catch (Exception error)
        {
            System.out.println("There was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
            System.exit(-1);
        }
    }


    /**
     * Iterate through the hierarchy of valid values to discover the tree structure of valid values.
     *
     * @param element last retrieved element
     * @param validValueInformationList current list of valid values
     * @param currentIndent current indent
     * @param referenceDataManager client
     */
    private void gatherValidValues(ValidValueElement           element,
                                   List<ValidValueInformation> validValueInformationList,
                                   int                         currentIndent,
                                   ReferenceDataManager        referenceDataManager) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        ValidValueInformation validValueInformation = new ValidValueInformation();

        validValueInformation.element = element;
        validValueInformation.indentLevel = currentIndent;
        validValueInformation.position = validValueInformationList.size();

        validValueInformationList.add(validValueInformation);

        if (propertyHelper.isTypeOf(element.getElementHeader(), OpenMetadataType.VALID_VALUE_SET.typeName))
        {
            int                     startFrom = 0;
            List<ValidValueElement> nestedElements = referenceDataManager.getValidValueSetMembers(clientUserId,
                                                                                                  element.getElementHeader().getGUID(),
                                                                                                  startFrom,
                                                                                                  100);

            while (nestedElements != null)
            {
                for (ValidValueElement nestedElement : nestedElements)
                {
                    gatherValidValues(nestedElement,
                                      validValueInformationList,
                                      currentIndent + 1,
                                      referenceDataManager);
                }

                startFrom = startFrom + 100;
                nestedElements = referenceDataManager.getValidValueSetMembers(clientUserId,
                                                                              element.getElementHeader().getGUID(),
                                                                              startFrom,
                                                                              100);
            }
        }
    }


    /**
     * Main program that controls the operation of the valid values report.  The parameters are passed space separated.
     * They are used to override the report's default values.
     *
     * @param args 1. service platform URL root, 2. client userId, 3. server name, 4. valid value set name
     */
    public static void main(String[] args)
    {
        String  serverName = "active-metadata-store";
        String  platformURLRoot = "https://localhost:9443";
        String  clientUserId = "erinoverview";
        String  validValueRoot = OpenMetadataValidValues.VALID_METADATA_VALUES_QUALIFIED_NAME_PREFIX;

        if (args.length > 0)
        {
            platformURLRoot = args[0];
        }

        if (args.length > 1)
        {
            clientUserId = args[1];
        }

        if (args.length > 2)
        {
            serverName = args[2];
        }

        if (args.length > 3)
        {
            validValueRoot = args[3];
        }

        System.out.println("===============================");
        System.out.println("Valid Values Report:    " + new Date());
        System.out.println("===============================");
        System.out.println("Running against platform: " + platformURLRoot);
        System.out.println("Focused on server: " + serverName);
        System.out.println("Using userId: " + clientUserId);
        System.out.println("Valid Value Root: " + validValueRoot);
        System.out.println();

        HttpHelper.noStrictSSL();

        try
        {
            ValidValuesReport report = new ValidValuesReport(serverName, platformURLRoot, clientUserId, validValueRoot);

            report.run();
        }
        catch (Exception  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }
    }


    /**
     * ValidValueInformation gathers information about a specific valid value.
     */
    static class ValidValueInformation
    {
        ValidValueElement element = null;
        int               indentLevel = 0;
        int               position = 0;
    }
}
