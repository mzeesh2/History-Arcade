public class Trivia {
    private String question;
    private String answer;
    
    public Trivia(String q, String a) {
    	question = q;
    	answer = a;
    }
    
	/**
	 * @return the trivia question
	 */
    public String getQuestion() {
    	return question;
    }
    
    /**
	 * @return the trivia answer
	 */
    public String getAnswer() {
    	return answer;
    }

	/**
	 * @param a an answer given by user
	 * @return whether or not the supplied answer is correct
	 */
    public boolean checkAnswer(String a) {
    	return answer.equalsIgnoreCase(a);
    }
}
