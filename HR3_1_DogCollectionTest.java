import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

/**
 * Testfall för hundsamlingen i uppgift HR3.1.
 * <p>
 * Beskrivningen av testfallens uppgift, styrka och svagheter från
 * <code>{@link HR1_1_OwnerTest}</code> gäller (naturligvis) också för
 * testfallen i denna klass. Var speciellt uppmärksam på att testfallen kan
 * uppdateras när som helst, inklusive <em>efter</em> deadline.
 * 
 * @author Henrik Bergström
 * @version 2024-01-28 11:14
 * @see HR1_1_OwnerTest
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("HR3.1: Testfall för hundsamlingen")
public class HR3_1_DogCollectionTest {

	public static final String VERSION = "2024-01-28 11:14";

	private static final Dog FIRST_DOG = new Dog("First", "Breed", 1, 2);
	private static final Dog SECOND_DOG = new Dog("Second", "Breed", 3, 4);
	private static final Dog THIRD_DOG = new Dog("Third", "Breed", 5, 6);

	private static final Dog A_DOG = SECOND_DOG;

	private static final Dog OTHER_DOG = new Dog("Other", "Breed", 5, 6);

	private static final Field ASSUMED_FIELD_CONTAINING_DOGS = identifyFieldContainingDogs();
	private static final Method ASSUMED_GET_DOGS_WITH_TAIL_METHOD = identifyGetDogsWithTailMethod();

	private DogCollection collection = new DogCollection();

	private static Field identifyFieldContainingDogs() {
		var fields = Stream.of(DogCollection.class.getDeclaredFields())
				.filter(f -> Collection.class.isAssignableFrom(f.getType())).toList();

		if (fields.size() != 1)
			return null;

		fields.get(0).setAccessible(true);
		return fields.get(0);
	}

	private static Method identifyGetDogsWithTailMethod() {
		var methods = Stream.of(DogCollection.class.getMethods())
				.filter(m -> m.getParameterCount() == 1 && m.getParameterTypes()[0] == double.class).toList();

		if (methods.size() != 1)
			return null;

		return methods.get(0);
	}

	@SuppressWarnings("unchecked")
	private Collection<Dog> dogsInCollection() {
		if (ASSUMED_FIELD_CONTAINING_DOGS == null)
			fail("Inget fält innehållandes hundar har kunnat identifieras i samlingsklassen. Detta kan bero på att det inte finns något sådant fält, på att det finns flera möjliga fält, eller på att datatypen är fel");

		try {
			return (Collection<Dog>) ASSUMED_FIELD_CONTAINING_DOGS.get(collection);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			fail("""
					Fel vid försök att accessa hundarna i samlingsklassen. Mer information om vad som gick fel ges i "Caused by"-delen av felmeddelandet.
					Fältet som accessades var: \t%s
					"""
					.formatted(ASSUMED_FIELD_CONTAINING_DOGS), e);

			// Kan inte inträffa på grund av fail-satsen ovan, men kompilatorn kan inte se
			// detta, så return-satsen är nödvändig
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Collection<Dog> callGetDogsWithTailMethod(double minimumTailLength) {
		if (ASSUMED_GET_DOGS_WITH_TAIL_METHOD == null)
			fail("Inget metod liknande getDogsWithTail har kunnat identifieras i samlingsklassen. Detta kan bero på att det inte finns någon sådand metod, på att det finns flera möjliga metoder, eller på att datatypen på parametern är fel");

		try {
			return (Collection<Dog>) ASSUMED_GET_DOGS_WITH_TAIL_METHOD.invoke(collection, minimumTailLength);
		} catch (IllegalAccessException | InvocationTargetException e) {
			fail("""
					Fel vid försök att anropa metoden för att hämta hundar med en viss svanslängd i samlingsklassen. Mer information om vad som gick fel ges i "Caused by"-delen av felmeddelandet.
					Metoden som användes var: \t%s
					"""
					.formatted(ASSUMED_FIELD_CONTAINING_DOGS), e);

			// Kan inte inträffa på grund av fail-satsen ovan, men kompilatorn kan inte se
			// detta, så return-satsen är nödvändig
			return null;
		}

	}

	private void addAllThreeTestDogsInOrder() {
		collection.addDog(FIRST_DOG);
		collection.addDog(SECOND_DOG);
		collection.addDog(THIRD_DOG);
	}

	private void assertCollectionUpdatedCorrectly(boolean expectedReturnValue, boolean actualReturnValue,
			Dog... expected) {
		var expectedAsList = Arrays.asList(expected);
		var actual = dogsInCollection();
		var extra = new ArrayList<>(actual);
		extra.removeAll(expectedAsList);
		var missing = new ArrayList<>(expectedAsList);
		missing.removeAll(actual);

		assertAll(() -> assertEquals(expectedReturnValue, actualReturnValue, "Fel returvärde"),
				() -> assertEquals(expected.length, actual.size(), "Fel antal hundar i samlingen"),
				() -> assertTrue(extra.isEmpty(), "Dessa hundar borde inte finnas i samlingen: " + extra),
				() -> assertTrue(missing.isEmpty(), "Dessa hundar saknas i samlingen: " + missing));
	}

	@Test
	@Order(10)
	@DisplayName("Hundsamlingsklassen implementerad enligt instruktionerna")
	public void validateOwnerCollectionImplementation() {
		new DogCollectionImplementationValidator().execute();
	}

	@Test
	@Order(20)
	@DisplayName("Hundklassen implementerad enligt instruktionerna")
	public void validateOwnerImplementation() {
		new DogImplementationValidator().execute();
	}

	@Test
	@Order(30)
	@DisplayName("En ny samling ska inte innehålla några hundar")
	public void noDogsInNewCollection() {
		assertTrue(dogsInCollection().isEmpty(), "En ny samling ska inte innehålla några hundar");
	}

	////////////////////////////////////////////
	//
	// Tester för addDog-metoden
	//
	////////////////////////////////////////////

	@Test
	@Order(40)
	@DisplayName("Lägg till en hund")
	public void addingOneDog() {
		var rv = collection.addDog(FIRST_DOG);
		assertCollectionUpdatedCorrectly(true, rv, FIRST_DOG);
	}

	@Test
	@Order(50)
	@DisplayName("Lägg till två hundar")
	public void addingTwoDogs() {
		var rv1 = collection.addDog(FIRST_DOG);
		assertCollectionUpdatedCorrectly(true, rv1, FIRST_DOG);
		var rv2 = collection.addDog(SECOND_DOG);
		assertCollectionUpdatedCorrectly(true, rv2, FIRST_DOG, SECOND_DOG);
	}

	@Test
	@Order(60)
	@DisplayName("Lägg till tre hundar")
	public void addingThreeDogs() {
		var rv1 = collection.addDog(FIRST_DOG);
		assertCollectionUpdatedCorrectly(true, rv1, FIRST_DOG);
		var rv2 = collection.addDog(SECOND_DOG);
		assertCollectionUpdatedCorrectly(true, rv2, FIRST_DOG, SECOND_DOG);
		var rv3 = collection.addDog(THIRD_DOG);
		assertCollectionUpdatedCorrectly(true, rv3, FIRST_DOG, SECOND_DOG, THIRD_DOG);
	}

	@Test
	@Order(70)
	@DisplayName("Lägg till två hundar med samma namn")
	public void addingTwoDogsWithTheSameName() {
		collection.addDog(FIRST_DOG);
		var rv = collection.addDog(new Dog(FIRST_DOG.getName(), "Breed", 10, 20));
		assertCollectionUpdatedCorrectly(false, rv, FIRST_DOG);
	}

	////////////////////////////////////////////
	//
	// Tester för containsDog-metoden som tar en
	// hund som parameter
	//
	////////////////////////////////////////////

	@Test
	@Order(80)
	@DisplayName("Sökning i en tom samling")
	public void attemptingToSearchForNonexistingDogInEmptyCollection() {
		assertFalse(collection.containsDog(OTHER_DOG));
	}

	@Test
	@Order(90)
	@DisplayName("Sökning efter en hund som inte existerar")
	public void attemptingToSearchForNonexistingDog() {
		addAllThreeTestDogsInOrder();
		assertFalse(collection.containsDog(OTHER_DOG));
	}

	@Test
	@Order(100)
	@DisplayName("Sökning efter den första hunden")
	public void searchingForFirstDog() {
		addAllThreeTestDogsInOrder();
		assertTrue(collection.containsDog(FIRST_DOG));
	}

	@Test
	@Order(110)
	@DisplayName("Sökning efter den andra hunden")
	public void searchingForSecondDog() {
		addAllThreeTestDogsInOrder();
		assertTrue(collection.containsDog(SECOND_DOG));
	}

	@Test
	@Order(120)
	@DisplayName("Sökning efter den sista hunden")
	public void searchingForLastDog() {
		addAllThreeTestDogsInOrder();
		assertTrue(collection.containsDog(THIRD_DOG));
	}

	@Test
	@Order(130)
	@DisplayName("Sökning förändrar inte innehållet i samlingen")
	public void searchingDoesNotChangeContent() {
		addAllThreeTestDogsInOrder();
		var rv = collection.containsDog(A_DOG);
		assertCollectionUpdatedCorrectly(true, rv, FIRST_DOG, SECOND_DOG, THIRD_DOG);
	}

	////////////////////////////////////////////
	//
	// Tester för containsDog-metoden som tar
	// ett namn som parameter
	//
	////////////////////////////////////////////

	@Test
	@Order(140)
	@DisplayName("Sökning i en tom samling via namn")
	public void attemptingToSearchForNonexistingDogInEmptyCollectionByName() {
		assertFalse(collection.containsDog(OTHER_DOG.getName()));
	}

	@Test
	@Order(150)
	@DisplayName("Sökning efter en hund som inte existerar via namn")
	public void attemptingToSearchForNonexistingDogByName() {
		addAllThreeTestDogsInOrder();
		assertFalse(collection.containsDog(OTHER_DOG.getName()));
	}

	@Test
	@Order(160)
	@DisplayName("Sökning efter den första hunden via namn")
	public void searchingForFirstDogByName() {
		addAllThreeTestDogsInOrder();
		assertTrue(collection.containsDog(FIRST_DOG.getName()));
	}

	@Test
	@Order(170)
	@DisplayName("Sökning efter den andra hunden via namn")
	public void searchingForSecondDogByName() {
		addAllThreeTestDogsInOrder();
		assertTrue(collection.containsDog(SECOND_DOG.getName()));
	}

	@Test
	@Order(180)
	@DisplayName("Sökning efter den sista hunden via namn")
	public void searchingForLastDogByName() {
		addAllThreeTestDogsInOrder();
		assertTrue(collection.containsDog(THIRD_DOG.getName()));
	}

	@Test
	@Order(190)
	@DisplayName("Sökning efter hund via namn, nytt strängobjekt för namnet")
	public void searchingForDogByNameNewStringObject() {
		addAllThreeTestDogsInOrder();
		assertTrue(collection.containsDog(new String(A_DOG.getName())));
	}

	@Test
	@Order(200)
	@DisplayName("Sökning via namn förändrar inte innehållet i samlingen")
	public void searchingDoesNotChangeContentByName() {
		addAllThreeTestDogsInOrder();
		var rv = collection.containsDog(A_DOG.getName());
		assertCollectionUpdatedCorrectly(true, rv, FIRST_DOG, SECOND_DOG, THIRD_DOG);
	}

	////////////////////////////////////////////
	//
	// Tester för getDog-metoden
	//
	////////////////////////////////////////////

	@Test
	@Order(210)
	@DisplayName("Försök att hämta ut en hund ur en tom samling")
	public void attemptingToGetDogFromEmptyCollection() {
		// TODO: hantera Optional
		var dog = collection.getDog(OTHER_DOG.getName());
		assertNull(dog, "Hunden borde inte finnas");
	}

	@Test
	@Order(220)
	@DisplayName("Försök att hämta en hund som inte existerar")
	public void attemptingToGetNonexistingDog() {
		addAllThreeTestDogsInOrder();
		var dog = collection.getDog(OTHER_DOG.getName());
		assertNull(dog, "Hunden borde inte finnas");
	}

	@Test
	@Order(230)
	@DisplayName("Hämta den enda hunden")
	public void getOnlyDog() {
		collection.addDog(A_DOG);
		var dog = collection.getDog(A_DOG.getName());
		assertEquals(A_DOG, dog);
	}

	@Test
	@Order(240)
	@DisplayName("Hämta den första hunden")
	public void getFirstDog() {
		addAllThreeTestDogsInOrder();
		var dog = collection.getDog(FIRST_DOG.getName());
		assertEquals(FIRST_DOG, dog);
	}

	@Test
	@Order(250)
	@DisplayName("Hämta den andra hunden")
	public void getSecondDog() {
		addAllThreeTestDogsInOrder();
		var dog = collection.getDog(SECOND_DOG.getName());
		assertEquals(SECOND_DOG, dog);
	}

	@Test
	@Order(260)
	@DisplayName("Hämta den sista hunden")
	public void getLastDog() {
		addAllThreeTestDogsInOrder();
		var dog = collection.getDog(THIRD_DOG.getName());
		assertEquals(THIRD_DOG, dog);
	}

	@Test
	@Order(270)
	@DisplayName("Hämta en hund, nytt strängobjekt för namnet")
	public void getDogNewStringObject() {
		addAllThreeTestDogsInOrder();
		var dog = collection.getDog(new String(A_DOG.getName()));
		assertEquals(A_DOG, dog);
	}

	@Test
	@Order(280)
	@DisplayName("Att hämta en hund förändrar inte innehållet i samlingen")
	public void getDogDoesNotChangeContent() {
		addAllThreeTestDogsInOrder();
		var dog = collection.getDog(A_DOG.getName());
		assertEquals(A_DOG, dog);
	}

	////////////////////////////////////////////
	//
	// Tester för getDogs-metoden
	//
	////////////////////////////////////////////

	@Test
	@Order(290)
	@DisplayName("Hämta hundarna från en tom samling")
	public void getDogsFromEmptyCollection() {
		var dogs = new HashSet<Dog>(collection.getDogs());
		assertEquals(new HashSet<>(), dogs);
	}

	@Test
	@Order(300)
	@DisplayName("Hämta hundarna från en samling innehållandes hundar")
	public void getDogsFromNonEmptyCollection() {
		addAllThreeTestDogsInOrder();
		var dogs = new HashSet<Dog>(collection.getDogs());
		assertEquals(new HashSet<>(Arrays.asList(FIRST_DOG, SECOND_DOG, THIRD_DOG)), dogs);
	}

	@Test
	@Order(310)
	@DisplayName("Att hämta hundarna förändrar inte innehållet i samlingen")
	public void getDogsDoesNotChangeContent() {
		addAllThreeTestDogsInOrder();
		new HashSet<Dog>(collection.getDogs());
		// De första argumenten är irrelevanta i detta test
		assertCollectionUpdatedCorrectly(true, true, FIRST_DOG, SECOND_DOG, THIRD_DOG);
	}

	@Test
	@Order(320)
	@DisplayName("Att hämta hundarna ger en kopia")
	public void getDogsReturnsCopy() {
		addAllThreeTestDogsInOrder();
		assertNotSame(dogsInCollection(), collection.getDogs());
	}

	@Test
	@Order(330)
	@DisplayName("Förändringar i samlingen med de hämtade hundarna påverkar inte orginalet")
	public void changesToCollectionFromGetDogsDoesNotChangeOriginal() {
		addAllThreeTestDogsInOrder();
		var dogs = collection.getDogs();
		try {
			dogs.remove(A_DOG);
		} catch (UnsupportedOperationException e) {
			// Förväntat möjligt undantag, inget att göra här.
		}
		// De första argumenten är irrelevanta i detta test
		assertCollectionUpdatedCorrectly(true, true, FIRST_DOG, SECOND_DOG, THIRD_DOG);
	}

	@Test
	@Order(340)
	@DisplayName("Hundarna kommer i rätt ordning")
	public void getDogsReturnsSortedList() {
		// Samma exempelhundar som i HR2.3, de ska dock bara sorteras på namn
		Dog rex = new Dog("Rex", "BREED", 15, 10);
		Dog kisen = new Dog("Kisen", "BREED", 20, 10);
		Dog karo = new Dog("Karo", "BREED", 10, 10);
		Dog fido = new Dog("Fido", "BREED", 15, 10);

		var dogs = Arrays.asList(rex, kisen, karo, fido);
		for (Dog dog : dogs) {
			collection.addDog(dog);
		}

		assertEquals(Arrays.asList(fido, karo, kisen, rex), collection.getDogs());
	}

	@RepeatedTest(5)
	@Order(350)
	@DisplayName("Hundarna kommer i rätt ordning oavsett hur de sätts in")
	public void getDogsReturnsSortedListIndependenOfInsertionOrder() {
		// Samma exempelhundar som i HR2.3, de ska dock bara sorteras på namn...
		Dog rex = new Dog("Rex", "BREED", 15, 10);
		Dog kisen = new Dog("Kisen", "BREED", 20, 10);
		Dog karo = new Dog("Karo", "BREED", 10, 10);
		Dog fido = new Dog("Fido", "BREED", 15, 10);

		var dogs = Arrays.asList(rex, kisen, karo, fido);

		// ... men nu i blandad ordning
		Collections.shuffle(dogs);
		for (Dog dog : dogs) {
			collection.addDog(dog);
		}

		assertEquals(Arrays.asList(fido, karo, kisen, rex), collection.getDogs());
	}

	////////////////////////////////////////////
	//
	// Tester för getDogsWithTail-metoden
	//
	////////////////////////////////////////////

	/**
	 * Detta test är *MYCKET* svagt. Det enda som testas är att metoden döpts om.
	 * Inga försök görs att kontrollera att metoden har ett bra namn. Detta
	 * kontrolleras istället i samband med granskningar och slutinlämning.
	 */
	@Test
	@Order(360)
	@DisplayName("getDogsWithTail har döpts om")
	public void getDogsWithTailIsRenamed() {
		assertThrows(NoSuchMethodException.class, () -> DogCollection.class.getMethod("getDogsWithTail", double.class),
				"I uppgiften ingår att döpa om getDogsWithTail");
	}

	@Test
	@Order(370)
	@DisplayName("Hämta hundarna med viss svanslängd från en tom samling")
	public void getDogsWithTailFromEmptyCollection() {
		var dogs = new HashSet<Dog>(callGetDogsWithTailMethod(0));
		assertEquals(new HashSet<>(), dogs);
	}

	@Test
	@Order(380)
	@DisplayName("Hämta hundarna med viss svanslängd från en samling där ingen hund har en tillräckligt lång svans")
	public void getDogsWithTailFromCollectionWhereNoDogHaveALongEnoughTail() {
		addAllThreeTestDogsInOrder();
		var dogs = new HashSet<Dog>(callGetDogsWithTailMethod(1000));
		assertEquals(new HashSet<>(), dogs);
	}

	@Test
	@Order(390)
	@DisplayName("Hämta hundarna med viss svanslängd från en samling där en hund har en tillräckligt lång svans")
	public void getDogsWithTailFromCollectionWhereOneDogHaveALongEnoughTail() {
		addAllThreeTestDogsInOrder();
		var dogs = new HashSet<Dog>(callGetDogsWithTailMethod(THIRD_DOG.getTailLength()));
		assertEquals(new HashSet<>(Arrays.asList(THIRD_DOG)), dogs);
	}

	@Test
	@Order(400)
	@DisplayName("Hämta hundarna med viss svanslängd från en samling där flera hundar har en tillräckligt lång svans")
	public void getDogsWithTailFromCollectionMultipleDogsHaveALongEnoughTail() {
		addAllThreeTestDogsInOrder();
		var dogs = new HashSet<Dog>(callGetDogsWithTailMethod(SECOND_DOG.getTailLength()));
		assertEquals(new HashSet<>(Arrays.asList(SECOND_DOG, THIRD_DOG)), dogs);
	}

	@Test
	@Order(410)
	@DisplayName("Hämta hundarna med viss svanslängd från en samling där alla hundar har en tillräckligt lång svans")
	public void getDogsWithTailFromCollectionWhereAllDogsHaveALongEnoughTail() {
		addAllThreeTestDogsInOrder();
		var dogs = new HashSet<Dog>(callGetDogsWithTailMethod(FIRST_DOG.getTailLength()));
		assertEquals(new HashSet<>(Arrays.asList(FIRST_DOG, SECOND_DOG, THIRD_DOG)), dogs);
	}

	@Test
	@Order(420)
	@DisplayName("Att hämta hundar med en viss svanslängd förändrar inte innehållet i samlingen")
	public void getDogsWithTailDoesNotChangeContent() {
		addAllThreeTestDogsInOrder();
		callGetDogsWithTailMethod(0);
		// De första argumenten är irrelevanta i detta test
		assertCollectionUpdatedCorrectly(true, true, FIRST_DOG, SECOND_DOG, THIRD_DOG);
	}
	
	@Test
	@Order(430)
	@DisplayName("Hundarna med viss minsta svanslängd kommer i rätt ordning")
	public void getDogsWithTailReturnsSortedList() {
		// Samma exempelhundar som i HR2.3
		Dog rex = new Dog("Rex", "BREED", 15, 10);
		Dog kisen = new Dog("Kisen", "BREED", 20, 10);
		Dog karo = new Dog("Karo", "BREED", 10, 10);
		Dog fido = new Dog("Fido", "BREED", 15, 10);

		var originalDogs = Arrays.asList(rex, kisen, karo, fido);
		for (Dog dog : originalDogs) {
			collection.addDog(dog);
		}

		var dogs = callGetDogsWithTailMethod(0);
		assertEquals(Arrays.asList(karo, fido, rex, kisen), dogs);
	}

	@RepeatedTest(5)
	@Order(440)
	@DisplayName("Hundarna med viss minsta svanslängd kommer i rätt ordning oavsett hur de sätts in")
	public void getDogsWithTailReturnsSortedListIndependenOfInsertionOrder() {
		// Samma exempelhundar som i HR2.3
		Dog rex = new Dog("Rex", "BREED", 15, 10);
		Dog kisen = new Dog("Kisen", "BREED", 20, 10);
		Dog karo = new Dog("Karo", "BREED", 10, 10);
		Dog fido = new Dog("Fido", "BREED", 15, 10);

		var originalDogs = Arrays.asList(rex, kisen, karo, fido);

		// ... men nu i blandad ordning
		Collections.shuffle(originalDogs);
		for (Dog dog : originalDogs) {
			collection.addDog(dog);
		}

		var dogs = callGetDogsWithTailMethod(0);
		assertEquals(Arrays.asList(karo, fido, rex, kisen), dogs);
	}

	////////////////////////////////////////////
	//
	// Tester för removeDog-metoden som tar en
	// hund som parameter
	//
	////////////////////////////////////////////

	@Test
	@Order(450)
	@DisplayName("Ta bort den enda hunden i samlingen")
	public void removingOnlyDog() {
		collection.addDog(FIRST_DOG);
		var rv = collection.removeDog(FIRST_DOG);
		assertCollectionUpdatedCorrectly(true, rv);
	}

	@Test
	@Order(460)
	@DisplayName("Ta bort den först tillagda hunden från samlingen")
	public void removingFirstDog() {
		addAllThreeTestDogsInOrder();
		var rv = collection.removeDog(FIRST_DOG);
		assertCollectionUpdatedCorrectly(true, rv, SECOND_DOG, THIRD_DOG);
	}

	@Test
	@Order(470)
	@DisplayName("Ta bort den andra tillagda hunden från samlingen")
	public void removingSecondDog() {
		addAllThreeTestDogsInOrder();
		var rv = collection.removeDog(SECOND_DOG);
		assertCollectionUpdatedCorrectly(true, rv, FIRST_DOG, THIRD_DOG);
	}

	@Test
	@Order(480)
	@DisplayName("Ta bort den sist tillagda hunden från samlingen")
	public void removingLastDog() {
		addAllThreeTestDogsInOrder();
		var rv = collection.removeDog(THIRD_DOG);
		assertCollectionUpdatedCorrectly(true, rv, FIRST_DOG, SECOND_DOG);
	}

	@Test
	@Order(490)
	@DisplayName("Ta bort alla hundar från samlingen i insättningsordning")
	public void removingAllDogsInOrder() {
		addAllThreeTestDogsInOrder();
		var rv1 = collection.removeDog(FIRST_DOG);
		assertCollectionUpdatedCorrectly(true, rv1, SECOND_DOG, THIRD_DOG);
		var rv2 = collection.removeDog(SECOND_DOG);
		assertCollectionUpdatedCorrectly(true, rv2, THIRD_DOG);
		var rv3 = collection.removeDog(THIRD_DOG);
		assertCollectionUpdatedCorrectly(true, rv3);
	}

	@Test
	@Order(500)
	@DisplayName("Ta bort alla hundar från samlingen i omvänd insättningsordning")
	public void removingAllDogsInReverseOrder() {
		addAllThreeTestDogsInOrder();
		var rv1 = collection.removeDog(THIRD_DOG);
		assertCollectionUpdatedCorrectly(true, rv1, FIRST_DOG, SECOND_DOG);
		var rv2 = collection.removeDog(SECOND_DOG);
		assertCollectionUpdatedCorrectly(true, rv2, FIRST_DOG);
		var rv3 = collection.removeDog(FIRST_DOG);
		assertCollectionUpdatedCorrectly(true, rv3);
	}

	@Test
	@Order(510)
	@DisplayName("Försök ta bort en hund som inte existerar från en tom samling")
	public void attemptingToRemoveNonexistingDogFromEmptyCollection() {
		var rv = collection.removeDog(OTHER_DOG);
		assertCollectionUpdatedCorrectly(false, rv);
	}

	@Test
	@Order(520)
	@DisplayName("Försök ta bort en hund som inte existerar i samlingen")
	public void attemptingToRemoveNonexistingDog() {
		addAllThreeTestDogsInOrder();
		var rv = collection.removeDog(OTHER_DOG);
		assertCollectionUpdatedCorrectly(false, rv, FIRST_DOG, SECOND_DOG, THIRD_DOG);
	}

	////////////////////////////////////////////
	//
	// Tester för removeDog-metoden som tar ett
	// namn som parameter
	//
	////////////////////////////////////////////

	@Test
	@Order(530)
	@DisplayName("Ta bort den enda hunden i samlingen via namn")
	public void removingOnlyDogByName() {
		collection.addDog(FIRST_DOG);
		var rv = collection.removeDog(FIRST_DOG.getName());
		assertCollectionUpdatedCorrectly(true, rv);
	}

	@Test
	@Order(540)
	@DisplayName("Ta bort den enda hunden i samlingen via namn, nytt strängobjekt för namnet")
	public void removingOnlyDogByNameNewStringObject() {
		collection.addDog(FIRST_DOG);
		var rv = collection.removeDog(new String(FIRST_DOG.getName()));
		assertCollectionUpdatedCorrectly(true, rv);
	}

	@Test
	@Order(550)
	@DisplayName("Ta bort den först tillagda hunden från samlingen via namn")
	public void removingFirstDogByName() {
		addAllThreeTestDogsInOrder();
		var rv = collection.removeDog(FIRST_DOG.getName());
		assertCollectionUpdatedCorrectly(true, rv, SECOND_DOG, THIRD_DOG);
	}

	@Test
	@Order(560)
	@DisplayName("Ta bort den andra tillagda hunden från samlingen via namn")
	public void removingSecondDogByName() {
		addAllThreeTestDogsInOrder();
		var rv = collection.removeDog(SECOND_DOG.getName());
		assertCollectionUpdatedCorrectly(true, rv, FIRST_DOG, THIRD_DOG);
	}

	@Test
	@Order(570)
	@DisplayName("Ta bort den sist tillagda hunden från samlingen via namn")
	public void removingLastDogByName() {
		addAllThreeTestDogsInOrder();
		var rv = collection.removeDog(THIRD_DOG.getName());
		assertCollectionUpdatedCorrectly(true, rv, FIRST_DOG, SECOND_DOG);
	}

	@Test
	@Order(580)
	@DisplayName("Ta bort alla hundar från samlingen i insättningsordning via namn")
	public void removingAllDogsInOrderByName() {
		addAllThreeTestDogsInOrder();
		var rv1 = collection.removeDog(FIRST_DOG.getName());
		assertCollectionUpdatedCorrectly(true, rv1, SECOND_DOG, THIRD_DOG);
		var rv2 = collection.removeDog(SECOND_DOG.getName());
		assertCollectionUpdatedCorrectly(true, rv2, THIRD_DOG);
		var rv3 = collection.removeDog(THIRD_DOG.getName());
		assertCollectionUpdatedCorrectly(true, rv3);
	}

	@Test
	@Order(590)
	@DisplayName("Ta bort alla hundar från samlingen i omvänd insättningsordning via namn")
	public void removingAllDogsInReverseOrderByName() {
		addAllThreeTestDogsInOrder();
		var rv1 = collection.removeDog(THIRD_DOG.getName());
		assertCollectionUpdatedCorrectly(true, rv1, FIRST_DOG, SECOND_DOG);
		var rv2 = collection.removeDog(SECOND_DOG.getName());
		assertCollectionUpdatedCorrectly(true, rv2, FIRST_DOG);
		var rv3 = collection.removeDog(FIRST_DOG.getName());
		assertCollectionUpdatedCorrectly(true, rv3);
	}

	@Test
	@Order(600)
	@DisplayName("Försök ta bort en hund som inte existerar från en tom samling via namn")
	public void attemptingToRemoveNonexistingDogFromEmptyCollectionByName() {
		var rv = collection.removeDog(OTHER_DOG.getName());
		assertCollectionUpdatedCorrectly(false, rv);
	}

	@Test
	@Order(610)
	@DisplayName("Försök ta bort en hund som inte existerar i samlingen via namn")
	public void attemptingToRemoveNonexistingDogByName() {
		addAllThreeTestDogsInOrder();
		var rv = collection.removeDog(OTHER_DOG.getName());
		assertCollectionUpdatedCorrectly(false, rv, FIRST_DOG, SECOND_DOG, THIRD_DOG);
	}

	/**
	 * Denna record (klass) används för att kontrollera att bara de förväntade
	 * metoderna finns i det publika gränssnittet i de klasser du implementerat.
	 * Kopior av MethodHeader finns i de flesta testfalls-filerna. Duplicerad kod är
	 * normalt en dålig idé eftersom det leder till svårigheter med underhåll när
	 * man måste hitta alla ställen koden finns på och applicera ändringar överallt.
	 * <p>
	 * Här är detta acceptabelt eftersom det gör att varje testklass innehåller allt
	 * som behövs för att kunna köra den, och för att alla ändringar automatiskt
	 * läggs in i alla klasser.
	 */
	public record MethodHeader(boolean isStatic, String returnType, java.util.regex.Pattern namePattern,
			String[] parameters) {
	
		public MethodHeader(boolean isStatic, String returnType, String namePattern, String... parameters) {
			this(isStatic, returnType, java.util.regex.Pattern.compile(namePattern), parameters);
		}
	
		public boolean matches(java.lang.reflect.Method m) {
			if (isStatic != java.lang.reflect.Modifier.isStatic(m.getModifiers()))
				return false;
	
			if (!returnType.equals(m.getReturnType().getSimpleName()))
				return false;
	
			if (!namePattern.matcher(m.getName()).matches())
				return false;
	
			var actualParams = java.util.stream.Stream.of(m.getParameterTypes()).map(p -> p.getSimpleName()).toList()
					.toArray(new String[] {});
			if (!java.util.Arrays.equals(parameters, actualParams))
				return false;
	
			return true;
		}
	
		@Override
		public String toString() {
			return "%s%s %s%s".formatted(isStatic ? "static " : "", returnType, namePattern,
					java.util.Arrays.toString(parameters));
		}
	
	}
	
	// @formatter:off
	/**
	 * Denna klass är automatiskt genererad. Ändringar i den kommer att skrivas
	 * över vid nästa uppdatering.
	 */
	public class DogImplementationValidator {
	
		private final Class<?> cut = Dog.class;
		private static final java.util.Collection<MethodHeader> EXPECTED_PUBLIC_METHODS = new java.util.ArrayList<>();
	
		/**
		 * Denna lista är automatiskt genererad från en referens-implementation.
		 * Ordningen på metoderna är fast, men har inget med den förväntade ordningen
		 * i klassen att göra.
		 */
		static {
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "Optional", "getOwner"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "Owner", "getOwner"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "String", "getBreed"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "String", "getName"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "String", "toString"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "boolean", "setOwner", "Owner"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "double", "getTailLength"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "int", ".*Year.*"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "int", ".*Year.*", "int"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "int", ".*[aA]ge.*"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "int", ".*[aA]ge.*", "int"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "int", "getAge"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "int", "getWeight"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "void", ".*Year.*"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "void", ".*Year.*", "int"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "void", ".*[aA]ge.*"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "void", ".*[aA]ge.*", "int"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "void", "setOwner", "Owner"));
		}
	
		public void execute() {
			org.junit.jupiter.api.Assertions.assertAll(this::onlyPrivateFields, this::allConstructorsPublicOrPrivate,
			this::expectedNumberOfPublicConstructors, this::allMethodsPublicOrPrivate, this::onlyExpectedPublicMethods);
		}
	
		private void onlyPrivateFields() {
			for (var f : cut.getDeclaredFields()) {
				org.junit.jupiter.api.Assertions.assertTrue(isPrivate(f),
					"Fältet %s i %s har inte en korrekt skyddsnivå. Endast privat är tillåten.".formatted(f.getName(),
					cut.getSimpleName()));
			}
		}
	
		private void expectedNumberOfPublicConstructors() {
			org.junit.jupiter.api.Assertions.assertEquals(1, cut.getDeclaredConstructors().length,
				"Fel antal publika konstruktorer i klassen %s".formatted(cut.getSimpleName()));
		}
	
		private void allConstructorsPublicOrPrivate() {
			for (var c : cut.getDeclaredConstructors()) {
				org.junit.jupiter.api.Assertions.assertTrue(isPublic(c) || isPrivate(c),
						"Konstruktorn %s i %s har inte en korrekt skyddsnivå. Endast publik eller privat är tillåtna."
								.formatted(c.getName(), cut.getSimpleName()));
			}
		}
	
		private void allMethodsPublicOrPrivate() {
			for (var m : cut.getDeclaredMethods()) {
				org.junit.jupiter.api.Assertions.assertTrue(isPublic(m) || isPrivate(m),
						"Metoden %s i %s har inte en korrekt skyddsnivå. Endast publik eller privat är tillåtna."
								.formatted(m.getName(), cut.getSimpleName()));
			}
		}
	
		public void onlyExpectedPublicMethods() {
			var matches = unexpectedPublicMethods();
			org.junit.jupiter.api.Assertions.assertTrue(matches.isEmpty(),
					"Det finns publika metoder i %s som testen inte förväntar sig:\n   %s\n".formatted(cut.getSimpleName(),
							String.join("\n   ", matches.stream().map(m -> m.toString()).toList())));
		}
	
		private java.util.List<java.lang.reflect.Method> unexpectedPublicMethods() {
			return java.util.stream.Stream.of(cut.getDeclaredMethods())
					.filter(m -> isPublic(m) && !isExpectedPublicMethod(m)).toList();
		}
	
		private boolean isExpectedPublicMethod(java.lang.reflect.Method m) {
			return EXPECTED_PUBLIC_METHODS.stream().anyMatch(mh -> mh.matches(m));
		}
	
		private static boolean isPublic(java.lang.reflect.Member m) {
			return java.lang.reflect.Modifier.isPublic(m.getModifiers());
		}
	
		private static boolean isPrivate(java.lang.reflect.Member m) {
			return java.lang.reflect.Modifier.isPrivate(m.getModifiers());
		}
	
	}
	// @formatter:on

	// @formatter:off
	/**
	 * Denna klass är automatiskt genererad. Ändringar i den kommer att skrivas
	 * över vid nästa uppdatering.
	 */
	public class DogCollectionImplementationValidator {
	
		private final Class<?> cut = DogCollection.class;
		private static final java.util.Collection<MethodHeader> EXPECTED_PUBLIC_METHODS = new java.util.ArrayList<>();
	
		/**
		 * Denna lista är automatiskt genererad från en referens-implementation.
		 * Ordningen på metoderna är fast, men har inget med den förväntade ordningen
		 * i klassen att göra.
		 */
		static {
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "ArrayList", ".*Tail.*", "double"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "ArrayList", "getDogs"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "Dog", "getDog", "String"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "Optional", "getDog", "String"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "boolean", "addDog", "Dog"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "boolean", "containsDog", "Dog"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "boolean", "containsDog", "String"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "boolean", "isEmpty"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "boolean", "removeDog", "Dog"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "boolean", "removeDog", "String"));
			EXPECTED_PUBLIC_METHODS.add(new MethodHeader(false, "int", "size"));
		}
	
		public void execute() {
			org.junit.jupiter.api.Assertions.assertAll(this::onlyPrivateFields, this::allConstructorsPublicOrPrivate,
			this::expectedNumberOfPublicConstructors, this::allMethodsPublicOrPrivate, this::onlyExpectedPublicMethods);
		}
	
		private void onlyPrivateFields() {
			for (var f : cut.getDeclaredFields()) {
				org.junit.jupiter.api.Assertions.assertTrue(isPrivate(f),
					"Fältet %s i %s har inte en korrekt skyddsnivå. Endast privat är tillåten.".formatted(f.getName(),
					cut.getSimpleName()));
			}
		}
	
		private void expectedNumberOfPublicConstructors() {
			org.junit.jupiter.api.Assertions.assertEquals(1, cut.getDeclaredConstructors().length,
				"Fel antal publika konstruktorer i klassen %s".formatted(cut.getSimpleName()));
		}
	
		private void allConstructorsPublicOrPrivate() {
			for (var c : cut.getDeclaredConstructors()) {
				org.junit.jupiter.api.Assertions.assertTrue(isPublic(c) || isPrivate(c),
						"Konstruktorn %s i %s har inte en korrekt skyddsnivå. Endast publik eller privat är tillåtna."
								.formatted(c.getName(), cut.getSimpleName()));
			}
		}
	
		private void allMethodsPublicOrPrivate() {
			for (var m : cut.getDeclaredMethods()) {
				org.junit.jupiter.api.Assertions.assertTrue(isPublic(m) || isPrivate(m),
						"Metoden %s i %s har inte en korrekt skyddsnivå. Endast publik eller privat är tillåtna."
								.formatted(m.getName(), cut.getSimpleName()));
			}
		}
	
		public void onlyExpectedPublicMethods() {
			var matches = unexpectedPublicMethods();
			org.junit.jupiter.api.Assertions.assertTrue(matches.isEmpty(),
					"Det finns publika metoder i %s som testen inte förväntar sig:\n   %s\n".formatted(cut.getSimpleName(),
							String.join("\n   ", matches.stream().map(m -> m.toString()).toList())));
		}
	
		private java.util.List<java.lang.reflect.Method> unexpectedPublicMethods() {
			return java.util.stream.Stream.of(cut.getDeclaredMethods())
					.filter(m -> isPublic(m) && !isExpectedPublicMethod(m)).toList();
		}
	
		private boolean isExpectedPublicMethod(java.lang.reflect.Method m) {
			return EXPECTED_PUBLIC_METHODS.stream().anyMatch(mh -> mh.matches(m));
		}
	
		private static boolean isPublic(java.lang.reflect.Member m) {
			return java.lang.reflect.Modifier.isPublic(m.getModifiers());
		}
	
		private static boolean isPrivate(java.lang.reflect.Member m) {
			return java.lang.reflect.Modifier.isPrivate(m.getModifiers());
		}
	
	}
	// @formatter:on

}
