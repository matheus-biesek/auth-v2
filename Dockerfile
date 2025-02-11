# Etapa de compilação usando Maven
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

# Copia apenas o arquivo pom.xml para baixar as dependências
COPY pom.xml .

# Baixa as dependências de compilação
RUN mvn dependency:go-offline

# Copia o código fonte
COPY src ./src

# Compila o projeto e gera o JAR
RUN mvn package -DskipTests

# Etapa de criação da imagem final
FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copia o JAR construído na etapa anterior para a imagem final
COPY --from=build /app/target/auth-0.0.1-SNAPSHOT.jar ./auth-v2.jar

# Expor a porta que a aplicação usa
EXPOSE 8080

# Comando para iniciar a aplicação e ativação do .properties de produção
CMD ["java", "-jar", "auth-v2.jar", "--spring.profiles.active=prod"]