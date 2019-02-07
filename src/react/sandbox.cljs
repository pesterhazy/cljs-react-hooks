(ns ^:figwheel-hooks react.sandbox
  (:require [react :as react] ;; presumably for side-effects
            [react-dom :as react-dom]))

(defn e
  [el props & children]
  (apply js/React.createElement el (clj->js props) children))

(defn mount
  []
  (js/ReactDOM.render
   (e "div" nil "Hello, world!!")
   (js/document.getElementById "app")))

;; This is called once
(defonce init (mount))

(defn ^:after-load reload []
  (mount))
