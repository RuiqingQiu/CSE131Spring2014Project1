
public class AndOp extends BooleanOp {

	@Override
	STO checkOperands(STO a, STO b) {
		Type aType = a.getType();
		Type bType = b.getType();
		//Both operands must be bool type
		if (aType instanceof BoolType && bType instanceof BoolType) {
			if(a instanceof ConstSTO && b instanceof ConstSTO){
				ConstSTO c = new ConstSTO("", new BoolType("bool", 4));
				if(((ConstSTO)a).getBoolValue() && ((ConstSTO)b).getBoolValue())
					c.setValue(1.0);
				else
					c.setValue(0.0);
				return c;
			}
			return new ExprSTO("AndOp", aType);
		}
		else {
			// error
			if(!(aType.isBool()))
				return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr,aType.getName(), "&&", "bool"));
			else
				return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr,bType.getName(), "&&", "bool"));
		} 
	}

}
