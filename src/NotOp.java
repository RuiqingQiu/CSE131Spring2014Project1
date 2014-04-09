
public class NotOp extends UnaryOp{

	STO checkOperands(STO a) {
		Type aType = a.getType();
		//Not must have bool operand
		if(aType instanceof BoolType){
			return new ExprSTO("NotOp", aType);
		}
		//Return an error
		else{
			return new ErrorSTO(Formatter.toString(ErrorMsg.error1u_Expr,aType.getName(), "!", "bool"));
		}
	}

}
