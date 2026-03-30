package project.battle;
import java.util.Deque;
import java.util.LinkedList;

import project.network.Server;
import project.utility.Time;

public interface BattleLog {

	// Currently prints all message to the terminal, soon will print into actual application
	public Deque<String> MESSAGE_QUEUE = new LinkedList<>();
	
	/**
	 * Adds a message to the queue
	 * @param message supplied as a String
	 */
	public static void add(String message) {
		MESSAGE_QUEUE.offer(message);
	}

	/**
	 * Adds a formatted message to the queue
	 * @param message supplied as a String
	 * @param args formatted arguments
	 */
	public static void add(String message, Object... args) {
		MESSAGE_QUEUE.offer(String.format(message, args));
	}

	// Adds a new line to the queue
	public static void addLine() {
		MESSAGE_QUEUE.offer("\n");
	}
	
	/**
	 * Prints out a message, does not add it to the queue
	 * @param message supplied as a String
	 */
	public static void addPrint(String message) {
		System.out.print(message);
	}

	/**
	 * Prints out a formatted message, does not add it to the queue
	 * @param message supplied as a String
	 * @param args formatter arguments
	 */
	public static void addPrint(String message, Object... args) {
		System.out.printf(String.format(message, args));
	}

	/**
	 * Prints out a message line, does not add it to the queue
	 * @param message supplied as a String
	 */
	public static void addPrintln(String message) {
		System.out.println(message);
	}
	
	/**
	 * Prints out all the messages to the terminal in order which they were added.
	 * The queue is cleared.
	 */
	public static void out() {
		while (!MESSAGE_QUEUE.isEmpty()) {
			String message = MESSAGE_QUEUE.poll();
			if (message.equals("")|| message.equals("\n")) 
				Server.broadcast(message);
			else {
				Server.broadcast("%s\n", message);
				Time.hold();
			}
		}
	}

}
