
public class ArrayType extends CompositeType{

	public ArrayType(String strName, int size) {
		super(strName, size);
	}

	@Override
	public boolean isAssignableTo(Type t) {
		if(t.isPointer())
			//Check if the pointer type is the same as the array type
			if(((PointerType)t).getElementType().isEquivalentTo(this.getElementType()))
				return true;
			else
				return false;
		else	
			return false;
	}

	@Override
	public boolean isEquivalentTo(Type t) {
		return false;
	}

	//Override the getName method for ArrayType
	public String getName ()
	{
		return this.getElementType().getName() + "[" + this.ArraySize + "]";
	}
	
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
