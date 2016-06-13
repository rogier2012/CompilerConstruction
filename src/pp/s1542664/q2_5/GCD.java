package pp.s1542664.q2_5;

import java.util.Scanner;

public class GCD {
	public static int count;
	
	public static void main(String[] args) {
		int num1 = in("Number 1? ");
		int num2 = in("Number 2? ");
		out("Greatest Common Divisor: ", gcd(num1, num2));
		out("Call count: ", count);
	}

	public static int gcd(int a1, int a2) {
		int result;
		if (a1 == a2) {
			result = a1;
		} else if (a1 > a2) {
			result = gcd(a1 - a2, a2);
		} else {
			result = gcd(a2 - a1, a1);
		}
		count = count + 1;
		return result;
	}

	private static final Scanner scan = new Scanner(System.in);

	public static int in(String message) {
		System.out.print(message);
		return scan.nextInt();
	}

	public static void out(String message, int val) {
		System.out.println(message + val);
	}
}
