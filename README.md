# ALFREDHUB - Sistema de Hogar Inteligente

![Java](https://img.shields.io/badge/Java-11%2B-blue)
![Maven](https://img.shields.io/badge/Maven-3.6%2B-red)
![License](https://img.shields.io/badge/License-MIT-green)

## Descripción

**AlfredHub** es un sistema centralizado de control y automatización para hogares inteligentes, inspirado en Alfred Pennyworth, el fiel mayordomo de Batman. El sistema actúa como un "mayordomo digital" que integra dispositivos IoT de diferentes fabricantes (Samsung, Xiaomi) bajo una única interfaz inteligente.

### Características principales

- **Abstract Factory**: Creación de dispositivos de diferentes fabricantes sin acoplamiento
- **Proxy Pattern**: Gestión eficiente de cámaras con lazy loading y cache de miniaturas
- **Cámara real**: Integración con cámara de laptop usando JavaCV
- **Interfaz moderna**: Tema Batman con colores oscuros y elegantes
- **Registro de actividad**: Logging detallado de todas las acciones del usuario

---

## Casos de uso implementados

| ID | Caso de Uso | Descripción |
|----|-------------|-------------|
| **CU-01** | Registrar dispositivo IoT | Agregar luces o cámaras con selección de marca y parámetros |
| **CU-02** | Controlar luz | Encender, apagar o ajustar brillo de luces inteligentes |
| **CU-03** | Ver dashboard unificado | Visualizar todos los dispositivos con su estado actual |
| **CU-04** | Ver cámara en vivo | Activar la cámara real y mostrar video en tiempo real |
| **CU-05** | Actualizar miniatura | Guardar la última imagen capturada como miniatura (cache) |

---

##  Requisitos previos

- Java JDK 11 o superior
- Maven 3.6 o superior
- Cámara de laptop (para funcionalidad de video)

---

## Instalación y ejecución

### Clonar el repositorio

bash
git clone https://github.com/Eddyflo/AlfredHub.git
cd AlfredHub

## Compilar
bash
mvn clean compile
## Ejecutar
bash
mvn exec:java -Dexec.mainClass="com.alfredhub.Main"
# Estructura del proyecto
text
AlfredHub/
├── src/main/java/com/alfredhub/
│   ├── Main.java                 # Punto de entrada
│   ├── ui/                       # Interfaz gráfica
│   ├── factory/                  # Abstract Factory
│   ├── devices/                  # Dispositivos IoT
│   └── proxy/                    # Patrón Proxy
├── pom.xml                       # Dependencias Maven
└── README.md                     # Este archivo
