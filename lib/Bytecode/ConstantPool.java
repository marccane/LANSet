import java.io.*;
import java.util.*;


//	Ajuda a la confeccio de codi en Bytecode. Practiques de MEC
//
//
//
//
//	Classes de la Constant Pool
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


class cp_info {

	Long CONSTANT_Class=7L;
	Long CONSTANT_Fieldref=9L;
	Long CONSTANT_Methodref=10L;
	Long CONSTANT_InterfaceMethodref=11L;
	Long CONSTANT_String=8L;
	Long CONSTANT_Integer=3L;
	Long CONSTANT_Float=4L;
	Long CONSTANT_Long=5L;
	Long CONSTANT_Double=6L;
	Long CONSTANT_NameAndType=12L;
	Long CONSTANT_Utf8=1L;



	public Long tag;

	cp_info() {tag=0L;};
	void write(FileOutputStream f) {};
	Boolean equal(String s) {return false;};
	Long isCPType() {return tag;}
	void show(){};

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

class CONSTANT_Class_info extends cp_info  {
	Long name_index;

	CONSTANT_Class_info(Long name_index0) {
		tag=CONSTANT_Class;
		name_index=name_index0;
	}
	void write(FileOutputStream f) {
		try {
		f.write(this.toByte(tag,1));
		f.write(this.toByte(name_index,2));
		}
		catch (IOException s){}
	};
	void show(){
		System.out.print("Classe        : "+name_index);
	};
}

class CONSTANT_Fieldref_info extends cp_info  {
	Long class_index;
	Long name_and_type_index;

	CONSTANT_Fieldref_info(Long class_index0, Long name_and_type_index0) {
		tag=CONSTANT_Fieldref;
		class_index=class_index0;
		name_and_type_index=name_and_type_index0;
	}
	void write(FileOutputStream f) {
		try {
		f.write(this.toByte(tag,1));
		f.write(this.toByte(class_index,2));
		f.write(this.toByte(name_and_type_index,2));
		}
		catch (IOException s){}
	};
	void show(){
		System.out.print("Ref. Field    : "+class_index+"."+name_and_type_index);
	};

}

class CONSTANT_Methodref_info extends cp_info {
	Long class_index;
	Long name_and_type_index;

	CONSTANT_Methodref_info(Long class_index0, Long name_and_type_index0) {
		tag=CONSTANT_Methodref;
		class_index=class_index0;
		name_and_type_index=name_and_type_index0;
	}
	void write(FileOutputStream f) {
		try {
		f.write(this.toByte(tag,1));
		f.write(this.toByte(class_index,2));
		f.write(this.toByte(name_and_type_index,2));
		}
		catch (IOException s){}
	};
	void show(){
		System.out.print("Metode Inf.   : "+class_index+"."+name_and_type_index);
	};
}

class CONSTANT_InterfaceMethodref_info extends cp_info {
	Long class_index;
	Long name_and_type_index;

	CONSTANT_InterfaceMethodref_info(Long class_index0, Long name_and_type_index0) {
		tag=CONSTANT_InterfaceMethodref;
		class_index=class_index0;
		name_and_type_index=name_and_type_index0;
	}
	void write(FileOutputStream f) {
		try {
		f.write(this.toByte(tag,1));
		f.write(this.toByte(class_index,2));
		f.write(this.toByte(name_and_type_index,2));
		}
		catch (IOException s){}
	};
	void show(){
		System.out.print("Int Metode Inf: "+class_index+"."+name_and_type_index);
	};
}

class CONSTANT_NameAndType_info extends cp_info {
	Long name_index;
	Long descriptor_index;

	CONSTANT_NameAndType_info(Long name_index0, Long descriptor_index0) {
		tag=CONSTANT_NameAndType;
		name_index=name_index0;
		descriptor_index=descriptor_index0;
	}
	void write(FileOutputStream f) {
		try {
		f.write(this.toByte(tag,1));
		f.write(this.toByte(name_index,2));
		f.write(this.toByte(descriptor_index,2));
		}
		catch (IOException s){}
	};
	void show(){
		System.out.print("Name and Type : "+name_index+"."+descriptor_index);
	};
}

class CONSTANT_String_info extends cp_info {
	Long string_index;

	CONSTANT_String_info(Long string_index0) {
		tag=CONSTANT_String;
		string_index=string_index0;
	}
	void write(FileOutputStream f) {
		try {
		f.write(this.toByte(tag,1));
		f.write(this.toByte(string_index,2));
		}
		catch (IOException s){}
	};
	void show(){
		System.out.print("String        : "+string_index);
	};
}

class CONSTANT_Integer_info extends cp_info {
	Long bytes;

	CONSTANT_Integer_info(Long bytes0) {
		tag=CONSTANT_Integer;
		bytes=bytes0;
	}
	void write(FileOutputStream f) {
		try {
		f.write(this.toByte(tag,1));
		f.write(this.toByte(bytes,4));
		}
		catch (IOException s){}
	};
	void show(){
		System.out.print("Integer       : "+bytes);
	};
}


class CONSTANT_Float_info extends cp_info {
	Float bytes;

	CONSTANT_Float_info(Float bytes0) {
		tag=CONSTANT_Float;
		bytes=bytes0;
	}
	void write(FileOutputStream f) {
		try {
		f.write(this.toByte(tag,1));
		Long i=new Long(bytes.floatToIntBits(bytes));
		f.write(this.toByte(i,4));
		}
		catch (IOException s){}
	};
	void show(){
		System.out.print("Float         : "+bytes);
	};
}

class CONSTANT_Utf8_info extends cp_info {
	Long length;
	String bytes;
	
	

	CONSTANT_Utf8_info(String s) {
		tag=CONSTANT_Utf8;
		bytes=new String(s);
	}
	Boolean equal(String s) {
		return (bytes.equals(s));
	};
	void write(FileOutputStream f) {
		int t,i;

		try {
		f.write(this.toByte(tag,1));
		t=bytes.length();
		length=new Long(t);
		f.write(this.toByte(length,2));
		i=0;
		while (i<t) {
			f.write(this.toByte(new Long(bytes.codePointAt(i)),1));
			i++;
			};
		}
		catch (IOException s){}
	};
	void show(){
		System.out.print("Ascii         : "+bytes);
	};
}






