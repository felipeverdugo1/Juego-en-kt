# Usa una imagen oficial con Java 17 (requerido para Kotlin 2.1 y AGP 8.8)
FROM eclipse-temurin:17-jdk-jammy

# Configurar variables de entorno para Android SDK
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=${PATH}:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools

# Instalar dependencias básicas del sistema
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    git \
    && rm -rf /var/lib/apt/lists/*

# Descargar e instalar las Command Line Tools del SDK de Android
RUN mkdir -p ${ANDROID_HOME}/cmdline-tools && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O /tmp/cmdline-tools.zip && \
    unzip -q /tmp/cmdline-tools.zip -d ${ANDROID_HOME}/cmdline-tools && \
    mv ${ANDROID_HOME}/cmdline-tools/cmdline-tools ${ANDROID_HOME}/cmdline-tools/latest && \
    rm /tmp/cmdline-tools.zip

# Aceptar licencias de Android e instalar componentes clave (API 35/34)
RUN yes | sdkmanager --licenses && \
    sdkmanager "platform-tools" "platforms;android-35" "build-tools;35.0.0"

WORKDIR /app

# Copiar archivos de configuración de Gradle para aprovechar el caché
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle/libs.versions.toml ./gradle/

# Dar permisos de ejecución a gradlew
RUN chmod +x gradlew

# Pre-descargar dependencias de Gradle
RUN ./gradlew dependencies --no-daemon || true

# Copiar el código fuente completo
COPY . .

# Comando por defecto: Compilar el APK en modo Debug
CMD ["sh", "gradlew", "assembleDebug", "--no-daemon"]