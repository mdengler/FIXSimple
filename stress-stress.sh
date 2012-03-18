#!/bin/sh

for c in 1 2 4 8 16 32 64 128 ; do
    for i in 1 2 3 4 ; do
        echo -n "$c $i " >> stress-stress.log ; CLASSPATH=./dist/lib/FIXSimple-20120319e-20120319.jar /usr/bin/time ./stress.sh >> stress-stress.log 2>&1
    done
done
