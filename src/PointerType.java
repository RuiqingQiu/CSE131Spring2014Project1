
public class PointerType extends PointerGroupType {

	public PointerType(String strName, int size) {
		super(strName, size);
		// TODO Auto-generated constructor stub
	}
	public Type clone(){
		PointerType t = new PointerType(this.getName(), this.getSize());
		t.ElementType = this.getElementType();
		return t;
	}
	public boolean isPointer(){
		return true;
	}
	
	@Override
	public boolean isAssignableTo(Type t) {
		if(t.isPointer()){
			if(t.isNullPointer())
				return false;
			//Check if the pointer type is the same as the array type
			return this.getElementType().isEquivalentTo((((PointerType)t).getElementType()));
		}
		else	
			return false;
	}
	
	public boolean isEquivalentTo(Type t){
		if(t.isPointer()){
			if(t.isNullPointer())
				return false;
			//Check if the pointer type is the same as the array type
			return this.getElementType().isEquivalentTo(((PointerType)t).getElementType());
		}
		else	
			return false;
	}
	
	public String getPrintedName(){
		if(this.getElementType().isPointer()){
			if(!this.getElementType().getName().equals("pointer")){
				return this.getElementType().getName();
			}
			return ((PointerType)this.getElementType()).getPrintedName() + "*";
		}
		else{
			return this.getElementType().getName();
		}
	}
	public void setElementType(Type t){
		if(this.getElementType() == null){
			this.ElementType = t.clone();
		}
		else{
			if(this.getElementType().isPointer()){
				((PointerType)this.getElementType()).setElementType(t);
			}
			else{
				this.ElementType = t.clone();
			}
		}
	}
	public Type getPointerBaseType(){
		if(this.getElementType().isPointer()){
			return ((PointerType)this.getElementType()).getPointerBaseType();
		}
		else{
			return this.ElementType;
		}
	}
}
