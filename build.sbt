name := "multiexchange"

version := "1.0"

scalaVersion := "2.12.1"

resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.8.0",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.8.2",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.8.6",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.2",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.6",
  "com.github.akarnokd" % "rxjava2-interop" % "0.8.3",
  "com.github.mmazi" % "rescu" % "1.9.0",
  "com.google.code.findbugs" % "jsr305" % "1.3.9",
  "com.google.code.findbugs" % "jsr305" % "3.0.1",
  "com.google.code.gson" % "gson" % "2.2.2",
  "com.google.errorprone" % "error_prone_annotations" % "2.0.18",
  "com.google.guava" % "guava" % "22.0",
  "com.google.j2objc" % "j2objc-annotations" % "1.1",
  "com.pusher" % "java-websocket" % "1.4.1",
  "com.pusher" % "pusher-java-client" % "1.4.0",
  "commons-codec" % "commons-codec" % "1.3",
  "commons-io" % "commons-io" % "2.5",
  "io.netty" % "netty-all" % "4.1.7.Final",
  "io.netty" % "netty-buffer" % "4.0.24.Final",
  "io.netty" % "netty-codec" % "4.0.24.Final",
  "io.netty" % "netty-codec-http" % "4.0.24.Final",
  "io.netty" % "netty-common" % "4.0.24.Final",
  "io.netty" % "netty-handler" % "4.0.24.Final",
  "io.netty" % "netty-transport" % "4.0.24.Final",
  "io.reactivex.rxjava2" % "rxjava" % "2.0.4",
  "io.reactivex" % "rxjava" % "1.2.5",
  "javax.ws.rs" % "jsr311-api" % "1.1.1",
  "junit" % "junit" % "4.12",
  "net.bytebuddy" % "byte-buddy" % "1.6.2",
  "net.bytebuddy" % "byte-buddy-agent" % "1.6.2",
  "net.iharder" % "base64" % "2.3.9",
  "oauth.signpost" % "signpost-core" % "1.2.1.2",
  "org.apache.logging.log4j" % "log4j-api" % "2.6.2",
  "org.apache.logging.log4j" % "log4j-core" % "2.6.2",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.6.2",
  "org.assertj" % "assertj-core" % "3.6.1",
  "org.codehaus.mojo" % "animal-sniffer-annotations" % "1.14",
  "org.hamcrest" % "hamcrest-core" % "1.3",
  "org.knowm.xchange" % "xchange-core" % "4.2.0",
  "org.knowm.xchange" % "xchange-poloniex" % "4.2.0",
  "org.knowm.xchange" % "xchange-btce" % "4.2.0",
  "info.bitrich.xchange-stream" % "xchange-stream-core" % "4.2.2",
  "info.bitrich.xchange-stream" % "service-wamp" % "4.2.2",
  "org.mockito" % "mockito-core" % "2.6.3",
  "org.msgpack" % "jackson-dataformat-msgpack" % "0.7.0-p7",
  "org.msgpack" % "msgpack-core" % "0.7.0-p7",
  "org.objenesis" % "objenesis" % "2.5",
  "org.reactivestreams" % "reactive-streams" % "1.0.0",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-api" % "1.7.21",
  "ws.wamp.jawampa" % "jawampa-core" % "0.4.2",
  "ws.wamp.jawampa" % "jawampa-netty" % "0.4.2",
  "com.typesafe.akka" %% "akka-actor" % "2.5.3",
  "com.typesafe.akka" %% "akka-stream" % "2.5.3",
  "com.typesafe.akka" %% "akka-http" % "10.0.8"
)


updateOptions := updateOptions.value.withLatestSnapshots(false)