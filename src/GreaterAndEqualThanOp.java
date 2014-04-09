
public class GreaterAndEqualThanOp extends BooleanOp {

	@Override
	STO checkOperands(STO a, STO b) {
		Type aType = a.getType();
		Type bType = b.getType();
		//both operands should be numeric
		if (!(aType instanceof NumericType) || !(bType  instanceof NumericType)) {
			// error
			return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr,"bool", ">="));
		}
		else {
			return new ExprSTO("GreaterAndEqualThanOp", new BoolType("Bool", 1));
		}
	}
}
