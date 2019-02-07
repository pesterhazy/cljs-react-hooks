(ns ^:figwheel-hooks react.sandbox
  (:require-macros [react.sandbox :refer [html]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn e
  [el props & children]
  (apply js/React.createElement el (clj->js props) children))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn counter-reducer
  [state action]
  (case (:type action)
    :inc (update state :count inc)
    :dec (update state :count dec)))

(defn Counter
  [initial-state]
  (let [[state dispatch] (js/React.useReducer counter-reducer
                                              (js->clj initial-state :keywordize-keys true))]
    (html
     [:*
      "Count: " (:count state)
      [:button {:onClick #(dispatch {:type :inc})} "+"]
      [:button {:onClick #(dispatch {:type :dec})} "-"]])))

(defn mount
  []
  (js/ReactDOM.render (e Counter {:count 7})
                      (js/document.getElementById "app")))

;; This is called once
(defonce init (mount))

(defn ^:after-load reload []
  (mount))
