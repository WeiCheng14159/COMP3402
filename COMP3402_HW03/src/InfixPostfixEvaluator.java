import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Class to evaluate infix and postfix expressions.
 * 
 * @author Paul E. Davis (feedback@willcode4beer.com)
 */
public class InfixPostfixEvaluator {

//		public static void main(String [] args){
//			try {
//				System.out.println( "Number: " + new InfixPostfixEvaluator().evalInfix("10+A-Q") );
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	
		private String number10Converter(String s){
			return s.replaceAll("10", "I");
		}
		
        /**
         * Operators in reverse order of precedence.
         */
        private static final String operators = "-+/*";
        private static final String operands = "0123456789AJQKI";

        public double evalInfix(String infix) throws Exception {
        	infix = number10Converter(infix);
                return evaluatePostfix(convert2Postfix(infix));
        }

        public String convert2Postfix(String infixExpr) {
                char[] chars = infixExpr.toCharArray();
                Stack<Character> stack = new Stack<Character>();
                StringBuilder out = new StringBuilder(infixExpr.length());

                for (char c : chars) {
                        if (isOperator(c)) {
                                while (!stack.isEmpty() && stack.peek() != '(') {
                                        if (operatorGreaterOrEqual(stack.peek(), c)) {
                                                out.append(stack.pop());
                                        } else {
                                                break;
                                        }
                                }
                                stack.push(c);
                        } else if (c == '(') {
                                stack.push(c);
                        } else if (c == ')') {
                                while (!stack.isEmpty() && stack.peek() != '(') {
                                        out.append(stack.pop());
                                }
                                if (!stack.isEmpty()) {
                                        stack.pop();
                                }
                        } else if (isOperand(c)) {
                                out.append(c);
                        }
                }
                while (!stack.empty()) {
                        out.append(stack.pop());
                }
                return out.toString();
        }

        public double evaluatePostfix(String postfixExpr) throws Exception {
        		postfixExpr = number10Converter(postfixExpr);
                char[] chars = postfixExpr.toCharArray();
                Stack<Double> stack = new Stack<Double>();
                for (char c : chars) {
                        if (isOperand(c)) {
                        	if(c == 'A'){
                        		stack.push(1.0); // convert char to int val
                        	}else if( c == 'I'){
                        		stack.push(10.0); // convert char to int val
                        	}else if( c == 'J'){
                        		stack.push(11.0); // convert char to int val
                        	}else if( c == 'Q'){
                        		stack.push(12.0); // convert char to int val
                        	}else if( c == 'K'){
                        		stack.push(13.0); // convert char to int val
                        	}else{
                        		stack.push((double)(c - '0')); // convert char to int val
                        	}
                        } else if (isOperator(c)) {
                        		double op1 = 0;
                        		double op2 = 0;
                        		try{
                        			op1 = stack.pop();
                                    op2 = stack.pop();
                        		}catch(EmptyStackException e){
                        			e.printStackTrace();
                        			throw new Exception("Format Wrong");
                        		}
                                double result;
                                switch (c) {
                                case '*':
                                        result = op1 * op2;
                                        stack.push(result);
                                        break;
                                case '/':
                                        result = op2 / op1;
                                        stack.push(result);
                                        break;
                                case '+':
                                        result = op1 + op2;
                                        stack.push(result);
                                        break;
                                case '-':
                                        result = op2 - op1;
                                        stack.push(result);
                                        break;
                                }
                        }
                }
                return stack.pop();
        }
        private int getPrecedence(char operator) {
                int ret = 0;
                if (operator == '-' || operator == '+') {
                        ret = 1;
                } else if (operator == '*' || operator == '/') {
                        ret = 2;
                }
                return ret;
        }
        private boolean operatorGreaterOrEqual(char op1, char op2) {
                return getPrecedence(op1) >= getPrecedence(op2);
        }

        private boolean isOperator(char val) {
                return operators.indexOf(val) >= 0;
        }

        private boolean isOperand(char val) {
                return operands.indexOf(val) >= 0;
        }

}