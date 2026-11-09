/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.surveyreports;

import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreConnector;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.controls.TabularDataSetConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataAuditCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyFolderAnnotationType;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.EgeriaConnectedAssetClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * MissingFileClassifiersTabularDataSetConnector lists the files that a folder survey could not classify using the
 * file reference data in the Core Content Pack.  The folder survey writes these files to a CSV file that is the
 * action target of its request for action annotation.  Since the annotation is reused by later surveys of the
 * same folder, only the CSV file from the most recent survey is read, and the rows are linked to the most recent
 * survey report that the annotation appears in.
 */
public class MissingFileClassifiersTabularDataSetConnector extends SurveyReportDataSetConnectorBase
{
    private static final String myConnectorName = MissingFileClassifiersTabularDataSetConnector.class.getName();

    /*
     * Columns of the CSV file written by the folder survey.
     */
    private static final int FILE_NAME_COLUMN                    = 0;
    private static final int FILE_EXTENSION_COLUMN               = 1;
    private static final int PATHNAME_COLUMN                     = 2;
    private static final int FILE_TYPE_COLUMN                    = 3;
    private static final int ASSET_TYPE_NAME_COLUMN              = 4;
    private static final int DEPLOYED_IMPLEMENTATION_TYPE_COLUMN = 5;
    private static final int ENCODING_COLUMN                     = 6;

    /*
     * Builds the connector to each CSV file from the asset's connection.  Created on first use.
     */
    private ConnectedAssetClient connectedAssetClient = null;


