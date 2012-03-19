#!/bin/sh
children=$1

CMD="java com.martindengler.proj.FIXSimple.Initiator"

retval=0
for i in $(seq 1 $children) ; do
    ( $CMD >> stress.log 2>&1 || echo success ) &
done

wait
