#!/bin/sh

git init
git add .
git commit -am "1"
git remote add origin https://github.com/906447521/smarttesting
git pull
git push -u -f origin master