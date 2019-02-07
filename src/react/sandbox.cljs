(ns ^:figwheel-hooks react.sandbox)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn e
  [el props & children]
  (apply js/React.createElement el (clj->js props) children))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn Example
  []
  (let [[count setCount] (js/React.useState 0)]
    (e "div" nil
       (e "p" nil "You clicked " count " times")
       (e "button"
          {:onClick
           (fn [e]
             (setCount (inc count)))}
          "Click Me"))))

(defn mount
  []
  (js/ReactDOM.render (e Example {})
                      (js/document.getElementById "app")))

;; This is called once
(defonce init (mount))

(defn ^:after-load reload []
  (mount))
