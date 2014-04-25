
public class NullPointerType extends PointerType {

	public NullPointerType(String strName, int size) {
		super("nullptr", 4);
	}
	
	public boolean isAssignableTo(Type t){
		if(t.isGeneralPointer()){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isEquivalentTo(Type t){
		if(t.isGeneralPointer()){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean isNullPointer() {return true;}
}
