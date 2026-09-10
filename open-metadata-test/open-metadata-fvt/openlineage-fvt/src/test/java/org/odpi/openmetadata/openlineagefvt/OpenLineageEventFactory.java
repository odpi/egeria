/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openlineagefvt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageDataQualityAssertionsInputDataSetFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageDataQualityAssertionsInputDataSetFacetAssertions;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageDataSetFacets;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageDocumentationDataSetFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageDocumentationJobFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageInputDataSet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageInputDataSetInputFacets;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageJob;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageJobFacets;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageLifecycleStateChangeDataSetFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageLifecycleStateChangeDataSetFacetPreviousIdentifier;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageNominalTimeRunFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageOutputDataSet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageOutputDataSetOutputFacets;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageOutputStatisticsOutputDataSetFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageOwnershipJobFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageOwnershipJobFacetOwner;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageParentRunFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageParentRunFacetJob;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageParentRunFacetRun;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRun;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunFacets;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageSQLJobFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageSchemaDataSetFacet;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageSchemaDataSetFacetField;

import java.net.URI;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * OpenLineageEventFactory builds the run events that the tests publish, using the Open Integration Framework's
 * Open Lineage beans so that the events are exactly what the connectors expect to parse.  The events describe a
 * small pipeline: a job that reads an orders table and a landing file, and writes an order counts table.
 */
class OpenLineageEventFactory
{
    static final URI    PRODUCER      = URI.create("https://egeria-project.org/openlineage-fvt");
    static final String JOB_NAMESPACE = "openlineage-fvt://jobs";

    static final String PARENT_JOB_NAME = "orders_etl";
    static final String JOB_NAME        = "orders_etl.count_orders";
    static final String KAFKA_JOB_NAME  = "orders_etl.publish_counts";
    static final String FAILING_JOB_NAME = "orders_etl.validate_orders";

    static final String TABLE_NAMESPACE   = "postgres://openlineage-fvt:5432";
    static final String ORDERS_TABLE      = "sales.public.orders";
    static final String COUNTS_TABLE      = "sales.public.order_counts";
    static final String RENAMED_COUNTS_TABLE = "sales.public.daily_order_counts";
    static final String FILE_NAMESPACE    = "file";
    static final String LANDING_FILE      = "/openlineage-fvt/landing/orders.csv";
    static final String TOPIC_NAMESPACE   = "kafka://openlineage-fvt:9092";
    static final String COUNTS_TOPIC      = "order-counts";

    static final String JOB_DESCRIPTION = "Counts the orders received each day.";
    static final String JOB_OWNER       = "team:openlineage-fvt";
    static final String ORDERS_DESCRIPTION = "Orders received from the web shop.";
    static final List<String> ORDERS_COLUMNS = List.of("id", "customer_id", "order_date", "total");

