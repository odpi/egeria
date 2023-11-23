;; SPDX-License-Identifier: Apache-2.0
;; Copyright Contributors to the ODPi Egeria project.
;;
;; Based on original work licensed as follows:
;; The MIT License (MIT)
;;
;; Copyright © 2018-2021 JUXT LTD.
;;
;; Permission is hereby granted, free of charge, to any person obtaining a copy of
;; this software and associated documentation files (the "Software"), to deal in
;; the Software without restriction, including without limitation the rights to
;; use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
;; the Software, and to permit persons to whom the Software is furnished to do so,
;; subject to the following conditions:
;;
;; The above copyright notice and this permission notice shall be included in all
;; copies or substantial portions of the Software.
;;
;; THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
;; IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
;; FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
;; COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
;; IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
;; CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

(ns xtdb.lucene.egeria
  (:require [clojure.spec.alpha :as s]
            [xtdb.codec :as cc]
            [xtdb.system :as sys]
            [xtdb.query :as q]
            [xtdb.lucene :as l])
  (:import org.apache.lucene.analysis.Analyzer
           [org.apache.lucene.analysis.core KeywordAnalyzer KeywordTokenizerFactory LowerCaseFilterFactory]
           [org.apache.lucene.analysis.custom CustomAnalyzer]
           [org.apache.lucene.document Document Field$Store StringField TextField]
           [org.apache.lucene.index IndexWriter Term]
           org.apache.lucene.queryparser.classic.QueryParser
           [org.apache.lucene.search BooleanClause$Occur BooleanQuery$Builder Query TermQuery]))

;; Used to store exact-match strings (no tokenization)
(defn ^String keyword->kcs [k]
  (subs (str k "-exact") 1))
(def ^:const ^:private field-xtdb-val-exact "_xtdb_val_exact")

;; Custom Egeria indexes
(defrecord EgeriaIndexer [index-store]
  l/LuceneIndexer

  (index! [_ index-writer docs]
    (doseq [{e :xtdb.db/id, :as xtdb-doc} (vals docs)
            [a ^String v] (->> (dissoc xtdb-doc :xtdb.db/id)
                               (mapcat (fn [[a v]]
                                         (for [v (cc/vectorize-value v)
                                               :when (string? v)]
                                           [a v]))))
            :let [id-str (l/->hash-str (l/->DocumentId e a v))
                  doc (doto (Document.)
                            ;; To search for triples by e-a-v for deduping
                            (.add (StringField. l/field-xt-id, id-str, Field$Store/NO))
                            ;; The actual term, which will be tokenized
                            (.add (TextField. (l/keyword->k a), v, Field$Store/YES))
                            ;; The actual term, to be used for exact matches
                            (.add (StringField. (keyword->kcs a), v, Field$Store/YES))
                            ;; Used for wildcard searches (case-insensitive)
                            (.add (TextField. l/field-xt-val, v, Field$Store/YES))
                            ;; Used for wildcard searches (case-sensitive)
                            (.add (StringField. field-xtdb-val-exact, v, Field$Store/YES))
                            ;; Used for all wildcard searches (to resolve the matching property)
                            (.add (StringField. l/field-xt-attr, (l/keyword->k a), Field$Store/YES))
                            ;; Used for eviction
                            (.add (StringField. l/field-xt-eid, (l/->hash-str e), Field$Store/NO)))]]
      (.updateDocument ^IndexWriter index-writer (Term. l/field-xt-id id-str) doc)))

  (evict! [_ index-writer eids]
    (let [qs (for [eid eids]
               (TermQuery. (Term. l/field-xt-eid (l/->hash-str eid))))]
      (.deleteDocuments ^IndexWriter index-writer ^"[Lorg.apache.lucene.search.Query;" (into-array Query qs)))))

(defn ->egeria-indexer
  {::sys/deps {:index-store :xtdb/index-store}}
  [{:keys [index-store]}]
  (EgeriaIndexer. index-store))

;;
;; Case-sensitive search methods
;;
(defn ^Analyzer ->cs-analyzer
  [_]
  (KeywordAnalyzer.))

;; Note: we will simply ignore whatever analyzer is passed-through and use our own for the queries
(defn ^Query build-query-cs
  "Standard build query fn (case-sensitive), taking a single field/val lucene term string."
  [^Analyzer analyzer, [a v]]
  (when-not (string? v)
    (throw (IllegalArgumentException. "Lucene text search values must be String")))
  (let [qp (doto (QueryParser. (keyword->kcs a) (->cs-analyzer analyzer)) (.setAllowLeadingWildcard true))
        b (doto (BooleanQuery$Builder.)
                (.add (.parse qp v) BooleanClause$Occur/MUST))]
    (.build b)))

