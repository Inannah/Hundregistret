
//Ida Andersson idan1222
import java.util.Comparator;

public class DogTailNameComparator implements Comparator<Dog> {

    public int compare(Dog first, Dog second) {
        // kolla om svanslängd sklijer sig
        if (first.getTailLength() < second.getTailLength()) {
            return -1;
        }
        if (first.getTailLength() > second.getTailLength()) {
            return 1;
        }
        // om samma svanslängd, sortera efter namn
        return first.getName().compareTo(second.getName());
    }

}
