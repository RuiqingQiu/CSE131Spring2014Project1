//---------------------------------------------------------------------
//
//---------------------------------------------------------------------

class VarSTO extends STO
{
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public 
	VarSTO (String strName)
	{
		super (strName);
		// You may want to change the isModifiable and isAddressable 
		// fields as necessary
	}

	public 
	VarSTO (String strName, Type typ)
	{
		super (strName, typ);
		// You may want to change the isModifiable and isAddressable 
		// fields as necessary
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public boolean   
	isVar () 
	{
		return true;
	}
	public STO getInit(){
		return init;
	}
	public void setInit(STO init){
		this.init = init;
	}
	
	public boolean isStatic(){
		return this.isStatic;
	}
	public void setStatic(boolean b){
		this.isStatic = b;
	}
	private STO init;
	private boolean isStatic;
}
