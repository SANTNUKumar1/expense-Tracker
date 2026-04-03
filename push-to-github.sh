#!/bin/bash
cd /c/project_1
git init
git add .
git commit -m "Initial commit: Spring Boot project with JWT authentication"
git branch -M main
git remote add origin https://github.com/SANTNUKumar1/expense-Tracker.git
git push -u origin main
