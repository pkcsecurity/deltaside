(ns deltaside.css
  (:require [garden.core :as css]))

(defn css [href]
  [:link {:rel :stylesheet :href href}])

(def csses
  ["/css/reset.min.css"
   "/css/basscss.min.css"
   "/css/animate.css"
   "/css/fontawesome-all.min.css"
   "https://use.typekit.net/duk7ddv.css"])

(def html-font-size "16px")

(defn font-settings [class [size line-height]]
  (let [class (name class)
        props {:font-size (str size "px")
               :line-height (str line-height "px")}]
    (list
      [class props]
      [(str "." class) props])))

(def font-sizes
  {:body [14 24]
   :p [14 24]
   :h1 [32 34]
   :h2 [24 28]
   :h3 [20 24]
   :h4 [16 22]
   :h5 [14 18]
   :h6 [13 16]
   :small [12 18]})

(def colors
  {:pri "#2a2f37"
   :sec "#00bbdd"
   :black "#000"
   :white "#fff"
   :gray-dark "#999"
   :gray "#dfdfdf"
   :info "#3ff"
   :info-dark "#37cefd"
   :success "#33ccff"
   :success-dark "#0bd"
   :warning "#ffcc00"
   :warning-dark "#ffcc00"
   :error "#ffcccc"
   :error-dark "#cc3300"})

(def shadows
  {:s1 "0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24)"
   :s2 "0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23)"
   :s3 "0 10px 20px rgba(0,0,0,0.19), 0 6px 6px rgba(0,0,0,0.23)"
   :s4 "0 14px 28px rgba(0,0,0,0.25), 0 10px 10px rgba(0,0,0,0.22)"
   :s5 "0 19px 38px rgba(0,0,0,0.30), 0 15px 12px rgba(0,0,0,0.22)"})

(defn color-settings [c hex]
  (let [c (name c)
        color hex]
    (list
      [(str "." c) {:color color}]
      [(str ".border-" c) {:border-color color}]
      [(str ".bg-" c) {:background-color color}])))

(defn shadow-settings [c setting]
  [(str "." (name c)) {:box-shadow setting}])

(def border-widths
  (map
    (fn [i] [(keyword (str ".b" i)) {:border-width (str i "px")}])
    (range 1 5)))

(def styles
  (css/css {:pretty-print? true
            :vendors ["ms" "webkit" "moz" "webkit-touch"]
            :auto-prefix #{:user-select}}
    [:* {:text-rendering "optimizelegibility"
         :box-sizing :border-box
         :margin 0
         :padding 0}]
    [:html :body :#app :#body
     {:height "100%"
      :width "100%"
      :min-width "250px"
      :color "#999"
      :font-family ["Helvetica" :sans-serif]
      :font-size html-font-size
      :background-color :white}]

    [:.placeholder-pri
     ".placeholder-pri::placeholder"
     ".placeholder-pri::-webkit-input-placeholder" {:color "#e0e0e0"}]
    [:.placeholder-error
     ".placeholder-error::placeholder"
     ".placeholder-error::-webkit-input-placeholder" {:color "white"}]

    [:hr {:background "#000"
          :border 0
          :height "1px"}]
    [:h1 :h2 :h3 :.h1 :.h2 :.h3 {:font-weight 700}]
    [:h4 :h5 :.h4 {:font-weight 600}]
    [:h5 :.h5 :h6 :.h6 {:font-weight 400}]
    [:h1 :h2 :h3 :h4 :h5 :h6
     :.h1 :.h2 :.h3 :.h4 :.h5 :.h6
     {:font-family ["FreightSans Pro" :sans-serif]}]
    [:p :.p {:font-family ["Helvetica" :sans-serif]}]
    [:.open-sans {:font-family ["Open Sans" :sans-serif]}]
    [:a {:text-decoration :none
         :color :inherit}]
    [:.link {:color "#0bd"}]
    [:small {:font-size "80%"
             :line-height 1.5}]
    [:.upper {:text-transform :uppercase
              :letter-spacing "0.8px"}]
    [:.fade {:opacity 0.3}]
    [:.invisible {:opacity 0}]
    [:.darken {:-webkit-filter "brightness(65%)"}]
    border-widths
    [:.transition {:transition [[:all "0.15s" :ease-in-out]]}]
    [:.transition-long {:transition [[:all "0.5s" :ease-in-out]]}]
    [:.pointer {:cursor :pointer}]
    [:.pointer-none {:cursor :not-allowed}]
    [:.outline-none {:outline 0}]
    [:.border-wide {:border-width "2px"}]
    [:.select-text {:user-select :text}]
    [:.select-none {:user-select :none}]
    [:.side-bar-width {:max-width "18em"}]
    ["button::-moz-focus-inner" {:border 0}]
    ["input::-moz-focus-inner" {:border 0}]
    ["div::-moz-focus-inner" {:border 0}]
    (for [[class shadow] shadows]
      (shadow-settings class shadow))
    (for [[class hex] colors]
      (color-settings class hex))
    [:.bg-trans {:background-color :transparent}]
    [:.border-trans {:border-color :transparent}]
    [:.trans {:color :transparent}]
    (for [[tag settings] font-sizes]
      (font-settings tag settings))))
