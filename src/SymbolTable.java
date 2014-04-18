
//---------------------------------------------------------------------
//
//---------------------------------------------------------------------
import java.util.*;


class SymbolTable
{
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public 
	SymbolTable ()
	{
		m_nLevel = 0;
		m_stkScopes = new Stack<Scope> ();
		m_scopeGlobal = null;
		this.whileLevel = 0;
		this.inWhile = false;
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void
	insert (STO sto)
	{
		Scope		scope = m_stkScopes.peek ();

		scope.InsertLocal (sto);
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public STO
	accessGlobal (String strName)
	{
		return (m_scopeGlobal.access (strName));
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public STO
	accessLocal (String strName)
	{
		Scope		scope = m_stkScopes.peek ();

		return (scope.accessLocal (strName));
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public STO
	access (String strName)
	{
		Stack<Scope>		stk = new Stack<Scope> ();
		Scope		scope;
		STO		stoReturn = null;	
		
		// Phase 0 fix
		for (Enumeration<Scope> e = m_stkScopes.elements(); e.hasMoreElements(); )
		{
			scope = e.nextElement();
			stk.push(scope);
			
		}
		while(!stk.empty()){
			scope = stk.pop();
			if ((stoReturn = scope.access (strName)) != null)
				return	stoReturn;
		}
		
		return (null);
		
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void
	openScope ()
	{
		Scope		scope = new Scope();

		//	The first scope created will be the global scope.
		if (m_scopeGlobal == null)
			m_scopeGlobal = scope;

		m_stkScopes.push (scope);
		m_nLevel++;
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void
	closeScope ()
	{
		m_stkScopes.pop ();
		m_nLevel--;
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public int
	getLevel ()
	{
		return m_nLevel;
	}
	
	public void incrementWhileLevel(){
		++whileLevel;
	}
	public void decrementWhileLevel(){
		--whileLevel;
	}
	public int getWhileLevel(){
		return this.whileLevel;
	}
	public void inWhileLoop(boolean b){
		this.inWhile = b;
	}
	public boolean isInWhileLoop(){
		return this.inWhile;
	}
	public Vector<STO> getDeclaredField(){
		Scope		scope = m_stkScopes.peek ();
		return scope.getLocals();
	}
    

	//----------------------------------------------------------------
	//	This is the function currently being parsed.
	//----------------------------------------------------------------
	public FuncSTO		getFunc () { return m_func; }
	public void		setFunc (FuncSTO sto) { m_func = sto; }
	public TypedefSTO getStruct() {return m_struct;}
	public void setStruct(TypedefSTO sto) {m_struct = sto;}
	public boolean structDefineComplete(){return structDefineComplete;}
	public void setStructDefineComplete(boolean b){this.structDefineComplete = b;}

//----------------------------------------------------------------
//	Instance variables.
//----------------------------------------------------------------
	private Stack<Scope>  		m_stkScopes;
	private int		            m_nLevel;
	private Scope		        m_scopeGlobal;
	private FuncSTO	            m_func = null;
	//A flag to indicate the struct definition has ended
	private boolean 			structDefineComplete = true;
	private TypedefSTO			m_struct = null;
	private int whileLevel;
	private boolean inWhile;
}
