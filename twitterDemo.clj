(ns WonderfulCoyote-twitterDemo
    (:use [twitter.oauth]
          [twitter.callbacks]
          [twitter.callbacks.handlers]
          [twitter.api.restful])
  (:import [twitter.callbacks.protocols SyncSingleCallback])
)

(def my-creds (make-oauth-creds "cnsmr-key"
                                "cnsmr-secrt"
                                "accss-key"
                                "accss-secrt"
                                ))
;; Get user
(users-show :oauth-creds my-creds :params {:screen-name "POTUS"})

;; Get some Tweets
(map
  ;; Notice that get/get-in can be used to easily and clearly access nested pieces of the nested map/JSON
  (fn [x] [(x :text) (map #(get % :text) (get-in x [:entities :hashtags])) (x :retweet_count) (x :favorite_count) (x :id)])
  ;; We use get-in body/statuses to get the tweets (Twitter calls these "statuses") in the "body" of the HTTP response
  (get-in (search-tweets :oauth-creds my-creds :params {:q "#friday" :count 100}) [:body :statuses])
)

;; Do some counting

;; Start with let, just to take the search business out of the way
(let [tweets   (get-in (search-tweets :oauth-creds my-creds :params {:q "friday" :count 100}) [:body :statuses])]
 ;; Return first element as first elem and count number of tweets for second elem
 (map (fn [x] [(get x 0) (count (get x 1))])
 ;; Group tweets by the number of hashtags they use   
 (group-by (fn [x] (count (get-in x [:entities :hashtags]))) tweets))
)


;; If we think we'll want to do this a bunch, we can create a function
(defn countByHashtagSearch 
  "Takes string and returns a 100-sample hashtag-use distribution"
  ;; Notice that we've overloaded the function definition here
  ;; You *can* specificy the number of tweets you'd like -- otherwise it defaults to 10
  ([my-string] (countByHashtagSearch my-string 10))
  ([my-string n]
  (let [tweets   (get-in (search-tweets :oauth-creds my-creds :params {:q my-string :count n}) [:body :statuses])]
  (map (fn [grp] [(get grp 0) (count (get grp 1))])
  (group-by (fn [twt] (count (get-in twt [:entities :hashtags]))) tweets)))))

;; Do those counts
  
(countByHashtagSearch "clojure" 100)
(countByHashtagSearch "scala" 100)
(countByHashtagSearch "haskell" 100)
(countByHashtagSearch "lisp" 100)

