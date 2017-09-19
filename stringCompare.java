import java.util.Scanner; 
public class stringCompare {
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter word 1:");
		String comp1 = scan.next();
		System.out.println("Enter word 2:");
		String comp2 = scan.next();
		stringCompare stringComp = new stringCompare(comp1, comp2);
	}
	public stringCompare(String compare1, String compare2){
		int counter = 0;
		for(int i = 0; i < compare1.length(); i++){
			if(compare1.charAt(i) != compare2.charAt(i)){
				counter += 1;
			}
		}
		System.out.println(counter);
	}
}
