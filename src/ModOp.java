
public class ModOp extends ArithmeticOp{

	@Override
	STO checkOperands(STO a, STO b) {
		Type aType = a.getType();
		Type bType= b.getType();
		
		//Expected both of equivalent to int
		if (!((aType.isInt()) && (bType.isInt()))) {
			// error
			if(!(aType.isInt()))
				return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr,aType.getName(), "%","int"));
			else
				return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr,bType.getName(), "%","int"));
		} else{
			if(a.isConst() && b.isConst()){
				if(((ConstSTO)b).getIntValue() == 0){
					return new ErrorSTO(ErrorMsg.error8_Arithmetic);
				}
				ConstSTO c = new ConstSTO("", aType);
				c.setValue(((ConstSTO)a).getIntValue() % ((ConstSTO)b).getIntValue());
				return c;	
			}
			else{
				return new ExprSTO("ModOp",aType);
			}
		}
	}
}
