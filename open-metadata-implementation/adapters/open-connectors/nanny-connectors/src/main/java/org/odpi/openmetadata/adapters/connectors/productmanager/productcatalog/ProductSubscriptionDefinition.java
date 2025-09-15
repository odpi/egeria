/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.productmanager.productcatalog;


/**
 * The ProductSubscriptionDefinition describes the types of subscription supported by the open metadata product catalog.
 */
public enum ProductSubscriptionDefinition
{
    /**
     * Delivery to CSV File with updates pushed within 1 hour of publication to open metadata.
     */
    CSV_FILE("org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider",
             "CSV-FILE-REGULAR-REFRESH",
             "Delivery to CSV File with updates pushed within 1 hour of publication to open metadata.",
             "This subscription delivers data as a CSV File.  Each field is a separate column in the file and each row is a record from the data set.  The source metadata is monitored once an hour and if the content has changed, the CSV file is updated.",
             null),

    POSTGRES_TABLE("org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider",
             "POSTGRESQL-TABLE-REGULAR-REFRESH",
             "Delivery to a PostgreSQLTable with updates pushed within 1 hour of publication to open metadata.",
             "This subscription delivers data as a CSV File.  Each field is a separate column in the file and each row is a record from the data set.  The source metadata is monitored once an hour and if the content has changed, the CSV file is updated.",
             null),


    ;


    private final String connectorProviderClassName;
    private final String identifier;
    private final String displayName;
    private final String description;
    private final String category;


    ProductSubscriptionDefinition(String connectorProviderClassName,
                                  String identifier,
                                  String displayName,
                                  String description,
                                  String category)
    {
        this.connectorProviderClassName = connectorProviderClassName;
        this.identifier                 = identifier;
        this.displayName                = displayName;
        this.description                = description;
        this.category                   = category;
    }


    /**
     * Returns the unique name for the subject area entity.
     *
     * @return qualified name
     */
    public String getQualifiedName()
    {
        return "OpenMetadataProductCatalog::DigitalSubscription::" + identifier + "::" + displayName;
    }


    /**
     * Returns the unique name for the collection type.
     *
     * @return type name
     */
    public String getConnectorProviderClassName()
    {
        return connectorProviderClassName;
    }


    /**
     * Returns the optional classification.
     *
     * @return type name
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Returns a descriptive name of the collection.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Returns a detailed description of the contents of the collection.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Returns an optional category for the collection.
     *
     * @return string
     */
    public String getCategory()
    {
        return category;
    }


    /**
     * String containing the definition values
     *
     * @return printable string
     */
    @Override
    public String toString()
    {
        return "ProductSubscriptionDefinition{" +
                "connectorProviderClassName='" + connectorProviderClassName + '\'' +
                ", identifier='" + identifier + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                "} " + super.toString();
    }
}
