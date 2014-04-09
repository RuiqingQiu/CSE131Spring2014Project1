
public class AndOp extends BooleanOp {

	@Override
	STO checkOperands(STO a, STO b) {
		Type aType = a.getType();
		Type bType = b.getType();
		//Both operands must be bool type
		if (aType instanceof BoolType && bType instanceof BoolType) {
			return new ExprSTO("AndOp", aType);
		}
		else {
			// error
			if(!(aType instanceof BoolType))
				return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr,aType.getName(), "&&", "bool"));
			else
				return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr,bType.getName(), "&&", "bool"));
		} 
	}

}
