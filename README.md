README
======

FIXSimple is a simple Java FIX 4.2 client and server with a goal of
demonstrating the Logon, NewOrderSingle, and Logout message exchange
between a FIX client and server.  FIXSimple was written over a few
hours on a weekend as an exercise.


Background
----------

FIX 4.2 is a client/server protocol defined by the FIX Working Group
to facilitate securities trading.  FIX messages are key-value pairs of
ISO-8895-1-encoded text (mostly) delimited by the SOH (ASCII 0x01)
character.


Building
--------

Run "ant build.xml".  For full instructions, see HACKING file.


Running
-------

ALL: ensure running the command "java" works.

WINDOWS: Run runServer.bat, then in another window run runClient.bat

POSIX:  Sourcing runServer.bat and runClient.bat should work fine; the gneeral command lines are:

Server including debugger:

CLASSPATH=./dist/lib/FIXSimple-20120319.jar java  -agentlib:jdwp=transport=dt_socket,address=18000,server=y,suspend=n com.martindengler.proj.FIXSimple.Acceptor

Client:

CLASSPATH=./dist/lib/FIXSimple-20120319.jar java com.martindengler.proj.FIXSimple.Initiator




Limitations
-----------

FIXSimple does not demonstrate message recovery (where gaps in the
sequence numbers are detected and resends of older messages requested
by the gap-observing party), though it does keep unacknowledged
messages in memory to facilitate this.

FIXSimple does not meet all the design considerations that are be
required of a production-ready FIX client / server library.  Some
design limitations (and reason for) besides the ones mentioned above
are:

 - Some effort has been made to make server and client classes
   MT-safe, but a rigourous audit has not been completed, nor has
   extensive stress-testing taken place

 - Not mockable (for simplicity)

 - FIX Protocol version is hardcoded (simplicity)

 - Only supports message tags required for
   Logon/NewOrderSingle/Logout

 - Minimal Javadoc comments (example comments in src/com/martindengler/proj/FIXSimple/FIXMessage.java)

 - Minimal build system support (ant, intellij projects)

FIXSimple documents areas with room for improvement directly in the code via a "FUTURE" comment.


Design commentary
-----------------

See HACKING
