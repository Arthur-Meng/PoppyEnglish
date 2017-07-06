package com.poppyenglish;



public class Game {
	private final static String str="111h11111111011111111m11111"
			+ "111b11111111u11111111r11111"
			+ "111g11111111e11111111r11111";

	private final static String da="111h11111111a11111111m11111"
			+ "111b11111111u11111111r11111"
			+ "111g11111111e11111111r11111";
	
	private final static String str1="111h1111111b000001111m11111"
			+ "111b11111111u11111111r11111"
			+ "111g11111111e11111111r11111";

	private final static String da1="111h1111111banana1111m11111"
			+ "111b11111111u11111111r11111"
			+ "111g11111111e11111111r11111";
	
	private final static String str2="111h1111111b000001111m11111"
			+ "111b11111100u01111111r11111"
			+ "111g11111111e11111111r11111";

	private final static String da2="111h1111111banana1111m11111"
			+ "111b111111club1111111r11111"
			+ "111g11111111e11111111r11111";
	
	private final static String str3="111h1111111b000001110m11111"
			+ "110b11111100u01111110r11111"
			+ "110g1111111ne11111111r11111";

	private final static String da3="111h1111111banana111om11111"
			+ "11ob111111club111111er11111"
			+ "11ag1111111ne11111111r11111";
	private final static String str4="111h1111111b000001110m11111"
			+ "110b11111100u01111110r11111"
			+ "110g1111111ne00111111r11111";

	private final static String da4="111h1111111banana111om11111"
			+ "11ob111111club111111er11111"
			+ "11ag1111111need111111r11111";
	private final static String str5="111h1111111b000001110m11111"
			+ "110b11111100ub1111110r01111"
			+ "110g0111111ne00111111r11111";
	private final static String da5="111h1111111banana111om11111"
			+ "11ob111111club111111erl1111"
			+ "11agu111111need111111r11111";
	private final static String str6="111h1111111b000001110m11111"
			+ "110b11111100ub1111110r01111"
			+ "110g000h111ne00111111r11111";

	private final static String da6="111h1111111banana111om11111"
			+ "11ob111111club111111erl1111"
			+ "11aguish111need111111r11111";
	private final static String str7="111h1111111b000001110m11111"
			+ "b00b01111100ub1111110r01111"
			+ "110g000h111ne00111111r11111";

	private final static String da7="111h1111111banana111om11111"
			+ "booby11111club111111erl1111"
			+ "11aguish111need111111r11111";
	
	private static char englishku[]=new char[9*9];
	private static char wj[]=new char[9*9];
	private static char b[]=new char[9*9];
	

	
	public int gq=0;
	public int question;
	char a;
	

	public Game(){
		englishku = fromPuzzleString(str);
		wj = fromPuzzleString(str);
		b=fromPuzzleString(da);
	

	}

	private char getTile(int x,int y){
		return  englishku[y*9+x];
	}

	public static char[] fromPuzzleString(String src) {
		char []  english=new char[src.length()];
		for(int i=0;i<src.length();i++){
			english[i]=src.charAt(i);
		}
		return english;
		
	}
	
	
	public  int hua(int x,int y){
		if(englishku[(y*9+x)]=='0'){
			return 1;
		}else return 0;
	}
	
	
	public  int huahei(int x,int y){
		if(englishku[(y*9+x)]=='1'){
			return 1;
		}else return 0;
	}
	protected boolean shx(int x,int y,int tile){
		
		if(hua(x,y)==1){
		setTile(x,y,tile);
		return true;
		}else return false;
	}
	
	
private void setTile(int x, int y, int tile) {
	int m=0,i;
	
	for( i=0;(i<=tile)&&(tile!=26);i++){
	if(i==tile){
		wj[9*y+x]=(char) ('a'+m);
	}
	else m++;
	}
if(tile==26){
	wj[9*y+x]=(char)'0';	
	}
	}

	public String wjs(int x,int y){
		char v=wj[y*9+x];
		if(v=='0'||v=='1'){
			return "";
		}else 
			return String.valueOf(v);
	}
	

	
	public boolean pd(){
		int i=0,m=0;
		while( i<81){
			if(wj[i]==b[i]){
			   i++;
			   m=i;
			}else break;
		}
		
		if(m==81){
			return true;
		}else return false;
	
	}
	final public  int look(int x){
		question=x;
		return question;
	}
	final public int change(){
		gq++;
		return gq;
	}
	
	
	
	public void ccc(int x){
		if(x==1){
			englishku = fromPuzzleString(str);
			wj = fromPuzzleString(str);
			b=fromPuzzleString(da);
		}
		if(x==2){
			englishku = fromPuzzleString(str1);
			wj = fromPuzzleString(str1);
			b=fromPuzzleString(da1);
		}
		if(x==3){
			englishku = fromPuzzleString(str2);
			wj = fromPuzzleString(str2);
			b=fromPuzzleString(da2);
		}
		if(x==4){
			englishku = fromPuzzleString(str3);
			wj = fromPuzzleString(str3);
			b=fromPuzzleString(da3);
		}
		if(x==5){
			englishku = fromPuzzleString(str4);
			wj = fromPuzzleString(str4);
			b=fromPuzzleString(da4);
		}
		if(x==6){
			englishku = fromPuzzleString(str5);
			wj = fromPuzzleString(str5);
			b=fromPuzzleString(da5);
		}
		if(x==7){
			englishku = fromPuzzleString(str6);
			wj = fromPuzzleString(str6);
			b=fromPuzzleString(da6);
		}
		if(x==8){
			englishku = fromPuzzleString(str7);
			wj = fromPuzzleString(str7);
			b=fromPuzzleString(da7);
		}
	}
}
