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

(defn Effect
  []
  (let [[count setCount] (js/React.useState 0)]
    (js/React.useEffect (fn []
                          (set! (.-title js/document)
                                (str "You clicked " count " times"))
                          identity))

    (html
     [:div
      [:p "You clicked " count " times"]
      [:button {:onClick (fn [e] (setCount (inc count)))}
       "Click Me"]])))

(defn EmojiKeys
  []
  (let [happyPress (js/useKeyPress "h")
        sadPress (js/useKeyPress "s")
        robotPress (js/useKeyPress "r")
        foxPress (js/useKeyPress "f")]
    (html
     [:div
      [:div
       "["
       (when happyPress "😊")
       (when sadPress "😢")
       (when robotPress "🤖")
       (when foxPress "🦊")
       "]"]
      [:div "h, s, r, f"]])))

(defn useLens
  [a f]
  (let [[value update-value] (js/React.useState (f @a))]
    (js/React.useEffect
     (fn []
       (let [k (gensym "useLens")]
         (add-watch a k
                    (fn [_ _ _ new-state]
                      (update-value (f new-state))))
         (fn []
           (remove-watch a k)))))
    value))

(defonce state-atom (atom {:foo 1
                           :bar 1
                           :baz 1}))

(defn Stateful
  []
  (let [x (useLens state-atom #(* (:foo %) (:bar %)))
        all (useLens state-atom identity)]
    (html
     [:div
      "foo*bar = " [:strong x]
      [:code [:pre (prn-str all)]]
      [:button
       {:onClick #(swap! state-atom update :foo inc)}
       "More foo"]
      [:button
       {:onClick #(swap! state-atom update :bar inc)}
       "More bar"]
      [:button
       {:onClick #(swap! state-atom update :baz inc)}
       "More baz"]])))

(defn mount
  []
  (js/ReactDOM.render (e Stateful {})
                      (js/document.getElementById "app")))

;; This is called once
(defonce init (mount))

(defn ^:after-load reload []
  (mount))
