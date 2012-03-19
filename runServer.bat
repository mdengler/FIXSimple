java -classpath ./dist/FIXSimple-20120319.jar -agentlib:jdwp=transport=dt_socket,address=18000,server=y,suspend=n -classpath ./build com.martindengler.proj.FIXSimple.Acceptor
