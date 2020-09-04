/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useEffect, useContext } from "react";
import Add16 from "../../../images/Egeria_add_16";
import Update16 from "../../../images/Egeria_edit_16";
import Delete16 from "../../../images/Egeria_delete_16";
import {
  LocalGlossaryCard,
  GlossaryCardSection,
} from "./GlossaryCard/GlossaryCard";
import GlossaryImage from "../../../images/Egeria_glossary_32";
import getNodeType from "./properties/NodeTypes.js";
import { issueRestGet } from "./RestCaller";
import { IdentificationContext } from "../../../contexts/IdentificationContext";

export default function GlossaryAuthorNavigation() {
  const identificationContext = useContext(IdentificationContext);
  const [glossaries, setGlossaries] = useState([]);
  // const [emptyCards, setEmptyCards] = useState(new Array(16));
  const nodeType = getNodeType("glossary");

  useEffect(() => {
    getGlossaries();
  }, []);
  // issue search for first page of glossaries
  const getGlossaries = () => {
    // encode the URI. Be aware the more recent RFC3986 for URLs makes use of square brackets which are reserved (for IPv6)
    const url = encodeURI(
      nodeType.url + "?offset=0&pageSize=1000&searchCriteria=.*"
    );
    issueRestGet(url, onSuccessfulSearch, onErrorSearch);
  };

  const onClickAdd = () => {
    console.log("Add");
  };
  const onClickDelete = () => {
    console.log("Delete");
  };
  const onClickUpdate = () => {
    console.log("Update");
  };
  const onErrorSearch = (msg) => {
    console.log("Error on Get " + msg);
    setErrorMsg(msg);
    setGlossaries([]);
  };
  const onSuccessfulSearch = (json) => {
    setGlossaries(json.result);
    // setEmptyCards(new Array(16-json.result.length));
  };
  const onClickExactMatch = () => {
    console.log("onClickExactMatch");
  };
  const getGlossaryUrl = (name) => {
    return identificationContext.getBrowserURL("glossary-author") + "/" + name;
    // setEmptyCards(new Array(16-json.result.length));
  };

  const onFilterCriteria = (criteria) => {
    console.log("onSearchCriteria " + criteria);
    // setErrorMsg("");
    // setSearchCriteria(criteria);
  };
  return (
    <div className="bx--grid">
      <GlossaryCardSection heading="Glossaries" className="landing-page__r3">
        <article className="glossary-card__controls bx--col-sm-4 bx--col-md-1 bx--col-lg-1 bx--col-xlg-1 bx--col-max-1">
          Choose glossary
        </article>
        <article className="glossary-card__controls bx--col-sm-4 bx--col-md-2 bx--col-lg-4 bx--col-xlg-4 bx--col-max-4">
          <input
            type="text"
            id="filter-input"
            onChange={onFilterCriteria}
            placeholder="Filter"
          />
        </article>
        <article className="glossary-card__controls bx--col-sm-4 bx--col-md-1 bx--col-lg-2 bx--col-xlg-2 bx--col-max-2">
          <div className="glossary-card__exact_control">
            <label forHtml="exactMatch">Exact Match </label>
            <input
              type="checkbox"
              id="exactMatch"
              onClick={onClickExactMatch}
            />
          </div>
        </article>
        <article className="glossary-card__controls bx--col-sm-4 bx--col-md-1 bx--col-lg-1 bx--col-xlg-1 bx--col-max-1">
          <div className="bx--row">
            <Add16 kind="primary" onClick={() => onClickAdd()} />
            <Delete16 onClick={() => onClickDelete()} />
            <Update16 onClick={() => onClickUpdate()} />
          </div>
        </article>
      </GlossaryCardSection>
      <GlossaryCardSection className="landing-page__r3">
        {glossaries.map((glossary) => (
          <LocalGlossaryCard
            key={glossary.name}
            heading={glossary.name}
            body={glossary.description}
            icon={<GlossaryImage />}
            link={getGlossaryUrl(glossary.name)}
          />
        ))}
      </GlossaryCardSection>
    </div>
  );
}
