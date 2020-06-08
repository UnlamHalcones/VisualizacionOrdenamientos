package ar.edu.unlam.ordenamientos;

import java.util.LinkedList;
import java.util.List;

import ar.edu.unlam.graphics.Ordenador;

public class QuickSort<T extends Comparable<T>> extends EstrategiaOrdenamiento<T> {

	public QuickSort(Ordenador jFrameReference) {
		super(jFrameReference);
	}

	@Override
	public void ordenar(List<T> arreglo) {
		quickSort(arreglo, 0, arreglo.size()-1);
	}
	
	
    private void quickSort(List<T> arreglo, int inicio, int fin){
        if(inicio < fin){
            int q = particion(arreglo, inicio, fin);
            quickSort(arreglo, inicio, q);
            quickSort(arreglo, q+1, fin);
        }
    }

    private int particion(List<T> arreglo, int inicio, int fin){
        T ultimo = arreglo.get(fin-1);
        int i = inicio-1;
        
		jFrameOrdenador.display(inicio, fin); 
		jFrameOrdenador.sleep();
		
        for(int j = inicio; j < fin-1; j++){
            if(arreglo.get(j).compareTo(ultimo) <= -1 || arreglo.get(j).compareTo(ultimo) == 0){
                i = i+1;
                intercambiar(arreglo, j, i);
            }
        }
        intercambiar(arreglo, fin-1, i+1);
        return i + 1;
    }


	

}
