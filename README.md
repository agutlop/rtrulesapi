# RTRulesAPI

API Rest para el proyecto fin de master del Experto en Big Data de la U-Tad Curso 2015-2016.

El proyecto consiste en la aplicación de reglas a los mesajes recibidos por un sistema de procesamiento streaming distribuido. 
Este programa proporciona una API para la inserción y recuperación de los distintos objetos de la base de datos.

RTRulesAPI cubre las siguientes funcionalidades:
- Alta y consulta de Metadatos,
- Alta y consulta de Topics
- Alta y consulta de Reglas


## Prerequisitos

Para el funcionamiento del api es necesario:
  - `sbt` 
  - Una instancia de MongoDB (`localhost:27017`) 
  - Consultar el repositorio rtrules-storage(https://github.com/agutlop/rtrules-storage) para consultar la informacion que debe contenter MongoDB


## Build & development

Descargar el repositorio 
    `git clone https://github.com/agutlop/rtrulesapi.git`

Lanzar en el puerto 9090
    `sbt "run 9090"`



Este programa se ha creado a partir del template de Play modern-web-template:
    https://github.com/lashford/modern-web-template

* **PlayFramework** - versión 2.3.9 para Scala
  *  [PlayFramework Docs](http://www.playframework.com/documentation/2.3.9/Home)

* **PlayReactiveMongo**
  * [Play-ReactiveMongo](https://github.com/ReactiveMongo/Play-ReactiveMongo)
