
public class NotEqualOp extends ComparisonOp{
	
	STO checkOperands(STO a, STO b) {
		Type aType = a.getType();
		Type bType = b.getType();
		
		//The operand must be either both numeric or both bool
		if (aType instanceof NumericType && bType instanceof NumericType) {
			if(a instanceof ConstSTO && b instanceof ConstSTO){
				ConstSTO c = new ConstSTO("", new BoolType("bool", 4));
				if(((ConstSTO)a).getFloatValue() != ((ConstSTO)b).getFloatValue())
					c.setValue(1.0);
				else
					c.setValue(0.0);
				return c;
			}
			return new ExprSTO("NotEqualOp", new BoolType("bool", 4));
		} else if(aType instanceof BoolType && bType instanceof BoolType) {
			if(a instanceof ConstSTO && b instanceof ConstSTO){
				ConstSTO c = new ConstSTO("", new BoolType("bool", 4));
				if(((ConstSTO)a).getBoolValue() != ((ConstSTO)b).getBoolValue())
					c.setValue(1.0);
				else
					c.setValue(0.0);
				return c;
			}
			return new ExprSTO("NotEqualOp", new BoolType("bool", 4));
		}
		//Check #17 to support pointer type
		else if(aType.isPointer() || bType.isPointer()){
			if(aType.isPointer() && bType.isPointer()){
				//Check if both are nullptr, if so return ConstSTO 
				if(aType.isNullPointer() && bType.isNullPointer()){
					ConstSTO tmp = new ConstSTO("false", new BoolType("bool", 4));
					tmp.setValue(0.0);
					return tmp;
				}
				//Not both null, do equivalent check
				else{
					//Not both null, do equivalent check
			        //bType still can be nullptr and a still can be nullptr
					if(bType.isNullPointer() || aType.isNullPointer()){
				    	//then it should be false since they are not equal
				        ConstSTO tmp = new ConstSTO("true", new BoolType("bool", 4));
					    tmp.setValue(1.0);
					    return tmp;
				    }
				    //a and b are not nullptr, need to check if basetype equals
				    else{ 
					    //System.out.println("bType base type is " + ((PointerType)bType).getPointerBaseType());
						if(!(((PointerType)aType).isEquivalentTo(((PointerType)bType)))){
							return new ErrorSTO(Formatter.toString(ErrorMsg.error17_Expr,"!=",aType.getName(), bType.getName()));
						}
				    }
					//otherwise it is correct return ExprSTO
					return new ExprSTO("NotEqualOp", new BoolType("bool",4));
				}
			}
			else{
				return new ErrorSTO(Formatter.toString(ErrorMsg.error17_Expr,"!=",aType.getName(), bType.getName()));
			}
		}
		else{
			return new ErrorSTO(Formatter.toString(ErrorMsg.error1b_Expr, aType.getName(), "!=", bType.getName()));
		}
	}

}
