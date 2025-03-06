//Ida Andersson idan1222

import java.util.*;

public class DogSorter {

    // yttre loop
    public static int sortDogs(Comparator<Dog> compType, ArrayList<Dog> dogList) {
        int currentIndex = 0;
        int nrOfSwaps = 0;
        while (currentIndex < dogList.size()) {
            int minElementIndex = nextDog(compType, dogList, currentIndex);
            if (minElementIndex != currentIndex) {
                swapDogs(dogList, minElementIndex, currentIndex);
                nrOfSwaps++;
            }
            currentIndex++;
        }
        return nrOfSwaps;
    }

    // hitta nästa hund
    private static int nextDog(Comparator<Dog> comp, ArrayList<Dog> dogList, int searchPoint) {
        int index = searchPoint;
        while (searchPoint < dogList.size() - 1) {
            int result = comp.compare(dogList.get(index), dogList.get(searchPoint + 1));
            if (result >= 0) {
                index = searchPoint + 1;
            }
            searchPoint++;
        }
        return index;
    }

    // byt plats på två hundar
    private static void swapDogs(ArrayList<Dog> dogList, int indexOne, int indexTwo) {
        Dog placeholder = dogList.get(indexOne);
        dogList.set(indexOne, dogList.get(indexTwo));
        dogList.set(indexTwo, placeholder);
    }

}
