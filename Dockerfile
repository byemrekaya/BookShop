# For Java 11, try this
FROM adoptopenjdk/maven-openjdk11

#COPY /app/src/main/resources/data/books.csv
#COPY /app/src/main/resources/data/user.csv
#COPY /app/src/main/resources/data/borrowed.csv

COPY src/ /app/src
COPY data/ /app/data
COPY pom.xml /app

# cd /opt/app
WORKDIR /app

CMD mvn clean install -DskipTests && java -jar target/book-shop.jar