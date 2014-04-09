
public class ModOp extends ArithmeticOp{

	@Override
	STO checkOperands(STO a, STO b) {
		Type aType = a.getType();
		Type bType= b.getType();
		
		//Expected both of equivalent to int
		if (!(aType instanceof IntType) || !(bType  instanceof IntType)) {
			// error
			return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr,"bool", "%","int"));
		} else
		    //Calculate the value of two Ints
			return new ExprSTO("ModOp", aType);
		}
}
