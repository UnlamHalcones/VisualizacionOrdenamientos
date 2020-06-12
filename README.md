# Halcones Ordenador Grafico

- El programa hace uso de cuatro algoritmos de ordenamiento, que son representados en forma visual, 
por medio de barras verticales proporcionales al valor entero que representan.
Esto contenido en una ventana, que de acuerdo a determinados colores orientan durante su ejecucion, 
el proceso de ordenamiento.


- Los algoritmos de ordenamiento disponibles son los siguientes:

	- BURBUJEO
	- INSERCION
	- SELECCION
	- QUICKSORT
	
- Los colores que nos reportaran en tiempo de ejecucion del algoritmo de ordenamiento, son los siguientes:

	- BLANCO: elemento de la lista no ordenado.
	- AMARILLO: elemento comparador seleccionado
	- ROJO: elemento actualmente comparado.
	- VERDE: elemento ya ordenado.

- En seccion superior de la ventana disponemos de tres contadores refrescados durante la ejecucion del programa:

	- Time: tiempo transcurrido durante el proceso de ordenamiento
	- Comparaciones: cantidad de comparaciones realizadas.
	- Intercambios: cantidad de intercambios realizados.

**Instrucciones de Uso:**
	Al realizar la ejecucion del programa, este nos permitira setear parametros los cuales configuraran el 
	escenario de ordenamiento. Los parametros a setear son los siguientes:
	

**1.** La 1ra opcion (desplegable) nor permite elejir que algoritmo de ordenamiento utilizara el programa.

**2.** La 2da opcion permite especificar que orden previo trae la lista a ordenar, las cuales son:
	- ORDENADO
	- CASI ORDENADO
	- ALEATORIO
	- CASI INVERTIDO
	- INVERTIDO
	
**3.** La 3ra opcion nos habilita para espcificar el tamaño de la lista (cantidad de elementos).

**4.** 4ta opcion nos permite setear el tiempo que demora entre cada operacion (mili segundos).

**5.** Luego de setear, se puede presionar el boton Start para iniciar el ordenamiento.

Desarrollado por Halcones.




---------
# Consigna

En la clase del sábado se vio como utilizar Graphics para poder representar datos de forma visual Hace unas clases también se aprendió el uso de algoritmos de ordenamiento (básicos y avanzados)
Lo que se pide en este TP es combinar ambos conocimientos para realizar una representación visual de los algoritmos de ordenamiento. Un ejemplo del resultado deseado puede ser este, pero sin sonidos. Deberá utilizarse la implementación de los algoritmos suministrada por la cátedra.

Se pide entregar un proyecto con (.zip):

 - README con instrucciones de uso, y autores del proyecto 
 - Al menos dos  implementaciones de algoritmos de ordenamiento básicos y uno de ordenamiento avanzado 
 -  Poder configurar previamente (no necesita tener interfaz, sino por constructor o métodos auxiliares):
	 - La cantidad de elementos  	  
	 - El tiempo de demora entre cada operación (ms)  	  
	 - El caso (ya ordenado, casi ordenado, aleatorio, casi invertido, invertido) 
	 - El algoritmo a utilizar
 - Debe mostrar contadores con refresco inmediato, con información extra:
	 - Cantidad de comparaciones 
	 - Cantidad de intercambios 
	 - Tiempo transcurrido

- Utilizar códigos de color para identificar: 
	- El elemento actual
	- El elemento con el que se lo está comparando
	- Los elementos ya ordenados
	
- Una vez ejecutado el algoritmo, debe guardarse en un archivo un registro estadístico de la ejecución, en una nueva línea en un archivo .csv. Por ejemplo:

#algoritmo,condicion,elementos,tiempo en segundos
burbujeo,casi ordenado,1000,10.3

**Nota:** Si es entregado de forma correcta antes del sábado 06/06/2020 a las 10am, la entrega sumara puntos extras.

**Nota 2:** Los mejores proyectos se integrarán al repositorio permanente de la materia. Puede optarse por ingresarlo en forma anónima, o con autoría.


Pendientes:

 - [ ] Hacer que se guarden los csv
 - [ ] Colores en QuickSort
 - [ ] Colores en Inserción
 - [ ] Actualizar este Readme con instrucciones de uso
