/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useContext } from "react";
import {
  Accordion,
  AccordionItem,
  DatePicker,
  DatePickerInput,
  Button,
  DataTable,
  Select,
  SelectItem,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableSelectAll,
  TableSelectRow,
  TableCell,
  TableHeader,
  TableBody,
} from "carbon-components-react";

// import { IdentificationContext } from "../../contexts/IdentificationContext";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";
import Info16 from "@carbon/icons-react/lib/information/16";

function NodeCreate(props) {
  console.log("Nodecreate");

  const glossaryAuthorContext = useContext(GlossaryAuthorContext);
  console.log("NodeCreate glossaryAuthorContext", glossaryAuthorContext);
  const [errorMsg, setErrorMsg] = useState();
  const [createBody, setCreateBody] = useState({});
  const [created, setCreated] = useState();
  /**
   * If there was an error the button has a class added to it to cause it to shake. After the animation ends, we need to remove the class.
   * @param {*} e end anomation event
   */
  const handleOnAnimationEnd = (e) => {
    document.getElementById("nodeCreateButton").classList.remove("shaker");
  };

  const handleClick = (e) => {
    console.log("handleClick(()");
    e.preventDefault();
    let body = createBody;
    const nodeType = glossaryAuthorContext.currentNodeType;
    if (nodeType.typeForCreate) {
      body.nodeType = nodeType.nodeTypeForCreate;
    } else {
      body.nodeType = nodeType.typeName;
    }
    if (nodeType.hasGlossary) {
      let glossary = {};
      glossary.guid = glossaryAuthorContext.myGlossary.systemAttributes.guid;
      body.glossary = glossary;
    }

    console.log("Body to be submitted is " + JSON.stringify(body));
    console.log("URL to be submitted is " + nodeType.url);

    fetch(nodeType.url, {
      method: "post",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    })
      .then((res) => res.json())
      .then((res) => {
        console.log("create worked " + JSON.stringify(res));

        const nodeResponse = res[nodeType.key];
        // if there is a node response then we have successfully created a node
        if (nodeResponse) {
          if (glossaryAuthorContext.authoringState == 1) {
            glossaryAuthorContext.setMyGlossary(nodeResponse);
            glossaryAuthorContext.setMyGlossaryLabel(nodeResponse.name);
            glossaryAuthorContext.setMyGlossaryState();
          } else if (glossaryAuthorContext.authoringState == 2) {
            glossaryAuthorContext.setMyProject(nodeResponse);
            glossaryAuthorContext.setMyProjectLabel(nodeResponse.name);
            glossaryAuthorContext.setMyProjectState();
          }

          // check if fully setup fully setup - we might still have only set the myProject or myGlossary (not both)
          if (
            glossaryAuthorContext.myProject &&
            glossaryAuthorContext.myGlossary
          ) {
            glossaryAuthorContext.setAuthoringState(5);
          }
          setCreated(nodeResponse);
        } else {
          let msg = "";
          if (res.responseCategory) {
            if (res.exceptionUserAction) {
              msg = "Create Failed: " + res.exceptionUserAction;
            } else {
              msg =
                "Create Failed unexpected Egeria response: " +
                JSON.stringify(res);
            }
          } else if (res.errno) {
            if (res.errno == "ECONNREFUSED") {
              msg = "Connection refused to the view server.";
            } else {
              // TODO create nice messages for all the http codes we think are relevant
              msg = "Create Failed with http errno " + res.errno;
            }
          } else {
            msg = "Create Failed - unexpected response" + JSON.stringify(res);
          }
          setErrorMsg(msg);
          document.getElementById("nodeCreateButton").classList.add("shaker");
        }
      })
      .catch((res) => {
        setErrorMsg("Create Failed - logic error");
      });
  };
  const validateForm = () => {
    //TODO consider marking name as manditory in the nodetype definition
    //return createBody.name && createBody.name.length > 0;

    return true;
  };
  const createLabelId = (labelKey) => {
    return "text-input-" + labelKey;
  };
  const setAttribute = (item, value) => {
    let myCreateBody = createBody;
    myCreateBody[item.key] = value;
    setCreateBody(myCreateBody);
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

  const getCreatedTableTitle = () => {
    return "Created " + created.name;
  };

  const getCreatedTableAttrRowData = () => {
    let rowData = [];
    const attributes = glossaryAuthorContext.currentNodeType.attributes;

    for (var prop in created) {
      if (
        prop != "systemAttributes" &&
        prop != "glossary" &&
        prop != "classifications" &&
        prop != "class"
      ) {
        let row = {};
        row.id = prop;
        row.attrName = prop;
        // if we know about the attribute then use the label.
        if (prop == "nodeType") {
          row.attrName = "Node Type";
        } else {
          for (var i = 0; i < attributes.length; i++) {
            if (attributes[i].key == prop) {
              row.attrName = attributes[i].label;
            }
          }
        }

        let value = created[prop];
        // TODO deal with the other types (and null? and arrays?) properly
        value = JSON.stringify(value);
        row.value = value;
        rowData.push(row);
      }
    }
    return rowData;
  };
  const getSystemDataRowData = () => {
    let rowData = [];
    const systemAttributes = created.systemAttributes;
    for (var prop in systemAttributes) {
      let row = {};
      row.id = prop;
      row.attrName = prop;
      // TODO if we know about the attribute then use the label.

      // for (var i = 0; i < attributes.length; i++) {
      // if (attributes[i].key == prop) {
      //   row.attrName = attributes[i].label;
      // }
      // }
      // }

      let value = systemAttributes[prop];
      // TODO deal with the other types (and null? and arrays?) properly
      value = JSON.stringify(value);
      row.value = value;
      rowData.push(row);
    }
    return rowData;
  };

  return (
    <div>
      {created && (
        <div>
          <DataTable
            isSortable
            rows={getCreatedTableAttrRowData()}
            headers={createdTableHeaderData}
            render={({ rows, headers, getHeaderProps }) => (
              <TableContainer title={getCreatedTableTitle()}>
                <Table size="normal">
                  <TableHead>
                    <TableRow>
                      {headers.map((header) => (
                        <TableHeader {...getHeaderProps({ header })}>
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

          <Accordion>
            <AccordionItem title="System Attributes">
              <div class="bx--form-item">
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
                              <TableHeader {...getHeaderProps({ header })}>
                                {header.header}
                              </TableHeader>
                            ))}
                          </TableRow>
                        </TableHead>
                        <TableBody>
                          {rows.map((row) => (
                            <TableRow key={row.id}>
                              {row.cells.map((cell) => (
                                <TableCell key={cell.id}>
                                  {cell.value}
                                </TableCell>
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
        </div>
      )}

      {!created && (
        <form>
          <div>
            <h4>
              Create{" "}
              {glossaryAuthorContext.currentNodeType
                ? glossaryAuthorContext.currentNodeType.typeName
                : ""}
              <Info16 />
            </h4>
          </div>

          {glossaryAuthorContext.currentNodeType &&
            !created &&
            glossaryAuthorContext.currentNodeType.attributes.map((item) => {
              return (
                <div class="bx--form-item">
                  <label for={createLabelId(item.key)} class="bx--label">
                    {item.label} <Info16 />
                  </label>
                  <input
                    id={createLabelId(item.key)}
                    type="text"
                    class="bx--text-input"
                    value={item.name}
                    onChange={(e) => setAttribute(item, e.target.value)}
                    placeholder={item.label}
                  ></input>
                </div>
              );
            })}
          <Accordion>
            <AccordionItem title="Advanced options">
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

          <div class="bx--form-item">
            <button
              id="nodeCreateButton"
              class="bx--btn bx--btn--primary"
              disabled={!validateForm()}
              onClick={handleClick}
              onAnimationEnd={handleOnAnimationEnd}
              type="button"
            >
              Create
            </button>
            <div style={{ color: "red" }}>{errorMsg}</div>
          </div>
        </form>
      )}
    </div>
  );
}

export default NodeCreate;
