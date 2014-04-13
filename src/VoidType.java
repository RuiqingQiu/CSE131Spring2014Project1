
public class VoidType extends Type {

	public VoidType(String strName, int size) {
		super("void", 4);
		// TODO Auto-generated constructor stub
	}
	public boolean isVoid(){return true;}
	@Override
	public boolean isAssignableTo(Type t) {
		if(t.isVoid())
			return true;
		return false;
	}

	@Override
	public boolean isEquivalentTo(Type t) {
		if(t.isVoid())
			return true;
		return false;
	}

}
