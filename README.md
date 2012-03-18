README.md
---------

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

Run "ant".  For full instructions, see HACKING file.


Running
-------

Run "ant client" or "ant server" as desired.
TODO: implement
TODO: implement Eclipse launchers
TODO: implement IntelliJ launchers
TODO: test Eclipse launchers on Windows
TODO: test IntelliJ launchers on Windows


Limitations
-----------

FIXSimple does not demonstrate message recovery (where gaps in the
sequence numbers are detected and resends of older messages requested
by the gap-observing party), though it does keep unacknowledged
messages in memory to facilitate this.

FIXSimple does not meet all the design considerations that may be
required of a production-ready FIX client / server library.  Some
design limitations (and reason for) besides the ones mentioned above
are:

 - Not mockable (for simplicity)

 - FIX Protocol version is hardcoded (simplicity)

 - Some effort has been made to make server and client classes
   MT-safe, but a rigourous audit has not been completed, nor has
   extensive stress-testing taken place

 - Only supports message tags required for
   Logon/NewOrderSingle/Heartbeat/Logout

 - TODO: review code for design limitations and document

 - Minimal Javadoc comments (for simplicity)

 - Minimal build system support (ant, eclipse, intellij projects)

FIXSimple documents any obvious areas with room for improvement in the
code.


Design commentary
-----------------

Overview

Key classes



TODO: add all parts of all messages sent / received that are NOT supported
TODO: document type-safety of message getters & setters
TOOD: document enum creation process
TODO: document any obvious inefficiences
TODO: document profiling
TODO: consider problems in http://www.odi.ch/prog/design/newbies.php#10
TODO: consider problems in http://zguide.zeromq.org/page:all#Why-We-Needed-MQ
TODO: ensure getters are consistently named; getFoo() or foo()

TODO: putM -> put
TODO: ensure use of int vs Integer is sensible and consistent
TODO: ensure System.err.println/format calls are removed where appropriate
TODO: sanity check design vs. quickfix/j
TODO: check 80 char line lengths
TODO: write tests
TODO: performance testing
TODO: add license comments to code
TODO: add style guide discussion http://www.oracle.com/technetwork/java/javase/documentation/index-137868.html#styleguide