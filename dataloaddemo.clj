(comment
;; This is the build info
;; Replace your project.clj with this
;; You'll want to put this file in the src folder 
;; in a subfolder called WonderfulCoyote
(defproject data-load-demo "0.1"
  :description "Loading Data in Clojure Demo"
  :url "www.jtwolohan.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                [com.github.kyleburton/clj-xpath "1.4.3"]
                [ultra-csv "0.2.1"]
                [clj-json "0.5.3"]]
  :main ^:skip-aot WonderfulCoyote.dataloaddemo
  :plugins [[lein-gorilla "0.4.0"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
)

(ns WonderfulCoyote.dataloaddemo
  (:gen-class)
  (:require [ultra-csv.core :as csv]
            [clj-xpath.core :as xpath]
            [clj-json.core :as json]
            ))
            
(defn -main        
  [& args]

;; CSV

;; Load CSV data
;; Data is the canonical Iris dataset, hosted by UCI Machine Learning Repository
;; https://archive.ics.uci.edu/ml/machine-learning-databases/iris/iris.data
(def iris-data (csv/read-csv "https://archive.ics.uci.edu/ml/machine-learning-databases/iris/iris.data"))
;; Show first row of data
(println (first iris-data))
;; Get label of all observations where sepal width > 3.5
(println (map last (filter #(> (get % 1) 3.5) iris-data)))

;; JSON

;; Load JSON data
;; Data is the Paper Reviews dataset, hosted by UCI Machine Learning Repository
;; http://archive.ics.uci.edu/ml/machine-learning-databases/00410/reviews.json
(def papers-data (json/parse-string (slurp "http://archive.ics.uci.edu/ml/machine-learning-databases/00410/reviews.json")))
;; Show first paper
(println (-> (first papers-data)
  (get 1) ;; Get papers
  (get 0)) ;; Get first papers
)

  ;; Get counts of decision types for all papers
(println (reduce #(update %1 (get %2 "preliminary_decision") (fnil inc 0)) ;; "wordcount" function
  {} 
  (get (first papers-data) 1)))
  
;; XML

;; Load XML data
;; Data is current build file for Clojure, hosted by Github
;; https://github.com/clojure/clojure/blob/master/build.xml
(def build-xml (xpath/xml->doc (slurp "https://raw.githubusercontent.com/clojure/clojure/master/build.xml")))
;; Parse with xpath and return some top-level tags.
(println (xpath/$x:text* "./*/*/@name" build-xml))
)
