import java.io.*;
import java.util.*;



//	Ajuda a la confeccio de codi en Bytecode. Practiques de MEC
//
//
//
//
//	Classes Metodes i Fields
//
//
//
//
//
//
//
//	Josep Suy, Marc Massot 		Ultima modificacio: 8 de maig de 2007
//
//
//
//



class method_info{

	Long access_flags;
	Long name_index;
	Long descriptor_index;
	Long attributes_count;
	Vector<attribute_info> attributes=new Vector<attribute_info>(2);

	method_info() {};
	// entCode= entrada Code de la TS
	// entException= entrada Exception de la TS
	// Exception_index0= entrada de la referencia a la funcio de la Excepcio a la TS
	method_info(Long entCode, Long entException,Long exception_index0,Long access_flags0,Long name_index0,Long descriptor_index0,Long max_stack0,Long max_locals0,Vector<Long> code0) {
		access_flags=access_flags0;
		name_index=name_index0;
		descriptor_index=descriptor_index0;
		attributes.add(new Code_attribute(entCode,max_stack0,max_locals0,code0));
		attributes.add(new Exceptions_attribute(entException,exception_index0));
		attributes_count=new Long(attributes.size());
	};
	void write(FileOutputStream f) {
		try {
			f.write(toByte(access_flags,2));
			f.write(toByte(name_index,2));
			f.write(toByte(descriptor_index,2));
			attributes_count=new Long(attributes.size());
			f.write(toByte(attributes_count,2));
			Iterator<attribute_info> i=attributes.iterator();
			while (i.hasNext()){
				i.next().write(f);
			}
		}
		catch (IOException s) {}
	};
	void show(){
		System.out.println("Metode        : "+name_index+"."+descriptor_index);
		System.out.println();
		Iterator<attribute_info> i=attributes.iterator();
		int j=1;
		while (i.hasNext()){
			i.next().show();
			j++;
		}
	};

 	byte[] toByte(Long num, int t) {
		int i;
		Long aux;

		byte[] bytes=new byte[t];
		aux=num;
		i=t-1;
		while (i>=0) { 
			bytes[i]=aux.byteValue(); 
			aux=(Long)aux.rotateRight(aux,8); 
			i--;
		}
		return bytes;
	} 
}



class attribute_info {

	Long attribute_name_index;
	Long attribute_length;

	attribute_info() {};
	void write(FileOutputStream f) {};
	void show() {};

 	byte[] toByte(Long num, int t) {
		int i;
		Long aux;

		byte[] bytes=new byte[t];
		aux=num;
		i=t-1;
		while (i>=0) { 
			bytes[i]=aux.byteValue(); 
			aux=(Long)aux.rotateRight(aux,8); 
			i--;
		}
		return bytes;
	} 
}

class Code_attribute extends attribute_info {

	Long max_stack;
	Long max_locals;
	Long code_length;
	Vector<Long> code=new Vector<Long>(50);
	Long exception_table_length;
	Long attributes_count;


