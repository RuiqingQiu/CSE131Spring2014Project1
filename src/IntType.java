
public class IntType extends NumericType{

	public IntType(String strName, int size) {
		super(strName, size);
		//Int 4 bytes
		this.setSize(4);
	}
	public boolean isInt(){return true;}
	@Override
	public boolean isAssignableTo(Type t) {
		if(t.isInt() || t.isFloat())
			return true;
		else
			return false;
	}
	@Override
	public boolean isEquivalentTo(Type t) {
		if(t.isInt())
			return true;
		else
			return false;
	}
	public Type clone(){
		return new IntType(this.getName(), this.getSize());
	}
}
