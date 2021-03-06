
name := """play-java"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "redis.clients" % "jedis" % "2.8.1",
  ("org.apache.zookeeper" % "zookeeper" % "3.4.6").exclude("org.slf4j", "slf4j-log4j12").exclude("log4j", "log4j"),
  "commons-io" % "commons-io" % "2.3",
  "org.apache.bval" % "bval-jsr303" % "0.5",
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "org.mongodb" % "mongo-java-driver" % "3.2.2",
  "org.mongodb" % "mongodb-driver-async" % "3.2.2",
  "mysql" % "mysql-connector-java" % "5.1.40",
  "com.qiniu" % "sdk" % "6.1.7",
  "org.apache.httpcomponents" % "httpclient" % "4.5.2",
  "org.apache.httpcomponents" % "httpcore" % "4.4.4",
  "com.maxmind.geoip" % "geoip-api" % "1.3.0",
  "org.ahocorasick" % "ahocorasick" % "0.2.4",
  "org.apache.poi" % "poi-ooxml" % "3.14",
  "org.quartz-scheduler" % "quartz" % "2.2.1",
  "org.quartz-scheduler" % "quartz-jobs" % "2.2.1",
  "com.alibaba" % "dubbo" % "2.5.3",
  ("com.github.sgroschupf" % "zkclient" % "0.1").exclude("org.slf4j", "slf4j-log4j12").exclude("log4j", "log4j"),
  "org.influxdb" % "influxdb-java" % "2.2",
  "org.elasticsearch" % "elasticsearch" % "2.2.1",
  "org.mockito" % "mockito-core" % "2.2.11" % "test",
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.3",
  "io.netty" % "netty-all" % "4.1.6.Final",
  "com.maxmind.geoip2" % "geoip2" % "2.8.0",
  ("biz.paluch.redis" % "lettuce" % "4.1.1.Final" classifier "shaded").exclude("io.netty", "netty-common").exclude("io.netty", "netty-transport").exclude("com.google.guava", "guava").exclude("org.apache.commons", "commons-pool2")
)
