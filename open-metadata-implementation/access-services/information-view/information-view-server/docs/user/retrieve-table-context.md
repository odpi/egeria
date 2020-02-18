<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Retrieve full context of a table

Retrieve table context: host, database name, schema name, table name, list of columns along with business terms assigned 

```
POST {serverURLRoot}/servers/{serverName}/open-metadata/access-services/information-view/users/{userId}/tables/{tableGUID}
```

`VoidResponse` response with error status in case of error.

Sample:

```json
{
    "class": "TableContextResponse",
    "relatedHTTPCode": 200,
    "tableContexts": [
        {
            "class": "TableContextEvent",
            "eventVersionId": 1,
            "tableSource": {
                "class": "TableSource",
                "@id": 1,
                "guid": "b1c497ce.54bd3a08.0v9mgsb2t.fae21gd.ehu9t3.egljqf3hf176clmuugvlc",
                "name": "EMPSALARYANALYSIS",
                "schemaName": "DB2INST1",
                "databaseSource": {
                    "class": "DatabaseSource",
                    "@id": 2,
                    "guid": "b1c497ce.6e83759b.0v9mgsb2t.fauo7tn.0vv57r.miqdq9fqjod6vnco6jfep",
                    "name": "EMPLSANL",
                    "endpointSource": {
                        "class": "EndpointSource",
                        "@id": 3,
                        "networkAddress": "{host}",
                        "connectorProviderName": "DB2Connector"
                    }
                }
            },
            "tableColumns": [
                {
                    "guid": "b1c497ce.60641b50.0v9mgsb2t.faunv1i.4jppgp.vcpqhhv5aub9uk5thtugp",
                    "qualifiedName": "(host_(engine))={host}::(database)=EMPLSANL::(database_schema)=DB2INST1::(database_table)=EMPSALARYANALYSIS::(database_column)=STATE",
                    "name": "STATE",
                    "position": 20,
                    "businessTerms": [{
                        "guid": "6662c0f2.e1b1ec6c.00263phfe.hdcsrms.095jod.69a74ckl4hrfodcsgeu2d",
                        "name": "State",
                        "description": "",
                        "abbreviation": "",
                        "usage": "",
                        "summary": "",
                        "examples": "",
                        "qualifiedName": "(category)=Coco Pharmaceuticals::(category)=Terms::(term)=State"
                    }],
                    "isNullable": false,
                    "isUnique": false,
                    "isPrimaryKey": false
                },
                {
                    "guid": "b1c497ce.60641b50.0v9mgsb2t.fadq4s6.5vn3hu.ehqs5908s76ig6cn8ho6g",
                    "qualifiedName": "(host_(engine))={host}::(database)=EMPLSANL::(database_schema)=DB2INST1::(database_table)=EMPSALARYANALYSIS::(database_column)=EMPSTATUS",
                    "name": "EMPSTATUS",
                    "position": 4,
                    "businessTerms": [{
                        "guid": "6662c0f2.e1b1ec6c.00263pfrb.66t5p8c.jk5353.69k7s3u2do49odaavci58",
                        "name": "Employee Status",
                        "description": "",
                        "abbreviation": "",
                        "usage": "",
                        "summary": "Employee Status represents current various status that an employee could have. Possible values: 1=Student; 2=Temporary-Assignment; 3=Board-Advisor; 4=Sabbatical; 5=Executive; 6=Part-Time-Perm; 7=Full-Time-Perm; 8=Left; 9=Retired; 10=DIS",
                        "examples": "",
                        "qualifiedName": "(category)=Coco Pharmaceuticals::(category)=Terms::(term)=Employee Status"
                    }],
                    "isNullable": false,
                    "isUnique": false,
                    "isPrimaryKey": false
                },
                {
                    "guid": "b1c497ce.60641b50.0v9mgsb2t.fadoft6.jdu07h.62mt3l35mh6qmt0lcufbv",
                    "qualifiedName": "(host_(engine))={host}::(database)=EMPLSANL::(database_schema)=DB2INST1::(database_table)=EMPSALARYANALYSIS::(database_column)=TAXP",
                    "name": "TAXP",
                    "position": 22,
                    "businessTerms": [{
                        "guid": "6662c0f2.e1b1ec6c.00263phg8.gdtncls.audklp.e6or1vj80e3j7mgaqignr",
                        "name": "Tax State",
                        "description": "",
                        "abbreviation": "",
                        "usage": "",
                        "summary": "",
                        "examples": "",
                        "qualifiedName": "(category)=Coco Pharmaceuticals::(category)=Terms::(term)=Tax State"
                    }],
                    "isNullable": false,
                    "isUnique": false,
                    "isPrimaryKey": false
                },
                {
                    "guid": "b1c497ce.60641b50.0v9mgsb2t.fadue7h.1773qq.bgf1nnglkv2j42jlnrs7o",
                    "qualifiedName": "(host_(engine))={host}::(database)=EMPLSANL::(database_schema)=DB2INST1::(database_table)=EMPSALARYANALYSIS::(database_column)=BONUS",
                    "name": "BONUS",
                    "position": 16,
                    "businessTerms": [{
                        "guid": "6662c0f2.e1b1ec6c.00263ph59.aqte17n.28ed5t.81mkudrsucorsqicli64s",
                        "name": "Discretionary Bonus",
                        "description": "",
                        "abbreviation": "",
                        "usage": "",
                        "summary": "",
                        "examples": "",
                        "qualifiedName": "(category)=Coco Pharmaceuticals::(category)=Terms::(term)=Discretionary Bonus"
                    }],
                    "isNullable": false,
                    "isUnique": false,
                    "isPrimaryKey": false
                },
                {
                    "guid": "b1c497ce.60641b50.0v9mgsb2t.fauqp0r.jqrvpc.6bo5obcmp33g86iegdc0l",
                    "qualifiedName": "(host_(engine))={host}::(database)=EMPLSANL::(database_schema)=DB2INST1::(database_table)=EMPSALARYANALYSIS::(database_column)=STREET",
                    "name": "STREET",
                    "position": 18,
                    "businessTerms": [{
                        "guid": "6662c0f2.e1b1ec6c.00263ph63.beh4unk.9jn1qm.db96h9bqgj8gkjoj7qhh6",
                        "name": "Street",
                        "description": "",
                        "abbreviation": "",
                        "usage": "",
                        "summary": "",
                        "examples": "",
                        "qualifiedName": "(category)=Coco Pharmaceuticals::(category)=Terms::(term)=Street"
                    }],
                    "isNullable": false,
                    "isUnique": false,
                    "isPrimaryKey": false
                },
                {
                    "guid": "b1c497ce.60641b50.0v9mgsb2t.fadn9e2.qq5dgo.t0gu7m9sucub1dns5u1ov",
                    "qualifiedName": "(host_(engine))={host}::(database)=EMPLSANL::(database_schema)=DB2INST1::(database_table)=EMPSALARYANALYSIS::(database_column)=SNUM",
                    "name": "SNUM",
                    "position": 17,
                    "isNullable": false,
                    "isUnique": false,
                    "isPrimaryKey": false
                }
            ]
        }
    ]
}
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.