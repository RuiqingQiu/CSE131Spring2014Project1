
public class ModOp extends ArithmeticOp{

	@Override
	STO checkOperands(STO a, STO b) {
		Type aType = a.getType();
		Type bType= b.getType();
		
		//Expected both of equivalent to int
		if (!((aType instanceof IntType) && (bType  instanceof IntType))) {
			// error
			if(!(aType instanceof IntType))
				return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr,aType.getName(), "%","int"));
			else
				return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr,bType.getName(), "%","int"));
		} else{
			if(a instanceof ConstSTO && b instanceof ConstSTO){
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
