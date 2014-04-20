
public abstract class CompositeType extends Type{

	public CompositeType(String strName, int size) {
		super(strName, size);
	}
	
	public Type getElementType(){
		return this.ElementType;
	}
	public void setElementType(Type t){
		this.ElementType = t.clone();
	}
	protected Type ElementType;

}
