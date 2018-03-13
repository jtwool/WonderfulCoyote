;; This is a demonstration of a simple StanfordCoreNLP
;; Running this file returns part of speech tags for the first sentence below
;; (PRP MD VB PRP IN DT NNP NNP .)
;; (c) JT Wolohan, 2018, GPL 3.0
(ns playground-clj.core
  (:gen-class)
  (:require [clj-json.core :as json])
  (:import  [edu.stanford.nlp.pipeline StanfordCoreNLP]))

(defn -main
  [& args]

(defn CoreNLP 
  "StanfordCoreNLP class builder"
  [props]
  (let [ps (java.util.Properties.)]
    (. ps setProperty "annotators" props)
    (StanfordCoreNLP. ps)))

(defn pipe2JSON 
  "Runs text through a CoreNLP pipeline"
  [pipeline t]
  (let [txt (.process pipeline t)]
    ;; (. pipeline annotate txt)
    (let [buffer (java.io.StringWriter.)]
      (.jsonPrint pipeline txt buffer)
      (json/parse-string (.toString buffer)))))
      

;; create nlp pipeline
(def nlpipe (CoreNLP "tokenize, ssplit, pos, lemma"))

;; call lein run --
(let [a  (pipe2JSON nlpipe "I'll meet you by the Sample Gates. Do you know where that is?") ]
  (println (map (fn [x] (get x "pos"))
  (get (get (get a "sentences") 0) "tokens"))))
)
