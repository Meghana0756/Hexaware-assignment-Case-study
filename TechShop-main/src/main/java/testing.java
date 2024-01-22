
public class testing {
	
	private static int storedValue = 20;

    public static void updateAndPrint() {
        storedValue++;

        // Print the updated value
        System.out.println("Updated Value: " + storedValue);
    }
	public static void main(String[] args) {
		
		updateAndPrint();
	}

}
