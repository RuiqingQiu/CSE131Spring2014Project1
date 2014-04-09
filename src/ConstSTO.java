//---------------------------------------------------------------------
//
//---------------------------------------------------------------------

class ConstSTO extends STO
{
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public 
	ConstSTO (String strName)
	{
		super (strName);
		m_value = null; // fix this
                // You may want to change the isModifiable and isAddressable                      
                // fields as necessary
	}

	public 
	ConstSTO (String strName, Type typ)
	{
		super (strName, typ);
		if(typ.isInt()){
			//Phase 0 part 3
			//It's a octal or hex value
			if(strName.charAt(0) == '0'){
				m_value = Integer.decode(strName).doubleValue();
			}
			//If it's a good format
			else
				m_value = Double.parseDouble(strName);
		}
		
		this.setIsAddressable(false);
		this.setIsModifiable(false);
                // You may want to change the isModifiable and isAddressable                      
                // fields as necessary
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public boolean
	isConst () 
	{
		return true;
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	private void
	setValue (double val) 
	{
		m_value = new Double(val);
	}

	public Double
	getValue () 
	{
		return m_value;
	}

	public int
	getIntValue () 
	{
		return m_value.intValue();
	}

	public float
	getFloatValue () 
	{
		return m_value.floatValue();
	}

	public boolean
	getBoolValue () 
	{
		return (m_value.intValue() != 0);
	}


//----------------------------------------------------------------
//	Constants have a value, so you should store them here.
//	Note: We suggest using Java's Double class, which can hold
//	floats and ints. You can then do .floatValue() or 
//	.intValue() to get the corresponding value based on the
//	type. Booleans/Ptrs can easily be handled by ints.
//	Feel free to change this if you don't like it!
//----------------------------------------------------------------
        private Double		m_value;
}
