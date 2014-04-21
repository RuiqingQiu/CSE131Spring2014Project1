
public class BoolType extends BasicType {

	public BoolType(String strName, int size) {
		super(strName, size);
		this.setSize(4);
	}
	public boolean isBool(){return true;}
	@Override
	public boolean isAssignableTo(Type t) {
		if (t.isBool())
			return true;
		return false;
	}
	@Override
	public boolean isEquivalentTo(Type t) {
		if (t.isBool())
			return true;
		return false;
	}
	
	public Type clone(){
		return new BoolType(this.getName(), this.getSize());
	}

}
