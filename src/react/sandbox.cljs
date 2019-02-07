(ns ^:figwheel-hooks react.sandbox
  (:require-macros [react.sandbox :refer [html]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn e
  [el props & children]
  (apply js/React.createElement el (clj->js props) children))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn ExampleHicada
  []
  (let [[count setCount] (js/React.useState 0)]
    (html
     [:div
      [:p "You clicked the button " count " times"]
      [:button {:onClick (fn [e]
                           (setCount (inc count)))}
       "Click Me"]])))

(defn mount
  []
  (js/ReactDOM.render (e ExampleHicada {})
                      (js/document.getElementById "app")))

;; This is called once
(defonce init (mount))

(defn ^:after-load reload []
  (mount))
