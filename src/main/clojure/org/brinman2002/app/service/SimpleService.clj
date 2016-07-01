(ns org.brinman2002.app.service.SimpleService
  (:import (org.springframework.web.bind.annotation RestController RequestMapping RequestMethod)
           (org.springframework.beans.factory.annotation Autowired)
           (org.brinman2002.app.service PlayerRepository)))

;; Class definition

(gen-class
  :name ^{RestController {} 
          RequestMapping {:value ["lightsword"]} } org.brinman2002.app.service.SimpleService
  :methods [[^{RequestMapping {:value ["greet"] :method [RequestMethod/GET]}} greet [] Object ]
            [^{RequestMapping {:value ["greet2"] :method [RequestMethod/GET]}} greet2 [] Object ]
            [^{RequestMapping {:value ["getResult"] :method [RequestMethod/GET]}} getResult [] Object]
            [^{Autowired {}} setPlayerRepository [org.brinman2002.app.service.PlayerRepository] void]]
  :state injected
  :init init)



;; Business logic functions

; TODO

;; Class method implementations
(defn -init 
  "Initialize the class by setting the state to an empty map, which can be populated with injected dependencies."
  []
  [[] (atom {})])

(defn -setPlayerRepository
  "Setter for player repository.  This stores the instance in the state of the object."
  [this repo]
  (swap! (.injected this) assoc-in [:player-repo] repo))


;; Response types

(defrecord Address [^String street
                    ^String city])

(defrecord GreetResponse [^String person
                          ^String stuff
                          ^long id
                          ^Address address])

(defrecord TestResult [
                       ^long id
                       ^int testSuiteId
                       ^String name
                       ^String actualOutput
                       ^int state
                       ])

(defn -greet
  "Handler for the /lightsword/greet resource using defrecord."
  [this]
  (GreetResponse. "Jack" "Software Engineer" 12345 (Address. "蒋村花园如意苑" "杭州")))

  ;(str "This is a greeting " (:player-repo @(.injected this) )))

(defn -greet2
  "Handler for the /lightsword/greet2 resource using maps. This actually doesn't seem to work; it throws an ArityException because Spring seems to treat it as a Callable instead of a JSON-able object."
  [this]
  {:test "This is a test" :of "returning raw maps"})


(defn -getResult
  [this]
  (TestResult. 1001 2 "登陆测试用例" "{\"sucesss\":true}" 1))