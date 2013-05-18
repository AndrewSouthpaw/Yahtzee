/**
 * CategoryResult keeps track of the category selection and whether the
 * dice fulfill the requirements for that category. 
 */

public class CategoryResult {
	
/** Creates a CategoryResult */
	public CategoryResult() {
		
	}
	
/**
 * Sets the category.
 * @param n The category number
 */
	public void setCategory(int n) {
		category = n;
	}
	
/**
 * Sets whether the category is valid.
 * @param b Whether category's validity
 */
	public void setValid(boolean b) {
		validity = b;
	}
	
/**
 * Returns the category.
 * @return The category number
 */
	public int getCategory() {
		return category;
	}
	
/**
 * Returns the validity of the choice.
 * @return Whether the dice are valid for the category
 */
	public boolean isValid() {
		return validity;
	}
	
/* Private instance variables */
	private int category;			/** The category number */
	private boolean validity;		/** Whether the dice are valid for the category */
	
}
