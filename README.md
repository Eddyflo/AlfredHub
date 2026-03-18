# AlfredHub
Manejador de dispositivos Iot para el hogar, desarrollado con Abstrac Factory y Proxy
# Propuesta general:
AlfredHub es un sistema centralizado de control y automatización para hogares
inteligentes que actúa como un "mayordomo digital", integrando dispositivos IoT de
diferentes fabricantes bajo una única interfaz inteligente. El sistema no solo unifica
el control de luces, cámaras y termostatos, sino que incorpora un motor de
correlación predictiva que optimiza automáticamente la seguridad y eficiencia
energética basándose en patrones de comportamiento y eventos en tiempo real.
El sistema implementa patrones de diseño robustos como Abstract Factory para
manejar la heterogeneidad de dispositivos IoT permitiendo integrar dispositivos de
cualquier fabricante (Samsung, Xiaomi, Philips, TP-Link, etc.) bajo un modelo común.
Cada familia de dispositivos es encapsulada en una fábrica concreta que traduce los
comandos universales del sistema a los protocolos específicos del fabricante.
Se estima la implementación de proxy para gestionar eficientemente recursos
pesados como streams de video. AlfredHub se diferencia del mercado al
correlacionar eventos múltiples para crear automatizaciones inteligentes
contextuales, aprendiendo de los hábitos de los usuarios para anticipar necesidades
y optimizar recursos.
