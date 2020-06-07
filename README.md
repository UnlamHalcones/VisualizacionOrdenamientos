
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
