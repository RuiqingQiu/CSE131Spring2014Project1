
public class VoidType extends Type {

	public VoidType(String strName, int size) {
		super("void", 4);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isAssignableTo(Type t) {
		return false;
	}

	@Override
	public boolean isEquivalentTo(Type t) {
		return false;
	}

}
