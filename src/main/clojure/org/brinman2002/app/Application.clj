(ns org.brinman2002.app.Application
  (:import (org.springframework.boot.autoconfigure SpringBootApplication)
           (org.springframework.boot SpringApplication)
           ))

(gen-class
  :name ^{SpringBootApplication []} org.brinman2002.app.Application
  :main true)

(defn -main
  []
  (SpringApplication/run (Class/forName "org.brinman2002.app.Application") (into-array String '())))
  
  
