package src.exercise.palindrome;

/**
 * Palindrome exercise.
 * This class requires to evaluate if a given word is a palindrome or not.
 * Example of palindrome words: "level", "radar", "civic", "madam", "refer".
 */
public class Palindrome {

    private String[] words = {"level", "radar", "card", "civic", "madam", "refer"};

    public static void main(String[] args) {

        Palindrome palindrome = new Palindrome();

        for (String word : palindrome.words) {
            if (palindrome.isPalindrome(word)){
                System.out.println("The word \"" + word + "\" is a palindrome.");
            } else {
                System.out.println("The word \"" + word + "\" is not a palindrome.");
            }
        }

    }

    public boolean isPalindrome(String word){
        var sb = new StringBuilder(word);
        var reversedWord = sb.reverse().toString();
        return  word.equalsIgnoreCase(reversedWord);
    }

}
