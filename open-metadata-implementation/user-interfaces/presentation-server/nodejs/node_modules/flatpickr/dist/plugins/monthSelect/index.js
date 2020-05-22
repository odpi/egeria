(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory() :
    typeof define === 'function' && define.amd ? define(factory) :
    (global = global || self, global.monthSelectPlugin = factory());
}(this, function () { 'use strict';

    /*! *****************************************************************************
    Copyright (c) Microsoft Corporation. All rights reserved.
    Licensed under the Apache License, Version 2.0 (the "License"); you may not use
    this file except in compliance with the License. You may obtain a copy of the
    License at http://www.apache.org/licenses/LICENSE-2.0

    THIS CODE IS PROVIDED ON AN *AS IS* BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION ANY IMPLIED
    WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A PARTICULAR PURPOSE,
    MERCHANTABLITY OR NON-INFRINGEMENT.

    See the Apache Version 2.0 License for specific language governing permissions
    and limitations under the License.
    ***************************************************************************** */

    var __assign = function() {
        __assign = Object.assign || function __assign(t) {
            for (var s, i = 1, n = arguments.length; i < n; i++) {
                s = arguments[i];
                for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p)) t[p] = s[p];
            }
            return t;
        };
        return __assign.apply(this, arguments);
    };

    var monthToStr = function (monthNumber, shorthand, locale) { return locale.months[shorthand ? "shorthand" : "longhand"][monthNumber]; };

    var defaultConfig = {
        shorthand: false,
        dateFormat: "F Y",
        altFormat: "F Y",
        theme: "light"
    };
    function monthSelectPlugin(pluginConfig) {
        var config = __assign({}, defaultConfig, pluginConfig);
        return function (fp) {
            fp.config.dateFormat = config.dateFormat;
            fp.config.altFormat = config.altFormat;
            var self = { monthsContainer: null };
            function clearUnnecessaryDOMElements() {
                if (!fp.rContainer || !fp.daysContainer || !fp.weekdayContainer)
                    return;
                fp.rContainer.removeChild(fp.daysContainer);
                fp.rContainer.removeChild(fp.weekdayContainer);
                fp.monthElements.forEach(function (element) {
                    if (!element.parentNode)
                        return;
                    element.parentNode.removeChild(element);
                });
            }
            function addListeners() {
                fp._bind(fp.prevMonthNav, "click", function () {
                    fp.currentYear -= 1;
                    selectYear();
                });
                fp._bind(fp.nextMonthNav, "mousedown", function () {
                    fp.currentYear += 1;
                    selectYear();
                });
            }
            function addMonths() {
                if (!fp.rContainer)
                    return;
                self.monthsContainer = fp._createElement("div", "flatpickr-monthSelect-months");
                self.monthsContainer.tabIndex = -1;
                fp.calendarContainer.classList.add("flatpickr-monthSelect-theme-" + config.theme);
                for (var i = 0; i < 12; i++) {
                    var month = fp._createElement("span", "flatpickr-monthSelect-month");
                    month.dateObj = new Date(fp.currentYear, i);
                    month.$i = i;
                    month.textContent = monthToStr(i, config.shorthand === true, fp.l10n);
                    month.tabIndex = -1;
                    month.addEventListener("click", selectMonth);
                    self.monthsContainer.appendChild(month);
                }
                fp.rContainer.appendChild(self.monthsContainer);
            }
            function setCurrentlySelected() {
                if (!fp.rContainer)
                    return;
                var currentlySelected = fp.rContainer.querySelectorAll(".flatpickr-monthSelect-month.selected");
                currentlySelected.forEach(function (month) {
                    month.classList.remove("selected");
                });
                var month = fp.rContainer.querySelector(".flatpickr-monthSelect-month:nth-child(" + (fp.currentMonth + 1) + ")");
                if (month) {
                    month.classList.add("selected");
                }
            }
            function selectYear() {
                var selectedDate = fp.selectedDates[0];
                selectedDate.setFullYear(fp.currentYear);
                fp.setDate(selectedDate, true);
            }
            function selectMonth(e) {
                e.preventDefault();
                e.stopPropagation();
                setMonth(e.target.dateObj);
            }
            function setMonth(date) {
                var selectedDate = new Date(date);
                selectedDate.setFullYear(fp.currentYear);
                fp.currentMonth = selectedDate.getMonth();
                fp.setDate(selectedDate, true);
                setCurrentlySelected();
            }
            var shifts = {
                37: -1,
                39: 1,
                40: 3,
                38: -3
            };
            function onKeyDown(_, __, ___, e) {
                var shouldMove = shifts[e.keyCode] !== undefined;
                if (!shouldMove && e.keyCode !== 13) {
                    return;
                }
                if (!fp.rContainer || !self.monthsContainer)
                    return;
                var currentlySelected = fp.rContainer.querySelector(".flatpickr-monthSelect-month.selected");
                var index = Array.prototype.indexOf.call(self.monthsContainer.children, document.activeElement);
                if (index === -1) {
                    var target = currentlySelected || self.monthsContainer.firstElementChild;
                    target.focus();
                    index = target.$i;
                }
                if (shouldMove) {
                    self.monthsContainer.children[(12 + index + shifts[e.keyCode]) % 12].focus();
                }
                else if (e.keyCode === 13 &&
                    self.monthsContainer.contains(document.activeElement)) {
                    setMonth(document.activeElement.dateObj);
                }
            }
            function destroyPluginInstance() {
                if (self.monthsContainer !== null) {
                    self.monthsContainer
                        .querySelectorAll(".flatpickr-monthSelect-month")
                        .forEach(function (element) {
                        element.removeEventListener("click", selectMonth);
                    });
                }
            }
            return {
                onParseConfig: function () {
                    fp.config.mode = "single";
                    fp.config.enableTime = false;
                },
                onValueUpdate: setCurrentlySelected,
                onKeyDown: onKeyDown,
                onReady: [
                    clearUnnecessaryDOMElements,
                    addListeners,
                    addMonths,
                    setCurrentlySelected,
                    function () {
                        fp.loadedPlugins.push("monthSelect");
                    },
                ],
                onDestroy: destroyPluginInstance
            };
        };
    }

    return monthSelectPlugin;

}));
