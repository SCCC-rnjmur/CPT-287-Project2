package project2;

import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class InFixParser {

	public static void main(String[] args) {
		
		// Create Scanner for console input from user
		Scanner console = new Scanner(System.in);
		
		// Get input from user
		System.out.print("Enter input to calculate:  ");
		String input = console.nextLine();
		
		// Return result to console
		// print divide by 0 error if needed
		try {
			System.out.println("Result: " + evaluateInfix(input));
		} catch (ArithmeticException ae) {
			System.out.println(ae.getMessage() + " error");
		}
		
		// Close Scanner
		console.close();
	}
	
	public static int evaluateInfix(String infix) {
		/**
		 * Evaluate infix String
		 * 
		 * @param: String	infix to parse
		 * @return: int		calculated return value
		 */
		
		// Create Stack for storing infix operators
		Stack<String> infixOpersStack = new Stack<String>();
		
		// Create Stack for storing infix Digits
		Stack<String> infixDigitsStack = new Stack<String>();
		
		// Create TreeMap with precedences
		TreeMap<String, Integer> precedence = new TreeMap<String, Integer>();
		precedence = setPrecedenceMap(precedence);
		
		// String to store digits
		String digits = "";
		
		// String to store operators
		String opers = "";
		
		// String to store infix after stripping space
		String infix_trim_space = "";
		
		// Remove spaces if present to normalize expression
		if (infix.contains(" ")) {
			String[] infix_split_space = infix.split("\\s+");
			for (int i=0; i < infix_split_space.length; ++i) {
				infix_trim_space = infix_trim_space.concat(infix_split_space[i]);
			}
		} else {
			infix_trim_space = infix;
		}
		
		// Loop through the infix String
		for (int i=0; i < infix_trim_space.length(); ++i) {
			// if character is a digit
			if (Pattern.matches("\\d", infix_trim_space.subSequence(i, i+1))) {
				if (!opers.equals("")) {
					if (!infixOpersStack.isEmpty()) {
						while ((precedence.get(opers) < precedence.get(infixOpersStack.peek())) &&
								precedence.get(opers) != 0) {
							String doOper = infixOpersStack.pop();
							int value = doCalculation(doOper, infixDigitsStack.pop(), 
									infixDigitsStack.pop());
							infixDigitsStack.push(String.valueOf(value));
							if (infixOpersStack.isEmpty()) { break; }
						}
					}
					infixOpersStack.push(opers);
					opers = "";
				}
				
				digits = digits.concat(String.valueOf(infix_trim_space.charAt(i)));
				
				if (i == infix_trim_space.length() - 1) {
					infixDigitsStack.push(digits);
					while (!infixOpersStack.isEmpty()) {
						String doOper = infixOpersStack.pop();
						int value = doCalculation(doOper, infixDigitsStack.pop(), 
								infixDigitsStack.pop());
						infixDigitsStack.push(String.valueOf(value));
					}
				}
			} else {   // If character is not a digit it is an operator
				if (!digits.equals("")) {
					infixDigitsStack.push(digits);
					digits = "";
				}
				if (String.valueOf(infix_trim_space.charAt(i)).equals(")")) {
					String doOper = infixOpersStack.pop();
					int value = 0;
					while (!doOper.equals("(")) {
						value = doCalculation(doOper, infixDigitsStack.pop(), 
								infixDigitsStack.pop());
						if (infixOpersStack.isEmpty()) { break; }
						doOper = infixOpersStack.pop();
						infixDigitsStack.push(String.valueOf(value));
					}
				} 
				else if (String.valueOf(infix_trim_space.charAt(i)).equals("(")) {
					if (opers != "") {
						infixOpersStack.push(opers);
						opers = String.valueOf(infix_trim_space.charAt(i));
					} else { infixOpersStack.push(String.valueOf(infix_trim_space.charAt(i))); }
				}
				else {
					opers = opers.concat(String.valueOf(infix_trim_space.charAt(i)));
				}
				if (i == infix_trim_space.length() - 1) {
					while (!infixOpersStack.isEmpty()) {
						String doOper = infixOpersStack.pop();
						int value = doCalculation(doOper, infixDigitsStack.pop(), 
								infixDigitsStack.pop());
						infixDigitsStack.push(String.valueOf(value));
					}
				}
			}
		}
		
		return Integer.valueOf(infixDigitsStack.pop());
	}
	
	public static TreeMap<String, Integer> setPrecedenceMap(TreeMap<String, Integer> prec) {
		/**
		 * Set values in TreeMap for precedence
		 * 
		 * @param: TreeMap		TreeMap to contain precedences
		 * @return: TreeMap		Returned TreeMap
		 */
		// Put precedence values in TreeMap
		prec.put("(", 0);
		prec.put("||", 1);
		prec.put("&&", 2);
		prec.put("==", 3);
		prec.put("!=", 3);
		prec.put(">", 4);
		prec.put(">=", 4);
		prec.put("<", 4);
		prec.put("<=", 4);
		prec.put("+", 5);
		prec.put("-", 5);
		prec.put("*", 6);
		prec.put("/", 6);
		prec.put("%", 6);
		prec.put("^", 7);
		
		return prec;
	}
	
	public static int doCalculation(String oper, String digit2, String digit1) {
		/**
		 * Do calculation operations
		 * 
		 * @param: String	the operator for calculation
		 * @param: String	digit for one side of the equation
		 * @param: String	digit for the other side of the equation
		 * @return: int		return value of the calculation
		 */
		// Do switch case on operator to perform proper calculation
		switch (oper)
		{
		case "||":
			if (Integer.valueOf(digit1) == 1 || Integer.valueOf(digit2) == 1) {
				return 1;
			} else { return 0; }
		case "&&":
			if (Integer.valueOf(digit1) == 1 && Integer.valueOf(digit2) == 1) {
				return 1;
			} else { return 0; }
		case "==":
			if (Integer.valueOf(digit1) == Integer.valueOf(digit2)) {
				return 1;
			} else { return 0; }
		case "!=":
			if (Integer.valueOf(digit1) != Integer.valueOf(digit2)) {
				return 1;
			} else { return 0; }
		case ">":
			if (Integer.valueOf(digit1) > Integer.valueOf(digit2)) {
				return 1;
			} else { return 0; }
		case ">=":
			if (Integer.valueOf(digit1) >= Integer.valueOf(digit2)) {
				return 1;
			} else { return 0; }
		case "<":
			if (Integer.valueOf(digit1) < Integer.valueOf(digit2)) {
				return 1;
			} else { return 0; }
		case "<=":
			if (Integer.valueOf(digit1) <= Integer.valueOf(digit2)) {
				return 1;
			} else { return 0; }
		case "+":
			return (Integer.valueOf(digit1) + Integer.valueOf(digit2));
		case "-":
			return (Integer.valueOf(digit1) - Integer.valueOf(digit2));
		case "*":
			return (Integer.valueOf(digit1) * Integer.valueOf(digit2));
		case "/":
			return (Integer.valueOf(digit1) / Integer.valueOf(digit2));
		case "%":
			return (Integer.valueOf(digit1) % Integer.valueOf(digit2));
		case "^":
			return (int)Math.pow(Integer.valueOf(digit1), Integer.valueOf(digit2));
		}
		return 0;
	}
}