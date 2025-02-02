# Usa una imagen base de Java 21
FROM openjdk:21-jdk-slim

# Copia el archivo .jar a la imagen
COPY target/turnerodef-0.0.1-SNAPSHOT.jar turnero.jar

# Expone el puerto 8080
EXPOSE 8080

# Comando para ejecutar el .jar
ENTRYPOINT ["java", "-jar", "turnero.jar"]
