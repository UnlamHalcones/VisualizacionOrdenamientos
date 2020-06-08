package ar.edu.unlam.generador.datos;

import ar.edu.unlam.entidades.CasoOrdenamiento;
import ar.edu.unlam.entidades.Elemento;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GeneradorDeDatos {

    public static List<Elemento> generarDatos(CasoOrdenamiento casoOrdenamiento, int cantidadElementos) {
        List<Elemento> elementosAOrdenar = new LinkedList<>();

        switch (casoOrdenamiento) {

            case ALEATORIO:
            default:
                //En vez de generarlo con un random, meto los elementos en la lista y hago un shuffle. Es una forma facil de hacer que no tengamos repetidos
                for (int i = 1; i < cantidadElementos + 1; ++i) {
                    elementosAOrdenar.add(new Elemento(i));
                }

                Collections.shuffle(elementosAOrdenar);
                break;
            case CASI_INVERTIDO:

                for (int i = cantidadElementos + 1; i > 0; --i) {

                    //Con estos nos aseguramos que est� un poco desordenado. Se supone que va a estar 33% deordenado
                    if (ThreadLocalRandom.current().nextInt(0, 3) == 1) {
                        elementosAOrdenar.add(new Elemento((i-1)));
                        elementosAOrdenar.add(new Elemento(i));
                        i--;
                    }
                    else {
                        elementosAOrdenar.add(new Elemento(i));
                    }
                }

                break;
            case CASI_ORDENADO:
                for (int i = 1; i < cantidadElementos + 1 ; ++i) {

                    //Con estos nos aseguramos que est� un poco desordenado. Se supone que va a estar 33% deordenado
                    if (ThreadLocalRandom.current().nextInt(0, 3) == 1) {
                        elementosAOrdenar.add(new Elemento((i+1)));
                        elementosAOrdenar.add(new Elemento(i));
                        i++;
                    }
                    else {
                        elementosAOrdenar.add(new Elemento(i));
                    }
                }

                break;
            case INVERTIDO:
                for (int i = cantidadElementos + 1; i > 0; --i) {
                    elementosAOrdenar.add(new Elemento(i));
                }
                break;
            case ORDENADO:
                for (int i = 1; i < cantidadElementos + 1; ++i) {
                    elementosAOrdenar.add(new Elemento(i));
                }
                break;

        }
        return elementosAOrdenar;
    }
}
