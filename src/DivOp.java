
public class DivOp extends ArithmeticOp{

	@Override
	STO checkOperands(STO a, STO b) {
		Type aType = a.getType();
		Type bType = b.getType();
		//Both operands have to be numeric
		if(!aType.isNumeric())
			return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr,aType.getName(), "/"));
		else if(!bType.isNumeric())
			return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr,bType.getName(), "/"));
		else if (aType.isInt() && bType.isInt()) {
			//Calculate the value of two Ints
			if(a.isConst() && b.isConst()){
				//Check if dividing by 0
				if(((ConstSTO)b).getIntValue() == 0){
					return new ErrorSTO(ErrorMsg.error8_Arithmetic);
				}
				int x = ((ConstSTO)a).getIntValue() / ((ConstSTO)b).getIntValue();
				ConstSTO c = new ConstSTO("", aType);
				c.setValue(x);
				return c;
			}
			return new ExprSTO("DivOp", aType);
		} else {
			if(a.isConst() && b.isConst()){
				//Check if dividing by 0
				if(((ConstSTO)b).getFloatValue() == 0.0){
					return new ErrorSTO(ErrorMsg.error8_Arithmetic);
				}
				float x =  ((ConstSTO)a).getFloatValue() / ((ConstSTO)b).getFloatValue();
				ConstSTO c = new ConstSTO("", new FloatType("float", 4));
				
				c.setValue(x);
				return c;
			}
			return new ExprSTO("DivOp", new FloatType("float", 4));
		}
	}
}
