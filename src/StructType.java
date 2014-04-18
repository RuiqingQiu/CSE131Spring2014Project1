import java.util.Vector;


public class StructType extends CompositeType{

	public StructType(String strName, int size) {
		super(strName, size);
		fields = new Vector<STO>();
	}

	@Override
	public boolean isAssignableTo(Type t) {
		return isEquivalentTo(t);
	}

	@Override
	public boolean isEquivalentTo(Type t) {
		if(t.isStruct()){
			//Check strict name equivalence
			if(this.getName().equals(t.getName()))
				return true;
			else
				return false;
		}
		else
			return false;
	}
	public void setField(Vector<STO> v){
		for(STO s : v){
			fields.addElement(s);
		}
	}
	
	public Vector<STO> getField(){
		return this.fields;
	}
	
	public Type clone(){
		StructType t = new StructType(this.getName(), this.getSize());
		t.fields = this.fields;
		return t;
	}
	
	public boolean isStruct(){return true;}
	private Vector<STO> fields;
}
