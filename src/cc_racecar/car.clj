(ns cc-racecar.car
  (:require [org.httpkit.client :as http]
            [clojure.string :as str]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.math :as math]
            [cheshire.core :as cc])
  (:import (java.net URLEncoder URLDecoder)))

(def url "http://localhost:3002")
(def register-url (str url "/register"))
(def move-url (str url "/move"))

(defn- post [url body]
  (let
    [{:keys [status body]}
     @(http/request
        {:url url
         :method :post
         :headers {"content-type" "application/json"}
         :body (cc/generate-string body)})]
    (if (>= status 400)
      (if (= status 403)
        body
        (throw "Error"))
      (cc/decode body keyword))))

(defn register [name]
  (post register-url {:username name}))

(defn move [id direction]
  (post move-url {:playerId id :direction direction}))