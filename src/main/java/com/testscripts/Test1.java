package com.testscripts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Test1 {

@SuppressWarnings({ "resource", "rawtypes" })
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
ArrayList<String> list = new ArrayList<>();

String str;
System.out.println("Enter a String: ");
str = scanner.nextLine();

int len = str.length();
String substr = "";

for (int i = 0; i < len;) {

char current = str.charAt(i);
String tempSubstr = "" + current;

while ((i + 1) < len && (current == str.charAt(i + 1))) {
tempSubstr = tempSubstr + str.charAt(++i);
}

if (tempSubstr.length() >= substr.length()) {
substr = tempSubstr;
list.add(substr);
}
i += 1;
}

Iterator it = list.iterator();
while(it.hasNext()){
	System.out.println(it.hasNext());
}
System.out.println(substr);
System.out.println(substr.length());
}
}