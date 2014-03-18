package crawl;

import java.util.LinkedList;

public class UnVisitedQueue {
	private static LinkedList queue = new LinkedList();
	//addurl
	public static void enQueue(Object t){
		queue.addLast(t);
	}
	//removeurl
	public static Object removeQueue(){
		return queue.removeFirst();
	}
	public static void enfirstqueue(Object t){
		queue.addFirst(t);
	}
	//isempty
	public static boolean isEmpty(){
		return queue.isEmpty();
	}
	//iscontain
	public static boolean contains(Object t){
		return queue.contains(t);
	}
	
	public static int getSize(){
		return queue.size();
	}
}
