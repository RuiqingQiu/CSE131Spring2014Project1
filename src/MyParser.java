
//---------------------------------------------------------------------
//
//---------------------------------------------------------------------

import java_cup.runtime.*;
import java.util.Vector;



class MyParser extends parser
{

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public 
	MyParser (Lexer lexer, ErrorPrinter errors)
	{
		m_lexer = lexer;
		m_symtab = new SymbolTable ();
		m_errors = errors;
		m_nNumErrors = 0;
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public boolean
	Ok ()
	{
		return (m_nNumErrors == 0);
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public Symbol
	scan ()
	{
		Token		t = m_lexer.GetToken ();

		//	We'll save the last token read for error messages.
		//	Sometimes, the token is lost reading for the next
		//	token which can be null.
		m_strLastLexeme = t.GetLexeme ();

		switch (t.GetCode ())
		{
			case sym.T_ID:
			case sym.T_ID_U:
			case sym.T_STR_LITERAL:
			case sym.T_FLOAT_LITERAL:
			case sym.T_INT_LITERAL:
			case sym.T_CHAR_LITERAL:
				return (new Symbol (t.GetCode (), t.GetLexeme ()));
			default:
				return (new Symbol (t.GetCode ()));
		}
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void
	syntax_error (Symbol s)
	{
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void
	report_fatal_error (Symbol s)
	{
		m_nNumErrors++;
		if (m_bSyntaxError)
		{
			m_nNumErrors++;

			//	It is possible that the error was detected
			//	at the end of a line - in which case, s will
			//	be null.  Instead, we saved the last token
			//	read in to give a more meaningful error 
			//	message.
			m_errors.print (Formatter.toString (ErrorMsg.syntax_error, m_strLastLexeme));
		}
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void
	unrecovered_syntax_error (Symbol s)
	{
		report_fatal_error (s);
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void
	DisableSyntaxError ()
	{
		m_bSyntaxError = false;
	}

	public void
	EnableSyntaxError ()
	{
		m_bSyntaxError = true;
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public String 
	GetFile ()
	{
		return (m_lexer.getEPFilename ());
	}

	public int
	GetLineNum ()
	{
		return (m_lexer.getLineNumber ());
	}

	public void
	SaveLineNum ()
	{
		m_nSavedLineNum = m_lexer.getLineNumber ();
	}

	public int
	GetSavedLineNum ()
	{
		return (m_nSavedLineNum);
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void
	DoProgramStart()
	{
		// Opens the global scope.
		m_symtab.openScope ();
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void
	DoProgramEnd()
	{
		m_symtab.closeScope ();
	}
	
	
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void
	DoVarDecl (Vector<VarSTO> stoList, Type type)
	{
		Vector<String> lstIDs = new Vector<String>();
		for (STO s : stoList){
			lstIDs.addElement(s.getName());
		}
		for (int i = 0; i < lstIDs.size (); i++)
		{
			String id = lstIDs.elementAt (i);
			//Check redeclare error
			if (m_symtab.accessLocal (id) != null)
			{
				m_nNumErrors++;
				m_errors.print (Formatter.toString(ErrorMsg.redeclared_id, id));
			}
			
			//Check global or static initialized is known at compile time
			if(stoList.elementAt(i).isStatic()){
				//If there's init
				if(stoList.elementAt(i).getInit() != null){
					if(!(stoList.elementAt(i).getInit() instanceof ConstSTO)){
						m_nNumErrors++;
						m_errors.print (Formatter.toString(ErrorMsg.error8a_CompileTime, id));
						return;
					}
				}
			}
			//Global or local
			else{
				//If it's  global scope
				if(m_symtab.getLevel() == 1){
					if(stoList.elementAt(i).getInit() != null){
						if(!(stoList.elementAt(i).getInit() instanceof ConstSTO)){
							m_nNumErrors++;
							m_errors.print (Formatter.toString(ErrorMsg.error8a_CompileTime, id));
							return;
						}
					}
				}
			}
			
			//Check if the init type is assignable to Type
			if(stoList.elementAt(i).getInit() != null){
				//If the init expression is not assignable to type declared
				if(!(stoList.elementAt(i).getInit().getType().isAssignableTo(type))){
					m_nNumErrors++;
					m_errors.print (Formatter.toString(ErrorMsg.error8_Assign,stoList.elementAt(i).getInit().getType().getName(), type.getName()));
				    return;
				}
			}

			VarSTO 		sto = new VarSTO (id);
			//Check if it's an array type
			if(stoList.elementAt(i).getType() != null){
				if(stoList.elementAt(i).getType().isArray()){
					((CompositeType)stoList.elementAt(i).getType()).setElementType(type);
					sto.setType(stoList.elementAt(i).getType());
					//Array is addressable but not modifiable
					sto.setIsAddressable(true);
					sto.setIsModifiable(false);
				}
				else if(stoList.elementAt(i).getType().isPointer()){
					((CompositeType)stoList.elementAt(i).getType()).setElementType(type);
					sto.setType(stoList.elementAt(i).getType());
					//Array is addressable but not modifiable
					sto.setIsAddressable(true);
					sto.setIsModifiable(true);
				}
			}
			else{
				sto.setType(type);
				//Regular declare, l-value
				sto.setIsAddressable(true);
				sto.setIsModifiable(true);
			}
			
			m_symtab.insert (sto);
		}
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void
	DoExternDecl (Vector<String> lstIDs)
	{
		for (int i = 0; i < lstIDs.size (); i++)
		{
			String id = lstIDs.elementAt (i);

			if (m_symtab.accessLocal (id) != null)
			{
				m_nNumErrors++;
				m_errors.print (Formatter.toString(ErrorMsg.redeclared_id, id));
			}

			VarSTO 		sto = new VarSTO (id);
			m_symtab.insert (sto);
		}
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void
	DoConstDecl (Vector<VarSTO> STOlst, Type type)
	{
		Vector<String> lstIDs = new Vector<String>();
		for(VarSTO s : STOlst){
			lstIDs.addElement(s.getName());
		}
		for (int i = 0; i < lstIDs.size (); i++)
		{
			String id = lstIDs.elementAt (i);
            //throw error if redeclaration occurs
			if (m_symtab.accessLocal (id) != null)
			{
				m_nNumErrors++;
				m_errors.print (Formatter.toString (ErrorMsg.redeclared_id, id));
				return;
			}
			//check if the const init is known at compile time
			if(!(STOlst.elementAt(i).getInit() instanceof ConstSTO)){
				m_nNumErrors++;
				m_errors.print (Formatter.toString (ErrorMsg.error8b_CompileTime, id));
				return;
			}
			//Check if the ConstExpr is assignable to Type
			if(!(STOlst.elementAt(i).getInit().getType().isAssignableTo(type))){
				m_errors.print (Formatter.toString (ErrorMsg.error8_Assign, STOlst.elementAt(i).getInit().getType().getName(), type.getName()));
			    return;
			}
			ConstSTO 	sto = new ConstSTO (id);
			sto.setType(type);
			sto.setIsAddressable(true);
			sto.setIsModifiable(false);
			ConstSTO c = (ConstSTO)(STOlst.elementAt(i).getInit());
			sto.setValue(c.getValue());
			m_symtab.insert (sto);
		}
	}
	

    //----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void
	DoTypedefDecl (Type type, Vector<String> lstIDs)
	{
		for (int i = 0; i < lstIDs.size (); i++)
		{
			String id = lstIDs.elementAt (i);

			if (m_symtab.accessLocal (id) != null)
			{
				m_nNumErrors++;
				m_errors.print (Formatter.toString(ErrorMsg.redeclared_id, id));
			}
			//set the name of the TypedefSTO
			TypedefSTO 	sto = new TypedefSTO (id);
			
			sto.setType(type);
			m_symtab.insert (sto);
		}
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void
	DoStructdefDecl (String id)
	{ 
		//Check redeclare id
		if (m_symtab.accessLocal (id) != null)
		{
			m_nNumErrors++;
			m_errors.print (Formatter.toString(ErrorMsg.redeclared_id, id));
		}

		TypedefSTO 	sto = new TypedefSTO (id);
		StructType st = new StructType("struct", 0);
		sto.setType(st);
		m_symtab.insert (sto);
		m_symtab.setStruct(sto);
		m_symtab.setStructDefineComplete(false);
	}
	
	void 
	DoStructFieldFill(){
		Vector<STO> structFields = m_symtab.getDeclaredField();
		int size = 0;
		for(STO s : structFields){
			size += s.getType().getSize();
		}
		StructType st = new StructType("struct", size);
		st.setField(structFields);
		m_symtab.access(m_symtab.getStruct().getName()).setType(st);
	}
	void 
	DoStructdefDeclEnd(){
		m_symtab.setStruct(null);
		m_symtab.setStructDefineComplete(true);
	}
    
	STO
	DoStructFieldDecl (Vector<VarSTO> vlist){
		boolean Error = false;
		for(VarSTO v : vlist ){
			
			String id = v.getName();
			//Check if the id is already declared in the struct
			if(m_symtab.accessLocal(id)!= null){
				m_nNumErrors++;
				m_errors.print (Formatter.toString(ErrorMsg.error13a_Struct, id));
				Error = true;
			}
			else{
				m_symtab.insert (v);
			}
		}
		//
		if(Error){
			return new ErrorSTO("struct field error");
		}else{
			for(VarSTO v : vlist ){
			//Check if recursive struct definition
				if(v.getType().isStruct()){
					//If the field declare is equal to the name of the current struct
					if(v.getType().getName().equals(m_symtab.getStruct().getName())){
						m_nNumErrors++;
						m_errors.print (Formatter.toString(ErrorMsg.error13b_Struct,v.getName()));
						Error = true;
					}
				}
			}
			if(Error){
				return new ErrorSTO("struct field error");
			}
			else
				return null;
		}
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void
	DoFuncDecl_1 (Type returnType, String id)
	{
		if (m_symtab.accessLocal (id) != null)
		{
			m_nNumErrors++;
			m_errors.print (Formatter.toString(ErrorMsg.redeclared_id, id));
		}
	
		FuncSTO sto = new FuncSTO (id);//initialize here so that we can insert parameter into the FuncSTO
		m_symtab.insert (sto);//inserted into current scope

		m_symtab.openScope ();
		m_symtab.setFunc (sto);
		m_symtab.getFunc().setReturnType(returnType);
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void
	DoFuncDecl_2 ()
	{
		
		m_symtab.closeScope ();//close scope(pops top scope off)
		//No top level return statement has been seen
		if (m_symtab.getFunc().getTopLevelReturn() == false && !(m_symtab.getFunc().getReturnType() instanceof VoidType)){
			m_nNumErrors++;
			m_errors.print (ErrorMsg.error6c_Return_missing);
		}
		m_symtab.setFunc (null);//Say we are back in outer scope
	}


	//----------------------------------------------------------------
	// DoFormalParams need to store all parameters to the FuncSTO and 
	// add all parameter list name binded to type and store in the 
	// symbol table
	//----------------------------------------------------------------
	void
	DoFormalParams (Vector<STO> params)
	{
		
		if (m_symtab.getFunc () == null)
		{
			m_nNumErrors++;
			m_errors.print ("internal: DoFormalParams says no proc!");
		}
		//If no arguments, return
		if(params.size() == 0)
			return;
		else
			//Add all the param to the symbal table and FuncSTO
			for(STO s : params){
				m_symtab.getFunc().addParameter(s);
				m_symtab.insert(s);
			}
	  // insert parameters here
	}
	
	void
	DoReturnCheck(STO s){
		Type t = s.getType();
		if (m_symtab.getFunc () == null)
		{
			m_nNumErrors++;
			m_errors.print ("internal: DoReturnCheck says no proc!");
			return;
		}
		
		//It's top level and a return statement is found
		if(m_symtab.getLevel() == 2){
			m_symtab.getFunc().setTopLevelReturn(true);
		}
		//Check if the return type is by reference or value
		if(m_symtab.getFunc().getReturnType().isReference()){
		  //Check if the type of return expression is not equivalent to the return 
		  //type of the function
		  if(!(t.isEquivalentTo(m_symtab.getFunc().getReturnType()))){
			  m_nNumErrors++;
			  m_errors.print (Formatter.toString(ErrorMsg.error6b_Return_equiv, t.getName(),m_symtab.getFunc().getReturnType().getName()));
			  return;
		  }
		  //return expression is not a modifiable L-value
		  if(!(s.isModLValue())){
			  m_nNumErrors++;
			  m_errors.print (ErrorMsg.error6b_Return_modlval);
			  return;
		  }
		}
		else{
			//no expr is specified and the return type is not void
			if(!(m_symtab.getFunc().getReturnType() instanceof VoidType)){
				//Return should not be void but is void
				if(t instanceof VoidType){
					m_nNumErrors++;
					m_errors.print (ErrorMsg.error6a_Return_expr);
					return;
				}
				//user given return type is not assignable to function return type
				if(!(t.isAssignableTo(m_symtab.getFunc().getReturnType()))){
					m_nNumErrors++;
					m_errors.print (Formatter.toString(ErrorMsg.error6a_Return_type, t.getName(),m_symtab.getFunc().getReturnType().getName()));
				    return;
				}
			}
			else{
				//user given return type is not assignable to function return type
				if(!(t.isAssignableTo(m_symtab.getFunc().getReturnType()))){
					m_nNumErrors++;
					m_errors.print (Formatter.toString(ErrorMsg.error6a_Return_type, t.getName(),m_symtab.getFunc().getReturnType().getName()));
				    return;
				}
			}
		}
		return;
	}
	
	void
	DoExitStmtCheck(STO s){
		//cehck if the STO is assignable to an int
		if(!(s.getType().isAssignableTo(new IntType("int",4)))){
			m_nNumErrors++;
			m_errors.print (Formatter.toString(ErrorMsg.error7_Exit, s.getType().getName()));
		}
	}
	
	void
	DoBreakStmtCheck(){
		//program + function + while loop
		if(!(m_symtab.isInWhileLoop())){
			m_nNumErrors++;
			m_errors.print (ErrorMsg.error12_Break);
		}
	}
	
	void
	DoContinueStmtCheck(){
		//program + function + while loop
		if(!(m_symtab.isInWhileLoop())){
		  m_nNumErrors++;
		  m_errors.print (ErrorMsg.error12_Continue);
		}	
	}

    //----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void
	DoBlockOpen ()
	{
		if(this.m_symtab.isInWhileLoop())
		  this.m_symtab.incrementWhileLevel();
		// Open a scope.
		m_symtab.openScope ();
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void
	DoBlockClose()
	{
		//If the block closing is in the while loop, decrement the level
		if(this.m_symtab.isInWhileLoop()){
			this.m_symtab.decrementWhileLevel();
			//If the level reach 0, then no more while loop
			if(this.m_symtab.getWhileLevel() == 0)
				this.m_symtab.inWhileLoop(false);
		}
		m_symtab.closeScope ();
	}
	
	//Check #1
	STO
	DoBinaryExpr(STO a, BinaryOp o, STO b)
	{
		if(a.isError())
			return a;
		else if(b.isError())
			return b;
		else{
			STO result = o.checkOperands(a, b);
		    if (result instanceof ErrorSTO) {
		    	result.setType(new ErrorType("error",8));
		    	m_nNumErrors++;
				m_errors.print (result.getName());
		    }
		    return result;
		}
	}
	
	STO
	DoUnaryExpr(STO a, UnaryOp o)
	{
		if(a.isError())
			return a;
		STO result = o.checkOperands(a);
		if(result instanceof ErrorSTO){
			result.setType(new ErrorType("error",8));
			m_nNumErrors++;
			m_errors.print (result.getName());
		}
		return result;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	STO
	DoAssignExpr (STO leftHandSide, STO rightHandSide)
	{
		//Check #3a
		if(rightHandSide.isError())
			return rightHandSide;
		else if(leftHandSide.isError())
			return leftHandSide;
		//Check if STO is not modifiable value
		if (!leftHandSide.isModLValue())
		{
			//Enter here if it's an error
			STO result = new ErrorSTO(ErrorMsg.error3a_Assign);
			result.setType(new ErrorType("error",8));
			m_nNumErrors++;
			m_errors.print (result.getName());
			return result;
			// Good place to do the assign check		
		}
		//It's a good modifiable L-value
		//Check #3b
		//Check if the expr is not assignable to the designator
		if (!rightHandSide.getType().isAssignableTo(leftHandSide.getType())){
			STO result = new ErrorSTO(Formatter.toString(ErrorMsg.error3b_Assign, 
					((CompositeType)rightHandSide.getType()).getElementType().getName() + "["+((CompositeType)rightHandSide.getType()).getSize() + "]" , 
					((CompositeType)leftHandSide.getType()).getElementType().getName()+ "*"));
			result.setType(new ErrorType("error",8));
			m_nNumErrors++;
			m_errors.print (result.getName());
			return result;
		}
		return leftHandSide;
	}

	void DoWhileStmt(){
		this.m_symtab.inWhileLoop(true);
	}
	STO
	DoIfWhileExpr(STO expr)
	{
		if(expr.getType().isBool() || expr.getType().isInt())
			return expr;
		
		STO result = new ErrorSTO(Formatter.toString(ErrorMsg.error4_Test, 
				expr.getType().getName()));
		result.setType(new ErrorType("error",8));
		m_nNumErrors++;
		m_errors.print (result.getName());
		return result;
	}
	
	

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	STO
	DoFuncCall (STO sto, Vector<STO> arguments)
	{
		if (!sto.isFunc())
		{
			m_nNumErrors++;
			m_errors.print (Formatter.toString(ErrorMsg.not_function, sto.getName()));
			return (new ErrorSTO (sto.getName ()));
		}
		
		//It's a FuncSTO, check number of arguments
		FuncSTO tmp = (FuncSTO) sto;
		//Check the number is the same
		if(arguments.size() == tmp.getParameterNumbers()){
			Vector<STO> params = (Vector<STO>) tmp.getParameterSTO().clone();
			for(int i = 0; i < arguments.size(); i++){
				if (params.get(i).getType().isReference()){
					//pass by reference, argument type is not equivalent to the parameter type
					if(!arguments.get(i).getType().isEquivalentTo(params.get(i).getType())){
						m_nNumErrors++;
						m_errors.print (Formatter.toString(ErrorMsg.error5r_Call, 
						  arguments.get(i).getType().getName(), params.get(i).getName(), params.get(i).getType().getName()));
						return (new ErrorSTO ("DoFuncCall,  pass-by-reference error"));
					}
					
					//pass by reference, argument is not a modifiable L-value
					if(!arguments.get(i).isModLValue()){
						m_nNumErrors++;
						m_errors.print (Formatter.toString(ErrorMsg.error5c_Call, 
								params.get(i).getName(), params.get(i).getType().getName()));
						return (new ErrorSTO ("DoFuncCall,  pass-by-reference L-value error"));
					}
					
					
					
				}
				//The argument is pass by value
				else{
					//If the type is not assignable
					if(!arguments.get(i).getType().isAssignableTo(params.get(i).getType())){
						m_nNumErrors++;
						m_errors.print (Formatter.toString(ErrorMsg.error5a_Call, 
						  arguments.get(i).getType().getName(), params.get(i).getName(), params.get(i).getType().getName()));
						return (new ErrorSTO ("DoFuncCall, pass-by-value error"));
					}
				}
			}
		    //The function evaluates to return type
		    return new ExprSTO("FuncCall", tmp.getReturnType());
		}
		else{
			m_nNumErrors++;
			m_errors.print (Formatter.toString(ErrorMsg.error5n_Call, arguments.size(), tmp.getParameterNumbers()));
			return (new ErrorSTO ("DoFuncCall, arugment number"));
		}
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	STO
	DoDesignator2_Dot (STO sto, String strID)
	{
		// Good place to do the struct checks
        //check the type of sto is a struct type
		//if(!(sto.getType().isStruct()))
		//check type of struct has no field named strID
		
		
		return sto;
	}


	Type
	DoArrayDeclCheck(STO sto){
		if(sto.isError())
		{
			return new ErrorType("error", 4);
		}
		//Check if the type of index expression is equivalent to int
		if(!(sto.getType().isEquivalentTo(new IntType("int", 4)))){
			m_nNumErrors++;
		 	m_errors.print (Formatter.toString(ErrorMsg.error10i_Array, sto.getType().getName()));	
		 	return new ErrorType("error", 4);
		}
		//Check if the value of the index expression is not known at compile time
		if(!(sto instanceof ConstSTO)){
			m_nNumErrors++;
		 	m_errors.print(ErrorMsg.error10c_Array);	
		 	return new ErrorType("error", 4);
		}
		//Check if the value of the index expression is not greater than 0
		if(((ConstSTO)sto).getIntValue() <= 0){
			m_nNumErrors++;
		 	m_errors.print(Formatter.toString(ErrorMsg.error10z_Array,((ConstSTO)sto).getIntValue()));	
		 	return new ErrorType("error", 4);
		}
		
		ArrayType t = new ArrayType("array", 4);
		t.setArraySize(((ConstSTO)sto).getIntValue());
		return t;
	}
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	STO
	DoDesignator2_Array (STO nameSto, STO indexExpr)
	{
		if(indexExpr.isError()){
			return indexExpr;
		}
		// Good place to do the array checks
		//Check the type of designator precding any [] operator is not an array or pointer type
		if(!(nameSto.getType() instanceof ArrayType) && !(nameSto.getType() instanceof PointerType)){
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.error11t_ArrExp,nameSto.getType().getName()));	
			return new ErrorSTO("error");
		}
		
	    //Check the type of the index expression is not equivalent to int
		if(!(indexExpr.getType().isEquivalentTo(new IntType("int",4)))){
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.error11i_ArrExp,indexExpr.getType().getName()));	
			return new ErrorSTO("error");
		}
		//Check if the index expression is a constant, an error should generate if the index is 
		//outside the bounds of the array
		if(nameSto.getType() instanceof ArrayType){
			if(indexExpr instanceof ConstSTO){
				if(((ConstSTO) indexExpr).getIntValue() >= ((ArrayType)nameSto.getType()).getArraySize() || 
				   ((ConstSTO) indexExpr).getIntValue() < 0){
					m_nNumErrors++;
					m_errors.print(Formatter.toString(ErrorMsg.error11b_ArrExp,((ConstSTO) indexExpr).getIntValue(),((ArrayType)nameSto.getType()).getArraySize()));	
					return new ErrorSTO("error");
				}	
		    }
		}
		ExprSTO e = new ExprSTO("array_doDesignator2", ((CompositeType)nameSto.getType()).getElementType());
		
		
		e.setIsAddressable(true);
		e.setIsModifiable(true);
		//Correct usage of array, dereference the array and get its element type
		return e;
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	STO
	DoDesignator3_ID (String strID)
	{
		STO		sto;
		if ((sto = m_symtab.access (strID)) == null)
		{
			m_nNumErrors++;
		 	m_errors.print (Formatter.toString(ErrorMsg.undeclared_id, strID));	
			sto = new ErrorSTO (strID);
		}
		return (sto);
	}
	//Check #0, global variable check
	STO
	DoDesignator3_Global_ID (String strID)
	{
		STO sto;
		if((sto = m_symtab.accessGlobal(strID)) == null)
		{
			m_nNumErrors++;
		 	m_errors.print (Formatter.toString(ErrorMsg.error0g_Scope, strID));	
			sto = new ErrorSTO (strID);
		}
		return (sto);
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	STO
	DoQualIdent (String strID)
	{
		STO		sto;

		if ((sto = m_symtab.access (strID)) == null)
		{
			m_nNumErrors++;
		 	m_errors.print (Formatter.toString(ErrorMsg.undeclared_id, strID));	
			return (new ErrorSTO (strID));
		}

		if (!sto.isTypedef())
		{
			m_nNumErrors++;
			m_errors.print (Formatter.toString(ErrorMsg.not_type, sto.getName ()));
			return (new ErrorSTO (sto.getName ()));
		}
		return (sto);
	}


//----------------------------------------------------------------
//	Instance variables
//----------------------------------------------------------------
	private Lexer			m_lexer;
	private ErrorPrinter	m_errors;
	private int 			m_nNumErrors;
	private String			m_strLastLexeme;
	private boolean			m_bSyntaxError = true;
	private int			m_nSavedLineNum;

	private SymbolTable		m_symtab;
}
