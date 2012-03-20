(ns clojure-tictactoe.core
  (:import MinMaxPlayer
           Move
           java.io.File)
  (:use [clojure-webserver
         option-parser
         http-server
         request-handler]))

(defn str-to-token [token]
  (if (= token \_)
    nil
    token))

(defn char-to-token [character]
  (if (= character nil)
    nil
    (str character)))

(defn token-to-str [token]
  (if (= nil token)
    "_"
    token))

(defn string-to-board [string]
  (->> (.substring string 1)
    (map str-to-token)
    (map char-to-token)
    (into-array String)))

(defn move-to-index [move]
  (let [row    (.getRow move)
        column (.getColumn move)]
    (+ (* row 3) column)))

(defn new-board [board move]
  (let [index (move-to-index move)
        token "O"
        board (vec board)]
  (->> (assoc board index token)
    (map token-to-str)
    (reduce str ""))))

(defn move-handler [env]
  (let [board   (string-to-board (:uri env))
        ai-move (.nextMove (MinMaxPlayer. board "O" "X"))]
    {:status 200
     :headers {}
     :body (new-board board ai-move)}))

(defn make-dispatcher [file-handler move-handler]
  (fn [env]
    (cond
      (.startsWith (:uri env) "/public")
        (file-handler env)
      (= (:uri env) "/")
        (file-handler (assoc env :uri "/public/index.html"))
      :otherwise
        (move-handler env))))

(defn -main [& args]
  (let [[directory port] (parse-options args)
        file-handler     (request-handler (File. directory))
        request-dispatch (make-dispatcher file-handler move-handler)]
    (start-server port request-dispatch)))
