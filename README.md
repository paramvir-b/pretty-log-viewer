Pretty Log Viewer
===================

Pretty log viewer expands JSON and XML to make easier to read.

Motivation
----------

On daily basis, I encountered logs which contain embedded compact JSON and XML content. So finally, I wrote a 
rudimentary java app which can parse logs and expands JSON and XML content.

Some JSON in log like

```text
19:53:40,384 INFO [main] some message {"array":[1,2,3],"boolean":true,"null":null,"number":123,"object":{"a":"b","c":"d","e":"f"},"string":"Hello World"} this is just
```

to pretty log

```text
19:53:40,384 INFO [main] some message 
{
  "array": [
    1.0,
    2.0,
    3.0
  ],
  "boolean": true,
  "number": 123.0,
  "object": {
    "a": "b",
    "c": "d",
    "e": "f"
  },
  "string": "Hello World"
}
 this is just
 ```


Features
--------

 * Support JSON and XML.
 * Supports reading from stdin or file.
 
Basic Usage
-----------

After running 'mvn clean package'

```text
$mvn clean package

$java -jar target/pretty-log-viewer-<version>-jar-with-dependencies.jar -h
usage: pretty-log-viewer [-h] [-f F]

Convert embedded json/xml objects into human readable form

optional arguments:
  -h, --help             show this help message and exit
  -f F                   Log file name
```

Reading from stdin
------------------

If you want to use parse logs as they come.

```text
cat src/test/resources/test_data/f1.log | java -jar target/pretty-log-viewer-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```

Reading from file
------------------

If you want to use an existing log file.

```text
java -jar target/pretty-log-viewer-1.0.0-SNAPSHOT-jar-with-dependencies.jar -f src/test/resources/test_data/f1.log
```

Sample
------

Input

```text
19:53:10,545 INFO [main] { "type" : "basic"}
19:53:40,374 INFO [main] {"type" : "basic"}
19:53:40,375 INFO [main] some message { "type" : "basic"}
19:53:40,376 INFO [main] some "message" : {"type" : "basic"}
19:53:40,377 INFO [main] some message
19:53:40,378 INFO [main] some message
19:53:40,379 INFO [main] some message {"type" : "basic"} this is just
19:53:40,380 INFO [main] {"array":[1,2,3],"boolean":true,"null":null,"number":123,"object":{"a":"b","c":"d","e":"f"},"string":"Hello World"}
19:53:40,381 INFO [main] some message {"array":[1,2,3],"boolean":true,"null":null,"number":123,"object":{"a":"b","c":"d","e":"f"},"string":"Hello World"}
19:53:40,382 INFO [main] some message
19:53:40,383 INFO [main] some message
19:53:40,384 INFO [main] some message {"array":[1,2,3],"boolean":true,"null":null,"number":123,"object":{"a":"b","c":"d","e":"f"},"string":"Hello World"} this is just
19:53:10,545 INFO [main] <hello>basic</hello>
19:53:10,546 INFO [main] <hello>"basic"</hello>
19:53:10,547 INFO [main] <xml name="hello">basic</xml>
19:53:10,548 INFO [main] <xml name="hello"><child>basic</child><child><childl2>basic</childl2></child></xml>
19:53:10,549 INFO [main] <xml name="hello"><child>basic</child><child>basic</child></xml> hello
```

Output

```text
/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/bin/java -ea -Didea.launcher.port=7547 "-Didea.launcher.bin.path=/Applications/IntelliJ IDEA 14.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath "/Applications/IntelliJ IDEA 14.app/Contents/lib/idea_rt.jar:/Applications/IntelliJ IDEA 14.app/Contents/plugins/junit/lib/junit-rt.jar:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/deploy.jar:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/dt.jar:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/javaws.jar:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/jce.jar:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/jconsole.jar:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/management-agent.jar:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/plugin.jar:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/sa-jdi.jar:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/charsets.jar:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/classes.jar:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/jsse.jar:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/ui.jar:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/ext/apple_provider.jar:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/ext/dnsns.jar:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/ext/localedata.jar:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/ext/sunjce_provider.jar:/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/ext/sunpkcs11.jar:/Users/havexz/work/git/pretty-log-viewer/target/test-classes:/Users/havexz/work/git/pretty-log-viewer/target/classes:/Users/havexz/.m2/repository/net/sourceforge/argparse4j/argparse4j/0.6.0/argparse4j-0.6.0.jar:/Users/havexz/.m2/repository/com/google/code/gson/gson/2.3.1/gson-2.3.1.jar:/Users/havexz/.m2/repository/junit/junit/4.12/junit-4.12.jar:/Users/havexz/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/Users/havexz/.m2/repository/org/slf4j/slf4j-api/1.7.12/slf4j-api-1.7.12.jar:/Users/havexz/.m2/repository/ch/qos/logback/logback-classic/1.1.3/logback-classic-1.1.3.jar:/Users/havexz/.m2/repository/ch/qos/logback/logback-core/1.1.3/logback-core-1.1.3.jar" com.intellij.rt.execution.application.AppMain com.intellij.rt.execution.junit.JUnitStarter -ideVersion5 com.rokoder.app.prettylogviewer.PrettyLogViewerMainTest,testBasic
src/test/resources/test_data/f1.log
19:53:10,545 INFO [main] 
{
  "type": "basic"
}
19:53:40,374 INFO [main] 
{
  "type": "basic"
}
19:53:40,375 INFO [main] some message 
{
  "type": "basic"
}
19:53:40,376 INFO [main] some "message" : 
{
  "type": "basic"
}
19:53:40,377 INFO [main] some message
19:53:40,378 INFO [main] some message
19:53:40,379 INFO [main] some message 
{
  "type": "basic"
}
 this is just
19:53:40,380 INFO [main] 
{
  "array": [
    1.0,
    2.0,
    3.0
  ],
  "boolean": true,
  "number": 123.0,
  "object": {
    "a": "b",
    "c": "d",
    "e": "f"
  },
  "string": "Hello World"
}
19:53:40,381 INFO [main] some message 
{
  "array": [
    1.0,
    2.0,
    3.0
  ],
  "boolean": true,
  "number": 123.0,
  "object": {
    "a": "b",
    "c": "d",
    "e": "f"
  },
  "string": "Hello World"
}
19:53:40,382 INFO [main] some message
19:53:40,383 INFO [main] some message
19:53:40,384 INFO [main] some message 
{
  "array": [
    1.0,
    2.0,
    3.0
  ],
  "boolean": true,
  "number": 123.0,
  "object": {
    "a": "b",
    "c": "d",
    "e": "f"
  },
  "string": "Hello World"
}
 this is just
19:53:10,545 INFO [main] 
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<hello>basic</hello>
19:53:10,546 INFO [main] 
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<hello>"basic"</hello>
19:53:10,547 INFO [main] 
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xml name="hello">basic</xml>
19:53:10,548 INFO [main] 
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xml name="hello">
  <child>basic</child>
  <child>
    <childl2>basic</childl2>
  </child>
</xml>
19:53:10,549 INFO [main] 
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xml name="hello">
  <child>basic</child>
  <child>basic</child>
</xml>
 hello
```
