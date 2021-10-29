package org.odpi.openmetadata.accessservices.dataengine.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
public class VirtualTable extends RelationalTable {
    private  DatabaseSchema databaseSchema;
}
