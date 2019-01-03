// Josep Suy abril 2007

import java.util.*;



public class SymTable<R>  {

	 HashMap<String,R> TS;
	

public SymTable ()
	{
	TS=new HashMap<String,R>(101);
	}

public SymTable (int capacitat)
	{
	TS=new HashMap<String,R>(capacitat);
	}

public void inserir(String s,R r)
	{
	TS.put(s,r);
	}

public R obtenir(String s)
	{
	return(TS.get(s));
	}

public boolean existeix(String s)
	{
	return(TS.containsKey(s));	
	}

public Set<String> conjuntClaus()
	{
	return(TS.keySet());
	} 

public void llistatClaus()
	{
	Iterator<String> i=TS.keySet().iterator();
	while (i.hasNext())
		{
		System.out.println(i.next());
		}
	
	}

public Collection<R> valors()
	{
	return(TS.values());
	} 


}


