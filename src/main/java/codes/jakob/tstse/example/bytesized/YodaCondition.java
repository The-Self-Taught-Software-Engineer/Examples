package codes.jakob.tstse.example.bytesized;

@SuppressWarnings("ConstantConditions")
public class YodaCondition {
    public static void main(String[] args) {
        String starWarsCharacter = null;

        if ("Yoda".equals(starWarsCharacter)) {
            System.out.println("Correct!");
        } else {
            System.out.println("Incorrect!");
        }
    }
}
