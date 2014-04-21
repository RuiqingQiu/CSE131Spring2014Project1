
public class FloatType extends NumericType{

	public FloatType(String strName, int size) {
		super(strName, size);
		this.setSize(4);
	}
	public boolean isFloat(){return true;}
	@Override
	public boolean isAssignableTo(Type t) {
		if(t.isFloat())
			return true;
		return false;
	}
	@Override
	public boolean isEquivalentTo(Type t) {
		if(t.isFloat())
			return true;
		return false;
	}

	public Type clone(){
		return new FloatType(this.getName(), this.getSize());
	}
}
