/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useEffect, useContext } from "react";
// import {
//   GlossaryCardSection,
//   LocalGlossaryCard,
// } from "./GlossaryCard/GlossaryCard";
import {LocalGlossaryCard, GlossaryCardSection} from "./GlossaryCard/GlossaryCard"
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
  const onErrorSearch = (msg) => {
    console.log("Error on Get " + msg);
    setErrorMsg(msg);
    setGlossaries([]);
  };
  const onSuccessfulSearch = (json) => {
    setGlossaries(json.result);
    // setEmptyCards(new Array(16-json.result.length));
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
         <article className="bx--col-md- bx--col-lg- bx--col-xlg-2 bx--offset-xlg-1">
           <section className="row-container">
            <div>Choose glossary</div>
            <div>
              <label id="filter-input" className="bx--label" htmlFor="filter-input">
                Filter
              </label>
            </div>
            <div>
              <input
                type="text"
                id="filter-input"
                onChange={onFilterCriteria}
                placeholder="Filter"
              />
            </div>
            </section>
          </article>
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
