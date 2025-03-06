//Ida Andersson idan1222

import java.util.ArrayList;

public class Owner implements Comparable<Owner> {

    // attribut
    private String name;
    private ArrayList<Dog> ownedDogs = new ArrayList<>();

    // konstruktor
    public Owner(String name) {
        this.name = formatName(name);
    }

    // standardoperationer
    public String getName() {
        return name;
    }

    // sortering
    @Override
    public int compareTo(Owner other) {
        return this.getName().compareTo(other.getName());
    }

    // formattering
    private String formatName(String name) {
        // kontrollera för blankt namn
        if (name.equals(null) || name.isBlank()) {
            name = "No Name";
        }
        // gör allt till gemener...
        name = name.toLowerCase();
        // dela upp namnen i en lista baserat på mellanslag..
        String[] arrOfName = name.split(" ");
        // ...och gör den första bokstaven i varje list-item till versal
        for (int i = 0; i < arrOfName.length; i++) {
            arrOfName[i] = arrOfName[i].substring(0, 1).toUpperCase() + arrOfName[i].substring(1);
        }
        // sätt ihop namnet i en String igen
        name = String.join(" ", arrOfName);
        return name;
    }

    // ägda hundar
    // lägg till hund
    public boolean addDog(Dog dog) {
        // finns hunden?
        if (dog == null) {
            return false;
        }
        // kolla om hunden redan har ägare eller om this är hundens ägare
        if (dog.getOwner() == null || dog.getOwner() == this) {
            // äger this redan hunden?
            if (!ownedDogs.contains(dog)) {
                // spara hunden
                ownedDogs.add(dog);
            }
            // har hunden ägare?
            if (dog.getOwner() == null) {
                // be hunden sätta this som ägare
                dog.setOwner(this);
                return true;
            }
            // är this redan hundens ägare?
            if (dog.getOwner() == this) {
                return false;
            }
        }
        return false;
    }

    public boolean removeDog(Dog dog) {
        // om hund är null
        if (dog == null) {
            return false;
        }
        // är this ägaren?
        if (dog != null && dog.getOwner() == this) {
            // ta bort hunden från ägarens lista
            ownedDogs.remove(dog);
            // ta bort ägaren från hunden
            dog.setOwner(null);
            return true;
        }
        return false;
    }

    // visa ägarens hundar
    public ArrayList<Dog> getDogs() {
        ArrayList<Dog> copyOfDogs = new ArrayList<>();
        for (Dog element : ownedDogs) {
            copyOfDogs.add(element);
        }
        return copyOfDogs;
    }

    // utskriftsformat
    @Override
    public String toString() {
        String dogString = "";
        for (Dog dog : ownedDogs) {
            dogString += dog.getName() + ", ";
        }
        return "%s %s".formatted(name, dogString);
    }

}