// Josep Suy abril 2007



public class Registre  {

	String lexema;
	char tipus;
	int adreca;


public Registre() {
	lexema="";
	tipus='I';
	adreca=0;
	}


public Registre(String l) {
	lexema=l;
	tipus='I';
	adreca=0;
	}
public Registre(String l, char t) {
	lexema=l;
	tipus=t;
	adreca=0;
	}
public Registre(String l, char t, int a) {
	lexema=l;
	tipus=t;
	adreca=a;
	}


public String getLexema() {
	return (lexema);
	}
public char getTipus() {
	return (tipus);
	}
public Integer getAdreca() {
	return (adreca);
	}

public void putLexema(String l) {
	lexema=l;
	}
public void putTipus(char t) {
	tipus=t;
	}
public void putAdreca(int a) {
	adreca=a;
	}

}
