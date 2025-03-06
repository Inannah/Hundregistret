
//Ida Andersson idan1222

import java.util.ArrayList;

public class DogRegister {
    private static final String EXIT_COMMAND = "EXIT";
    private static final String REGISTER_NEW_OWNER = "REGISTER NEW OWNER";
    private static final String REMOVE_OWNER = "REMOVE OWNER";
    private static final String REGISTER_NEW_DOG = "REGISTER NEW DOG";
    private static final String REMOVE_DOG = "REMOVE DOG";
    private static final String LIST_OWNERS = "LIST OWNERS";
    private static final String LIST_DOGS = "LIST DOGS";
    private static final String GIVE_DOG_OWNER = "GIVE DOG TO OWNER";
    private static final String INCREASE_AGE = "INCREASE AGE";
    private static final String REMOVE_DOG_OWNER = "REMOVE DOG FROM OWNER";
    private static final String HELP_COMMAND = "HELP";
    private static final int TABLE_FORMATTING_ROWS = 2;
    private static final int COLUMN_PADDING = 2;

    private InputReader input = new InputReader();
    private OwnerCollection owners = new OwnerCollection();
    private DogCollection dogs = new DogCollection();

    // programskelett
    private void start() {
        initialize();
        runCommandLoop();
        shutDown();
    }

    private void initialize() {
        System.out.println("Starting up");
    }

    private void shutDown() {
        System.out.println("Shutting down");
    }

    private void runCommandLoop() {
        System.out.println("WELCOME TO THE DOG REGISTER\n");
        cmdShowCommands();
        String command;
        do {
            command = readCommand().toUpperCase();
            handlecommand(command);
        } while (!command.equals(EXIT_COMMAND));
    }

    private void cmdShowCommands() {
        System.out.println("The following commands are available:" + "\n+ " + REGISTER_NEW_DOG + "\n+ " + REMOVE_DOG
                + "\n+ " + REGISTER_NEW_OWNER + "\n+ " + REMOVE_OWNER + "\n+ " + GIVE_DOG_OWNER + "\n+ "
                + REMOVE_DOG_OWNER + "\n+ " + INCREASE_AGE + "\n+ " + LIST_DOGS + "\n+ " + LIST_OWNERS + "\n+ "
                + HELP_COMMAND + "\n+ " + EXIT_COMMAND);
    }

    private String readCommand() {
        return input.readLine("\nType a command: \n");
    }

    private void handlecommand(String command) {
        switch (command) {
            case EXIT_COMMAND:
                break;
            case REGISTER_NEW_OWNER:
                cmdRegisterNewOwner();
                break;
            case REMOVE_OWNER:
                cmdRemoveOwner();
                break;
            case REGISTER_NEW_DOG:
                cmdRegisterNewDog();
                break;
            case REMOVE_DOG:
                cmdRemoveDog();
                break;
            case INCREASE_AGE:
                cmdIncreaseDogAge();
                break;
            case LIST_OWNERS:
                cmdListOwners();
                break;
            case LIST_DOGS:
                cmdListDogs();
                break;
            case GIVE_DOG_OWNER:
                cmdGiveDogOwner();
                break;
            case REMOVE_DOG_OWNER:
                cmdRemoveDogFromOwner();
                break;
            case HELP_COMMAND:
                cmdShowCommands();
                break;
            default:
                System.out.println("ERROR: Unrecognized command");
        }
    }

    // register new owner
    private void cmdRegisterNewOwner() {
        String name = input.readLine("Enter new owner: \n");
        name = checkOwnerName(name);
        if (name != null) {
            // ägarens namn finns redan i owners
            if (owners.containsOwner(name)) {
                System.out.println("ERROR: An owner with that name already exists");
                return;
            } else {
                addOwner(name);
            }
        }
    }

    // register new dog
    private void cmdRegisterNewDog() {
        String name = input.readLine("Enter name of dog: \n");
        name = checkDogName(name);
        if (name == null || dogs.containsDog(name)) {
            System.out.println("ERROR: Name not accepted (name was empty or dog already exists).");
            return;
        }
        String breed = input.readLine("Enter breed of dog: \n");
        checkDogBreed(breed);
        int age = input.readInt("Enter age of dog: \n");
        checkDogAge(age);
        int weight = input.readInt("Enter weight of dog in kilos: \n");
        checkDogWeight(weight);
        addDog(name, breed, age, weight);
    }

