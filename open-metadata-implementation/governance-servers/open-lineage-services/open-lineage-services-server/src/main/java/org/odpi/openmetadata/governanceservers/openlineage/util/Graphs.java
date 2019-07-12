package org.odpi.openmetadata.governanceservers.openlineage.util;

public enum Graphs {

    main("main"),
    buffer("buffer"),
    history("history"),
    mock("mock"),
    ultimatesource("ultimate-source"),
    ultimatedestination("ultimate-destination");

    private String name;

    Graphs(String name) {
        this.name = name;
    }


}

