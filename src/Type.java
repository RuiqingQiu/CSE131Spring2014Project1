//---------------------------------------------------------------------
// This is the top of the Type hierarchy. You most likely will need to
// create sub-classes (since this one is abstract) that handle specific
// types, such as IntType, FloatType, ArrayType, etc.
//---------------------------------------------------------------------


abstract class Type
{
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public 
	Type (String strName, int size)
	{
		setName(strName);
		setSize(size);
	}
	

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public String
	getName ()
	{
		return m_typeName;
	}

	protected void
	setName (String str)
	{
		m_typeName = str;
	}
	public Type clone(){
		return null;
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public int
	getSize ()
	{
		return m_size;
	}

	protected void
	setSize (int size)
	{
		m_size = size;
	}

	//(int -> float)
	public abstract boolean isAssignableTo(Type t);
	// Same type
	public abstract boolean isEquivalentTo(Type t);
	public boolean isReference(){ return isReference; }
	public void setReference(boolean b) {this.isReference = b;}
	//----------------------------------------------------------------
	//	It will be helpful to ask a Type what specific Type it is.
	//	The Java operator instanceof will do this, but you may
	//	also want to implement methods like isNumeric(), isInt(),
	//	etc. Below is an example of isInt(). Feel free to
	//	change this around.
	//----------------------------------------------------------------
	public boolean isInt ()	{ return false; }
	public boolean isBool()		{ return false; }
	public boolean isError()    { return false; }
	public boolean isFloat()	{ return false; }
	public boolean isVoid() {return false;}
	public boolean isArray() {return false;}
	public boolean isPointer() {return false;}
	public boolean isStruct() {return false;}
	public boolean isNumeric(){return false;}
	public boolean isNullPointer() {return false;}
	public boolean isFuncPointer(){return false;}
	public boolean isGeneralPointer(){return false;}
	//----------------------------------------------------------------
	//	Name of the Type (e.g., int, bool, or some typedef
	//----------------------------------------------------------------
	protected String  	m_typeName;
	private int		m_size;
	private boolean isReference = false;
}
