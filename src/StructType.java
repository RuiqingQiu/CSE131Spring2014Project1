import java.util.Vector;


public class StructType extends CompositeType{

	public StructType(String strName, int size) {
		super(strName, size);
		this.id = strName;
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
			if(this.id.equals(((StructType)t).id))
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
	
	public int getStructSize(){
		int total = 0;
		for(STO s : fields){
			total += s.getType().getSize();
			
		}
		return total;
	}
	
	public Vector<STO> getField(){
		return this.fields;
	}
	
	public Type clone(){
		StructType t = new StructType(this.getName(), this.getSize());
		t.fields = this.fields;
		t.id = this.id;
		return t;
	}
	
	public boolean isStruct(){return true;}
	private Vector<STO> fields;
	private String id;
}
