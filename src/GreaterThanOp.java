
public class GreaterThanOp extends BooleanOp {

	@Override
	STO checkOperands(STO a, STO b) {
		Type aType = a.getType();
		Type bType = b.getType();
		//both operands should be numeric
		if (!(aType.isNumeric()) || !(bType.isNumeric())) {
			// error
			return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr,"bool", ">"));
		}
		else {
			if(a.isConst() && b.isConst()){
				ConstSTO c = new ConstSTO("", new BoolType("bool", 1));
				if(((ConstSTO)a).getFloatValue() > ((ConstSTO)b).getFloatValue())
					c.setValue(1.0);
				else
					c.setValue(0.0);
				return c;
			}
			return new ExprSTO("GreaterThanOp", new BoolType("Bool", 1));
		}
    }
}
