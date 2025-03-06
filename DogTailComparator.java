
//Ida Andersson idan1222
import java.util.Comparator;

public class DogTailComparator implements Comparator<Dog> {

    public int compare(Dog first, Dog second) {
        if (first.getTailLength() < second.getTailLength()) {
            return -1;
        }
        if (first.getTailLength() > second.getTailLength()) {
            return 1;
        }
        return 0;
    }

}