	Code_attribute() {};
	Code_attribute(Long ent, Long max_stack0,Long max_locals0,Vector<Long> code0) {
		attribute_name_index=ent; //Correspont a l'entrada Code de la taula de simbols
		attribute_length=new Long(12+code0.size()); // 2(max_stack)+2(max_locals)+4(code_length)+2(exception_table_length)+2(attributes_count)+code.size
		max_stack=max_stack0;
		max_locals=max_locals0;
		code=code0;
		code_length=new Long(code.size());
		exception_table_length=0L;
		attributes_count=0L;
	};
	void write(FileOutputStream f) {
		try {
			f.write(toByte(attribute_name_index,2));
			attribute_length=new Long(12+code.size()); // 2(max_stack)+2(max_locals)+4(code_length)+2(exception_table_length)+2(attributes_count)+code.size
			f.write(toByte(attribute_length,4));
			f.write(toByte(max_stack,2));
			f.write(toByte(max_locals,2));
			f.write(toByte(code_length,4));
			Iterator<Long> i=code.iterator();
			while (i.hasNext()){
				f.write(toByte(i.next(),1));
			}
			f.write(toByte(exception_table_length,2));
			f.write(toByte(attributes_count,2));		
		}
		catch (IOException s) {}		
	};
	void show(){
		System.out.println("          Pila          : "+max_stack);
		System.out.println("          Locals        : "+max_locals);
		System.out.println();
		int j=1;
		Iterator<Long> i=code.iterator();
		Long v=0l;
		CMethod c=new CMethod();
		Integer a;
		while (i.hasNext()){
			a=new Integer(i.next().toString());
			if (v==0l) {
				System.out.println();
				System.out.print("		 "+j+"	: ");
				
				System.out.print(c.MAP[a]+"   ");
				v=c.OPS[a];
			}
			else
			{
				System.out.print(a+"	");
				v--;
			}
			j++;
		}
		System.out.println();
		System.out.println();

	};


}


class Exceptions_attribute extends attribute_info {

	Long number_of_exceptions;
	Long exception_index;


	Exceptions_attribute() {};
	Exceptions_attribute(Long ent, Long exception_index0) {
		attribute_name_index=ent; //Correspont a l'entrada Exceptions de la taula de simbols
		attribute_length=4L;
		number_of_exceptions=1L;
		exception_index=exception_index0; // correspont a la funcio d'exception de la TS
	};
	void write(FileOutputStream f) {
		try {
			f.write(toByte(attribute_name_index,2));
			attribute_length=4l; 
			f.write(toByte(attribute_length,4));
			f.write(toByte(number_of_exceptions,2));
			f.write(toByte(exception_index,2));
		}
		catch (IOException s) {}		
	};
	void show() {};


}



class ConstantValue_attribute extends attribute_info {

	Long constantvalue_index;


	ConstantValue_attribute() {};
	ConstantValue_attribute(Long ent, Long constantvalue_index0) {
		attribute_name_index=ent; //Correspont a l'entrada Constantvalue de la taula de simbols
		attribute_length=2L;
		constantvalue_index=constantvalue_index0; 
	};
	void write(FileOutputStream f) {
		try {
			f.write(toByte(attribute_name_index,2));
			attribute_length=2l; 
			f.write(toByte(attribute_length,4));
			f.write(toByte(constantvalue_index,2));
		}
		catch (IOException s) {}		
	};
	void show() {};


}





class field_info {

	Long access_flags;
	Long name_index;
	Long descriptor_index;
	Long attributes_count;
	Vector<attribute_info> attributes=new Vector<attribute_info>(2);

	field_info() {};
	// entConstantValue= entrada ConstantValue de la TS
	field_info(Long entConstantValue,Long access_flags0,Long name_index0,Long descriptor_index0,Long constantvalue_index0) {
		access_flags=access_flags0;
		name_index=name_index0;
		descriptor_index=descriptor_index0;
		attributes.add(new ConstantValue_attribute(entConstantValue,constantvalue_index0));
		attributes_count=new Long(attributes.size());
	};
	void write(FileOutputStream f) {
		try {
			f.write(toByte(access_flags,2));
			f.write(toByte(name_index,2));
			f.write(toByte(descriptor_index,2));
			attributes_count=new Long(attributes.size());
			f.write(toByte(attributes_count,2));
			Iterator<attribute_info> i=attributes.iterator();
			while (i.hasNext()){
				i.next().write(f);
			}
		}
		catch (IOException s) {}
	};
	void show() {
	};

 	byte[] toByte(Long num, int t) {
		int i;
		Long aux;

		byte[] bytes=new byte[t];
		aux=num;
		i=t-1;
		while (i>=0) { 
			bytes[i]=aux.byteValue(); 
			aux=(Long)aux.rotateRight(aux,8); 
			i--;
		}
		return bytes;
	} 
}





