(ns do-it.components.common
  (:require [reagent.core :as r]))

(def ReactNative (js/require "react-native"))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def text-input (r/adapt-react-class (.-TextInput ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def touchable-opacity (r/adapt-react-class (.-TouchableOpacity ReactNative)))
(def modal (r/adapt-react-class (.-Modal ReactNative)))
(def activity-indicator (r/adapt-react-class (.-ActivityIndicator ReactNative)))

(defn header [header-text]
  (let [view-style {:background-color "#F8F8F8"
                    :justify-content "center"
                    :align-items "center"
                    :height 60
                    :padding-top 15
                    :shadow-color "#000"
                    :shadow-offset {:width 0, :height 2}
                    :shadow-opacity 0.2
                    :elevation 2,
                    :position "relative"}]
    [view {:style view-style}
     [text {:style {:font-size 20}} header-text]]))

(defn button [on-press button-text]
  (let [text-style {:align-self "center"
                    :color "#007aff"
                    :font-size 16
                    :font-weight "600"
                    :padding-top 10
                    :padding-bottom 10}
        button-style {:flex 1
                      :align-self "stretch"
                      :background-color "#fff"
                      :border-radius 5
                      :border-width 1
                      :border-color "#007aff"
                      :margin-left 5
                      :margin-right 5}]
    [touchable-opacity {:on-press on-press
                        :style button-style}
     [text {:style text-style} button-text]]))

(defn card-section []
  (let [this (r/current-component)
        style (:style (r/props this))
        card-style {:border-bottom-width 1
                    :padding 5
                    :background-color "#fff"
                    :justify-content "flex-start"
                    :flex-direction "row"
                    :border-color "#ddd"
                    :position "relative"}]
    (into [view {:style (merge card-style style)}]
          (r/children this))))

(defn card [& children]
  (let [style {:border-width 1
               :border-radius 2
               :border-color "#ddd"
               :border-bottom-width 0
               :shadow-color "#000"
               :shadow-offset {:width 0, :height 2}
               :shadow-opacity 0.1
               :shadow-radius 2
               :elevation 1
               :margin-left 5
               :margin-right 5
               :margin-top 10}]
    [view {:style style}
     (map-indexed #(with-meta %2 {:key %1}) children)]))

(defn input
  [{:keys [label placeholder value secure-text-entry on-change-text]}]
  (let [input-style {:color "#000"
                     :padding-right 5
                     :padding-left 5
                     :font-size 18
                     :line-height 23
                     :flex 2}
        label-style {:font-size 19
                     :padding-left 20
                     :flex 1}
        container-style {:height 40
                         :flex 1
                         :flex-direction "row"
                         :align-items "center"}]
    (fn []
      [view {:style container-style}
       [text {:style label-style} label]
       [text-input {:style input-style
                    :secure-text-entry secure-text-entry
                    :auto-correct false
                    :placeholder placeholder
                    :value @value
                    :auto-capitalize "none"
                    :on-change-text (fn [value]
                                      (on-change-text value)
                                      (r/flush))}]])))

(defn confirm []
  (let [this (r/current-component)
        {:keys [on-accept on-decline visible]} (r/props this)
        card-section-style {:justify-content "center"}
        text-style {:flex 1
                    :font-size 18
                    :text-align "center"
                    :line-height 40}
        container-style {:background-color "rgba(0, 0, 0, 0.75)"
                         :position "relative"
                         :flex 1
                         :justify-content "center"}]
    [modal {:animation-type "slide"
            :on-request-close #()
            :transparent true
            :visible visible}
     [view {:style container-style}
      [card-section {:style card-section-style}
       (into [text {:style text-style}]
             (r/children this))]
      [card-section
       [button on-accept "Yes"]
       [button on-decline "No"]]]]))

(defn spinner
  ([] (spinner "large"))
  ([size]
   (let [style {:flex 1
                :justify-content "center"
                :align-items "center"}]
     [view {:style style}
      [activity-indicator {:size size}]])))
