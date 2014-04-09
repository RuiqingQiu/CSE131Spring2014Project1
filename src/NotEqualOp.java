
public class NotEqualOp extends ComparisonOp{

	STO checkOperands(STO a, STO b) {
		Type aType = a.getType();
		Type bType = b.getType();
		
		//The operand must be either both numeric or both bool
		if (aType instanceof NumericType && bType instanceof NumericType) {
			//Calculate the value of two Ints
			return new ExprSTO("NotEqualOp", new BoolType("bool", 1));
		} else if(aType instanceof BoolType && bType instanceof BoolType) {
			return new ExprSTO("NotEqualOp", new BoolType("bool", 1));
		}
		else{
			return new ErrorSTO(Formatter.toString(ErrorMsg.error1b_Expr, aType.getName(), "!=", bType.getName()));
		}
	}

}
