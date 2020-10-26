/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState } from "react";
import { Toggle } from "carbon-components-react";

export default function NodeChildren(props) {
  const [isTerm, setIsTerm] = useState(false);
  const [isCategoryAll, setIsCategoryAll] = useState(false);
  const onToggleTermsCategories = () => {
    if (isTerm) {
      setIsTerm(false);
    } else {
      setIsTerm(true);
    }
  };
  const onToggleTop = () => {
    if (isCategoryAll) {
      setIsCategoryAll(false);
    } else {
      setIsCategoryAll(true);
    }
  };

  return (
    <div>
      <Toggle
        aria-label="termCategorytoggle"
        defaultToggled
        labelA="Categories"
        labelB="Terms"
        id="term-category-toggle"
        labelText="Show Categories or Terms"
        onToggle={onToggleTermsCategories}
      />
      <Toggle
        aria-label="topCategorytoggle"
        defaultToggled
        labelA="All Categories"
        labelB="Top Categories"
        id="category-filter-toggle"
        labelText="Category Filter"
        onToggle={onToggleTop}
        disabled={isTerm}
      />
    </div>
  );
}
