import java.util.Vector;


public class FunctionPointerType extends PointerGroupType {

	public FunctionPointerType(String strName, int size) {
		//Pointer is always size 4 bytes
		super(strName, 4);
		this.parameters = new Vector<STO>();
	}
	public Type clone(){
		FunctionPointerType t = new FunctionPointerType(this.getName(), 4);
		t.setParameters(this.getParameters());
		t.setReturnType(this.getReturnType());
		return t;
	}
	public boolean isFuncPointer(){
		return true;
	}
	//TODO
	public boolean isEquivalentTo(Type t){
		if(t.isFuncPointer()){
			//Check formal parameter
			if(((FunctionPointerType)t).getParameterNumbers() != this.getParameterNumbers())
				return false;
			for(int i = 0; i < this.getParameterNumbers(); i++){
				if(this.getParameters().elementAt(i).getType().isReference() == ((FunctionPointerType)t).getParameters().elementAt(i).getType().isReference()){
					if(this.getParameters().elementAt(i).getType().isEquivalentTo(((FunctionPointerType)t).getParameters().elementAt(i).getType())){
						
					}else{
						return false;
					}
				}
				//Any parameters not matching, return false
				else{
					return false;
				}
			}
			//Check return type as well as pass by reference or pass by value
			if(((FunctionPointerType)t).getReturnType().isReference() == (this.getReturnType().isReference())){
				if(((FunctionPointerType)t).getReturnType().isEquivalentTo(this.getReturnType()))
					return true;
				else
					return false;
			}else
				return false;
			
		}
		else{
			return false;
		}
	}
	
	public boolean isAssignableTo(Type t){
		return isEquivalentTo(t);
	}
	
	public void setParameters(Vector<STO> stolist){
		for(STO s : stolist){
			parameters.addElement(s);
		}
	}
	
	public String getErrorName(){
		String paramList = "";
		for(int i = 0; i < this.parameters.size(); i++){
			STO s = this.parameters.elementAt(i);
			if(i == this.parameters.size()-1){
				paramList += s.getType().getName();
				if(s.getType().isReference())
					paramList += " &"+ s.getName();
				else
					paramList += " " + s.getName();
			}
			else{
				paramList += s.getType().getName();
				if(s.getType().isReference())
					paramList += " &"+ s.getName() + ", ";
				else
					paramList += " " + s.getName() + ", ";
			}
		}
		String result = "";
		result += "funcptr : " + this.getReturnType().getName();
		if(this.getReturnType().isReference()){
			result += " &";
		}
		result += " (" + paramList + ")";
		return result;
	}
	
	public void addParameter(STO s){
		this.parameters.addElement(s);
	}
	
	public int getParameterNumbers(){
		return this.parameters.size();
	}
	
	public Vector<STO> getParameters(){
		return this.parameters;
	}
	
	private Vector<STO> parameters;
	
	public void setReturnType(Type t){
		this.returnType = t;
	}
	public Type getReturnType(){
		return this.returnType;
	}
	private Type returnType;

}
