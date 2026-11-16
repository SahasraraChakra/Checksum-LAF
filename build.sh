#!/bin/sh
# Build checksum-laf.jar (needs JDK 17+; developed against 21, works on 26)
rm -rf out && mkdir out
javac -d out src/chk/*.java && cp -r res/chk out/ && jar --create --file checksum-laf.jar --main-class chk.ChkDemo -C out .
echo "built checksum-laf.jar  -  run the gallery:  java -jar checksum-laf.jar"
