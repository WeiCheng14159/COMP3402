import java.util.Stack;

public class PostFix {

	public static void main(String[] args) {
		String [] testcase = { "6", "1", "3", "4", "/", "-", "/" };
		PostFix postfix = new PostFix(testcase);
		System.out.println("Postfix " + postfix + " evaluates to "+postfix.evaluate());
	}

	private String[] tokens; 
	public PostFix(String [] tokens) {
		this.tokens = tokens;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(String s: tokens) {
			sb.append(" "+s);
		}
		sb.deleteCharAt(0);
		return sb.toString();
	}
	
	public double evaluate() {
		// Assume that the postfix is always valid
		Stack<Double> stack = new Stack<Double>();
		for(String token: tokens) {
			if(token.equals("+")) {
				stack.push(stack.pop()+stack.pop());	
			} else if(token.equals("-")) {
                // TODO
			} else if(token.equals("*")) {
                // TODO
			} else if(token.equals("/")) {
                // TODO
			} else {
                // TODO
			}
		}
		return stack.pop();
	}
}
