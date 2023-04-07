import java.io.File;
import java.io.IOException;
import java.util.*;

public class App {
    private List<Character> alphabet = new ArrayList<Character>();
    private String word;
    private Hashtable<String, List<String>> actions = new Hashtable<String, List<String>>();
    private Hashtable<String, List<String>> dependant = new Hashtable<String, List<String>>();
    private Hashtable<String, List<String>> independant = new Hashtable<String, List<String>>();
    private ArrayList<ArrayList<String>>  FNF;

    public void run() throws IOException {
        String file1 = "src\\main\\java\\assets\\data1.txt";
        String file2 = "src\\main\\java\\assets\\data2.txt";
        this.readData(file1);

        System.out.println(this.alphabet);
        System.out.println(word);
        System.out.println(this.actions);

        this.dependancy();
        this.printList(this.dependant, "D");
        this.independancy();
        this.printList(this.independant, "I");
        this.createFNF();
        this.printFNF();
    }

    public void readData(String file) throws IOException {

        File tmp_file = new File(file);
        String path = tmp_file.getCanonicalPath();
        System.out.println(path);
        File res = new File(path);


        Scanner scanner = new Scanner(res);
        while(scanner.hasNextLine()){
            //process each line
            String line = scanner.nextLine();
            if(line.charAt(0) == 'A'){
                for(int i = 0; i < line.length(); i++){
                    if(line.charAt(i) == '{' || line.charAt(i) == ','){
                        i++;
                        this.alphabet.add(line.charAt(i));
                    }
                }
            }
            else if(line.charAt(0) == 'w'){
                this.word = line.substring(4, line.length());
            }
            else{
                String key = String.valueOf(line.charAt(1));
                String[] splitted = line.substring(4, line.length()).split(" ");
                List<String> res_dict = new ArrayList<String>();
                int j = 0;
                for(int i = 0; i < splitted.length; i++){
                    if(Objects.equals(splitted[i], "-")){
                        res_dict.add("-"+splitted[i+1]);
                        i++;
                    }
                    else if(!Objects.equals(splitted[i], "+") && !Objects.equals(splitted[i], "=")){
                        res_dict.add(splitted[i]);
                    }
                }
                this.actions.put(key, res_dict);
            }
        }
        scanner.close();
        this.initializeFNF();
    }

    public void initializeFNF(){
        FNF = new ArrayList<>(word.length());
        for(int i=0 ;i < word.length(); i++){
            FNF.add(new ArrayList<String>());
        }
    }

    public void dependancy(){
        for(int i=0 ; i < actions.size(); i++){
            List<String> res_list = new ArrayList<String>();
            for(int j = 0; j < actions.size(); j++){
                if(i != j){
                    if(!dependencyCheck(j, res_list, i, this.alphabet.get(j)));
                    dependencyCheck(i, res_list, j, this.alphabet.get(j));
                }
            }
            res_list.add(String.valueOf(this.alphabet.get(i)));
            dependant.put(String.valueOf(this.alphabet.get(i)), res_list);
        }
    }

    private boolean dependencyCheck(int i, List<String> res_list, int j, Character character) {
        for(int k = 1; k < actions.get(String.valueOf(this.alphabet.get(i))).size(); k++){
            if(actions.get(String.valueOf(this.alphabet.get(j))).get(0).contains(actions.get(String.valueOf(this.alphabet.get(i))).get(k))){
                res_list.add(String.valueOf(character));
                return true;
            }
        }
        return false;
    }

    public void printList(Hashtable<String, List<String>> tmp, String string){
        System.out.print(string+" = {");
        for(Character cha: alphabet){
            int i = 0;
            for(String letter: tmp.get(String.valueOf(cha))){
                System.out.print("("+cha+","+letter+")");
                if(i != tmp.get(String.valueOf(cha)).size()-1 || cha != alphabet.get(alphabet.size()-1)){
                    System.out.print(",");
                }
                i++;
            }
        }
        System.out.println("}");
    }

    public void independancy(){
        List<String> tmp = new ArrayList<String>();
        for(int i = 0 ;i < alphabet.size();i++) {
            tmp.add(String.valueOf(alphabet.get(i)));
        }

        for(int i = 0 ;i < alphabet.size();i++){
            independant.put(String.valueOf(alphabet.get(i)), new ArrayList<String>(tmp));
        }
        for(int i = 0 ;i < alphabet.size();i++) {
            for (String letter : dependant.get(String.valueOf(alphabet.get(i)))){
                independant.get(String.valueOf(alphabet.get(i))).remove(letter);
            }
        }
    }

    public void createFNF(){
        int i = 0;
        for(int j = 0; j < word.length(); j++){
            if(FNF.get(i).size() == 0 && j == 0){
                FNF.get(i).add(String.valueOf(word.charAt(j)));
            }
            else{
                int flag = 0;
                int curr = i;
                while(curr >= 0){
                    if(checkIfDependant(curr, j)){
                        FNF.get(curr+1).add(String.valueOf(word.charAt(j)));
                        flag = 1;
                        break;
                    }
                    curr--;
                }
                if(flag == 0){
                    FNF.get(0).add(String.valueOf(word.charAt(j)));
                }
                else{
                    i++;
                }
            }
        }
    }

    public boolean checkIfDependant(int curr, int j){
        for(String letter: FNF.get(curr)){
            if(dependant.get(letter).contains(String.valueOf(word.charAt(j)))){
                return true;
            }
        }
        return false;
    }

    public void printFNF(){
        System.out.print("FNF([w]) = ");
        for(ArrayList<String> clas: FNF){
            if(clas.size() > 0){
                System.out.print("(");
                for(String letter: clas){
                    System.out.print(letter);
                }
                System.out.print(")");
            }
        }
    }
}
