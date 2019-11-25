import java.util.HashMap;
import java.util.Map;

public class MyCounter {
	private static Map<String, Integer> counter = new HashMap<String, Integer>();

	public static synchronized void increase(String stmt) {
		if (counter.containsKey(stmt)) {
			Integer count = counter.get(stmt);
			counter.put(stmt, count + 1);
		} else {
			counter.put(stmt, 1);
		}
	}

	public static synchronized void report() {
		System.err.println("counter : ");
		for (Map.Entry<String, Integer> item : counter.entrySet()) {
			System.err.println(item.getKey() + ": " + item.getValue());
		}

	}
}