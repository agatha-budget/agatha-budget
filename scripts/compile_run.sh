#!/bin/bash

gradle uberjar
java -jar build/libs/tresorier-backend-uber.jar
