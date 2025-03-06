//Ida Andersson idan1222

import java.util.*;

public class DogCollection {

    // Hundlistan
    private ArrayList<Dog> dogList = new ArrayList<>();

    // metoder
    // lägg till hund
    public boolean addDog(Dog dog) {
        if (containsDog(dog)) {
            return false;
        }
        dogList.add(dog);
        return true;
    }

    // ta bort hund
    public boolean removeDog(Dog dog) {
        // kontrollera att hunden ej har ägare
        // kontrollera att hunden finns i samlingen
        if (containsDog(dog) && dog.getOwner() == null) {
            dogList.remove(dog);
            return true;
        }
        return false;
    }

    // ta bort hund med visst namn
    public boolean removeDog(String dogName) {
        int index = indexOfDog(dogName);
        // överlagring med metoden ovan
        if (index >= 0 && removeDog(dogList.get(index))){
            return true;
        }
        return false;
    }

    // kolla om hund finns
    // måste vara boolean
    public boolean containsDog(Dog dog) {
        return containsDog(dog.getName());
    }

    public boolean containsDog(String dogName) {
        if (indexOfDog(dogName) >= 0) {
            return true;
        }
        return false;
    }
    //hjälpmetod för att hitta hunds index
    private int indexOfDog(String dogName) {
        for (Dog element : dogList) {
            String name = element.getName();
            if (name.equalsIgnoreCase(dogName)) {
                return dogList.indexOf(element);
            }
        }
        return -1;
    }

    // hämta hund
    public Dog getDog(String dogName) {
        int index = indexOfDog(dogName);
        if (index >= 0) {
            return dogList.get(index);
        }
        return null;
    }

    // hämta alla hundar
    public ArrayList<Dog> getDogs() {
        // skapar kopia för att inte påverka ordningen i originallistan
        ArrayList<Dog> listCopy = new ArrayList<>();
        for (Dog element : dogList) {
            listCopy.add(element);
        }
        DogNameComparator compareNames = new DogNameComparator();
        DogSorter.sortDogs(compareNames, listCopy);
        // returnerar kopian
        return listCopy;
    }

    // hämta hundar med svans längre än X. Originalnamn: getDogsWithTails
    public ArrayList<Dog> getDogsWithMinTailLength(double tail) {
        ArrayList<Dog> longTailedDogs = new ArrayList<>();
        for (Dog element : dogList) {
            if (element.getTailLength() >= tail) {
                longTailedDogs.add(element);
            }
        }
        DogTailNameComparator compareTailsNames = new DogTailNameComparator();
        DogSorter.sortDogs(compareTailsNames, longTailedDogs);
        return longTailedDogs;
    }
}
