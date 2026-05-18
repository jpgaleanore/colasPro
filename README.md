# Teoría de Colas (ColasPro)

Aplicación Android para el análisis de sistemas de colas mediante los modelos **M/M/1** y **M/M/S**. Permite ingresar parámetros de campo (tasa de llegada λ, tasa de servicio μ y número de servidores S), calcular las métricas de desempeño del sistema y generar un reporte PDF con los resultados y las ecuaciones empleadas.

A partir de la **v2.0** la app incluye una vista de **Teoría** con la explicación de los modelos, parámetros y casos de uso, además de una vista **Acerca de** con el enlace al repositorio y los desarrolladores.

## Novedades de la v2.0

- 🆕 **Vista de Teoría:** explica qué es la Teoría de Colas, los modelos M/M/1 y M/M/S, los supuestos, cómo medir cada parámetro (λ, μ, S), el significado de cada métrica de salida y los casos de uso (bancos, supermercados, hospitales, call centers, peajes, líneas de producción, redes).
- 🆕 **Vista Acerca de:** información de la aplicación, enlace al repositorio en GitHub y créditos de los desarrolladores.
- 🆕 **Menú de navegación** en la barra superior para alternar entre Calculadora, Teoría y Acerca de.

## Características

- Cálculo de modelos **M/M/1** y **M/M/S**.
- Métricas obtenidas: ρ (utilización), P₀, L, Lq, W, Wq y Pw.
- Captura de metadatos del estudio: proyecto, analista, ubicación y observaciones.
- Generación de reporte **PDF** con los resultados y las ecuaciones aplicadas.
- Acciones rápidas para **abrir** o **compartir** el PDF generado (se guarda en la carpeta Descargas del dispositivo).
- Vista de **Teoría** integrada con explicación detallada del modelo y las fórmulas.
- Vista **Acerca de** con enlace directo al repositorio y créditos.
- Interfaz construida con **Jetpack Compose** y **Material 3**.

## Stack técnico

- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **minSdk:** 24 (Android 7.0)
- **targetSdk / compileSdk:** 36
- **Build system:** Gradle (Kotlin DSL)

## Estructura del proyecto

```
app/src/main/java/com/yeipi/teoriacolas/
├── MainActivity.kt              # UI principal y navegación entre vistas
├── domain/
│   └── QueueingCalculator.kt    # Lógica de cálculo M/M/1 y M/M/S
├── report/
│   ├── PdfReportGenerator.kt    # Generación del reporte PDF
│   └── PdfActions.kt            # Abrir / compartir el PDF
└── ui/
    ├── TheoryScreen.kt          # Vista de Teoría (modelos, parámetros, casos de uso)
    ├── AboutScreen.kt           # Vista Acerca de (repo y desarrolladores)
    └── theme/                   # Tema Material 3
```

## Compilación desde el código fuente

Requisitos: **Android Studio** (Koala o superior) y **JDK 11+**.

```bash
# Clonar el repositorio
git clone https://github.com/jpgaleanore/colasPro
cd colasPro

# Generar el APK de debug
./gradlew assembleDebug

# Generar el APK de release (sin firma)
./gradlew assembleRelease
```

El APK generado quedará en:

```
app/build/outputs/apk/debug/app-debug.apk
app/build/outputs/apk/release/app-release-unsigned.apk
```

---

## 📦 APK e instalación en Android

Los APK listos para instalar se encuentran en la carpeta [`releases/`](./releases/) de este repositorio:

| Versión | Archivo | Tamaño aprox. | Cambios |
|---------|---------|---------------|---------|
| 2.0 (debug) | [`releases/TeoriaColas-v2.0.apk`](./releases/TeoriaColas-v2.0.apk) | ~11 MB | Vistas de Teoría y Acerca de, navegación por menú |
| 1.0 (debug) | [`releases/TeoriaColas-v1.0.apk`](./releases/TeoriaColas-v1.0.apk) | ~11 MB | Versión inicial: calculadora M/M/1 y M/M/S + PDF |

> Se recomienda instalar siempre la versión más reciente.

### Instalación paso a paso

1. **Descarga el APK** desde el enlace de la tabla anterior y cópialo a tu dispositivo Android (puedes descargarlo directamente desde el navegador del teléfono, transferirlo por cable USB, Bluetooth o cualquier servicio en la nube).

2. **Habilita la instalación desde orígenes desconocidos:**
   - Abre **Ajustes** → **Aplicaciones** (o **Seguridad**, según la versión de Android).
   - Busca la opción **Instalar aplicaciones desconocidas** o **Orígenes desconocidos**.
   - Selecciona la aplicación desde la que vas a abrir el APK (por ejemplo, **Archivos** o **Chrome**) y activa el permiso.

3. **Instala la aplicación:**
   - Abre la app **Archivos** (o el gestor de archivos de tu dispositivo) y localiza el APK descargado.
   - Toca el archivo `TeoriaColas-v2.0.apk`.
   - Pulsa **Instalar** y espera a que finalice el proceso.
   - Cuando termine, pulsa **Abrir** para ejecutar la aplicación.

4. **Primer uso:**
   - Concede los permisos solicitados (almacenamiento, si aplica) para poder guardar los reportes PDF en la carpeta Descargas.

### Requisitos mínimos del dispositivo

- Android **7.0 (API 24)** o superior.
- ~20 MB de espacio libre.
- Permiso de almacenamiento para guardar los PDF generados.

### Desinstalación

Para desinstalar, mantén pulsado el ícono de **ColasPro** en el lanzador de aplicaciones y selecciona **Desinstalar**, o ve a **Ajustes** → **Aplicaciones** → **ColasPro** → **Desinstalar**.

---

## Uso de la aplicación

### Calculadora

1. Completa los campos de **Proyecto**, **Analista** y **Ubicación**.
2. Selecciona el modelo: **M/M/1** o **M/M/S**.
3. Ingresa **λ** (tasa de llegada) y **μ** (tasa de servicio) en clientes/hora. Si elegiste M/M/S, ingresa también el número de servidores **S**.
4. Pulsa **Calcular** para ver los resultados (ρ, P₀, L, Lq, W, Wq y Pw cuando aplique).
5. Pulsa **Generar PDF** para crear el reporte. El archivo se guarda en la carpeta **Descargas** del dispositivo y puede abrirse o compartirse directamente desde la app.

### Navegación entre vistas

Desde el botón **Menú** de la barra superior puedes alternar entre:

- **Calculadora:** pantalla principal para ingresar datos y calcular las métricas.
- **Teoría:** explicación detallada de la Teoría de Colas, los modelos implementados, los parámetros que debes medir y los casos de uso típicos.
- **Acerca de:** información de la app, enlace al repositorio en GitHub y créditos de los desarrolladores.

## Desarrolladores

- **David Gómez**
- **Juan Pablo Galeano**
- **David Sánchez**

Repositorio: [github.com/jpgaleanore/colasPro](https://github.com/jpgaleanore/colasPro)

## Licencia

Uso académico / educativo.
