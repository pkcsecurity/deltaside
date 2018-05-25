(ns deltaside.cljs.utils
  (:require [clojure.string :as str]
            [goog.text.LoremIpsum]
            [goog.dom :as dom]
            [goog.format.EmailAddress :as addr]
            [goog.userAgent :as ua]
            [cljs.pprint :as pp]
            [cljs.spec.alpha :as s]))

(defn remove-nils [coll]
  (filter #(some? %) coll))

(def path (.-pathname js/location))

(defn set-path [path]
  (set! (.-pathname js/location) path))

(defn uid [] (str (gensym)))

(defn uid->elem [uid]
  (dom/getElement uid))

(defn focus-uid [uid]
  (when-let [elem (uid->elem uid)]
    (.focus elem)))

(defn off [a]
  (fn [] (reset! a false)))

(defn on [a]
  (fn [] (reset! a true)))

(defn toggle [a]
  (fn [] (swap! a not)))

(def not-blank? (complement str/blank?))

(def blank? str/blank?)

(defn only-letters? [t]
  (or (nil? t) (re-find #"^[a-zA-Z]*$" t)))

(defn less-than? [n t]
  (> n (count t)))

(def less-than-100? (partial less-than? 100))
(def less-than-50? (partial less-than? 50))

(def trim #(str/trim (if % % "")))

(def nop (constantly nil))

(defn pp-number [x]
  (.toLocaleString (js/Number. x)))

(defn ipsum-words [n]
  (take n
    (mapcat (comp #(clojure.string/split % #"[ \.\,]+") clojure.string/lower-case)
      (repeatedly #(.generateSentence (goog.text.LoremIpsum.))))))

(defn ipsum [n]
  (clojure.string/join " "
    (repeatedly n
      #(.generateSentence
         (goog.text.LoremIpsum.)))))
(defn truncate [s max-length]
  (if (> (count s) max-length)
    (let [sub (.substring s 0 max-length)
          idx (.lastIndexOf sub " ")]
      (str (.substring s 0 idx) "..."))
    s))

(defn blank-as-nil [s]
  (if (str/blank? s)
    nil
    s))

(defn on-event [obj ev f]
  (.addEventListener obj ev f))

(defn remove-event [obj ev f]
  (.removeEventListener obj ev f))

(defn parse-int [s]
  (let [maybe-int (js/parseInt s)]
    (when-not (.isNaN js/Number maybe-int)
      maybe-int)))

(defn string [x]
  (and (string? x)
    (not-blank? x)
    (< (count x) 256)))

(defn optional-string [x]
  (and (string? x)
    (or (str/blank? x)
      (string x))))

(defn email [x] (addr/isValidAddress x))

(defn password [x]
  (and (string x)
    (>= (count x) 8)
    (<= (count x) 256)))

(defn set-text-content [element-id content]
  (set! (.-textContent (uid->elem element-id)) content))

(defn dec-clamped [x min-value]
  (max (dec x) min-value))

(defn inc-clamped [x max-value]
  (min (inc x) max-value))

(def pp pp/pprint)

(defn spec-problems [kw->error-message spec m]
  (let [ed (s/explain-data spec m)]
    (into {}
      (map (fn [x] [x (get kw->error-message x (str "The " (name x) " field is invalid."))])
        (keep (fn [{[x] :in}] x)
          (::s/problems ed))))))

(defn no-problems? [problems k]
  (not (contains? problems k)))

(defn not-xs
  "Adds classes to make element full width when screen is phone sized (smaller
  than base's 'sm') and n wide otherwise."
  [n]
  {:class (apply str "col-12" (map #(str " " % "-col-" n) ["sm" "md" "lg"]))})

(defn utc->normal-date [utc-date]
  (let [date (.toLocaleDateString (js/Date. utc-date))]
    date))

(defn mobile? []
  (or ua/ANDROID
    ua/IOS
    ua/IPHONE
    ua/IPAD
    ua/IPOD
    ua/MOBILE))

(defn oxford-comma [coll]
  (cond
    (or (= 0 (count coll)) (nil? coll)) ""
    (= 1 (count coll)) (str (first coll))
    (= 2 (count coll)) (str (first coll) " and " (second coll))
    :else (let [strings (interpose ", " coll)]
            (str
              (clojure.string/join (take (- (count strings) 1) strings))
              "and "
              (first (take-last 1 strings))))))

