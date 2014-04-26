import java.util.Vector;

//---------------------------------------------------------------------
//
//---------------------------------------------------------------------

class FuncSTO extends STO
{
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public 
	FuncSTO (String strName)
	{
		super (strName);
		setReturnType (null);
                // You may want to change the isModifiable and isAddressable                      
                // fields as necessary
		this.parameters = new Vector<STO>();
		this.top_level_return = false;
		this.overloadedFuncs = new Vector<STO>();
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public boolean
	isFunc () 
	{ 
		return true;
                // You may want to change the isModifiable and isAddressable                      
                // fields as necessary
	}


	//----------------------------------------------------------------
	// This is the return type of the function. This is different from 
	// the function's type (for function pointers).
	//----------------------------------------------------------------
	public void
	setReturnType (Type typ)
	{
		m_returnType = typ;
	}

	public Type
	getReturnType ()
	{
		return m_returnType;
	}
	
	public void addParameter(STO s){
		this.parameters.add(s);
	}
	public int getParameterNumbers(){
		return this.parameters.size();
	}
	public Vector<STO> getParameterSTO(){
		return parameters;
	}
	public void setTopLevelReturn(boolean b){
		this.top_level_return = b;
	}
	public boolean getTopLevelReturn(){
		return this.top_level_return;
	}
	
	private Vector<STO> overloadedFuncs;
//----------------------------------------------------------------
//	Instance variables.
//----------------------------------------------------------------
	private Type 		m_returnType;
	private boolean     top_level_return;
	private Vector<STO> parameters;
	
	//For extra credit
	private boolean overloaded;
	
}
