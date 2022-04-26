<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Released](../../../../../../images/egeria-content-status-released.png#pagewidth)

# CSV File Connector

The CSV file connector is able to retrieve data from a
Comma Separated Values (CSV) file where the contents are stored in logical columns
with a special character delimiter between the columns.

The name of the file is passed to the connector in the **address**
property in the Endpoint object of the Connection object used to create the connector.

If the first row of the file contains column names,
this connector is able to return a schema structure for the file
that can be used by a discovery engine or data platform engine or
governance daemon or OMAS to extract and store the schema.

There are three configuration properties supported by this connector.

* **delimiterCharacter** - the character used to delimit the columns.
  The comma '**,**' character is the default.
  
* **quoteCharacter** - the character used to provide quotes around
  column content that includes the delimiter character.  The default is the double-quote **"**;
  
* **columnNames** - list of column names - used when the first line of the
  file is not the column names.

These properties are stored in the configuration properties of the Connection object used to create the connector instance.

The unit test cases use examples of different CSV files.  These files are located in the
test resources folder.  For example, **SimpleColumnsWithColumnNames.csv** shows a very traditional
CSV file.  Here is a snippet of the file:

```text
RecId,EType,Email
1,C,ZachNow@Coco-Pharmaceuticals.com
1,S,zach@Coco-Pharmaceuticals.com
2,C,SteveStarter@Coco-Pharmaceuticals.com
2,S,steve@Coco-Pharmaceuticals.com
3,C,TerriDaring@Coco-Pharmaceuticals.com
3,S,terri@Coco-Pharmaceuticals.com
 :             :          :
```

The first line has the column names.  Then the data records follow, line by line.
The data for each of the three columns is
separated by commas.   This file can be processed directly by the structured file connector without
any of the additional properties.

Quotes can be used in CSV files if a comma appears in the value of a column.  Two quotes together are
used if the value of a column also includes a quote.  **ComplexColumnsWithColumnNames.csv**
has quoted column values with spaces and quotes.  This file can also be processed without additional properties.

```text
RecId,ContactType,FirstName,LastName,Company,JobTitle,WorkLocation
1,E,Zach,Now,Coco Pharmaceuticals,Founder,3
2,E,Steve,Starter,Coco Pharmaceuticals,Founder,1
3,E,Terri,Daring,Coco Pharmaceuticals,Founder,2
4,E,Tanya,Tidy,Coco Pharmaceuticals,"Data Steward, ""New"" Clinical Trials",3
5,E,Polly,Tasker,Coco Pharmaceuticals,IT Project Leader,1
6,E,Tessa,Tube,Coco Pharmaceuticals,"Lead Researcher, Clinical Trials",3
  :             :          :
```

**NoColumnNames.csv** is an example of a CSV file that uses unconventional delimiters and has no column
headers so it needs to use all three of the additional properties.

```text
1-O-'1-912-333-4444 (ext 4)'
1-M-'1-912-380-7277'
2-O-'31-20-5274986 (ext 2)'
3-O-'44-20-372-2223 (ext 4)'
4-O-'1-912-333-4444'
5-O-'31-20-5274986 (ext 3)'
  :             :          :
```

The delimiter between columns is a dash (**-**) and a single quote (**'**) has been used to group text that includes the
delimiter character.  There are also no column names in the file so they need to be supplied in the additional
properties.  If the column names are not supplied, then the structured file connector assumes the first
line of the file contains the column names.


----
Return to the [file-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.