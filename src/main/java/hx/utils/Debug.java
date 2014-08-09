package hx.utils;

public class Debug {

	public static void log(int...thing){
		StringBuilder sb = new StringBuilder();
		for(int i : thing)sb.append(i + ", ");
		System.err.println(sb.toString());
	}

	public static void log(String string) {
		System.err.println(string);
	}
	
}
