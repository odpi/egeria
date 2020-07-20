/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useEffect, useContext } from "react";
import {
  Accordion,
  AccordionItem,
  DatePicker,
  DatePickerInput,
  DataTable,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableHeader,
  TableBody,
} from "carbon-components-react";

import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";
import Info16 from "@carbon/icons-react/lib/information/16";

function NodeUpdate(props) {
  // const {node} = props;
  console.log("NodeUpdate");

  const glossaryAuthorContext = useContext(GlossaryAuthorContext);
  console.log("NodeUpdate glossaryAuthorContext", glossaryAuthorContext);

  const [errorMsg, setErrorMsg] = useState();
  const [updateBody, setUpdateBody] = useState({});

  /**
   * If there was an error the button has a class added to it to cause it to shake. After the animation ends, we need to remove the class.
   * @param {*} e end anomation event
   */
  const handleOnAnimationEnd = (e) => {
    document.getElementById("nodeUpdateButton").classList.remove("shaker");
  };

  const handleUpdate = (e) => {
    console.log("handleUpdate(()");
    e.preventDefault();
    let body = updateBody;
    const nodeType = glossaryAuthorContext.currentNodeType;
    body.nodeType = nodeType.typeName;
    if (nodeType.hasGlossary) {
      let glossary = {};
      glossary.guid = glossaryAuthorContext.myGlossary.systemAttributes.guid;
      body.glossary = glossary;
    }
    const url =
      nodeType.url +
      "/" +
      glossaryAuthorContext.selectedNode.systemAttributes.guid;
    console.log("Body to be submitted is " + JSON.stringify(body));
    console.log("URL to be submitted is " + url);

    fetch(url, {
      method: "put",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    })
      .then((res) => res.json())
      .then((res) => {
        console.log("update worked " + JSON.stringify(res));
        // glossaryAuthorContext.updateSelectedNode(res);

        if (res.relatedHTTPCode == 200 && res.result && res.result[0]) {
          // const nodeResponse = res.result[0];
          // glossaryAuthorContext.updateSelectedNode(nodeResponse);
          glossaryAuthorContext.setRefreshSearchActionState();
        } else {
          let msg = "";
          // if this is a formatted Egeria response, we have a user action
          if (res.relatedHTTPCode) {
            if (res.exceptionUserAction) {
              msg = "Update Failed: " + res.exceptionUserAction;
            } else {
              msg =
                "Update Failed unexpected Egeria response: " +
                JSON.stringify(res);
            }
          } else if (res.errno) {
            if (res.errno == "ECONNREFUSED") {
              msg = "Connection refused to the view server.";
            } else {
              // TODO create nice messages for all the http codes we think are relevant
              msg = "Update Failed with http errno " + res.errno;
            }
          } else {
            msg = "Update Failed - unexpected response" + JSON.stringify(res);
          }
          setErrorMsg(msg);
          document.getElementById("nodeUpdateButton").classList.add("shaker");
        }
      })
      .catch((res) => {
        setErrorMsg("Update Failed - logic error");
      });
  };
  const validateForm = () => {
    //TODO consider marking name as manditory in the nodetype definition
    //return updateBody.name && updateBody.name.length > 0;

    return true;
  };
  const createLabelId = (labelKey) => {
    return "text-input-" + labelKey;
  };

  const getSystemDataRowData = () => {
    let rowData = [];
    const systemAttributes =
      glossaryAuthorContext.selectedNode.systemAttributes;
    for (var prop in systemAttributes) {
      let row = {};
      row.id = prop;
      row.attrName = prop;
      let value = systemAttributes[prop];
      // TODO deal with the other types (and null? and arrays?) properly
      value = JSON.stringify(value);
      row.value = value;
      rowData.push(row);
    }
    return rowData;
  };
  const setAttribute = (item, value) => {
    let myupdateBody = updateBody;
    myupdateBody[item.key] = value;
    setUpdateBody(myupdateBody);
  };
  const createdTableHeaderData = [
    {
      header: "Attribute Name",
      key: "attrName",
    },
    {
      header: "Value",
      key: "value",
    },
  ];

  // const getTableAttrRowData = () => {
  //   let rowData = [];
  //   const attributes = glossaryAuthorContext.currentNodeType.attributes;

  //   for (var prop in glossaryAuthorContext.selectedNode) {
  //     if (
  //       prop != "systemAttributes" &&
  //       prop != "glossary" &&
  //       prop != "classifications" &&
  //       prop != "class"
  //     ) {
  //       let row = {};
  //       row.id = prop;
  //       row.attrName = prop;
  //       // if we know about the attribute then use the label.
  //       if (prop == "nodeType") {
  //         row.attrName = "Node Type";
  //       } else {
  //         for (var i = 0; i < attributes.length; i++) {
  //           if (attributes[i].key == prop) {
  //             row.attrName = attributes[i].label;
  //           }
  //         }
  //       }

  //       let value = updateResponse[prop];
  //       // TODO deal with the other types (and null? and arrays?) properly
  //       value = JSON.stringify(value);
  //       row.value = value;
  //       rowData.push(row);
  //     }
  //   }
  //   return rowData;
  // };

  return (
    <div>
      <form>
        <div>
          <h4>
            Update{" "}
            {glossaryAuthorContext.currentNodeType
              ? glossaryAuthorContext.currentNodeType.typeName
              : ""}
            <Info16 />
          </h4>
        </div>
        {glossaryAuthorContext.currentNodeType.attributes.map((item) => {
          return (
            <div className="bx--form-item" key={item.key}>
              <label htmlFor={createLabelId(item.key)} className="bx--label">
                {item.label} <Info16 />
              </label>
              <input
                id={createLabelId(item.key)}
                type="text"
                className="bx--text-input"
                value={glossaryAuthorContext.selectedNode[item.key]}
                //value={item.name}
                onChange={(e) => setAttribute(item, e.target.value)}
                placeholder={item.label}
              >
                {glossaryAuthorContext.selectedNode[item.name]}
              </input>
            </div>
          );
        })}

        <Accordion>
          <AccordionItem title="Effectivity">
            <DatePicker dateFormat="m/d/Y" datePickerType="range">
              <DatePickerInput
                id="date-picker-range-start"
                placeholder="mm/dd/yyyy"
                labelText="Effective from date"
                type="text"
              />
              <DatePickerInput
                id="date-picker-range-end"
                placeholder="mm/dd/yyyy"
                labelText="Effective to date"
                type="text"
              />
            </DatePicker>
          </AccordionItem>
        </Accordion>
        <Accordion>
          <AccordionItem title="System Attributes">
            <div className="bx--form-item">
              <DataTable
                isSortable
                rows={getSystemDataRowData()}
                headers={createdTableHeaderData}
                render={({ rows, headers, getHeaderProps }) => (
                  <TableContainer title="System Attributes">
                    <Table size="normal">
                      <TableHead>
                        <TableRow>
                          {headers.map((header) => (
                            <TableHeader key={header.key}  {...getHeaderProps({ header })} >
                              {header.header}
                            </TableHeader>
                          ))}
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {rows.map((row) => (
                          <TableRow key={row.id}>
                            {row.cells.map((cell) => (
                              <TableCell key={cell.id}>{cell.value}</TableCell>
                            ))}
                          </TableRow>
                        ))}
                      </TableBody>
                    </Table>
                  </TableContainer>
                )}
              />
            </div>
          </AccordionItem>
        </Accordion>

        <div className="bx--form-item">
          <button
            id="nodeUpdateButton"
            className="bx--btn bx--btn--primary"
            disabled={!validateForm()}
            onClick={handleUpdate}
            onAnimationEnd={handleOnAnimationEnd}
            type="button"
          >
            Update
          </button>
          <div style={{ color: "red" }}>{errorMsg}</div>
        </div>
      </form>
    </div>
  );
}

export default NodeUpdate;
