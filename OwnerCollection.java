
//Ida Andersson idan1222
import java.util.*;

public class OwnerCollection {

    // Ägarlistan
    private Owner[] ownerList = new Owner[0];

    // metoder
    // lägg till ägare
    public boolean addOwner(Owner owner) {
        if (containsOwner(owner)) {
            return false;
        }
        Owner[] tempList = Arrays.copyOf(ownerList, ownerList.length + 1);
        tempList[tempList.length - 1] = owner;
        ownerList = tempList;
        return true;
    }

    // ta bort ägare
    public boolean removeOwner(Owner owner) {
        if (owner.getDogs().isEmpty() || owner.getDogs() == null) {
            return removeOwner(owner.getName());
        }
        return false;
    }

    /*
     * ta bort ägare med visst namn
     * (byter plats på ägaren vi vill ta bort med det sista elementet, kortar sedan ner arrayen med 1 vilket automatiskt
     *  tar bort sista elementet)
     */
    public boolean removeOwner(String ownerName) {
        int index = indexOfOwner(ownerName);        
        if (index >= 0) { 
            if (ownerList[index].getDogs().isEmpty() || ownerList[index].getDogs() == null) {
                ownerList[index] = ownerList[ownerList.length - 1];
                Owner[] tempList = Arrays.copyOf(ownerList, ownerList.length-1);
                ownerList = tempList;
                return true;
            }
        }
        return false;
    }

    // kolla om ägare finns
    public boolean containsOwner(Owner owner) {
        return containsOwner(owner.getName());
    }

    public boolean containsOwner(String ownerName) {
        if (indexOfOwner(ownerName) >= 0) {
            return true;
        }
        return false;
    }

    // vilket index ett visst ägarnamn hör till, hjälpmetod för contains- och
    // get-metoder

    private int indexOfOwner(String ownerName) {
        for (int i = 0; i < ownerList.length; i++) {
            if (ownerList[i] != null) {
                String name = ownerList[i].getName();
                if (name.equalsIgnoreCase(ownerName)) {
                    return i;
                }
            }

        }
        return -1;
    }

    // hämta ägare
    public Owner getOwner(String ownerName) {
        int index = indexOfOwner(ownerName);
        if (index >= 0) {
            return ownerList[index];
        }
        return null;
    }

    // hämta alla ägare.
    public ArrayList<Owner> getOwners() {
        ArrayList<Owner> ownersAsArrayList = new ArrayList<>();
        for (Owner element : ownerList) {
            ownersAsArrayList.add(element);
        }
        ownersAsArrayList.sort(null);
        return ownersAsArrayList;
    }

}
