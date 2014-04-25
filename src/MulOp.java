
public class MulOp extends ArithmeticOp{
    @Override
	STO checkOperands(STO a, STO b) {
		Type aType = a.getType();
		Type bType = b.getType();
		if (!(aType.isNumeric()) || !(bType.isNumeric())) {
			// error
			return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr,"bool", "*"));
		} else if (aType.isInt() && bType.isInt()) {
			if(a.isConst() && b.isConst()){
				int x = ((ConstSTO)a).getIntValue() * ((ConstSTO)b).getIntValue();
				ConstSTO c = new ConstSTO("", aType);
				c.setValue(x);
				return c;
			}
			//Calculate the value of two Ints
			return new ExprSTO("MulOp", aType);
		} else {
			if(a.isConst() && b.isConst()){
				float x =  ((ConstSTO)a).getFloatValue() * ((ConstSTO)b).getFloatValue();
				ConstSTO c = new ConstSTO("", aType);
				
				c.setValue(x);
				return c;
			}
			return new ExprSTO("MulOp", new FloatType("Float", 4));
		}
	}
}
