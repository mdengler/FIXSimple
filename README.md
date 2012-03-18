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


Building & Installation
--------

Run "ant".  For full instructions, see HACKING file.


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

Not mockable (for simplicity)
FIX Protocol version is hardcoded (simplicity)
Client library is not MT-safe (simplicity)
Only supports message tags required for Logon/NewOrderSingle/Heartbeat/Logout
TODO: review code for design limitations and document
Minimal Javadoc comments (for simplicity)
Minimal build system support (ant, eclipse, intellij projects)

FIXSimple documents any obvious areas with room for improvement in the code.


TODO: add all parts of all messages sent / received that are NOT supported
TOOD: document enum creation process
TODO: document any obvious inefficiences
TODO: document profiling