
public class LessThanOp extends ComparisonOp{

	@Override
	STO checkOperands(STO a, STO b) {
		Type aType = a.getType();
		Type bType = b.getType();
		if (!(aType instanceof NumericType) || !(bType  instanceof NumericType)) {
			// error
			return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr,"bool", "<"));
		}
		else {
			return new ExprSTO("LessThanOp", new BoolType("Bool", 1));
		}
	}

}
