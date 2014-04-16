
public class ArrayType extends CompositeType{

	public ArrayType(String strName, int size) {
		super(strName, size);
	}

	@Override
	public boolean isAssignableTo(Type t) {
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
	private int ArraySize;
	
}
