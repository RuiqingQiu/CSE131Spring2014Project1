
public class BwAndOp extends BitwiseOp{

	STO checkOperands(STO a, STO b) {
		Type aType = a.getType();
		Type bType = b.getType();
		//Both operands must be bool type
		if (aType instanceof IntType && bType instanceof IntType) {
			if(a instanceof ConstSTO && b instanceof ConstSTO){
				int x = ((ConstSTO)a).getIntValue() & ((ConstSTO)b).getIntValue();
				ConstSTO c = new ConstSTO("",aType);
				c.setValue(x);
				return c;
			}
			return new ExprSTO("BwAndOp", aType);
		}
		else {
			// error
			if(!(aType.isInt()))
				return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr,aType.getName(), "&", "int"));
			else
				return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr,bType.getName(), "&", "int"));
		} 
	}

}
