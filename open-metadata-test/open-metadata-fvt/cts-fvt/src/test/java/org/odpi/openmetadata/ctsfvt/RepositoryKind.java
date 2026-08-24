/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.ctsfvt;

/**
 * RepositoryKind is the local repository that the technology under test runs, chosen when the run is
 * started - see this module's build.gradle for the Gradle properties that select it.
 * <br>
 * Each kind carries its own server names, cohort name and metadata collection ids rather than sharing
 * one set.  That is what keeps the two kinds from interfering with each other: a cohort registry store
 * outlives the run that created it, so if both kinds registered in the same cohort under the same
 * identities, a run of one kind would find the other kind's server registered but not running, and the
 * enterprise connector would keep trying to reach it.  Separate identities mean each kind has a cohort
 * of its own and neither ever sees the other.
 */
enum RepositoryKind
{
    /**
     * A metadata access store backed by the PostgreSQL repository connector.  Needs a reachable
     * PostgreSQL server as well as a reachable Apache Kafka broker.
     */
    POSTGRES("postgres",
             "ctsFvtTutMetadataStore",
             "ctsFvtConformanceServer",
             "ctsFvtCohort",
             "4d7e6f5a-8b9c-4012-b3c4-6374734676743",
             "5e8f7a6b-9c0d-4123-c4d5-748576877854",
             "PostgreSQL-backed metadata access store certified by the conformance suite."),

    /**
     * A metadata access store backed by the in-memory repository connector.  Needs only Apache Kafka -
     * there is no database - and it is quick to start from nothing, since each run begins with an empty
     * repository whatever happened before.
     */
    IN_MEMORY("inmemory",
              "ctsFvtTutInMemoryStore",
              "ctsFvtInMemoryConformanceServer",
              "ctsFvtInMemoryCohort",
              "6f9a8b7c-0d1e-4234-d5e6-859687988865",
              "7a0b9c8d-1e2f-4345-e6f7-960798099976",
              "In-memory metadata access store certified by the conformance suite.");


    /**
     * The system property the harness reads to find out which kind to run.  It is set from the Gradle
     * property that started the run.
     */
    static final String REPOSITORY_PROPERTY_NAME = "cts.fvt.repository";

    private final String propertyValue;
    private final String tutServerName;
    private final String ctsServerName;
    private final String cohortName;
    private final String tutMetadataCollectionId;
    private final String ctsMetadataCollectionId;
    private final String tutServerDescription;


    RepositoryKind(String propertyValue,
                   String tutServerName,
                   String ctsServerName,
                   String cohortName,
                   String tutMetadataCollectionId,
                   String ctsMetadataCollectionId,
                   String tutServerDescription)
    {
        this.propertyValue           = propertyValue;
        this.tutServerName           = tutServerName;
        this.ctsServerName           = ctsServerName;
        this.cohortName              = cohortName;
        this.tutMetadataCollectionId = tutMetadataCollectionId;
        this.ctsMetadataCollectionId = ctsMetadataCollectionId;
        this.tutServerDescription    = tutServerDescription;
    }


    /**
     * Return the kind of repository this run is testing.
     * <br>
     * The default is PostgreSQL, so that a run started without the property behaves as this harness
     * always has.  An unrecognised value is refused rather than quietly defaulted: it means the run is
     * not testing what whoever started it believes it is testing.
     *
     * @return repository kind
     */
    static RepositoryKind getConfiguredKind()
    {
        String requestedKind = System.getProperty(REPOSITORY_PROPERTY_NAME);

        if ((requestedKind == null) || (requestedKind.isBlank()))
        {
            return POSTGRES;
        }

        for (RepositoryKind kind : RepositoryKind.values())
        {
            if (kind.propertyValue.equals(requestedKind.trim()))
            {
                return kind;
            }
        }

        throw new IllegalStateException(REPOSITORY_PROPERTY_NAME + " is set to '" + requestedKind
                                                + "' which is not a repository this harness knows how to run - expected '"
                                                + POSTGRES.propertyValue + "' or '" + IN_MEMORY.propertyValue + "'");
    }


    /**
     * Return the name of the technology under test's server.
     *
     * @return server name
     */
    String getTutServerName()
    {
        return tutServerName;
    }


    /**
     * Return the name of the conformance test server that drives the technology under test.
     *
     * @return server name
     */
    String getCtsServerName()
    {
        return ctsServerName;
    }


    /**
     * Return the name of the cohort both servers join.
     *
     * @return cohort name
     */
    String getCohortName()
    {
        return cohortName;
    }


    /**
     * Return the fixed metadata collection id of the technology under test.
     *
     * @return metadata collection id
     */
    String getTutMetadataCollectionId()
    {
        return tutMetadataCollectionId;
    }


    /**
     * Return the fixed metadata collection id of the conformance test server.
     *
     * @return metadata collection id
     */
    String getCtsMetadataCollectionId()
    {
        return ctsMetadataCollectionId;
    }


    /**
     * Return the directory name this kind's conformance report is written under, so that running one
     * kind never overwrites the other kind's results.
     *
     * @return directory name
     */
    String getReportDirectoryName()
    {
        return propertyValue;
    }


    /**
     * Return the description given to the technology under test's server, so that a configuration
     * document says which repository it was set up to exercise.
     *
     * @return server description
     */
    String getTutServerDescription()
    {
        return tutServerDescription;
    }
}
