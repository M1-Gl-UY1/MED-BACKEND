# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copier les fichiers Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Donner les permissions d'exécution
RUN chmod +x mvnw

# Télécharger les dépendances (cache layer)
RUN ./mvnw dependency:go-offline -B

# Copier le code source
COPY src ./src

# Builder l'application
RUN ./mvnw package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Créer un utilisateur non-root
RUN addgroup -S spring && adduser -S spring -G spring

# Créer le dossier uploads
RUN mkdir -p /app/uploads/images && chown -R spring:spring /app

# Copier le JAR depuis le stage builder
COPY --from=builder /app/target/*.jar app.jar

# Changer vers l'utilisateur non-root
USER spring:spring

# Exposer le port
EXPOSE 8085

# Variables d'environnement par défaut
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Démarrer l'application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
