
public class EqualOp extends ComparisonOp{

	STO checkOperands(STO a, STO b) {
		Type aType = a.getType();
		Type bType = b.getType();
		
		//The operand must be either both numeric or both bool
		if (aType.isNumeric()&& bType.isNumeric()) {
			if(a instanceof ConstSTO && b instanceof ConstSTO){
				ConstSTO c = new ConstSTO("", new BoolType("bool", 4));
				if(((ConstSTO)a).getFloatValue() == ((ConstSTO)b).getFloatValue())
					c.setValue(1.0);
				else
					c.setValue(0.0);
				return c;
			}
			return new ExprSTO("EqualOp", new BoolType("bool", 4));
         
		} else if(aType.isBool() && bType.isBool()) {
			if(a.isConst() && b.isConst()){
				ConstSTO c = new ConstSTO("", new BoolType("bool", 4));
				if(((ConstSTO)a).getBoolValue() == ((ConstSTO)b).getBoolValue())
					c.setValue(1.0);
				else
					c.setValue(0.0);
				return c;
			}
			return new ExprSTO("EqualOp", new BoolType("bool", 4));

		}
		//Check #17 to support pointer type
		//check if either operand is pointer
		else if(aType.isGeneralPointer() || bType.isGeneralPointer())
		{
			//If both are regular pointer
			if(aType.isNullPointer() && bType.isNullPointer()){
				ConstSTO tmp = new ConstSTO("true", new BoolType("bool", 4));
				tmp.setValue(1.0);
				return tmp;
			}
			else if(aType.isGeneralPointer() && bType.isNullPointer()){
				ExprSTO tmp = new ExprSTO(aType.getName() + " == " +bType.getName(), new BoolType("bool", 4));
				return tmp;
			}
			else if(bType.isGeneralPointer() && aType.isNullPointer()){
				ExprSTO tmp = new ExprSTO(aType.getName() + " == " +bType.getName(), new BoolType("bool", 4));
				return tmp;
			}
			//There won't be a case where a is nullptr
			else if(aType.isGeneralPointer() && bType.isGeneralPointer())
			{
				if(aType.isEquivalentTo(bType)){
					ExprSTO tmp = new ExprSTO(aType.getName() + " == " +bType.getName(), new BoolType("bool", 4));
					return tmp;
				}else{
					return new ErrorSTO(Formatter.toString(ErrorMsg.error17_Expr,"==",aType.getName(), bType.getName()));
				}
			}
			//Error message from check #17, since one is pointer type and the other is not
			else{
				return new ErrorSTO(Formatter.toString(ErrorMsg.error17_Expr,"==",aType.getName(), bType.getName()));
			}
		}
		else{
			return new ErrorSTO(Formatter.toString(ErrorMsg.error1b_Expr, aType.getName(), "==", bType.getName()));
		}
	}

}
