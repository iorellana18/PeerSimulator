# Laoratorio 1 Sistemas distribuídos
Ian Orellana 

# Descripción
El programa está en base al template entregado en clases
Se modifica en su mayoría la clase ExampleNode, creando los métodos para seleccionar nodos destino e imprimir las trazas según la acción que se realiza y la clase Layer para seleccionar las acciones que ocurren en el nodo y las respuestas a los mensajes.
En el archivo de configuración sólo es relevante la cantidad de nodos en la red y los ciclos realizados. Se recomienda un número elevado de ciclos para presencair la mayoría de las acciones. Se considera 0 la probabilidad de caía del mensaje.

# Ejemplos relevantes :

# Al registrar nuevo tópico:

Registrar nuevo tópico
Se crea nuevo nodo
Nodo: 8 añade tópico : 10
Tópicos a los que publica: [10]

--- Luego cuando recibe el mensaje

Mensaje recibido
Nodo 10
Me inscribo como tópico para el  nodo publicador : 8
Publicadores de tópico 10 : [8]

# Al registrarse como publicador

Registrar nuevo tópico
Se crea nuevo nodo
Nodo: 8 añade tópico : 10
Tópicos a los que publica: [10]

---- Luego cuando se recibe el mensaje

Mensaje recibido
Nodo 10
Me inscribo como tópico para el  nodo publicador : 8
Publicadores de tópico 10 : [8]


# Al publicar mensaje

Publicar en tópico
Nodo: 4 publica a tópico : 10

--- Cuando mensaje lega a tópico

Mensaje recibido
Nodo 10
Reenvíar publicación a suscriptores
Tópico 10 recibe publicación de nodo 4
Preparándose para reenviar publicación a nodo(s) [0, 8, 7, 11]
Enviando a : 0
Enviando a : 8
Enviando a : 7
Enviando a : 11

--- Cuando suscriptor recibe mensaje

Mensaje recibido
Nodo 0
Publicación recibida
Nodo 0 añade mensaje : Mensaje desde tópico 10
Lista de publicaciones guardadas : [Mensaje desde tópico 10]

# Request update
Request update
Nodo 63
Procesando publicacion : Mensaje desde tópico 11
Procesando publicacion : Mensaje desde tópico 14
Procesando publicacion : Mensaje desde tópico 11
Procesando publicacion : Mensaje desde tópico 15
Procesando publicacion : Mensaje desde tópico 20
Procesando publicacion : Mensaje desde tópico 20


## Además se añaden funciones para desincribirse como publicador o suscriptor, borrar publicación. Se controlan errores cuando no existen topicos, suscriptores o publicadores para el nodo informando por pantalla