    /**
     * Default constructor
     */
    public MissingFileClassifiersTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.MISSING_FILE_CLASSIFIERS);
    }


    /**
     * One row per unclassified file listed in the CSV file of the most recent survey of each folder.
     *
     * @throws Exception problem retrieving the metadata or reading a CSV file
     */
    @Override
    protected void collectSurveyRows() throws Exception
    {
        Set<String> rowKeys = new HashSet<>();

        for (OpenMetadataRootElement annotation : getAnnotations(OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName,
                                                                 List.of(SurveyFolderAnnotationType.MISSING_REF_DATA.getName())))
        {
            MetadataElementSummary latestReport = getLatestReportForAnnotation(annotation);
            String                 csvAssetGUID = getLatestActionTargetGUID(annotation);

            if ((latestReport == null) || (csvAssetGUID == null))
            {
                continue;
            }

            String surveyReportGUID = latestReport.getElementHeader().getGUID();

            for (List<String> csvRecord : readCSVFile(csvAssetGUID))
            {
                String pathName = getColumn(csvRecord, PATHNAME_COLUMN);

                /*
                 * The survey report and pathname identify the row.  A file found by surveys of both a folder and
                 * its parent folder is only listed once.
                 */
                if ((pathName != null) && (rowKeys.add(surveyReportGUID + "::" + pathName)))
                {
                    Map<String, String> values = newValues();

                    putValue(values, ProductDataFieldDefinition.SURVEY_REPORT_GUID, surveyReportGUID);
                    putValue(values, ProductDataFieldDefinition.PATHNAME, pathName);
                    putValue(values, ProductDataFieldDefinition.FILE_NAME, getColumn(csvRecord, FILE_NAME_COLUMN));
                    putValue(values, ProductDataFieldDefinition.FILE_EXTENSION, getColumn(csvRecord, FILE_EXTENSION_COLUMN));
                    putValue(values, ProductDataFieldDefinition.FILE_TYPE, getColumn(csvRecord, FILE_TYPE_COLUMN));
                    putValue(values, ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME, getColumn(csvRecord, ASSET_TYPE_NAME_COLUMN));
                    putValue(values, ProductDataFieldDefinition.DEPLOYED_IMPLEMENTATION_TYPE, getColumn(csvRecord, DEPLOYED_IMPLEMENTATION_TYPE_COLUMN));
                    putValue(values, ProductDataFieldDefinition.ENCODING, getColumn(csvRecord, ENCODING_COLUMN));

                    addRow(annotation, values);
                }
            }
        }
    }


    /**
     * Return the unique identifier of the most recently created action target of the annotation.  Each survey
     * writes a new CSV file.
     *
     * @param annotation request for action annotation
     * @return guid or null
     */
    private String getLatestActionTargetGUID(OpenMetadataRootElement annotation)
    {
        String latestGUID = null;
        Date   latestTime = null;

        if (annotation.getRequestForActionTargets() != null)
        {
            for (RelatedMetadataElementSummary actionTarget : annotation.getRequestForActionTargets())
            {
                if ((actionTarget != null) && (actionTarget.getRelatedElement() != null))
                {
                    Date createTime = null;

                    if (actionTarget.getRelatedElement().getElementHeader().getVersions() != null)
                    {
                        createTime = actionTarget.getRelatedElement().getElementHeader().getVersions().getCreateTime();
                    }

                    if ((latestGUID == null) || ((createTime != null) && ((latestTime == null) || (createTime.after(latestTime)))))
                    {
                        latestGUID = actionTarget.getRelatedElement().getElementHeader().getGUID();
                        latestTime = createTime;
                    }
                }
            }
        }

        return latestGUID;
    }


    /**
     * Read the records of a CSV file catalogued as an asset.  A file that cannot be read (for example, because it
     * has been deleted) is logged and contributes no rows, so the other folders are still listed.
     *
     * @param csvAssetGUID unique identifier of the CSV file's asset
     * @return list of records
     */
    private List<List<String>> readCSVFile(String csvAssetGUID)
    {
        final String methodName = "readCSVFile";

        Connector connector = null;

        try
        {
            connector = getConnectedAssetClient().getConnectorForAsset(super.getClientUserId(), csvAssetGUID, auditLog);

            if (connector != null)
            {
                connector.start();

                if (connector instanceof ReadableTabularDataSource readableTabularDataSource)
                {
                    List<List<String>> records     = new ArrayList<>();
                    long               recordCount = readableTabularDataSource.getRecordCount();

                    for (long recordNumber = 0; recordNumber < recordCount; recordNumber ++)
                    {
                        List<String> csvRecord = readableTabularDataSource.readRecord(recordNumber);

                        if (csvRecord != null)
                        {
                            records.add(csvRecord);
                        }
                    }

                    return records;
                }

                return readCSVFileStore(connector);
            }
        }
        catch (Exception | LinkageError error)
        {
            /*
             * LinkageError: the CSV file connector is not installed alongside this connector.
             */
            logRecord(methodName, TabularDataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                 error.getClass().getName(),
                                                                                                 methodName + "(" + csvAssetGUID + ")",
                                                                                                 error.getMessage()));
        }
        finally
        {
            if (connector != null)
            {
                try
                {
                    connector.disconnect();
                }
                catch (Exception error)
                {
                    // the connector is being discarded; its complaint changes nothing
                }
            }
        }

        return List.of();
    }


    /**
     * Read the records through the CSV file store connector, which is how the folder survey catalogues the file.
     * This class is only resolved when such a connector is returned, so its absence surfaces as a LinkageError
     * for the caller to handle.
     *
     * @param connector connector to the CSV file
     * @return list of records (empty if the connector is not a CSV file store connector)
     * @throws Exception problem reading the file
     */
    private List<List<String>> readCSVFileStore(Connector connector) throws Exception
    {
        List<List<String>> records = new ArrayList<>();

        if (connector instanceof CSVFileStoreConnector csvFileStoreConnector)
        {
            long recordCount = csvFileStoreConnector.getRecordCount();

            for (long recordNumber = 0; recordNumber < recordCount; recordNumber ++)
            {
                List<String> csvRecord = csvFileStoreConnector.readRecord(recordNumber);

                if (csvRecord != null)
                {
                    records.add(csvRecord);
                }
            }
        }

        return records;
    }


    /**
     * Return the client that builds connectors for assets.  It talks to the same server as the metadata
     * client, with the same secrets.
     *
     * @return client
     * @throws Exception problem creating the client
     */
    private synchronized ConnectedAssetClient getConnectedAssetClient() throws Exception
    {
        if (connectedAssetClient == null)
        {
            String serverName  = super.getStringConfigurationProperty(TabularDataSetConfigurationProperty.SERVER_NAME.getName(), connectionBean.getConfigurationProperties());
            int    maxPageSize = super.getIntConfigurationProperty(TabularDataSetConfigurationProperty.MAX_PAGE_SIZE.getName(), connectionBean.getConfigurationProperties());

            connectedAssetClient = new EgeriaConnectedAssetClient(serverName,
                                                                  connectionBean.getEndpoint().getNetworkAddress(),
                                                                  secretsStoreConnectorMap,
                                                                  maxPageSize,
                                                                  auditLog);
        }

        return connectedAssetClient;
    }


    /**
     * Return a column of a CSV record, treating an empty value as null.
     *
     * @param csvRecord record
     * @param column column number
     * @return value or null
     */
    private static String getColumn(List<String> csvRecord, int column)
    {
        if ((csvRecord.size() > column) && (csvRecord.get(column) != null) && (! csvRecord.get(column).isBlank()))
        {
            return csvRecord.get(column);
        }

        return null;
    }
}
