
public class ArrayType extends CompositeType{

	public ArrayType(String strName, int size) {
		super(strName, size);
	}

	@Override
	public boolean isAssignableTo(Type t) {

		if(t.isPointer()){
			//Check if the pointer type is the same as the array type
			if(((PointerType)t).getElementType().isEquivalentTo(this.getElementType())){
				return true;
			}
			else
				return false;
		}
		else	
			return false;
	}

	@Override
	public boolean isEquivalentTo(Type t) {
		if(t.isArray()){
			//It's an array and element type is the same, return true
			if(this.getElementType().isEquivalentTo(((ArrayType)t).getElementType())){
				if(this.getArraySize() == ((ArrayType)t).getArraySize())
					return true;
				else
					return false;
			}

			//If the element type is not the same
			else
				return false;
		}else{
			return false;
		}
	}

	/*
	 * How many elements does it contain
	 */
	public int getArraySize(){
		return this.ArraySize;
	}
	public void setArraySize(int size){
		this.ArraySize = size;
	}
	
	public Type clone(){
		ArrayType t = new ArrayType(this.getName(), this.getSize());
		t.setArraySize(this.getArraySize());
		t.setElementType(this.getElementType());
		
		return t;
	}
	
	public boolean isArray(){
		return true;
	}
	private int ArraySize;
}
