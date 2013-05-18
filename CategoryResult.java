/**
 * CategoryResult keeps track of the category selection and whether the
 * dice fulfill the requirements for that category. 
 */

public class CategoryResult {
	
	public CategoryResult() {
		
	}
	
	public void setCategory(int n) {
		category = n;
	}
	
	public void setValid(boolean b) {
		validity = b;
	}
	
	public int getCategory() {
		return category;
	}
	
	public boolean isValid() {
		return validity;
	}
	
/* Private instance variables */
	private int category;
	private boolean validity;
	
}
