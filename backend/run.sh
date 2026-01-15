#!/bin/bash

# Load environment variables from .env file if it exists
if [ -f ../.env ]; then
    export $(cat ../.env | grep -v '^#' | xargs)
fi

# Check if required environment variables are set
if [ -z "$DATABASE_URL" ] || [ -z "$DATABASE_USERNAME" ] || [ -z "$DATABASE_PASSWORD" ]; then
    echo "ERROR: Database environment variables are not set!"
    echo "Please set DATABASE_URL, DATABASE_USERNAME, and DATABASE_PASSWORD"
    echo "Or create a .env file in the project root with these variables."
    exit 1
fi

# Run the Spring Boot application
mvn spring-boot:run
