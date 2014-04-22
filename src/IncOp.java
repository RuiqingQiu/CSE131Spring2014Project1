
public class IncOp extends UnaryOp {

	@Override
	STO checkOperands(STO a) {
		Type aType = a.getType();
		if (aType instanceof NumericType) {
			if(a.getIsAddressable() == true && a.getIsModifiable() == true)
				return new ExprSTO("IncOp", aType);
			else
				return new ErrorSTO(Formatter.toString(ErrorMsg.error2_Lval,  "++"));
		} 
		else if(aType.isPointer()){
			//check if aType is a functptr type
			if(aType.isFuncPointer()){
				return new ErrorSTO(Formatter.toString(ErrorMsg.error2_Type, aType.getName(), "++"));
			}
		    return new ExprSTO("IncOp",aType);
		}
		else{
			return new ErrorSTO(Formatter.toString(ErrorMsg.error2_Type, aType.getName(), "++"));
		}
	}

}
