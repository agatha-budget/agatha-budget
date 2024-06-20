#!/bin/bash

gradle uberjar
java -jar -Dlogback.configurationFile=logback.xml build/libs/tresorier-backend-uber.jar