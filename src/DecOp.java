
public class DecOp extends UnaryOp{
	@Override
	STO checkOperands(STO a) {
		Type aType = a.getType();
		//Check operand is numeric type
		if (aType.isNumeric()) {
			//get if the operand is a modifiable L-value
			if(a.getIsAddressable() == true && a.getIsModifiable() == true)
				return new ExprSTO("DecOp", aType);
			else
				return new ErrorSTO(Formatter.toString(ErrorMsg.error2_Lval,  "--"));		
		}
		else if(aType.isPointer()){
			if(!(a.isModLValue())){
				return new ErrorSTO(Formatter.toString(ErrorMsg.error2_Lval,  "--"));
			}
			//check if aType is a funcptr type
			if(aType.isFuncPointer()){
				return new ErrorSTO(Formatter.toString(ErrorMsg.error2_Type, aType.getName(), "--"));
			}
			return new ExprSTO("DecOp",aType);
		}
		else{
			return new ErrorSTO(Formatter.toString(ErrorMsg.error2_Type, aType.getName(), "--"));
		}
	}
}
