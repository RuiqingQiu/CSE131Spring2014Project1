
public class FloatType extends NumericType{

	public FloatType(String strName, int size) {
		super(strName, size);
		this.setSize(32);
	}
	public boolean isFloat(){return true;}

}