    private static final ObjectMapper      OBJECT_MAPPER = new ObjectMapper();
    private static final DateTimeFormatter TIMESTAMP     = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneOffset.UTC);


    /**
     * Format a time as an OpenLineage event time.
     *
     * @param instant instant
     * @return ISO-8601 string
     */
    static String timestamp(Instant instant)
    {
        return TIMESTAMP.format(instant);
    }


    /**
     * Serialize an event to JSON.
     *
     * @param event event bean
     * @return JSON
     * @throws Exception serialization problem
     */
    static String toJSON(OpenLineageRunEvent event) throws Exception
    {
        return OBJECT_MAPPER.writeValueAsString(event);
    }


    /**
     * Build the skeleton of a run event for the standard job.
     *
     * @param eventType START, RUNNING, COMPLETE, FAIL or ABORT
     * @param eventTime time of the event
     * @param runId run identifier
     * @param jobName job name
     * @return event with job and run filled in
     */
    static OpenLineageRunEvent runEvent(String  eventType,
                                        Instant eventTime,
                                        UUID    runId,
                                        String  jobName)
    {
        OpenLineageRunEvent event = new OpenLineageRunEvent();

        event.setProducer(PRODUCER);
        event.setEventType(eventType);
        event.setEventTime(timestamp(eventTime));

        OpenLineageJob job = new OpenLineageJob();
        job.setNamespace(JOB_NAMESPACE);
        job.setName(jobName);

        OpenLineageJobFacets jobFacets = new OpenLineageJobFacets();

        OpenLineageDocumentationJobFacet documentation = new OpenLineageDocumentationJobFacet();
        documentation.set_producer(PRODUCER);
        documentation.setDescription(JOB_DESCRIPTION);
        jobFacets.setDocumentation(documentation);

        OpenLineageSQLJobFacet sql = new OpenLineageSQLJobFacet();
        sql.set_producer(PRODUCER);
        sql.setQuery("INSERT INTO order_counts SELECT order_date, count(*) FROM orders GROUP BY order_date");
        sql.setDialect("postgres");
        jobFacets.setSql(sql);

        OpenLineageOwnershipJobFacet ownership = new OpenLineageOwnershipJobFacet();
        ownership.set_producer(PRODUCER);
        OpenLineageOwnershipJobFacetOwner owner = new OpenLineageOwnershipJobFacetOwner();
        owner.setName(JOB_OWNER);
        owner.setType("MAINTAINER");
        ownership.setOwners(List.of(owner));
        jobFacets.setOwnership(ownership);

        job.setFacets(jobFacets);
        event.setJob(job);

        OpenLineageRun run = new OpenLineageRun();
        run.setRunId(runId);

        OpenLineageRunFacets runFacets = new OpenLineageRunFacets();

        OpenLineageNominalTimeRunFacet nominalTime = new OpenLineageNominalTimeRunFacet();
        nominalTime.set_producer(PRODUCER);
        nominalTime.setNominalStartTime(timestamp(eventTime.minusSeconds(3600)));
        nominalTime.setNominalEndTime(timestamp(eventTime));
        runFacets.setNominalTime(nominalTime);

        OpenLineageParentRunFacet parent = new OpenLineageParentRunFacet();
        parent.set_producer(PRODUCER);
        OpenLineageParentRunFacetJob parentJob = new OpenLineageParentRunFacetJob();
        parentJob.setNamespace(JOB_NAMESPACE);
        parentJob.setName(PARENT_JOB_NAME);
        parent.setJob(parentJob);
        OpenLineageParentRunFacetRun parentRun = new OpenLineageParentRunFacetRun();
        parentRun.setRunId(UUID.nameUUIDFromBytes(("parent-" + runId).getBytes()));
        parent.setRun(parentRun);
        runFacets.setParent(parent);

        run.setFacets(runFacets);
        event.setRun(run);

        event.setInputs(new ArrayList<>());
        event.setOutputs(new ArrayList<>());

        return event;
    }


    /**
     * Add the standard inputs (the orders table with its schema and documentation, and the landing file).
     *
     * @param event event
     * @param withAssertions whether to include the data quality assertions on the orders table
     * @return the event
     */
    static OpenLineageRunEvent withInputs(OpenLineageRunEvent event,
                                          boolean             withAssertions)
    {
        OpenLineageInputDataSet orders = new OpenLineageInputDataSet();
        orders.setNamespace(TABLE_NAMESPACE);
        orders.setName(ORDERS_TABLE);

        OpenLineageDataSetFacets ordersFacets = new OpenLineageDataSetFacets();

        OpenLineageDocumentationDataSetFacet documentation = new OpenLineageDocumentationDataSetFacet();
        documentation.set_producer(PRODUCER);
        documentation.setDescription(ORDERS_DESCRIPTION);
        ordersFacets.setDocumentation(documentation);

        OpenLineageSchemaDataSetFacet schema = new OpenLineageSchemaDataSetFacet();
        schema.set_producer(PRODUCER);
        List<OpenLineageSchemaDataSetFacetField> fields = new ArrayList<>();
        long position = 1;
        for (String column : ORDERS_COLUMNS)
        {
            OpenLineageSchemaDataSetFacetField field = new OpenLineageSchemaDataSetFacetField();
            field.setName(column);
            field.setType(column.equals("total") ? "NUMERIC" : column.equals("order_date") ? "DATE" : "INTEGER");
            field.setOrdinalPosition(position++);
            fields.add(field);
        }
        schema.setFields(fields);
        ordersFacets.setSchema(schema);
        orders.setFacets(ordersFacets);

        if (withAssertions)
        {
            OpenLineageInputDataSetInputFacets inputFacets = new OpenLineageInputDataSetInputFacets();
            OpenLineageDataQualityAssertionsInputDataSetFacet assertions = new OpenLineageDataQualityAssertionsInputDataSetFacet();
            assertions.set_producer(PRODUCER);

            OpenLineageDataQualityAssertionsInputDataSetFacetAssertions notNull = new OpenLineageDataQualityAssertionsInputDataSetFacetAssertions();
            notNull.setAssertion("not_null");
            notNull.setColumn("id");
            notNull.setSuccess(true);

            OpenLineageDataQualityAssertionsInputDataSetFacetAssertions unique = new OpenLineageDataQualityAssertionsInputDataSetFacetAssertions();
            unique.setAssertion("unique");
            unique.setColumn("id");
            unique.setSuccess(false);
            unique.setSeverity("error");

            assertions.setAssertions(List.of(notNull, unique));
            inputFacets.setDataQualityAssertions(assertions);
            orders.setInputFacets(inputFacets);
        }

        OpenLineageInputDataSet landing = new OpenLineageInputDataSet();
        landing.setNamespace(FILE_NAMESPACE);
        landing.setName(LANDING_FILE);

        event.getInputs().add(orders);
        event.getInputs().add(landing);

        return event;
    }


    /**
     * Add the standard output (the order counts table) with output statistics.
     *
     * @param event event
     * @param tableName name of the output table
     * @param rowCount rows written (null for no statistics)
     * @return the event
     */
    static OpenLineageRunEvent withOutput(OpenLineageRunEvent event,
                                          String              tableName,
                                          Long                rowCount)
    {
        OpenLineageOutputDataSet counts = new OpenLineageOutputDataSet();
        counts.setNamespace(TABLE_NAMESPACE);
        counts.setName(tableName);

        if (rowCount != null)
        {
            OpenLineageOutputDataSetOutputFacets outputFacets = new OpenLineageOutputDataSetOutputFacets();
            OpenLineageOutputStatisticsOutputDataSetFacet statistics = new OpenLineageOutputStatisticsOutputDataSetFacet();
            statistics.set_producer(PRODUCER);
            statistics.setRowCount(rowCount);
            statistics.setSize(rowCount * 64);
            outputFacets.setOutputStatistics(statistics);
            counts.setOutputFacets(outputFacets);
        }

        event.getOutputs().add(counts);

        return event;
    }


    /**
     * Add an output that reports a lifecycle state change for the order counts table.
     *
     * @param event event
     * @param newName name of the table after the change
     * @param lifecycleStateChange RENAME or DROP
     * @param previousName previous name (for RENAME) or null
     * @return the event
     */
    static OpenLineageRunEvent withLifecycleChange(OpenLineageRunEvent event,
                                                   String              newName,
                                                   String              lifecycleStateChange,
                                                   String              previousName)
    {
        OpenLineageOutputDataSet counts = new OpenLineageOutputDataSet();
        counts.setNamespace(TABLE_NAMESPACE);
        counts.setName(newName);

        OpenLineageDataSetFacets facets = new OpenLineageDataSetFacets();
        OpenLineageLifecycleStateChangeDataSetFacet change = new OpenLineageLifecycleStateChangeDataSetFacet();
        change.set_producer(PRODUCER);
        change.setLifecycleStateChange(lifecycleStateChange);

        if (previousName != null)
        {
            OpenLineageLifecycleStateChangeDataSetFacetPreviousIdentifier previous = new OpenLineageLifecycleStateChangeDataSetFacetPreviousIdentifier();
            previous.setNamespace(TABLE_NAMESPACE);
            previous.setName(previousName);
            change.setPreviousIdentifier(previous);
        }

        facets.setLifecycleStateChange(change);
        counts.setFacets(facets);

        event.getOutputs().add(counts);

        return event;
    }


    /**
     * Build the event for the job that the Kafka listener test publishes: it reads the counts table and writes a topic.
     *
     * @param eventType event type
     * @param eventTime event time
     * @param runId run id
     * @return event
     */
    static OpenLineageRunEvent kafkaJobEvent(String  eventType,
                                             Instant eventTime,
                                             UUID    runId)
    {
        OpenLineageRunEvent event = runEvent(eventType, eventTime, runId, KAFKA_JOB_NAME);

        OpenLineageInputDataSet counts = new OpenLineageInputDataSet();
        counts.setNamespace(TABLE_NAMESPACE);
        counts.setName(COUNTS_TABLE);
        event.getInputs().add(counts);

        OpenLineageOutputDataSet topic = new OpenLineageOutputDataSet();
        topic.setNamespace(TOPIC_NAMESPACE);
        topic.setName(COUNTS_TOPIC);
        event.getOutputs().add(topic);

        return event;
    }
}
