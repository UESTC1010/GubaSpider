package crawl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
	public static void init( int size ){
		tt = Executors.newFixedThreadPool(size);
	}
	
	public static void shutdown(){
		tt.shutdown();
	}
	public static ExecutorService tt = null;
}
