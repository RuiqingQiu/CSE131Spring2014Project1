
public class NotOp extends UnaryOp{

	STO checkOperands(STO a) {
		Type aType = a.getType();
		//Not must have bool operand
		if(aType.isBool()){
			if(a.isConst()){
				ConstSTO c = new ConstSTO("", new BoolType("bool", 4));
				if(!((ConstSTO)a).getBoolValue())
					c.setValue(1.0);
				else
					c.setValue(0.0);
				return c;
			}
			return new ExprSTO("NotOp", aType);
		}
		//Return an error
		else{
			return new ErrorSTO(Formatter.toString(ErrorMsg.error1u_Expr,aType.getName(), "!", "bool"));
		}
	}

}
