(ns clojure-tictactoe.core
  (:import java.io.File)
  (:use [clojure-webserver option-parser http-server request-handler])
  (:use [clojure-tictactoe.move-handler :only [move-handler]]))

(defn make-dispatcher [file-handler move-handler]
  (fn [env]
    (let [request-string (:uri env)]
      (cond
        (.startsWith request-string "/public")
          (file-handler env)
        (= request-string "/")
          (file-handler (assoc env :uri "/public/index.html"))
        :otherwise
          (move-handler env)))))

(defn -main [& args]
  (let [[directory port] (parse-options args)
        file-handler     (request-handler (File. directory))
        request-dispatch (make-dispatcher file-handler move-handler)]
    (start-server port request-dispatch (fn [server]
      (println "Server is listening on port " port)
      (println "Server is serving static files from " directory)
      (println "Press ^C to stop the server")))))
