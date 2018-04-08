(ns wonderfulcoyote.gndrprdct
  (:gen-class)
  (:require [ultra-csv.core :as ucsv]
            [clojure.string :as str])
  )

(def SapWeights
  (reduce
    (fn [a b] (assoc a (b :term) (b :weight)))
    {}
    (ucsv/read-csv "./resources/gender_lex.csv")))

(defn predictgender
 "Predict-tweet author gender from text. 0 is male, 1 is female"
 [t]
 (let [g
 ;; Take the sine 
   (Math/sin
   ;; Subtract the intercept
     (- 0.067242152
     ;; Sum them
     (reduce +
     ;; Lookup values for each word
       (map #(get SapWeights % 0)
       ;; Tokenization
         (str/split (str/lower-case t) #"\s+")))))]
 ;; Assign appropriate gender  
 (if (<= g 0) 0 1))
)

(defn -main
  "Tweet text expected as single user per line"
  [& args]
  (let [fp  (first *command-line-args* )]
  (with-open [rdr (clojure.java.io/reader fp)]
    (doseq [line (line-seq rdr)]
      (println (predictgender line))
))))

