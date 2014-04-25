
public class ErrorType extends Type{

	public ErrorType(String strName, int size) {
		super("ERROR", 4);
	}

	@Override
	public boolean isAssignableTo(Type t) {
		return false;
	}

	@Override
	public boolean isEquivalentTo(Type t) {
		return false;
	}
	public boolean isError(){
		return true;
	}

}
