
public class NotEqualOp extends ComparisonOp{
	
	STO checkOperands(STO a, STO b) {
		Type aType = a.getType();
		Type bType = b.getType();
		
		//The operand must be either both numeric or both bool
		if (aType instanceof NumericType && bType instanceof NumericType) {
			if(a instanceof ConstSTO && b instanceof ConstSTO){
				ConstSTO c = new ConstSTO("", new BoolType("bool", 1));
				if(((ConstSTO)a).getFloatValue() != ((ConstSTO)b).getFloatValue())
					c.setValue(1.0);
				else
					c.setValue(0.0);
				return c;
			}
			return new ExprSTO("NotEqualOp", new BoolType("bool", 1));
		} else if(aType instanceof BoolType && bType instanceof BoolType) {
			if(a instanceof ConstSTO && b instanceof ConstSTO){
				ConstSTO c = new ConstSTO("", new BoolType("bool", 1));
				if(((ConstSTO)a).getBoolValue() != ((ConstSTO)b).getBoolValue())
					c.setValue(1.0);
				else
					c.setValue(0.0);
				return c;
			}
			return new ExprSTO("NotEqualOp", new BoolType("bool", 1));
		}
		else{
			return new ErrorSTO(Formatter.toString(ErrorMsg.error1b_Expr, aType.getName(), "!=", bType.getName()));
		}
	}

}
