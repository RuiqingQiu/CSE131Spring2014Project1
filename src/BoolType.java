
public class BoolType extends BasicType {

	public BoolType(String strName, int size) {
		super(strName, size);
		this.setSize(1);
	}
	public boolean isBool(){return true;}

}
