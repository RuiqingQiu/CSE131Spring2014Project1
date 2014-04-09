

public class AddOp extends ArithmeticOp  {

	@Override
	STO checkOperands(STO a, STO b) {
		Type aType = a.getType();
		Type bType = b.getType();
		if (!(aType instanceof NumericType) || !(bType  instanceof NumericType)) {
			// error
			return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr,"bool", "+"));
		} else if (aType instanceof IntType && bType instanceof IntType) {
			//Calculate the value of two Ints
			return new ExprSTO("AddOp", aType);
		} else {
			return new ExprSTO("AddOp", new FloatType("Float", 32));
		}
	}
}
