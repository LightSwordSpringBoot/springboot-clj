# Clojure 与 SpringBoot


## Clojure 使用 SpringBoot

### 新建 maven 工程

pom 配置:

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.brandon</groupId>
  <artifactId>springboot-clj</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>demo</name>
  <description>Demo project for Spring Boot</description>

  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.2.5.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
  </parent>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <java.version>1.8</java.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-rest</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-jersey</artifactId>
    </dependency>

    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>com.offbytwo.iclojure</groupId>
      <artifactId>iclojure</artifactId>
      <version>1.2.0</version>
    </dependency>
    <dependency>
      <groupId>org.clojure</groupId>
      <artifactId>clojure</artifactId>
      <version>1.7.0</version>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>com.theoryinpractise</groupId>
        <artifactId>clojure-maven-plugin</artifactId>
        <version>1.7.1</version>
        <extensions>true</extensions>
        <executions>
          <execution>
            <id>compile-clojure</id>
            <phase>compile</phase>
            <goals>
              <goal>compile</goal>
            </goals>
          </execution>
          <execution>
            <id>test-clojure</id>
            <phase>test</phase>
            <goals>
              <goal>test</goal>
            </goals>
          </execution>
        </executions>
      </plugin>

      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>
  <repositories>
    <repository>
      <id>clojars.org</id>
      <url>http://clojars.org/repo</url>
    </repository>
  </repositories>

</project>


```

### 工程目录

```
.
|____main
| |____clojure
| | |____org
| | | |____brinman2002
| | | | |____app
| | | | | |____Application.clj
| | | | | |____service
| | | | | | |____SimpleService.clj
| |____java
| | |____org
| | | |____brinman2002
| | | | |____app
| | | | | |____controller
| | | | | | |____JavaBasedController.java
| | | | | |____domain
| | | | | | |____Player.java
| | | | | |____service
| | | | | | |____PlayerRepository.java
| |____resources
| | |____application.properties


```

项目源代码: https://github.com/universsky/springboot-clj


## 光剑说

在使用 Clojure 与 SpringBoot 混合开发的过程中,发现代码不太优雅,显得不伦不类,蹩脚的代码.没有 Scala 与 SpringBoot 融合的那么优雅.

我们看看两者代码的风格对比

### Scala

```
package com.springboot.in.action

import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class AppConfig

```

```
package com.springboot.in.action

import org.springframework.boot.SpringApplication

object LightSwordApplication extends App {
  SpringApplication.run(classOf[AppConfig])
}
```

### Clojure

```
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
  
  

```


看到那么多的 ^, [], () ,还有java 类的冗长的全路径代码.有点迷乱.


### 写个 Controller


其实总体上看,也还好.看习惯也加觉得好看了.

```
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

```

但是相比 Scala 与 Java 的融合,毕竟 Clojure 离 OOP 是有点远的.所以代码看起来不是那么的优雅.


```
package com.springboot.in.action.controller

import java.util.Date

import com.alibaba.fastjson.JSONObject
import org.springframework.web.bind.annotation.{RequestMapping, RequestParam, RestController}

/**
 * Created by jack on 16/6/24.
 *
 * 系统内部测试用
 */

@RestController
class HelloController {

  @RequestMapping(Array("/hello"))
  def greeting(@RequestParam(value="name", defaultValue="LightSword")  name: String) = {
    val now = new Date
    val content =  "Hello, " + name + "! Now is: " + now

    val json = new JSONObject
    json.put("conent", content)

    json

  }

}


```