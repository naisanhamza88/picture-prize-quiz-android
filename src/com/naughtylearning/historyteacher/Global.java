package com.naughtylearning.historyteacher;

import java.util.Random;
import java.util.Vector;

import android.content.res.Resources;

public class Global {
	// Random number generator!
    public static Random rand_gen_ = new Random();
    public static final int MAX_LEVEL = 2;
	
	public static void setImagesAndQuestionsForLevel(
			int level_number,
			Vector<Integer> image_set,
			Vector<Question> question_set,
			Resources r) {
		if (level_number == 0) {
	        image_set.clear();
	        image_set.add(new Integer(R.drawable.set1_pic1)); image_set.add(new Integer(R.drawable.set1_pic2));
	        image_set.add(new Integer(R.drawable.set1_pic3)); image_set.add(new Integer(R.drawable.set1_pic4));
	        image_set.add(new Integer(R.drawable.set1_pic5)); image_set.add(new Integer(R.drawable.set1_pic6));
	        image_set.add(new Integer(R.drawable.set1_pic7)); image_set.add(new Integer(R.drawable.set1_pic8));
	        image_set.add(new Integer(R.drawable.set1_pic9)); image_set.add(new Integer(R.drawable.set1_pic10));
	        image_set.add(new Integer(R.drawable.set1_pic11));
	        
	        Vector<Question> tmp_vector = new Vector<Question>();
	        String[] questions = r.getStringArray(R.array.level_1_questions);
	        for (int i = 0; i < questions.length; ++i) {
	        	tmp_vector.add(new Question(questions[i]));
	        }
	        // now choose 10
	        question_set.clear();
	        for (int i = 0; (i < 10) && tmp_vector.size() > 0; ++i) {
	        	int next_question = Global.rand_gen_.nextInt(tmp_vector.size());
	        	question_set.add(tmp_vector.elementAt(next_question));
	        	tmp_vector.removeElementAt(next_question);
	        }
		} else if (level_number == 1) {
	        image_set.clear();
	        image_set.add(new Integer(R.drawable.set2_pic1)); image_set.add(new Integer(R.drawable.set2_pic2));
	        image_set.add(new Integer(R.drawable.set2_pic3)); image_set.add(new Integer(R.drawable.set2_pic4));
	        image_set.add(new Integer(R.drawable.set2_pic5)); image_set.add(new Integer(R.drawable.set2_pic6));
	        image_set.add(new Integer(R.drawable.set2_pic7)); image_set.add(new Integer(R.drawable.set2_pic8));
	        image_set.add(new Integer(R.drawable.set2_pic9)); image_set.add(new Integer(R.drawable.set2_pic10));
	        image_set.add(new Integer(R.drawable.set2_pic11));
	        
	        Vector<Question> tmp_vector = new Vector<Question>();
	        String[] questions = r.getStringArray(R.array.level_2_questions);
	        for (int i = 0; i < questions.length; ++i) {
	        	tmp_vector.add(new Question(questions[i]));
	        }
	        // now choose 10
	        question_set.clear();
	        for (int i = 0; (i < 10) && tmp_vector.size() > 0; ++i) {
	        	int next_question = Global.rand_gen_.nextInt(tmp_vector.size());
	        	question_set.add(tmp_vector.elementAt(next_question));
	        	tmp_vector.removeElementAt(next_question);
	        }
		} else {
	        image_set.clear();
	        image_set.add(new Integer(R.drawable.set3_pic1)); image_set.add(new Integer(R.drawable.set3_pic2));
	        image_set.add(new Integer(R.drawable.set3_pic3)); image_set.add(new Integer(R.drawable.set3_pic4));
	        image_set.add(new Integer(R.drawable.set3_pic5)); image_set.add(new Integer(R.drawable.set3_pic6));
	        image_set.add(new Integer(R.drawable.set3_pic7)); image_set.add(new Integer(R.drawable.set3_pic8));
	        image_set.add(new Integer(R.drawable.set3_pic9)); image_set.add(new Integer(R.drawable.set3_pic10));
	        image_set.add(new Integer(R.drawable.set3_pic11));
	        
	        Vector<Question> tmp_vector = new Vector<Question>();
	        String[] questions = r.getStringArray(R.array.level_3_questions);
	        for (int i = 0; i < questions.length; ++i) {
	        	tmp_vector.add(new Question(questions[i]));
	        }
	        // now choose 10
	        question_set.clear();
	        for (int i = 0; (i < 10) && tmp_vector.size() > 0; ++i) {
	        	int next_question = Global.rand_gen_.nextInt(tmp_vector.size());
	        	question_set.add(tmp_vector.elementAt(next_question));
	        	tmp_vector.removeElementAt(next_question);
	        }
		}
	}
}
