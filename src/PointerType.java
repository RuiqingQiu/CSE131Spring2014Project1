
public class PointerType extends PointerGroupType {

	public PointerType(String strName, int size) {
		super(strName, size);
		// TODO Auto-generated constructor stub
	}
	public Type clone(){
		return new PointerType(this.getName(), this.getSize());
	}
	public boolean isPointer(){
		return true;
	}
	public String getName ()
	{
		return this.getElementType().getName() + "*";
	}
}
