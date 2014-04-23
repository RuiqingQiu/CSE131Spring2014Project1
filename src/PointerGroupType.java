
public class PointerGroupType extends CompositeType{

	public PointerGroupType(String strName, int size) {
		super(strName, size);
		// TODO Auto-generated constructor stub
	}

	public boolean isGeneralPointer(){
		return true;
	}
	@Override
	public boolean isAssignableTo(Type t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEquivalentTo(Type t) {
		// TODO Auto-generated method stub
		return false;
	}

}