    // remove owner
    private void cmdRemoveOwner() {
        String name = selectOwner();
        if (name == null) {
            return;
        }
        // ägaren har hundar
        if (!owners.getOwner(name).getDogs().isEmpty()) {
            removeDogsOfOwner(name);
        }
        // ta bort ägaren
        owners.removeOwner(name);
        System.out.println("Owner " + name + " has been removed.");

    }

    // remove dog
    private void cmdRemoveDog() {
        String name = selectDog();
        if (name == null) {
            return;
        } else {
            cmdRemoveDog(name);
            System.out.println("Dog " + name + " has been removed.");
        }
    }

    private void cmdRemoveDog(String name) {
        // ta bort hund från ägare om ägare finns
        if (dogs.getDog(name).getOwner() != null) {
            dogs.getDog(name).setOwner(null);
        }
        // ta bort hund
        dogs.removeDog(name);
    }

    // list dogs
    private void cmdListDogs() {
        if (dogsEmpty()) {
            return;
        }
        double tailLength = input.readDouble("Enter minimum tail length: \n");
        createDogTable(tailLength);
    }

    // list owners
    private void cmdListOwners() {
        if (ownersEmpty()) {
            return;
        }
        createOwnerTable();
    }

    // increase age
    private void cmdIncreaseDogAge() {
        String dogName = selectDog();
        if (dogName == null) {
            return;
        } else {
            // öka åldern
            dogs.getDog(dogName).increaseAge();
            System.out.println(dogs.getDog(dogName).getName() + " is now one year older.");
        }
    }

    // give dog to owner
    private void cmdGiveDogOwner() {
        // inga ägare eller hundar finns
        if (!ownersEmpty() && !dogsEmpty()) {
            String dogName = selectDog();
            if (dogName != null) {
                if (dogs.getDog(dogName).getOwner() != null) {
                    System.out.println("ERROR: Dog " + dogName + " already has an owner.");
                    return;
                }
                setOwnerForDog(dogName);
            }
        }
    }

    // remove dog from owner
    private void cmdRemoveDogFromOwner() {
        // inga ägare eller hundar finns
        if (!ownersEmpty() && !dogsEmpty()) {
            String dogName = selectDog();
            if (dogName != null) {
                // ta bort hunden från ägaren
                if (dogs.getDog(dogName).getOwner() == null) {
                    System.out.println("ERROR: " + dogName + " already has no owner.");
                    return;
                } else {
                    dogs.getDog(dogName).getOwner().removeDog(dogs.getDog(dogName));
                    System.out.println(dogName + " now has no owner.");
                }
            }
        }
    }

    private String checkOwnerName(String name) {
        // name är tomt
        while (name.isBlank() || name.isEmpty()) {
            System.out.println("ERROR: Name may not be blank, please try again.");
            name = input.readLine("Enter new owner: \n");
        }
        return name;
    }

    private void addOwner(String name) {
        // lägg till owner
        Owner owner = new Owner(name);
        if (owners.addOwner(owner)) {
            System.out.println("Owner added: " + owner.getName());
        } else {
            System.out.println("ERROR: Owner could not be added.");
        }
    }

    private String checkDogName(String name) {
        // input är tomt
        while (name.isBlank() || name.isEmpty()) {
            System.out.println("ERROR: Name may not be blank, please try again.");
            name = input.readLine("Enter name of dog:  \n");
        }
        return name;
    }

    private void checkDogBreed(String breed) {
        while (breed.isBlank() || breed.isEmpty()) {
            System.out.println("ERROR: Breed may not be blank, please try again.");
            breed = input.readLine("Enter breed of dog:  \n");
        }
    }

    private void checkDogAge(int age) {
        while (age < 0) {
            System.out.println("ERROR: Age may not be negative or empty, please try again.");
            age = input.readInt("Enter age of dog:  \n");
        }
    }

