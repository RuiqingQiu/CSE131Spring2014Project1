
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
	DoVarDecl (Vector<String> lstIDs, Type type)
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
			sto.setType(type);
			sto.setIsAddressable(true);
			sto.setIsModifiable(true);
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
	DoConstDecl (Vector<String> lstIDs)
	{
		for (int i = 0; i < lstIDs.size (); i++)
		{
			String id = lstIDs.elementAt (i);

			if (m_symtab.accessLocal (id) != null)
			{
				m_nNumErrors++;
				m_errors.print (Formatter.toString (ErrorMsg.redeclared_id, id));
			}
		
			ConstSTO 	sto = new ConstSTO (id);
			m_symtab.insert (sto);
		}
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void
	DoTypedefDecl (Vector<String> lstIDs)
	{
		for (int i = 0; i < lstIDs.size (); i++)
		{
			String id = lstIDs.elementAt (i);

			if (m_symtab.accessLocal (id) != null)
			{
				m_nNumErrors++;
				m_errors.print (Formatter.toString(ErrorMsg.redeclared_id, id));
			}
		
			TypedefSTO 	sto = new TypedefSTO (id);
			m_symtab.insert (sto);
		}
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void
	DoStructdefDecl (String id)
	{
		if (m_symtab.accessLocal (id) != null)
		{
			m_nNumErrors++;
			m_errors.print (Formatter.toString(ErrorMsg.redeclared_id, id));
		}
		
		TypedefSTO 	sto = new TypedefSTO (id);
		m_symtab.insert (sto);
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
	DoReturnCheck(Type t){
		if (m_symtab.getFunc () == null)
		{
			m_nNumErrors++;
			m_errors.print ("internal: DoReturnCheck says no proc!");
		}
		//no expr is specified and the return type is not void
		if(!(m_symtab.getFunc().getReturnType() instanceof VoidType)){
			if(t instanceof VoidType){
				m_nNumErrors++;
				m_errors.print (ErrorMsg.error6a_Return_expr);
			}
		}
		
		
	}

    //----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void
	DoBlockOpen ()
	{
		// Open a scope.
		m_symtab.openScope ();
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void
	DoBlockClose()
	{
		m_symtab.closeScope ();
	}
	
	//Check #1
	STO
	DoBinaryExpr(STO a, BinaryOp o, STO b)
	{
		STO result = o.checkOperands(a, b);
	    if (result instanceof ErrorSTO) {
	    	result.setType(new ErrorType("error",8));
	    	m_nNumErrors++;
			m_errors.print (result.getName());
	    }
	    return result;
	}
	
	STO
	DoUnaryExpr(STO a, UnaryOp o)
	{
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
	DoAssignExpr (STO stoDes, STO expr)
	{
		//Check #3a
		if(expr.isError())
			return expr;
		//Check if STO is not modifiable value
		if (!stoDes.isModLValue())
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
		if (!expr.getType().isAssignableTo(stoDes.getType())){
			STO result = new ErrorSTO(Formatter.toString(ErrorMsg.error3b_Assign, 
					expr.getType().getName(), stoDes.getType().getName()));
			result.setType(new ErrorType("error",8));
			m_nNumErrors++;
			m_errors.print (result.getName());
			return result;
		}
		return stoDes;
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

		return sto;
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	STO
	DoDesignator2_Array (STO sto)
	{
		// Good place to do the array checks

		return sto;
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
	private ErrorPrinter		m_errors;
	private int 			m_nNumErrors;
	private String			m_strLastLexeme;
	private boolean			m_bSyntaxError = true;
	private int			m_nSavedLineNum;

	private SymbolTable		m_symtab;
}