(defmethod q/pred-args-spec 'text-search-cs [_]
  (s/cat :pred-fn  #{'text-search-cs} :args (s/spec (s/cat :attr keyword? :v (some-fn string? symbol?))) :return (s/? :xtdb.query/binding)))

(defmethod q/pred-constraint 'text-search-cs [_ pred-ctx]
  (let [resolver (partial l/resolve-search-results-a-v (second (:arg-bindings pred-ctx)))]
    (l/pred-constraint #'build-query-cs resolver pred-ctx)))

;; Note: we will simply ignore whatever analyzer is passed-through and use our own for the queries
(defn ^Query build-query-cs-wildcard
  "Wildcard query builder (case-sensitive)"
  [^Analyzer analyzer, [v]]
  (when-not (string? v)
    (throw (IllegalArgumentException. "Lucene text search values must be String")))
  (let [qp (doto (QueryParser. field-xtdb-val-exact (->cs-analyzer analyzer)) (.setAllowLeadingWildcard true))
        b (doto (BooleanQuery$Builder.)
                (.add (.parse qp v) BooleanClause$Occur/MUST))]
    (.build b)))

(defmethod q/pred-args-spec 'wildcard-text-search-cs [_]
  (s/cat :pred-fn #{'wildcard-text-search-cs} :args (s/spec (s/cat :v string?)) :return (s/? :xtdb.query/binding)))

(defmethod q/pred-constraint 'wildcard-text-search-cs [_ pred-ctx]
  (l/pred-constraint #'build-query-cs-wildcard #'l/resolve-search-results-a-v-wildcard pred-ctx))

;;
;; Case-insensitive search methods
;;
(defn ^Analyzer ->ci-analyzer
  [_]
  (.build (doto (CustomAnalyzer/builder)
                (.withTokenizer ^String KeywordTokenizerFactory/NAME ^"[Ljava.lang.String;" (into-array String []))
                (.addTokenFilter ^String LowerCaseFilterFactory/NAME ^"[Ljava.lang.String;" (into-array String [])))))

;; Note: we will simply ignore whatever analyzer is passed-through and use our own for the queries
(defn ^Query build-query-ci
  "Standard build query fn (case-insensitive), taking a single field/val lucene term string."
  [^Analyzer analyzer, [a v]]
  (when-not (string? v)
    (throw (IllegalArgumentException. "Lucene text search values must be String")))
  (let [qp (doto (QueryParser. (l/keyword->k a) (->ci-analyzer analyzer)) (.setAllowLeadingWildcard true))
        b (doto (BooleanQuery$Builder.)
                (.add (.parse qp v) BooleanClause$Occur/MUST))]
    (.build b)))

(defmethod q/pred-args-spec 'text-search-ci [_]
  (s/cat :pred-fn  #{'text-search-ci} :args (s/spec (s/cat :attr keyword? :v (some-fn string? symbol?))) :return (s/? :xtdb.query/binding)))

(defmethod q/pred-constraint 'text-search-ci [_ pred-ctx]
  (let [resolver (partial l/resolve-search-results-a-v (second (:arg-bindings pred-ctx)))]
    (l/pred-constraint #'build-query-ci resolver pred-ctx)))

;; Note: we will simply ignore whatever analyzer is passed-through and use our own for the queries
(defn ^Query build-query-ci-wildcard
  "Wildcard query builder (case insensitive)"
  [^Analyzer analyzer, [v]]
  (when-not (string? v)
    (throw (IllegalArgumentException. "Lucene text search values must be String")))
  (let [qp (doto (QueryParser. l/field-xt-val (->ci-analyzer analyzer)) (.setAllowLeadingWildcard true))
        b (doto (BooleanQuery$Builder.)
                (.add (.parse qp v) BooleanClause$Occur/MUST))]
    (.build b)))

(defmethod q/pred-args-spec 'wildcard-text-search-ci [_]
  (s/cat :pred-fn #{'wildcard-text-search-ci} :args (s/spec (s/cat :v string?)) :return (s/? :xtdb.query/binding)))

(defmethod q/pred-constraint 'wildcard-text-search-ci [_ pred-ctx]
  (l/pred-constraint #'build-query-ci-wildcard #'l/resolve-search-results-a-v-wildcard pred-ctx))
