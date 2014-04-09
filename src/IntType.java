
public class IntType extends NumericType{

	public IntType(String strName, int size) {
		super(strName, size);
		//Int 32 bits
		this.setSize(32);
	}
	public boolean isInt(){return true;}
}
