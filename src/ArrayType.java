
public class ArrayType extends CompositeType{

	public ArrayType(String strName, int size) {
		super(strName, size);
	}

	@Override
	public boolean isAssignableTo(Type t) {
		if(t.isPointer())
			return true;
		else
			return false;
	}

	@Override
	public boolean isEquivalentTo(Type t) {
		return false;
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
