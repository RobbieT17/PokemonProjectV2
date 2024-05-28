package battle;
import java.util.Deque;
import java.util.LinkedList;

public abstract class BattleLog {

	// Currently prints all message to the terminal, soon will print into actual application
	private static final Deque<String> MESSAGE_LIST = new LinkedList<>();
	
	/**
	 * Adds a message to the queue
	 * @param message supplied as a String
	 */
	public static void add(String message) {
		MESSAGE_LIST.offer(message);
	}
	
	/**
	 * Prints out a message, does not add it to the queue
	 * @param message supplied as a String
	 */
	public static void addPrint(String message) {
		System.out.println(message);
	}
	
	/**
	 * Prints out all the messages to the terminal in order which they were added to
	 * The queue is cleared
	 */
	public static void out() {
		while (!MESSAGE_LIST.isEmpty()) {
			String message = MESSAGE_LIST.poll();
			if (message.equals("")|| message.equals("\n")) 
				System.out.print(message);
			else {
				System.out.println(message);
				Time.hold();
			}
		}
	}

}
