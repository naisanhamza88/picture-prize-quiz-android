package com.naughtylearning.historyteacher;

import java.util.Scanner;
import java.util.Vector;

//Question class, mostly a struct with string parsing.
public class Question {
	public Question() {}
	public Question(String input_string) {
		Scanner sc = new Scanner(input_string).useDelimiter("::");
		String type = sc.next();
		if (type.equals("MC")) {
			question_string_ = sc.next();
			answer_string_ = sc.next();
			wrong_answers_ = new Vector<String>();
			while (sc.hasNext()) {
				wrong_answers_.add(sc.next());
			}
		}
	}
	public String question_string_;
	public String answer_string_;
	public Vector<String> wrong_answers_;
}
