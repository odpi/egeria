/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState } from "react";
import Add32 from "../../../images/Egeria_add_32";
import getNodeType from "./properties/NodeTypes.js";
import { Form, FormGroup, TextInput, Button } from "carbon-components-react";
import { issueRestCreate } from "./RestCaller";

export default function QuickTerms() {
  const [terms, setTerms] = useState([]);
  const restUrl = getNodeType("term").url;

  const validateForm = () => {
    return true;
  };
  const onAdd = (e) => {
    console.log("onAdd");
    let newTerm = {};
    newTerm.name = "";
    newTerm.description = "";
    const workingTerms = [...terms, newTerm];
    setTerms(workingTerms);
  };

  const onSubmit = (e) => {
    console.log("onSubmit");
    e.preventDefault();

    if (terms.length > 0) {
      // create body with glossary and list of terms
      let body = {};
      let glossary = {};
      glossary.guid = getGlossaryGuid();
      body.glossary = glossary;
      body.terms = terms;

      //TODO issue create using the body we have just created. 
    } else {
      alert("Nothing to create");
    }
  };
  function onSuccessfulCreate() {
    //TODO
  }
  //return next term to create
  function onErrorCreate(msg) {
    TODO;
  }

  const getGlossaryGuid = () => {
    const result = window.location.pathname.split("/");
    return result[result.length - 2];
  };

  const issueCreate = (body) => {
    console.log("issueCreate " + restUrl);
    issueRestCreate(restUrl, body, onSuccessfulCreate, onErrorCreate);
  };
  function setTermName(index, name) {
    let workingTerms = terms;
    workingTerms[index].name = name;
    setTerms(workingTerms);
  }
  function setTermDescription(index, description) {
    let workingTerms = terms;
    workingTerms[index].description = description;
    setTerms(workingTerms);
  }

  return (
    <div>
      <h3>Quickly add terms.</h3>

      <Add32 kind="primary" onClick={() => onAdd()} />
      <Form>
        <FormGroup>
          {terms.map((item, index) => {
            return (
              <div className="bx--form-item" key={index}>
                <div className="row-container">
                  <TextInput
                    type="text"
                    placeholder="Term name"
                    defaultValue={item.name}
                    onChange={(e) => {
                      setTermName(index, e.target.value);
                    }}
                  />
                  <TextInput
                    type="text"
                    placeholder="Term description"
                    defaultValue={item.description}
                    onChange={(e) => {
                      setTermDescription(index, e.target.value);
                    }}
                  />
                  <div>{item.result}</div>
                </div>
              </div>
            );
          })}
          <Button type="submit" onClick={onSubmit} disabled={!validateForm()}>
            Create Terms on Server
          </Button>
        </FormGroup>
      </Form>
    </div>
  );
}
