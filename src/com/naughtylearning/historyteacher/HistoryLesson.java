package com.naughtylearning.historyteacher;

import java.util.Vector;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HistoryLesson extends Activity {
	
	public static final int CHANGE_LEVEL = Menu.FIRST;
	public static final int MENU_ABOUT = Menu.FIRST + 1;
	
	public static final int CHANGE_LEVEL_DIALOG = 1;
	public static final int ABOUT_DIALOG = 2;
	
	public class AnswerListener implements View.OnClickListener {
		public AnswerListener(int questionNumber, boolean answerIsCorrect) {
			questionNumber_ = questionNumber;
			answerIsCorrect_ = answerIsCorrect;
		}
		
		public void onClick(View v) {
			giveResult(questionNumber_, answerIsCorrect_);
		}
		
		int questionNumber_;
		boolean answerIsCorrect_;
	}
	
	public class LevelChangeListener implements View.OnClickListener {
		public LevelChangeListener(int level) {
			level_ = level;
		}
		
		public void onClick(View v) {
			hideQuestion();
			current_level_ = level_;
			resetToLevel(current_level_, getRandomArrayString(R.array.start_strings));
			dismissDialog(CHANGE_LEVEL_DIALOG);
		}
		
		int level_;
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Set up the image set and question stuff
        current_level_ = 0;
        Global.setImagesAndQuestionsForLevel(current_level_,image_set_,question_set_,getResources());

        orient_ = new OrientationEventListener(this) {
			@Override
			public void onOrientationChanged(int orientation) {
				// TODO Auto-generated method stub
				if (((orientation >= 0) && (orientation < 45)) || ((orientation >= 315) && (orientation < 360))) {
					if (!vertical_) {
						vertical_ = true;
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					}
				} else if ((orientation >= 225) && (orientation < 315)) {
					if (vertical_) {
						vertical_ = false;
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					}
				}
			}
        };
        orient_.enable();
        
        // Start at the first view, in non-question mode
        questionNumber_ = 0;
        questionsRight_ = 0;
        setTeacherImage(0);
        giveResult(0, true);
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if ((event.getAction() == MotionEvent.ACTION_DOWN) && !active_question_) {
			if (question_set_.size() > 0) {
				askQuestion();
				return true;
			} else {
				hideQuestion();
				resetToLevel(current_level_, getRandomArrayString(R.array.start_strings));
				return true;
			}
		}
		return super.onTouchEvent(event);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(1, CHANGE_LEVEL, 0, "Change level");
        menu.add(2, MENU_ABOUT, 1, "About...");
        return result;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case CHANGE_LEVEL:
        	showDialog(CHANGE_LEVEL_DIALOG);
            return true;
        case MENU_ABOUT:
        	showDialog(ABOUT_DIALOG);
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	@Override
    protected Dialog onCreateDialog(int id) {
        if(id == CHANGE_LEVEL_DIALOG) {
        	if (change_level_dialog_ == null) { change_level_dialog_ = new Dialog(this); }
        	change_level_dialog_.requestWindowFeature(Window.FEATURE_NO_TITLE);
            return change_level_dialog_;
        } else if (id == ABOUT_DIALOG) {
        	if (about_dialog_ == null) { about_dialog_ = new Dialog(this); }
        	about_dialog_.requestWindowFeature(Window.FEATURE_NO_TITLE);
        	about_dialog_.setContentView(R.layout.about);
        	return about_dialog_;
        }
        return super.onCreateDialog(id);
    }
	
	@Override
    protected void onPrepareDialog(int id, Dialog d) {
        if(id == CHANGE_LEVEL_DIALOG) {
        	change_level_dialog_.setContentView(R.layout.change_level);
        	LinearLayout l = (LinearLayout)change_level_dialog_.findViewById(R.id.level_change_layout);
        	for (int i = 0; i <= highest_level_; ++i) {
        		Button b = new Button(this);
        		b.setLayoutParams(new LinearLayout.LayoutParams(
        				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        		b.setText("Level " + Integer.toString(i + 1));
        		b.setOnClickListener(new LevelChangeListener(i));
        		l.addView(b);
        	}
        	
        	WindowManager.LayoutParams dialog_params = change_level_dialog_.getWindow().getAttributes();
        	dialog_params.width = WindowManager.LayoutParams.FILL_PARENT;
        	change_level_dialog_.getWindow().setAttributes(dialog_params);
        }
    }
	
	public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
	
	private void askQuestion() {
		// Ask a question by revealing all question asking machinery
		questionNumber_++;
		findViewById(R.id.question_text).setVisibility(View.VISIBLE);
        findViewById(R.id.answer1_button).setVisibility(View.VISIBLE);
        findViewById(R.id.answer2_button).setVisibility(View.VISIBLE);
        findViewById(R.id.answer3_button).setVisibility(View.VISIBLE);
        findViewById(R.id.answer4_button).setVisibility(View.VISIBLE);
        
        ((ImageView)findViewById(R.id.background_image)).setColorFilter(
        		Color.rgb(110,110,110),PorterDuff.Mode.DARKEN);
        
        // Pick a question at random in the question array
        int next_question_index = Global.rand_gen_.nextInt(question_set_.size());
        current_question_ = question_set_.elementAt(next_question_index);
        question_set_.removeElementAt(next_question_index);
        ((Button)findViewById(R.id.question_text)).setText(current_question_.question_string_);
        int answer_position = Global.rand_gen_.nextInt(4);
        
        if (answer_position == 0) {
        	((Button)findViewById(R.id.answer1_button)).setText(current_question_.answer_string_);
        } else {
        	int wrong_answer_position = Global.rand_gen_.nextInt(current_question_.wrong_answers_.size());
        	((Button)findViewById(R.id.answer1_button)).setText(current_question_.wrong_answers_.elementAt(wrong_answer_position));
        	current_question_.wrong_answers_.removeElementAt(wrong_answer_position);
        }
        ((Button)findViewById(R.id.answer1_button)).setOnClickListener(new AnswerListener(questionNumber_,answer_position == 0));
        
        if (answer_position == 1) {
        	((Button)findViewById(R.id.answer2_button)).setText(current_question_.answer_string_);
        } else {
        	int wrong_answer_position = Global.rand_gen_.nextInt(current_question_.wrong_answers_.size());
        	((Button)findViewById(R.id.answer2_button)).setText(current_question_.wrong_answers_.elementAt(wrong_answer_position));
        	current_question_.wrong_answers_.removeElementAt(wrong_answer_position);
        }
        ((Button)findViewById(R.id.answer2_button)).setOnClickListener(new AnswerListener(questionNumber_,answer_position == 1));
        
        if (answer_position == 2) {
        	((Button)findViewById(R.id.answer3_button)).setText(current_question_.answer_string_);
        } else {
        	int wrong_answer_position = Global.rand_gen_.nextInt(current_question_.wrong_answers_.size());
        	((Button)findViewById(R.id.answer3_button)).setText(current_question_.wrong_answers_.elementAt(wrong_answer_position));
        	current_question_.wrong_answers_.removeElementAt(wrong_answer_position);
        }
        ((Button)findViewById(R.id.answer3_button)).setOnClickListener(new AnswerListener(questionNumber_,answer_position == 2));
        
        if (answer_position == 3) {
        	((Button)findViewById(R.id.answer4_button)).setText(current_question_.answer_string_);
        } else {
        	int wrong_answer_position = Global.rand_gen_.nextInt(current_question_.wrong_answers_.size());
        	((Button)findViewById(R.id.answer4_button)).setText(current_question_.wrong_answers_.elementAt(wrong_answer_position));
        	current_question_.wrong_answers_.removeElementAt(wrong_answer_position);
        }
        ((Button)findViewById(R.id.answer4_button)).setOnClickListener(new AnswerListener(questionNumber_,answer_position == 3));
        
        // Hide the result machinery
        findViewById(R.id.result_message).setVisibility(View.GONE);
        
        // There is now a question active
        active_question_ = true;
	}
	
	private String getRandomArrayString(int array_id) {
		return getResources().getStringArray(array_id)[Global.rand_gen_.nextInt(
						getResources().getStringArray(array_id).length)];
	}
	
	private void giveResult(int number, boolean isCorrect) {
		hideQuestion();
		if (number == 0) {
			resetToLevel(current_level_, getRandomArrayString(R.array.start_strings));
		} else if (question_set_.isEmpty()) {
			double score = (double)questionsRight_/(double)questionNumber_;
			if (score < 0.7) {
				resetToLevel(current_level_, getRandomArrayString(R.array.failed_quiz_strings).replace(
						"%score%", String.format("%2.0f%%",score*100)));
			} else {
				String next_level_msg = "";
				if (current_level_ < Global.MAX_LEVEL) {
					current_level_++;
					if (current_level_ > highest_level_) {
						highest_level_ = current_level_;
					}
					next_level_msg = getRandomArrayString(R.array.success_quiz_strings).replace(
							"%score%", String.format("%2.0f%%", score*100));
				} else {
					next_level_msg = getRandomArrayString(R.array.success_mastered_strings).replace(
							"%score%", String.format("%2.0f%%", score*100));
				}
				((Button)findViewById(R.id.result_message)).setText(next_level_msg);
				((Button)findViewById(R.id.level_message)).setText("Level " + Integer.toString(current_level_ + 1));
				setTeacherImage(10);
				//resetToLevel(current_level_, next_level_msg);
			}
		} else {
			if (isCorrect) {
				questionsRight_++;
				((Button)findViewById(R.id.result_message)).setText(
						getRandomArrayString(R.array.success_question_strings));
				
				setTeacherImage(questionsRight_);
			} else {
		        ((Button)findViewById(R.id.result_message)).setText(
		        		"Oooo, so close, it was actually " + current_question_.answer_string_ + "! Don't get discouraged though!");
			}
		}
	}
	
	private void setTeacherImage(int number) {
		((ImageView)findViewById(R.id.background_image)).setImageResource(image_set_.elementAt(number));
	}
	
	private void hideQuestion() {
		findViewById(R.id.question_text).setVisibility(View.GONE);
        findViewById(R.id.answer1_button).setVisibility(View.GONE);
        findViewById(R.id.answer2_button).setVisibility(View.GONE);
        findViewById(R.id.answer3_button).setVisibility(View.GONE);
        findViewById(R.id.answer4_button).setVisibility(View.GONE);
        
        ((ImageView)findViewById(R.id.background_image)).clearColorFilter();
        
        active_question_ = false;
        
		findViewById(R.id.result_message).setVisibility(View.VISIBLE);
	}
	
	private void resetToLevel(int level_number, String result_string) {
		questionNumber_ = 0;
		questionsRight_ = 0;
		Global.setImagesAndQuestionsForLevel(level_number,image_set_,question_set_,getResources());
		setTeacherImage(0);
		((Button)findViewById(R.id.result_message)).setText(result_string);
		((Button)findViewById(R.id.level_message)).setText("Level " + Integer.toString(level_number + 1));
	}
	
	
	boolean active_question_;
	Question current_question_;
	int questionNumber_;
	int questionsRight_;
	int current_level_;
	int highest_level_;
    Vector<Integer> image_set_ = new Vector<Integer>();
	Vector<Question> question_set_ = new Vector<Question>();
	
	Dialog change_level_dialog_;
	Dialog about_dialog_;
	
	boolean vertical_ = true;
	OrientationEventListener orient_;
}
