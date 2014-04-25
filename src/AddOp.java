

public class AddOp extends ArithmeticOp  {

	@Override
	STO checkOperands(STO a, STO b) {
		Type aType = a.getType();
		Type bType = b.getType();
		
		if(!aType.isNumeric())
			return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr,aType.getName(), "+"));
		else if(!bType.isNumeric())
			return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr,bType.getName(), "+"));
		else if (aType instanceof IntType && bType instanceof IntType) {
			if(a instanceof ConstSTO && b instanceof ConstSTO){
				int x = ((ConstSTO)a).getIntValue() + ((ConstSTO)b).getIntValue();
				ConstSTO c = new ConstSTO("", aType);
				c.setValue(x);
				return c;
			}
			//Calculate the value of two Ints
			return new ExprSTO("AddOp", aType);
		} else {
			if(a instanceof ConstSTO && b instanceof ConstSTO){
				float x =  ((ConstSTO)a).getFloatValue() + ((ConstSTO)b).getFloatValue();
				ConstSTO c = new ConstSTO("", aType);
				
				c.setValue(x);
				return c;
			}
			return new ExprSTO("AddOp", new FloatType("float", 4));
		}
	}
}