    private void checkDogWeight(int weight) {
        // input är tomt
        while (weight < 0) {
            System.out.println("ERROR: Weight may not be negative or empty, please try again.");
            weight = input.readInt("Enter weight of dog in kilos: \n");
        }
    }

    private void addDog(String name, String breed, int age, int weight) {
        // skapa hunden
        Dog dog = new Dog(name, breed, age, weight);
        if (dogs.addDog(dog)) {
            System.out.println("Dog added: " + dog);
        } else {
            System.out.println("ERROR: Dog could not be added.");
        }
    }

    private String selectOwner() {
        // inga ägare finns
        if (!ownersEmpty()) {
            String name = input.readLine("Enter name of owner: \n");
            checkOwnerName(name);
            // finns ägaren?
            if (owners.containsOwner(name)) {
                return name;
            } else {
                System.out.println("ERROR: Owner " + name + " does not exist.");
            }
        }
        return null;
    }

    private boolean ownersEmpty() {
        if (owners.getOwners().isEmpty()) {
            System.out.println("ERROR: No owners in register.");
            return true;
        } else {
            return false;
        }
    }

    private String selectDog() {
        // inga hundar finns
        if (!dogsEmpty()) {
            String name = input.readLine("Enter name of dog: \n");
            // tomt name
            checkDogName(name);
            // hunden finns inte
            if (dogs.containsDog(name)) {
                return name;
            } else {
                System.out.println("ERROR: Dog " + name + " does not exist.");
            }
        }
        return null;
    }

    private boolean dogsEmpty() {
        if (dogs.getDogs().isEmpty()) {
            System.out.println("ERROR: No dogs in register.");
            return true;
        } else {
            return false;
        }
    }

    // radera en ägares hundar
    private void removeDogsOfOwner(String ownerName) {
        System.out.print(owners.getOwner(ownerName).getName() + "'s dog(s) ");
        for (Dog dog : owners.getOwner(ownerName).getDogs()) {
            System.out.print(dog.getName());
            cmdRemoveDog(dog.getName());
            if (owners.getOwner(ownerName).getDogs().size() > 0) {
                System.out.print(", ");
            }
        }
        System.out.println(" have been removed.");
    }

    // bygg tabell för hundar
    private void createDogTable(double tailLength) {
        // Hitta hundar
        ArrayList<Dog> dogsToPrint = dogs.getDogsWithMinTailLength(tailLength);
        // skapa matris
        String[] headers = { "\nNAME ", " BREED", " AGE", " WEIGHT", " TAIL", " OWNER" };
        String[][] dogTable = createMatrix(headers, dogsToPrint);
        // skriv ut tabell
        for (int row = 0; row < dogTable[0].length - 1; row++) {
            printTableRow(dogTable, row);
        }
    }

    // bygg tabell för ägare
    private void createOwnerTable() {
        String[] headers = { "\nNAME ", " DOGS" };
        String[][] ownerTable = createMatrix(headers);
        for (int row = 0; row < ownerTable[0].length - 1; row++) {
            printTableRow(ownerTable, row);
        }
    }

    // bygg matris, hundar
    private String[][] createMatrix(String[] headers, ArrayList<Dog> dogsToPrint) {
        // gör plats för rubrikrad och kolumnbreddshållare
        int rows = dogsToPrint.size() + TABLE_FORMATTING_ROWS;
        String[][] table = fillMatrix(rows, dogsToPrint, headers);
        setColumnWidth(headers.length, rows, table);
        return table;
    }

    // bygg matris, ägare.
    private String[][] createMatrix(String[] headers) {
        // gör plats för rubrikrad och kolumnbreddshållare
        int rows = owners.getOwners().size() + TABLE_FORMATTING_ROWS;
        String[][] table = fillMatrix(rows, headers);
        setColumnWidth(headers.length, rows, table);
        return table;
    }

