
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
	
	public String getPrintedName(){
		
		if(this.getElementType().isPointer()){
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
}
