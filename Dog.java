//Ida Andersson idan1222
public class Dog {

    private static final double DACHSHUND_TAIL_LENGTH = 3.7;
    private static final int TAIL_DIVIDER = 10;

    // array över ord för tax på olika språk
    private static String[] arrOfDachshunds = { "tax", "dachshund", "mäyräkoira", "teckel", "melhundo", "gravhund",
            "dackel", "bassotto tedesco", "Та́кса", "jazavčar", "Taks", "Taksis", "Taksas", "tacskó", "Jamnik",
            "Jazvečík", "Jazavičar", "Dakhund", "Јазавичар", "Дакел", "الدَّشْهَند", "داشهوند", "תחש", "腊肠犬", "ダックスフント",
            "닥스훈트", "Chó Dachshund", "ดัคส์ฮุนท์", "臘腸狗" };

    // attribut
    private String name;
    private String breed;
    private int age;
    private int weight;
    private double tailLength;
    private Owner owner;

    // konstruktor
    public Dog(String name, String breed, int age, int weight) {
        this.name = formatName(name);
        this.breed = formatName(breed);
        this.age = age;
        this.weight = weight;
        this.tailLength = setTailLength(age, weight);
    }

    // get-metoder
    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public int getAge() {
        return age;
    }

    public int getWeight() {
        return weight;
    }

    public double getTailLength() {
        return tailLength;
    }

    // metoder
    // Jag funderade på att anropa namn-metoden i ägarklassen, men var osäker på om
    // ökningen av coupling var värt det.

    private String formatName(String name) {
        // kontrollerar för tomt namn
        if (name.equals(null) || name.isBlank()) {
            name = "No Name";
        }
        // formattering genom att först sätta alla bokstäver som gemener, därefter dela
        // upp namnen i flera String i en lista och sätta första bokstaven i varje
        // String som versal, och därefter sätta ihop det till en enda String igen
        name = name.toLowerCase();
        String[] arrOfName = name.split(" ");
        for (int i = 0; i < arrOfName.length; i++) {
            arrOfName[i] = arrOfName[i].substring(0, 1).toUpperCase() + arrOfName[i].substring(1);
        }
        name = String.join(" ", arrOfName);
        return name;
    }

    // svanslängd
    private double setTailLength(int age, int weight) {
        // svanslängd för taxar
        for (String element : arrOfDachshunds) {
            // kontrollerar om angiven ras är ett ord som betyder tax
            if (breed.equalsIgnoreCase(element)) {
                tailLength = DACHSHUND_TAIL_LENGTH;
                return tailLength;
            }
        }
        // beräkning av svanslängd för icke-taxar
        double w = weight;
        tailLength = age * w;
        tailLength = tailLength / TAIL_DIVIDER;
        return tailLength;
    }

    // Öka åldern med 1
    public int increaseAge() {
        if (age == Integer.MAX_VALUE) {
            return age;
        }
        age++;
        setTailLength(age, weight);
        return age;
    }

    // lägg till ägare
    public boolean setOwner(Owner owner) {
        // ta bort ägare om metoden anropas med null
        if (owner == null) {
            if (this.owner == null){
                return false;
            }
            // ta bort hunden från ägaren om den finns i ägarens hundlista
            if (this.owner.getDogs().contains(this)) {
                this.owner.removeDog(this);
            }
            // sätt ägaren till null
            this.owner = owner;
            return true;
        }
        // Har this redan en ägare?
        if (this.owner == null) {
            // Spara ägaren
            this.owner = owner;
            // Äger ägaren redan this?
            if (owner.getDogs().contains(this)) {
                return true;
            }
            if (!owner.getDogs().contains(this)) {
                // Be ägaren lägga till this
                owner.addDog(this);
                return true;
            }
        }
        return false;
    }

    // kolla ägare
    public Owner getOwner() {
        return owner;
    }

    // utskriftsformat
    @Override
    public String toString() {
        return "Name: " + getName() + " Age: " + getAge() + " Breed: " + getBreed() + " Weight: " + getWeight()
                + " Tail: " + getTailLength() + " Owner: " + getOwner();
    }

}