    // fyll matris, hundar
    private String[][] fillMatrix(int rows, ArrayList<Dog> dogsToPrint, String[] headers) {
        String[][] table = new String[headers.length][rows];
        // skapa rubrikrad
        for (int col = 0; col < headers.length; col++) {
            table[col][0] = headers[col];
        }
        // lägg in rader
        for (int row = 1; row < rows - 1; row++) {
            Dog currentDog = dogsToPrint.get(row - 1);
            for (int col = 0; col < headers.length; col++) {
                // -1 för att komma åt items på index 0, eftersom rad 0 i tabellen är
                // rubrikraden
                table[col][row] = attributeStrings(currentDog, headers[col]);
            }
        }
        return table;
    }

    // fyll matris, ägare
    private String[][] fillMatrix(int rows, String[] headers) {
        String[][] table = new String[headers.length][rows];
        for (int col = 0; col < headers.length; col++) {
            table[col][0] = headers[col];
        }
        // lägg in rader
        for (int row = 1; row < rows - 1; row++) {
            Owner currentOwner = owners.getOwners().get(row - 1);
            for (int col = 0; col < headers.length; col++) {
                // -1 för att komma åt items på index 0, eftersom rad 0 i tabellen är
                // rubrikraden
                table[col][row] = attributeStrings(currentOwner, headers[col]);
            }

        }
        return table;
    }

    // hitta kolumnbredd för varje kolumn
    private void setColumnWidth(int columns, int rows, String[][] table) {
        for (int col = 0; col < columns; col++) {
            int colWidth = 0;
            for (int row = 0; row < rows; row++) {
                if (table[col][row] != null) {
                    int width = table[col][row].length();
                    if (colWidth < width) {
                        colWidth = width;
                    }
                }
            }
            table[col][rows - 1] = String.valueOf(colWidth + COLUMN_PADDING);
        }
    }

    // formattera tabellrad
    private void printTableRow(String[][] stringMatrix, int rowCount) {
        String line = "";
        for (int column = 0; column < stringMatrix.length; column++) {
            line += createTableCell(stringMatrix[column][rowCount],
                    Integer.valueOf(stringMatrix[column][stringMatrix[0].length - 1]));
        }
        System.out.println(line);
    }

    // formatera celler i tabell
    private String createTableCell(String string, int columnWidth) {
        if (string == null) {
            return " ".repeat(columnWidth);
        }
        while (string.length() <= columnWidth) {
            string += " ";
        }
        return string;
    }

    private String attributeStrings(Dog currentDog, String header) {
        switch (header) {
            case "\nNAME ":
                return currentDog.getName();
            case " BREED":
                return currentDog.getBreed();
            case " AGE":
                return String.valueOf(currentDog.getAge());
            case " WEIGHT":
                return String.valueOf(currentDog.getWeight());
            case " TAIL":
                return String.valueOf(currentDog.getTailLength());
            case " OWNER":
                if (currentDog.getOwner() != null) {
                    return currentDog.getOwner().getName();
                } else {
                    return "";
                }
            default:
                return null;
        }
    }

    private String attributeStrings(Owner currentOwner, String header) {
        switch (header) {
            case "\nNAME ":
                return currentOwner.getName();
            case " DOGS":
                return ownedDogsString(currentOwner);
            default:
                return null;
        }
    }

    // skapa string med ägarens hundar
    private String ownedDogsString(Owner currentOwner) {
        String ownedDogs = "";
        for (Dog dog : currentOwner.getDogs()) {
            ownedDogs += dog.getName();
            if (currentOwner.getDogs().indexOf(dog) < currentOwner.getDogs().size() - 1) {
                ownedDogs += ", ";
            }
        }
        return ownedDogs;
    }

    private void setOwnerForDog(String dogName) {
        // hund har redan ägare
        String ownerName = input.readLine("Enter name of owner: \n");
        if (owners.containsOwner(ownerName) && dogs.getDog(dogName).setOwner(owners.getOwner(ownerName))) {
            System.out.println(
                    dogs.getDog(dogName).getName() + " is now owned by " + owners.getOwner(ownerName).getName());
        } else {
            System.out.println("ERROR: Failed to assign dog to owner.");
        }
    }

    public static void main(String[] args) {
        new DogRegister().start();
    }

}
